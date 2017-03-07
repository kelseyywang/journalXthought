package com.example.kelseywang.journalx;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelseywang on 3/6/17.
 */

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ListElement> objects;

    private class ViewHolder {
        TextView q1_tv;
        TextView q2_tv;
        ImageView image_view;
    }

    public CustomAdapter(Context context, ArrayList<ListElement> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public ListElement getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_element, null);
            holder.q1_tv = (TextView) convertView.findViewById(R.id.q1_tv);
            holder.q2_tv = (TextView) convertView.findViewById(R.id.q2_tv);
            holder.image_view = (ImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.q1_tv.setText(objects.get(position).getQ1());
        holder.q2_tv.setText(objects.get(position).getQ2());
        TextDrawable drawable = TextDrawable.builder()
                .buildRoundRect(objects.get(position).getDrawableChar(), Color.RED, 10);
        holder.image_view.setImageDrawable(drawable);
        return convertView;
    }
        }
