package com.medassi.ecommerce.user.Activity.Activity;

import android.Manifest;
//import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.medassi.ecommerce.user.Activity.Model.CartList;
import com.medassi.ecommerce.user.Activity.Model.Navigation;
import com.medassi.ecommerce.user.Activity.Model.for_map_stokistshow.For_Map_Stokistshowbean;
import com.medassi.ecommerce.user.Activity.Model.for_map_stokistshow.For_Map_XStokistshowbean;
import com.medassi.ecommerce.user.Activity.adapter.CustomAdapter;
import com.medassi.ecommerce.user.Activity.adapter.NavAdapter;
import com.medassi.ecommerce.user.Activity.app.Config;
import com.medassi.ecommerce.user.Activity.fragment.FirstFragment;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.GPSTracker;
import com.medassi.ecommerce.user.Activity.utils.NotificationUtils;
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

public class RecyclerViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
    FrameLayout frameLayout;
    TextView name_tv, mobile_tv,userlocation
            ,item_count , nosearchdata,dearuser;
    ImageView menu_icon,user_account,located_at, user_cart,crossImage;
    ArrayList<CartList> search_list;
    List<Address> address;
    Context ctx;
    ProgressDialog pd;
    String token,cart_item,user_id;
    LocationManager mlocationmanager;
    LocationListener mLocationListener;
    double currentlat,currentlong;
    Geocoder geocoder;
    For_Map_Stokistshowbean for_map_stokistshowbean;

    public BroadcastReceiver mRegistrationBroadcastReceiver;
    String  name,lastnm,mobile,email,billing_add_one,billing_add_two,shipping_add_one,shipping_add_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        version = new ArrayList<>();
        search_list = new ArrayList<CartList>();

        sessionManager = new SessionManager(RecyclerViewActivity.this);
        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        user_id =  sessionManager.getData(SessionManager.KEY_USER_ID);

        setCartItem();
        Initialize();

        setUp();

        //checkForPermission();

        setCount();
        //locationUpdate();
        clickListner();
        registerReceiver();

        setNavigation();
        setData();


    }


    private void setUp() {
        String type = getIntent().getStringExtra("activity");
        if (type!=null) {

           // Toast.makeText(ctx, "order", Toast.LENGTH_SHORT).show();
            Fragment fragment = new OrderHistory();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
                    //.addToBackStack("1").commit();
            }
        else
        {
           // Toast.makeText(ctx, "recycle", Toast.LENGTH_SHORT).show();
            FirstFragment firstFragment = new FirstFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,firstFragment).commit();
        }
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
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("idd", "Firebase reg id: " + regId);
    }
    /* private void locationUpdate() {

        address = new ArrayList<>();
        mlocationmanager = (LocationManager)getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
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


           mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentlat =  location.getLatitude();
                currentlong =  location.getLongitude();

                Log.i("Response ..","1.."+currentlat);
                Log.i("Response","1.."+currentlong);

                geocoder = new Geocoder(ctx,Locale.getDefault());
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
                        Toast.makeText(ctx, "no address found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mlocationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
                1, mLocationListener);
    }*/


    private void Initialize() {
        ctx = this;
        pd = new ProgressDialog(ctx);
        connectionDetector = new ConnectionDetector();

        name_tv = (TextView) findViewById(R.id.tv_username);
        dearuser = (TextView) findViewById(R.id.usernametv);

        menu_icon = (ImageView)findViewById(R.id.menu_open);
        item_count = (TextView)findViewById(R.id.item_counte1);
        user_cart = (ImageView) findViewById(R.id.addtocart);
        crossImage = (ImageView) findViewById(R.id.hideDialog);
        frameLayout = (FrameLayout)findViewById(R.id.framelayout);
        user_account = (ImageView)findViewById(R.id.user_account);
        listView = (ListView)findViewById(R.id.lv_nav_listview);

       drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

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

    }


    private void setNavigation() {

        nav_list = new ArrayList<>();
        nav_list.add(new Navigation(R.drawable.homee_icon, "Home"));
        nav_list.add(new Navigation(R.drawable.shopping_cart_icon, "MyCart"));
        nav_list.add(new Navigation(R.drawable.user_order_icon, "Order History"));
        nav_list.add(new Navigation(R.drawable.user_new_icon, "Update Profile"));
        nav_list.add(new Navigation(R.drawable.user_cp_icon, "Change Password"));
        nav_list.add(new Navigation(R.drawable.user_support_icon, "Contact Support"));
        nav_list.add(new Navigation(R.drawable.user_logout_icon, "Logout"));

        NavAdapter navAdapter = new NavAdapter(ctx, nav_list);
        listView.setAdapter(navAdapter);
    }


    private void clickListner() {
        listView.setOnItemClickListener(this);
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
                menu_icon.startAnimation(AnimationUtils.loadAnimation(ctx,
                        R.anim.slide_in_right));
            }
        });
        user_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new UpdateProfile())
                        .addToBackStack("1")
                        .commit();

               // user_account.startAnimation(AnimationUtils.loadAnimation(ctx,
                       // R.anim.slide_up));
            }
        });


        user_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user_cart.startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.slide_down));
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new MyCartActivty())
                        .addToBackStack("1")
                        .commit();

            }
        });

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.startAnimation(AnimationUtils.loadAnimation(ctx,R.anim.slide_in_left));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });



    }

  /* private void setFragment(Fragment fragment) {

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout,fragment);
        ft.commit();
    }*/

  @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Fragment fragment = null;
         if (position == 0) {
           fragment = new FirstFragment();
            // setFragment(fragment);
             getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment)
             .commit();
        }
        else if (position == 1) {

             //Intent intent = new Intent(RecyclerViewActivity.this,MyCartActivty.class);
           //  startActivity(intent);

             getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new MyCartActivty())
                     .addToBackStack(null)
                     .commit();

        } else if (position == 2) {
         //Intent intent = new Intent(RecyclerViewActivity.this, OrderHistory.class);
                // startActivity(intent);

                 getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new OrderHistory())
                       .addToBackStack("1")
                         .commit();

        } else if (position == 3) {
            //shareApp();
              // Intent intent = new Intent(RecyclerViewActivity.this, UpdateProfile.class);
               //  startActivity(intent);

             getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new UpdateProfile())
                     .addToBackStack("1")
                     .commit();

        } else if (position == 4) {
             getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new Changepassword())
                     .addToBackStack("1")
                     .commit();
            // Intent intent = new Intent(RecyclerViewActivity.this,Changepassword.class);
            // startActivity(intent);
        } else if (position == 5) {

             getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new ContactusActivity())
                     .addToBackStack("1")
                     .commit();

             //Intent intent = new Intent(RecyclerViewActivity.this, ContactusActivity.class);
            // startActivity(intent);

        } else if (position == 6) {
             sessionManager.logoutUser();
             Toast.makeText(ctx, "You have logged-out Successfully.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ctx, "Something wrong", Toast.LENGTH_LONG).show();
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
     }

    private void setData() {

        String fname = sessionManager.getData(SessionManager.KEY_FIRST_NAME);
        String lname = sessionManager.getData(SessionManager.KEY_LAST_NAME);
        name_tv.setText("Welcome "+fname+" "+lname);

    }



   @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        System.out.println("Count%%%" + count);

        if (count == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(Intent.ACTION_MAIN);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            // finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

       else {
            getSupportFragmentManager().popBackStack();
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


    @Override
    protected void onResume() {
        super.onResume();

        setUp();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        //setCount();
    }

  @Override
    protected void onPause() {
     // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
      super.onPause();
  }


  private void setCount() {
        final ProgressDialog pd = new ProgressDialog(RecyclerViewActivity.this);
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
                                Toast.makeText(ctx, "Somthing went wrong", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
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

    private void setCartItem() {
        final ProgressDialog pd = new ProgressDialog(RecyclerViewActivity.this);
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
                        //Toast.makeText(ctx, "Cart Is Empty.!", Toast.LENGTH_SHORT).show();

                    } else {
                        pd.dismiss();
                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(RecyclerViewActivity.this);
        requestQueue.add(stringRequest);

    }


    private void setView(ArrayList<CartList> search_list) {
        //Toast.makeText(ctx, "Seearc"+ search_list.size(), Toast.LENGTH_SHORT).show();
        sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(search_list.size()));
    }



    private void checkForPermission() {
        int permissionCheckForCamera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckForGallery = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckForAccessCamera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int permissionCheckForAccessFinelocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckForAccessCoarselocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheckForCallPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheckForCamera != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForGallery != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForAccessCamera != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForAccessFinelocation != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForCallPhone != PackageManager.PERMISSION_GRANTED ||
                permissionCheckForAccessCoarselocation != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.CALL_PHONE,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1001);
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
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("Location permission must required for this app",
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
                           /* Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();*/

                            showDialogOK("Location permission must required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null)));

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
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }









}