package com.example.android.shopinggururegister.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopinggururegister.Dos.CategoryListData;
import com.example.android.shopinggururegister.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by anjal on 26/11/2016.
 */
public class CategoryGridAdapter extends BaseAdapter {

    String[] result;
    Context context;
    int[] imageId;
    ArrayList<CategoryListData> arrData = new ArrayList<CategoryListData>();
    private static LayoutInflater inflater = null;

    public CategoryGridAdapter(Context context, ArrayList<CategoryListData> arrData) {
        // TODO Auto-generated constructor stub
        this.arrData = arrData;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class ViewHolder {
        ImageView product_image;
        TextView product_name;
        TextView product_desc;
        TextView product_actual_price;
        TextView product_selling_price;
        ImageView like_button;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;

        if (view == null) {

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_categories_grid, null);

            holder.product_image = (ImageView) view.findViewById(R.id.productImageView);
            holder.product_name = (TextView) view.findViewById(R.id.productTitleTextView);
            //  holder.product_desc=(TextView)view.findViewById(R.id.discountTextView);
            holder.product_actual_price = (TextView) view.findViewById(R.id.originalPriceTextView);
            holder.product_selling_price = (TextView) view.findViewById(R.id.sellingPriceTextView);
            holder.like_button = (ImageView) view.findViewById(R.id.like_button);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(context)
                .load("" + arrData.get(position).getProduct_image())
                .placeholder(R.drawable.default_product)
                .into(holder.product_image);


        // holder.product_image.setImageResource(Integer.parseInt(arrData.get(position).getProduct_image()));
        holder.product_name.setText(arrData.get(position).getProduct_name());
        // holder.product_desc.setText(arrData.get(position).getProduct_desc());
        holder.product_actual_price.setText(arrData.get(position).getProduct_actual_price());
        holder.product_selling_price.setText(arrData.get(position).getProduct_selling_price());

//        holder.like_button.setOnLikeListener(new OnLikeListener() {
//            @Override
//            public void liked(LikeButton likeButton) {
//
//
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//
//            }
//        });


        return view;

    }

}

