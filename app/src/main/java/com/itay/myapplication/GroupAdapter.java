package com.itay.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.List;

public class GroupAdapter extends ArrayAdapter {
    Context context;
    List<Group> GroupList;

    public GroupAdapter(@NonNull Context context, @LayoutRes int resource,
                       @IdRes int textViewResourceId, @NonNull List<Group> GroupList) {
        super(context, resource, textViewResourceId, GroupList);
        this.context = context;
        this.GroupList = GroupList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)
                context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        Group temp = GroupList.get(position);
        TextView tvpUserName = (TextView) view.findViewById(R.id.tvGroupName);
        tvpUserName.setText(temp.getGroupName());
        return view;
    }
}
