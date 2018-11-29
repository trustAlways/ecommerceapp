package com.medassi.ecommerce.user.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.medassi.ecommerce.user.Activity.app.Config;
import com.google.firebase.messaging.FirebaseMessaging;



public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
            Log.e("Login","Complete");
            // gcm successfully registered
            // now subscribe to `global` topic to receive app wide notifications
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
            displayFirebaseRegId();

        } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
            // new push notification is received

            String message = intent.getStringExtra("message");
            Toast.makeText(App.getInstance(), "Push notification: " + message, Toast.LENGTH_LONG).show();
            System.out.println("Messege"+message);
        }
    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = App.getInstance().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
        {
            // txtRegId.setText("Firebase Reg Id: " + regId);
           // regid1=regId;
           // System.out.println("Reg1**"+regid1);
            Toast.makeText(App.getInstance(), "Firebase Reg Id:" + regId, Toast.LENGTH_SHORT).show();
            System.out.println("Firebase Reg Id:" + regId);
        }
        else {
            Toast.makeText(App.getInstance(), "Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();
            System.out.println("Firebase Reg Id is not received yet!");
        }

        //  txtRegId.setText("Firebase Reg Id is not received yet!");
    }
}
