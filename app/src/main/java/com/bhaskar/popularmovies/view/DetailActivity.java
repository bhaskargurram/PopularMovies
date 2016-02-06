package com.bhaskar.popularmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bhaskar.popularmovies.R;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        if (savedInstanceState == null) {
            DetailFragment detail = new DetailFragment();
            detail.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, detail)
                    .commit();
        }


    }

}
