package com.wms.mwt.etrafficviolation.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wms.mwt.etrafficviolation.ListViews.ViolationsLogAdapter;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.Violation;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    ListView listView;
    TextView txtViewTotalTax;
    double totalTax;
    ArrayList<ViolationLog>violationLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        listView=findViewById(R.id.productsListView);

        txtViewTotalTax=findViewById(R.id.txtViewTotalTax);

        Intent intent=getIntent();
        violationLogs=(ArrayList<ViolationLog>) intent.getSerializableExtra(ViolationLog.VIOLATIONS_LOG);
        totalTax=intent.getDoubleExtra(Violation.TAX,0);
        viewList();
    }

    private void viewList() {

        txtViewTotalTax.setText(String.valueOf(totalTax));
        BaseAdapter adapter=new ViolationsLogAdapter(this,violationLogs);
        listView.setAdapter(adapter);
    }
}
