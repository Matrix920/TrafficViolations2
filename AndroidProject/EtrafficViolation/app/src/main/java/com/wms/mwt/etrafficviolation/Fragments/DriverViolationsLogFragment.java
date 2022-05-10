package com.wms.mwt.etrafficviolation.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.Activities.VehicleInfoActivity;
import com.wms.mwt.etrafficviolation.Activities.ViolationLogInfoActivity;
import com.wms.mwt.etrafficviolation.ListViews.DriverViolationLogAdapter;
import com.wms.mwt.etrafficviolation.ListViews.ViolationsLogAdapter;
import com.wms.mwt.etrafficviolation.Sessions.SessionManager;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;
import com.wms.mwt.etrafficviolation.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

/**
 * Created by Matrix on 12/4/2018.
 */

public class DriverViolationsLogFragment extends MainFragment {

    List<ViolationLog> violationsLog;

    public static final int VIOLATIONS_LOG_CASE=2;

    public PayAsync payAsync=new PayAsync();

    DriverViolationLogAdapter adapter;

    public DriverViolationsLogFragment() {
        // Required empty public constructor
    }

    public class PayAsync extends AsyncTask<Integer,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProgressDialog(getContext(),"paying");
        }

        @Override
        protected JSONObject doInBackground(Integer... integers) {
            String url= HttpRequests.pay(integers[0]);

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(Vehicle.PLUGED_NUMBER,""));

            JsonParser jsonParser=new JsonParser();
            JSONObject data=jsonParser.makeHttpRequest(url,"GET",params);

            return data;
        }



        @Override
        protected void onPostExecute(JSONObject data) {
            Utils.dismiss();
            try {
                boolean error=data.getBoolean(HttpRequests.ERROR);
                if(!error){
                    showMessage("violation has been paid");
                    getItems();
                }
            }catch (Exception e){
                showMessage("error:\n"+e.getMessage());
            }
        }
    }

    private void showMessage(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onStart() {
        super.onStart();
        getItems();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void getItems() {
        String url= HttpRequests.getAllViolationsLogForDriver(String.valueOf(SessionManager.getInstance(getContext()).getPlugedNumber()));

        new GetItems().execute(url);
    }

    @Override
    public void extractAndViewItems(JSONObject jsonObject) {
//Extract data
        try {
            //check error
            boolean error = jsonObject.getBoolean("error");
            if (!error) {

                violationsLog = new ArrayList<>();

                JSONArray violationsArray = jsonObject.getJSONArray(ViolationLog.VIOLATIONS);
                for (int i = 0; i < violationsArray.length(); i++) {

                    JSONObject violationLogObject = violationsArray.getJSONObject(i);

                    int violationLogID = violationLogObject.getInt(ViolationLog.VIOLATION_LOG_ID);

                    int isPaid = violationLogObject.getInt(ViolationLog.IS_PAID);

                    double tax = violationLogObject.getDouble(ViolationLog.TAX);

                    String date = Utils.importDate(violationLogObject.getString(ViolationLog.DATE));

                    String violationType = violationLogObject.getString(ViolationLog.VIOLATION_TYPE);

                    String location = violationLogObject.getString(ViolationLog.LOCATION);

                    ViolationLog violationLog=new ViolationLog(date,location,violationType,violationLogID,tax,isPaid);

                    violationsLog.add(violationLog);
                }

                //dataHandler.setViolationsLog(violationsLog);
                viewViolationsLog(violationsLog);
            } else {
                viewError("you have no violations not paid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void viewViolationsLog(List<ViolationLog> violationsLog){

        if(adapter!=null){
            adapter.setlist(violationsLog);
        }else {

            adapter = new DriverViolationLogAdapter(getContext(), violationsLog, this);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int id = (int) adapter.getItemId(i);
                    startSecondActivity(id);
                }
            });
        }
    }

    @Override
    public void startSecondActivity(int id) {
        Intent i=new Intent(getContext(), ViolationLogInfoActivity.class);
        i.putExtra(ViolationLog.VIOLATION_LOG_ID,id);
        startActivity(i);
    }

    @Override
    public void viewFab() {

    }
}