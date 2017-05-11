package com.example.android.shopinggururegister.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.WishListAdapter;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.WishListData;
import com.example.android.shopinggururegister.R;

import java.util.ArrayList;

/**
 * Created by Android on 07-12-2016.
 */

public class WishListFragment extends Fragment implements View.OnClickListener {
    private ProductsListDb productsListDb;
    private ListView listView_products;
    private Button goShoppingButton;
    private TextView wishlistNoItemTitleTextView, wishlistTitleTextView;

//    ArrayList<CategoryListData> arrayList;
//    CategoryListAdapter listAdapter;

    ArrayList<WishListData> arrayList;
    WishListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_products, null);
        listView_products = (ListView) view.findViewById(R.id.listView_products);
        goShoppingButton = (Button) view.findViewById(R.id.goShoppingButton);
        wishlistNoItemTitleTextView = (TextView) view.findViewById(R.id.wishlistNoItemTitleTextView);
        wishlistTitleTextView = (TextView) view.findViewById(R.id.wishlistTitleTextView);
        TextView countTextView = (TextView) getActivity().findViewById(R.id.countTextView);
//        startActivity(new Intent(MainActivityNew.this, BuyNowActivity.class));
        ProductsListDb db = new ProductsListDb(getActivity());
        int cart_counts = db.getCartCount();
        db.close();
        countTextView.setText(Integer.toString(cart_counts));

        wishlistTitleTextView.setVisibility(View.VISIBLE);
        productsListDb = new ProductsListDb(getActivity());
        productsListDb.getProductsFromDatabase();
        arrayList = productsListDb.getProductsFromDatabase();
        if (arrayList.size() == 0) {

            goShoppingButton.setVisibility(View.VISIBLE);
            wishlistNoItemTitleTextView.setVisibility(View.VISIBLE);
            listView_products.setVisibility(View.GONE);
            goShoppingButton.setOnClickListener(this);
            Toast.makeText(getActivity(), "No item in wishlist", Toast.LENGTH_SHORT).show();
        } else {
            goShoppingButton.setVisibility(View.GONE);
            wishlistNoItemTitleTextView.setVisibility(View.GONE);
            listView_products.setVisibility(View.VISIBLE);
            listAdapter = new WishListAdapter(getActivity(), arrayList);
            listView_products.setAdapter(listAdapter);
        }
        productsListDb.close();
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Class fragmentClass = null;

        switch (id) {
            case R.id.goShoppingButton:
                fragmentClass = ProductCategoryListFragment.class;
                Fragment fragment = null;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
                break;
        }
    }
}
