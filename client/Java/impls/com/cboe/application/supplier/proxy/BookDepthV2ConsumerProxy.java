package com.cboe.application.supplier.proxy;

import com.cboe.idl.cmiMarketData.*;
import com.cboe.idl.cmiCallbackV2.CMIOrderBookConsumer;
import com.cboe.idl.cmiConstants.QueueActions;

import com.cboe.infrastructureServices.foundationFramework.utilities.Log;
import com.cboe.interfaces.domain.session.BaseSessionManager;
import com.cboe.interfaces.domain.SupplierProxyMessageTypes;
import com.cboe.util.channel.ChannelEvent;

import com.cboe.application.supplier.*;

/**
 * BookDepthV2ConsumerProxy serves as a SessionManager managed proxy to
 * the BookDepthConsumer object on the presentation side in
 * com.cboe.presentation.consumer.  The BookDepthSupplier on the CAS uses
 * this proxy object to communicate to the GUI callback object.  If a connection
 * to the presentation side consumer fails the <CODE>lostConnection</CODE> method
 * will be called letting the SessionManager this consumer reference is no longer
 * valid.
 *
 * @see com.cboe.consumers.internalPresentation.BookDepthConsumerImpl
 * @see com.cboe.idl.cmiCallbackV2.CMIOrderBookConsumer
 *
 * @author William Wei
 * @version 12/26/2001
 */

public class BookDepthV2ConsumerProxy extends InstrumentedConsumerProxy
{
    /**
     * BookDepthV2ConsumerProxy constructor.
     *
     * @param bookDepthConsumer a reference to the proxied implementation object.
     * @param sessionManager a reference to the SessionManager managing subscriptions for this proxy.
     */
    public BookDepthV2ConsumerProxy(CMIOrderBookConsumer bookDepthConsumer, BaseSessionManager sessionManager, short queuePolicy)
    {
        super(sessionManager, BookDepthV2SupplierFactory.find(sessionManager), bookDepthConsumer, queuePolicy);
        interceptor = new OrderBookV2ConsumerInterceptor(bookDepthConsumer);
    }

    public CMIOrderBookConsumer getOrderBookConsumer()
    {
        return ((OrderBookV2ConsumerInterceptor)interceptor).cmiObject;
    }

    public OrderBookV2ConsumerInterceptor getOrderBookConsumerInterceptor()
    {
        return (OrderBookV2ConsumerInterceptor)interceptor;
    }

    /**
     * This method is called by ChannelThreadCommand.  It takes the passed
     * EventChannelEvent, parses out the relevant data for the proxied object,
     * and calls the proxied objects callback method passing in the appropriate
     * data.
     *
     * @param event the ChannelEvent containing the data to send the listener.
     */
    public void channelUpdate(ChannelEvent event)
    {
        if (Log.isDebugOn())
        {
            Log.debug(this,"calling channelUpdate for " + getSessionManager());
        }
        if (event != null)
        {
            short action = QueueActions.NO_ACTION;
            try
            {
                if (getProxyWrapper().getQueueSize() > this.getNoActionProxyQueueDepthLimit()) // NEED TO PUT THIS IN THE XML
                {
                    action = QueueActions.DISCONNECT_CONSUMER;
                    String us = this.toString();
                    StringBuilder discon = new StringBuilder(us.length()+40);
                    discon.append("Disconnection consumer for : ").append(us)
                          .append(" Q=").append(getProxyWrapper().getQueueSize());
                    Log.information(this, discon.toString());
                }
                ((OrderBookV2ConsumerInterceptor)interceptor).acceptBookDepth((BookDepthStruct[])event.getEventData(),
                        getProxyWrapper().getQueueSize(), action);
            }
            catch (org.omg.CORBA.TIMEOUT toe)
            {
                Log.exception(this, "session:" + getSessionManager(), toe);
            }
            catch (Exception e)
            {
                Log.exception(this, "session:" + getSessionManager(), e);
                lostConnection(event);
            }
            if (action == QueueActions.DISCONNECT_CONSUMER)
            {
                lostConnection(event);
            }
        }
        else
        {
            Log.information(this, "Null event");
        }
    }

    public String getMethodName(ChannelEvent event)
    {
        return "acceptBookDepth";
    }

    public String getMessageType()
    {
        return SupplierProxyMessageTypes.BOOK_DEPTH;
    }

}
