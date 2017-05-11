package com.example.android.shopinggururegister.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.shopinggururegister.R;

/**
 * Created by Android on 25-08-2016.
 */
public class CustomPagerAdapter extends PagerAdapter {
    private Context context;
    private String image_path;
    private ImageView slideImageView;
    private int[] flag;

    int[] mResources = {
            R.drawable.bigsalegirl,
            R.drawable.newseasonfootwear,
            R.drawable.menscasual2016,
            R.drawable.electronicbanner
    };
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context) {
        context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        slideImageView = (ImageView) itemView.findViewById(R.id.slideImageView);
        slideImageView.setImageResource(mResources[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((FrameLayout) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Big sale ";
            case 1:
                return "Footwear";
            case 2:
                return "Men casuals";
            case 3:
                return "Electronics";
        }
        return null;
    }
//
//    @Override
//    public float getPageWidth(int position) {
//        return 0.9f;
//    }

}