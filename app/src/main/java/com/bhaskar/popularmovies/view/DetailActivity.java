package com.bhaskar.popularmovies.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.controller.FavouritesContract;

import java.util.Locale;

/**
 * This activity displays the details of the movie clicked by the user.
 * It extends AppCompatActivity to give backward compatibility support of Material Design.
 */
public class DetailActivity extends AppCompatActivity {
    boolean favourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //create a bundle of all details passed by the MainActivity to be passed to the DetailFragment
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        bundle.putString("poster_path", intent.getStringExtra("poster_path"));
        bundle.putString("overview", intent.getStringExtra("overview"));
        bundle.putString("release_date", intent.getStringExtra("release_date"));
        bundle.putString("original_title", intent.getStringExtra("original_title"));

        bundle.putString("original_language", new Locale(intent.getStringExtra("original_language")).getDisplayLanguage());
        bundle.putString("title", intent.getStringExtra("title"));
        bundle.putString("backdrop_path", intent.getStringExtra("backdrop_path"));
        bundle.putInt("adult", (intent.getBooleanExtra("adult", false) == true) ? 1 : 0);
        bundle.putDouble("popularity", intent.getDoubleExtra("popularity", 0));
        bundle.putDouble("vote_average", intent.getDoubleExtra("vote_average", 0));
        bundle.putLong("vote_count", intent.getLongExtra("vote_count", 0));
        bundle.putLong("id", intent.getLongExtra("id", 0));
        bundle.putBoolean("favourites", intent.getBooleanExtra("favourites", false));
        //see if this movie is a favorite movie
        favourite = intent.getBooleanExtra("favourites", false);
        if (savedInstanceState == null) {
            DetailFragment detail = new DetailFragment();
            detail.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, detail)
                    .commit();
        }


    }

    //onOptionsItemSelected will be called when user clicks the menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("bhaskar", "hello");
        switch (item.getItemId()) {
            case R.id.home:
                //user has clicked the back button in the app bar. So the app should go back to the MainActivity
                Intent intent2 = new Intent(DetailActivity.this, MainActivity.class);

                if (favourite) {
                    intent2.putExtra("favourite", true);
                }
                startActivity(intent2);
                return true;
            case R.id.like_btn:
                //this is implemented in the fragment so return false here
                return false;


            case R.id.share_trailer:
                //this is implemented in the fragment so return false here
                return false;


            case R.id.fav:
                //this is implemented in the fragment so return false here
                return false;


            case R.id.play:
                //this is implemented in the fragment so return false here
                return false;
            default:
                break;
        }
        return false;
    }
}
