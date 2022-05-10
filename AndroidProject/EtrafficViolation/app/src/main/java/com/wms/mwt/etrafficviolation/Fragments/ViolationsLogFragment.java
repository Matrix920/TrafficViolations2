package com.wms.mwt.etrafficviolation.Fragments;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.wms.mwt.etrafficviolation.Activities.MainActivity;
import com.wms.mwt.etrafficviolation.Activities.ViolationLogInfoActivity;
import com.wms.mwt.etrafficviolation.ListViews.VehiclesAdapter;
import com.wms.mwt.etrafficviolation.ListViews.ViolationsLogAdapter;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;
import com.wms.mwt.etrafficviolation.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Connections.HttpRequests;

/**
 * Created by Matrix on 11/28/2018.
 */

public class ViolationsLogFragment extends MainFragment {

    List<ViolationLog> violationsLog;

    ViolationsLogAdapter adapter;

    public static final int VIOLATIONS_LOG_CASE=2;

    public ViolationsLogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!dataHandler.isViolationsLogEmpty()){
            viewViolationsLog(dataHandler.getViolationsLog());
        }
        else
        {
            getItems();
        }
        //MainActivity.customizeFab(VIOLATIONS_LOG_CASE);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        dataHandler.setViolationsLog(violationsLog);
        super.onStop();
    }

    @Override
    public void getItems() {
        String url= HttpRequests.getAllViolationsLog();

        new GetItems().execute(url);
    }

    @Override
    public void extractAndViewItems(JSONObject jsonObject) {
//Extract data
        try {
            //check error
            boolean error = jsonObject.getBoolean("error");
            if (!error) {

                violationsLog= new ArrayList<>();

                JSONArray violationsArray = jsonObject.getJSONArray(ViolationLog.VIOLATIONS);
                for (int i = 0; i < violationsArray.length(); i++) {

                    JSONObject violationLogObject = violationsArray.getJSONObject(i);

                    String driver = violationLogObject.getString(ViolationLog.DRIVER);

                    int plugedNumber = violationLogObject.getInt(ViolationLog.PLUGED_NUMBER);

                    int violationLogID = violationLogObject.getInt(ViolationLog.VIOLATION_LOG_ID);

                    int violationID=violationLogObject.getInt(Violation.VIOLATION_ID);

                    double tax = violationLogObject.getDouble(ViolationLog.TAX);

                    String strDate = violationLogObject.getString(ViolationLog.DATE);
                    String date=Utils.importDate(strDate);

                    int isPaid = violationLogObject.getInt(ViolationLog.IS_PAID);

                    String violationType = violationLogObject.getString(ViolationLog.VIOLATION_TYPE);

                    String location = violationLogObject.getString(ViolationLog.LOCATION);

                    ViolationLog violationLog=new ViolationLog(violationID,date,location,isPaid,violationType,driver,plugedNumber,violationLogID,tax);

                    violationsLog.add(violationLog);
                }

                dataHandler.setViolationsLog(violationsLog);
                viewViolationsLog(violationsLog);
            } else {
                viewError("you have no violations");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void viewViolationsLog(List<ViolationLog> violationsLog){
        if(adapter!=null){
            adapter.setViolations(violationsLog);
            adapter.notifyDataSetChanged();
        }

        adapter=new ViolationsLogAdapter(getContext(),violationsLog);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id=(int)adapter.getItemId(i);
                startSecondActivity(id);
            }
        });

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
