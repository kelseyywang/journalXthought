//This is my adapter for the ListView
package com.journalxapp.kelseywang.journalx;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ListElement> objects;

    //Defines the ViewHolder
    private class ViewHolder {
        CardView card_view;
        TextView q1_tv;
        TextView q2_tv;
        TextView month;
        ImageView image_view;
    }

    //Constructor for this CustomAdaptor
    public CustomAdapter(Context context, ArrayList<ListElement> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    //Returns the count of objects
    public int getCount() {
        return objects.size();
    }

    //Returns item as specified position
    public ListElement getItem(int position) {
        return objects.get(position);
    }

    //Returns item's index as id
    public long getItemId(int position) {
        return position;
    }

    //returns the View with list elements set appropriately
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_element, null);
            holder.q1_tv = (TextView) convertView.findViewById(R.id.q1_tv);
            holder.q2_tv = (TextView) convertView.findViewById(R.id.q2_tv);
            holder.month = (TextView) convertView.findViewById(R.id.month);
            holder.image_view = (ImageView) convertView.findViewById(R.id.image_view);
            holder.card_view = (CardView) convertView.findViewById(R.id.card_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.q1_tv.setText(objects.get(position).getQ1());
        holder.q2_tv.setText(objects.get(position).getQ2());

        TextDrawable drawable = TextDrawable.builder()
                .buildRoundRect(objects.get(position).getDrawableChar(), Color.parseColor("#6d87ce"), 25);
        holder.month.setText(objects.get(position).getMonth());
        holder.image_view.setImageDrawable(drawable);
        int colorInt = objects.get(position).getColor();

        if (colorInt == 1) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#dce2f7"));
        }
        return convertView;
    }
}
