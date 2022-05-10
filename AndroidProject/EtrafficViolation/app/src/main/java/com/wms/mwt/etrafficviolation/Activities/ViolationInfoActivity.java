package com.wms.mwt.etrafficviolation.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Utils.Utils;

public class ViolationInfoActivity extends AppCompatActivity {

    Violation violation;
    int violationID;

    TextView txtViewViolationType,txtViewTax;
    Button btnDelete,btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation_info);

        txtViewViolationType=findViewById(R.id.txtViewViolation);
        txtViewTax=findViewById(R.id.txtViewTax);

        Intent intent=getIntent();
        violationID=intent.getIntExtra(Violation.VIOLATION_ID,0);
        setupItems();

        btnUpdate=findViewById(R.id.btnUpdate);
        setupBtn();
    }

    private void setupItems(){
        violation= Utils.getViolationByID(violationID);

        txtViewTax.setText(String.valueOf(violation.tax));
        txtViewViolationType.setText(violation.violationType);
    }

    private void setupBtn(){
        //set up update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViolationInfoActivity.this,UpdateViolationActivity.class);
                intent.putExtra(Violation.VIOLATION_ID,violationID);
                startActivity(intent);
                finish();
            }
        });


    }
}
