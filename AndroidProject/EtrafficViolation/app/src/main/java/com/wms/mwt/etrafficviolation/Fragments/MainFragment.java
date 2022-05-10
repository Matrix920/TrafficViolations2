package com.wms.mwt.etrafficviolation.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.wms.mwt.etrafficviolation.Application.DataHandler;
import com.wms.mwt.etrafficviolation.ListViews.ListViewAdapter;
import com.wms.mwt.etrafficviolation.ListViews.ViolationDetails;
import com.wms.mwt.etrafficviolation.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Connections.JsonParser;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MainFragment extends Fragment implements ILoadList {

    protected ProgressBar pBar;
    //BaseAdapter adapter;
    View view;
    final protected DataHandler dataHandler=DataHandler.getInstance();
    ImageButton btnRefresh;

    ListView listView;
    //SessionManager sessionManager= c

    //not used
    RecyclerView recyclerView;
   // private static ProductsAdapter productsAdapter;
    //RequestQueue requestQueuee=AppController.getInstance().getRequestQueue();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onDestroy() {
        dataHandler.clear();
        super.onDestroy();
    }

    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //view= inflater.inflate(R.layout.fragment_main_1,container,false);
        view= inflater.inflate(R.layout.fragment_main,container,false);
        listView=view.findViewById(R.id.productsListView);


/*
        view= inflater.inflate(R.layout.fragment_main,container,false);
        recyclerView=view.findViewById(R.id.recycler_view);
*/
        pBar=view.findViewById(R.id.productsPogressBar);
        //btnRefresh=view.findViewById(R.id.btnRefresh);

        return view;
    }

    class GetItems extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
            pBar.setIndeterminate(true);
        }

        @Override
        protected JSONObject doInBackground(String... url) {
            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("x",""));

            JsonParser jsonParser=new JsonParser();
            JSONObject data=jsonParser.makeHttpRequest(url[0],"GET",params);
            return data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            pBar.setVisibility(View.GONE);
            if(jsonObject!=null) {
                //hide refresh button
              //  btnRefresh.setVisibility(View.GONE);
                extractAndViewItems(jsonObject);

            }else{
             //   viewRefreshButton();

            }
        }
    }
/*
    protected void viewRefreshButton(){
        viewError("check internet connection");
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRefresh.setVisibility(View.GONE);
                //getProducts();
            }
        });
    }
*/
    public Date getDate(String strDate){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd", Locale.US);
            return sdf.parse(strDate);
        }catch (Exception e){
            return null;
        }
    }

    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }



    protected void viewError(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }
/*
    private void setOnItemClicked(){
        productsAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(int product_id) {
                startProductActivity(product_id);
            }
        });
    }
*/


}
