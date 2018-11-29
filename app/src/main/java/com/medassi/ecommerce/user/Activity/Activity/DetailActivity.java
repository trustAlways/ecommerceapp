package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
import com.medassi.ecommerce.user.Activity.Model.CartList;
import com.medassi.ecommerce.user.Activity.Model.Item_Count;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

public class DetailActivity extends Fragment implements View.OnClickListener {

    public TextView title_tv, textView_description, distrivutor,textView_dist,price ,Product_Qty,
            remove_item,add_item ,informtext,cart_item_total;
    EditText item_count,add_item_number;
    ImageView imageView,plusImage,minusimage;
    Button addtocart,btn_AddTocart,btn_gotocart;
    int qut=0;
   String product_quantity;
    LinearLayout expandableLayout1;
    String Product_id, product_name,stockiest_name,product_price,type,product_image,stockiest,product_quantiti,product_sku,
            description,distance;
    String sold_qunty= String.valueOf(0);
    String token;
    Context ctx;
    private int itemCount = 0;
    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    String product_id,Stockiest_id,user_id;
    double currentlat,currentlong;
    ConnectionDetector connectionDetector;
    SessionManager sessionManager;
    ArrayList<Item_Count> AddCart;
    ArrayList<CartList> search_list;
    String listsize;
    String updateqty;
    int q;
    boolean bool_check_data=false;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activitydetail2,null);

        initXml();
        setTvAdapter2();
        clickEvents();

         TextView t =  getActivity().findViewById(R.id.header_name);
         t.setText("Product Detail");

        boolean internet = connectionDetector.isConnected(ctx);
        if (internet)
        {
            loadBuisnessData(product_id,Stockiest_id,currentlat,currentlong);
        }
        else
        {
            Toast.makeText(ctx, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
        }

        return  v;
    }

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitydetail2);


    }
