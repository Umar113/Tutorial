package com.example.android.shopinggururegister.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.android.shopinggururegister.Fragments.ConfirmProductFragment;
import com.example.android.shopinggururegister.Fragments.PaymentMethodFragment;
import com.example.android.shopinggururegister.Fragments.ProfileFragment;
import com.example.android.shopinggururegister.R;
import com.kofigyan.stateprogressbar.StateProgressBar;

/**
 * Created by Android on 02-01-2017.
 */
public class BuyNowActivity extends AppCompatActivity implements View.OnClickListener {
    private String indicatorString[] = {"User Details", "Payment", "Confirm"};
    private StateProgressBar paymentStateProgressBar;
    private FrameLayout buyNowFrameLayout;

    private Button nextBuyButton;

    private int buttonPressed = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_now_activity_layout);

        paymentStateProgressBar = (StateProgressBar) findViewById(R.id.paymentStateProgressBar);
        paymentStateProgressBar.setStateDescriptionData(indicatorString);
        buyNowFrameLayout = (FrameLayout) findViewById(R.id.buyNowFrameLayout);
        nextBuyButton = (Button) findViewById(R.id.nextBuyButton);
        nextBuyButton.setOnClickListener(this);
        Class fragmentClass = null;
        Fragment fragment = null;
        fragmentClass = ProfileFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putInt("data", 1);
        fragment.setArguments(args);
        fragmentManager.beginTransaction().add(R.id.buyNowFrameLayout, fragment).commit();
    }

    @Override
    public void onClick(View view) {
        if (buttonPressed == 0) {
            paymentStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            buttonPressed += 1;
            Class fragmentClass0 = null;
            Fragment fragment0 = null;
            fragmentClass0 = PaymentMethodFragment.class;
            try {
                fragment0 = (Fragment) fragmentClass0.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentManager paymentFragmentManager = getSupportFragmentManager();
            paymentFragmentManager.beginTransaction().replace(R.id.buyNowFrameLayout, fragment0).commit();

        } else if (buttonPressed == 1) {
            buttonPressed += 1;
            paymentStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
            Class fragmentClass1 = null;
            Fragment fragment1 = null;
            fragmentClass1 = ConfirmProductFragment.class;
            try {
                fragment1 = (Fragment) fragmentClass1.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentManager addFragmentManager = getSupportFragmentManager();
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("key");
            bundle.putBundle("key", bundle);

//            intent.putExtra("key", "value");
            fragment1.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().commit();
            addFragmentManager.beginTransaction().replace(R.id.buyNowFrameLayout, fragment1).commit();
            nextBuyButton.setVisibility(View.GONE);

        } else if (buttonPressed == 2) {
//            paymentStateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
            buttonPressed = 0;
//            Class fragmentClass1 = null;
//            Fragment fragment1 = null;
//            fragmentClass1 = ProfileFragment.class;
//            try {
//                fragment1 = (Fragment) fragmentClass1.newInstance();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            FragmentManager addFragmentManager = getSupportFragmentManager();
//            addFragmentManager.beginTransaction().replace(R.id.buyNowFrameLayout, fragment1).commit();
            nextBuyButton.setVisibility(View.GONE);
        }
    }
}