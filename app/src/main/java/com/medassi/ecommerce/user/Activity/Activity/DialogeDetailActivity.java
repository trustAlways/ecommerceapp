package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.medassi.ecommerce.user.Activity.Model.CenterRepository;
import com.medassi.ecommerce.user.Activity.Model.Money;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView tv_Stockiest_name, tv_distance_kms, user_location,tv_product_name,tv_ProductPrice ,
            tv_Product_Availability,tv_specification;

    EditText edt_qty;
    ImageView cutImage;
    Button addtocart;
    String product_id,Stockiest_id,user_id;

    String Product_id, product_name,distributor_name,product_price,product_image,stockiest,product_quantity,product_sku,
            description;
    ConnectionDetector connectionDetector;
    SessionManager sessionManager;



    Context ctx;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_details);

        initXml();
        clickEvents();
        loadBuisnessData(product_id,Stockiest_id);

    }

    private void initXml() {
        ctx = this;
        sessionManager = new SessionManager(getApplicationContext());
          //tv_Stockiest_name = (TextView)findViewById(R.id.Stockiest_name);
         edt_qty = (EditText)findViewById(R.id.enterqty);
        tv_distance_kms = (TextView)findViewById(R.id.tv_distance);
        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        user_location = (TextView)findViewById(R.id.userlocation);
        tv_product_name = (TextView)findViewById(R.id.product_name);
        tv_ProductPrice = (TextView)findViewById(R.id.ProductPrice);
        tv_Product_Availability = (TextView)findViewById(R.id.Availability);

        cutImage = (ImageView)findViewById(R.id.hideDialog);
        addtocart = (Button)findViewById(R.id.addincart);

        product_id = sessionManager.getData(SessionManager.KEY_PRODUCT_ID);
        Stockiest_id = sessionManager.getData(SessionManager.KEY_STOCKIEST_ID);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);

      /*  if (itemCount != 0) {
            for (CartList cartList : CenterRepository.getCenterRepository()
                    .getListOfProductsInCart()) {

                updateCheckOutAmount(BigDecimal.valueOf(Long.valueOf(cartList.getPrice())), true);
            }
        }*/


    }

    private void clickEvents()
    {
        addtocart.setOnClickListener(this);
    }

    private void loadBuisnessData(final String product_id, String stockiest_id)
    {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("wait..");
        pd.setCancelable(false);
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.ROOT_URL+"product?product_id="+product_id+"&stockiest_id="+stockiest_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.i("response..!","product Deatil"+ response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")||msg.equals("product details")) {
                        JSONArray array = obj.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            Product_id = jsonObject.getString("product_id");
                            product_sku = jsonObject.getString("product_sku");
                            product_name = jsonObject.getString("product_name");
                            product_image = jsonObject.getString("product_image");
                            distributor_name = jsonObject.getString("distributor_name");
                            description = jsonObject.getString("product_description");


                            JSONArray jsonArraystockiest_id = jsonObject.getJSONArray("stockiest_id");


                            for (int j = 0; j < jsonArraystockiest_id.length(); j++) {
                                stockiest = String.valueOf(jsonArraystockiest_id.getInt(j));
                            }

                            JSONArray jsonArrayprice = jsonObject.getJSONArray("product_price");
                            for (int k = 0; k < jsonArrayprice.length();k++) {
                                product_price = jsonArrayprice.getString(k);
                            }

                            JSONArray jsonArrayqty = jsonObject.getJSONArray("product_qty");
                            for (int k = 0; k < jsonArrayqty.length();k++) {
                                product_quantity = jsonArrayqty.getString(k);
                            }


                        }
                        setData(product_name,product_price,product_image,product_quantity,distributor_name);

                    }
                    else
                    {
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

    private void setData(String product_name, String product_price, String product_image, String product_quantity, String distributor_name)
    {

         tv_Stockiest_name.setText(distributor_name);

         tv_distance_kms.setText(product_name);

         tv_product_name.setText(product_name);

         tv_ProductPrice.setText(product_price);

         if (this.product_quantity.equals("0"))
         {
            tv_Product_Availability.setText("out of stock");
            edt_qty.setEnabled(false);
         }
         else {
            tv_Product_Availability.setText(product_quantity + "Unit Available");
         }
       /* String url = product_image;
        if (!url.equals("")) {
            String icon_url = "http://192.168.1.4/webservices/uploads/stockiest_"+ stockiest+"/" + url;
            icon_url = icon_url.replace(" ","%20");
            Log.i("response..!","icon url"+ icon_url);
            Picasso.with(ctx)
                    .load(icon_url)
                    .resize(100, 100)
                    .placeholder(R.drawable.ic_new_web_logo)
                    .error(R.drawable.ic_new_web_logo)
                    .into(cutImage);
        }*/
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addincart) {
            loadAddCart();
        }
    }

    private void loadAddCart() {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("wait..");
        //pd.setCancelable(false);
        pd.show();
        final String quantity = edt_qty.getText().toString();
        if (TextUtils.isEmpty(quantity))
        {
            pd.dismiss();
            edt_qty.setError("please enter quantity");
            edt_qty.requestFocus();
            return;

        }
        /*final String code = item_count.getText().toString();
        final String quantity = add_item_number.getText().toString();

        if (TextUtils.isEmpty(code))
        {
            pd.dismiss();
            item_count.setError("please enter Product Code");
            item_count.requestFocus();
            return;

        }

*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDTOCART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    Log.i("response..!","add_to_card%%%"+ response);
                    //sessionManager.setData(SessionManager.KEY_CART_DATA,response);

                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.i("response..!","add_to_card"+ response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")|| msg.equals("product added to cart successfully!")) {
                        Toast.makeText(ctx,"product added to cart successfully.",Toast.LENGTH_SHORT).show();
                    }
                     else if (Status.equals("Expired token"))
                    {
                        pd.dismiss();
                        Toast.makeText(ctx, "Please login Again.", Toast.LENGTH_LONG).show();
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
                params.put("available_qty",product_quantity);
                params.put("product_description",description);
                params.put("product_image",product_image);

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
}
