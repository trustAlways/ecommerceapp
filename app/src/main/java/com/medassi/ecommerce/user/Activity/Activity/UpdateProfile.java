package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medassi.ecommerce.user.Activity.customview.TextDrawable;
import com.medassi.ecommerce.user.Activity.utils.ColorGenerator;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends Fragment {

    TextView tv_username,tv_email,tv_mobile,tv_changepass;
    TextView tv_bill_1_Err,tv_bill_2_Err,tv_shipp_1_Err,tv_shipp_2_Err;
    EditText et_bill_add_1,et_bill_add_2,et_shipp_add_1,et_shipp_add_2;
    Button btn_update;
    CircleImageView userimage;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    String token,user_id;
    Context context;
    String  name,lastnm,mobile,email,billing_add_one,billing_add_two,shipping_add_one,shipping_add_two;
    SessionManager sessionManager;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_update_profile,null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("My Registered Profile");

        Initview();
        setData();
        Clicklistner();


        return v;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


    }*/


    private void Initview() {

        context = getActivity();
        sessionManager = new SessionManager(getActivity());
        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);

        tv_username = (TextView)v.findViewById(R.id.user_name);
        tv_email = (TextView)v.findViewById(R.id.user_email);
        tv_mobile = (TextView)v.findViewById(R.id.user_phone);
        tv_changepass = (TextView)v.findViewById(R.id.bt_changepass);

        tv_bill_1_Err = (TextView)v.findViewById(R.id.billing_add_1_err);
        tv_bill_2_Err = (TextView)v.findViewById(R.id.billing_add_2_err);
        tv_shipp_1_Err = (TextView)v.findViewById(R.id.shipping_add_1_err);
        tv_shipp_2_Err = (TextView)v.findViewById(R.id.shipping_add_2_err);

        et_bill_add_1 = (EditText)v.findViewById(R.id.billing_add_1);
        et_bill_add_2 = (EditText)v.findViewById(R.id.billing_add_2);
        et_shipp_add_1 = (EditText)v.findViewById(R.id.shipping_add_1);
        et_shipp_add_2 = (EditText)v.findViewById(R.id.shipping_add_2);

        btn_update = (Button)v.findViewById(R.id.bt_update_profile);


    }
    private void setData() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading..");
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

                                Log.i("response..!","print"+billing_add_one);
                                Log.i("response..!","print"+billing_add_two);
                                Log.i("response..!","print"+shipping_add_one);
                                Log.i("response..!","print"+shipping_add_two);

                                setProfile(name,lastnm,email,mobile,billing_add_one,billing_add_two,shipping_add_one,
                                        shipping_add_two);
                            }
                            else if (Status.equals("failed"))
                            {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(),  error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setProfile(String name, String lastnm, String email,
                            String mobile, String billing_address_one,
                            String billing_address_two, String shipping_address_one,
                            String shipping_address_two) {

        tv_username.setText(name +" "+ lastnm);
        tv_mobile.setText(mobile + "");
        tv_email.setText(email + "");

        if (billing_address_two.equals("null") && billing_address_one.equals("null"))
        {
            et_bill_add_1.setText("");
            et_bill_add_2.setText("");

        }
        else
        {
            et_bill_add_1.setText(billing_address_one);
            et_bill_add_2.setText(billing_address_two);
            sessionManager.setData(SessionManager.KEY_BILL_1,billing_address_one);
            sessionManager.setData(SessionManager.KEY_BILL_2,billing_address_two);
        }

        if (shipping_address_one.equals("null") || shipping_address_two.equals("null"))
        {
            et_shipp_add_1.setText("");
            et_shipp_add_2.setText("");

        }
        else
            {
                et_shipp_add_1.setText(shipping_address_one);
                et_shipp_add_2.setText(shipping_address_two);
                sessionManager.setData(SessionManager.KEY_SHIPP_1,shipping_address_one);
                sessionManager.setData(SessionManager.KEY_SHIPP_2,shipping_address_two);
            }
    }

    private void Clicklistner() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate1();
            }
        });
        tv_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new Changepassword())
                        .commit();
               /* Intent intent  = new Intent(getActivity(),Changepassword.class);
                startActivity(intent);*/
            }
        });

    }

   private void Validate1()
    {

        final String billing_one = et_bill_add_1.getText().toString().trim();
        final String billing_two  = et_bill_add_2.getText().toString().trim();
        final String shiping_one = et_shipp_add_1.getText().toString().trim();
        final String shiping_two = et_shipp_add_2.getText().toString().trim();


        boolean iserror=false;

        if(billing_one.equalsIgnoreCase(""))
        {
            iserror=true;
            tv_bill_1_Err.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            tv_bill_1_Err.setText(Html.fromHtml("This Field is Required"));
            //tv_firstname.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            tv_bill_1_Err.setVisibility(View.GONE);
        }



        if(billing_two.equalsIgnoreCase(""))
        {
            iserror=true;

            tv_bill_2_Err.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            tv_bill_2_Err.setText(Html.fromHtml("This Field is Required"));
            //tv_firstname.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

        }
        else
        {

            //edtEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            tv_bill_2_Err.setVisibility(View.GONE);
        }


        if(shiping_one.equalsIgnoreCase(""))
        {
            iserror=true;
            //et_billibng_address_one.setError("");
            tv_shipp_1_Err.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            tv_shipp_1_Err.setText(Html.fromHtml("This Field is Required"));
            //tv_billibng_address_one.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

        }
        else
        {
            //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            tv_shipp_1_Err.setVisibility(View.GONE);
        }

        if(shiping_two.equalsIgnoreCase(""))
        {
            iserror=true;
            //et_billing_address_two.setError("");
            tv_shipp_2_Err.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            tv_shipp_2_Err.setText(Html.fromHtml("This Field is Required"));
            //tv_billing_address_two.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            //edtPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            tv_shipp_2_Err.setVisibility(View.GONE);
        }

        if(!iserror)
        {
              tv_bill_1_Err.setVisibility(View.GONE);
              tv_bill_2_Err.setVisibility(View.GONE);
              tv_shipp_1_Err.setVisibility(View.GONE);
              tv_shipp_2_Err.setVisibility(View.GONE);
              updateUserProfile();
        }

    }


    private void updateUserProfile() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading..");
        pd.show();

        final String f_name = name;
        final String l_name = lastnm;
        final String bill_one = et_bill_add_1.getText().toString().trim();
        final String bill_two = et_bill_add_2.getText().toString().trim();
        final String shipp_one = et_shipp_add_1.getText().toString().trim();
        final String shipp_two  = et_shipp_add_2.getText().toString().trim();



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


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SAVE_USER_PROFILE,
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
                            if (Status.equals("success")||msg.equals("Your profile updated successfully!"))
                            {
                                JSONObject jsonObject = obj.getJSONObject("result");
                                sessionManager.setData(SessionManager.KEY_BILL_1,jsonObject.getString("billing_address_one"));
                                sessionManager.setData(SessionManager.KEY_BILL_2,jsonObject.getString("billing_address_two"));
                                sessionManager.setData(SessionManager.KEY_SHIPP_1,jsonObject.getString("shipping_address_one"));
                                sessionManager.setData(SessionManager.KEY_SHIPP_2,jsonObject.getString("shipping_address_two"));

                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), RecyclerViewActivity.class);
                                startActivity(intent);

                            }
                            else if (Status.equals("failed"))
                            {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(),  error.getMessage(), Toast.LENGTH_SHORT).show();
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id",user_id);
                params.put("first_name", f_name);
                params.put("last_name", l_name);
                params.put("billing_address_one", bill_one);
                params.put("billing_address_two", bill_two);
                params.put("shipping_address_one", shipp_one);
                params.put("shipping_address_two", shipp_two);
                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
       // stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,//
           //     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//
           //     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//
        requestQueue.add(stringRequest);

    }




}
