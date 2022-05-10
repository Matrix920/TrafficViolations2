package com.wms.mwt.etrafficviolation.Application;

import android.util.Log;

import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;

import java.util.List;

/**
 * Created by Matrix on 11/28/2018.
 */

public class DataHandler {
    private static DataHandler dataHandler;
    private List<Violation>violations;
    private List<ViolationLog>violationsLog;
    private List<Vehicle>vehicles;

    public static final String TAG=DataHandler.class.getSimpleName();

    private DataHandler(){

    }




    public boolean isVehiclesEmpty(){
        return vehicles==null||vehicles.isEmpty();
    }

    public boolean isViolationsEmpty(){
        return violations==null||violations.isEmpty();
    }

    public boolean isViolationsLogEmpty(){
        return violationsLog==null||violationsLog.isEmpty();
    }
    public static DataHandler getInstance(){
        if(dataHandler==null){
            Log.e(TAG,"initialize");
            dataHandler=new DataHandler();
            return dataHandler;

        }
        Log.e(TAG,"return");
        return dataHandler;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    public void setViolationsLog(List<ViolationLog> violationsLog) {
        this.violationsLog = violationsLog;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Violation> getViolations() {
        return violations;
    }

    public List<ViolationLog> getViolationsLog() {
        return violationsLog;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void clear() {
        vehicles.clear();
        violations.clear();
        violationsLog.clear();
    }
}
