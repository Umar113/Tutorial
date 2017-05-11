package com.example.android.shopinggururegister.Adapter;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Activities.ProductsListActivity;
import com.example.android.shopinggururegister.Activities.VariationsActivity;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.CategoryListData;
import com.example.android.shopinggururegister.Preferences.SharedData;
import com.example.android.shopinggururegister.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;

/**
 * Created by anjal on 26/11/2016.
 */
public class CategoryListAdapter extends BaseAdapter {

    Context context;
    ArrayList<CategoryListData> arrData = new ArrayList<CategoryListData>();
    private static LayoutInflater inflater = null;

    private boolean isLikeButtonClicked = true;

    private ProductsListDb productsListDb;
    SharedData sharedData;
    Boolean list_grid_sharedata;
    byte[] photo;
    String id = "";
    String name = "";
    String desc = "";
    String original_price = "";
    String selling_price = "";
    String image = "";
    byte[] accImage;

    byte[] logoImage;

    public CategoryListAdapter(Context context, ArrayList<CategoryListData> arrData) {
        // TODO Auto-generated constructor stub
        this.arrData = arrData;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedData = new SharedData(context);
        list_grid_sharedata = sharedData.getPrefs().getBoolean("KEY_ITEM_LIST_GRID", true);
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
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;
        productsListDb = new ProductsListDb(context);

//        if (view == null) {
//
//            holder = new ViewHolder();
//            view = inflater.inflate(R.layout.item_categories_list, null, true);
//
//            holder.product_image = (ImageView) view.findViewById(R.id.productImageView);
//            Picasso.with(context)
//                    .load("" + arrData.get(position).getProduct_image())
//                    .placeholder(R.drawable.default_product)
//                    .into(holder.product_image);
//            final CategoryListData categoryListData = arrData.get(position);
//
//            // holder.product_image.setImageResource(Integer.parseInt(arrData.get(position).getProduct_image()));
//            holder.product_name = (TextView) view.findViewById(R.id.productTitleTextView);
//            //  holder.product_desc=(TextView)view.findViewById(R.id.discountTextView);
//            holder.product_actual_price = (TextView) view.findViewById(R.id.originalPriceTextView);
//            holder.product_selling_price = (TextView) view.findViewById(R.id.sellingPriceTextView);
//            holder.like_button = (ImageView) view.findViewById(R.id.like_button);
//
//            view.setTag(holder);
//            if (categoryListData.getLikeStatus()) {
//                holder.like_button.setImageResource(R.drawable.ic_favorite_black_24dp);
//                categoryListData.setLikeStatus(false);
//            } else {
//                holder.like_button.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                categoryListData.setLikeStatus(true);
//
//            }
//
//
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//        holder.product_name.setText(arrData.get(position).getProduct_name());
//        holder.product_actual_price.setPaintFlags(holder.product_actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.product_selling_price.setText(arrData.get(position).getProduct_selling_price());
//        holder.product_actual_price.setText(arrData.get(position).getProduct_actual_price());
//
//
//        holder.like_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isLikeButtonClicked == false) {
//                    productsListDb.insertProductIntoDb("", holder.product_name.getText().toString(), holder.product_selling_price.getText().toString(), holder.product_actual_price.getText().toString());
//                    holder.like_button.setImageResource(R.drawable.ic_favorite_black_24dp);
//                    isLikeButtonClicked = true;
//                } else {
//                    holder.like_button.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                    productsListDb.deleteProductsFromDatabase();
//                    Toast.makeText(context, "Removed From Wishlist", Toast.LENGTH_SHORT).show();
//                    isLikeButtonClicked = false;
//                }
//            }
//        });
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_categories_list, null);
        } else {
            holder = (ViewHolder) view.getTag();

        }
        holder.product_image = (ImageView) view.findViewById(R.id.productImageView);
        Picasso.with(context)
                .load("" + arrData.get(position).getProduct_image())
                .placeholder(R.drawable.default_product)
                .into(holder.product_image);

        holder.product_name = (TextView) view.findViewById(R.id.productTitleTextView);
        holder.product_selling_price = (TextView) view.findViewById(R.id.sellingPriceTextView);
        holder.product_actual_price = (TextView) view.findViewById(R.id.originalPriceTextView);
        holder.product_id = (TextView) view.findViewById(R.id.producIdTextView);

        final ImageView like_button = (ImageView) view.findViewById(R.id.like_button);
        holder.productRelativeLayout = (RelativeLayout) view.findViewById(R.id.productRelativeLayout);
        //productAPriceTextView.setText(productDo.getProductAPrice());
        //productDiscountTextView.setText(productDo.getProductDiscount());

        final CategoryListData categoryListData = arrData.get(position);

        holder.product_name.setText(categoryListData.getProduct_name());
        holder.product_selling_price.setText(categoryListData.getProduct_selling_price());
        holder.product_actual_price.setText(categoryListData.getProduct_actual_price());
        holder.product_id.setText(categoryListData.getProduct_id());

        view.setTag(holder);
        if (categoryListData.getLiked() == 1) {
            like_button.setImageResource(R.drawable.ic_favorite_black_24dp);

        } else {
            like_button.setImageResource(R.drawable.ic_favorite_border_black_24dp);


        }
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryListData.getLiked() == 1) {
                    categoryListData.setLiked(0);
                    like_button.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    productsListDb.deleteParticularProductsFromDatabase(holder.product_id.getText().toString());
//                    productsListDb.deleteAllProductsFromDatabase();

                } else {
                    categoryListData.setLiked(1);
                    like_button.setImageResource(R.drawable.ic_favorite_black_24dp);

                    //  productsListDb.insertProductIntoDb(holder.product_id.getText().toString(), holder.product_name.getText().toString(), holder.product_selling_price.getText().toString(), holder.product_actual_price.getText().toString(), categoryListData.getLiked());
                    ProductsListDb productsListDb = new ProductsListDb(context);
                    image = arrData.get(position).getProduct_image();
                    id = arrData.get(position).getProduct_id();

                    Log.d(">>>>>>id", id);
                    name = arrData.get(position).getProduct_name();
                    desc = arrData.get(position).getProduct_desc();
                    original_price = arrData.get(position).getProduct_actual_price();
                    selling_price = arrData.get(position).getProduct_selling_price();

                    new ImageDownloader().execute(image);

                }
            }
        });
        holder.productRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cat_id = arrData.get(position).getProduct_id();
                //  Toast.makeText(context, "You have selected:"+cat_id, Toast.LENGTH_SHORT).show();

                Intent cat = new Intent(context, VariationsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CAT_ID", cat_id);

                cat.putExtras(bundle);
                cat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(cat);
            }
        });
        return view;
    }
//    private void addToWishList(CategoryListData categoryListData) {
//        arrData.add(categoryListData);
//    }


    private class ImageDownloader extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(String... param) {


            logoImage = getLogoImage(param[0]);

            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(context, "Wait", "Downloading Image");

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            Log.e("data:>>>>> ", id + "\n" + name + "\n" + selling_price + "\n" + original_price + "\n" + photo);
            productsListDb.insertProductIntoDb(id, name, selling_price, original_price, 0, photo);
            Toast.makeText(context, "added successfully....!", Toast.LENGTH_SHORT).show();

        }

    }

    private byte[] getLogoImage(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            System.out.println("11111");
            InputStream is = ucon.getInputStream();
            System.out.println("12121");

            BufferedInputStream bis = new BufferedInputStream(is);
            System.out.println("22222");

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            System.out.println("23333");

            while ((current = bis.read()) != -1) {
                baf.append((byte) current);

            }
            photo = baf.toByteArray();
            System.out.println("photo length" + photo);


        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return accImage;
    }
}


