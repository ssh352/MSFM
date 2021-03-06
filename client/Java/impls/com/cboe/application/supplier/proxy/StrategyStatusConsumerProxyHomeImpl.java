package com.cboe.application.supplier.proxy;

import com.cboe.idl.cmiCallback.*;
import com.cboe.interfaces.application.*;
import com.cboe.interfaces.domain.session.BaseSessionManager;
import com.cboe.application.shared.ServicesHelper;
import com.cboe.util.ThreadPool;
import com.cboe.util.channel.ChannelListenerProxy;
import com.cboe.util.channel.ChannelEvent;
import com.cboe.util.channel.ChannelListener;
import com.cboe.util.channel.ChannelAdapter;
import com.cboe.domain.supplier.proxy.BaseConsumerProxyHomeImpl;

/**
 * StrategyStatusConsumerProxyHomeImpl.
 * @author Jimmy Wang
 */
public class StrategyStatusConsumerProxyHomeImpl extends BaseConsumerProxyHomeImpl implements StrategyStatusConsumerProxyHome
{
    /** constructor. **/
    public StrategyStatusConsumerProxyHomeImpl()
    {
        super();
    }

    /**
      * Follows the proscribed method for creating and generating a impl class.
      * Sets the Session Manager parent class and initializes the Order Query.
      * @param theSession com.cboe.application.session.SessionManager
      * @returns ChannelListener
      */
    public ChannelListener create(CMIStrategyStatusConsumer consumer, BaseSessionManager sessionManager)
    {
        StrategyStatusConsumerProxy bo = new StrategyStatusConsumerProxy(consumer, sessionManager);

        //Every bo object must be added to the container BEFORE anything else.
        addToContainer(bo);
        //Every BOObject create MUST have a name...if the object is to be a managed object.
        bo.create(String.valueOf(bo.hashCode()));
        if(getInstrumentationEnablementProperty())
        {
            bo.startMethodInstrumentation(getInstrumentationProperty());
        }
        bo.initConnectionProperty(getConnectionProperty(sessionManager));
        return bo;
    }
}
