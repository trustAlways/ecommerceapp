package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.medassi.ecommerce.user.Activity.App;
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

public class Changepassword extends Fragment {
EditText oldpass,newpass,cnfrmpass;
TextView oldpasserr,newpasserr,cnfrmerr;
Button btnupdate;
SessionManager sessionManager;
ConnectionDetector connectionDetector;
ProgressDialog pd ;
Context ctx;
String Tkn , id;
    String encrypt_oldpassword;
    String encrypt_newpassword;
    String encrypt_confirmpassword;
  View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.user_change_password,null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("Change Password");

        Initialize();
        clickListner();

        return v;
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_change_password);


    }*/

    private void clickListner() {

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean internet = connectionDetector.isConnected(ctx);
                if(internet) {
                    Validate1();
                }
                else
                {
                    Toast.makeText(ctx, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    private void Initialize() {
        ctx = getActivity();
        sessionManager = new SessionManager(getActivity());
        connectionDetector = new ConnectionDetector();
        pd = new ProgressDialog(getActivity());
        oldpass = (EditText)v.findViewById(R.id.edt_user_oldpassword);
        newpass = (EditText)v.findViewById(R.id.edt_user_newpassword);
        cnfrmpass = (EditText)v.findViewById(R.id.edt_user_confrmpassword);

        oldpasserr = (TextView) v.findViewById(R.id.edt_user_oldpassworderr);
        newpasserr = (TextView)v. findViewById(R.id.edt_user_newpassworderr);
        cnfrmerr = (TextView)v. findViewById(R.id.edt_user_confrmpassworderr);

        btnupdate = (Button)v.findViewById(R.id.btn_change_password);
        Tkn = sessionManager.getData(SessionManager.KEY_TOKEN);
        id = sessionManager.getData(SessionManager.KEY_USER_ID);
        }

    private void Validate1()
    {
        final String old = oldpass.getText().toString().trim();
        final String newpss = newpass.getText().toString().trim();
        final String cnfrm = cnfrmpass.getText().toString().trim();
        boolean iserror=false;

        if(old.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtUserName.setError("");
            oldpasserr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            oldpasserr.setText(Html.fromHtml("Old password is required"));
            // edtUserNameerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            oldpasserr.setVisibility(View.GONE);
        }
        if(newpss.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtUserName.setError("");
            newpasserr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            newpasserr.setText(Html.fromHtml("New password is required"));
            // edtUserNameerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            if (newpss.length() < 6)
            {
                iserror=true;
                newpasserr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                newpasserr.setText(Html.fromHtml("Password minimum 6 character in length."));

            }
            else {
                newpasserr.setVisibility(View.GONE);
            }

            //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
            //newpasserr.setVisibility(View.GONE);
        }




        if(cnfrm.equalsIgnoreCase(""))
        {
            iserror=true;
            //edtUserName.setError("");
            cnfrmerr.setVisibility(View.VISIBLE);
            //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
            cnfrmerr.setText(Html.fromHtml("Confirm password is required"));
            // edtUserNameerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
        }
        else
        {
            if (cnfrm.length() < 6)
            {
                iserror=true;
                cnfrmerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                cnfrmerr.setText(Html.fromHtml("Password minimum 6 character in length."));

            }
            else {
                cnfrmerr.setVisibility(View.GONE);
            }
            //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
           // cnfrmerr.setVisibility(View.GONE);
        }






        if(!iserror)
        {
            oldpasserr.setVisibility(View.GONE);
            newpasserr.setVisibility(View.GONE);
            cnfrmerr.setVisibility(View.GONE);
            loadingChangePasswordData();
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


    private void loadingChangePasswordData() {
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        final String oldpassword = oldpass.getText().toString().trim();
        final String newpassword = newpass.getText().toString().trim();
        final String confirmpassword = cnfrmpass.getText().toString().trim();

        String unsecurepassword = oldpassword;
        encrypt_oldpassword = md5(unsecurepassword );

        String newpass = newpassword;
        encrypt_newpassword = md5(newpass );

        String cnfrmpass = confirmpassword;
        encrypt_confirmpassword = md5(cnfrmpass );

       /* if (TextUtils.isEmpty(oldpassword)) {
            pd.dismiss();
            oldpass.setError("Please enter old password.");
            oldpass.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newpassword)) {
            pd.dismiss();
            newpass.setError("Please enter new password.");
            newpass.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmpassword)) {
            pd.dismiss();
            cnfrmpass.setError("Please enter confirm password.");
            cnfrmpass.requestFocus();
            return;
        }
        if(!confirmpassword.equalsIgnoreCase(newpassword))
        {
            cnfrmpass.setError("Confirm Password not matched with the new password ");
        }
*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CHANGEPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //print the response from server
                            Log.i("response","change password..."+response);

                            String  Status = obj.getString("status");
                            String msg = obj.getString("message");

                            //if no error in response
                            if (Status.equals("success") || msg.equals("your password has been changed"))
                            {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ctx,RecyclerViewActivity.class);
                                startActivity(intent);

                                //oldpass.setText("");
                                //newpass.setText("");
                                //cnfrmpass.setText("");

                            }
                            else if (Status.equals("failed")|| msg.equals("old password is wrong!"))
                            {
                                String msgg = capitalize(msg);
                                Toast.makeText(getActivity(), msgg, Toast.LENGTH_SHORT).show();
                            }
                            else if (Status.equals("failed") || msg.equals("The Confirm New Password field does not match the New Password field"))
                            {
                                String msgg = capitalize(msg);
                                Toast.makeText(getActivity(), msgg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "some eror", Toast.LENGTH_SHORT).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",id);
                params.put("old_password",oldpassword);
                params.put("new_password", newpassword);
                params.put("cnew_password", confirmpassword);
                return params;
/*

*/
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params1 = new HashMap<>();
                params1.put("X-API-KEY","TEST@123");
                params1.put("Authorization","Bearer "+ Tkn );
                return params1;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
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
  /*  @Override
    public boolean onTouch(View v, MotionEvent event) {
       switch (v.getId())
       {
           case R.id.edt_user_oldpassword:
               oldpass.setEnabled(true);
               break;
           case R.id.edt_user_newpassword:
               newpass.setEnabled(true);
               break;
           case R.id.edt_user_confrmpassword:
               cnfrmpass.setEnabled(true);
               break;
       }

        return false;
    }*/
}
