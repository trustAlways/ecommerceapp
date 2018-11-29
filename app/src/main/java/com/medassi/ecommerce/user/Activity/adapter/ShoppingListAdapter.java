/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.medassi.ecommerce.user.Activity.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.medassi.ecommerce.user.Activity.Activity.DetailActivity;
import com.medassi.ecommerce.user.Activity.Activity.MyCartActivty;
import com.medassi.ecommerce.user.Activity.Activity.RecyclerViewActivity;
import com.medassi.ecommerce.user.Activity.Model.CartList;
import com.medassi.ecommerce.user.Activity.Model.CenterRepository;
import com.medassi.ecommerce.user.Activity.Model.Money;
import com.medassi.ecommerce.user.Activity.Model.Search;
import com.medassi.ecommerce.user.Activity.customview.ItemTouchHelperAdapter;
import com.medassi.ecommerce.user.Activity.customview.ItemTouchHelperViewHolder;
import com.medassi.ecommerce.user.Activity.customview.OnStartDragListener;
import com.medassi.ecommerce.user.Activity.customview.TextDrawable;
import com.medassi.ecommerce.user.Activity.utils.ColorGenerator;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.Activity.utils.URLs;
import com.medassi.ecommerce.user.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to
 * respond to move and dismiss events from a
 * {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Hitesh Sahu (hiteshsahu.com)
 */
   public class ShoppingListAdapter extends
        RecyclerView.Adapter<ShoppingListAdapter.ItemViewHolder> {

    private static OnItemClickListener clickListener;
    //private final OnStartDragListener mDragStartListener;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private String ImageUrl;
    SessionManager sessionManager;
    private List<CartList> productList = new ArrayList<CartList>();
     Activity context;
    double totalprice;
    String token,product_id,user_id,id,product_name,product_sku,desc,cost,qty;
    String updateqty;
    TextView delete_item;

    public ShoppingListAdapter(Context context, ArrayList<CartList> search_list) {
       // mDragStartListener = dragStartListener;
        this.context = (Activity) context;
        productList = search_list;//CenterRepository.getCenterRepository().getListOfProductsInShoppingList();
        sessionManager = new SessionManager(context);
        token = sessionManager.getData(SessionManager.KEY_TOKEN);
        user_id = sessionManager.getData(SessionManager.KEY_USER_ID);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_product_list, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        MyCartActivty.updateCheckOutSize(productList.size());

         sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(productList.size()));
        /* final Double price1 = Double.parseDouble(productList.get(position).getPrice());
         int qut = Integer.parseInt(productList.get(position).getQty());
         Double total = (price1 * qut);*/



        Log.i("qty","size,45,!!"+  productList.size());

        double baseprice=0;
        int qut=0;
        int myproduct = 0;
        double finalprice = 0;
        for(int i = 0; i < productList.size(); i++) {

            myproduct = Integer.parseInt(productList.get(position).getProduct_id());
            baseprice = Double.parseDouble(productList.get(i).getPrice());
            qut = (int) Double.parseDouble(productList.get(i).getQty());

            sessionManager.setData(SessionManager.KEY_UPDATE_QTY, String.valueOf(qut));


            totalprice = baseprice*qut;
            Log.i("qty","qty,,!!"+ qut);
            Log.i("qty","baseprice,,!!"+ baseprice);
            Log.i("qty","totalprice,,!!"+ totalprice);
            Log.i("qty","myproduct,,!!"+ myproduct);
            finalprice = finalprice + totalprice;

            product_id = productList.get(position).getProduct_id();
            qty = productList.get(position).getAvailable_qty();
            id = productList.get(position).getStockiest_id();

            Log.i("qty","piddd,,!!"+ product_id);
            Log.i("qty","qttttt,,!!"+ qty);
            Log.i("qty","idd,,!!"+ id);


        }

       MyCartActivty.updateCheckOutAmount(finalprice);

        String n = capitalize(productList.get(position).getName());
        holder.itemName.setText(n);

        product_name = productList.get(position).getName();
        product_sku = productList.get(position).getProduct_Sku();

        holder.itemDesc.setText(productList.get(position).getProduct_desc());
        desc = productList.get(position).getProduct_desc();

        final int quantity = Integer.parseInt(productList.get(position).getAvailable_qty());

        if (quantity==0)
        {
            holder.availability.setText("Out Of Stock");
            holder.addItem.setVisibility(View.INVISIBLE);
            holder.removeItem.setVisibility(View.INVISIBLE);
            holder.quanitity.setVisibility(View.GONE);

        }
        else {
            holder.availability.setText("Available");
            holder.quanitity.setText(productList.get(position).getQty());
        }


       /* holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setData(SessionManager.KEY_PRODUCT_ID, productList.get(position).getId());
                sessionManager.setData(SessionManager.KEY_STOCKIEST_ID,productList.get(position).getStockiest_id());
                Intent intent = new Intent(context,DetailActivity.class);
                context.startActivity(intent);
            }
        });*/

        final double finalBaseprice2 = baseprice;
     delete_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Are you sure,you want to delete this item?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String  product_idd = productList.get(position).getProduct_id();
                        String cart_id = productList.get(position).getCart_id();

                        Log.i("product","id,,!!"+ product_idd);
                        Log.i("product","sku,,!!"+ cart_id);

                        removecartItem(product_idd,cart_id);

                        productList.remove(position);
                        notifyDataSetChanged();

                        // Toast.makeText(context, "position "+position, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "pId "+ product_idd, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(context, "uId "+ user_id, Toast.LENGTH_SHORT).show();


                        double baseprice=0;
                        for(int i = 0; i < productList.size(); i++) {
                            baseprice += Double.parseDouble(productList.get(i).getPrice());
                        }

                        MyCartActivty.updateCheckOutAmount(baseprice);

                        sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(productList.size()));
                        sessionManager.setData(SessionManager.KEY_UPDATE_QTY_COUNT,"0");

                        notifyItemRemoved(position);
                        notifyDataSetChanged();

                       MyCartActivty.updateCheckOutSize(productList.size());

                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.show();

                /*android.support.v7.app.AlertDialog.Builder exitScreenDialog =
                        new android.support.v7.app.AlertDialog.Builder(context, R.style.PauseDialog);

                exitScreenDialog.setMessage("Are you sure,You want to delete this item? ");
                exitScreenDialog.setCancelable(true);

                exitScreenDialog.setPositiveButton(
                        "Remove",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String  product_idd = productList.get(position).getProduct_id();

                                removecartItem(product_idd);

                                productList.remove(position);
                                notifyDataSetChanged();

                                // Toast.makeText(context, "position "+position, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context, "pId "+ product_idd, Toast.LENGTH_SHORT).show();
                                // Toast.makeText(context, "uId "+ user_id, Toast.LENGTH_SHORT).show();


                                double baseprice=0;
                                for(int i = 0; i < productList.size(); i++) {
                                    baseprice += Double.parseDouble(productList.get(i).getPrice());
                                }

                                ((MyCartActivty) context).updateCheckOutAmount(baseprice);

                                sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(productList.size()));
                                sessionManager.setData(SessionManager.KEY_UPDATE_QTY_COUNT,"0");

                                notifyItemRemoved(position);
                                notifyDataSetChanged();

                                ((MyCartActivty) context).updateCheckOutSize(productList.size());
                            }
                        });

                exitScreenDialog.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                android.support.v7.app.AlertDialog alert11 = exitScreenDialog.create();
                alert11.show();


                Button bq = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
                bq.setTextColor(Color.WHITE);
                Button bq1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
                bq1.setTextColor(Color.WHITE);*/

               /* String  product_idd = productList.get(position).getProduct_id();

                removecartItem(product_idd);

                productList.remove(position);
                notifyDataSetChanged();

               // Toast.makeText(context, "position "+position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "pId "+ product_idd, Toast.LENGTH_SHORT).show();
               // Toast.makeText(context, "uId "+ user_id, Toast.LENGTH_SHORT).show();


                double baseprice=0;
                for(int i = 0; i < productList.size(); i++) {
                    baseprice += Double.parseDouble(productList.get(i).getPrice());
                }

                ((MyCartActivty) context).updateCheckOutAmount(baseprice);

                  sessionManager.setData(SessionManager.KEY_CART_ITEM_COUNT, String.valueOf(productList.size()));
                  sessionManager.setData(SessionManager.KEY_UPDATE_QTY_COUNT,"0");

                 notifyItemRemoved(position);
                 notifyDataSetChanged();

                ((MyCartActivty) context).updateCheckOutSize(productList.size());
*/
            }
        });

        holder.itemCost.setText(productList.get(position).getPrice());
            cost = productList.get(position).getPrice();
        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(productList
                .get(position).getName().charAt(0)), mColorGenerator
                .getColor(productList.get(position).getName()));


        ImageUrl = productList.get(position).getProduct_image();
        String url = ImageUrl;

        if (!url.equals("")) {

            String icon_url = "http://192.168.1.8/webservices/uploads/stockiest_"+ id+"/" + url;
            icon_url = icon_url.replace(" ","%20");
            Log.i("response..!","icon url"+ icon_url);

            String url1 = ImageUrl;
            String newString = url1.replace("candid-15-pc", "192.168.1.8");

           /* Picasso.with(context)
                    .load(url)
                    .resize(100, 100)
                    .rotate(90)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(holder.imagView);*/


        Glide.with(context)
                .load(url1)
                //.thumbnail(0.5f)
                // .crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.placeholder(R.drawable.logo)
                .into(holder.imagView);

        }
        /*Picasso.with(context).load(ImageUrl).
                placeholder(drawable)
                .resize(100,100)
                .error(drawable)
                .centerCrop()
                .into(holder.imagView);*/

        // Start a drag whenever the handle view it touched
        /*holder.imagView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });*/

        final double finalBaseprice = baseprice;
        holder.addItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
           if (Integer.parseInt(productList.get(position).getQty()) < Integer.parseInt(productList.get(position).getAvailable_qty())) {
               productList
                       .get(position)
                       .setQty(
                               String.valueOf(
                                       Integer.valueOf(productList
                                               .get(position).getQty()) + 1));

               holder.quanitity.setText(productList.get(position).getQty());
               updateqty = productList.get(position).getQty();

               double fp = 0;
               for (int i = 0; i < productList.size(); i++) {
                   product_id = productList.get(position).getProduct_id();
                   qty = productList.get(position).getAvailable_qty();
                   id = productList.get(position).getStockiest_id();


                   if (Integer.parseInt(updateqty) > Integer.parseInt(qty)) {
                       holder.addItem.setClickable(false);
                       holder.quanitity.setText(qty);
                   } else {

                       loadAddCart(product_id, id, qty);

                       double pprc = Double.parseDouble(productList.get(i).getPrice());
                       int pq = (int) Double.parseDouble(productList.get(i).getQty());

                       totalprice = pprc * pq;
                       Log.i("qty", "up,,!!" + pq);
                       Log.i("qty", "baseprice,,!!" + pprc);
                       Log.i("qty", "totalprice,,!!" + totalprice);

                       fp = fp + totalprice;
                       Log.i("qty", "totalprice,,!!" + fp);
                   }
               }
              MyCartActivty.updateCheckOutAmount(fp);

           }
           else
           {
               //Toast.makeText(context, productList.get(position).getAvailable_qty()+" Quantity Available", Toast.LENGTH_SHORT).show();
               Toast.makeText(context, "You cannot add more quantity than available stock", Toast.LENGTH_SHORT).show();

           }
                //((MyCartActivty) context).updateCheckOutAmount(totalprice);
                // ((MyCartActivty) context).updateItemCount(false);
                //Utils.vibrate(context);
            }
        });


        final double finalBaseprice1 = baseprice;
        holder.removeItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Integer.valueOf(productList.get(position).getQty()) > 1) {

                    productList
                            .get(position)
                            .setQty(
                                    String.valueOf(
                                            Integer.valueOf(productList
                                                    .get(position).getQty()) - 1));

                    holder.quanitity.setText(productList.get(position).getQty());
                    updateqty = productList.get(position).getQty();

                    double fp = 0;
                    for(int i = 0; i < productList.size(); i++) {
                        product_id = productList.get(position).getProduct_id();
                        qty = productList.get(position).getAvailable_qty();
                        id = productList.get(position).getStockiest_id();
                        if (Integer.parseInt(updateqty) > Integer.parseInt(qty))
                        {
                            holder.addItem.setClickable(true);
                            holder.quanitity.setText(qty);
                        }
                        else
                        {
                            loadAddCart(product_id,id,qty);
                            double pprc = Double.parseDouble(productList.get(i).getPrice());
                            int pq = (int) Double.parseDouble(productList.get(i).getQty());
                            totalprice = pprc*pq;

                            Log.i("qty","qty,,!!"+ pq);
                            Log.i("qty","baseprice,,!!"+ pprc);
                            Log.i("qty","totalprice,,!!"+ totalprice);

                            fp = fp + totalprice;

                            Log.i("qty","totalprice,,!!"+ fp);
                        }

                    }

                   MyCartActivty.updateCheckOutAmount(fp);

                    /*int qut=0;
                    for(int i = 0; i < productList.size(); i++) {
                        qut += (int) Double.parseDouble(productList.get(i).getQty());
                    }*/

                  /*  final Double price1 = Double.parseDouble(productList.get(position).getPrice());
                    int qut = Integer.parseInt(productList.get(position).getQty());

                    Double total1 = (price1 * qut);

                    Log.i("qty","qut--,,!!"+qut);
                    Log.i("qty","total1--,,!!"+total1);

                    ((MyCartActivty) context).updateCheckOutAmount(total1);*/


                   /* ((MyCartActivty) context).updateCheckOutAmount(
                            BigDecimal.valueOf(Long.valueOf(productList
                                    .get(position).getPrice())), false);*/

                   // Utils.vibrate(context);
                } /*else if (Integer.valueOf(productList.get(position).getQty()) == 1) {

                    ((MyCartActivty) context).updateItemCount(false);

                    ((MyCartActivty) context).updateCheckOutAmount(
                            BigDecimal.valueOf(Long.valueOf(productList
                                    .get(position).getPrice())), false);

                    productList.remove(position);

                  if (Integer.valueOf(((DetailActivity) context)
                            .getItemCount()) == 0) {

                        MyCartActivty.updateMyCartFragment(false);

                    }
                }*/

            }
        });
    }


    private void loadAddCart(final String product_id, final String id, final String qty) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        final String quantity = updateqty;



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
                        //Toast.makeText(context,"product added to cart successfully!",Toast.LENGTH_SHORT).show();
                        //setTvAdapter();
                    }
                    else if (Status.equals("failed"))
                    {
                        pd.dismiss();
                        String msgg =capitalize(msg);
                        Toast.makeText(context, msgg, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pd.dismiss();
                        String msgg =capitalize(msg);
                        Toast.makeText(context, msgg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("product_id",product_id);
                params.put("stockiest_id", id);
                params.put("product_qty", quantity);
                params.put("available_qty", qty);

                Log.i("respo","qtyy"+quantity);
                Log.i("respo","uid"+user_id);
               ;Log.i("respo","pid"+product_id);
                Log.i("respo","id"+id);
                Log.i("respo","qt"+qty);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void removecartItem(final String product_idd, final String cart_id)
    {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading..!");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REMOVE_CART_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try{
                    Log.i("response..!","Cart Detail%%%"+ response);
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);
                    Log.i("response..!","add_to_card"+ response);

                    String Status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (Status.equals("success")|| msg.equals("your cart items!")) {
                        Toast.makeText(context, "Item Removed Successfully.", Toast.LENGTH_SHORT).show();
                    }

                    else if (Status.equals("failed"))
                    {
                        pd.dismiss();
                        String msgg =capitalize(msg);
                        Toast.makeText(context, msgg, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pd.dismiss();
                        String msgg = capitalize(msg);
                        Toast.makeText(context, msgg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product_idd);
                params.put("user_id",user_id);
                params.put("cart_id",cart_id);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }




    /*@Override
    public void onItemDismiss(int position) {

        ((ECartHomeActivity) context).updateItemCount(false);

        ((ECartHomeActivity) context).updateCheckOutAmount(
                BigDecimal.valueOf(Long.valueOf(CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList().get(position)
                        .getSellMRP())), false);

        CenterRepository.getCenterRepository().getListOfProductsInShoppingList().remove(position);

        if (Integer.valueOf(((ECartHomeActivity) context).getItemCount()) == 0) {

            MyCartFragment.updateMyCartFragment(false);

        }

        // productList.remove(position);
        notifyItemRemoved(position);
    }*/

    public boolean onItemMove(int fromPosition, int toPosition) {

        Collections.swap(productList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }



    @Override
    public int getItemCount() {
        return productList.size();
        }


    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    /**
     * Simple example of a view holder that implements
     * {@link ItemTouchHelperViewHolder} and has a "handle" view that initiates
     * a drag event when touched.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, OnClickListener {

        // public final ImageView handleView;

        TextView itemName, itemDesc, itemCost, availability, quanitity,
                addItem, removeItem;
        ImageView imagView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            // handleView = (ImageView) itemView.findViewById(R.id.handle);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            itemCost = (TextView) itemView.findViewById(R.id.item_price);

            availability = (TextView) itemView
                    .findViewById(R.id.iteam_avilable);

            quanitity = (TextView) itemView.findViewById(R.id.iteam_quantity);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

            addItem = (TextView) itemView.findViewById(R.id.add_item);

            removeItem = (TextView) itemView.findViewById(R.id.remove_item);
            delete_item = (TextView) itemView.findViewById(R.id.iteam_Delete);

            //itemView.setOnClickListener(this);


        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) {

            //clickListener.onItemClick(v, getPosition());
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
