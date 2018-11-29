package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    private Animation animation;
    private ImageView logo;
    private TextView appTitle;
    private TextView appSlogan;
    Context ctx;
    String user_id,token;
    ArrayList<CartList> search_list;
    SessionManager sessionManager;
    ConnectionDetector connectionDetector;
    String  name,lastnm,mobile,email,billing_add_one,billing_add_two,shipping_add_one,shipping_add_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ctx = this;
        logo = (ImageView) findViewById(R.id.logo_img);
        appTitle = (TextView) findViewById(R.id.track_txt);
        search_list = new ArrayList<>();
        sessionManager = new SessionManager(ctx);
        user_id =  sessionManager.getData(SessionManager.KEY_USER_ID);
        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        connectionDetector = new ConnectionDetector();



       /* // Font path
        String fontPath = "font/CircleD_Font_by_CrazyForMusic.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Applying font
        appTitle.setTypeface(tf);
        appSlogan.setTypeface(tf);*/

        if (savedInstanceState == null) {
            flyIn();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //setData();
                //setTvAdapter();
                endSplash();
            }
        }, 3000);
    }

    private void flyIn() {

       /* Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        appTitle.startAnimation(anim);*/

        animation = AnimationUtils.loadAnimation(this,
                R.anim.donot_move);
        appTitle.startAnimation(animation);

        }


    private void setData(){
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setCancelable(false);
        pd.setMessage("Loading...!!");
        pd.show();

        /*  //first we will do the validations
        if (TextUtils.isEmpty(username)) {
            pd.dismiss();
            et_firstname.setError("Please enter firsrname");
            //edtUserName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            pd.dismiss();
            et_lastname.setError("Please enter your email");
            //edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pd.dismiss();
            edtPassword.setError("Enter a password");
            //edtPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            pd.dismiss();
            edtMobile.setError("Enter a  Mobile Number");
            //edtMobile.requestFocus();
            return;
        }
      if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Enter your address");
            edtAddress.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(city)) {
            edtCity.setError("Enter your city");
            edtCity.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(country)) {
            edtcountry.setError("Enter your country");
            edtcountry.requestFocus();
            return;
        }*/


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

        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,//
        //     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//
        //     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//
        requestQueue.add(stringRequest);

    }

    private void setProfile(String name, String lastnm, String email, String mobile,
                            String billing_add_one, String billing_add_two,
                            String shipping_add_one, String shipping_add_two) {

        sessionManager.setData(SessionManager.KEY_BILL_1,billing_add_one);
        sessionManager.setData(SessionManager.KEY_BILL_2,billing_add_two);
        sessionManager.setData(SessionManager.KEY_SHIPP_1,shipping_add_one);
        sessionManager.setData(SessionManager.KEY_SHIPP_2,shipping_add_two);

        Log.i("response..!","print"+billing_add_one);
        Log.i("response..!","print"+billing_add_two);
        Log.i("response..!","print"+shipping_add_one);
        Log.i("response..!","print"+shipping_add_two);
    }

    private void setTvAdapter() {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Loading..!");
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
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);

    }

    private void setView(ArrayList<CartList> search_list) {
        //Toast.makeText(ctx, "Seearc"+search_list.size(), Toast.LENGTH_SHORT).show();
        sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(search_list.size()));
    }

    private void endSplash() {
       /* animation = AnimationUtils.loadAnimation(this,
                R.anim.logo_animation_back);
        logo.startAnimation(animation);*/

        animation = AnimationUtils.loadAnimation(this,
                R.anim.donot_move);
        appTitle.startAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                boolean internet = connectionDetector.isConnected(ctx);
                if(internet) {
                    boolean login = sessionManager.isLoggedIn();
                    if (login) {
                        startActivity(new Intent(ctx, RecyclerViewActivity.class));
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    startActivity(new Intent(ctx, RecyclerViewActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
