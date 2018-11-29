package com.medassi.ecommerce.user.Activity.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.medassi.ecommerce.user.Activity.Activity.LoginActivity;
import com.medassi.ecommerce.user.Activity.Activity.RecyclerViewActivity;
import com.medassi.ecommerce.user.Activity.Model.User;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context ctx;

    int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "candidtechnologysharedpref";
    public static final String IS_LOGIN = "IsLoggedIn";

    public static final String RUN_FIRST = "firstTime";

    public static final String KEY_USER_ID = "userid";
    public static final String KEY_ORDER_ID = "orderid";

    public static final String KEY_FIRST_NAME = "keyfirstname";
    public static final String KEY_LAST_NAME = "keylastname";

    public static final String KEY_EMAIL = "keyemail";
    public static final String KEY_MOBILE = "keymobile";
    public static final String KEY_SEARCH_DATA = "search_data";
    public static final String KEY_PRODUCT_ID = "keyproductid";
    public static final String KEY_PRODUCT_NAME = "keyproductname";
    public static final String KEY_USER_CURRENT_ADD = "keyusercurrentadd";
    public static final String  KEY_STOCKIEST_ID ="keystockiestid";
    public static final String KEY_LST_SIZE = "keylistsize";
    public  static final String  KEY_CART_ITEM_COUNT = "keycartitemcount";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TOKEN_DEVICE = "devicetoken";

    public static final String KEY_UPDATE_QTY = "updateqty";

    public static final String KEY_UPDATE_QTY_COUNT = "count_qty";

    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";

    public static final String KEY_BILL_1 = "keybill1";
    public static final String KEY_BILL_2 = "keybill2";
    public static final String KEY_SHIPP_1 = "keyshipp1";
    public static final String KEY_SHIPP_2 = "keyshipp2";

    public SessionManager(Context context) {
        this.ctx = context;
        pref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /*public void createLoginSession(String name, String mobile, String email) {

    }*/

    public void createLoginSession(User user)
    {
        editor.putString(KEY_FIRST_NAME, user.getF_name());
        editor.putString(KEY_LAST_NAME,user.getL_name());
        editor.putString(KEY_MOBILE, user.getMobile());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(ctx, RecyclerViewActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, null));
        user.put(KEY_LAST_NAME, pref.getString(KEY_LAST_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }

    public void logoutUser() {

        editor.clear();
        editor.commit();
        Intent i = new Intent(ctx, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isRunable() {
        return pref.getBoolean(RUN_FIRST, false);
    }


    public String getData(String key) {
        return pref.getString(key, null);
    }
    public void setData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

}
