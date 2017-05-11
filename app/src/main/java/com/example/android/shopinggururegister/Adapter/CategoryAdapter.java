package com.example.android.shopinggururegister.Adapter;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.shopinggururegister.Activities.ProductsListActivity;
import com.example.android.shopinggururegister.Dos.ProductsData;
import com.example.android.shopinggururegister.R;

import java.util.ArrayList;

/**
 * Created by anjal on 25/11/2016.
 */
public class CategoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<ProductsData> arrData = new ArrayList<ProductsData>();
    private static LayoutInflater inflater = null;

    public CategoryAdapter(Context context, ArrayList<ProductsData> arrData) {
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
        TextView textView1;
//        CardView cardview;


    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;

        if (view == null) {

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_product_list, null);


            holder.textView1 = (TextView) view.findViewById(R.id.cat_name);
//            holder.cardview=(CardView)view.findViewById(R.id.cardview_cat);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

		/*Picasso.with(context)
                .load(""+arrData.get(position).getImage())
				.placeholder(R.drawable.closeiconnew)
				.error(R.drawable.closeiconnew)
				.into(holder.imageView1);*/
        holder.textView1.setText(arrData.get(position).getCat_name());
        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = arrData.get(position).getCat_name();
                String cat_id = arrData.get(position).getCat_id();
                //  Toast.makeText(context, "You have selected:"+cat_id, Toast.LENGTH_SHORT).show();
                Intent cat = new Intent(context, ProductsListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ID", cat_id);
                cat.putExtras(bundle);
                cat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(cat);
            }
        });


        return view;

    }

}

