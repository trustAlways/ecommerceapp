package com.medassi.ecommerce.user.Activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medassi.ecommerce.user.Activity.Model.Navigation;
import com.medassi.ecommerce.user.Activity.utils.SessionManager;

import java.util.ArrayList;

import com.medassi.ecommerce.user.R;


/**
 * Created by kamlesh on 12/7/2017.
 */
public class NavAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<Navigation> nav_list;
    String type = "";
    SessionManager sessionManager;

    public NavAdapter(Context ctx, ArrayList<Navigation> nav_list) {
        this.ctx = ctx;
        this.nav_list = nav_list;
        sessionManager = new SessionManager(ctx);


        //type = sessionManager.getData(SessionManager.KEY_NAV_VIEW_TYPE);
    }

    @Override
    public int getCount() {
        return nav_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.nav_adapter, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_navadp_image);
        TextView name_tv = (TextView) view.findViewById(R.id.tv_navadp_name);

        imageView.setImageResource(nav_list.get(position).getImage());
        name_tv.setText(nav_list.get(position).getName() + "");


        return view;
    }
}
