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
//import com.wms.mwt.etrafficviolation.Sessions.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matrix on 12/20/2017.
 */

public class ListViewAdapter extends BaseAdapter{

    LayoutInflater inflater;
    List<ViolationDetails>products=new ArrayList<>();
    Context context;

    public ListViewAdapter(Context context,List<ViolationDetails>products){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.products=products;
    }


    @Override
    public long getItemId(int i) {
        return getItem(i).violationLogID;
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
       // ImageButton imgDelete=view.findViewById(R.id.img_delete);
       // ImageButton imgEdit=view.findViewById(R.id.img_update);

        txtDate.setText(String.valueOf(getItem(i).date));
        txtPlugedNumber.setText(String.valueOf(getItem(i).plugedNumber));
        txtViolation.setText(String.valueOf(getItem(i).violationType));
        txtLocation.setText(String.valueOf(getItem(i).location));
        txtDriver.setText(String.valueOf(getItem(i).driver));
        //txtTax.setText(String.valueOf(getItem(i).t));

        int isPaid=getItem(i).isPaid;
        imgIsPaid.setImageResource(isPaid==0?R.mipmap.ic_not_paid:R.mipmap.ic_paid);
       // setup()

        return view;
    }



    public void setProducts(List<ViolationDetails>products){
        this.products=products;
        notifyDataSetChanged();
    }

    @Override
    public ViolationDetails getItem(int i) {
        return products.get(i);
    }

    @Override
    public int getCount() {
        return products.size();
    }
}
