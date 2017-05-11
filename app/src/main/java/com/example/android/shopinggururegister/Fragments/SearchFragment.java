package com.example.android.shopinggururegister.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.android.shopinggururegister.R;

/**
 * Created by Android on 22-08-2016.
 */
public class SearchFragment extends FragmentActivity {

    String[] products = {"air conditioner", "batteries", "cakes", "fans", "invertor", "kitchenware", "ladies innerwear", "LED TV", "microwave", "mobile", "refrigerator", "router and modems", "washin machine", "wired keyboard", "wired Mouse", "wireless mouse and keyboard"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_fragment);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, products);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView messageAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.messageAutoCompleteTextView);
        messageAutoCompleteTextView.setThreshold(1);//will start working from first character
        messageAutoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }
}
