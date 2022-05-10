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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class LoginActivity extends AppCompatActivity{
    Button btnLogin;
    Button btnRegister;

    ProgressDialog pDialog;
    JsonParser jsonParser;
    EditText edtDriver,edtPlugedNumber;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);

        edtDriver=findViewById(R.id.edtDriver);
        edtPlugedNumber=findViewById(R.id.edtPlugedNumber);

        jsonParser=new JsonParser();

        prepareLoginButton();
        prepareRegisterButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionManager=SessionManager.getInstance(getApplicationContext());

        sessionManager.checkLogin();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    private void prepareLoginButton(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for input
                if(! TextUtils.isEmpty(edtDriver.getText()) && !TextUtils.isEmpty(edtPlugedNumber.getText())){

                    new LoginSync().execute();

                }else{
                    viewError("please enter values");
                }
            }
        });
    }


    class LoginSync extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(LoginActivity.this);
            pDialog.setCancelable(false);

            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url=HttpRequests.getLogin();

            String driver=edtDriver.getText().toString().trim().toLowerCase();
            String plugedNumber=edtPlugedNumber.getText().toString();

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(Vehicle.PLUGED_NUMBER,plugedNumber));

            params.add(new BasicNameValuePair(Vehicle.DRIVER,driver));

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
                    boolean isAdmin=data.getBoolean(Vehicle.IS_ADMIN);
                    int plugedNumber=Integer.parseInt(edtPlugedNumber.getText().toString().trim());
                    if(!isAdmin)
                    {
                        plugedNumber = data.getInt(Vehicle.PLUGED_NUMBER);
                    }
                    sessionManager.login(plugedNumber,isAdmin);
                }else{
                    viewError("Login Authentication error");
                }
            }catch (Exception e){
                 viewError("error : "+e.getMessage());
            }
        }
    }

    private void viewError(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    private void prepareRegisterButton(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }


        });
    }

}
