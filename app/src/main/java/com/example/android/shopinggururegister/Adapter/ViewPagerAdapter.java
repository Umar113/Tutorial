package com.example.android.shopinggururegister.Adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.shopinggururegister.Dos.ViewPagerData;
import com.example.android.shopinggururegister.R;
import com.example.android.shopinggururegister.Utilities.ListUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<Integer> imageIdList;
    ArrayList<ViewPagerData> arrData = new ArrayList<ViewPagerData>();

    private int size;
    private boolean isInfiniteLoop;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, ArrayList<ViewPagerData> arrData) {
        this.context = context;
        this.arrData = arrData;
        this.size = ListUtils.getSize(imageIdList);
        isInfiniteLoop = false;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // Infinite loop
        return arrData.size();
    }


    private int getPosition(int position) {
        return position;
        //isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.pager_item, null);


            holder.slideImageView = (ImageView) view.findViewById(R.id.slideImageView);
            // holder.txtTitle1=(TextView)view.findViewById(R.id.txtTitle1);

            // holder.txtDescription=(TextView)view.findViewById(R.id.txtTitle2);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //    holder.image.setImageResource(Integer.parseInt(arrData.get(position).getImage()));

        Picasso.with(context)
                .load("" + arrData.get(position).getImage())
                // .placeholder(R.drawable.useri_icon)

                // .resize(100,100)
                //.onlyScaleDown()
                .into(holder.slideImageView);

        //  holder.txtTitle1.setText(arrData.get(position).getTitle());

        //  holder.txtDescription.setText(arrData.get(position).getStore_name());

        return view;
    }

    private static class ViewHolder {

        ImageView slideImageView;

    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

}
