package com.wms.mwt.etrafficviolation.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Filter;
import android.widget.ProgressBar;

import com.wms.mwt.etrafficviolation.Application.DataHandler;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;
import com.wms.mwt.etrafficviolation.Widjets.ObjectAutocomplete;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Matrix on 11/30/2018.
 */

public class Utils {

    static ProgressDialog pd;

    /*
    get date for view in TextViews and EditTexts
     */
    public static String getDate(Date date){
        return date.getDay()+" / "+date.getMonth() +" / "+date.getYear();
    }

    public static String importDate(String strDate){
       final SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        final SimpleDateFormat EEEddMMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String outputDateStr = "";
        outputDateStr = parseDate(strDate, ymdFormat, EEEddMMMyyyy);

        return outputDateStr;
    }

    public static String exportDate(String strDate){
        final SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        final SimpleDateFormat EEEddMMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String outputDateStr = "";
        outputDateStr = parseDate(strDate, EEEddMMMyyyy, ymdFormat);

        return outputDateStr;
    }

    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    public static boolean checkInt(String str){
        try{
            int x=Integer.parseInt(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean checkDate(String strDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyy");
            Date d = sdf.parse(strDate);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean checkIsPaid(String strInt){
        if(strInt.equals("0") || strInt.equals("1") ){
            return true;
        }
        return false;
    }

    public static boolean checkTax(String strDouble){
        try {
            double x = Double.parseDouble(strDouble);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static ObjectAutocomplete[] convertViolationsToObjectAutocomplete(List<Violation> violationList){
        int size=violationList.size();

        ObjectAutocomplete[]violationArray=new ObjectAutocomplete[size];

        for(int i=0;i<size;i++){
            Violation violation=violationList.get(i);

            violationArray[i]=new ObjectAutocomplete(violation.violationType,violation.violationID);
        }

        return violationArray;
    }

    public static String[] convertPlugedNumbersToStringArray(List<Vehicle> vehicleList){
        int size=vehicleList.size();

        String []vehicleArray=new String[size];

        for(int i=0;i<size;i++){
            Vehicle vehicle=vehicleList.get(i);

            vehicleArray[i]=String.valueOf(vehicle.plugedNumber);
        }

        return vehicleArray;
    }

        public static ObjectAutocomplete[]  performFiltering(ObjectAutocomplete[]originalData,CharSequence constraint) {

            String text=constraint.toString().toLowerCase();

            ArrayList<ObjectAutocomplete> newList=new ArrayList<>();

            for(ObjectAutocomplete objectAutocomplete:originalData){
                if(objectAutocomplete.text.toLowerCase().contains(text)){
                    newList.add(objectAutocomplete);
                }
            }


            return newList.toArray(new ObjectAutocomplete[newList.size()]);

        }

        public static void showProgressDialog(Context context,String text){
            pd=new ProgressDialog(context);
            pd.setTitle("please wait...");
            pd.setMessage(text);
            pd.setCancelable(false);
            pd.show();
        }

        public static void dismiss(){
            pd.dismiss();
        }

        public static ViolationLog getViolationLogByID(int violationId){
            List<ViolationLog>violationLogList= DataHandler.getInstance().getViolationsLog();
            for(ViolationLog violationLog:violationLogList){
                if(violationLog.violationLogID==violationId){
                    return violationLog;
                }
            }
            return null;
        }

       // public static showToast(Context context,String message){

     //   }
    public static Violation getViolationByID(int violationId){
        List<Violation>violationList= DataHandler.getInstance().getViolations();
        for(Violation violation:violationList){
            if(violation.violationID==violationId){
                return violation;
            }
        }
        return null;
    }

    public static String[]  performFilteringInt(String[]originalData,CharSequence constraint) {

        String text=constraint.toString().toLowerCase();

        ArrayList<String> newList=new ArrayList<>();

        for(String plugedNumber:originalData){
            if(plugedNumber.toLowerCase().contains(text)){
                newList.add(plugedNumber);
            }
        }


        return newList.toArray(new String[newList.size()]);

    }



    public static ObjectAutocomplete[] convertPlugedNumbersToObjectAutocomplete(List<Vehicle> vehicleList){
        int size=vehicleList.size();

        ObjectAutocomplete[]vehicleArray=new ObjectAutocomplete[size];

        for(int i=0;i<size;i++){
            Vehicle vehicle=vehicleList.get(i);

            vehicleArray[i]=new ObjectAutocomplete(vehicle.driver,vehicle.plugedNumber);
        }

        return vehicleArray;
    }
}
