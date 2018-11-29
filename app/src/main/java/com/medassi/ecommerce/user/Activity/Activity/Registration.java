package com.medassi.ecommerce.user.Activity.Activity;

//import android.app.ProgressDialog;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration extends AppCompatActivity {

    //Instance Variable
 EditText edtUserName,edtLastName,edtEmail,edtPassword,edtMobile;
 TextView edtUserNameerr, edtLastNameerr,edtEmailerr,edtPassworderr,edtMobileerr, edttermandcndtnerr,tv_termofservice;

 TextView loginText;
 Button Signup,agreebuton;
 Context ctx;
 CheckBox checkbox;
 ConnectionDetector connectionDetector;
 ProgressDialog pd;
    Dialog mydialog;
 SessionManager sessionManager;
    String token;
     String encrypt_password;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //For Initialize all the view
          initView();
        // getPermission();
         //checkForPermission();

     boolean login = sessionManager.isLoggedIn();
     if (login) {
         startActivity(new Intent(Registration.this, RecyclerViewActivity.class));
         finish();
     }
     //To handle All the Click events
        clickListner();
 }


   //method where all the views initialized
    private void initView()
    {
        ctx = this;
        sessionManager = new SessionManager(Registration.this);
        //sharedPrefManager = SharedPrefManager.getInstance(Registration.this);
        connectionDetector = new ConnectionDetector();
        pd = new ProgressDialog(Registration.this);

        edtUserName = (EditText)findViewById(R.id.signUpName);
        edtLastName = (EditText)findViewById(R.id.signUpLastName);
        edtPassword = (EditText)findViewById(R.id.signUPassword);
        edtEmail = (EditText)findViewById(R.id.signUpEmail);
        edtMobile = (EditText)findViewById(R.id.signUpMobile);

        edtUserNameerr = (TextView) findViewById(R.id.signUpNameerr);
        edtLastNameerr = (TextView) findViewById(R.id.signUpLastNameerr);
        edtPassworderr = (TextView) findViewById(R.id.signUpPasseorderr);
        edtEmailerr = (TextView) findViewById(R.id.signUpEmailerr);
        edtMobileerr = (TextView) findViewById(R.id.signUpMobileerr);
        edttermandcndtnerr = (TextView) findViewById(R.id.termconditinerr);
        tv_termofservice = (TextView) findViewById(R.id.textView2);

        String styledText1 = "I agree with Medaasi "+" <font color='#c0171d'> Terms of Services </font> ";
        tv_termofservice.setText(Html.fromHtml(styledText1));

        checkbox = (CheckBox)findViewById(R.id.checkBox1);

         Signup = (Button)findViewById(R.id.bt_reg_register);

         loginText = (TextView)findViewById(R.id.bt_regToLog);

        String styledText = "Already Registered? "+" <font color='red'> Click here to Login </font> ";
        loginText.setText(Html.fromHtml(styledText));

    }

     //method where all the click event
     private void clickListner()
     {
         //event invoked when text for login clicked
         loginText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(Registration.this,LoginActivity.class);
             startActivity(intent);
           }
       });
         //event invoked when signup button clicked
          Signup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
           boolean internet = connectionDetector.isConnected(Registration.this);
               if(internet) {
                   Validate1();
               }
               else
               {
                   Toast.makeText(Registration.this, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
               }

             //RegisterRequest();
         }
     });

      tv_termofservice.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Myalertdialog();
          }
      });
    }
    public static boolean isValid(String str)
    {
        boolean isValid = false;
        String expression = "^[a-z_A-Z ]*$";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }
    private void Validate1()
    {
        final String username = edtUserName.getText().toString().trim();
        final String lastname = edtLastName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        final String mobile  = edtMobile.getText().toString().trim();
        boolean iserror=false;

        if(username.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtUserName.setError("");
            edtUserNameerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edtUserNameerr.setText(Html.fromHtml("This field is required"));
           // edtUserNameerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else if(!isValid(username))
        {
            iserror=true;
            edtUserNameerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edtUserNameerr.setText(Html.fromHtml("Only alphabates are allowed"));
        }
        else
        {
            //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            edtUserNameerr.setVisibility(View.GONE);
        }

        if(lastname.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtUserName.setError("");
            edtLastNameerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edtLastNameerr.setText(Html.fromHtml("This field is required"));
            // edtUserNameerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }

        else if(!isValid(lastname))
        {
            iserror=true;
            edtLastNameerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edtLastNameerr.setText(Html.fromHtml("Only alphabates are allowed"));
        }

        else
        {
            //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            edtLastNameerr.setVisibility(View.GONE);
        }



        if(email.equalsIgnoreCase(""))
        {
            iserror=true;

            //edtEmail.setError("");
            edtEmailerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edtEmailerr.setText(Html.fromHtml("This field is required"));
            //edtEmailerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

        }
        else
        {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                iserror=true;
                //edtEmail.setError("");
                edtEmailerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                edtEmailerr.setText(Html.fromHtml("Valid email is required"));
                //edtEmailerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            }
            else
            {
                //edt_email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Gray), PorterDuff.Mode.SRC_ATOP);
                edtEmailerr.setVisibility(View.GONE);
            }

            //edtEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
           // edtEmailerr.setVisibility(View.GONE);
        }




        if(password.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtMobile.setError("");
            edtPassworderr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edtPassworderr.setText(Html.fromHtml("This field is required"));
            //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

        }
        else
        {
            if (password.length() < 6)
            {
                iserror=true;
                edtPassworderr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                edtPassworderr.setText(Html.fromHtml("Password minimum 6 character in length."));

            }
            else {
                edtPassworderr.setVisibility(View.GONE);

            }

            //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            //edtPassworderr.setVisibility(View.GONE);
        }




        if(mobile.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtMobile.setError("");
            edtMobileerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edtMobileerr.setText(Html.fromHtml("This field is required"));
            //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

        }
        else
        {
            if (mobile.length() < 9)
            {
                iserror=true;
                edtMobileerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                edtMobileerr.setText(Html.fromHtml("Phone number minimum 9 digit required."));
            }
            else {
                edtMobileerr.setVisibility(View.GONE);

            }

            //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
           // edtMobileerr.setVisibility(View.GONE);
        }



        if(!checkbox.isChecked())
        {
            iserror=true;
            //edtPassword.setError("");
            edttermandcndtnerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            edttermandcndtnerr.setText(Html.fromHtml("Please check term of service"));
            //edtPassworderr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            //edtPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            edttermandcndtnerr.setVisibility(View.GONE);
        }




        if(!iserror)
        {
            edtUserNameerr.setVisibility(View.GONE);
            edtMobileerr.setVisibility(View.GONE);
            edtEmailerr.setVisibility(View.GONE);
            edtPassworderr.setVisibility(View.GONE);
            edttermandcndtnerr.setVisibility(View.GONE);
            RegisterRequest();
        }

    }

    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }



    private void RegisterRequest() {
     pd.setMessage("Loading..");
     pd.show();

     final String username = edtUserName.getText().toString().trim();
     final String lastname = edtLastName.getText().toString().trim();
     final String email = edtEmail.getText().toString().trim();
     final String password = edtPassword.getText().toString().trim();
     final String mobile  = edtMobile.getText().toString().trim();

        final String unsecurepassword = password;
        final String securepassword = md5(unsecurepassword );
        Log.i("encrypt","pass"+ securepassword);

       /* try {
            encrypt_password = Base64.encodeToString( password.getBytes("ISO-8859-1"), Base64.DEFAULT );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("encrypt","pass"+ encrypt_password);

        final String  pass = new String( Base64.decode( encrypt_password, Base64.DEFAULT ) );
        Log.i("decrypt","pass"+ pass);*/



    /* final String address = edtAddress.getText().toString().trim();
     final String city = edtCity.getText().toString().trim();
     final String country = edtcountry.getText().toString().trim();*/

        //first we will do the validations
        if (TextUtils.isEmpty(username)) {
            pd.dismiss();
            edtUserName.setError("Please enter username");
            //edtUserName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            pd.dismiss();
            edtEmail.setError("Please enter your email");
            //edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            pd.dismiss();
            edtEmail.setError("Enter a valid email");
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
       /* if (mobile.length()!=9)
        {
            pd.dismiss();
            edtMobile.setError("Enter atleast 9 digit Mobile Number");
        }*/
       /* if (TextUtils.isEmpty(address)) {
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


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
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
                            if (Status.equals("success")||msg.equals("Record has been added successfully!"))
                            {
                                Toast.makeText(Registration.this, "You have been registerd successfully.", Toast.LENGTH_SHORT).show();
                               /* User user = new User(username,email,mobile);
                                sharedPrefManager.userLogin(user);*/
                                Intent intent = new Intent(Registration.this, LoginActivity.class);
                                startActivity(intent);

                            }
                            else if (Status.equals("failed"))
                            {
                                Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Registration.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(getApplicationContext(), "errror"+ error.getMessage(), Toast.LENGTH_SHORT).show();

                        if( error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "servererrror", Toast.LENGTH_SHORT).show();
                          }
                        else if( error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "servererrror", Toast.LENGTH_SHORT).show();
                          }
                      else if( error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authfailure", Toast.LENGTH_SHORT).show();
                        }
                      else if( error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "parseerror", Toast.LENGTH_SHORT).show();
                        }
                      else if( error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "Noconnectionerror", Toast.LENGTH_SHORT).show();
                        }
                      else if( error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(), "TimeOutError", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("first_name", username);
                params.put("last_name", lastname);
                params.put("email", email);
                params.put("password", unsecurepassword);
                params.put("phone_number", mobile);
                /*params.put("address", address);
                params.put("city_id", city);
                params.put("country_id", country);*/
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params1 = new HashMap<>();
                params1.put("X-API-KEY","TEST@123");
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
        /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
       requestQueue.add(stringRequest);

    }


   /* private void loadSearchingData() {

       *//* final String selectd = et_search.getText().toString();

        if (TextUtils.isEmpty(selectd)) {
            pd.dismiss();
            et_search.setError("Enter a search value");
            et_search.requestFocus();
            return;
        }*//*

        pd.setMessage("wait..");
        // pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        Log.i("response","re12345..."+response);

                        sessionManager.setData(SessionManager.KEY_SEARCH_DATA, response);
                        Toast.makeText(ctx, "succeshjkl"+ response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "some eror", Toast.LENGTH_SHORT).show();
                    }
                })

        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params1 = new HashMap<>();
                params1.put("X-API-KEY","TEST@123");
                params1.put("Authorization","Bearer "+token );
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }
*/

    // runtime permission

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.RECEIVE_SMS};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[3])) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Registration.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Find Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Registration.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Registration.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Registration.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
            }
        }
    }

    private void getPermission() {

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(Registration.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Registration.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Registration.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Registration.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Registration.this, permissionsRequired[3])) {
                //Show Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Registration.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Access fine location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Registration.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Registration.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Read and Wrte Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Registration.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.

        }
    }
        private void checkForPermission() {
            int permissionCheckForCamera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCheckForGallery = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheckForAccessCamera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
            int permissionCheckForAccessFinelocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCheckForAccessCoarselocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionCheckForCamera != PackageManager.PERMISSION_GRANTED ||
                    permissionCheckForGallery != PackageManager.PERMISSION_GRANTED ||
                    permissionCheckForAccessCamera != PackageManager.PERMISSION_GRANTED ||
                    permissionCheckForAccessFinelocation != PackageManager.PERMISSION_GRANTED ||
                    permissionCheckForAccessCoarselocation != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        1001);
            }

        }

    public void Myalertdialog(){
        try {
            /*getInstance of dialog*/
            mydialog = new Dialog(ctx);
            mydialog.setCancelable(false);
            mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mydialog.setContentView(R.layout.termandcondition_dialog);

            agreebuton = (Button)mydialog.findViewById(R.id.agreebutton);
            agreebuton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edttermandcndtnerr.setVisibility(View.GONE);
                    checkbox.setChecked(true);
                    mydialog.cancel();
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



    @Override
    public void onBackPressed() {
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
}
