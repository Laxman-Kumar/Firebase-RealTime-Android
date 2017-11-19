package com.example.shared.myfirebaseproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Laxman on 10-09-2017.
 */

public class ListviewAdapter extends BaseAdapter {
    Activity activity;
    List<User> listuser;
    LayoutInflater inflater;

    public ListviewAdapter(Activity activity, List<User> listuser) {
        this.activity = activity;
        this.listuser = listuser;
    }

    @Override
    public int getCount() {
        return listuser.size();
    }

    @Override
    public Object getItem(int i) {
        return listuser.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemview = inflater.inflate(R.layout.listview_item,null);

        TextView textUser = (TextView)itemview.findViewById(R.id.textName);
        TextView textEmail = (TextView)itemview.findViewById(R.id.textEmail);

        textUser.setText(listuser.get(i).getName());
        textEmail.setText(listuser.get(i).getEmail());

        return itemview;
    }
}
