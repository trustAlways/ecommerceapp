package com.medassi.ecommerce.user.Activity.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.medassi.ecommerce.user.Activity.Activity.DetailActivity;
import com.medassi.ecommerce.user.Activity.Activity.MapsActivity;
import com.medassi.ecommerce.user.Activity.Activity.MyCartActivty;
import com.medassi.ecommerce.user.Activity.Activity.RecyclerViewActivity;
import com.medassi.ecommerce.user.Activity.Model.ForLatLongitude;
import com.medassi.ecommerce.user.Activity.Model.Search;
import com.medassi.ecommerce.user.Activity.Model.for_map_stokistshow.For_Map_XStokistshowbean;
import com.medassi.ecommerce.user.Activity.Model.product;
import com.medassi.ecommerce.user.Activity.fragment.SecondFragment;
import com.medassi.ecommerce.user.Activity.utils.MySharedPref;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<product> search_item;

    SessionManager sessionManager;
    List<For_Map_XStokistshowbean> result1;

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";
  //  Gson gson;
   String str_lat,str_lon,str_stockiest,str_product_price,str_available_qty;
    Activity context;
    //private MySharedPref sharedPreference;

    private ArrayList<ForLatLongitude> scores;

    ArrayList<String> arraylist_lat_value;
    ArrayList<String> arraylist_lon_value;
    ArrayList<String> arraylist_stokiest_value;
    ArrayList<String> arraylist_product_price_value;
    ArrayList<String> arraylist_available_qty_value;

    /* public CustomAdapter(RecyclerViewActivity recyclerViewActivity, ArrayList<product> namelist) {
        this.search_item = namelist;
        context = recyclerViewActivity;
        sessionManager = new SessionManager(context);

    }*/

  /* public CustomAdapter(RecyclerViewActivity recyclerViewActivity, List<For_Map_XStokistshowbean> result) {
        this.result1 = result;
        context = recyclerViewActivity;
        sessionManager = new SessionManager(context);

    }*/

    public CustomAdapter(Activity activity, List<For_Map_XStokistshowbean> result) {

        this.result1 = result;
        context = activity;
        sessionManager = new SessionManager(context);

    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);

        return new ViewHolder(v);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tv_versionname.setText(result1.get(position).getProductName());

        //saveArrayList(result1.get(position).getLet(),"alllatitude");
        /*  for(int j=0;j<result1.size();j++) {
            ForLatLongitude forLatLongitude=new ForLatLongitude();

             str_lat = String.valueOf(result1.get(j).getLet());
             str_lon = String.valueOf(result1.get(j).getLong());
             str_stockiest = String.valueOf(result1.get(j).getStockiestId());
             str_product_id = result1.get(j).getProductId();

             forLatLongitude.setLat(str_lat);
             forLatLongitude.setLon(str_lon);

             forLatLongitudes.add(forLatLongitude);
            System.out.println("List Latitude%%%"+str_lat);
            try {
                JSONArray jsonArray=new JSONArray(str_lat);
                arraylist_lat_value=new ArrayList<>();
                for (int k=0;k<jsonArray.length();k++) {


                    System.out.println("List Lat^^^"+jsonArray.get(k));
                    String str_arraylist_lat_value= String.valueOf(jsonArray.get(k));
                    arraylist_lat_value.add(str_arraylist_lat_value);
                   // saveArrayList(arraylist_lat_value, "alllatitude");

                }

                Log.i("Responseeee","cutom"+ result1.get(position).getLet());
                Log.i("Responseeee","cutom"+ result1.get(position).getLong());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/



        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click on view"+ search_item.get(position).getId(), Toast.LENGTH_SHORT).show();
                sessionManager.setData(SessionManager.KEY_PRODUCT_ID, search_item.get(position).getId());
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,search_item.get(position).getStockiest_id());
                Intent intent = new Intent(context,DetailActivity.class);
                context.startActivity(intent);
            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  scores=new ArrayList<>();

                System.out.println("List OnClick###"+result1.get(position).getLet());
                str_lat = String.valueOf(result1.get(position).getLet());
                try {
                    JSONArray jsonArray=new JSONArray(str_lat);
                    arraylist_lat_value=new ArrayList<>();

                    for (int k=0;k<jsonArray.length();k++) {

                       // ForLatLongitude forLatLongitude=new ForLatLongitude();

                        System.out.println("List Lat^^^"+jsonArray.get(k));
                        String str_arraylist_lat_value= String.valueOf(jsonArray.get(k));
                        arraylist_lat_value.add(str_arraylist_lat_value);
                       /* forLatLongitude.setLat(str_arraylist_lat_value);
                        scores.add(forLatLongitude);*/

                        saveArrayList(arraylist_lat_value, "alllatitude");

                    }
                    Log.i("Responseeee","cutom"+ result1.get(position).getLet());
                    Log.i("Responseeee","cutom"+ result1.get(position).getLong());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                str_lon = String.valueOf(result1.get(position).getLong());
                try {
                    JSONArray jsonArray=new JSONArray(str_lon);
                    arraylist_lon_value=new ArrayList<>();
                    // forLatLongitudes=new ArrayList<>();
                    for (int k=0;k<jsonArray.length();k++) {


                        System.out.println("List Lat^^^"+jsonArray.get(k));
                        String str_arraylist_lon_value= String.valueOf(jsonArray.get(k));
                        arraylist_lon_value.add(str_arraylist_lon_value);


                        saveArrayList2(arraylist_lon_value, "alllongitude");

                    }



                    Log.i("Responseeee","cutom"+ result1.get(position).getLet());
                    Log.i("Responseeee","cutom"+ result1.get(position).getLong());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                str_stockiest = String.valueOf(result1.get(position).getStockiestId());
                try {
                    JSONArray jsonArray=new JSONArray(str_stockiest);
                    arraylist_stokiest_value=new ArrayList<>();
                    for (int k=0;k<jsonArray.length();k++) {
                        //  ForLatLongitude forLatLongitude=new ForLatLongitude();
                        System.out.println("List Lat^^^"+jsonArray.get(k));
                        String str_arraylist_stokist_value= String.valueOf(jsonArray.get(k));
                        arraylist_stokiest_value.add(str_arraylist_stokist_value);
                     /*   forLatLongitude.setStockiest_id(str_arraylist_stokist_value);
                        scores.add(forLatLongitude);*/

                        saveArrayList3(arraylist_stokiest_value, "allstokiest");

                    }
                    Log.i("Responseeee","str_stockiest"+ str_stockiest);
                    Log.i("Responseeee","str_lon"+ str_lon);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

                str_product_price = String.valueOf(result1.get(position).getProductPrice());
                try {
                    JSONArray jsonArray=new JSONArray(str_product_price);
                    arraylist_product_price_value=new ArrayList<>();
                    for (int k=0;k<jsonArray.length();k++) {
                        //  ForLatLongitude forLatLongitude=new ForLatLongitude();
                        System.out.println("List Lat^^^"+jsonArray.get(k));
                        String str_arraylist_stokist_value= String.valueOf(jsonArray.get(k));
                        arraylist_product_price_value.add(str_arraylist_stokist_value);
                     /*   forLatLongitude.setStockiest_id(str_arraylist_stokist_value);
                        scores.add(forLatLongitude);*/

                        saveArrayList4(arraylist_product_price_value, "allproductprice");

                    }
                    Log.i("Responseeee","str_stockiest"+ str_stockiest);
                    Log.i("Responseeee","str_lon"+ str_lon);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
                str_available_qty = String.valueOf(result1.get(position).getAvailableQty());
                try {
                    JSONArray jsonArray=new JSONArray(str_available_qty);
                    arraylist_available_qty_value=new ArrayList<>();
                    for (int k=0;k<jsonArray.length();k++) {
                        //  ForLatLongitude forLatLongitude=new ForLatLongitude();
                        System.out.println("List Lat^^^"+jsonArray.get(k));
                        String str_arraylist_stokist_value= String.valueOf(jsonArray.get(k));
                        arraylist_available_qty_value.add(str_arraylist_stokist_value);
                     /*   forLatLongitude.setStockiest_id(str_arraylist_stokist_value);
                        scores.add(forLatLongitude);*/

                        saveArrayList5(arraylist_available_qty_value, "allavailableqty");

                    }
                    Log.i("Responseeee","str_stockiest"+ str_stockiest);
                    Log.i("Responseeee","str_lon"+ str_lon);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }


                //Toast.makeText(context, "click on view"+ result1.get(position).getProductId()
                        //+result1.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                /*sessionManager.setData(SessionManager.KEY_PRODUCT_ID, search_item.get(position).getId());
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,search_item.get(position).getStockiest_id());*/
                sessionManager.setData(SessionManager.KEY_LONGITUDE, str_lon);
                sessionManager.setData(SessionManager.KEY_LATITUDE, str_lat);
                sessionManager.setData(SessionManager.KEY_PRODUCT_NAME,result1.get(position).getProductName());
                sessionManager.setData(SessionManager.KEY_PRODUCT_ID,result1.get(position).getProductId());
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID, str_stockiest);


                Intent intent = new Intent(context,MapsActivity.class);
                context.startActivity(intent);
            }
        });

    }

    public void saveArrayList(List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }
    public void saveArrayList2(List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }
    public void saveArrayList3(List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }
    public void saveArrayList4(List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public void saveArrayList5(List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }


    @Override
    public int getItemCount() {
        return result1.size();
    }

    /*public void filterList(ArrayList<String> filterdNames) {
        this.search_item = filterdNames;
        notifyDataSetChanged();
    }
*/

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*public void filter(String s)
    {
       s = s.toLowerCase(Locale.getDefault());
       search_item.clear();
       if (s.length()==0)
       {
           search_item.addAll(search_list);
       }
       else
       {
           for (product product : search_list)
           {
            if (product.getName().toLowerCase(Locale.getDefault()).contains(s))
            {
                search_item.add(product);
            }
           }
       }
       notifyDataSetChanged();
    }*/

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_versionname;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_versionname = (TextView) itemView.findViewById(R.id.tv_versionname);
        }
    }

   public void filterList(List<For_Map_XStokistshowbean> filterdNames) {
        if (filterdNames!=null)
        {
            this.result1 = filterdNames;
            notifyDataSetChanged();

        }

    }
}
