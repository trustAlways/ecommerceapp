package com.medassi.ecommerce.user.Activity.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.medassi.ecommerce.user.R;

public class NewOrderHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_order_history);

        Fragment fragment = new OrderHistory();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new OrderHistory()).commit();
    }

  /*  @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewOrderHistory.this,RecyclerViewActivity.class);
        startActivity(intent);
    }*/
}
