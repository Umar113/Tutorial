package com.example.android.shopinggururegister.Activities;

/**
 * Created by Android on 28-11-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.shopinggururegister.Preferences.WelcomePrefManager;
import com.example.android.shopinggururegister.R;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Android on 11-11-2016.
 */
public class WelcomeActivity extends AppCompatActivity {
    private ViewPager welcomeViewPager;
    private LinearLayout dotsLinearLayout;
    private TextView dots[];
    private CircleIndicator welcomeCircleIndicator;
    private int layouts[] = {R.layout.welcome_screen_layout_1, R.layout.welcome_screen_layout_2, R.layout.welcome_screen_layout_3, R.layout.welcome_screen_layout_4};
    private Button nextButton, skipButton;
    private WelcomePrefManager welcomePrefManager;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        welcomePrefManager = new WelcomePrefManager(this);
        if (!welcomePrefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        setContentView(R.layout.welcome_activity_layout);
        initViews();
//        addPageIndicatorDots(0);
        registerEvents();

    }

//    private void addPageIndicatorDots(int currentPage) {
//        dots = new TextView[layouts.length];
//        int[] activeColor = getResources().getIntArray(R.array.array_dot_active);
//        int[] inactiveColor = getResources().getIntArray(R.array.array_dot_inactive);
//        dotsLinearLayout.removeAllViews();
//        for (int i = 0; i < dots.length; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(inactiveColor[currentPage]);
//            dotsLinearLayout.addView(dots[i]);
//
//        }
//        if (dots.length > 0) {
//            dots[currentPage].setTextColor(activeColor[currentPage]);
//        }
//    }

    private int getItem(int i) {
        return welcomeViewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
//            addPageIndicatorDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                nextButton.setText(getString(R.string.start));
                skipButton.setVisibility(View.GONE);
            } else {
                // still pages are left
                nextButton.setText(getString(R.string.next));
                skipButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initViews() {
        welcomeViewPager = (ViewPager) findViewById(R.id.welcomeViewPager);
        dotsLinearLayout = (LinearLayout) findViewById(R.id.dotsLinearLayout);
        nextButton = (Button) findViewById(R.id.nextButton);
        skipButton = (Button) findViewById(R.id.skipButton);
        welcomeCircleIndicator = (CircleIndicator) findViewById(R.id.welcomeCircleIndicator);

    }

    private void registerEvents() {
        myViewPagerAdapter = new MyViewPagerAdapter();
        welcomeViewPager.setAdapter(myViewPagerAdapter);
        welcomeCircleIndicator.setViewPager(welcomeViewPager);
        welcomeViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    welcomeViewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchHomeScreen();
            }
        });
    }

    private void launchHomeScreen() {
        welcomePrefManager.setFirstTimeLaunch(false);
        finish();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
