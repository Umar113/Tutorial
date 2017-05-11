package com.example.android.shopinggururegister.Fragments;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.android.shopinggururegister.Database.ProductsListDb;
//import com.example.android.shopinggururegister.R;
//
///**
// * Created by Android on 26-07-2016.
// */
//public class MyCartFragment extends Fragment implements View.OnClickListener {
//
//    private Button shopNowButton;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.mycart_layout, null);
//        shopNowButton = (Button) view.findViewById(R.id.shopNowButton);
//        shopNowButton.setOnClickListener(this);
//        TextView countTextView = (TextView) getActivity().findViewById(R.id.countTextView);
//
//
//        FloatingActionButton myCardFAB = (FloatingActionButton) getActivity().findViewById(R.id.myCardFAB);
////                startActivity(new Intent(MainActivityNew.this, BuyNowActivity.class));
//        ProductsListDb db = new ProductsListDb(getActivity());
//        int cart_counts = db.getCartCount();
//        db.close();
//        countTextView.setText(Integer.toString(cart_counts));
//        return view;
//    }
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.shopNowButton:
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.mainFrameLayout, new ProductCategoryListFragment());
//                fragmentTransaction.commit();
//                break;
//        }
//    }
//}

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Adapter.CartAdapter;
import com.example.android.shopinggururegister.Adapter.CategoryListAdapter;
import com.example.android.shopinggururegister.Adapter.WishListAdapter;
import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Dos.CategoryListData;
import com.example.android.shopinggururegister.Dos.WishListData;
import com.example.android.shopinggururegister.R;

import java.util.ArrayList;

/**
 * Created by Android on 07-12-2016.
 */

public class MyCartFragment extends Fragment implements View.OnClickListener {
    private ProductsListDb productsListDb;
    private ListView cart_listView_products;
    private Button goShoppingCartButton;
    private TextView cartNoItemTitleTextView, cartTitleTextView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

//    ArrayList<CategoryListData> arrayList;
//    CategoryListAdapter listAdapter;

    ArrayList<WishListData> arrayList;
    CartAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment_layout, null);
        cart_listView_products = (ListView) view.findViewById(R.id.cart_listView_products);
        goShoppingCartButton = (Button) view.findViewById(R.id.goShoppingCartButton);
        cartNoItemTitleTextView = (TextView) view.findViewById(R.id.cartNoItemTitleTextView);
        cartTitleTextView = (TextView) view.findViewById(R.id.cartTitleTextView);

        TextView countTextView = (TextView) getActivity().findViewById(R.id.countTextView);


//                startActivity(new Intent(MainActivityNew.this, BuyNowActivity.class));
        ProductsListDb db = new ProductsListDb(getActivity());
        int cart_counts = db.getCartCount();
        db.close();
        countTextView.setText(Integer.toString(cart_counts));


        productsListDb = new ProductsListDb(getActivity());
        arrayList = productsListDb.getProductsFromCartDatabase();
        if (arrayList.size() == 0) {

            goShoppingCartButton.setVisibility(View.VISIBLE);
            cartNoItemTitleTextView.setVisibility(View.VISIBLE);
            cart_listView_products.setVisibility(View.GONE);
            goShoppingCartButton.setOnClickListener(this);
            Toast.makeText(getActivity(), "No item in wishlist", Toast.LENGTH_SHORT).show();
        } else {
            goShoppingCartButton.setVisibility(View.GONE);
            cartNoItemTitleTextView.setVisibility(View.GONE);
            cart_listView_products.setVisibility(View.VISIBLE);
            listAdapter = new CartAdapter(getActivity(), arrayList);
            cart_listView_products.setAdapter(listAdapter);
        }
        db.close();
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Class fragmentClass = null;

        switch (id) {
            case R.id.goShoppingCartButton:
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
