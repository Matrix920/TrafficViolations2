package com.wms.mwt.etrafficviolation.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.wms.mwt.etrafficviolation.Activities.MainActivity;
import com.wms.mwt.etrafficviolation.Activities.ViolationInfoActivity;
import com.wms.mwt.etrafficviolation.ListViews.VehiclesAdapter;
import com.wms.mwt.etrafficviolation.ListViews.ViolationsAdapter;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.HttpRequests;

/**
 * Created by Matrix on 11/28/2018.
 */

public class ViolationsFragment extends MainFragment {

    List<Violation> violations;

    ViolationsAdapter adapter;


    public static final int VIOLATIONS_CASE=1;

    public ViolationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!dataHandler.isViolationsEmpty()){
            viewViolations(dataHandler.getViolations());
        }
        else
        {
            getItems();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        dataHandler.setViolations(violations);
        super.onStop();
    }

    @Override
    public void getItems() {
        String url= HttpRequests.getAllViolaions();

        new GetItems().execute(url);
    }

    @Override
    public void extractAndViewItems(JSONObject jsonObject) {

//Extract data
        try {
            //check error
            boolean error = jsonObject.getBoolean("error");
            if (!error) {

                violations = new ArrayList<>();

                JSONArray violationsArray = jsonObject.getJSONArray(Violation.VIOLATIONS);
                for (int i = 0; i < violationsArray.length(); i++) {

                    JSONObject violationObject = violationsArray.getJSONObject(i);

                    int violationId = violationObject.getInt(Violation.VIOLATION_ID);

                    String violationType = violationObject.getString(Violation.VIOLATION_TYPE);

                    double tax=violationObject.getDouble(Violation.TAX);

                    Violation violation=new Violation(violationId,violationType,tax);

                    violations.add(violation);
                }

                viewViolations(violations);
                dataHandler.setViolations(violations);

               ;
            } else {
                viewError("you have no violations types");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void viewViolations(List<Violation> violations){
        if(adapter!=null){
            adapter.setlist(violations);
            adapter.notifyDataSetChanged();
        }
        adapter=new ViolationsAdapter(getContext(),violations);
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
        Intent i=new Intent(getContext(), ViolationInfoActivity.class);
        i.putExtra(Violation.VIOLATION_ID,id);
        startActivity(i);
    }

    @Override
    public void viewFab() {

    }
}
