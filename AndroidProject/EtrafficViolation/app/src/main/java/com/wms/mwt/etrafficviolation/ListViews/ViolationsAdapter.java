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
import com.wms.mwt.etrafficviolation.Sessions.Violation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matrix on 11/28/2018.
 */

public class ViolationsAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<Violation> list=new ArrayList<>();
    Context context;

    public ViolationsAdapter(Context context,List<Violation>list){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.list=list;
    }


    @Override
    public long getItemId(int i) {
        return getItem(i).violationID;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =inflater.inflate(R.layout.violation_item,null);
        TextView txtViewViolationID=view.findViewById(R.id.txtViewViolationID);
        TextView txtViewViolation=view.findViewById(R.id.txtViewViolation);
        TextView txtViewTax=view.findViewById(R.id.txtViewTax);

        txtViewViolationID.setText(String.valueOf(getItem(i).violationID));
        txtViewViolation.setText(String.valueOf(getItem(i).violationType));
        txtViewTax.setText(String.valueOf(getItem(i).tax));



        return view;
    }



    public void setlist(List<Violation>list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public Violation getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
