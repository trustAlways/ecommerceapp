package com.medassi.ecommerce.user.Activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import com.medassi.ecommerce.user.R;

/**
 * Created by kamlesh on 2/13/2018.
 */
public class Adp_Search extends ArrayAdapter<String> implements Filterable {

    Context mContext;
    int layoutResourceId;
    ArrayList<String> namelist;

    public Adp_Search(Context mContext, int layoutResourceId, ArrayList<String> search_list) {

        super(mContext, layoutResourceId, search_list);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.namelist = search_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                // inflate the layout
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            TextView textViewItem = (TextView) convertView.findViewById(R.id.tv_customactv);
            textViewItem.setText(namelist.get(position));

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }



   /* public void filterList(ArrayList<String> filterdNames)
    {
        this.namelist = filterdNames;
         notifyDataSetChanged();
    }*/
}