*/
    private void loadBuisnessData(final String product_id, final String stockiest_id, final double currentlat,
                                  final double currentlong)
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
                 //product_image = obj.getString("image_url");
                 sold_qunty = obj.getString("sold_qty");

                 if (Status.equals("success")||msg.equals("product details")) {

                     JSONObject jsonObject = obj.getJSONObject("result");
                     Product_id = jsonObject.getString("product_id");
                     product_sku = jsonObject.getString("product_sku");
                     product_name = jsonObject.getString("product_name");
                     product_image = jsonObject.getString("image_url");

                     description = jsonObject.getString("product_description");
                     product_quantiti = jsonObject.getString("product_quantity");
                     product_price = jsonObject.getString("product_price");
                     //product_price = jsonObject.getString("product_price");
                     Stockiest_id = jsonObject.getString("stockiest_id");
                     type =jsonObject.getString("type");


                     double Stock_lat,Stock_long;
                     JSONObject jsonstockiestObject = obj.getJSONObject("stockiest");
                     Stock_lat = Double.parseDouble(jsonstockiestObject.getString("lat"));
                     Stock_long = Double.parseDouble(jsonstockiestObject.getString("lon"));
                     stockiest_name = jsonstockiestObject.getString("stockiest_retailer_name");


                     Log.i("response..!","product description"+ description);
                     Log.i("response..!","product Deatil"+ product_price);
                     //setData(product_name,product_price,product_image,product_quantity,distributor_name);
                 }

                 else
                 {
                     Toast.makeText(ctx,"somthing wrong",Toast.LENGTH_SHORT).show();
                 }

               setData(product_name,product_price,product_image,product_quantiti,description,stockiest_name,distance,type,sold_qunty);


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
                params.put("stockiest_id", stockiest_id);
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



    private void setData(String product_name, String product_price, String product_image, String product_quantity2,
                         String description, String stockiest_name, String distance, String type, String sold_qunty)
    {

        String url = product_image;
        if (!url.equals("false")) {

            //String icon_url = "http://192.168.1.2/webservices/uploads/stockiest_"+ Stockiest_id+"/" + url;
            //icon_url = icon_url.replace(" ","%20");


            String url1 = product_image;
            String new_url = url1.replace("candid-15-pc","192.168.1.8");

            Log.i("response..!","icon url"+ url1);
            Log.i("response..!","icon url"+ new_url);

            Glide.with(ctx)
                    .load(url1)
                    //.thumbnail(0.5f)
                    // .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    //.placeholder(R.drawable.logo)
                    .into(imageView);

          /* Picasso.with(ctx)
                    .load(url)
                    //.resize(100, 100)
                   .rotate(90)                    //if you want to rotate by 90 degrees
                   .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(imageView);*/
        }


        String n = capitalize(product_name);
        title_tv.setEnabled(false);
        title_tv.setText(n);

         textView_description.setText(description);
        //textView_underline.setText(product_name);
         textView_dist.setText(distance+" away");

         //distrivutor.setText(type+" :"+distributor_name+", ");
        if (type.equals("stockiest"))
        {
            distrivutor.setText("Stockiest: "+stockiest_name+", ");
        }
        else {
            distrivutor.setText("Retailer: "+stockiest_name+", ");
        }



        price.setText(product_price);
        // Product_Qty.setText(product_quantity +" Units Available");

        if (!sold_qunty.equals("null")) {

           q = Integer.parseInt(product_quantity2) - Integer.parseInt(sold_qunty);

            if (q == 0) {
                Product_Qty.setText("Out Of Stock");
                add_item_number.setEnabled(false);
                btn_AddTocart.setClickable(false);
            }
            else {
                Product_Qty.setText(q + " Unit Available");
            }
        }
        else
        {
           q = Integer.parseInt(String.valueOf(Integer.parseInt(product_quantity2) - 0));

            Product_Qty.setText(q + " Unit Available");
            //add_item_number.setEnabled(false);
        }

       /* String url = product_image;
        if (!url.equals("false")) {

            //String icon_url = "http://192.168.1.2/webservices/uploads/stockiest_"+ Stockiest_id+"/" + url;
            //icon_url = icon_url.replace(" ","%20");


            String url1 = product_image;
            String new_url = url1.replace("candid-15-pc","192.168.1.7");

            Log.i("response..!","icon url"+ url1);
            Log.i("response..!","icon url"+ new_url);

           Glide.with(ctx)
                    .load(url)
                    //.thumbnail(0.5f)
                   // .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                   //.placeholder(R.drawable.logo)
                   .into(imageView);

          *//* Picasso.with(ctx)
                    .load(url)
                    //.resize(100, 100)
                   .rotate(90)                    //if you want to rotate by 90 degrees
                   .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(imageView);*//*
        }*/
    }

    private void initXml() {
          ctx = getActivity();
          connectionDetector = new ConnectionDetector();
          sessionManager = new SessionManager(getActivity());
          AddCart = new ArrayList<Item_Count>();
          search_list = new ArrayList<>();

          title_tv = (TextView)v.findViewById(R.id.tv_product_title);
          textView_description = (TextView)v.findViewById(R.id.description);
          add_item_number  = (EditText)v.findViewById(R.id.add_quantity);
          distrivutor = (TextView)v.findViewById(R.id.tv_distributorname);
          textView_dist = (TextView)v.findViewById(R.id.tv_kms);
          price = (TextView)v.findViewById(R.id.tv_price);
          Product_Qty = (TextView)v.findViewById(R.id.tv_qty_status);

          informtext = (TextView)v.findViewById(R.id.informtext);
          cart_item_total = (TextView)getActivity().findViewById(R.id.item_counte1);
          listsize = sessionManager.getData(SessionManager.KEY_LST_SIZE);
          String styledText = "Click here to see more prices from more " +
                  //"<font color='red'>" + listsize + "</font> "+
                  "stockiest";

          informtext.setText(Html.fromHtml(styledText));

           imageView = (ImageView)v.findViewById(R.id.productImage);
           plusImage = (ImageView)v.findViewById(R.id.plus);
           minusimage = (ImageView)v.findViewById(R.id.minus);
           btn_AddTocart = (Button)v.findViewById(R.id.AddToCard);
           btn_gotocart = (Button)v.findViewById(R.id.go_to_cart);

         expandableLayout1 = (LinearLayout) v.findViewById(R.id.expandableLayout4);
           token = sessionManager.getData(SessionManager.KEY_TOKEN);
          product_id = sessionManager.getData(SessionManager.KEY_PRODUCT_ID);
          Stockiest_id = sessionManager.getData(SessionManager.KEY_STOCKIEST_ID);
          user_id = sessionManager.getData(SessionManager.KEY_USER_ID);
          currentlat = Double.parseDouble(sessionManager.getData(SessionManager.KEY_LATITUDE));
          currentlong = Double.parseDouble(sessionManager.getData(SessionManager.KEY_LONGITUDE));

            /*for(int i = 0; i < search_list.size(); i++) {
            qut = (int) Double.parseDouble(search_list.get(i).getQty());
            //loadAddCart(qut);
            Toast.makeText(ctx, "qty "+ qut, Toast.LENGTH_SHORT).show();
        }*/

      /*  if (itemCount != 0) {
            for (CartList cartList : CenterRepository.getCenterRepository()
                    .getListOfProductsInCart()) {

                updateCheckOutAmount(BigDecimal.valueOf(Long.valueOf(cartList.getPrice())), true);
            }
        }*/


    }

    private void clickEvents()
    {
        btn_AddTocart.setOnClickListener(this);
        btn_gotocart.setOnClickListener(this);
        plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    plusImage.setVisibility(View.GONE);
                    minusimage.setVisibility(View.VISIBLE);
                    if (expandableLayout1.getVisibility()==View.GONE)
                    {
                        expandableLayout1.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        expandableLayout1.setVisibility(View.GONE);
                    }
                }
                }
        });
        minusimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    plusImage.setVisibility(View.VISIBLE);
                    minusimage.setVisibility(View.GONE);
                    if (expandableLayout1.getVisibility()==View.VISIBLE)
                    {
                        expandableLayout1.setVisibility(View.GONE);
                    }
                    else
                    {
                        expandableLayout1.setVisibility(View.VISIBLE);
                    }                
                }
            }
        });

        informtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new MapsActivity())
                        .addToBackStack("4")
                        .commit();

               // Intent intent = new Intent(ctx,MapsActivity.class);
              //  startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.AddToCard) {
            final ProgressDialog pd= new  ProgressDialog(ctx);
            pd.setMessage("Loading..");
            pd.setCancelable(false);
            pd.show();
             btn_AddTocart.setClickable(false);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    pd.dismiss();

                    if (product_quantity==null)
                    {
                        product_quantity ="0";
                        loadAddCart(Integer.parseInt(product_quantity));
                    }
                    else
                    {
                        loadAddCart(Integer.parseInt(product_quantity));

                    }
                }
            }, 2000);

            }
         if (id==R.id.go_to_cart)

         {

             getActivity().getSupportFragmentManager().beginTransaction().
                     replace(R.id.framelayout,new MyCartActivty())
                     .addToBackStack("3")
                     .commit();

            /* Intent intent = new Intent(ctx,MyCartActivty.class);
             intent.putExtra("detailactivity","detailactivity");
             startActivity(intent);*/
         }

        }


    private void loadAddCart(int qut) {
        final ProgressDialog pd = new ProgressDialog(ctx);
         pd.setMessage("Loading..");
         pd.setCancelable(false);
         pd.show();

        final String quantity = add_item_number.getText().toString();

        if (TextUtils.isEmpty(quantity))
        {
            pd.dismiss();
            add_item_number.setError("please enter quantity");
            add_item_number.requestFocus();
            btn_AddTocart.setClickable(true);
            return;
        }

        if (Integer.parseInt(quantity) > q)
        {
            pd.dismiss();
            Toast.makeText(ctx, "only "+ q +" quantity available!", Toast.LENGTH_SHORT).show();
            add_item_number.setError("please enter valid quantity");
            add_item_number.requestFocus();
            btn_AddTocart.setClickable(true);
            return;
        }
        int u_q = 0;
        try {
           u_q = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
           e.printStackTrace(); // this will cause the parameter check for quantity ordered to fail and pop toast
        }

        int o = qut + u_q;

        try
        {
            System.out.println("O Value is@@@"+o);
            System.out.println("Quant Value is@@@"+qut);
            System.out.println("User Value is@@@"+u_q);

            if (o > q && o!=q)
            {
                pd.dismiss();
                add_item_number.setError("You cannot add more quantity than available stock");
                add_item_number.requestFocus();
                btn_AddTocart.setClickable(true);

                return;

            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDTOCART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    Log.i("response..!","add_to_card%%%"+ response);
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")|| msg.equals("product added to cart successfully!")) {
                        Toast.makeText(ctx,"The product has been added to your cart.",Toast.LENGTH_SHORT).show();

                        setTvAdapter2();
                        btn_AddTocart.setClickable(true);
                        if(bool_check_data==false)
                        {
                            setTvAdapter();
                        }
                        JSONArray array = obj.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String cart_id = jsonObject.getString("id");
                            String product_id = jsonObject.getString("product_id");
                            String stockies_id = jsonObject.getString("stockiest_id");
                            String product_name = jsonObject.getString("product_name");
                            DetailActivity.this.qut = Integer.parseInt(jsonObject.getString("available_qty"));
                        }
                        //setTvAdapter();

                    }
                    else if (Status.equals("failed"))
                    {
                        pd.dismiss();
                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
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
                params.put("user_id",user_id);
                params.put("product_sku",product_sku);
                params.put("product_id",product_id);
                params.put("product_name", product_name);
                params.put("stockiest_id", Stockiest_id);
                params.put("product_price", product_price);
                params.put("product_qty", quantity);
                params.put("available_qty", String.valueOf(q));
                params.put("product_description",description);
                params.put("product_image",product_image);

                Log.i("respo","qtyy"+quantity);
                Log.i("respo","uid"+q);
                ;Log.i("respo","pid"+product_id);



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




    private void setTvAdapter2() {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Loading..");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_CART, new Response.Listener<String>() {
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

                         JSONObject jsonObject = obj.getJSONObject("result");

                            String cart_id = jsonObject.getString("id");
                            String product_id = jsonObject.getString("product_id");
                            String stockies_id = jsonObject.getString("stockiest_id");
                            String product_name = jsonObject.getString("product_name");
                            String product_price = jsonObject.getString("product_price");
                            String product_image = jsonObject.getString("product_image");
                            product_quantity = String.valueOf(jsonObject.getInt("product_qty"));
                            String product_desc = jsonObject.getString("product_description");
                            String available_qty = jsonObject.getString("available_qty");

                            Log.i("result", "12233" + product_name);


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
                Toast.makeText(ctx, "Some Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("product_id",product_id);
                params.put("stockiest_id",Stockiest_id);
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
                            product_quantity = String.valueOf(jsonObject.getInt("product_qty"));
                            String product_desc = jsonObject.getString("product_description");
                            String available_qty = jsonObject.getString("available_qty");

                            Log.i("result", "12233" + product_name);

                            search_list.add(new CartList(cart_id, product_id, stockies_id, product_name,
                                    product_price, product_image, product_quantity, product_desc, product_sku, available_qty));
                            }
                        bool_check_data=true;
                        String item = String.valueOf(search_list.size());
                        cart_item_total.setText(item);
                        setView(search_list);

                    } else if (Status.equals("failed")) {
                        pd.dismiss();
                        //Toast.makeText(ctx, "Cart Is Empty.!", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(DetailActivity.this, "produ q"+product_quantity, Toast.LENGTH_SHORT).show();

                    } else {
                        pd.dismiss();
                        Toast.makeText(ctx, "somthing wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ctx, "Some Error", Toast.LENGTH_SHORT).show();
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


    private void lodAddCart(final String product_id, final String stockiest_id, final int q)
    {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("wait..");
        pd.setCancelable(false);
        pd.show();

        final String quantity = add_item_number.getText().toString();



       /* if (TextUtils.isEmpty(code))
        {
            pd.dismiss();
            item_count.setError("please enter Product Code");
            item_count.requestFocus();
            return;

        }
        if (TextUtils.isEmpty(quantity))
        {
            pd.dismiss();
            add_item_number.setError("please enter quantity");
            add_item_number.requestFocus();
            return;

        }
*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    Log.i("response..!","add_to_card%%%"+ response);
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")|| msg.equals("product added to cart successfully!")) {
                        Toast.makeText(ctx,"The product has been added to your cart.",Toast.LENGTH_SHORT).show();

                        JSONArray array = obj.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String cart_id = jsonObject.getString("id");
                            String product_id = jsonObject.getString("product_id");
                            String stockies_id = jsonObject.getString("stockiest_id");
                            String product_name = jsonObject.getString("product_name");
                             qut = Integer.parseInt(jsonObject.getString("product_name"));
                        }

                        //setTvAdapter();
                    }
                    else if (Status.equals("failed"))
                    {
                        pd.dismiss();
                        String msgg =capitalize(msg);
                        Toast.makeText(ctx, msgg, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pd.dismiss();
                        String msgg =capitalize(msg);
                        Toast.makeText(ctx, msgg, Toast.LENGTH_SHORT).show();
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
                params.put("product_id",product_id);
                params.put("stockiest_id", stockiest_id);
                params.put("product_qty", quantity);
                params.put("available_qty", String.valueOf(q));

                Log.i("respo","qtyy"+quantity);
                Log.i("respo","uid"+user_id);
                ;Log.i("respo","pid"+product_id);
                Log.i("respo","id"+stockiest_id);
                Log.i("respo","qt"+q);
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

   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(ctx,MapsActivity.class);
        startActivity(intent);
    }
*/

    private String capitalize(String capString){
       StringBuffer capBuffer = new StringBuffer();
       Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
       while (capMatcher.find()){
           capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
       }

       return capMatcher.appendTail(capBuffer).toString();
   }
}
