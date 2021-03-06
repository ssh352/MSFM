package com.cboe.consumers.eventChannel;

import com.cboe.domain.startup.ClientBOHome;
import com.cboe.domain.util.SessionKeyContainer;
import com.cboe.exceptions.AuthorizationException;
import com.cboe.exceptions.CommunicationException;
import com.cboe.exceptions.DataValidationException;
import com.cboe.exceptions.SystemException;
import com.cboe.infrastructureServices.eventService.EventService;
import com.cboe.infrastructureServices.foundationFramework.utilities.Log;
import com.cboe.interfaces.events.IECRemoteCASCurrentMarketConsumerHome;
import com.cboe.interfaces.events.RemoteCASCurrentMarketConsumer;
import com.cboe.util.ChannelKey;
import com.cboe.util.ChannelType;

public class RemoteCASCurrentMarketConsumerHomeEventImpl extends ClientBOHome implements IECRemoteCASCurrentMarketConsumerHome
{
    private RemoteCASCurrentMarketEventConsumerInterceptor consumerProxy;
    private RemoteCASCurrentMarketEventConsumerImpl event;
    private EventService eventService;
    private EventChannelFilterHelper eventChannelFilterHelper;
    private final String CHANNEL_NAME = "RemoteCASCurrentMarket";

    public RemoteCASCurrentMarketConsumer create()
    {
        return find();
    }

    public RemoteCASCurrentMarketConsumer find()
    {
        return consumerProxy;
    }

    public void clientStart()
        throws Exception
    {
        if (eventService == null)
        {
            eventService = eventChannelFilterHelper.connectEventService();
        }

        String interfaceRepId = com.cboe.idl.events.RemoteCASCurrentMarketEventConsumerHelper.id();

        // connect to the event channel without filter and add the constraint filter later on
        // using addConstraint - Eric J. Fredericks
        eventChannelFilterHelper.connectConsumer( CHANNEL_NAME, interfaceRepId, event );
    }

    public void clientInitialize()
    {
        eventChannelFilterHelper = new EventChannelFilterHelper();
        RemoteCASCurrentMarketConsumerIECImpl consumer = new RemoteCASCurrentMarketConsumerIECImpl();
        consumer.create(String.valueOf(consumer.hashCode()));

        //Every BObject must be added to the container.
        addToContainer(consumer);
        consumerProxy = new RemoteCASCurrentMarketEventConsumerInterceptor(consumer);
        if(getInstrumentationEnablementProperty())
        {
            consumerProxy.startInstrumentation(getInstrumentationProperty());
        }
        event = new RemoteCASCurrentMarketEventConsumerImpl(consumerProxy);
    }

    /**
     * Returns the constraint string based on the channel key
     *
     * @param channelKey the event channel key
     *
     * @author Eric J. Fredericks
     */
    private String getConstraintString(ChannelKey channelKey)
    {
        return getParmName(channelKey);
    }

