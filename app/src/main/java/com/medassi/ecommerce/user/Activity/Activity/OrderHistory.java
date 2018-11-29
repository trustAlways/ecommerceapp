package com.medassi.ecommerce.user.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.FrameLayout;
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
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.medassi.ecommerce.user.Activity.Model.order_history_detail;
import com.medassi.ecommerce.user.Activity.utils.ConnectionDetector;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderHistory extends Fragment {
    Context mContext;
    ArrayList<order_history_detail> orderdItem_list;
     FrameLayout noItemLayout;
     LinearLayout orderedItem;

     Button btn_start_shopping,btn_addproducts;

    RecyclerView recyclerView;
    SimpleStringRecyclerViewAdapter simpleStringRecyclerViewAdapter;
    AllProductlistAdapter allProductlistAdapter;
    String order_id,order_number,order_date,order_status,payment_mode,shipping_full_name,shipping_phone_number,
    shipping_address_one,shipping_address_two,billing_address,sub_total,total;
    int c;
    int b=1;
    int f;
    int l;
     ProgressDialog pd;
    SessionManager sessionManager;
    ConnectionDetector connectionDetector;
    String token,user_id ,ordr_id;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_order_history,null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("My Orders");

        initView();
        boolean internet = connectionDetector.isConnected(mContext);
        if (internet) {
            //init();
            setTvAdapter();
        }
        else {
            Toast.makeText(mContext, "No internet connectiom", Toast.LENGTH_SHORT).show();
        }
        clickevent();

        return v;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);



    }
