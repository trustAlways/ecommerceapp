package com.medassi.ecommerce.user.Activity.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by technorizen on 19/4/17.
 */

public class MySharedPref {
     SharedPreferences sp;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    // Context
    private Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "pref";
    private static final String SCORES = "scores";

    public MySharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public  void removeFromSharedPreferences(Context context, String key) {

        sp=context.getSharedPreferences("E_Commerce",context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
     /*  if (mContext != null) {
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
            if (mSharedPreferences != null)
                mSharedPreferences.edit().remove(key).commit();
        }  */
    }
    public void saveHighScoreList(String scoreString) {
        editor.putString(SCORES, scoreString);
        editor.commit();
    }

    public String getHighScoreList() {
        return pref.getString(SCORES, "");
    }





}
