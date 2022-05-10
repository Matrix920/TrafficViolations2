package com.wms.mwt.etrafficviolation.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;
import com.wms.mwt.etrafficviolation.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class VehicleInfoActivity extends AppCompatActivity {

    Vehicle vehicle;
    int plugedNumber;
    TextView txtViewPlugedNumber,txtViewCategory,txtViewType,txtViewProductionDate,txtViewRegisterationDate,txtViewIsCrossOut,txtViewDriver;
    Button btnCrossout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);

        Intent intent=getIntent();
        plugedNumber=intent.getIntExtra(Vehicle.PLUGED_NUMBER,0);


        txtViewCategory=findViewById(R.id.txtViewCategory);
        txtViewDriver=findViewById(R.id.txtViewDriver);
        txtViewType=findViewById(R.id.txtViewType);
        txtViewIsCrossOut=findViewById(R.id.txtViewIsCrossOut);
        txtViewProductionDate=findViewById(R.id.txtViewProductionDate);
        txtViewRegisterationDate=findViewById(R.id.txtViewRegisterationDate);
        txtViewPlugedNumber=findViewById(R.id.txtViewPlugedNumber);
        //setupItems();

        btnCrossout=findViewById(R.id.btnCrossout);
        setupBtn();
        new GetVehicleInfoAsync().execute();
    }

    private void setupItems(){
        //get vehicle info

        txtViewRegisterationDate.setText(vehicle.registerationDate.toString());
        txtViewProductionDate.setText(vehicle.productionDate.toString());
        txtViewIsCrossOut.setText(vehicle.isCrossOut);
        txtViewType.setText(vehicle.type);
        txtViewCategory.setText(vehicle.category);
        txtViewPlugedNumber.setText(vehicle.plugedNumber);
    }

    private void setupBtn(){
        btnCrossout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //crossout
                new CrossOutVehicleAsync().execute();
            }
        });
    }

    class GetVehicleInfoAsync extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utils.showProgressDialog(VehicleInfoActivity.this,"loading vehicle information");
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url= HttpRequests.getVehicle(plugedNumber);

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(Violation.VIOLATION_TYPE,""));

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
                    showMessage("vehicle loaded");
                    extractAndViewItems(data);
                }
            }catch (Exception e){
                showMessage("no internet connection");
            }
        }
    }

    class CrossOutVehicleAsync extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProgressDialog(VehicleInfoActivity.this,"crossing out vehicle");
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url= HttpRequests.crossOutVehicle();

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(vehicle.PLUGED_NUMBER,String.valueOf(plugedNumber)));

            JsonParser jsonParser=new JsonParser();
            JSONObject data=jsonParser.makeHttpRequest(url,"POST",params);

            return data;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            Utils.dismiss();
            try {
                boolean error=data.getBoolean(HttpRequests.ERROR);
                if(!error){
                    showMessage("car has been crossed out");
                    finish();
                }
            }catch (Exception e){
                showMessage("error:\n"+e.getMessage());
            }
        }
    }


    public void extractAndViewItems(JSONObject jsonObject) {
//Extract data
        try {

               // double tax=jsonObject.getDouble(Violation.TAX);

                //boolean isAdmin=jsonObject.getInt(Vehicle.IS_ADMIN)==1;

                String driver = jsonObject.getString(Vehicle.DRIVER);

                int plugedNumber = jsonObject.getInt(Vehicle.PLUGED_NUMBER);

                String  productionDate = Utils.importDate(jsonObject.getString(Vehicle.PRODUCTION_DATE));

                String registerationDate = Utils.importDate(jsonObject.getString(Vehicle.REGISTERATION_DATE));

                int isCrossOut = jsonObject.getInt(Vehicle.IS_CROSS_OUT);

                String category = jsonObject.getString(Vehicle.CATEGORY);

                String  type = jsonObject.getString(Vehicle.TYPE);

                Vehicle vehicle=new Vehicle(false,plugedNumber,driver,type,category,productionDate,registerationDate,isCrossOut);

                view(vehicle);

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("fucking shit\n"+e.getLocalizedMessage());
        }
    }

    public Date getDate(String strDate){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
            return sdf.parse(strDate);
        }catch (Exception e){
            return null;
        }
    }
    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    private void view(Vehicle vehicle){
        txtViewPlugedNumber.setText(String.valueOf(vehicle.plugedNumber));
        txtViewCategory.setText(String.valueOf(vehicle.category));
        txtViewType.setText(String.valueOf(vehicle.type));

        txtViewIsCrossOut.setText(String.valueOf(vehicle.isCrossOut));
        txtViewProductionDate.setText(String.valueOf(vehicle.productionDate));
        txtViewRegisterationDate.setText(String.valueOf(vehicle.registerationDate));

        txtViewDriver.setText(vehicle.driver);

        plugedNumber=vehicle.plugedNumber;
    }
}