    /**
     * Returns the constraint parameter string based on the channel key
     *
     * @param channelKey the event channel key
     *
     * @author Eric J. Fredericks
     */
    private String getParmName(ChannelKey channelKey)
    {
        String key = channelKey.key.toString();
        StringBuilder name = new StringBuilder(key.length()+70);
        
        switch(channelKey.channelType)
        {
            case ChannelType.SUBSCRIBE_CURRENT_MARKET_BY_CLASS_V3:
                name.append(key).append(" in $.subscribeCurrentMarketForClassV3.routingParameters.groups");
                return name.toString();
            
            case ChannelType.SUBSCRIBE_CURRENT_MARKET_BY_PRODUCT_V3:
                name.append(key).append(" in $.subscribeCurrentMarketForProductV3.routingParameters.groups");
                return name.toString();

            case ChannelType.UNSUBSCRIBE_CURRENT_MARKET_BY_CLASS_V3:
                name.append(key).append(" in $.unsubscribeCurrentMarketForClassV3.routingParameters.groups");
                return name.toString();

            case ChannelType.UNSUBSCRIBE_CURRENT_MARKET_BY_PRODUCT_V3:
                name.append(key).append(" in $.unsubscribeCurrentMarketForProductV3.routingParameters.groups");
                return name.toString();

            case ChannelType.SUBSCRIBE_CURRENT_MARKET_BY_CLASS_V2:
                name.append(key).append(" in $.subscribeCurrentMarketForClassV2.routingParameters.groups");
                return name.toString();

            case ChannelType.SUBSCRIBE_CURRENT_MARKET_BY_PRODUCT_V2:
                name.append(key).append(" in $.subscribeCurrentMarketForProductV2.routingParameters.groups");
                return name.toString();

            case ChannelType.UNSUBSCRIBE_CURRENT_MARKET_BY_CLASS_V2:
                name.append(key).append(" in $.unsubscribeCurrentMarketForClassV2.routingParameters.groups");
                return name.toString();

            case ChannelType.UNSUBSCRIBE_CURRENT_MARKET_BY_PRODUCT_V2:
                name.append(key).append(" in $.unsubscribeCurrentMarketForProductV2.routingParameters.groups");
                return name.toString();

            case ChannelType.SUBSCRIBE_CURRENT_MARKET_BY_CLASS:
                name.append(key).append(" in $.subscribeCurrentMarketForClass.routingParameters.groups");
                return name.toString();

            case ChannelType.SUBSCRIBE_CURRENT_MARKET_BY_PRODUCT:
                name.append(key).append(" in $.subscribeCurrentMarketForProduct.routingParameters.groups");
                return name.toString();

            case ChannelType.UNSUBSCRIBE_CURRENT_MARKET_BY_CLASS:
                name.append(key).append(" in $.unsubscribeCurrentMarketForClass.routingParameters.groups");
                return name.toString();

            case ChannelType.UNSUBSCRIBE_CURRENT_MARKET_BY_PRODUCT:
                name.append(key).append(" in $.unsubscribeCurrentMarketForProduct.routingParameters.groups");
                return name.toString();

            default:
                Log.alarm(this, "Invalid Channel Type for filtering for context: " + channelKey.channelType);
                return EventChannelFilterHelper.NO_EVENTS_CONSTRAINT;

        }

    }

    /**
     * Adds a  Filter to the internal event channel. Constraints based on the
     * ChannelKey will be added as well. Do not make call to addConstraints when this method has
     * already being called.
     *
     * @param channelKey the event channel key
     *
     * @author Eric J. Fredericks
     */
    public void addFilter ( ChannelKey channelKey )
        throws SystemException, CommunicationException, AuthorizationException, DataValidationException
    {
        SessionKeyContainer sessionGroupKey = (SessionKeyContainer) channelKey.key;
        ChannelKey newKey = new ChannelKey(channelKey.channelType, Integer.valueOf(sessionGroupKey.getKey()));
        addConstraint(newKey);
    }

    /**
     * Adds constraint based on the channel key
     *
     * @param channelKey the event channel key
     *
     * @author Eric J. Fredericks
     */
    private void addConstraint(ChannelKey channelKey)
        throws SystemException
    {
        String constraintString = getConstraintString(channelKey);
        eventChannelFilterHelper.addEventFilter(event, channelKey,
                eventChannelFilterHelper.getChannelName(CHANNEL_NAME), constraintString);
    }

    /**
     * Removes the event channel Filter from the CBOE event channel.
     *
     * @param channelKey the event channel key
     *
     * @author Eric J. Fredericks
     */
    public void removeFilter ( ChannelKey channelKey )
        throws SystemException, CommunicationException, AuthorizationException, DataValidationException
    {
        SessionKeyContainer sessionGroupKey = (SessionKeyContainer) channelKey.key;
        ChannelKey newKey = new ChannelKey(channelKey.channelType, Integer.valueOf(sessionGroupKey.getKey()));
        removeConstraint(newKey);
    }

    /**
     * Removes constraint based on the channel key
     *
     * @param channelKey the event channel key
     *
     * @author Eric J. Fredericks
     */
    private void removeConstraint(ChannelKey channelKey)
        throws SystemException
    {
        String constraintString = getConstraintString(channelKey);
        eventChannelFilterHelper.removeEventFilter( channelKey, constraintString);
    }
}// EOF
