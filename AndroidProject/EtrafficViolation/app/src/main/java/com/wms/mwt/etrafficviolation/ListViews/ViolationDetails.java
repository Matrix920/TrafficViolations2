package com.wms.mwt.etrafficviolation.ListViews;

import java.util.Date;

/**
 * Created by Matrix on 11/13/2018.
 */

public class ViolationDetails {
    public String driver;
    public int plugedNumber;
    public String location;
    public String violationType;
    public int isPaid;
    public Date date;
    public float tax;
    public int violationLogID;

    public ViolationDetails(int violationLogID,String driver, int plugedNumber, String location, String violationType, int isPaid, Date date) {
        this.violationLogID=violationLogID;
        this.driver = driver;
        this.plugedNumber = plugedNumber;
        this.location = location;
        this.violationType = violationType;
        this.isPaid = isPaid;
        this.date = date;
    }

    public static final String DRIVER="Driver";
    public static final String PLUGED_NUMBER="PlugedNumber";
    public static final String DATE="Date";
    public static final String LOCATION="Location";
    public static final String TAX="Tax";
    public static final String VIOLATION_TYPE ="ViolationType";
    public static final String VIOLATIONS="violations";
    public static final String IS_PAID="IsPaid";
    public static final String VIOLATIONS_LOG_ID="ViolationLogID";
}
