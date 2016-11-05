package com.nike.hacklikeagirl.ui;

import com.nike.hacklikeagirl.Constants;
import com.nike.hacklikeagirl.R;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String STATS_URL = "http://wecode2016.stumptownsarah.com/food.html";
    private static final String ACTIVITY_URL = "http://wecode2016.stumptownsarah.com/food.html";
    private static final String FOOD_URL = "http://wecode2016.stumptownsarah.com/food.html";
    private static final String MARKET_URL = "http://wecode2016.stumptownsarah.com/food.html";
    private static final String FRIEDS_URL = "http://wecode2016.stumptownsarah.com/leaderboard.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, LandingPageFragment.newInstance());
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_stats) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.activity_stats), Constants.BLUE,
                    STATS_URL);
        } else if (id == R.id.nav_activity) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.activity_log), Constants.GREEN, ACTIVITY_URL);
        } else if (id == R.id.nav_food) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.food), Constants.YELLOW, FOOD_URL);
        } else if (id == R.id.nav_market) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.market), Constants.ORANGE, MARKET_URL);
        } else if (id == R.id.nav_friends) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.friends), Constants.SALMON, FRIEDS_URL);
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
