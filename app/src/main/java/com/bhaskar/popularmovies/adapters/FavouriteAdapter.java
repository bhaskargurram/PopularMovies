package com.bhaskar.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.controller.FavouritesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by bhaskar on 1/2/16.
 */
public class FavouriteAdapter extends CursorAdapter {
    public FavouriteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.gridview_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.grid_image);
        String url = "http://image.tmdb.org/t/p/" + "w342" + cursor.getString(FavouritesContract.COL_POSTER_PATH);
        Picasso.with(context).load(url).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).noFade().into(imageView);

    }

}
