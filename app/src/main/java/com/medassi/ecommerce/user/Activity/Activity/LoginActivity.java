package com.medassi.ecommerce.user.Activity.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medassi.ecommerce.user.Activity.Model.User;
import com.medassi.ecommerce.user.Activity.app.Config;
import com.medassi.ecommerce.user.Activity.service.MyFirebaseInstanceIDService;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import android.provider.Settings.Secure;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView registerText, tvemailerr, tvpassworderr, tvresetpassword, tvnewpass;
    Button Signin;
    SessionManager sessionManager;
    ConnectionDetector connectionDetector;
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog pd;
    String m_deviceId;
    String encrypt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //For Initialize all the view
        initView();
        checkForPermission();

        boolean internet = connectionDetector.isConnected(LoginActivity.this);
        if (internet) {
            boolean login = sessionManager.isLoggedIn();
            if (login) {
                startActivity(new Intent(LoginActivity.this, RecyclerViewActivity.class));
                finish();
            }
        }
        else {
            Toast.makeText(LoginActivity.this, "Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
        }
        //To handle All the Click events
        clickListner();
    }

    private void initView() {

        sessionManager = new SessionManager(LoginActivity.this);
        //sharedPrefManager = SharedPrefManager.getInstance(LoginActivity.this);
        connectionDetector = new ConnectionDetector();
        pd = new ProgressDialog(LoginActivity.this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        m_deviceId = pref.getString("regId", null);

        Log.e("idd", "Firebase reg id: " + m_deviceId);

        edtEmail = (EditText)findViewById(R.id.signInEmail);
        edtPassword = (EditText)findViewById(R.id.signInPassword);

        tvemailerr = (TextView) findViewById(R.id.signInEmailerr);
        tvpassworderr = (TextView) findViewById(R.id.signInPassworderr);
        tvnewpass = (TextView) findViewById(R.id.newpass);

        Signin = (Button)findViewById(R.id.bt_log_login);
        registerText = (TextView)findViewById(R.id.bt_logToReg);

        tvresetpassword = (TextView)findViewById(R.id.bt_forgetpassword);

        String styledText = "New User? "+" <font color='red'> Click here to Register </font> ";
        registerText.setText(Html.fromHtml(styledText));

        String styledText2 = "Forgot Password? "+" <font color='red'> Click here to Reset </font> ";
        tvresetpassword.setText(Html.fromHtml(styledText2));

        Bundle b;
        b = getIntent().getExtras();
        if (b!=null)
        {
            String newpasss=b.getString("newpass");
            tvnewpass.setVisibility(View.VISIBLE);
            tvnewpass.setText("New Password :"+newpasss);
        }
        else
        {
            tvnewpass.setVisibility(View.GONE);
        }
    }


    private void clickListner()
    {
        //event invoked when text for login clicked
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Registration.class);
                startActivity(intent);
                finish();
            }
        });

        //event invoked when signup button clicked
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             boolean internet = connectionDetector.isConnected(LoginActivity.this);
               if(internet) {
                   Validate1();
               }
               else
               {
                   Toast.makeText(LoginActivity.this, "Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
               }

            }
        });

        tvresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,User_Reset_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Validate1() {

        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        boolean iserror=false;

        if(email.equalsIgnoreCase(""))
        {
            iserror=true;
           // edtEmail.setError("");
            tvemailerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            tvemailerr.setText(Html.fromHtml("This field is required"));
            //tvemailerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                iserror=true;
                // edtEmail.setError("");
                tvemailerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                tvemailerr.setText(Html.fromHtml("Valid email is required"));
                //tvemailerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            }
            else
            {
                //edt_email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Gray), PorterDuff.Mode.SRC_ATOP);
                tvemailerr.setVisibility(View.GONE);
            }

            //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
           // tvemailerr.setVisibility(View.GONE);
        }



        if(password.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtPassword.setError("");
            tvpassworderr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            tvpassworderr.setText(Html.fromHtml("This field is required"));
            //tvpassworderr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            if (password.length() < 6)
            {
                iserror=true;
                tvpassworderr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                tvpassworderr.setText(Html.fromHtml("Password minimum 6 character in length."));

            }
            else {
                tvpassworderr.setVisibility(View.GONE);
            }

            //edtPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            //tvpassworderr.setVisibility(View.GONE);
        }



        if(!iserror)
        {
            tvemailerr.setVisibility(View.GONE);
            tvpassworderr.setVisibility(View.GONE);
            LoginRequest();

        }

    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
            alertDialog();
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


    private void LoginRequest() {
        pd.setMessage("Loading..");
        pd.show();

        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        String unsecurepassword = password;
        final String securepassword = md5(unsecurepassword );

        Log.i("encrypt","pass"+ securepassword);
       /* try {
            encrypt_password = Base64.encodeToString( password.getBytes("ISO-8859-1"), Base64.DEFAULT );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("encrypt","pass"+ encrypt_password);

        final String  pass = new String( Base64.decode( encrypt_password, Base64.DEFAULT ) );
        Log.i("decrypt","pass"+ pass);
*/


        if (TextUtils.isEmpty(email)) {
            pd.dismiss();
            edtEmail.setError("Please enter your email");
            edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            pd.dismiss();
            edtEmail.setError("Enter a valid email");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pd.dismiss();
            edtPassword.setError("Enter a password");
            edtPassword.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            Log.i("response..!","1235"+response);

                            String Status = obj.getString("status");
                            String msg = obj.getString("message");



                            //if no error in response
                          if (Status.equals("success") || msg.equals("login successfully!"))
                            {
                                Toast.makeText(LoginActivity.this, " You have logged-in successfully.", Toast.LENGTH_SHORT).show();

                                String Token = obj.getString("token");
                                sessionManager.setData(SessionManager.KEY_TOKEN,Token);
                                Log.i("Token","Login..."+ Token);

                                JSONObject object = obj.getJSONObject("result");
                                String userid = object.getString("user_id");
                                String f_name = object.getString("first_name");
                                String l_name = object.getString("last_name");
                                String email = object.getString("email");
                                String mobile = object.getString("phone_number");

                                sessionManager.setData(SessionManager.KEY_USER_ID, userid);

                                User user = new User(f_name,l_name,email,mobile);
                                //sharedPrefManager.userLogin(user);
                                sessionManager.createLoginSession(user);

                                Intent intent = new Intent(LoginActivity.this,RecyclerViewActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else if (Status.equals("failed"))
                            {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                          else if (Status.equals("Expired token"))
                          {
                              Toast.makeText(LoginActivity.this, "Don't Worry,Just Login Again", Toast.LENGTH_LONG).show();
                          }
                            else
                            {
                                Toast.makeText(LoginActivity.this, Status, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", password);
                Log.e("idd", "Firebase reg id: " + m_deviceId);

                if (m_deviceId.equals(null))
                {
                    params.put("device_token","");
                }
                else
                {
                    params.put("device_token",m_deviceId);
                }


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-API-KEY","TEST@123");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }



    private void alertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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
                            Manifest.permission.CALL_PHONE,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1001);
        }

    }


}
