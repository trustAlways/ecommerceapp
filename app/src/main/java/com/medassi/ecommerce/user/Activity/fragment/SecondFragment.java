package com.medassi.ecommerce.user.Activity.fragment;

import android.app.Dialog;
//import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.medassi.ecommerce.user.Activity.Activity.MapsActivity;
import com.medassi.ecommerce.user.Activity.Model.CartList;
import com.medassi.ecommerce.user.Activity.Model.ForLatLongitude;
import com.medassi.ecommerce.user.Activity.Model.Stockiestdetail;
import com.medassi.ecommerce.user.Activity.utils.MySharedPref;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;
import com.medassi.ecommerce.user.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SecondFragment extends Fragment implements OnMapReadyCallback

{
    TextView tv_Stockiest_name,tv_stockretail, tv_distance_kms, user_location,tv_product_name,tv_ProductPrice ,
            tv_Product_Availability,tv_specification,tv_info,
            Search_fro,show_search_result,
            searched_product_list1,mapview1,
            searched_product_list2,mapview2;

    ArrayList<CartList> search_list;

    String Product_id, product_name,distributor_name,product_price,product_image,stockiest,product_quantity,product_sku,
            description,distance,solid_qunty,stockiest_retailer_name,stockiest_type,stockiest_distance,stockiest_product_qty,stockiest_product_price,type;

    public static LinearLayout Linear_map,Linear_list;
    public static ListView listView;
    MapsActivity.StockiestListAdapter stockiestListAdapter;
    EditText edt_qty;
    String product_quntity;

    ImageView cutImage;
    Button addtocart;
    String product_id,Stockiest_id,Stockiest_Idd;
    String addresss,city,country;
    String token;
    List<Address> address;
    double dist;
    private GoogleMap mMap;
    SessionManager sessionManager;
    public Context ctx;
    String name;
    String id, Stockiest_Id,user_id, user_add;
    double latitude, longitude,currentlat,currentlong;
    LocationManager mlocationmanager;
    LocationListener mLocationListener;
    Dialog mydialog;
    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";
    Gson gson;
    private MySharedPref sharedPreference;
    private ArrayList<ForLatLongitude> scores;
    ArrayList<String> arr_stokist;
    ArrayList<String> arr_lat;
    ArrayList<String> arr_lon;
    ArrayList<String> arr_product_prize;
    ArrayList<String> arr_product_avail_qty;
    ArrayList<Stockiestdetail> stockiest_detail;

    JSONArray jsonArray6,jsonArray7;
    String json1,json2,json3,json4,json5;
    int k,a;

    double dub_lat4,dub_lon4;
    String stokeist_id4;
    ForLatLongitude forLatLongitude;
    HashMap<String, String> hashmap;
    String str_arraylist_lat_valueeee,str_arraylist_lon_valueeee,str_arraylist_stokiest_valueeee,str_arraylist_prodct_prize_valueeee,str_arraylist_avl_qty_valueeee;
    int q;
    boolean bool_check_data=false;
 View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.secondfragment, null);


        //creating a support fragment for map
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
