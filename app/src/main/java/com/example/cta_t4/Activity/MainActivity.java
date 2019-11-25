package com.example.cta_t4.Activity;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cta_t4.Fragment.CaloriesFragment;
import com.example.cta_t4.Fragment.FatSecretFragment;
import com.example.cta_t4.Fragment.HomeFragment;
import com.example.cta_t4.Fragment.MapFragment;
import com.example.cta_t4.Fragment.PieFragment;
import com.example.cta_t4.Fragment.ReportFragment;
import com.example.cta_t4.Fragment.SearchFragment;
import com.example.cta_t4.Fragment.StepsFragment;
import com.example.cta_t4.R;
import com.example.cta_t4.entity.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User login_user;
    private HashMap<Integer,Double> cal_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cal_map = new HashMap<Integer, Double>();
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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            User user = bundle.getParcelable("login_user");
            setLogin_user(user);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        if(login_user != null) {
            int id = item.getItemId();
            Fragment fragment = null;
            if (id == R.id.nav_camera) {
                // Handle the camera action
                fragment = new HomeFragment();
            } else if (id == R.id.nav_gallery) {
                fragment = new SearchFragment();
            } else if (id == R.id.nav_slideshow) {
                fragment = new FatSecretFragment();
            } else if (id == R.id.nav_manage) {
                fragment = new StepsFragment();
            } else if (id == R.id.nav_share) {
                fragment = new CaloriesFragment();
            } else if (id == R.id.nav_send) {
                fragment = new PieFragment();
            } else if (id == R.id.nav_send2) {
                fragment = new ReportFragment();
            } else if (id == R.id.nav_send3) {
                fragment = new MapFragment();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            return true;
        }
        else {
            return false;
        }
    }

    public User getLogin_user() {
        return login_user;
    }

    public void setLogin_user(User login_user) {
        this.login_user = login_user;
    }

    public HashMap<Integer, Double> getCal_map() {
        return cal_map;
    }

    public void setCal_map(HashMap<Integer, Double> cal_map) {
        this.cal_map = cal_map;
    }
}
