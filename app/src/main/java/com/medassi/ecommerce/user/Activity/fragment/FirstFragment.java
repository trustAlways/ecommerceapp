package com.medassi.ecommerce.user.Activity.fragment;


import android.Manifest;
//import android.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.medassi.ecommerce.user.Activity.Activity.LoginActivity;
import com.medassi.ecommerce.user.Activity.Activity.MapsActivity;
import com.medassi.ecommerce.user.Activity.Activity.MyCartActivty;
import com.medassi.ecommerce.user.Activity.Activity.RecyclerViewActivity;
import com.medassi.ecommerce.user.Activity.Activity.UpdateProfile;
import com.medassi.ecommerce.user.Activity.Model.CartList;
import com.medassi.ecommerce.user.Activity.Model.ForLatLongitude;
import com.medassi.ecommerce.user.Activity.Model.Navigation;
import com.medassi.ecommerce.user.Activity.Model.for_map_stokistshow.For_Map_Stokistshowbean;
import com.medassi.ecommerce.user.Activity.Model.for_map_stokistshow.For_Map_XStokistshowbean;
import com.medassi.ecommerce.user.Activity.Model.product;
import com.medassi.ecommerce.user.Activity.adapter.CustomAdapter;
import com.medassi.ecommerce.user.Activity.app.Config;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.GPSTracker;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstFragment extends Fragment {

    RecyclerView recyclerView;
    EditText editTextversion;
    List<For_Map_XStokistshowbean> version;
    ArrayList<Navigation> nav_list;
    ConnectionDetector connectionDetector;
    ListView listView;
    Button btn_go;
    SessionManager sessionManager;
    CustomAdapter adapter;
    DrawerLayout drawerLayout;
    String Item;

    TextView name_tv, mobile_tv, userlocation, item_count, nosearchdata, dearuser;
    ImageView menu_icon, user_account, located_at, user_cart, crossImage;
    ArrayList<CartList> search_list;
    List<Address> address;
    Context ctx;
    ProgressDialog pd;
    String token, cart_item, user_id;
    LocationManager mlocationmanager;
    LocationListener mLocationListener;
    double currentlat, currentlong;
    Geocoder geocoder;
    For_Map_Stokistshowbean for_map_stokistshowbean;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String name, lastnm, mobile, email, billing_add_one, billing_add_two, shipping_add_one, shipping_add_two;


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.firstfragment, null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("Search Products");

        version = new ArrayList<>();
        search_list = new ArrayList<CartList>();

        sessionManager = new SessionManager(getActivity());
        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);

        setCartItem();
        initxml();
        checkForPermission();

        setCount();
        getLocation();

        clickEvent();
        registerReceiver();

        boolean internet = connectionDetector.isConnected(getActivity());
        if (internet) {
            Boolean login = sessionManager.isLoggedIn();
            if (login) {
                loadSearchingData();
            } else {
                Toast.makeText(getActivity(), "Check Your Login Status..!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        } else {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
        }
       // setData();
        return view;
    }


    private void initxml() {

        pd = new ProgressDialog(getActivity());
        connectionDetector = new ConnectionDetector();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        name_tv = (TextView) view.findViewById(R.id.tv_username);
        dearuser = (TextView) view.findViewById(R.id.usernametv);
        //mobile_tv = (TextView) findViewById(R.id.tv_nav_mobile);
        editTextversion = (EditText) view.findViewById(R.id.editTextversion);
        nosearchdata = (TextView) view.findViewById(R.id.no_search_data);
        btn_go = (Button) view.findViewById(R.id.buttonGo);
        item_count = (TextView) getActivity().findViewById(R.id.item_counte1);


        located_at = (ImageView) view.findViewById(R.id.locted_at);
        Animation pulse = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);
        located_at.startAnimation(pulse);
        userlocation = (TextView) view.findViewById(R.id.userlocation);

        //sessionManager = new SessionManager(RecyclerViewActivity.this);
        //token = sessionManager.getData(SessionManager.KEY_TOKEN);

       /* cart_item = sessionManager.getData(SessionManager.KEY_CART_ITEM_COUNT);
        if (cart_item!=null)
        {
            item_count.setText(cart_item);
        }
        else
        {
            item_count.setText("0");
        }*/


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //adapter = new CustomAdapter(RecyclerViewActivity.this,version);
        //recyclerView.setAdapter(adapter);

        /*recyclerView.addOnItemTouchListener(
                new MainActivity.RecyclerItemClickListener(ctx, recyclerView, new MainActivity.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Toast.makeText(ctx, "postion"+ position, Toast.LENGTH_SHORT).show();
                        sessionManager.setData(SessionManager.KEY_PRODUCT_ID, search_list.get(position).getId());
                        sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,search_list.get(position).getStockiest_id());
                        Intent intent = new Intent(RecyclerViewActivity.this,DetailActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/

        editTextversion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    if (version.size() > 0) {
                        filter(charSequence.toString());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        nosearchdata.setVisibility(View.GONE);
                    } else {
                        nosearchdata.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                    }
                } else {
                    nosearchdata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void clickEvent() {
       /*listView.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
                menu_icon.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_in_right));
            }
        });
        user_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
                user_account.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_up));
            }
        });


        user_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_cart.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down));
                Intent intent = new Intent(getActivity(), MyCartActivty.class);
                startActivity(intent);
            }
        });

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
*/
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nosearchdata.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

    }

    private void registerReceiver() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    // Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    Log.e("msg", "Firebase msg: " + message);

                    //txtMessage.setText(message);
                }
            }
        };
        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("idd", "Firebase reg id: " + regId);
        /*if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");*/
    }


   /* private void setData() {

        String fname = sessionManager.getData(SessionManager.KEY_FIRST_NAME);
        String lname = sessionManager.getData(SessionManager.KEY_LAST_NAME);
        name_tv.setText("Welcome " + fname + " " + lname);
        dearuser.setText("Dear " + fname + ",");

    }
*/
    private void filter(String text) {
        List<For_Map_XStokistshowbean> filterdNames = new ArrayList<>();

        for (For_Map_XStokistshowbean s : version) {
            if (s.getProductName().toLowerCase(Locale.getDefault()).contains(text)) {
                filterdNames.add(s);
            }
        }

        if (filterdNames != null) {
            adapter.filterList(filterdNames);
        } else {
            nosearchdata.setVisibility(View.GONE);
        }
        //adapter.notifyItemChanged(po);

    }


    private void setCount() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading..");
        // pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_ACCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //print the response from server
                            Log.i("response..!","1235"+response);

                            String  Status = obj.getString("status");
                            String msg = obj.getString("message");

                            //if no error in response
                            if (Status.equals("success")||msg.equals("user profile details"))
                            {
                                JSONObject jsonObject = obj.getJSONObject("result");
                                name = jsonObject.getString("first_name");
                                lastnm = jsonObject.getString("last_name");
                                email = jsonObject.getString("email");
                                mobile = jsonObject.getString("phone_number");
                                billing_add_one = jsonObject.getString("billing_address_one");
                                billing_add_two = jsonObject.getString("billing_address_two");
                                shipping_add_one = jsonObject.getString("shipping_address_one");
                                shipping_add_two = jsonObject.getString("shipping_address_two");



                                setProfile(name,lastnm,email,mobile,billing_add_one,billing_add_two,shipping_add_one,
                                        shipping_add_two);
                            }
                            else if (Status.equals("failed"))
                            {
                                //Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        //Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,//
        //     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//
        //     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//
        requestQueue.add(stringRequest);

    }

    private void setProfile(String name, String lastnm, String email, String mobile,
                            String billing_add_one, String billing_add_two, String shipping_add_one,
                            String shipping_add_two) {

        sessionManager.setData(SessionManager.KEY_BILL_1,billing_add_one);
        sessionManager.setData(SessionManager.KEY_BILL_2,billing_add_two);
        sessionManager.setData(SessionManager.KEY_SHIPP_1,shipping_add_one);
        sessionManager.setData(SessionManager.KEY_SHIPP_2,shipping_add_two);

        Log.i("response..!","print"+billing_add_one);
        Log.i("response..!","print"+billing_add_two);
        Log.i("response..!","print"+shipping_add_one);
        Log.i("response..!","print"+shipping_add_two);

    }

    private void getLocation() {

        GPSTracker gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            double currentlat = gps.getLatitude();
            double currentlong = gps.getLongitude();

            geocoder = new Geocoder(getActivity(),Locale.getDefault());
            try {
                address  = geocoder.getFromLocation(currentlat,currentlong,1);
                if (address != null && address.size() > 0) {
                    String addresss = address.get(0).getAddressLine(0);
                    String city = address.get(0).getLocality();
                    String country = address.get(0).getCountryName();

                    sessionManager.setData(SessionManager.KEY_USER_CURRENT_ADD, String.valueOf(address));

                    userlocation.setText(addresss + " " + city);
                    located_at.clearAnimation();
                }
                else
                {
                    Toast.makeText(getActivity(), "No Address Found.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //sessionManager.setDouble(SessionManager.KEY_LAT, latitude);
            //sessionManager.setDouble(SessionManager.KEY_LONG, longitude);


        } else {
            gps.showSettingsAlert();
        }
    }

    private void loadSearchingData() {
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        Log.i("response","Recycler..."+response);
                        Log.i("response","token..."+ token);
                        sessionManager.setData(SessionManager.KEY_SEARCH_DATA, response);
                        setTvAdapter();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (getActivity()!=null)
                        {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params1 = new HashMap<>();
                params1.put("X-API-KEY","TEST@123");
                params1.put("Authorization","Bearer "+ token );
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void setTvAdapter() {
        pd.setMessage("Loading..!");
        pd.show();
        String response = sessionManager.getData(SessionManager.KEY_SEARCH_DATA);
        try {
            pd.dismiss();
            JSONObject object = new JSONObject(response);

            Log.i("response..!!", "123" + response);
            String  Status = object.getString("status");
            String msg = object.getString("message");


            if (Status.equals("success")) {
                Gson gson=new Gson();

                for_map_stokistshowbean=gson.fromJson(response,For_Map_Stokistshowbean.class);
                adapter=new CustomAdapter(getActivity(),for_map_stokistshowbean.getResult());
                System.out.println("Rsult^^^"+for_map_stokistshowbean.getResult().get(0).getProductName());
                version=for_map_stokistshowbean.getResult();

                //version=for_map_stokistshowbean.getResult();

                /*JSONArray array = object.getJSONArray("result");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String id = jsonObject.getString("product_id");
                    //String distributor_name = jsonObject.getString("distributor_name");
                    String name = jsonObject.getString("product_name");

                    String stockiest_id=null ;
                    JSONArray jsonArraystockiest_id = jsonObject.getJSONArray("stockiest_id");
                    for (int j = 0; j < jsonArraystockiest_id.length(); j++) {
                        stockiest_id = String.valueOf(jsonArraystockiest_id.getInt(j));
                        Log.i("Responseeee", "Recycler" + stockiest_id);
                    }

                    String price = null;
                    JSONArray jsonArrayprice = jsonObject.getJSONArray("product_price");
                    for (int k = 0; k < jsonArrayprice.length();k++) {
                        price = jsonArrayprice.getString(k);

                    }

                    String description = jsonObject.getString("product_description");
                    String product_image = jsonObject.getString("product_image");

                    String Latitude = null;
                    JSONArray jsonArraylet= jsonObject.getJSONArray("let");
                    for (int l = 0; l < jsonArraylet.length(); l++) {
                        Latitude = jsonArraylet.getString(l);
                        }

                        String Longitude = null;
                    JSONArray jsonArraylong= jsonObject.getJSONArray("long");
                    for (int l = 0; l < jsonArraylong.length(); l++) {
                        Longitude = jsonArraylong.getString(l);


                    }

                    String available_qty = null;
                    JSONArray jsonArrayqty= jsonObject.getJSONArray("available_qty");
                    for (int l = 0; l < jsonArrayqty.length(); l++) {
                        available_qty = jsonArrayqty.getString(l);


                    }


                    version.add(new product(name, id, stockiest_id, Longitude, Latitude));
                    search_list.add(new Search(name, id, available_qty, price, stockiest_id,
                            product_image, Longitude, Latitude,description));

                    Log.i("Responseeee", "Recycler" + search_list.size());

                }*/
            }
            else if(Status.equalsIgnoreCase("failed")) {
                pd.dismiss();
              //  String msg1 = object.getString("message");
               // String msgg = capitalize(msg1);
               //Toast.makeText(getActivity(), msg1, Toast.LENGTH_SHORT).show();
                nosearchdata.setVisibility(View.VISIBLE);
            }
            else {
                String msgg = capitalize(msg);
                Toast.makeText(getActivity(), msgg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void setCartItem() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
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
                            String product_quantity = String.valueOf(jsonObject.getInt("product_qty"));
                            String product_desc = jsonObject.getString("product_description");
                            String available_qty = jsonObject.getString("available_qty");
                            String product_sku = jsonObject.getString("product_sku");

                            Log.i("result", "12233" + product_name);

                            search_list.add(new CartList(cart_id, product_id, stockies_id, product_name,
                                    product_price, product_image, product_quantity, product_desc, product_sku, available_qty));

                        }

                        Item= String.valueOf(search_list.size());
                        item_count.setText(Item);
                        setView(search_list);

                    } else if (Status.equals("failed")) {
                        pd.dismiss();
                        //Toast.makeText(, "Cart Is Empty.!", Toast.LENGTH_SHORT).show();

                    } else {
                        pd.dismiss();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //Toast.makeText(g, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    private void setView(ArrayList<CartList> search_list) {
        //Toast.makeText(ctx, "Seearc"+ search_list.size(), Toast.LENGTH_SHORT).show();
        sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(search_list.size()));
    }


    private void checkForPermission() {
        int permissionCheckForCamera = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckForGallery = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckForAccessCamera = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int permissionCheckForAccessFinelocation = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckForAccessCoarselocation = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheckForCallPhone = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

        if (permissionCheckForCamera != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForGallery != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForAccessCamera != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForAccessFinelocation != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForCallPhone != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForAccessCoarselocation != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.CALL_PHONE,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1001);
        }

    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public class CustomAdapter extends RecyclerView.Adapter<com.medassi.ecommerce.user.Activity.adapter.CustomAdapter.ViewHolder> {

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
            if (context!=null)
            {
                sessionManager = new SessionManager(context);
            }

        }


        @Override
        public com.medassi.ecommerce.user.Activity.adapter.CustomAdapter.ViewHolder
        onCreateViewHolder(final ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_layout, parent, false);

            return new com.medassi.ecommerce.user.Activity.adapter.CustomAdapter.ViewHolder(v);
        }




        @Override
        public void onBindViewHolder(com.medassi.ecommerce.user.Activity.adapter.CustomAdapter.ViewHolder holder,
                                     final int position) {

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

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new MapsActivity())
                    .addToBackStack("1")
                    .commit();

                    //Intent intent = new Intent(context,MapsActivity.class);
                   // context.startActivity(intent);
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_versionname;

        ViewHolder(View itemView) {
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



}
