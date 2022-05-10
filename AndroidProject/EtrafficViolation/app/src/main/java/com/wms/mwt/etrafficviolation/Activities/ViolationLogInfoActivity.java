package com.wms.mwt.etrafficviolation.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.Application.DataHandler;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;
import com.wms.mwt.etrafficviolation.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class ViolationLogInfoActivity extends AppCompatActivity {

    ViolationLog violationLog;
    int violationLogID;
    TextView txtViewPlugedNumber,txtViewLocation,txtViewViolationType,txtViewIsPaid,txtViewDate;
    Button btnUpdate,btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation_log_info);

        txtViewDate=findViewById(R.id.txtViewDate);
        txtViewLocation=findViewById(R.id.txtViewLocation);
        txtViewIsPaid=findViewById(R.id.txtViewIsPaid);
        txtViewViolationType=findViewById(R.id.txtViewViolation);
        txtViewPlugedNumber=findViewById(R.id.txtViewPlugedNumber);

        btnDelete=findViewById(R.id.btnDelete);
        btnUpdate=findViewById(R.id.btnUpdate);

        Intent intent=getIntent();
        violationLogID=intent.getIntExtra(ViolationLog.VIOLATION_LOG_ID,0);
        setItems();
        setupButtons();
    }

    class DeleteViolationLogAsync extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utils.showProgressDialog(ViolationLogInfoActivity.this,"Deleting Violation from log");

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url= HttpRequests.deleteViolationLog();

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(ViolationLog.VIOLATION_LOG_ID,String.valueOf(violationLogID)));

            JsonParser jsonParser=new JsonParser();
            JSONObject data=jsonParser.makeHttpRequest(url,"POST",params);

            return data;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            //  pDialog.hide();
            Utils.dismiss();
            try {
                boolean error=data.getBoolean(HttpRequests.ERROR);
                if(!error){
                    showMessage("violation was deleted");
                    DataHandler.getInstance().setViolationsLog(null);
                    finish();
                }
            }catch (Exception e){
                showMessage("no internet connection");
            }
        }
    }

    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    private void setItems(){
        violationLog= Utils.getViolationLogByID(violationLogID);

        txtViewPlugedNumber.setText(String.valueOf(violationLog.plugedNumber));
        txtViewViolationType.setText(violationLog.violationType);
        txtViewIsPaid.setText(String.valueOf(violationLog.isPaid));
        txtViewLocation.setText(violationLog.location);
        txtViewDate.setText(violationLog.date.toString());
    }

    private void setupButtons(){
        //setup update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // /start update activity
                Intent intent=new Intent(ViolationLogInfoActivity.this,UpdateViolationLogActivity.class);
                intent.putExtra(ViolationLog.VIOLATION_LOG_ID,violationLogID);
                startActivity(intent);
            }
        });

        //setup delete button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // /execute delete async
                new DeleteViolationLogAsync().execute();

            }
        });
    }
}
