package com.bhaskar.popularmovies.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.adapters.FavouriteAdapter;
import com.bhaskar.popularmovies.controller.FavouritesContract;
import com.bhaskar.popularmovies.model.Communicator;
import com.bhaskar.popularmovies.model.Item;

import java.util.ArrayList;

/**
 * Created by bhaskar on 4/2/16.
 */
public class Favourites extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    GridView gridView;
    ArrayList<Item> arrayList;
    FavouriteAdapter adapter;
    public static final int FAVOURITE_ID = 0;
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_frag_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        arrayList = new ArrayList<Item>();
        adapter = new FavouriteAdapter(getActivity(), null, 0);
        gridView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(FAVOURITE_ID, null, this);
        SharedPreferences preferences1 = getActivity().getSharedPreferences("twopane_pref", getActivity().MODE_PRIVATE);
        Boolean twopane = preferences1.getBoolean("twopane", false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
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
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        communicator = (Communicator) activity;
        super.onAttach(activity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), FavouritesContract.FavouritesEntry.CONTENT_URI, FavouritesContract.projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
