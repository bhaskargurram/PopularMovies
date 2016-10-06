package com.bhaskar.popularmovies.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.adapters.FavouriteAdapter;
import com.bhaskar.popularmovies.controller.FavouritesContract;
import com.bhaskar.popularmovies.model.Communicator;
import com.bhaskar.popularmovies.model.Item;

import java.util.ArrayList;

/**
 * Created by bhaskar on 4/2/16.
 */

/**This class is used to display Favourites bookmarked by the user.
 * This is a fragment which will be displayed on MainActivity
 * It takes data from the sqlite database stored internally and uses Loader Manager to load the data into the gridview
 * */
public class Favourites extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    GridView gridView;
    ArrayList<Item> arrayList;
    FavouriteAdapter adapter;
    public static final int FAVOURITE_ID = 0;
    Communicator communicator;
    TextView text;

    //Initializing the display.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflating R.layout.activity_frag_main
        View rootView = inflater.inflate(R.layout.activity_frag_main, container, false);
        //uses a gridview
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        //an arraylist to store details
        arrayList = new ArrayList<Item>();
        //adapter of the gridview
        adapter = new FavouriteAdapter(getActivity(), null, 0);
        gridView.setAdapter(adapter);
        text = (TextView) rootView.findViewById(R.id.no_fav);
        Log.d("bhaskar", "bhaskar " + adapter.getCount());
        getActivity().getSupportLoaderManager().initLoader(FAVOURITE_ID, null, this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favourites");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences preferences1 = getActivity().getSharedPreferences("twopane_pref", getActivity().MODE_PRIVATE);
                Boolean twopane = preferences1.getBoolean("twopane", false);
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                if (cursor.getColumnCount() > 0) {
                    getActivity().findViewById(R.id.no_fav).setVisibility(View.GONE);
                    if (!twopane) {
                        //Mobile device. Transit to Detail Activity
                        Log.d("", "Main fragment single pane onclick");
                        Intent intent = new Intent(getActivity(), DetailActivity.class);

                        intent.putExtra("poster_path", cursor.getString(FavouritesContract.COL_POSTER_PATH));
                        intent.putExtra("title", cursor.getString(FavouritesContract.COL_TITLE));
                        intent.putExtra("id", cursor.getLong(FavouritesContract.COL_MOVIE_ID));
                        intent.putExtra("overview", cursor.getString(FavouritesContract.COL_OVERVIEW));
                        intent.putExtra("release_date", cursor.getString(FavouritesContract.COL_RELEASE_DATE));
                        intent.putExtra("original_title", cursor.getString(FavouritesContract.COL_ORIGINAL_TITLE));
                        intent.putExtra("original_language", cursor.getString(FavouritesContract.COL_ORIGINAL_LANGUAGE));
                        intent.putExtra("backdrop_path", "http://image.tmdb.org/t/p/" + "w185" + cursor.getString(FavouritesContract.COL_BACKDROP_PATH));
                        intent.putExtra("adult", (cursor.getLong(FavouritesContract.COL_ADULT) == 0) ? false : true);
                        intent.putExtra("popularity", cursor.getLong(FavouritesContract.COL_POPULARITY));
                        intent.putExtra("vote_average", cursor.getDouble(FavouritesContract.COL_VOTE_AVERAGE));
                        intent.putExtra("vote_count", cursor.getLong(FavouritesContract.COL_VOTE_COUNT));
                        intent.putExtra("favourites", true);
                        startActivity(intent);
                    } else {
                        //Tablet Device. Use Communicator interface to send data to other fragment
                        Bundle bundle = new Bundle();
                        bundle.putString("poster_path", cursor.getString(FavouritesContract.COL_POSTER_PATH));
                        bundle.putString("title", cursor.getString(FavouritesContract.COL_TITLE));
                        bundle.putLong("id", cursor.getLong(FavouritesContract.COL_MOVIE_ID));
                        bundle.putString("overview", cursor.getString(FavouritesContract.COL_OVERVIEW));
                        bundle.putString("release_date", cursor.getString(FavouritesContract.COL_RELEASE_DATE));
                        bundle.putString("original_title", cursor.getString(FavouritesContract.COL_ORIGINAL_TITLE));
                        bundle.putString("original_language", cursor.getString(FavouritesContract.COL_ORIGINAL_LANGUAGE));
                        bundle.putString("backdrop_path", "http://image.tmdb.org/t/p/" + "w185" + cursor.getString(FavouritesContract.COL_BACKDROP_PATH));
                        bundle.putBoolean("adult", (cursor.getLong(FavouritesContract.COL_ADULT) == 0) ? false : true);
                        bundle.putDouble("popularity", cursor.getLong(FavouritesContract.COL_POPULARITY));
                        bundle.putDouble("vote_average", cursor.getDouble(FavouritesContract.COL_VOTE_AVERAGE));
                        bundle.putLong("vote_count", cursor.getLong(FavouritesContract.COL_VOTE_COUNT));
                        communicator.respond(bundle);
                    }
                } else {
                    Toast.makeText(getActivity(), "hello", Toast.LENGTH_LONG).show();
                    getActivity().findViewById(R.id.no_fav).setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        communicator = (Communicator) activity;
        super.onAttach(activity);
    }
    //OnCreateLoader initializes the loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), FavouritesContract.FavouritesEntry.CONTENT_URI, FavouritesContract.projection, null, null, null);
    }
    //The loading of data is finished and onLoadFinished is called
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //change the adapter
        adapter.swapCursor(data);
        if (data.getCount() == 0) {
            //no favorites, so display a textview showing no favorites and hide the gridview
            text.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        } else {
            //favorites exist. So remove the textView and display the GridView
            text.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //reseting the loader
        adapter.swapCursor(null);
    }
}