*/
    private void init() {
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(simpleStringRecyclerViewAdapter)
                .load(R.layout.layout_cartlist_item)
                .show();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //setTvAdapter();
                skeletonScreen.hide();
            }
        }, 1000);
        return;

    }


    private void clickevent() {
        btn_start_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RecyclerViewActivity.class);
               startActivity(intent);
            }
        });

       btn_addproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderdItem_list!=null) {


                    if (b < c)
                    {
                        System.out.println("Value of B^^^" + b);
                        System.out.println("Value of L^^^" + l);
                        System.out.println("Value of C^^^" + c);
                        b = b + 1;
                        l = l + 10;
                        System.out.println("Valueinc of B^^^" + b);
                        System.out.println("Valueinc of L^^^" + l);
                        System.out.println("Valueinc of C^^^" + c);

                        allProductlistAdapter = new AllProductlistAdapter(recyclerView, orderdItem_list,l);
                        recyclerView.setAdapter(allProductlistAdapter);
                        allProductlistAdapter.notifyDataSetChanged();
                    } else {

                        System.out.println("F value is%%%"+f);

                        if(f==0)
                        {
                            btn_addproducts.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();

                        }

                        else
                        {
                            f=l+f;
                            System.out.println("F value is^^^"+f);

                            allProductlistAdapter = new AllProductlistAdapter(recyclerView, orderdItem_list,f);
                            recyclerView.setAdapter(allProductlistAdapter);
                            allProductlistAdapter.notifyDataSetChanged();
                            btn_addproducts.setVisibility(View.GONE);
                        }


                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initView() {
         mContext = getActivity();
        orderdItem_list = new ArrayList<>();
        pd = new ProgressDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        connectionDetector = new ConnectionDetector();


        noItemLayout = (FrameLayout)v.findViewById(R.id.default_nodata);
        orderedItem = (LinearLayout)v.findViewById(R.id.OrderedItem);
        btn_start_shopping= (Button)v.findViewById(R.id.start_shopping);

        btn_addproducts = (Button)v.findViewById(R.id.btn_load_more);

        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);

         recyclerView = (RecyclerView)v.findViewById(R.id.Order_list_recycler_view);
         RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);
         recyclerView.setLayoutManager(recylerViewLayoutManager);
         //simpleStringRecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(recyclerView, orderdItem_list);
         //recyclerView.setAdapter(simpleStringRecyclerViewAdapter);

    }

    private void setTvAdapter() {
        pd.setCancelable(false);
        pd.setMessage("Loading..");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_USER_ORDER_HISTORY,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    Log.i("response..!","Order History"+ response);
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")|| msg.equals("your cart items!")) {

                        JSONArray array = obj.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            order_id = jsonObject.getString("order_id");
                            order_number = jsonObject.getString("order_number");
                            order_date = jsonObject.getString("order_date");
                            order_status = jsonObject.getString("order_status");
                            payment_mode = jsonObject.getString("payment_mode");
                            shipping_full_name = jsonObject.getString("shipping_full_name");
                            shipping_phone_number = String.valueOf(jsonObject.getInt("shipping_phone_number"));
                            shipping_address_one = jsonObject.getString("shipping_address_one");
                            shipping_address_two = jsonObject.getString("shipping_address_two");
                            billing_address = jsonObject.getString("billing_address_one");
                            sub_total = jsonObject.getString("sub_total");
                            total = jsonObject.getString("total");

                            orderdItem_list.add(new order_history_detail(order_id,order_number,order_date,order_status,payment_mode,
                                    shipping_full_name,shipping_phone_number,
                                    shipping_address_one,shipping_address_two,
                                    billing_address,sub_total,total));

                            //simpleStringRecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(recyclerView, orderdItem_list);
                           // recyclerView.setAdapter(simpleStringRecyclerViewAdapter);
                        }
                        setView(orderdItem_list);
                    }
                    else if (Status.equals("failed"))
                    {
                        pd.dismiss();
                        orderedItem.setVisibility(View.GONE);
                        noItemLayout.setVisibility(View.VISIBLE);

                        String n = capitalize(msg);
                        Toast.makeText(mContext, n, Toast.LENGTH_SHORT).show();
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
                pd.dismiss();
                Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                noItemLayout.setVisibility(View.VISIBLE);
                orderedItem.setVisibility(View.GONE);
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

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent  = new Intent(OrderHistory.this,RecyclerViewActivity.class);
        startActivity(intent);

    }*/

    private void setView(ArrayList<order_history_detail> orderdItem_list) {
        if (orderdItem_list.size()>0) {
            orderedItem.setVisibility(View.VISIBLE);
            noItemLayout.setVisibility(View.GONE);

            if (orderdItem_list.size() <= 10) {

                simpleStringRecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(recyclerView, orderdItem_list);
                recyclerView.setAdapter(simpleStringRecyclerViewAdapter);
                simpleStringRecyclerViewAdapter.notifyDataSetChanged();

            }
            if (orderdItem_list.size()> 10) {
                 l=0;

                l=l+10;
                allProductlistAdapter = new AllProductlistAdapter(recyclerView, orderdItem_list,l);
                recyclerView.setAdapter(allProductlistAdapter);
                allProductlistAdapter.notifyDataSetChanged();

                c=orderdItem_list.size()/10;
                f=orderdItem_list.size()%10;
                System.out.println("C Value is$$$"+c);

                btn_addproducts.setVisibility(View.VISIBLE);

            }

        }
        else
        {
            noItemLayout.setVisibility(View.VISIBLE);
            orderedItem.setVisibility(View.GONE);
        }
    }
    public class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<order_history_detail> mCartlistImageUri;
        private RecyclerView mRecyclerView;
        SessionManager sessionManager;

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final LinearLayout mLayoutItem;
            TextView ordrno,ordrdate,ordrprice,ordrstatus,paymntmode;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image_cartlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                ordrno = (TextView)view.findViewById(R.id.ordernumber);
                ordrdate = (TextView)view.findViewById(R.id.orderdate);
                ordrprice = (TextView)view.findViewById(R.id.product_price);
                ordrstatus = (TextView)view.findViewById(R.id.orderStatus);
                paymntmode = (TextView)view.findViewById(R.id.paymentmode);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<order_history_detail> wishlistImageUri) {
            mCartlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
            sessionManager = new SessionManager(mContext);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

             holder.ordrno.setText("Order No "+mCartlistImageUri.get(position).getOrder_number());
             holder.ordrdate.setText(mCartlistImageUri.get(position).getOrder_date());
             holder.ordrprice.setText(mCartlistImageUri.get(position).getTotal());

               String sttus = capitalize(mCartlistImageUri.get(position).getOrder_status());
             holder.ordrstatus.setText("Status: "+sttus);

             holder.paymntmode.setText("Payment: "+mCartlistImageUri.get(position).getPayment_mode());

          /*  final Uri uri = Uri.parse(mCartlistImageUri.get(position).toString());
            holder.mImageView.setImageURI(uri);*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionManager.setData(SessionManager.KEY_ORDER_ID,mCartlistImageUri.get(position).getOrder_id());
                    sessionManager.setData(SessionManager.KEY_USER_ID,user_id);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new OrderDetailActivity())
                            .addToBackStack("2")
                            .commit();

                   // Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    // startActivity(intent);
                }
            });

        }
        @Override
        public int getItemCount() {
            return mCartlistImageUri.size();
        }
    }

    public class AllProductlistAdapter
            extends RecyclerView.Adapter<AllProductlistAdapter.ViewHolder> {

        private ArrayList<order_history_detail> mCartlistImageUri;
        private RecyclerView mRecyclerView;
        SessionManager sessionManager;
        int j;
        public  class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final LinearLayout mLayoutItem;
            TextView ordrno,ordrdate,ordrprice,ordrstatus,paymntmode;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image_cartlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                ordrno = (TextView)view.findViewById(R.id.ordernumber);
                ordrdate = (TextView)view.findViewById(R.id.orderdate);
                ordrprice = (TextView)view.findViewById(R.id.product_price);
                ordrstatus = (TextView)view.findViewById(R.id.orderStatus);
                paymntmode = (TextView)view.findViewById(R.id.paymentmode);

            }
        }

        public AllProductlistAdapter(RecyclerView recyclerView, ArrayList<order_history_detail> wishlistImageUri, int l) {
            mCartlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
            sessionManager = new SessionManager(mContext);
            this.j=l;
            System.out.println("List value$$$"+j);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
          if (mCartlistImageUri!=null)
          {
              holder.ordrno.setText("Order No "+mCartlistImageUri.get(position).getOrder_number());
              holder.ordrdate.setText(mCartlistImageUri.get(position).getOrder_date());
              holder.ordrprice.setText(mCartlistImageUri.get(position).getTotal());
              String stts = capitalize(mCartlistImageUri.get(position).getOrder_status());
              holder.ordrstatus.setText("Status: "+stts);
              holder.paymntmode.setText("Payment: "+mCartlistImageUri.get(position).getPayment_mode());

          /*  final Uri uri = Uri.parse(mCartlistImageUri.get(position).toString());
            holder.mImageView.setImageURI(uri);*/
              holder.itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      sessionManager.setData(SessionManager.KEY_ORDER_ID,mCartlistImageUri.get(position).getOrder_id());
                      sessionManager.setData(SessionManager.KEY_USER_ID,user_id);
                      getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new OrderDetailActivity())
                              .addToBackStack("3")
                              .commit();
                      //  Intent intent = new Intent(mContext, OrderDetailActivity.class);
                      //  startActivity(intent);
                  }
              });
          }


        }
        @Override
        public int getItemCount() {
            return j;
        }
    }


 /*  @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(OrderHistory.this,OrderHistory.class));

    }
*/

    @Override
    public void onResume() {
        super.onResume();
        //startActivity(new Intent(mContext,OrderHistory.class));

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
