package com.wms.mwt.etrafficviolation.Sessions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.wms.mwt.etrafficviolation.Activities.MainActivity;
import com.wms.mwt.etrafficviolation.Activities.*;

/**
 * Created by Matrix on 12/14/2017.
 */

public class SessionManager{
    private Context mContext;
    private SharedPreferences sharedPref;
    public static final String SHARED_PREF_NAME="traffic_violations";
    private static final int PRIVATE_MODE=0;
    public static final String IS_ADMIN="is admin";

    private static final String IS_LOGIN="login";
    SharedPreferences.Editor editor;

    private static SessionManager sessionManager;

    private SessionManager(Context context){
        mContext=context;
        sharedPref=mContext.getSharedPreferences(SHARED_PREF_NAME,PRIVATE_MODE);
        editor=sharedPref.edit();
    }

    public static SessionManager getInstance(Context context){
        if(sessionManager==null){
            sessionManager=new SessionManager(context);
        }
        return sessionManager;
    }

    public void logout(){
        //clear session
        editor.clear();
        editor.commit();

        //go to login activity
        goLogin();
    }

    public int getPlugedNumber(){
        return sharedPref.getInt(Vehicle.PLUGED_NUMBER,0);
    }


    public void login(int plugedNumber,boolean isAdmin){
        editor.putBoolean(IS_LOGIN,true);
        editor.putInt(Vehicle.PLUGED_NUMBER,plugedNumber);
        //if(isAdmin){
            editor.putBoolean(IS_ADMIN,isAdmin);
      //  }
      //  else{
     //       editor.putBoolean(IS_ADMIN,false);
     //   }
        editor.commit();

        //go to home activity
        goHome();
    }

    private void goLogin(){
        Intent i=new Intent(mContext, LoginActivity.class);

        //closing all activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //start new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(i);
    }

    private void goHome(){

        Intent i=new Intent(mContext, MainActivity.class);

        //closing all activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //start new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(i);
    }

    public  boolean isAdmin() {
        boolean isAdmin = sharedPref.getBoolean(IS_ADMIN, false);
        return isAdmin;
    }


    public void checkLogin(){
        boolean isLogin= sharedPref.getBoolean(IS_LOGIN,false);

        if(isLogin){
            goHome();
        }
    }

    public void checkLogout(){
        boolean isLogin= sharedPref.getBoolean(IS_LOGIN,false);

        if(! isLogin){
            goLogin();
        }
    }

}
