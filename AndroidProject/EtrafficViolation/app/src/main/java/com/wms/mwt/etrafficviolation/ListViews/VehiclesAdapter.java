package com.wms.mwt.etrafficviolation.ListViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wms.mwt.etrafficviolation.R;
import com.wms.mwt.etrafficviolation.Sessions.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matrix on 11/28/2018.
 */

public class VehiclesAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<Vehicle> list=new ArrayList<>();
    Context context;

    public VehiclesAdapter(Context context,List<Vehicle>list){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.list=list;
    }


    @Override
    public long getItemId(int i) {
        return getItem(i).plugedNumber;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =inflater.inflate(R.layout.vehicle_item,null);
        //TextView txtViolationLogID=view.findViewById(R.id.);
        TextView txtViewPlugedNumber=view.findViewById(R.id.txtViewPlugedNumber);
        TextView txtViewDriver=view.findViewById(R.id.txtViewDriver);

        txtViewPlugedNumber.setText(String.valueOf(getItem(i).plugedNumber));
        txtViewDriver.setText(String.valueOf(getItem(i).driver));

        return view;
    }



    public void setlist(List<Vehicle>list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public Vehicle getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
