package com.example.android.shopinggururegister.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.android.shopinggururegister.R;

/**
 * Created by Android on 03-01-2017.
 */
public class PaymentMethodFragment extends Fragment {
    private RadioButton codRadioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_method_payment, container, false);
        codRadioButton = (RadioButton) view.findViewById(R.id.codRadioButton);
        codRadioButton.setChecked(true);
        return view;
    }
}
