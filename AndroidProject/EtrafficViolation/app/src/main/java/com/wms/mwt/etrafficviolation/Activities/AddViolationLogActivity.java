package com.wms.mwt.etrafficviolation.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wms.mwt.etrafficviolation.Application.DataHandler;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;
import com.wms.mwt.etrafficviolation.Utils.Utils;
import com.wms.mwt.etrafficviolation.Widjets.AutocompleteBaseAdapter;
import com.wms.mwt.etrafficviolation.Widjets.AutocompleteCustomArrayAdapter;
import com.wms.mwt.etrafficviolation.Widjets.ObjectAutocomplete;

import org.apache.http.NameValuePair;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.HttpRequests;
import Connections.JsonParser;

public class AddViolationLogActivity extends AppCompatActivity {

    AutoCompleteTextView edtPlugedNumber,edtViolation;

    Button btnAddViolationLog;

    DataHandler dataHandler=DataHandler.getInstance();

    EditText edtLocation,edtIsPaid,edtDate;

    int newViolationID=0;


    ArrayAdapter publgedNumberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_violation_log);

        edtPlugedNumber=findViewById(R.id.edtPlugedNumber);
       setupPlugedNumbes();

        edtViolation=findViewById(R.id.edtViolation);
       setupViolations();

       btnAddViolationLog=findViewById(R.id.btnAddViolationLog);
       setupBtnAdd();

       edtDate=findViewById(R.id.edtDate);
       edtIsPaid=findViewById(R.id.edtIsPaid);
       edtLocation=findViewById(R.id.edtLocation);
    }

    private void setupBtnAdd(){
        btnAddViolationLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!areValuesEmpty()){
                    new AddViolationLogAsync().execute();
                }else{
                    showMessage("fill the fields & check your values\n pluged number , is paid , date");
                }
            }
        });
    }

    private boolean areValuesEmpty(){
        if(TextUtils.isEmpty(edtViolation.getText())||TextUtils.isEmpty(edtIsPaid.getText())
                ||TextUtils.isEmpty(edtPlugedNumber.getText()) || TextUtils.isEmpty(edtDate.getText())
                ||TextUtils.isEmpty(edtLocation.getText()))
            if(Utils.checkInt(edtPlugedNumber.getText().toString().trim()) && Utils.checkDate(edtDate.getText().toString().trim())
                && Utils.checkIsPaid(edtIsPaid.getText().toString().trim()))
                return true;

        return false;
    }


    class AddViolationLogAsync extends AsyncTask<Void,Void,JSONObject> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProgressDialog(AddViolationLogActivity.this,"adding new violation to log");
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            String url = HttpRequests.addViolationLog();

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair(ViolationLog.PLUGED_NUMBER,edtPlugedNumber.getText().toString().trim()));
            params.add(new BasicNameValuePair(ViolationLog.DATE,Utils.exportDate(edtDate.getText().toString().trim())));
            params.add(new BasicNameValuePair(ViolationLog.LOCATION,edtLocation.getText().toString().trim()));
            params.add(new BasicNameValuePair(Violation.VIOLATION_ID,String.valueOf(newViolationID)));
            params.add(new BasicNameValuePair(ViolationLog.IS_PAID,edtIsPaid.getText().toString().trim()));

            JsonParser jsonParser=new JsonParser();
            JSONObject data=jsonParser.makeHttpRequest(url,"POST",params);
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Utils.dismiss();

            if(jsonObject!=null) {
                showMessage("New violatin added");
                DataHandler.getInstance().setViolationsLog(null);
                finish();

            }else{
                showMessage("pluged number or violation not matches");

            }
        }
    }

    private void setupPlugedNumbes() {

        if(!dataHandler.isVehiclesEmpty()) {

            List<Vehicle> plugedNumbers = dataHandler.getVehicles();

            final String[] plugedNumbersArray = Utils.convertPlugedNumbersToStringArray(plugedNumbers);

            publgedNumberAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, plugedNumbersArray);

            edtPlugedNumber.setAdapter(publgedNumberAdapter);

            edtPlugedNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    publgedNumberAdapter.notifyDataSetChanged();

                    String[]newData=Utils.performFilteringInt(plugedNumbersArray,s);
                    ArrayAdapter publgedNumberAdapter=new ArrayAdapter(AddViolationLogActivity.this,android.R.layout.select_dialog_item,newData);
                    edtPlugedNumber.setAdapter(publgedNumberAdapter);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }else{
            Toast.makeText(this,"fucking shit ",Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViolations() {
        if (!dataHandler.isViolationsEmpty()) {
            List<Violation> violations = dataHandler.getViolations();
            final ObjectAutocomplete[] violationsArray = Utils.convertViolationsToObjectAutocomplete(violations);

              final ArrayAdapter violationTypesAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.list_view_autocomplete, violationsArray);
            edtViolation.setAdapter(violationTypesAdapter);

            edtViolation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RelativeLayout rl = (RelativeLayout) view;

                    TextView txtViewViolationType = (TextView) rl.getChildAt(0);
                    edtViolation.setText(txtViewViolationType.getText());

                    TextView txtViewViolationID = (TextView) rl.getChildAt(1);
                    String strId=txtViewViolationID.getText().toString();
                    newViolationID = Integer.valueOf(strId);
                }
            });

            edtViolation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    violationTypesAdapter.notifyDataSetChanged();
                  ObjectAutocomplete[]newData=Utils.performFiltering(violationsArray,s);
                  ArrayAdapter violationTypesAdapter=new AutocompleteCustomArrayAdapter(AddViolationLogActivity.this,R.layout.list_view_autocomplete,newData);
                  edtViolation.setAdapter(violationTypesAdapter);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }else{
        }
    }

    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }
}
