package Connections;

import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;

/**
 * Created by Matrix on 11/13/2018.
 */

public class HttpRequests {
    public static final String API="https://matrix92.000webhostapp.com/v1/";
    public static final String ERROR="error";
    private static String vehicle;

    public static String getAllvehicles(){
        return API+ Vehicle.VEHICLES;
    }

    public static String getAllViolaions(){
        return API+ Violation.VIOLATIONS;
    }

    public static String getAllViolationsLog(){
        return API+ ViolationLog.VIOLATIONS_LOG;
    }

    public static String register() {
        return API+"register";
    }

    public static String getLogin() {
        return API+"login";
    }

    public static String addViolation() {
        return API+"violations/add";
    }

    public static String addViolationLog() {
        return API+"violationslog/add";
    }

    public static String searchByDriver(String driver) {
      return  API+"violationslog/driver/"+driver;
    }

    public static String searchByLocation(String location) {
        return API+"violationslog/location/"+location;
    }

    public static String searchByPlugedNumber(String plugedNumber) {
        return API+"violationslog/plugednumber/"+plugedNumber;
    }

    public static String updateViolation() {
        return  API+"violations/update";
    }

    public static String updateViolationLog() {
        return  API+"violationslog/update";
    }

    public static String deleteViolationLog() {
        return  API+"violationslog/delete";
    }

    public static String crossOutVehicle() {
        return  API+"vehicles/crossout";
    }

    public static String getVehicle(int plugedNumber) {
        return API+"vehiclesinfo/"+plugedNumber;
    }

    public static String searchByDate() {
        return API+"violationslog/date";
    }

    public static String getAllViolationsLogForDriver(String plugedNumber) {
        return API+"violationslog/plugednumberdriver/"+plugedNumber;
    }

    public static String pay(int violationLogID) {
        return API+"/violationlog/pay/"+violationLogID;
    }


}