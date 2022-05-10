package com.wms.mwt.etrafficviolation.ListViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wms.mwt.etrafficviolation.Activities.UpdateViolationLogActivity;
import com.wms.mwt.etrafficviolation.Activities.ViolationLogInfoActivity;
import com.wms.mwt.etrafficviolation.Fragments.DriverViolationsLogFragment;
import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.ViolationLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matrix on 12/3/2018.
 */

public class DriverViolationLogAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<ViolationLog> list=new ArrayList<>();
    Context context;
    DriverViolationsLogFragment fragment;

    public DriverViolationLogAdapter(Context context,List<ViolationLog>list,DriverViolationsLogFragment fragment){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.list=list;
        this.fragment=fragment;
    }




    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =inflater.inflate(R.layout.driver_violation_log_details,null);

        TextView txtViolation=view.findViewById(R.id.txt_view_violation);
        TextView txtDate=view.findViewById(R.id.txt_view_date);
        TextView txtLocation=view.findViewById(R.id.txt_view_location);
        TextView txtTax=view.findViewById(R.id.txt_view_tax);
        ImageView imgIsPad=view.findViewById(R.id.img_is_paid);
        Button btnPay=view.findViewById(R.id.btnPay);

       ;

        txtDate.setText(String.valueOf(getItem(i).date));
        txtViolation.setText(String.valueOf(getItem(i).violationType));
        txtLocation.setText(String.valueOf(getItem(i).location));
        txtTax.setText(String.valueOf(getItem(i).tax));

        int isPaid=getItem(i).isPaid;
        imgIsPad.setImageResource(isPaid==0?R.mipmap.ic_not_paid:R.mipmap.ic_paid);

        setupBtns(btnPay,(int)getItemId(i));

        return view;
    }

    private void setupBtns(Button btnPay,final int id) {
            btnPay.setVisibility(View.VISIBLE);

            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.payAsync.execute((int)id);
                }
            });
    }

    public void setlist(List<ViolationLog>list){
        this.list=list;
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


