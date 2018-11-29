package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_Reset_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_user_errrfield,txt_stockist_retailer_passwordfield,txt_registration_reg,txt_reset_login;
    EditText edt_email;
    Button btn_log;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

           initUi();
           clickevents();
    }

    private void initUi() {

        connectionDetector = new ConnectionDetector();
        edt_email = (EditText)findViewById(R.id.resetpassEmail);
        txt_registration_reg=(TextView) findViewById(R.id.txt_registration_reg);
        txt_reset_login=(TextView)findViewById(R.id.txt_to_login);
        txt_user_errrfield=(TextView)findViewById(R.id.resetpassEmailerr);

        String styledText = "New User? "+" <font color='red'>" + "Click here to Register" + "</font> ";
        txt_registration_reg.setText(Html.fromHtml(styledText));

        String styledText1 = "Already Registered? "+" <font color='red'>" + "Click here to Login" + "</font> ";
        txt_reset_login.setText(Html.fromHtml(styledText1));

        btn_log=(Button)findViewById(R.id.bt_res_reset);

    }

    private void clickevents() {
        btn_log.setOnClickListener(this);
        txt_registration_reg.setOnClickListener(this);
        txt_reset_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==txt_registration_reg)
        {
            Intent ii=new Intent(User_Reset_Activity.this,Registration.class);
            startActivity(ii);
        }
        if(v==txt_reset_login)
        {
            Intent ii=new Intent(User_Reset_Activity.this,LoginActivity.class);
            startActivity(ii);
        }

        if(v==btn_log) {
            boolean internet = connectionDetector.isConnected(User_Reset_Activity.this);
            if (internet) {
                Validate1();
            } else {
                Toast.makeText(User_Reset_Activity.this, "Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onBackPressed() {

        Intent ii=new Intent(User_Reset_Activity.this,LoginActivity.class);
        startActivity(ii);
    }


    private void Validate1() {
        String email=edt_email.getText().toString().trim();
        boolean iserror=false;

        if(email.equalsIgnoreCase(""))
        {
            iserror=true;
            txt_user_errrfield.setVisibility(View.VISIBLE);
            txt_user_errrfield.setText(Html.fromHtml("This Field is Required."));

        }
        else
        {
            txt_user_errrfield.setVisibility(View.GONE);
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            iserror=true;
            txt_user_errrfield.setVisibility(View.VISIBLE);
            txt_user_errrfield.setText(Html.fromHtml("Valid Email is Required."));
        }
        else
        {
            //edt_email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Gray), PorterDuff.Mode.SRC_ATOP);
            txt_user_errrfield.setVisibility(View.GONE);
        }

        if(!iserror)
        {
            txt_user_errrfield.setVisibility(View.GONE);
            ResetPassword();
            //------------------------------------ Call WebService for User Reset Password-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
        }
    }
    private void ResetPassword() {
        final ProgressDialog pd = new ProgressDialog(User_Reset_Activity.this);
        pd.setCancelable(true);
        pd.setMessage("In Progress...!!");
        pd.show();

        final String email = edt_email.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_FORGET_PASSWORD,
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
                            if (Status.equals("success") || msg.equals("your password has been sent to your email"))
                            {
                                Toast.makeText(User_Reset_Activity.this, "Successfully Reset Password..!!", Toast.LENGTH_SHORT).show();
                                String NewPassword = obj.getString("result");
                                Intent intent = new Intent(User_Reset_Activity.this,LoginActivity.class);
                                intent.putExtra("newpass", NewPassword);
                                startActivity(intent);
                                finish();

                            }
                            else if (Status.equals("failed")|| msg.equals("Email does not exists!"))
                            {
                                String msgg = capitalize(msg);
                                Toast.makeText(User_Reset_Activity.this, msgg, Toast.LENGTH_SHORT).show();                            }
                            else
                            {
                                String msgg = capitalize(msg);
                                Toast.makeText(User_Reset_Activity.this, msgg, Toast.LENGTH_SHORT).show();
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
                params.put("email",email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-API-KEY","TEST@123");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(User_Reset_Activity.this);
        requestQueue.add(stringRequest);

    }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

}


