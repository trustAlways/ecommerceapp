package com.medassi.ecommerce.user.Activity.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
//import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medassi.ecommerce.user.Activity.Model.CartList;
import com.medassi.ecommerce.user.Activity.Model.ForLatLongitude;
import com.medassi.ecommerce.user.Activity.Model.Stockiestdetail;
import com.medassi.ecommerce.user.Activity.utils.GPSTracker;
import com.medassi.ecommerce.user.Activity.utils.MySharedPref;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    TextView tv_Stockiest_name,tv_stockretail, tv_distance_kms, user_location,tv_product_name,tv_ProductPrice ,
            tv_Product_Availability,tv_specification,tv_info,cart_item_count,
    Search_fro,show_search_result,
            searched_product_list1,mapview1,
            searched_product_list2,mapview2;

    ArrayList<CartList> search_list;

       String Product_id, product_name,distributor_name,product_price,product_image,stockiest,product_quantity,product_sku,
            description,distance,solid_qunty,stockiest_retailer_name,stockiest_type,stockiest_distance,stockiest_product_qty,stockiest_product_price,type;

    public static LinearLayout Linear_map,Linear_list;
    public static ListView listView;
    public StockiestListAdapter stockiestListAdapter;
    EditText edt_qty;
    String product_quntity;

    ImageView cutImage;
    Button addtocart;
    String product_id,Stockiest_id,Stockiest_Idd;
    String addresss,city,country;
    String token;
    List<Address> address;
    double dist;
    private GoogleMap mMap;
    SessionManager sessionManager;
    public  Context ctx;
    String name;
    String id, Stockiest_Id,user_id, user_add;
    double latitude, longitude,currentlat,currentlong;
    LocationManager mlocationmanager;
    LocationListener mLocationListener;
    Dialog mydialog;
    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";
    Gson gson;
    private MySharedPref sharedPreference;
    private ArrayList<ForLatLongitude> scores;
    ArrayList<String> arr_stokist;
    ArrayList<String> arr_lat;
    ArrayList<String> arr_lon;
    ArrayList<String> arr_product_prize;
    ArrayList<String> arr_product_avail_qty;
    ArrayList<Stockiestdetail> stockiest_detail;

    JSONArray jsonArray6,jsonArray7;
    String json1,json2,json3,json4,json5;
    int k,a;

    double dub_lat4,dub_lon4;
    String stokeist_id4;
    ForLatLongitude forLatLongitude;
    HashMap<String, String> hashmap;
    String str_arraylist_lat_valueeee,str_arraylist_lon_valueeee,str_arraylist_stokiest_valueeee,str_arraylist_prodct_prize_valueeee,str_arraylist_avl_qty_valueeee;
    int q;
    boolean bool_check_data=false;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.demo, null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("Search Results");

        initView();
        checkForPermission();

        getLocation();
        click();
        getStockiestData(id);

        //creating a support fragment for map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }



   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        initView();
        checkForPermission();

        getLocation();
        click();
        getStockiestData(id);

        //creating a support fragment for map
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }*/

    private void initView() {

        ctx = getActivity();
        sessionManager = new SessionManager(getActivity());
        address = new ArrayList<>();
        stockiest_detail = new ArrayList<>();
        search_list = new ArrayList<>();
        token = sessionManager.getData(SessionManager.KEY_TOKEN);

        //  getAllColor("alllatitude","alllongitude","allstokiest");
        getArrayList("alllatitude");
        getArrayList2("alllongitude");
        getArrayList3("allstokiest");
        getArrayList4("allproductprice");
        getArrayList5("allavailableqty");

        getAllColor(json1,json2,json3,json4,json5);
        name = sessionManager.getData(SessionManager.KEY_PRODUCT_NAME);

        Search_fro = (TextView)view.findViewById(R.id.search_for);

        String n = capitalize(name);

        String styledText = "You searched for"+" <font color='red'>" + n + "</font> ";
        Search_fro.setText(Html.fromHtml(styledText));

        tv_info = (TextView)view.findViewById(R.id.infotext);

        show_search_result = (TextView)view.findViewById(R.id.show_search_result);

        searched_product_list1 = (TextView)view.findViewById(R.id.searched_product_list1);
        mapview1 =(TextView)view.findViewById(R.id.mapview1);

        searched_product_list2 = (TextView)view.findViewById(R.id.searched_product_list2);
        mapview2 =(TextView)view.findViewById(R.id.mapview2);

        Linear_map = (LinearLayout)view.findViewById(R.id.Linear_map);
        Linear_list = (LinearLayout)view.findViewById(R.id.Linear_list);

        listView = (ListView)view.findViewById(R.id.listview);
        //getting data from shared
        id = sessionManager.getData(SessionManager.KEY_PRODUCT_ID);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);
        user_add = sessionManager.getData(SessionManager.KEY_USER_CURRENT_ADD);
    }

    private void getLocation() {
        GPSTracker gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            currentlat = gps.getLatitude();
            currentlong = gps.getLongitude();

            //sessionManager.setDouble(SessionManager.KEY_LAT, latitude);
            //sessionManager.setDouble(SessionManager.KEY_LONG, longitude);

        } else {
            gps.showSettingsAlert();
        }
    }

    private void click() {

        searched_product_list1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (Linear_map.getVisibility()==View.VISIBLE)
                {
                    Linear_map.setVisibility(View.GONE);
                    Linear_list.setVisibility(View.VISIBLE);
                    tv_info.setVisibility(View.VISIBLE);
                    searched_product_list1.setVisibility(View.GONE);
                    mapview2.setVisibility(View.VISIBLE);
                    mapview1.setVisibility(View.GONE);
                    searched_product_list2.setVisibility(View.VISIBLE);
                }
             /* else
              {
                  mapview2.setVisibility(View.GONE);
                  mapview1.setVisibility(View.VISIBLE);
                  Linear_map.setVisibility(View.VISIBLE);
                  Linear_list.setVisibility(View.GONE);
                  tv_info.setVisibility(View.GONE);
                  searched_product_list1.setVisibility(View.VISIBLE);
                  searched_product_list2.setVisibility(View.GONE);

                  }*/
            }
        });

        searched_product_list2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (Linear_map.getVisibility()==View.GONE)
                {
                    mapview2.setVisibility(View.VISIBLE);
                    mapview1.setVisibility(View.GONE);
                    Linear_map.setVisibility(View.GONE);
                    Linear_list.setVisibility(View.VISIBLE);
                    tv_info.setVisibility(View.VISIBLE);
                    searched_product_list1.setVisibility(View.GONE);
                    searched_product_list2.setVisibility(View.VISIBLE);
                }
               /* else
                {
                    mapview2.setVisibility(View.GONE);
                    mapview1.setVisibility(View.VISIBLE);
                    Linear_map.setVisibility(View.VISIBLE);
                    Linear_list.setVisibility(View.GONE);
                    tv_info.setVisibility(View.GONE);
                    searched_product_list1.setVisibility(View.GONE);
                    searched_product_list2.setVisibility(View.VISIBLE);


                }*/
            }
        });

        mapview1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (Linear_list.getVisibility()==View.GONE)
                {
                    Linear_list.setVisibility(View.GONE);
                    tv_info.setVisibility(View.GONE);
                    Linear_map.setVisibility(View.VISIBLE);
                    mapview1.setVisibility(View.VISIBLE);
                    mapview2.setVisibility(View.GONE);
                    searched_product_list1.setVisibility(View.VISIBLE);
                    searched_product_list2.setVisibility(View.GONE);
                }
               /* else
                {
                    Linear_list.setVisibility(View.VISIBLE);
                    tv_info.setVisibility(View.VISIBLE);
                    Linear_map.setVisibility(View.GONE);
                    mapview1.setVisibility(View.VISIBLE);
                    mapview2.setVisibility(View.GONE);
                    searched_product_list1.setVisibility(View.GONE);
                    searched_product_list2.setVisibility(View.VISIBLE);
                }*/
            }
        });
        mapview2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (Linear_list.getVisibility()==View.VISIBLE)
                {
                    Linear_list.setVisibility(View.GONE);
                    tv_info.setVisibility(View.GONE);
                    Linear_map.setVisibility(View.VISIBLE);
                    mapview1.setVisibility(View.VISIBLE);
                    mapview2.setVisibility(View.GONE);
                    searched_product_list2.setVisibility(View.GONE);
                    searched_product_list1.setVisibility(View.VISIBLE);
                }
               /* else
                {
                    Linear_list.setVisibility(View.GONE);
                    tv_info.setVisibility(View.GONE);
                    Linear_map.setVisibility(View.VISIBLE);
                    mapview1.setVisibility(View.VISIBLE);
                    mapview2.setVisibility(View.GONE);
                    searched_product_list2.setVisibility(View.GONE);
                    searched_product_list1.setVisibility(View.VISIBLE);
                }*/
            }
        });
    }




    private void getStockiestData(final String id) {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_STOCKIEST_BY_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try{
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            Log.i("response..!","Stockiest Deatil"+ response);
                            String Status = obj.getString("status");
                            String msg = obj.getString("message");

                            if (Status.equals("success")||msg.equals("product details")) {
                                JSONArray jsonArray = obj.getJSONArray("result");
                                  for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    Product_id = jsonObject.getString("product_id");
                                    product_sku = jsonObject.getString("product_sku");
                                    product_name = jsonObject.getString("product_name");
                                    product_image = jsonObject.getString("product_image");
                                    distributor_name = jsonObject.getString("distributor_name");
                                    description = jsonObject.getString("product_description");
                                    Stockiest_id = jsonObject.getString("stockiest_id");
                                    stockiest_retailer_name = jsonObject.getString("stockiest_retailer_name");
                                    stockiest_type = jsonObject.getString("type");
                                    stockiest_distance = jsonObject.getString("distance");
                                    stockiest_product_qty = jsonObject.getString("product_quantity");
                                    stockiest_product_price = jsonObject.getString("product_price");

                                    stockiest_detail.add(new Stockiestdetail(Stockiest_id,stockiest_retailer_name,stockiest_distance,
                                            stockiest_product_qty,stockiest_product_price,stockiest_type ));

                                      Log.i("response..!", "product Deatil" + Stockiest_id);
                                      Log.i("response..!", "product Deatil" + stockiest_retailer_name);
                                    //setData(product_name,product_price,product_image,product_quantity,distributor_name);
                                }
                                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,Stockiest_id);



                                stockiestListAdapter = new StockiestListAdapter(ctx,R.layout.stockiest_list_adapter,stockiest_detail);
                                listView.setAdapter(stockiestListAdapter);
                            }
                            else
                            {
                                pd.dismiss();
                                Toast.makeText(ctx,"somthing wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", id);
                params.put("user_lat", String.valueOf(currentlat));
                params.put("user_lon", String.valueOf(currentlong));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params1 = new HashMap<>();
                params1.put("X-API-KEY","TEST@123");
                params1.put("Authorization","Bearer "+ token );
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }





   /* private void createMarker(double lat4, double lon4, final double stockiest_id4) {

        System.out.println("List lat@@@" +lat4);
        System.out.println("List lon@@@" +lon4);
        System.out.println("List stokistid@@@" +stockiest_id4);

        LatLng sydney = new LatLng(dub_lat4,dub_lon4);
        LatLng current = new LatLng(currentlat,currentlong);
        mMap.addMarker(new MarkerOptions().position(sydney).title(String.valueOf(dub_lat4+""+dub_lon4)).snippet(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
        *//*click event on marker*//*
       mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                stokeist_id4= String.valueOf(stockiest_id4);
                System.out.println("Stokiest idMapClick^^^"+stokeist_id4);

                Log.i("Response ..","id"+id);
                Log.i("Response","Stockiest"+stokeist_id4);

                Toast.makeText(ctx, "click on map"+ id + stokeist_id4,Toast.LENGTH_SHORT).show();

                sessionManager.setData(SessionManager.KEY_PRODUCT_ID, id);
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,stokeist_id4);
                sessionManager.setData(SessionManager.KEY_USER_ID,user_id);
                Myalertdialog();
                return true;
            }
        });
    }*/

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
         Gson gson = new Gson();
         json1 = prefs.getString(key, null);

         Type type = new TypeToken<ArrayList<String>>() {}.getType();

         System.out.println("List Json$$$"+json1);
         System.out.println("List Type$$$"+type);
       /* try {
            JSONArray jsonArray=new JSONArray(json);
         //   scores=new ArrayList<>();

             for (int k=0;k<jsonArray.length();k++) {

                String str_arraylist_lat_value= String.valueOf(jsonArray.get(k));
                System.out.println("List JsonLon$$$"+str_arraylist_lat_value);
                forLatLongitude.setLat(str_arraylist_lat_value);

                scores.add(forLatLongitude);
             }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return gson.fromJson(json1, type);
    }
    public ArrayList<String> getArrayList2(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
         json2 = prefs.getString(key, null);

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        System.out.println("List Json$$$"+json2);
        System.out.println("List Type$$$"+type);
       /* try {
            JSONArray jsonArray=new JSONArray(json);
          //  scores=new ArrayList<>();

            for (int k=0;k<jsonArray.length();k++) {


                String str_arraylist_lon_value= String.valueOf(jsonArray.get(k));

                System.out.println("List JsonLon$$$"+str_arraylist_lon_value);
                forLatLongitude.setLon(str_arraylist_lon_value);
                scores.add(forLatLongitude);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        return gson.fromJson(json2, type);
    }
    public ArrayList<String> getArrayList3(String key){
         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
         Gson gson = new Gson();
         json3 = prefs.getString(key, null);

         Type type = new TypeToken<ArrayList<String>>() {}.getType();
         System.out.println("List Json$$$"+json3);
         System.out.println("List Type$$$"+type);
       /*  try {
            JSONArray jsonArray=new JSONArray(json);
           // scores=new ArrayList<>();

            for (int k=0;k<jsonArray.length();k++) {
                String str_arraylist_stokist_value= String.valueOf(jsonArray.get(k));
                forLatLongitude.setStockiest_id(str_arraylist_stokist_value);
                // System.out.println("List JsonLat$$$"+str_arraylist_lat_value);
                System.out.println("List JsonLon$$$"+str_arraylist_stokist_value);
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,str_arraylist_stokist_value);
                scores.add(forLatLongitude);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return gson.fromJson(json3, type);
    }
    public ArrayList<String> getArrayList4(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        json4 = prefs.getString(key, null);

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        System.out.println("List Json$$$"+json4);
        System.out.println("List Type$$$"+type);
       /*  try {
            JSONArray jsonArray=new JSONArray(json);
           // scores=new ArrayList<>();

            for (int k=0;k<jsonArray.length();k++) {
                String str_arraylist_stokist_value= String.valueOf(jsonArray.get(k));
                forLatLongitude.setStockiest_id(str_arraylist_stokist_value);
                // System.out.println("List JsonLat$$$"+str_arraylist_lat_value);
                System.out.println("List JsonLon$$$"+str_arraylist_stokist_value);
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,str_arraylist_stokist_value);
                scores.add(forLatLongitude);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return gson.fromJson(json3, type);
    }
    public ArrayList<String> getArrayList5(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        json5 = prefs.getString(key, null);

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        System.out.println("List Json$$$"+json5);
        System.out.println("List Type$$$"+type);
       /*  try {
            JSONArray jsonArray=new JSONArray(json);
           // scores=new ArrayList<>();

            for (int k=0;k<jsonArray.length();k++) {
                String str_arraylist_stokist_value= String.valueOf(jsonArray.get(k));
                forLatLongitude.setStockiest_id(str_arraylist_stokist_value);
                // System.out.println("List JsonLat$$$"+str_arraylist_lat_value);
                System.out.println("List JsonLon$$$"+str_arraylist_stokist_value);
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,str_arraylist_stokist_value);
                scores.add(forLatLongitude);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        return gson.fromJson(json3, type);
    }




    public ArrayList<HashMap<String, String>> getAllColor(String firstcolor , String secondcolor , String thirdcolor,String fourthcolor,
                                                          String fifthcolor)
    {
        ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> hashmap = new HashMap<String, String>();
        hashmap.put("firstcolor", firstcolor);
        hashmap.put("second color",secondcolor);
        hashmap.put("thirdcolor",thirdcolor);
        hashmap.put("fourthcolor",fourthcolor);
        hashmap.put("fifthcolor",fifthcolor);
        array_list.add(hashmap);

        System.out.println("Array List Size###"+array_list.size());
        System.out.println("Array List HashMapSize###"+hashmap.get("firstcolor"));

        String value=hashmap.get("firstcolor");
        String value2=hashmap.get("second color");
        String value3=hashmap.get("thirdcolor");
        String value4=hashmap.get("fourthcolor");
        String value5=hashmap.get("fifthcolor");
        scores=new ArrayList<>();

        try {
            JSONArray jsonArray5 = new JSONArray(value);
            for (int i = 0; i < jsonArray5.length(); i++) {
                forLatLongitude = new ForLatLongitude();
                forLatLongitude.setLat(value);
                forLatLongitude.setLon(value2);
                forLatLongitude.setStockiest_id(value3);
                forLatLongitude.setProduct_price(value4);
                forLatLongitude.setAvailable_qty(value5);
                scores.add(forLatLongitude);
            }
        }
        catch (JSONException e) {
                e.printStackTrace();
            }


        return array_list;
       /* try {
            JSONArray jsonArray5=new JSONArray(value);
            for (int i=0;i<jsonArray5.length();i++)
            {
                forLatLongitude=new ForLatLongitude();
                String str_arraylist_lat_value= String.valueOf(jsonArray5.get(i));


                forLatLongitude.setStockiest_id(str_arraylist_lat_value);
                // System.out.println("List JsonLat$$$"+str_arraylist_lat_value);
                System.out.println("List JsonLon$$$"+str_arraylist_lat_value);
                JSONArray jsonArray6=new JSONArray(value2);
                for (int k=0;k<jsonArray6.length();k++)

                {
                    String str_arraylist_stokist_value= String.valueOf(jsonArray6.get(i));


                    forLatLongitude.setStockiest_id(str_arraylist_stokist_value);
                    // System.out.println("List JsonLat$$$"+str_arraylist_lat_value);
                    System.out.println("List JsonLon$$$"+str_arraylist_stokist_value);
                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

*/

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        /*LatLng latLng = new LatLng(dub_lat4, dub_lon4);
        MarkerOptions marker = new MarkerOptions().position(latLng).flat(true).anchor(0.5f, 0.5f);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        //    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.addMarker(marker);*/
        // Add a marker in Sydney and move the camera


        System.out.println("List JsonSize###"+scores.size());

        String n = capitalize(name);
        String styledText1 = "<font color='red'>" +scores.size()+ "</font> "+"stockiest/retailer found for"+
                " <font color='red'>" + n + "</font> "+
                "near by your current location";
        show_search_result.setText(Html.fromHtml(styledText1));

        for(int j=0;j<scores.size();j++)
        {
            String lat4=scores.get(j).getLat();
            String lon4=scores.get(j).getLon();
            String stokistid4=scores.get(j).getStockiest_id();
            String product_prize4=scores.get(j).getProduct_price();
            String product_avl_qty4=scores.get(j).getAvailable_qty();

            System.out.println("List JsonDataLat@@@" +lat4);
            System.out.println("List JsonDataLon@@@" +lon4);
            System.out.println("List JsonDatastokist@@@" +stokistid4);
            System.out.println("List scores@@@" +scores.size());

            try {
                 jsonArray6=new JSONArray(lat4);
                 arr_lat=new ArrayList<>();
                  for(k=0;k<jsonArray6.length();k++)
                {
                     str_arraylist_lat_valueeee= String.valueOf(jsonArray6.get(k));
                     arr_lat.add(str_arraylist_lat_valueeee);

                     }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArray7=new JSONArray(lon4);
                arr_lon=new ArrayList<>();
                for(a=0;a<jsonArray7.length();a++)
                {
                    str_arraylist_lon_valueeee= String.valueOf(jsonArray7.get(a));
                    arr_lon.add(str_arraylist_lon_valueeee);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray8=new JSONArray(stokistid4);
                arr_stokist=new ArrayList<>();

                for(int i=0;i<jsonArray8.length();i++)
                {
                    str_arraylist_stokiest_valueeee= String.valueOf(jsonArray8.get(i));
                    arr_stokist.add(str_arraylist_stokiest_valueeee);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray9=new JSONArray(product_prize4);
                arr_product_prize=new ArrayList<>();

                for(int i=0;i<jsonArray9.length();i++)
                {
                    str_arraylist_prodct_prize_valueeee= String.valueOf(jsonArray9.get(i));
                    arr_product_prize.add(str_arraylist_prodct_prize_valueeee);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray10=new JSONArray(product_avl_qty4);
                arr_product_avail_qty=new ArrayList<>();

                for(int i=0;i<jsonArray10.length();i++)
                {
                    str_arraylist_avl_qty_valueeee= String.valueOf(jsonArray10.get(i));
                    arr_product_avail_qty.add(str_arraylist_avl_qty_valueeee);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




          /*  if(str_arraylist_lat_valueeee!=null)
            {
                System.out.println("List JsonDataLat@@@" +str_arraylist_lat_valueeee);
            }
            if(str_arraylist_lon_valueeee!=null)
            {
                System.out.println("List JsonDataLon@@@" +str_arraylist_lon_valueeee);

            }
            if(str_arraylist_stokiest_valueeee!=null)
            {
                System.out.println("List JsonDataStokiest@@@" +str_arraylist_stokiest_valueeee);

            }*/

          /* distance(dub_lat4,dub_lon4,currentlat,currentlong);

            createMarker(scores.get(j).getLat(),scores.get(j).getLon());*/


           // createMarker(dub_lat4,dub_lon4,dub_stokist_id4);

        }
        for(int l=0;l<arr_lat.size();l++)

        {
              dub_lat4 = Double.parseDouble(arr_lat.get(l));
              dub_lon4 = Double.parseDouble(arr_lon.get(l));
              stokeist_id4=arr_stokist.get(l);

              distance(currentlat,currentlong,dub_lat4,dub_lon4);

             /* System.out.println("List maplat@@@" +dub_lat4);
              System.out.println("List maplong@@@" +dub_lon4);
              System.out.println("List mapstocksit@@@" +stokeist_id4);
              System.out.println("List mapprise@@@" +arr_product_prize.get(l));
              System.out.println("List mapqty@@@" +arr_product_avail_qty.get(l));*/

              Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
              try {
                address  = geocoder.getFromLocation(dub_lat4,dub_lon4,1);
                if (address != null && address.size() > 0) {
                    addresss = address.get(0).getAddressLine(0);
                    city = address.get(0).getLocality();
                    country = address.get(0).getCountryName();
                }
                else
                {
                    Toast.makeText(ctx, "No Address Found", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            int height = 60;
            int width = 60;
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.located_at);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            LatLng latLng3 = new LatLng(dub_lat4, dub_lon4);
            MarkerOptions marker2 = new MarkerOptions().position(latLng3)
                    .flat(true).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .title(stokeist_id4)
                    .snippet(String.valueOf(dist));


            CameraUpdate cameraUpdate3 = CameraUpdateFactory.newLatLngZoom(latLng3, 5);
            mMap.animateCamera(cameraUpdate3);
            mMap.addMarker(marker2);
        }

      mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                int position = (int)(marker.getTag());
                String title1= marker.getTitle();

                System.out.println("Position^^^"+title1);
                System.out.println("Stokiest idMapClick^^^"+title1);

                Log.i("Response ..","id"+id);
                Log.i("Response","Stockiest"+title1);

                sessionManager.setData(SessionManager.KEY_PRODUCT_ID, id);
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,title1);
                sessionManager.setData(SessionManager.KEY_USER_ID,user_id);
                Myalertdialog();
                return true;
            }
        });
        //mMap.addMarker(new MarkerOptions().position(current).title("My Location"));
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
         dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        //double totaldstnce = dist/1000;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public void Myalertdialog(){
        try {
            /*getInstance of dialog*/
            mydialog = new Dialog(ctx);
            mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mydialog.setContentView(R.layout.dialog_details);

            /*initializing all the views of dialog */
            tv_Stockiest_name = (TextView)mydialog.findViewById(R.id.Stockiest_name);
            tv_stockretail = (TextView)mydialog.findViewById(R.id.tv_stockiestretailer);
            tv_distance_kms = (TextView)mydialog.findViewById(R.id.tv_distance);
            user_location = (TextView)mydialog.findViewById(R.id.userlocation);
            tv_product_name = (TextView)mydialog.findViewById(R.id.product_name);
            tv_ProductPrice = (TextView)mydialog.findViewById(R.id.ProductPrice);
            tv_Product_Availability = (TextView)mydialog.findViewById(R.id.Availability);
            tv_specification = (TextView)mydialog.findViewById(R.id.more);
            edt_qty = (EditText)mydialog.findViewById(R.id.enterqty);
            cutImage = (ImageView)mydialog.findViewById(R.id.hideDialog);
            addtocart = (Button)mydialog.findViewById(R.id.addincart);
            cart_item_count = (TextView)getActivity().findViewById(R.id.item_counte1);
            /*geting data from sharedprefrence*/
           // token = sessionManager.getData(SessionManager.KEY_TOKEN);
            product_id = sessionManager.getData(SessionManager.KEY_PRODUCT_ID);
            Stockiest_Idd = sessionManager.getData(SessionManager.KEY_STOCKIEST_ID);
            user_id = sessionManager.getData(SessionManager.KEY_USER_ID);

            System.out.println("Stockiest_idd^^^"+Stockiest_Idd);

            product_quntity = "0";
            setTvAdapter2(Stockiest_Idd);

            loadBuisnessData(product_id,Stockiest_Idd);


            /*adding product data to textview */
            /*user can see product detail and add to cart the particular item*/
            //tv_Stockiest_name.setText(distributor_name.toString());

             //DecimalFormat df2 = new DecimalFormat(".##");
             //tv_distance_kms.setText(df2.format(dist) +"km away from your current location");

            //tv_product_name.setText(product_name.toString());
             //user_location.setText(addresss + " " + city + " " + country);

            //tv_ProductPrice.setText(product_price);

           /* if (this.product_quantity.toString().equals("0"))
            {
                tv_Product_Availability.setText("out of stock");
                 edt_qty.setEnabled(false);
            }
            else {
                tv_Product_Availability.setText(product_quantity.toString()+"Unit Available");
            }*/

             /*click listner for add item to cart*/
            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ProgressDialog pd= new  ProgressDialog(ctx);
                    pd.setMessage("Loading..");
                    pd.setCancelable(false);
                    pd.show();
                    addtocart.setClickable(false);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            pd.dismiss();
                            System.out.println("O Value of product quantity@@@"+product_quntity);

                    if (product_quntity==null)
                    {
                        product_quntity ="0";
                        loadAddCart(Integer.parseInt(product_quntity));
                    }
                    else
                    {
                        loadAddCart(Integer.parseInt(product_quntity));

                    }
                        }
                    }, 2000);
                    // loadAddCart();
                    }
            });

            cutImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydialog.dismiss();
                }
            });

            /*click listner for more details of product*/
            tv_specification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionManager.setData(SessionManager.KEY_LATITUDE, String.valueOf(currentlat));
                    sessionManager.setData(SessionManager.KEY_LONGITUDE, String.valueOf(currentlong));
                    sessionManager.setData(SessionManager.KEY_PRODUCT_ID, id);
                    sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,Stockiest_id);
                    sessionManager.setData(SessionManager.KEY_USER_ID,user_id);
                     mydialog.dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new DetailActivity())
                            .addToBackStack("2")
                           .commit();

                    //Intent intent = new Intent(ctx,DetailActivity.class);
                    //ctx.startActivity(intent);
                }
            });
        }
        catch (Exception e)
        {
            Log.i("error","eroro..!!"+ e.getMessage());
            e.printStackTrace();
        }
       mydialog.show();
    }

    private void setTvAdapter2(final String stockiest_Idd) {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Loading..");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    Log.i("response..!", "Cart Detail%%%" + response);

                    //Toast.makeText(ctx, "resssss"+response, Toast.LENGTH_SHORT).show();
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success") || msg.equals("your cart items!")) {

                        JSONObject jsonObject = obj.getJSONObject("result");

                        String cart_id = jsonObject.getString("id");
                        String product_id = jsonObject.getString("product_id");
                        String stockies_id = jsonObject.getString("stockiest_id");
                        String product_name = jsonObject.getString("product_name");
                        String product_price = jsonObject.getString("product_price");
                        String product_image = jsonObject.getString("product_image");
                        product_quntity = String.valueOf(jsonObject.getInt("product_qty"));
                        String product_desc = jsonObject.getString("product_description");
                        String available_qty = jsonObject.getString("available_qty");


                        // search_list.add(new CartList(cart_id, product_id, stockies_id, product_name,
                        //  product_price, product_image, product_quantity, product_desc, product_sku, available_qty));
                    } else if (Status.equals("failed")) {
                        pd.dismiss();
                        //Toast.makeText(ctx, "Cart Is Empty.!", Toast.LENGTH_SHORT).show();

                    } else {
                        pd.dismiss();
                        Toast.makeText(ctx, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ctx, "Some Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("product_id",id);
                params.put("stockiest_id",stockiest_Idd);

                Log.i("respo","uid"+user_id);
                Log.i("respo","pid"+id);
                Log.i("respo","sid"+ stockiest_Idd);

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("X-API-KEY", "TEST@123");
                params1.put("Authorization", "Bearer " + token);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);

    }

    private void loadBuisnessData(final String product_id, final String stockiest_Idd)
    {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PRODUCT_DETAIL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.i("response..!","product Deatil"+ response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");
                    distance = obj.getString("distance");
                    solid_qunty = obj.getString("sold_qty");

                    if (Status.equals("success")||msg.equals("product details")) {

                         JSONObject jsonObject = obj.getJSONObject("result");
                         Product_id = jsonObject.getString("product_id");
                         product_sku = jsonObject.getString("product_sku");
                         product_name = jsonObject.getString("product_name");
                         product_image = jsonObject.getString("image_url");
                         //distributor_name = jsonObject.getString("distributor_name");
                         description = jsonObject.getString("product_description");
                         product_quantity = jsonObject.getString("product_quantity");
                         product_price = jsonObject.getString("product_price");
                         product_price = jsonObject.getString("product_price");
                         Stockiest_id = jsonObject.getString("stockiest_id");
                         type =jsonObject.getString("type");

                         String stockiest_name;
                        JSONObject jsonObject1 = obj.getJSONObject("stockiest");
                        stockiest_name = jsonObject1.getString("stockiest_retailer_name");

                        if (type.equals("stockiest"))
                        {
                            tv_stockretail.setText("Stockiest Name:");
                            tv_Stockiest_name.setText(stockiest_name);
                        }
                        else {
                            tv_stockretail.setText("Retailer Name:");
                            tv_Stockiest_name.setText(stockiest_name);
                            }

                        String n = capitalize(product_name);
                        tv_product_name.setText(n);


                        if (!solid_qunty.equals("null")) {

                            q = Integer.parseInt(product_quantity) - Integer.parseInt(solid_qunty);
                            if (q == 0) {
                                tv_Product_Availability.setText("Out Of Stock");
                                edt_qty.setEnabled(false);
                                addtocart.setClickable(false);
                            } else {
                                tv_Product_Availability.setText(q + " Unit Available");
                            }
                        }
                        else
                        {
                           q = Integer.parseInt(String.valueOf(Integer.parseInt(product_quantity) - 0));
                            tv_Product_Availability.setText(q + " Unit Available");
                            //add_item_number.setEnabled(false);
                        }


                        /*int q = Integer.parseInt(product_quantity)-Integer.parseInt(solid_qunty);
                        if (q==0)
                        {
                            tv_Product_Availability.setText("Out Of Stock");
                             edt_qty.setEnabled(false);
                        }
                        else {
                            tv_Product_Availability.setText(q+" Unit Available");
                        }*/

                        tv_ProductPrice.setText(product_price);
                        tv_distance_kms.setText(distance +" away from your current location");


                        double Stock_lat,Stock_long;
                        JSONObject jsonstockiestObject = obj.getJSONObject("stockiest");
                        Stock_lat = Double.parseDouble(jsonstockiestObject.getString("lat"));
                        Stock_long = Double.parseDouble(jsonstockiestObject.getString("lon"));

                        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
                        try {
                            address  = geocoder.getFromLocation(Stock_lat,Stock_long,1);
                            if (address != null && address.size() > 0) {
                                addresss = address.get(0).getAddressLine(0);
                                city = address.get(0).getLocality();
                                country = address.get(0).getCountryName();
                            }
                            else
                            {
                                Toast.makeText(ctx, "No Address Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        user_location.setText(addresss+" "+city);

                        Log.i("response..!","product Deatil"+ product_name);
                        Log.i("response..!","product Deatil"+ product_price);
                        Log.i("response..!","product Deatil"+ product_quantity);
                        //setData(product_name,product_price,product_image,product_quantity,distributor_name);
                    }
                    else
                    {
                        Toast.makeText(ctx,"somthing wrong",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "some error", Toast.LENGTH_SHORT).show();
            }
        })

           {
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> params = new HashMap<>();

                   params.put("product_id", product_id);
                   params.put("stockiest_id", stockiest_Idd);
                   params.put("user_lat", String.valueOf(currentlat));
                   params.put("user_lon", String.valueOf(currentlong));
                  /*params.put("address", address);
                   params.put("city_id", city);
                   params.put("country_id", country);*/
                   return params;
               }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params1 = new HashMap<>();
                params1.put("X-API-KEY","TEST@123");
                params1.put("Authorization","Bearer "+ token );

                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    private void loadAddCart(int i) {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        Log.i("response..!","add_to_card"+ user_id);
        Log.i("response..!","add_to_card"+ product_id);
        Log.i("response..!","add_to_card"+ product_name);
        Log.i("response..!","add_to_card"+ Stockiest_id);
        Log.i("response..!","add_to_card"+ stockiest);

        final String quantity = edt_qty.getText().toString();

        if (TextUtils.isEmpty(quantity))
        {
            pd.dismiss();
            edt_qty.setError("please enter quantity");
            edt_qty.requestFocus();
            addtocart.setClickable(true);
            return;

        }

        if (Integer.parseInt(quantity)>q)
        {
            pd.dismiss();
            Toast.makeText(ctx, "only "+ q +" quantity available!", Toast.LENGTH_SHORT).show();
            edt_qty.setError("please enter valid quantity");
            edt_qty.requestFocus();
            addtocart.setClickable(true);
            return;
        }

        int u_q = Integer.parseInt(quantity);
        int o = i + u_q  ;


        try
        {
            System.out.println("O Value is@@@"+o);
            System.out.println("Quant Value is@@@"+i);
            System.out.println("User Value is@@@"+u_q);

            if (o > q && o!= q)
            {
                pd.dismiss();
                edt_qty.setError("You cannot add more quantity than available stock");
                edt_qty.requestFocus();
                addtocart.setClickable(true);

                return;

            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }



         /*   if (o >= q)
        {
            pd.dismiss();
            edt_qty.setError("You cannot add more quantity than available stock");
            edt_qty.requestFocus();
            return;
        }*/


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDTOCART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    Log.i("response..!","add_to_card%%%"+ response);
                    //sessionManager.setData(SessionManager.KEY_CART_DATA,response);

                    //converting response to json object
                    JSONObject obj = new JSONObject(response);
                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")|| msg.equals("product added to cart successfully!"))
                    {
                        Toast.makeText(ctx,"The product has been added to your cart.",Toast.LENGTH_SHORT).show();
                        setTvAdapter2(Stockiest_Idd);
                        addtocart.setClickable(true);
                        if(bool_check_data==false)
                        {
                            setTvAdapter();
                        }
                    }
                    else if(Status.equals("failed"))
                    {
                        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ctx,"Server Error",Toast.LENGTH_SHORT).show();
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("product_sku",product_sku);
                params.put("product_id",product_id);
                params.put("product_name", product_name);
                params.put("stockiest_id", Stockiest_Idd);
                params.put("product_price", product_price);
                params.put("product_qty", quantity);
                params.put("available_qty", String.valueOf(q));
                params.put("product_description",description);
                params.put("product_image",product_image);

               Log.i("response..!","add_to_card"+ user_id);
               Log.i("response..!","add_to_card"+ product_id);
                Log.i("response..!","add_to_card"+ product_name);
                Log.i("response..!","add_to_card"+ Stockiest_id);
                Log.i("response..!","add_to_card"+ stockiest);



                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params1 = new HashMap<>();
                params1.put("X-API-KEY","TEST@123");
                params1.put("Authorization","Bearer "+ token );
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }



    private void setTvAdapter() {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Loading..");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    Log.i("response..!", "Cart Detail%%%" + response);
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success") || msg.equals("your cart items!")) {

                        JSONArray array = obj.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String cart_id = jsonObject.getString("id");
                            String product_id = jsonObject.getString("product_id");
                            String stockies_id = jsonObject.getString("stockiest_id");
                            String product_name = jsonObject.getString("product_name");
                            String product_price = jsonObject.getString("product_price");
                            String product_image = jsonObject.getString("product_image");
                             product_quntity = String.valueOf(jsonObject.getInt("product_qty"));
                            String product_desc = jsonObject.getString("product_description");
                            String available_qty = jsonObject.getString("available_qty");

                            Log.i("result", "12233" + product_name);

                            search_list.add(new CartList(cart_id, product_id, stockies_id, product_name,
                                    product_price, product_image, product_quntity, product_desc, product_sku, available_qty));
                        }
                        bool_check_data=true;
                        String item = String.valueOf(search_list.size());
                        cart_item_count.setText(item);
                        setView(search_list);

                    } else if (Status.equals("failed")) {
                        pd.dismiss();
                        //Toast.makeText(ctx, "Cart Is Empty.!", Toast.LENGTH_SHORT).show();

                    } else {
                        pd.dismiss();
                        Toast.makeText(ctx, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("X-API-KEY", "TEST@123");
                params1.put("Authorization", "Bearer " + token);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);

    }

    private void setView(ArrayList<CartList> search_list) {
        //Toast.makeText(ctx, "Seearc"+ search_list.size(), Toast.LENGTH_SHORT).show();
        sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(search_list.size()));
    }


   /* public void updateCheckOutSize() {
        Linear_map.setVisibility(View.GONE);
        Linear_list.setVisibility(View.VISIBLE);
    }
*/

    public class StockiestListAdapter extends BaseAdapter{

        ArrayList<Stockiestdetail> arrayList;
        Context context;
        int q;
        public StockiestListAdapter(Context ctx, int stockiest_list_adapter,
                                    ArrayList<Stockiestdetail> stockiest_detail) {
            context = ctx;
            arrayList = stockiest_detail;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.adp_shoplistview, parent, false);
            }

            // get the TextView for item name and item description
           /* TextView textViewserialNo = (TextView)
                    convertView.findViewById(R.id.serialNo);*/
            TextView textViewstockiest_name = (TextView)
                    convertView.findViewById(R.id.stockiest_name);
            TextView textViewItemkmsfrom = (TextView)
                    convertView.findViewById(R.id.kmsfrom);
            TextView textViewpriceandqty = (TextView)
                    convertView.findViewById(R.id.priceandqty);
            TextView textViewqty = (TextView)
                    convertView.findViewById(R.id.qty);
            TextView textViewtype = (TextView)
                    convertView.findViewById(R.id.stockiest_type);
            System.out.println("List arraylat@@@" +arrayList.size());


            String n = capitalize(arrayList.get(position).getStockiest_type());
            textViewtype.setText(n);
            textViewstockiest_name.setText(arrayList.get(position).getRetailer_Name()+",");
            textViewItemkmsfrom.setText(" "+arrayList.get(position).getStockiest_distance()+" km away");
            textViewpriceandqty.setText(arrayList.get(position).getStockiest_price());

              q = Integer.parseInt(arrayList.get(position).getStockiest_qty());

              if (q==0)
              {
                  textViewqty.setText("Out Of Stock");

              }
              else {
                  textViewqty.setText(arrayList.get(position).getStockiest_qty()+" Unit Available");
              }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionManager.setData(SessionManager.KEY_LATITUDE, String.valueOf(currentlat));
                    sessionManager.setData(SessionManager.KEY_LONGITUDE, String.valueOf(currentlong));
                    sessionManager.setData(SessionManager.KEY_PRODUCT_ID, id);
                    sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,arrayList.get(position).getStockiest_id());
                    sessionManager.setData(SessionManager.KEY_USER_ID,user_id);
                    sessionManager.setData(SessionManager.KEY_LST_SIZE, String.valueOf(scores.size()));

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new DetailActivity())
                            .addToBackStack("3")
                            .commit();


                   // Intent intent = new Intent(ctx,DetailActivity.class);
                   // ctx.startActivity(intent);
                }
            });




            /*for(int j=0;j<arrayList.size();j++)
            {
                String lat4=arrayList.get(j).getLat();
                String lon4=arrayList.get(j).getLon();
                String stokistid4=arrayList.get(j).getStockiest_id();
                String product_prize4=arrayList.get(j).getProduct_price();
                String product_avl_qty4=arrayList.get(j).getAvailable_qty();

                System.out.println("List JsonDataLat@@@" +lat4);
                System.out.println("List JsonDataLon@@@" +lon4);
                System.out.println("List JsonDatastokist@@@" +stokistid4);
                System.out.println("List scores@@@" +arrayList.size());

                try {
                    jsonArray6=new JSONArray(lat4);
                    arr_lat=new ArrayList<>();
                    for(k=0;k<jsonArray6.length();k++)
                    {
                        str_arraylist_lat_valueeee= String.valueOf(jsonArray6.get(k));
                        arr_lat.add(str_arraylist_lat_valueeee);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonArray7=new JSONArray(lon4);
                    arr_lon=new ArrayList<>();
                    for(a=0;a<jsonArray7.length();a++)
                    {
                        str_arraylist_lon_valueeee= String.valueOf(jsonArray7.get(a));
                        arr_lon.add(str_arraylist_lon_valueeee);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray jsonArray8=new JSONArray(stokistid4);
                    arr_stokist=new ArrayList<>();

                    for(int i=0;i<jsonArray8.length();i++)
                    {
                        str_arraylist_stokiest_valueeee= String.valueOf(jsonArray8.get(i));
                        arr_stokist.add(str_arraylist_stokiest_valueeee);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray jsonArray9=new JSONArray(product_prize4);
                    arr_product_prize=new ArrayList<>();

                    for(int i=0;i<jsonArray9.length();i++)
                    {
                        str_arraylist_prodct_prize_valueeee= String.valueOf(jsonArray9.get(i));
                        arr_product_prize.add(str_arraylist_prodct_prize_valueeee);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray jsonArray10=new JSONArray(product_avl_qty4);
                    arr_product_avail_qty=new ArrayList<>();

                    for(int i=0;i<jsonArray10.length();i++)
                    {
                        str_arraylist_avl_qty_valueeee= String.valueOf(jsonArray10.get(i));
                        arr_product_avail_qty.add(str_arraylist_avl_qty_valueeee);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }*/

            /*for(int l=0;l<arrayList.size();l++)
            {
                dub_lat4 = Double.parseDouble(arr_lat.get(l));
                dub_lon4 = Double.parseDouble(arr_lon.get(l));
                stokeist_id4=arr_stokist.get(l);

                String Prize =  arr_product_prize.get(l);
                String product_avail_qty =  arr_product_avail_qty.get(l);

                String styledText =  arr_product_prize.get(l) +"("+
                        arr_product_avail_qty.get(l)+" Units available)";

                System.out.println("List maplat@@@" +dub_lat4);
                System.out.println("List maplong@@@" +dub_lon4);
                System.out.println("List mapstocksit@@@" +stokeist_id4);
                System.out.println("List mapprise@@@" + Prize);
                System.out.println("List mapprise@@@" + distributor_name);
                System.out.println("List mapprise@@@" + product_avail_qty);


                DecimalFormat df2 = new DecimalFormat(".##");
                distance(currentlat,currentlong,dub_lat4,dub_lon4);
                textViewItemkmsfrom.setText(df2.format(dist)+" km aaway");

                textViewserialNo.setText(""+(position+1));



                textViewpriceandqty.setText(Html.fromHtml(styledText));

                textViewstockiest_name.setText(distributor_name);


            }

*/
           /* //sets the text for item name and item description from the current item object
            for(int j=0;j<arrayList.size();j++) {

                Log.i("Value of element "+j, String.valueOf(arrayList.get(j)));

                textViewserialNo.setText(""+(position+1));
                String styledText =  arrayList.get(position).getProduct_price() +"("+
                        arrayList.get(position).getAvailable_qty()+"Units available)";
                textViewpriceandqty.setText(Html.fromHtml(styledText));
                textViewstockiest_name.setText(distributor_name);
                textViewItemkmsfrom.setText("3.4 km aaway");
            }*/
            return convertView;
        }
    }

   /* private void setData(String product_name, String product_price, String product_image,
                         String product_quantity, String distributor_name) {

        tv_Stockiest_name.setText(distributor_name);

        tv_distance_kms.setText(product_name);

       tv_product_name.setText(product_name);

       tv_ProductPrice.setText(product_price);

        if (this.product_quantity.equals("0"))
        {
            tv_Product_Availability.setText("out of stock");
            edt_qty.setEnabled(false);
        }
        else {
            tv_Product_Availability.setText(product_quantity+"Unit Available");
        }
       *//* String url = product_image;
        if (!url.equals("")) {
            String icon_url = "http://192.168.1.4/webservices/uploads/stockiest_"+ stockiest+"/" + url;
            icon_url = icon_url.replace(" ","%20");
            Log.i("response..!","icon url"+ icon_url);
            Picasso.with(ctx)
                    .load(icon_url)
                    .resize(100, 100)
                    .placeholder(R.drawable.ic_new_web_logo)
                    .error(R.drawable.ic_new_web_logo)
                    .into(cutImage);
        }*//*
    }*/

    private void checkForPermission() {
        int permissionCheckForCamera = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckForGallery = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckForAccessCamera = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int permissionCheckForAccessFinelocation = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckForAccessCoarselocation = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionCheckForAccessFinelocation != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForAccessCoarselocation != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{/*android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,*/
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1001);

            Intent intent = new Intent(ctx,RecyclerViewActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("", "Permission callback called-------");
        switch (requestCode) {
            case 1001: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("", "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("Location Services Permission must required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkForPermission();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                  //  .show();

                            showDialogOK("Location Services Permission must required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getActivity().getPackageName(), null)));

                                        }

                                    });


                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(ctx,RecyclerViewActivity.class);
        startActivity(intent);
    }*/

    private String capitalize(String capString){
       StringBuffer capBuffer = new StringBuffer();
       Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
       while (capMatcher.find()){
           capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
       }

       return capMatcher.appendTail(capBuffer).toString();
   }

}
