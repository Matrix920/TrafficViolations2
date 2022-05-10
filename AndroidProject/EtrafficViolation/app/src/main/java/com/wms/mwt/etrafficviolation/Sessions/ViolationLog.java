package com.wms.mwt.etrafficviolation.Sessions;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Matrix on 11/27/2018.
 */

public class ViolationLog implements Serializable{

    public static final String VIOLATIONS="violations";
    public static final String DATE="Date";
    public static final String LOCATION="Location";
    public static final String IS_PAID="IsPaid";
    public static final String FROM_DATE="FromDate";
    public static final String TO_DATE="ToDate";
    public static final String VIOLATION_TYPE="ViolationType";
    public static final String DRIVER="Driver";
    public static final String PLUGED_NUMBER="PlugedNumber";
    public static final String VIOLATION_LOG_ID="ViolationLogID";
    public static final String TOTAL_TAX="Tax";
    public static final String TAX="Tax";
    public static final String VIOLATIONS_LOG="violationslog";

    //used to move violationLog between violationLogInfo and ViolationLogUpdate
    public static final String VIOLATION_LOG="violationlog";

    public String date;
    public String location;
    public int isPaid;
    public String violationType;
    public String driver;
    public int plugedNumber;
    public int violationLogID;
    public double totalTax;
    public int violationID;
    public double tax;



    public ViolationLog(int violationID,String date, String location, int isPaid, String violationType, String driver, int plugedNumber, int violationLogID, double tax) {
        this.date = date;
        this.location = location;
        this.violationID=violationID;
        this.isPaid = isPaid;
        this.violationType = violationType;
        this.driver = driver;
        this.plugedNumber = plugedNumber;
        this.violationLogID = violationLogID;
        this.tax = tax;
    }

    public ViolationLog(String date, String location, String violationType, int violationLogID, double tax,int isPaid) {
        this.date = date;
        this.location = location;
        this.violationType = violationType;
        this.violationLogID = violationLogID;
        this.tax = tax;
        this.isPaid=isPaid;
    }
}
