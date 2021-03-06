package com.cboe.cfix.util;

/**
 * OverlayPolicyMarketDataExpectedOpeningPriceStructListIF.java
 *
 * @author Dmitry Volpyansky
 *
 */

import com.cboe.exceptions.*;
import com.cboe.idl.cmiMarketData.*;
import com.cboe.interfaces.cfix.*;

public interface OverlayPolicyMarketDataExpectedOpeningPriceStructListIF extends OverlayPolicyMarketDataListIF
{
    public void add(CfixMarketDataConsumer cfixMarketDataConsumer, ExpectedOpeningPriceStruct   struct) throws SystemException, CommunicationException, AuthorizationException, DataValidationException, NotFoundException;
    public void add(CfixMarketDataConsumer cfixMarketDataConsumer, ExpectedOpeningPriceStruct[] structs, int offset, int length) throws SystemException, CommunicationException, AuthorizationException, DataValidationException, NotFoundException;
}
