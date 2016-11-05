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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main_drawer, menu);
//
//        // Get the root inflator.
//        LayoutInflater baseInflater = (LayoutInflater)getBaseContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        // Inflate your custom view.
////        RelativeLayout registerView = (RelativeLayout)baseInflater.inflate(R.layout.custom_menu_item, null);
////        TextView registerText = (TextView) registerView.findViewById(R.id.custom_menu_title);
////        registerText.setText("Register");
////        menu.findItem(R.id.nav_register).setActionView(registerView);
//
//        // If myCustomView has additional children, you might have to inflate them separately here.
//        // In my case, I used buttons in my custom view, and registered onClick listeners at this point.
//        return true;
//    }

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
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.activity_stats), Constants.BLUE);
        } else if (id == R.id.nav_activity) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.activity_log), Constants.GREEN);
        } else if (id == R.id.nav_food) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.food), Constants.YELLOW);
        } else if (id == R.id.nav_market) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.market), Constants.ORANGE);
        } else if (id == R.id.nav_friends) {
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.friends), Constants.SALMON);
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
