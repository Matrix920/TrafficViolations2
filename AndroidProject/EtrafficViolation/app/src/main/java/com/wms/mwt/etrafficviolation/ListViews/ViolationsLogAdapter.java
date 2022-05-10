package com.wms.mwt.etrafficviolation.ListViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wms.mwt.etrafficviolation.Activities.UpdateViolationLogActivity;
import com.wms.mwt.etrafficviolation.Activities.ViolationLogInfoActivity;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matrix on 11/28/2018.
 */

public class ViolationsLogAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ViolationLog> list=new ArrayList<>();
    Context context;

    public ViolationsLogAdapter(Context context,List<ViolationLog>list){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.list=list;
    }




    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =inflater.inflate(R.layout.violation_log_details,null);
        //TextView txtViolationLogID=view.findViewById(R.id.);
        TextView txtPlugedNumber=view.findViewById(R.id.txt_view_pluged_number);
        TextView txtViolation=view.findViewById(R.id.txt_view_violation);
        TextView txtDate=view.findViewById(R.id.txt_view_date);
        TextView txtLocation=view.findViewById(R.id.txt_view_location);
        TextView txtTax=view.findViewById(R.id.txt_view_tax);
        ImageView imgIsPaid=view.findViewById(R.id.img_is_paid);
        TextView txtDriver=view.findViewById(R.id.txt_view_driver);

        Button btnUpdate=view.findViewById(R.id.btnUpdate);
        Button btnDelete=view.findViewById(R.id.btnDelete);

        txtDate.setText(String.valueOf(getItem(i).date));
        txtPlugedNumber.setText(String.valueOf(getItem(i).plugedNumber));
        txtViolation.setText(String.valueOf(getItem(i).violationType));
        txtLocation.setText(String.valueOf(getItem(i).location));
        txtDriver.setText(String.valueOf(getItem(i).driver));
        txtTax.setText(String.valueOf(getItem(i).tax));

        setupBtns(btnUpdate,btnDelete,(int)getItemId(i));

        int isPaid=getItem(i).isPaid;
        imgIsPaid.setImageResource(isPaid==0?R.mipmap.ic_not_paid:R.mipmap.ic_paid);
        // setup()

        return view;
    }

    private void setupBtns(Button btnUpdate,Button btnDelete,final int id){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpdateViolationLogActivity.class);
                intent.putExtra(ViolationLog.VIOLATION_LOG_ID,id);
                context.startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViolationLogInfoActivity.class);
                intent.putExtra(ViolationLog.VIOLATION_LOG_ID,id);
                context.startActivity(intent);
            }
        });
    }



    public void setViolations(List<ViolationLog>violations){
        this.list=violations;
        notifyDataSetChanged();
    }

    @Override
    public ViolationLog getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).violationLogID;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}


