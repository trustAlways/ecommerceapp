package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.medassi.ecommerce.user.Activity.Model.Money;
import com.medassi.ecommerce.user.Activity.Model.OredrDetailBean;
import com.medassi.ecommerce.user.Activity.Model.order_history_detail;
import com.medassi.ecommerce.user.Activity.adapter.ShoppingListAdapter;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderDetailActivity extends Fragment implements View.OnClickListener {


    String order_id,order_number,order_date,order_status,payment_mode,shipping_full_name,shipping_phone_number,
            shipping_address_one,shipping_address_two,billing_address,billing_address2,order_proccessed,order_pending,order_confirm,
            order_delivery,order_completed,sub_total,total,prodct_qty,price,p_id,s_id,order_p_id,
            prdct_name,product_sku,image_url,status;

    TextView ordrno,ordrdate,ordrprice,ordrstatus,orderstatusdate,paymntmode,prc,qty,productprice,product_name,
            billing_adddress,shipping_address,shipping_name,shipping_number;
    ImageView billing_plus,billing_minus,shipping_plus,shipping_minus,shipping_in_plus,shipping_in_minus;
    Button btn_start;
    LinearLayout ll_billingadd,ll_shippingadd,ll_shipping_info;
    RecyclerView recyclerView;
    OrderDeatilAdapter orderDeatilAdapter;
    Context mContext;
    ArrayList<OredrDetailBean> orderdetail;
    ConnectionDetector connectionDetector;
    SessionManager sessionManager;
    String token,ordr_id,user_id;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.view_user_order_adpl,null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("My Order Product");

        initView();
        Boolean internet = connectionDetector.isConnected(getActivity());
        if (internet)
        {
            getOrderDetail();
        }
        else
        {
            Toast.makeText(mContext, "Please Check Your Internet Connection..!!", Toast.LENGTH_SHORT).show();
        }
        clickEvent();

        return  v;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_order_adpl);


    }*/

    private void initView() {

        mContext = getActivity();
        connectionDetector = new ConnectionDetector();
        sessionManager = new SessionManager(getActivity());
        orderdetail = new ArrayList<>();

        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        ordr_id = sessionManager.getData(SessionManager.KEY_ORDER_ID);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);

        ordrno = (TextView)v.findViewById(R.id.order_number);
        ordrdate = (TextView)v.findViewById(R.id.order_date);
        ordrprice = (TextView)v.findViewById(R.id.product_price);
        ordrstatus = (TextView)v.findViewById(R.id.order_Status);
        orderstatusdate = (TextView)v.findViewById(R.id.order_Status_date);

        paymntmode = (TextView)v.findViewById(R.id.paymode);
        prc = (TextView)v.findViewById(R.id.product_price2);
        qty = (TextView)v.findViewById(R.id.Product_qty);
        productprice = (TextView)v.findViewById(R.id.price);
        product_name = (TextView)v.findViewById(R.id.product_name);
        billing_adddress = (TextView)v.findViewById(R.id.billing_adddress);
        shipping_address = (TextView)v.findViewById(R.id.shipping_address);

        shipping_name = (TextView)v.findViewById(R.id.shipping_fullname);
        shipping_number = (TextView)v.findViewById(R.id.shipping_phonenumber);


        billing_plus = (ImageView)v.findViewById(R.id.billing_plus);
        billing_minus = (ImageView)v.findViewById(R.id.billing_minus);

        shipping_plus = (ImageView)v.findViewById(R.id.shipping_plus);
        shipping_minus = (ImageView)v.findViewById(R.id.shipping_minus);

        shipping_in_plus = (ImageView)v.findViewById(R.id.shipping_info_plus);
        shipping_in_minus = (ImageView)v.findViewById(R.id.shipping_info_minus);

        ll_billingadd = (LinearLayout) v.findViewById(R.id.ll_billingaddress);
        ll_shippingadd = (LinearLayout) v.findViewById(R.id.ll_shippingaddress);
        ll_shipping_info = (LinearLayout) v.findViewById(R.id.ll_shipping_info);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycle_products_description);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        btn_start = (Button)v.findViewById(R.id.start_shopping);

    }
    private void clickEvent() {
        btn_start.setOnClickListener(this);
        billing_plus.setOnClickListener(this);
        shipping_plus.setOnClickListener(this);
        shipping_in_plus.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
      if (v==btn_start)
      {
          Intent intent = new Intent(getActivity(),RecyclerViewActivity.class);
          startActivity(intent);
      }
        if (v==billing_plus)
        {
            billing_plus.setVisibility(View.GONE);
            billing_minus.setVisibility(View.VISIBLE);
            if (ll_billingadd.getVisibility()==View.GONE)
            {
                ll_billingadd.setVisibility(View.VISIBLE);
            }
            else
            {
                ll_billingadd.setVisibility(View.GONE);
            }
        }
        billing_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    billing_plus.setVisibility(View.VISIBLE);
                    billing_minus.setVisibility(View.GONE);
                    if (ll_billingadd.getVisibility()==View.VISIBLE)
                    {
                        ll_billingadd.setVisibility(View.GONE);
                    }
                    else
                    {
                        ll_billingadd.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


      if (v==shipping_plus)
        {
            shipping_plus.setVisibility(View.GONE);
            shipping_minus.setVisibility(View.VISIBLE);

            if (ll_shippingadd.getVisibility()==View.GONE)
            {
                ll_shippingadd.setVisibility(View.VISIBLE);
            }
            else
            {
                ll_shippingadd.setVisibility(View.GONE);
            }
        }

        shipping_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    shipping_plus.setVisibility(View.VISIBLE);
                    shipping_minus.setVisibility(View.GONE);
                    if (ll_shippingadd.getVisibility()==View.VISIBLE)
                    {
                        ll_shippingadd.setVisibility(View.GONE);
                    }
                    else
                    {
                        ll_shippingadd.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        if (v==shipping_in_plus)
        {
            shipping_in_plus.setVisibility(View.GONE);
            shipping_in_minus.setVisibility(View.VISIBLE);

            if (ll_shipping_info.getVisibility()==View.GONE)
            {
                ll_shipping_info.setVisibility(View.VISIBLE);
            }
            else
            {
                ll_shipping_info.setVisibility(View.GONE);
            }
        }

        shipping_in_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    shipping_in_plus.setVisibility(View.VISIBLE);
                    shipping_in_minus.setVisibility(View.GONE);
                    if (ll_shipping_info.getVisibility()==View.VISIBLE)
                    {
                        ll_shipping_info.setVisibility(View.GONE);
                    }
                    else
                    {
                        ll_shipping_info.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }

    private void getOrderDetail() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Loading..");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_ORDER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    Log.i("response..!","Order Detail"+ response);
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")|| msg.equals("your order details!")) {
                        JSONObject jsonObj = obj.getJSONObject("result");

                        JSONArray array = jsonObj.getJSONArray("order_details");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            order_id = jsonObject.getString("order_id");
                            order_number = jsonObject.getString("order_number");
                            order_date = jsonObject.getString("order_date");
                            order_status = jsonObject.getString("order_status");
                            status = jsonObject.getString("status");
                            payment_mode = jsonObject.getString("payment_mode");
                            shipping_full_name = jsonObject.getString("shipping_full_name");
                            shipping_phone_number = String.valueOf(jsonObject.getInt("shipping_phone_number"));
                            shipping_address_one = jsonObject.getString("shipping_address_one");
                            shipping_address_two = jsonObject.getString("shipping_address_two");
                            billing_address = jsonObject.getString("billing_address_one");
                            billing_address2 = jsonObject.getString("billing_address_two");

                            order_proccessed = jsonObject.getString("processed_status_date");
                            order_pending = jsonObject.getString("pending_status_date");
                            order_confirm = jsonObject.getString("confirmed_status_date");
                            order_delivery = jsonObject.getString("delivered_status_date");
                            order_completed = jsonObject.getString("completed_status_date");

                            sub_total = jsonObject.getString("sub_total");
                            total = jsonObject.getString("total");
                            prodct_qty = jsonObject.getString("qty");
                            price = jsonObject.getString("price");
                            p_id = jsonObject.getString("product_id");
                            s_id = jsonObject.getString("stockiest_id");
                            order_p_id = jsonObject.getString("order_product_id");
                            prdct_name = jsonObject.getString("product_name");
                            product_sku =jsonObject.getString("product_sku");


                            image_url = jsonObject.getString("image_url");
                           orderdetail.add(new OredrDetailBean(order_id,order_number,order_date,order_status,payment_mode,
                                   total,prodct_qty,price,p_id,
                                   shipping_full_name,sub_total,order_p_id,
                                   s_id,prdct_name,billing_address,
                                   billing_address2,
                                   shipping_address_one,
                                   shipping_address_two,product_sku,shipping_phone_number,
                                   image_url));

                            setData(order_number,order_date,order_status,payment_mode,total,prodct_qty,price,p_id,
                                  s_id,prdct_name,billing_address,billing_address2,shipping_address_one,shipping_address_two,
                                    image_url,order_pending,order_proccessed,order_confirm,order_delivery,order_completed,status
                            ,shipping_full_name,
                                    shipping_phone_number);

                        }
                        orderDeatilAdapter = new OrderDeatilAdapter(mContext,orderdetail);
                        recyclerView.setAdapter(orderDeatilAdapter);
                    }
                    else if (Status.equals("failed"))
                    {
                        pd.dismiss();
                        String msgg = capitalize(msg);
                        Toast.makeText(mContext, msgg, Toast.LENGTH_SHORT).show();
                    }
                    else if (Status.equals("Expired token"))
                    {
                        pd.dismiss();
                        Toast.makeText(mContext, "Please login Again.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("order_id",ordr_id);
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

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }

    private void setData(String order_number, String order_date, String order_status,
                         String payment_mode, String total, String prodct_qty, String price,
                         String p_id, String s_id, String prdct_name, String billing_address,
                         String shipping_address_one, String shipping_address_two, String shippingAddressTwo,
                         String image_url, String order_pending, String order_proccessed,
                         String order_confirm,String order_delivery,String order_completed,String status
    ,String name,String phone_number) {

        ordrno.setText("Order Number: "+order_number);
        ordrdate.setText(order_date);
        shipping_name.setText(name);
        shipping_number.setText(phone_number);

        String otal_order_status = capitalize(order_status);

        Log.i("date","order"+ status);

        Log.i("date","order"+ order_pending);
        Log.i("date","order"+ order_proccessed);
        Log.i("date","order"+ order_confirm);
        Log.i("date","order"+ order_delivery);
        Log.i("date","order"+ order_completed);



        if (status.equals("pending"))
        {
            ordrstatus.setText(otal_order_status);
            String input = order_pending;
            String output = input.substring(0, 10);
            orderstatusdate.setText(output);
        }
        else if (status.equals("confirmed"))
        {
            ordrstatus.setText(otal_order_status);
            String input = order_confirm;
            String output = input.substring(0, 10);
            orderstatusdate.setText(output);
        }
        else if (status.equals("processed"))
        {
            ordrstatus.setText(otal_order_status);
            String input = order_proccessed;
            String output = input.substring(0, 10);
            orderstatusdate.setText(output);
        }

        else if (status.equals("delivered"))
        {
            ordrstatus.setText(otal_order_status);
            String input = order_delivery;
            String output = input.substring(0, 10);
            orderstatusdate.setText(output);
        }
        else
        {
            ordrstatus.setText(otal_order_status);
            String input = order_completed;
            String output = input.substring(0, 10);
            orderstatusdate.setText(output);
        }

        ordrprice.setText("Order Total: "+total);
        paymntmode.setText(payment_mode);

        billing_adddress.setText(billing_address+", "+shipping_address_one);
        shipping_address.setText(shipping_address_two+", "+shippingAddressTwo);

    }
    public class OrderDeatilAdapter
            extends RecyclerView.Adapter<OrderDetailActivity.OrderDeatilAdapter.ViewHolder> {

        private ArrayList<OredrDetailBean> mOredrDetailBean;
        private Context  mctx;
        SessionManager sessionManager;

        public OrderDeatilAdapter(Context mContext, ArrayList<OredrDetailBean> orderdetail) {
            mctx = mContext;
            mOredrDetailBean = orderdetail;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            TextView product_name,product_code,ordered_qty,product_price,sub_total;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.product_image);
                product_name= (TextView)view.findViewById(R.id.txt_product_name);
                product_code = (TextView)view.findViewById(R.id.txt_product_code);
                ordered_qty = (TextView)view.findViewById(R.id.txt_ordered_qty);
                product_price = (TextView)view.findViewById(R.id.txt_product_pr_prc);
                sub_total = (TextView)view.findViewById(R.id.txt_order_total);

            }
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            //holder.mImageView.setImageResource(R.drawable.logo);
            String n = capitalize(mOredrDetailBean.get(position).getPrdct_name());
            holder.product_name.setText(n);

            holder.product_code.setText(mOredrDetailBean.get(position).getShipping_phone_number());

            holder.ordered_qty.setText(mOredrDetailBean.get(position).getProdct_qty());
            holder.product_price.setText(mOredrDetailBean.get(position).getPrice());
            String url = mOredrDetailBean.get(position).getImage_url();
            Log.i("response..!","icon url"+ url);

            String nw = url;
           /* if (!url.equals("")) {
                //String icon_url = "http://motaso.com/mapps/uploads/stockiest_"+ order_id +"/" + url;
                //icon_url = icon_url.replace(" ","%20");
               // Log.i("response..!","icon url"+ icon_url);

                Picasso.with(mctx)
                        .load(url)
                        .resize(100, 100)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.mImageView);
            }*/

            if (!nw.equals("false")) {

                //String icon_url = "http://192.168.1.2/webservices/uploads/stockiest_"+ Stockiest_id+"/" + url;
                //icon_url = icon_url.replace(" ","%20");


                String url1 = nw;
                String new_url = url1.replace("candid-15-pc","192.168.1.8");

                Log.i("response..!","icon url"+ url1);
                Log.i("response..!","icon url"+ new_url);

            Glide.with(mctx)
                    .load(url1)
                    //.thumbnail(0.5f)
                   // .crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.logo)
                    .into(holder.mImageView);

               /* Picasso.with(mctx)
                        .load(url)
                        .rotate(90)
                        //.resize(100, 100)
                        //.placeholder(R.drawable.logo)
                        //.error(R.drawable.logo)
                        .into(holder.mImageView);*/
            }




            double baseprice=0;
            int qut=0;
            double totalprice = 0;
            for(int i = 0; i < mOredrDetailBean.size(); i++) {

                     baseprice = Double.parseDouble(mOredrDetailBean.get(position).getPrice());
                     qut = (int) Double.parseDouble(mOredrDetailBean.get(position).getProdct_qty());
                     totalprice = baseprice * qut;
                     holder.sub_total.setText(Money.rupees(BigDecimal.valueOf(totalprice)).toString());
            }

        }

        @Override
        public OrderDetailActivity.OrderDeatilAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_order_adpl_descp, parent, false);
            return new OrderDetailActivity.OrderDeatilAdapter.ViewHolder(view);
        }


        @Override
        public int getItemCount() {
            return mOredrDetailBean.size();
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
}
