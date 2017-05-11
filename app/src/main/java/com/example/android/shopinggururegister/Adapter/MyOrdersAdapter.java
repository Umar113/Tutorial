package com.example.android.shopinggururegister.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.shopinggururegister.Activities.OrderdetailsActivity;
import com.example.android.shopinggururegister.Activities.ProductsListActivity;
import com.example.android.shopinggururegister.Dos.ProductsData;
import com.example.android.shopinggururegister.Dos.RecentOrdersDataObject;
import com.example.android.shopinggururegister.R;

import java.util.ArrayList;

/**
 * Created by Android on 24-01-2017.
 */
public class MyOrdersAdapter extends BaseAdapter {
    Context context;
    ArrayList<RecentOrdersDataObject> arrData = new ArrayList<RecentOrdersDataObject>();
    private static LayoutInflater inflater = null;

    public MyOrdersAdapter(Context context, ArrayList<RecentOrdersDataObject> arrData) {
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
        TextView orderTotalTextView, orderDateTextView, orderStatusTextView, orderIdsTextView;
        CardView recentOrdersCardView;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;

        if (view == null) {

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.my_orders_content_list_view_fragment, null);


            holder.orderTotalTextView = (TextView) view.findViewById(R.id.orderTotalTextView);
            holder.orderDateTextView = (TextView) view.findViewById(R.id.orderDateTextView);
            holder.orderIdsTextView = (TextView) view.findViewById(R.id.orderIdTextView);

            holder.orderStatusTextView = (TextView) view.findViewById(R.id.orderStatusTextView);
            holder.recentOrdersCardView = (CardView) view.findViewById(R.id.recentOrdersCardView);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.orderTotalTextView.setText(arrData.get(position).getOrder_total());
        holder.orderDateTextView.setText(arrData.get(position).getOrder_date());
        holder.orderStatusTextView.setText(arrData.get(position).getOrder_status());
        holder.orderIdsTextView.setText(arrData.get(position).getOrder_id());


        Log.d(">>>>ostatus", arrData.get(position).getOrder_total());


        holder.recentOrdersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String data = arrData.get(position).getCat_name();
                String cat_id = arrData.get(position).getOrder_id();
//                Toast.makeText(context, cat_id, Toast.LENGTH_SHORT).show();
                Intent cat = new Intent(context, OrderdetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY_ID", cat_id);
                bundle.putString("total", arrData.get(position).getOrder_total());
                bundle.putString("order_status", arrData.get(position).getOrder_status());
                bundle.putString("order_date", arrData.get(position).getOrder_date());
                cat.putExtras(bundle);
                cat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(cat);
            }
        });

        return view;

    }

}