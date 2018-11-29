package com.medassi.ecommerce.user.Activity.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.medassi.ecommerce.user.Activity.Model.CartList;
import com.medassi.ecommerce.user.Activity.Model.Money;
import com.medassi.ecommerce.user.Activity.adapter.ShoppingListAdapter;
import com.medassi.ecommerce.user.Activity.app.Config;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCartActivty extends android.support.v4.app.Fragment {
    private static LinearLayout noItemDefault, Item_layout, payment_lay;
    private static RecyclerView recyclerView;
    private ItemTouchHelper mItemTouchHelper;
    LinearLayout linearLayout;
    static SessionManager sessionManager;
    static Dialog mydialog;
    public static   Button checkout,btn_shop_nw,Confirm,agreebuton;;
    static View view;
    String  name,lastnm,mobile,email,billing_add_one,billing_add_two,shipping_add_one,shipping_add_two;
    static String user_id;
    String cart_id;
    String product_id;
    String product_sku;
    String product_name;
    String product_price;
    String product_image;
    String stockies_id;
    String product_quantity;
    String product_desc;
    String available_qty;
    static RadioGroup rg;
    static String selectedRadioButtonText;
    int selectedRadioButtonID;
    RadioButton selectedRadioButton;
    static ConnectionDetector connectionDetector;
   public static TextView textView, itemcounter, payable_amount,termndpolicy,cart_item_unit;
   public static    EditText First_name, Last_Name, Billing_eail, Billing_address,Billing_address2, Billing_phone, Shipping_fullname, shipping_add_1,
            shipping_add_2, shipping_phone;
   public static TextView First_nameerr, Last_Nameerr, Billing_eailerr, Billing_addresserr,Billing_address2err, Billing_phoneerr, Shipping_fullnameerr
            , shipping_add_1err, shipping_add_2err, shipping_phoneerr, paymnt_mode,edttermandcndtnerr;
    String m_deviceId;

    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private int itemCount = 0;
    ShoppingListAdapter shoppinListAdapter;
    ArrayList<CartList> search_list;
    static Context c;
    static String token;
   public static Double price;
    ProgressBar pb;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_my_cart_activty,null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("My Cart");

        initView();
        //init();
        setTvAdapter();
        clickListner();
        //updateCheckOutSize(search_list.size());
        return  v;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart_activty);



        initView();
        //init();
        setTvAdapter();
        clickListner();
        //updateCheckOutSize(search_list.size());

    }*/

    private void init() {
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(shoppinListAdapter)
                .load(R.layout.item_skeleton_news)
                .show();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setTvAdapter();
                skeletonScreen.hide();
            }
        }, 1000);
        return;

    }

    /*private void setView(ArrayList<CartList> search_list) {

        try{
            if (search_list.size() != 0)
            {
                Item_layout.setVisibility(View.VISIBLE);
                noItemDefault.setVisibility(View.GONE);
                payment_lay.setVisibility(View.GONE);

            }
            else
            {
                Item_layout.setVisibility(View.GONE);
                noItemDefault.setVisibility(View.VISIBLE);
                payment_lay.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    private void initView() {

        c = getActivity();
        sessionManager = new SessionManager(getActivity());
        connectionDetector = new ConnectionDetector();
        pb = (ProgressBar)v.findViewById(R.id.progressbar);


        SharedPreferences pref = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
        m_deviceId = pref.getString("regId", null);

        cart_item_unit = (TextView)getActivity().findViewById(R.id.item_counte1);


        payment_lay = (LinearLayout)v.findViewById(R.id.layout_payment);
        noItemDefault = (LinearLayout)v.findViewById(R.id.layout_cart_empty);
        Item_layout = (LinearLayout)v.findViewById(R.id.layout_items);
        linearLayout = (LinearLayout)v.findViewById(R.id.ll_Linear_cart_list);

        recyclerView = (RecyclerView)v.findViewById(R.id.product_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        checkout = (Button)v.findViewById(R.id.checkout);
        btn_shop_nw = (Button)v.findViewById(R.id.btn_shop_now);

        textView = (TextView)v.findViewById(R.id.checkout_amount1);
        itemcounter = (TextView)v.findViewById(R.id.item_count1);

        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);
        search_list = new ArrayList<>();

    }

   /* private void setView(ArrayList<CartList> search_list)
    {

     try{
           if (search_list.size() == 0)
          {
            Item_layout.setVisibility(View.GONE);
            noItemDefault.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
          }
           else
          {
            Item_layout.setVisibility(View.VISIBLE);
            noItemDefault.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
          }
      }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void clickListner() {

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPurchaseDialog();
            }
        });

        btn_shop_nw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,RecyclerViewActivity.class);
                startActivity(intent);

            }
        });


    }

   /* @Override
    public void onBackPressed() {
        Bundle b;
        b = getIntent().getExtras();
        if (b!=null)
        {
            startActivity(new Intent(c,DetailActivity.class));
            //super.onBackPressed();
        }
        else
        {
            startActivity(new Intent(c,RecyclerViewActivity.class));
        }

    }*/

    private void setTvAdapter() {
        pb.setVisibility(View.VISIBLE);

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
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
                            cart_id = jsonObject.getString("id");
                            product_id = jsonObject.getString("product_id");
                            product_sku = jsonObject.getString("product_sku");
                            stockies_id = jsonObject.getString("stockiest_id");
                            product_name = jsonObject.getString("product_name");
                            product_price = jsonObject.getString("product_price");
                            product_image = jsonObject.getString("product_image");
                            product_quantity = String.valueOf(jsonObject.getInt("product_qty"));
                            product_desc = jsonObject.getString("product_description");
                            available_qty = jsonObject.getString("available_qty");
                            Log.i("result", "12233" + product_name);
                            search_list.add(new CartList(cart_id, product_id, stockies_id, product_name,
                                    product_price, product_image, product_quantity,
                                    product_desc,product_sku,available_qty));
                        }

                        shoppinListAdapter = new ShoppingListAdapter(getActivity(), search_list);
                        recyclerView.setAdapter(shoppinListAdapter);

                        pb.setVisibility(View.GONE);

                        Item_layout.setVisibility(View.VISIBLE);
                        payment_lay.setVisibility(View.VISIBLE);
                        noItemDefault.setVisibility(View.GONE);
                    } else if (Status.equals("failed")) {
                        pd.dismiss();
                        pb.setVisibility(View.GONE);
                        noItemDefault.setVisibility(View.VISIBLE);
                        payment_lay.setVisibility(View.GONE);
                        Item_layout.setVisibility(View.GONE);
                        String msgg =capitalize(msg);
                        Toast.makeText(c, msgg, Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        String msgg =capitalize(msg);
                        Toast.makeText(c, msgg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(c, error.getMessage(), Toast.LENGTH_SHORT).show();
                noItemDefault.setVisibility(View.VISIBLE);
                payment_lay.setVisibility(View.GONE);
                Item_layout.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);

    }

    public void showPurchaseDialog() {

        AlertDialog.Builder exitScreenDialog = new AlertDialog.Builder(getActivity(), R.style.PauseDialog);

        exitScreenDialog.setTitle("Order Confirmation")
                .setMessage("Would you like to place this order ?");
        exitScreenDialog.setCancelable(true);

        exitScreenDialog.setPositiveButton(
                "Place Order",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        payment_lay.setVisibility(View.GONE);
                        sessionManager.setData(SessionManager.KEY_PRODUCT_ID, product_id);
                        sessionManager.setData(SessionManager.KEY_USER_ID, user_id);
                        sessionManager.setData(SessionManager.KEY_STOCKIEST_ID, stockies_id);

                        Fragment Checkout = new CheckoutFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_Linear_cart_list,Checkout)
                               // .addToBackStack(null)
                                .commit();

                       /* FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.ll_Linear_cart_list, Checkout);
                        ft.commit();*/

                    }
                });

        exitScreenDialog.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        /*exitScreenDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Snackbar.make(MyCartActivty.this.getWindow().getDecorView().findViewById(android.R.id.content)
                        , "Order Placed Successfully, Happy Shopping !!", Snackbar.LENGTH_LONG)
                        .setAction("Go To Home", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(MyCartActivty.this, RecyclerViewActivity.class));
                            }
                        }).show();
            }
        });*/

        AlertDialog alert11 = exitScreenDialog.create();
        alert11.show();


        Button bq = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
        bq.setTextColor(Color.WHITE);
        Button bq1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
        bq1.setTextColor(Color.WHITE);
    }

    public static  void updateCheckOutAmount(Double total) {

        price = total;
        textView.setText(Money.rupees(BigDecimal.valueOf(total)).toString());

    }

    public static void updateCheckOutSize(int size) {
        if (size > 0) {
            itemcounter.setText(String.valueOf(size));
            cart_item_unit.setText(String.valueOf(size));
        } else {
             itemcounter.setText("0");
             cart_item_unit.setText("0");

             noItemDefault.setVisibility(View.VISIBLE);
             payment_lay.setVisibility(View.GONE);
             Item_layout.setVisibility(View.GONE);
        }
    }


    @SuppressLint("ValidFragment")
      public static class CheckoutFragment extends Fragment {
        CheckBox checkbox;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_checkout, null);
             initxml();
             clickEvent();
             setData();
            return view;
        }


        private void setData() {

            String fname = sessionManager.getData(SessionManager.KEY_FIRST_NAME);
            String lname = sessionManager.getData(SessionManager.KEY_LAST_NAME);
            String email = sessionManager.getData(SessionManager.KEY_EMAIL);
            String mobile = sessionManager.getData(SessionManager.KEY_MOBILE);

            String bill1 = sessionManager.getData(SessionManager.KEY_BILL_1);
            String bill2 = sessionManager.getData(SessionManager.KEY_BILL_2);
            String shipp1 = sessionManager.getData(SessionManager.KEY_SHIPP_1);
            String shipp2 = sessionManager.getData(SessionManager.KEY_SHIPP_2);

            First_name.setText(fname);
            Last_Name.setText(lname);
            Billing_eail.setText(email);
            Billing_phone.setText(mobile);


            Log.i("response..!","print"+bill1);
            Log.i("response..!","print"+bill2);
            Log.i("response..!","print"+shipp1);
            Log.i("response..!","print"+shipp2);

            if (bill1.equals("null") && bill2.equals("null"))
            {
                Billing_address.setText("");
                Billing_address2.setText("");

            }
            else
            {
                Billing_address.setText(bill1);
                Billing_address2.setText(bill2);

            }
            if (shipp1.equals("null") || shipp2.equals("null"))
            {
                shipping_add_1.setText("");
                shipping_add_2.setText("");

            }
            else
            {
                shipping_add_1.setText(shipp1);
                shipping_add_2.setText(shipp2);
            }



            //Billing_address.setText(bill1);
            //Billing_address2.setText(bill2);
            //shipping_add_1.setText(shipp1);
           // shipping_add_2.setText(shipp2);
        }

        private void clickEvent() {
            Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog pd= new  ProgressDialog(c);
                    pd.setMessage("Loading..");
                    pd.setCancelable(false);
                    pd.show();
                    Confirm.setClickable(false);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                         pd.dismiss();
                           boolean internet = connectionDetector.isConnected(c);
                            if (internet) {
                                Validate1();
                            } else {
                                Toast.makeText(c, "No internet connectiom", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, 2000);
                }
            });

            termndpolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Myalertdialog();
                }
            });
        }

        public  boolean isValid(String str)
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

        private void Validate1() {
            final String fistnam = First_name.getText().toString().trim();
            final String lstnam = Last_Name.getText().toString().trim();
            final String billemil = Billing_eail.getText().toString().trim();
            final String billaddress = Billing_address.getText().toString().trim();
            final String billaddress2 =  Billing_address2.getText().toString().trim();
            final String billphone = Billing_phone.getText().toString().trim();
            final String shippfullname = Shipping_fullname.getText().toString().trim();
            final String shipadd1 = shipping_add_1.getText().toString().trim();
            final String shipadd2 = shipping_add_2.getText().toString().trim();
            final String shipphn = shipping_phone.getText().toString().trim();

            boolean iserror = false;

            if (fistnam.equalsIgnoreCase("")) {
                iserror = true;
                //edtUserName.setError("");
                First_nameerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                First_nameerr.setText(Html.fromHtml("This Field is Required"));

                // edtUserNameerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            } else {
                //edtUserName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                First_nameerr.setVisibility(View.GONE);
            }


            if (lstnam.equalsIgnoreCase("")) {
                iserror = true;

                //edtEmail.setError("");
                Last_Nameerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Last_Nameerr.setText(Html.fromHtml("This field is required"));
                //edtEmailerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {

                //edtEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                Last_Nameerr.setVisibility(View.GONE);
            }

            if (billemil.equalsIgnoreCase("")) {
                iserror = true;

                //edtEmail.setError("");
                Billing_eailerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Billing_eailerr.setText(Html.fromHtml("This field is required"));
                //edtEmailerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {

                //edtEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                Billing_eailerr.setVisibility(View.GONE);
            }


            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(billemil).matches()) {
                iserror = true;
                //edtEmail.setError("");
                Billing_eailerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Billing_eailerr.setText(Html.fromHtml("Valid email is required"));
                //edtEmailerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {
                //edt_email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.Gray), PorterDuff.Mode.SRC_ATOP);
                Billing_eailerr.setVisibility(View.GONE);
            }

            if (billaddress.equalsIgnoreCase("")) {
                iserror = true;
                //edtMobile.setError("");
                Billing_addresserr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Billing_addresserr.setText(Html.fromHtml("This field is required"));
                //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {
                //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                Billing_addresserr.setVisibility(View.GONE);
            }

            if (billaddress2.equalsIgnoreCase("")) {
                iserror = true;
                //edtMobile.setError("");
                Billing_address2err.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Billing_address2err.setText(Html.fromHtml("This field is required"));
                //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {
                //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                Billing_address2err.setVisibility(View.GONE);
            }


            if (billphone.equalsIgnoreCase("")) {
                iserror = true;
                //edtPassword.setError("");
                Billing_phoneerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Billing_phoneerr.setText(Html.fromHtml("This field is required"));
                //edtPassworderr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            } else {
                //edtPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                Billing_phoneerr.setVisibility(View.GONE);
            }

            if (shippfullname.equalsIgnoreCase("")) {
                iserror = true;
                //edtMobile.setError("");
                Shipping_fullnameerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Shipping_fullnameerr.setText(Html.fromHtml("This field is required"));
                Confirm.setClickable(true);

                //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            }
            else if(!isValid(shippfullname))
            {
                iserror=true;
                Shipping_fullnameerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                Shipping_fullnameerr.setText(Html.fromHtml("Only alphabates are allowed"));
                Confirm.setClickable(true);

            }

            else {
                //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                Shipping_fullnameerr.setVisibility(View.GONE);
            }

            if (shipadd1.equalsIgnoreCase("")) {
                iserror = true;
                //edtMobile.setError("");
                shipping_add_1err.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                shipping_add_1err.setText(Html.fromHtml("This field is required"));
                //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {
                //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                shipping_add_1err.setVisibility(View.GONE);
            }

            if (shipadd2.equalsIgnoreCase("")) {
                iserror = true;
                //edtMobile.setError("");
                shipping_add_2err.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                shipping_add_2err.setText(Html.fromHtml("This field is required"));
                //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {
                //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                shipping_add_2err.setVisibility(View.GONE);
            }

            if (shipphn.equalsIgnoreCase("")) {
                iserror = true;
                //edtMobile.setError("");
                shipping_phoneerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                shipping_phoneerr.setText(Html.fromHtml("This field is required"));
                Confirm.setClickable(true);

                //edtMobileerr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));

            } else {
                if (shipphn.length() < 9)
                {
                    iserror=true;
                    shipping_phoneerr.setVisibility(View.VISIBLE);
                    //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                    shipping_phoneerr.setText(Html.fromHtml("Phone number minimum 9 digit required."));
                    Confirm.setClickable(true);

                }
                else {
                    shipping_phoneerr.setVisibility(View.GONE);

                }

                //edtMobile.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
               // shipping_phoneerr.setVisibility(View.GONE);
            }



            if(!checkbox.isChecked())
            {
                iserror=true;
                //edtPassword.setError("");
                edttermandcndtnerr.setVisibility(View.VISIBLE);
                //  txt_stockist_retailer_namefield.setText(Html.fromHtml(getString(R.string.edit_error_msg)+""+getString(R.string.stockist_retailer_name)<sup>2</sup>));
                edttermandcndtnerr.setText(Html.fromHtml("Please check term and policy"));
                Confirm.setClickable(true);

                //edtPassworderr.getBackground().mutate().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                //   edt_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
                //   txtinput_stockist_retailer_name.setError(getString(R.string.edit_error_msg)+" "+getString(R.string.stockist_retailer_name));
            }
            else
            {
                //edtPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                edttermandcndtnerr.setVisibility(View.GONE);
            }

            int selectedRadioButtonID = rg.getCheckedRadioButtonId();

            // If nothing is selected from Radio Group, then it return -1
            try {
                if (selectedRadioButtonID != -1) {

                    RadioButton selectedRadioButton = (RadioButton) view.findViewById(selectedRadioButtonID);
                    selectedRadioButtonText = selectedRadioButton.getText().toString();
                    paymnt_mode.setVisibility(View.GONE);
                    paymnt_mode.setText(selectedRadioButtonText + " selected.");
                }
                else {
                    paymnt_mode.setVisibility(View.VISIBLE);
                    paymnt_mode.setText("Nothing selected from Radio Group.");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

             // Get the checked Radio Button ID from Radio Grou[
             /*selectedRadioButtonID = rg.getCheckedRadioButtonId();
            // If nothing is selected from Radio Group, then it return -1
             selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);

            if (selectedRadioButtonID == -1) {

                selectedRadioButtonText = selectedRadioButton.getText().toString();
                paymnt_mode.setText(selectedRadioButtonText + " selected.");

            } else {

                paymnt_mode.setVisibility(View.VISIBLE);
                paymnt_mode.setText("Nothing selected from Radio Group.");
            }
*/
            if (!iserror) {
                First_nameerr.setVisibility(View.GONE);
                Last_Nameerr.setVisibility(View.GONE);
                Billing_eailerr.setVisibility(View.GONE);
                Billing_addresserr.setVisibility(View.GONE);
                Billing_address2err.setVisibility(View.GONE);
                Billing_phoneerr.setVisibility(View.GONE);
                Shipping_fullnameerr.setVisibility(View.GONE);
                shipping_add_1err.setVisibility(View.GONE);
                shipping_add_2err.setVisibility(View.GONE);
                shipping_phoneerr.setVisibility(View.GONE);
                paymnt_mode.setVisibility(View.GONE);
                OrderRequest();
            }

        }

        private void OrderRequest() {
            final ProgressDialog pd = new ProgressDialog(c);
            pd.setCancelable(false);
            pd.setMessage("Ordering..");
            pd.show();

            final String fistnam = First_name.getText().toString().trim();
            final String lstnam = Last_Name.getText().toString().trim();
            final String billemil = Billing_eail.getText().toString().trim();
            final String billaddress = Billing_address.getText().toString().trim();
            final String billaddress2 =  Billing_address2.getText().toString().trim();
            final String billphone = Billing_phone.getText().toString().trim();
            final String shippfullname = Shipping_fullname.getText().toString().trim();
            final String shipadd1 = shipping_add_1.getText().toString().trim();
            final String shipadd2 = shipping_add_2.getText().toString().trim();
            final String shipphn = shipping_phone.getText().toString().trim();


    /* final String address = edtAddress.getText().toString().trim();
     final String city = edtCity.getText().toString().trim();
     final String country = edtcountry.getText().toString().trim();*/

          /*  //first we will do the validations
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


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PlACE_ORDER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            try {
                                Log.i("response..!", "1235" + response);

                                //converting response to json object
                                JSONObject obj = new JSONObject(response);

                                //print the response from server

                                String Status = obj.getString("status");
                                String msg = obj.getString("message");

                                //if no error in response
                                if (Status.equals("success") || msg.equals("Your order has been confirmed successfully")) {


                                    String msgg =capitalize(msg);
                                    Toast.makeText(c, msgg, Toast.LENGTH_SHORT).show();
                                    sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, "0");

                                     Fragment fragment = new OrderHistory();
                                     getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment)
                                             .addToBackStack("1")
                                             .commit();

                                     TextView textView = (TextView)getActivity().findViewById(R.id.item_counte1);
                                     textView.setText("0");
                                    /*FragmentManager fragmentManager = getActivity().getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                     fragmentTransaction.replace(R.id.framelayout,fragment); */
                                    //Intent intent = new Intent(c, RecyclerViewActivity.class);
                                   // startActivity(intent);

                                    Confirm.setClickable(false);

                                }
                                else if (Status.equals("failed")) {
                                    String msgg =capitalize(msg);
                                    Confirm.setClickable(true);

                                    final android.app.AlertDialog.Builder alertDialog =
                                            new android.app.AlertDialog.Builder(c);
                                    alertDialog.setMessage(msgg);
                                    alertDialog.setCancelable(false);
                                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,
                                                new MyCartActivty())
                                                .addToBackStack(null)
                                                .commit();
                                            }
                                    });
                                   /* alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
*/
                                    alertDialog.show();


                                   // Toast.makeText(c, msgg, Toast.LENGTH_LONG).show();
                                } else {
                                    Confirm.setClickable(true);
                                    Toast.makeText(c, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),  error.getMessage(), Toast.LENGTH_SHORT).show();
                            Confirm.setClickable(true);

                            if (error instanceof NetworkError) {
                                Toast.makeText(getActivity(), "servererrror", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getActivity(), "servererrror", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getActivity(), "Authfailure", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getActivity(), "parseerror", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NoConnectionError) {
                                Toast.makeText(getActivity(), "Noconnectionerror", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof TimeoutError) {
                                Toast.makeText(getActivity(), "TimeOutError", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("first_name", fistnam);
                    params.put("last_name", lstnam);
                    params.put("billing_email", billemil);
                    params.put("billing_phone_no", billphone);
                    params.put("billing_address_one", billaddress);
                    params.put("billing_address_two", billaddress2);
                    params.put("shipping_full_name", shippfullname);
                    params.put("shipping_address_one", shipadd1);
                    params.put("shipping_address_two", shipadd2);
                    params.put("shipping_phone_number", shipphn);
                    params.put("user_id", user_id);
                    params.put("payment_mode",selectedRadioButtonText);
                    //params.put("device_token",m_deviceId);
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
            RequestQueue requestQueue = Volley.newRequestQueue(c);
            requestQueue.add(stringRequest);
        }


        private void initxml() {

            payable_amount = (TextView) view.findViewById(R.id.payable_ammount);
            payable_amount.setText(Money.rupees(BigDecimal.valueOf(price)).toString());


            checkbox = (CheckBox)view.findViewById(R.id.checkBox);
            termndpolicy = (TextView) view.findViewById(R.id.termandpolicy);

            String styledText1 = "I agree with Medaasi? "+" <font color='#c0171d'> Terms And Policy </font> ";
            termndpolicy.setText(Html.fromHtml(styledText1));

            edttermandcndtnerr = (TextView)view.findViewById(R.id.termconditionerr);

            //Edittext initialization..
            First_name = (EditText) view.findViewById(R.id.firstname);
            Last_Name = (EditText) view.findViewById(R.id.lastname);

            Billing_eail = (EditText) view.findViewById(R.id.billing_email);
            Billing_address = (EditText) view.findViewById(R.id.billing_address_1);
            Billing_address2 = (EditText) view.findViewById(R.id.billing_address_2);

            Billing_phone = (EditText) view.findViewById(R.id.billing_phone);

            Shipping_fullname = (EditText) view.findViewById(R.id.shipping_full_name);
            shipping_add_1 = (EditText) view.findViewById(R.id.shipping_address_one);
            shipping_add_2 = (EditText) view.findViewById(R.id.shipping_address_two);
            shipping_phone = (EditText) view.findViewById(R.id.shipping_phone_no);
            Confirm = (Button) view.findViewById(R.id.bt_confirm);

            //TextView Initialization..
            First_nameerr = (TextView) view.findViewById(R.id.firstnameerr);
            Last_Nameerr = (TextView) view.findViewById(R.id.lastnameerr);
            Billing_eailerr = (TextView) view.findViewById(R.id.billing_email_err);
            Billing_addresserr = (TextView) view.findViewById(R.id.billing_address_1err);
            Billing_address2err = (TextView) view.findViewById(R.id.billing_address_2err);
            Billing_phoneerr = (TextView) view.findViewById(R.id.billing_phone_err);
            Shipping_fullnameerr = (TextView) view.findViewById(R.id.shipping_full_name_err);
            shipping_add_1err = (TextView) view.findViewById(R.id.shipping_address_one_err);
            shipping_add_2err = (TextView) view.findViewById(R.id.shipping_address_two_err);
            shipping_phoneerr = (TextView) view.findViewById(R.id.shipping_phone_no_err);
            paymnt_mode = (TextView) view.findViewById(R.id.paymnt_modeerr);

            //RadioGroup && buttons initialization
            rg = (RadioGroup) view.findViewById(R.id.option);
            /* r1 = (RadioButton) view.findViewById(R.id.option1);
            r2 = (RadioButton) view.findViewById(R.id.option2);
            r3 = (RadioButton) view.findViewById(R.id.option3);*/

        }

        public void Myalertdialog(){


            try {
                /*getInstance of dialog*/
                mydialog = new Dialog(c);
                mydialog.setCancelable(false);
                mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mydialog.setContentView(R.layout.termandpolicy_dialog);

                agreebuton = (Button)mydialog.findViewById(R.id.Agree_btn);
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


    }

    private static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}