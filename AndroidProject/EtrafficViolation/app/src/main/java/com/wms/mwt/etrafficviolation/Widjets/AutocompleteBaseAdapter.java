package com.wms.mwt.etrafficviolation.Widjets;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.wms.mwt.etrafficviolation.Activities.MainActivity;
import com.wms.mwt.etrafficviolation.R;

import java.util.ArrayList;

    /**
     * Created by Matrix on 11/30/2018.
     */


    public class AutocompleteBaseAdapter  extends BaseAdapter implements Filterable {

        final String TAG = "AutocompleteCustomArrayAdapter.java";

        Context mContext;
        int layoutResourceId;
        Filter filter=new ItemFilter();
        ObjectAutocomplete[] originalData,filteredData = null;

        public AutocompleteBaseAdapter(Context mContext, int layoutResourceId, ObjectAutocomplete[] originalData) {
            this.mContext = mContext;
            this.layoutResourceId = layoutResourceId;
            this.originalData = originalData;
            this.filteredData=originalData;
        }

        @Override
        public int getCount() {
            return filteredData.length;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {

            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
                if (convertView == null) {
                    // inflate the layout
                    LayoutInflater inflater = ((MainActivity) mContext).getLayoutInflater();
                    convertView = inflater.inflate(layoutResourceId, parent, false);
                }

                // object item based on the position
                ObjectAutocomplete objectItem = filteredData[position];

                // get the TextView and then set the text (item name) and tag (item ID) values
                TextView textViewItem = (TextView) convertView.findViewById(R.id.txtViewAutocompleteText);
                textViewItem.setText(objectItem.text);

                TextView textViewID = (TextView) convertView.findViewById(R.id.txtViewAutocompleteID);
                textViewID.setText(objectItem.id);

                // in case you want to add some style, you can do something like:
                textViewItem.setBackgroundColor(Color.CYAN);

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;

        }

        @Override
        public ObjectAutocomplete getItem(int position) {
            return filteredData[position];
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }



        @NonNull
        @Override
        public Filter getFilter() {
            return filter;
        }

        private class ItemFilter extends Filter{
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String text=constraint.toString().toLowerCase();

                FilterResults filterResults=new FilterResults();

                ArrayList<ObjectAutocomplete> newList=new ArrayList<>();

                for(ObjectAutocomplete objectAutocomplete:originalData){
                    if(objectAutocomplete.text.toLowerCase().contains(text)){
                        newList.add(objectAutocomplete);
                    }
                }

                filterResults.values=newList.toArray();
                filterResults.count=newList.size();

                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData=(ObjectAutocomplete[]) results.values;
                notifyDataSetChanged();
            }
        }

    }