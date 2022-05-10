package com.wms.mwt.etrafficviolation.Activities;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class SearchActivity extends AppCompatActivity {

    RadioButton radioDriver,radioLocation,radioPlugedNumber,radioDate;
    EditText edtPlugedNumber,edtFromDate,edtToDate,edtDriver,edtLocation;
    LinearLayout llDate;
    Button btnSearch;
    int searchBy;

    String url;
    List<NameValuePair> params=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtDriver=findViewById(R.id.edtDriver);
        edtPlugedNumber=findViewById(R.id.edtPlugedNumber);
        edtLocation=findViewById(R.id.edtLocation);
        edtFromDate=findViewById(R.id.edtFromDate);
        edtToDate=findViewById(R.id.edtToDate);

        llDate=findViewById(R.id.llSearchDate);

        radioDate=findViewById(R.id.radioSearchDate);
        radioDriver=findViewById(R.id.radioSearchDriver);
        radioLocation=findViewById(R.id.radioSearchLocation);
        radioPlugedNumber=findViewById(R.id.radioSearchPlugedNumber);

        btnSearch=findViewById(R.id.btnSearch);
        setupBtn();
    }

    private void setupBtn(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (searchBy){
                    case R.id.radioSearchDriver:
                    {

                        //searchByDriver();
                        if(!TextUtils.isEmpty(edtDriver.getText())) {
                            url = HttpRequests.searchByDriver(edtDriver.getText().toString().trim().toLowerCase());
                            new SearchByAsync().execute();
                        }
                        break;
                    }
                    case R.id.radioSearchLocation:
                    {
                        //searchByLocation();
                        if(!TextUtils.isEmpty(edtLocation.getText())) {
                            url = HttpRequests.searchByLocation(edtLocation.getText().toString().trim().toLowerCase());
                            new SearchByAsync().execute();
                        }
                        break;
                    }
                    case R.id.radioSearchPlugedNumber:
                    {
                        //searchByPlugedNumber();
                        if(!TextUtils.isEmpty(edtPlugedNumber.getText())) {
                            if(Utils.checkInt(edtPlugedNumber.getText().toString().trim())) {
                                url = HttpRequests.searchByPlugedNumber(edtPlugedNumber.getText().toString().trim());
                                new SearchByAsync().execute();
                            }else{
                                showMessage("enter correct number");
                            }
                        }else {
                            showMessage("fill fields");
                        }
                        break;
                    }
                    case R.id.radioSearchDate:
                    {
                        //searchByDate();
                        //searchByPlugedNumber();
                        if(!TextUtils.isEmpty(edtFromDate.getText()) && !TextUtils.isEmpty(edtToDate.getText()) ) {
                            if(Utils.checkDate(edtFromDate.getText().toString().trim())&& Utils.checkDate(edtToDate.getText().toString().trim())) {
                                url = HttpRequests.searchByDate();
                                new SearchByAsync().execute();
                            }else{
                                showMessage("enter correct number");
                            }
                        }else {
                            showMessage("fill fields");
                        }
                        break;
                    }
                }


            }
        });
    }

    class SearchByAsync extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProgressDialog(SearchActivity.this,"Searching for violations");
          //  showMessage("start searching...");
           // pDialog.setCancelable(false);

//            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String method="GET";
            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(Vehicle.PLUGED_NUMBER,""));

            JsonParser jsonParser=new JsonParser();
            if(radioDate.isChecked()) {
                method="POST";
                params.add(new BasicNameValuePair(ViolationLog.FROM_DATE,Utils.exportDate(edtFromDate.getText().toString().trim())));
                params.add(new BasicNameValuePair(ViolationLog.TO_DATE,Utils.exportDate(edtToDate.getText().toString().trim())));
            }

            JSONObject data=jsonParser.makeHttpRequest(url,method,params);
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            Utils.dismiss();
            try {
                boolean error=data.getBoolean(HttpRequests.ERROR);
                if(!error){
                    extractAndViewItems(data);
                }else{
                    showMessage("no result matches");
                }
            }catch (Exception e){
                showMessage("no internet connection");
            }
        }
    }

    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }



    public void extractAndViewItems(JSONObject jsonObject) {
        try {
            //check error
            boolean error = jsonObject.getBoolean("error");
            if (!error) {

                double totalTax=jsonObject.getDouble(Violation.TAX);

                ArrayList<ViolationLog> violationsLog = new ArrayList<>();

                JSONArray violationsArray = jsonObject.getJSONArray(ViolationLog.VIOLATIONS);
                for (int i = 0; i < violationsArray.length(); i++) {

                    JSONObject violationLogObject = violationsArray.getJSONObject(i);

                    String driver = violationLogObject.getString(ViolationLog.DRIVER);

                    int plugedNumber = violationLogObject.getInt(ViolationLog.PLUGED_NUMBER);

                    int violationLogID = violationLogObject.getInt(ViolationLog.VIOLATION_LOG_ID);

                    int violationID=violationLogObject.getInt(Violation.VIOLATION_ID);

                    double tax = violationLogObject.getDouble(ViolationLog.TAX);

                    String date = Utils.importDate(violationLogObject.getString(ViolationLog.DATE));

                    int isPaid = violationLogObject.getInt(ViolationLog.IS_PAID);

                    String violationType = violationLogObject.getString(ViolationLog.VIOLATION_TYPE);

                    String location = violationLogObject.getString(ViolationLog.LOCATION);

                    ViolationLog violationLog=new ViolationLog(violationID,date,location,isPaid,violationType,driver,plugedNumber,violationLogID,tax);

                    violationsLog.add(violationLog);
                }

                viewViolations(violationsLog,totalTax);

            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void viewViolations(ArrayList<ViolationLog>violationLogs,double tax){
        //show result
        Intent intent=new Intent(this,SearchResultActivity.class);
        intent.putExtra(ViolationLog.VIOLATIONS_LOG,violationLogs);
        intent.putExtra(ViolationLog.TAX,tax);

        startActivity(intent);
    }



    public void hideAndShowSearchable(View view){
        RadioButton radioButton=(RadioButton)view;
        searchBy=radioButton.getId();
        switch (searchBy){
            case R.id.radioSearchDriver:
            {
                hideAndShow(View.VISIBLE,View.GONE,View.GONE,View.GONE);
                break;
            }
            case R.id.radioSearchLocation:
            {
                hideAndShow(View.GONE,View.GONE,View.GONE,View.VISIBLE);
                break;
            }
            case R.id.radioSearchPlugedNumber:
            {
                hideAndShow(View.GONE,View.VISIBLE,View.GONE,View.GONE);
                break;
            }
            case R.id.radioSearchDate:
            {
                hideAndShow(View.GONE,View.GONE,View.VISIBLE,View.GONE);
                break;
            }
        }
    }

    private void hideAndShow(int driver,int plugedNumber,int date,int location){
        llDate.setVisibility(date);
        edtDriver.setVisibility(driver);
        edtLocation.setVisibility(location);
        edtPlugedNumber.setVisibility(plugedNumber);
    }
}
