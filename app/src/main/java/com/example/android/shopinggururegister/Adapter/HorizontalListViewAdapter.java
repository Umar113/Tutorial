package com.example.android.shopinggururegister.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopinggururegister.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anjal on 02/12/2016.
 */
public class HorizontalListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;

    ArrayList<String> alName;
    ArrayList<String> a2Name;
    ArrayList<String> a3Name;
    ArrayList<String> a4Name;
    ArrayList<String> a5Name;
    ArrayList<String> alImage;

    public HorizontalListViewAdapter(Context context, ArrayList<String> alName,ArrayList<String> a2Name,
                                     ArrayList<String> a3Name,ArrayList<String> a4Name,ArrayList<String> a5Name,ArrayList<String> alImage) {

        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.alName = alName;
        this.a2Name = a2Name;
        this.a3Name = a3Name;
        this.a4Name = a4Name;
        this.a5Name = a5Name;
        this.alImage = alImage;
    }

    @Override
    public int getCount() {
        return alName.size();
    }

    @Override
    public Object getItem(int position) {
        return alName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_horizontal_listview, parent, false);
            holder = new ViewHolder();
            holder.productHorizontalImageView = (ImageView) view.findViewById(R.id.productHorizontalImageView);
            holder.titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            holder.priceTextView = (TextView) view.findViewById(R.id.priceTextView);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.titleTextView.setText(alName.get(position));
        holder.priceTextView.setText(a2Name.get(position));
     //   holder.productHorizontalImageView.setImageResource(alImage.get(position));

        Picasso.with(context)
                .load(""+alImage.get(position))
                .placeholder(R.drawable.default_product)
                .into(holder.productHorizontalImageView);
        return view;
    }

    private class ViewHolder {
        public ImageView productHorizontalImageView;
        public TextView titleTextView;
        public TextView priceTextView;
    }
}
