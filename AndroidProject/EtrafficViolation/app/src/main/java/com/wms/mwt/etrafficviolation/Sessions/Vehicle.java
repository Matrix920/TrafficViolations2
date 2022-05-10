package com.wms.mwt.etrafficviolation.Sessions;

import java.util.Date;

/**
 * Created by Matrix on 11/27/2018.
 */

public class Vehicle {

    public static final String IS_ADMIN="isAdmin";
    public static final String PLUGED_NUMBER="PlugedNumber";
    public static final String DRIVER="Driver";
    public static final String TYPE="Type";
    public static final String CATEGORY="Category";
    public static final String PRODUCTION_DATE="ProductionDate";
    public static final String REGISTERATION_DATE="RegisterationDate";
    public static final String IS_CROSS_OUT="IsCrossOut";
    public static final String VEHICLES="vehicles";

    public boolean isAdmin;
    public int plugedNumber;
    public String driver;
    public String type;
    public String category;
    public String productionDate;
    public String registerationDate;
    public int isCrossOut;

    public Vehicle(boolean isAdmin, int plugedNumber, String driver, String type, String category, String productionDate, String registerationDate, int isCrossOut) {
        this.isAdmin = isAdmin;
        this.plugedNumber = plugedNumber;
        this.driver = driver;
        this.type = type;
        this.category = category;
        this.productionDate = productionDate;
        this.registerationDate = registerationDate;
        this.isCrossOut = isCrossOut;
    }

    public Vehicle(int plugedNumber, String driver) {
        this.plugedNumber = plugedNumber;
        this.driver = driver;
    }
}
