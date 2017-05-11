package com.example.android.shopinggururegister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Activities.VariationsActivity;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.CategoryListData;
import com.example.android.shopinggururegister.Dos.WishListData;
import com.example.android.shopinggururegister.Fragments.WishListFragment;
import com.example.android.shopinggururegister.Preferences.SharedData;
import com.example.android.shopinggururegister.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;

/**
 * Created by Android on 20-12-2016.
 */
public class WishListAdapter extends BaseAdapter {

    Context context;
    ArrayList<WishListData> arrData = new ArrayList<WishListData>();
    private static LayoutInflater inflater = null;

    private boolean isLikeButtonClicked = true;

    private ProductsListDb productsListDb;
    SharedData sharedData;
    Boolean list_grid_sharedata;
    String id = "";
    String name = "";
    String desc = "";
    String original_price = "";
    String selling_price = "";
    ProductsListDb productListDb;
    byte[] imageByteArray;
    int flag = 0;

    public WishListAdapter(Context context, ArrayList<WishListData> arrData) {
        // TODO Auto-WishListAdapter constructor stub
        this.arrData = arrData;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedData = new SharedData(context);
        list_grid_sharedata = sharedData.getPrefs().getBoolean("KEY_ITEM_LIST_GRID", true);
        productListDb = new ProductsListDb(context);
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
        TextView product_id;
        ImageView product_image;
        TextView product_name;
        TextView product_desc;
        TextView product_actual_price;
        TextView product_selling_price;
        RelativeLayout productRelativeLayout;
        ImageView like_button;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (view == null) {

            holder = new ViewHolder();

            view = inflater.inflate(R.layout.item_wishlist, null);

            holder.product_image = (ImageView) view.findViewById(R.id.productImageView);
            holder.product_name = (TextView) view.findViewById(R.id.productTitleTextView);
            //  holder.product_desc=(TextView)view.findViewById(R.id.discountTextView);
            holder.product_actual_price = (TextView) view.findViewById(R.id.originalPriceTextView);
            holder.product_selling_price = (TextView) view.findViewById(R.id.sellingPriceTextView);
            holder.like_button = (ImageView) view.findViewById(R.id.like_button);
            holder.productRelativeLayout = (RelativeLayout) view.findViewById(R.id.productRelativeLayout);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        Picasso.with(context)
//                .load("" + arrData.get(position).getProduct_image())
//                .placeholder(R.drawable.abc)
//                .into(holder.product_image);


        // holder.product_image.setImageResource(Integer.parseInt(arrData.get(position).getProduct_image()));
        imageByteArray = new byte[arrData.size()];
        imageByteArray = arrData.get(position).getProduct_image();
        Log.e("size", String.valueOf(arrData.size()));
        Log.e("photo", String.valueOf(arrData.get(position).getProduct_image()));

        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.product_image.setImageBitmap(theImage);


        holder.product_name.setText(arrData.get(position).getProduct_name());
        // holder.product_desc.setText(arrData.get(position).getProduct_desc());
        holder.product_actual_price.setText(arrData.get(position).getProduct_actual_price());
        holder.product_selling_price.setText(arrData.get(position).getProduct_selling_price());

        holder.like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = arrData.get(position).getProduct_id();
                Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                holder.like_button.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                productListDb.deleteParticularProductsFromDatabase(id);

                // new WishListAdapter(context,arrData);
            }
        });
        holder.productRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cat_id = arrData.get(position).getProduct_id();
                //  Toast.makeText(context, "You have selected:"+cat_id, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("CAT_ID", cat_id);
                Intent cat = new Intent(context, VariationsActivity.class);

                cat.putExtras(bundle);
                cat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(cat);
            }
        });


        return view;


    }

}
