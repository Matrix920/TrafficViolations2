package com.wms.mwt.etrafficviolation.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.Application.DataHandler;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class UpdateViolationActivity extends AppCompatActivity {

    Violation violation;
    int violationID;

    Button btnUpdate;
    EditText edtViolation,edtTax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_violation);

        Intent intent=getIntent();
        violationID=intent.getIntExtra(Violation.VIOLATION_ID,0);

        edtTax=findViewById(R.id.edtTax);
        edtViolation=findViewById(R.id.edtViolation);
        setItems();

        btnUpdate=findViewById(R.id.btnUpdate);
        setupBtn();
    }

    private void setItems(){
        violation= Utils.getViolationByID(violationID);

        edtViolation.setText(violation.violationType);
        edtTax.setText(String.valueOf(violation.tax));
    }

    private void setupBtn(){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!areValuesEmpty())
                    new UpdateViolationAsync().execute();
            }
        });
    }

    private boolean areValuesEmpty(){
        if(TextUtils.isEmpty(edtViolation.getText())||TextUtils.isEmpty(edtTax.getText()) )
            return true;

        return false;
    }


    class UpdateViolationAsync extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utils.showProgressDialog(UpdateViolationActivity.this,"Saving changes");
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String url= HttpRequests.updateViolation();

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(Violation.VIOLATION_TYPE,edtViolation.getText().toString().trim()));
            params.add(new BasicNameValuePair(Violation.TAX,edtTax.getText().toString().trim()));
            params.add(new BasicNameValuePair(Violation.VIOLATION_ID,String.valueOf(violationID)));

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
                    showMessage("violation has updated");
                    DataHandler.getInstance().setViolations(null);
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
}
