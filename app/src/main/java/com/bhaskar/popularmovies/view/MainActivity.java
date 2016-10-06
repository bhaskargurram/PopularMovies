package com.bhaskar.popularmovies.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.model.Communicator;

/*
*This is the launcher activity which extends AppCompatActivity to give backward support of Material Design.
*It implements the interface Communicator to communicate between 2 fragments. These fragments are placed side by side when viewed on tablet
*/
public class MainActivity extends AppCompatActivity implements Communicator {
    boolean twopane = false;
    private final String MAIN_FRAGMENT_TAG = "main_tag";
    private final String DETAIL_FRAGMENT_TAG = "detail_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            if (getIntent().getBooleanExtra("favourite", false)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Favourites(), DETAIL_FRAGMENT_TAG)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MainFragment(), MAIN_FRAGMENT_TAG)
                        .commit();
            }
        }
        //checking if the device is a tablet. If the device is a tablet it will load activity_main.xml of sw600dp. So will contain 2 fragments on the main screen.
        if (findViewById(R.id.container_for_detail) != null) {
            twopane = true;
            Log.d("", "contains container for detail");
            SharedPreferences preferences = getSharedPreferences("twopane_pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("twopane", twopane);
            editor.commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_for_detail, new DetailFragment(), DETAIL_FRAGMENT_TAG)
                    .commit();


        }
        //setting the title of the action bar
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    //creating menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //triggerred when a menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.toggle) {
            //The user wants to see movies by popularity.
            getSupportActionBar().setTitle(getString(R.string.app_name));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MainFragment(), MAIN_FRAGMENT_TAG)
                    .commit();
            SharedPreferences preferences = getSharedPreferences("toggle", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("toggle", "popularity.desc");


            if (isConnectingToInternet()) {

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.container), "Movies sorted by popularity", Snackbar.LENGTH_LONG);
                snackbar.show();


            } else {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.container), "No Internet Connection", Snackbar.LENGTH_LONG);
                snackbar.show();

            }

            editor.commit();


        } else if (id == R.id.toggle_ratings) {
            //The user wants to see movies by ratings.

            getSupportActionBar().setTitle(getString(R.string.app_name));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MainFragment(), MAIN_FRAGMENT_TAG)
                    .commit();
            SharedPreferences preferences = getSharedPreferences("toggle", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("toggle", "vote_average.desc");
            if (isConnectingToInternet()) {

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.container), "Movies sorted by rating", Snackbar.LENGTH_LONG);
                snackbar.show();


            } else {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.container), "No Internet Connection", Snackbar.LENGTH_LONG);
                snackbar.show();

            }


            editor.commit();
        } else if (id == R.id.fav_menu) {
            //The user wants to see favorites bookmarked by him.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Favourites(), DETAIL_FRAGMENT_TAG).addToBackStack("favourites")
                    .commit();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * this function isConnectingToInternet() checks if internet connection is available
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    /**
     * overriding the function of communicator interface
     */
    @Override
    public void respond(Bundle bundle) {
        DetailFragment fragment2 = new DetailFragment();
        fragment2.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_for_detail, fragment2, DETAIL_FRAGMENT_TAG)
                .commit();
    }


}
