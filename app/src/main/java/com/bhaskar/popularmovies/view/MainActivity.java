package com.bhaskar.popularmovies.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.model.Communicator;


public class MainActivity extends AppCompatActivity implements Communicator {
    boolean twopane = false;
    private String MAIN_FRAGMENT_TAG = "main_tag";

    private String DETAIL_FRAGMENT_TAG = "detail_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment(), MAIN_FRAGMENT_TAG)
                    .commit();
        }

        if (findViewById(R.id.container_for_detail) != null) {
            twopane = true;
            Log.d("", "contains container for detail");
            SharedPreferences preferences = getSharedPreferences("twopane_pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("twopane", twopane);
            editor.commit();
            // if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_for_detail, new DetailFragment(), DETAIL_FRAGMENT_TAG)
                    .commit();
            //}

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.toggle) {
            SharedPreferences preferences = getSharedPreferences("toggle", MODE_PRIVATE);
            String toggle = preferences.getString("toggle", "popularity.desc");
            SharedPreferences.Editor editor = preferences.edit();
            if (toggle.equals("popularity.desc")) {

                editor.putString("toggle", "vote_average.desc");

                item.setTitle("Popularity");
                Toast.makeText(getApplicationContext(), "Movies sorted by rating", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("toggle", "popularity.desc");

                item.setTitle("Rating");
                Toast.makeText(getApplicationContext(), "Movies sorted by popularity", Toast.LENGTH_SHORT).show();
            }
            editor.commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MainFragment(), MAIN_FRAGMENT_TAG)
                    .commit();
//            pageno = 1;
            //          arrayList = new ArrayList<Item>();
            //        checkAndLoad();

        } else if (id == R.id.fav_menu) {
            // Toast.makeText(getApplicationContext(),"R.id.fav_menu",Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Favourites(), DETAIL_FRAGMENT_TAG)
                    .commit();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void respond(Bundle bundle) {
        DetailFragment fragment2 = new DetailFragment();
        fragment2.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_for_detail, fragment2, DETAIL_FRAGMENT_TAG)
                .commit();
        Log.d("", "inside respond of main activity");
    }


}
