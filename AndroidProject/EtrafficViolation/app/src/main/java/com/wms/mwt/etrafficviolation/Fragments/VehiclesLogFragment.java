package com.wms.mwt.etrafficviolation.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.Activities.MainActivity;
import com.wms.mwt.etrafficviolation.Activities.VehicleInfoActivity;
import com.wms.mwt.etrafficviolation.Application.DataHandler;
import com.wms.mwt.etrafficviolation.ListViews.ListViewAdapter;
import com.wms.mwt.etrafficviolation.ListViews.VehiclesAdapter;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Connections.HttpRequests;

/**
 * Created by Matrix on 11/28/2018.
 */

public class VehiclesLogFragment extends MainFragment {

    List<Vehicle> vehicles;

    VehiclesAdapter adapter;

    public static final int VEHICLES_CASE=3;

    public VehiclesLogFragment() {
        // Required empty public constructor
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!dataHandler.isVehiclesEmpty() ){
            viewVehicles(dataHandler.getVehicles());
        }
        else
        {
            getItems();
        }

      //  MainActivity.customizeFab(VEHICLES_CASE);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        dataHandler.setVehicles(vehicles);
        super.onStop();
    }

    @Override
    public void getItems() {
        String url= HttpRequests.getAllvehicles();

        new GetItems().execute(url);
    }

    @Override
    public void viewFab() {

    }

    @Override
    public void extractAndViewItems(JSONObject jsonObject) {
        //Extract data

        try {
            //check error
            boolean error = jsonObject.getBoolean("error");
            if (!error) {

                vehicles = new ArrayList<>();

                JSONArray productsArray = jsonObject.getJSONArray(Vehicle.VEHICLES);
                for (int i = 0; i < productsArray.length(); i++) {

                    JSONObject productObject = productsArray.getJSONObject(i);

                    String driver = productObject.getString(Vehicle.DRIVER);

                    int plugedNumber = productObject.getInt(Vehicle.PLUGED_NUMBER);

                    Vehicle vehicle=new Vehicle(plugedNumber,driver);

                    vehicles.add(vehicle);
                }

                viewVehicles(vehicles);
                dataHandler.setVehicles(vehicles);
              //  dataHandler.setVehicles(vehicles);

            } else {
                viewError("you have no vehicles");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void viewVehicles(List<Vehicle> vehicles){
        if(adapter!=null){
            adapter.setlist(vehicles);
            adapter.notifyDataSetChanged();
        }
        adapter=new VehiclesAdapter(getContext(),vehicles);
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
        Intent i=new Intent(getContext(), VehicleInfoActivity.class);
        i.putExtra(Vehicle.PLUGED_NUMBER,id);

        startActivity(i);
    }
}
