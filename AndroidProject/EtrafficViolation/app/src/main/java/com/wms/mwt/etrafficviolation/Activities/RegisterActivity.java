package com.wms.mwt.etrafficviolation.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.SessionManager;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class RegisterActivity extends AppCompatActivity {

    JsonParser jsonParser;
    EditText edtPlugedNumber,edtDriver,edtType,edtCategory,edtRegisterationDate,edtProductionDate,edtIsCrossOut;

    HttpRequests httpRequests=new HttpRequests();
   SessionManager sessionManager;
    Button btnRegister,btnLogin;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        pDialog=new ProgressDialog(this);

        edtCategory=findViewById(R.id.edtCategory);
        edtType=findViewById(R.id.edtType);
        edtDriver=findViewById(R.id.edtDriver);
        edtPlugedNumber=findViewById(R.id.edtPlugedNumber);
        edtRegisterationDate=findViewById(R.id.edtRegisterationDate);
        edtProductionDate=findViewById(R.id.edtProductionDate);
        edtIsCrossOut=findViewById(R.id.edtIsCrossOut);

        btnRegister=findViewById(R.id.btnRegister);
        btnLogin=findViewById(R.id.btnLogin);

        setupRegister();
        setupLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionManager=SessionManager.getInstance(getApplicationContext());
    }

    private void setupLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupRegister(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!areValuesEmpty()){

                    new Register().execute();
                }else{
                    showMessage("check your values\n pluged number,registeration & production dates & is crossout");
                }
            }
        });
    }

    private boolean areValuesEmpty(){
        if(TextUtils.isEmpty(edtCategory.getText())||TextUtils.isEmpty(edtType.getText())
                ||TextUtils.isEmpty(edtPlugedNumber.getText()) || TextUtils.isEmpty(edtDriver.getText())
                ||TextUtils.isEmpty(edtProductionDate.getText()) || TextUtils.isEmpty(edtRegisterationDate.getText())
                ||TextUtils.isEmpty(edtIsCrossOut.getText()) )
            if(Utils.checkIsPaid(edtIsCrossOut.getText().toString().trim())&& Utils.checkDate(edtProductionDate.getText().toString().trim())
                    && Utils.checkDate(edtRegisterationDate.getText().toString().trim())&&Utils.checkInt(edtPlugedNumber.getText().toString().trim()))
                return true;
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionManager.checkLogin();
    }


    class Register extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setCancelable(false);

            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url=HttpRequests.register();

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(Vehicle.PLUGED_NUMBER,edtPlugedNumber.getText().toString().trim()));
            params.add(new BasicNameValuePair(Vehicle.DRIVER,edtDriver.getText().toString().trim()));
            params.add(new BasicNameValuePair(Vehicle.PRODUCTION_DATE,Utils.exportDate(edtProductionDate.getText().toString().trim())));
            params.add(new BasicNameValuePair(Vehicle.REGISTERATION_DATE, Utils.exportDate(edtRegisterationDate.getText().toString().trim())));
            params.add(new BasicNameValuePair(Vehicle.CATEGORY,edtCategory.getText().toString().trim()));
            params.add(new BasicNameValuePair(Vehicle.TYPE,edtType.getText().toString().trim()));
            params.add(new BasicNameValuePair(Vehicle.IS_CROSS_OUT,edtIsCrossOut.getText().toString().trim()));

            JsonParser jsonParser=new JsonParser();
            JSONObject data=jsonParser.makeHttpRequest(url,"POST",params);
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            pDialog.hide();
            try {
                boolean error=data.getBoolean(HttpRequests.ERROR);
                if(!error){
                    showMessage("you have registered successfully\nuse your LOGIN and PASSWORD to login");
                }
            }catch (Exception e){
                showMessage(e.getMessage());
            }
        }
    }

    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
