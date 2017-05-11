package com.example.android.shopinggururegister.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopinggururegister.Database.ProductsListDb;
import com.example.android.shopinggururegister.Fragments.HomeFragment;
import com.example.android.shopinggururegister.Fragments.MyCartFragment;
import com.example.android.shopinggururegister.Fragments.MyOrderFragment;
import com.example.android.shopinggururegister.Fragments.ProductCategoryListFragment;
import com.example.android.shopinggururegister.Fragments.ProfileFragment;
import com.example.android.shopinggururegister.Fragments.WishListFragment;
import com.example.android.shopinggururegister.Preferences.SessionManager;
import com.example.android.shopinggururegister.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivityNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout mainFrameLayout;
    DrawerLayout drawer;
    boolean doubleBackToExitPressedOnce = false;
    SessionManager sessionManager;

    private SearchView searchSearchView;

    private CoordinatorLayout mainCoordinatorLayout;
    private TextView countTextView;

    NavigationView navigationView;
    String keywordString;

    public static final String KEY_SEARCH = "search";
    private FloatingActionButton myCardFAB;

    Fragment fragment = null;
    Class fragmentClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.add(R.id.mainFrameLayout, new HomeFragment());
        tx.commit();
        sessionManager = new SessionManager(getApplicationContext());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivityNew.this);
        searchSearchView = (SearchView) findViewById(R.id.searchSearchView);
        searchSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keywordString = searchSearchView.getQuery().toString();
                Bundle sendData = new Bundle();
                sendData.putString(KEY_SEARCH, keywordString);
                Intent searchIntent = new Intent(MainActivityNew.this, SearchResultActivity.class);
                searchIntent.putExtras(sendData);
                startActivity(searchIntent);
//                Toast.makeText(MainActivityNew.this, keywordString, Toast.LENGTH_SHORT).show();
//                new SearchTask().execute();
//                searchListveiw.setAdapter(null);
//                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                in.hideSoftInputFromWindow(searchview.getApplicationWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);
        mainFrameLayout = (FrameLayout) findViewById(R.id.mainFrameLayout);
        countTextView = (TextView) findViewById(R.id.countTextView);
        countTextView.setVisibility(View.VISIBLE);
        ProductsListDb db = new ProductsListDb(MainActivityNew.this);
        int cart_counts = db.getCartCount();
        db.close();
        countTextView.setText(Integer.toString(cart_counts));

        myCardFAB = (FloatingActionButton) findViewById(R.id.myCardFAB);
        myCardFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivityNew.this, BuyNowActivity.class));
                fragmentClass = MyCartFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
            }
        });
        DateFormat dateFormat = new SimpleDateFormat("hh a");
        Date d = Calendar.getInstance().getTime();
        String currentData = dateFormat.format(d);
        convertTo24Hour(currentData);
    }

    public String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat dateAmPm = new SimpleDateFormat("hh mm a");
        String dateAmPmString = dateAmPm.toString();
        DateFormat f2 = new SimpleDateFormat("hh");
        String time = f2.format(d); // "23:00"
        int timeInInt = Integer.parseInt(time);
        if (timeInInt <= 12 && dateAmPmString.endsWith("am")) {
            Snackbar.make(mainCoordinatorLayout, "Good Morning", Snackbar.LENGTH_LONG).show();
        } else if (timeInInt >= 12 && timeInInt <= 4 && dateAmPmString.endsWith("pm")) {
            Snackbar.make(mainCoordinatorLayout, "Good Afternoon", Snackbar.LENGTH_LONG).show();
        } else if (timeInInt >= 4 && timeInInt <= 12 && dateAmPmString.endsWith("pm")) {
            Snackbar.make(mainCoordinatorLayout, "Good Evening", Snackbar.LENGTH_LONG).show();
        }
        return time;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductsListDb db = new ProductsListDb(MainActivityNew.this);
        int cart_counts = db.getCartCount();
        db.close();
        countTextView.setText(Integer.toString(cart_counts));
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProductsListDb db = new ProductsListDb(MainActivityNew.this);
        int cart_counts = db.getCartCount();
        db.close();
        countTextView.setText(Integer.toString(cart_counts));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ProductsListDb db = new ProductsListDb(MainActivityNew.this);
        int cart_counts = db.getCartCount();
        db.close();
        countTextView.setText(Integer.toString(cart_counts));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }

        this.doubleBackToExitPressedOnce = true;

//        Toast toast = Toast.makeText(getApplicationContext(), R.string.toastBack, Toast.LENGTH_SHORT);
////        View view = toast.getView();
////
////        view.setBackgroundResource(R.color.white);
//        toast.show();
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityNew.this);
        builder.setTitle("Exit Shopingguru.in");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
                System.exit(0);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_new, menu);
        if (sessionManager.isLoggedIn()) {
            menu = navigationView.getMenu();
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_register).setVisible(false);
            menu.findItem(R.id.nav_my_orders).setVisible(true);

        }
        return super.onCreateOptionsMenu(menu);
    }

    boolean searchClicked = true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            if (searchClicked == true) {
                searchSearchView.setVisibility(View.VISIBLE);
                searchClicked = false;
            } else {
                searchSearchView.setVisibility(View.GONE);
                searchClicked = true;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
        }
        if (id == R.id.nav_profile) {
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
            args.putInt("data", 2);
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();

        } else if (id == R.id.nav_products) {
//            startActivity(i);
            fragmentClass = ProductCategoryListFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
        } else if (id == R.id.nav_register) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        } else if (id == R.id.nav_wishlist) {
            fragmentClass = WishListFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
        } else if (id == R.id.nav_my_orders) {
            fragmentClass = MyOrderFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();

        } else if (id == R.id.nav_logout) {
//            drawer.closeDrawer(GravityCompat.START);
//            sessionManager.logoutUser();
            logoutDialog();
        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityNew.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout? Logging out will remove items from your wishlist");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Hope to see you soon", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                sessionManager.logoutUser();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
