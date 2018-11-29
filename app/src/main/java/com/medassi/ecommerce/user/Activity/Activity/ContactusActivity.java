package com.medassi.ecommerce.user.Activity.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.medassi.ecommerce.user.R;

import java.util.HashMap;
import java.util.Map;

public class ContactusActivity extends Fragment {

    TextView contact_tv,email;
  View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.contactus,null);

        TextView t =  getActivity().findViewById(R.id.header_name);
        t.setText("Contact Support");

        checkForPermission();
        email = (TextView)view.findViewById(R.id.tv_email);

        contact_tv = (TextView)view.findViewById(R.id.tv_contact_calling);
        contact_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contact = contact_tv.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +contact));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contact_email = email.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SENDTO);//common intent
                intent.setData(Uri.parse("mailto:" +contact_email));

                startActivity(intent);
            }
        });

        return view;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);

        checkForPermission();
        email = (TextView) findViewById(R.id.tv_email);

        contact_tv = (TextView) findViewById(R.id.tv_contact_calling);
        contact_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contact = contact_tv.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +contact));
                if (ActivityCompat.checkSelfPermission(ContactusActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contact_email = email.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SENDTO);//common intent
                intent.setData(Uri.parse("mailto:" +contact_email));

                startActivity(intent);
            }
        });




    }*/

    private void checkForPermission() {
        //int permissionCheckForAccessFinelocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        //int permissionCheckForAccessCoarselocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheckForCallPhone = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

        if (permissionCheckForCallPhone != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.CALL_PHONE,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1001);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("", "Permission callback called-------");
        switch (requestCode) {
            case 1001: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("", "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("Call Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkForPermission();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                 //   .show();

                            showDialogOK("Call Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getActivity().getPackageName(), null)));

                                        }

                                    });


                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }



}
