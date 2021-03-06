package com.cboe.domain.util;
import com.cboe.idl.cmiOrder.*;
import com.cboe.idl.cmiConstants.*;
import com.cboe.idl.quote.QuoteInfoStruct;
import com.cboe.idl.cmiUtil.DateTimeStruct;

/**
 * This is container class for our data struct.
 * @author Connie Feng
 */
public class QuoteKeyFillReportContainer {
    private short statusChange;
    private FilledReportStruct[] data;
    private int[] groups;
    private QuoteInfoStruct quoteInfo;
    private DateTimeStruct dateTimeAtCreation;
    private String eventInitiator;

    /**
      * Sets the internal fields to the passed values
      */
    public QuoteKeyFillReportContainer(QuoteInfoStruct quoteInfo, FilledReportStruct[] data)
    {
        this( new int[0], quoteInfo, StatusUpdateReasons.NEW, data );
    }

    public QuoteKeyFillReportContainer(int[] groups, QuoteInfoStruct quoteInfo, short statusChange, FilledReportStruct[] data) {
        this.groups         = groups;
        this.quoteInfo      = quoteInfo;
        this.statusChange   = statusChange;
		this.data           = data;
        this.dateTimeAtCreation = new DateWrapper().toDateTimeStruct();
    }

    public QuoteKeyFillReportContainer(int[] groups, QuoteInfoStruct quoteInfo, short statusChange, FilledReportStruct[] data, String eventInitiator) {
        this(groups, quoteInfo, statusChange, data);
        this.eventInitiator = eventInitiator;
    }

    public int[] getGroups()
    {
        return groups;
    }

    public QuoteInfoStruct getQuoteInfoStruct()
    {
        return quoteInfo;
    }

    public short getStatusChange()
    {
        return statusChange;
    }

    public FilledReportStruct[] getFilledReportStruct()
    {
        return data;
    }
    public DateTimeStruct getDateTimeAtCreation()
    {
        return dateTimeAtCreation;
    }
    public String getEventInitiator()
    {
        return eventInitiator;
    }
}
