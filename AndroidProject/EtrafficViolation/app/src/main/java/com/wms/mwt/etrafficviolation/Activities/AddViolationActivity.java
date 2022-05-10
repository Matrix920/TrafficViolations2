package com.wms.mwt.etrafficviolation.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.Application.DataHandler;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class AddViolationActivity extends AppCompatActivity {

    EditText edtViolation,edtTax;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_violation);

        edtTax=findViewById(R.id.edtTax);
        edtViolation=findViewById(R.id.edtViolation);

        btnAdd=findViewById(R.id.btnAddViolation);

        setupBtn();
    }
    private boolean areValuesEmpty(){
        if(TextUtils.isEmpty(edtViolation.getText())||TextUtils.isEmpty(edtTax.getText()) )
                return true;

        return false;
    }

    private void setupBtn(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!areValuesEmpty()){
                    new AddViolationAsync().execute();
                }else{
                    showMessage("fill the fields");
                }
            }
        });
    }

    class AddViolationAsync extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utils.showProgressDialog(AddViolationActivity.this,"adding new violation");

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url= HttpRequests.addViolation();

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(Violation.VIOLATION_TYPE,edtViolation.getText().toString().trim()));
            params.add(new BasicNameValuePair(Violation.TAX,edtTax.getText().toString().trim()));

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
                    showMessage("new violation has been added");
                    DataHandler.getInstance().setViolations(null);
                    finish();
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
