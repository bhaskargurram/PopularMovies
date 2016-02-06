package com.bhaskar.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bhaskar on 25/12/15.
 */
public class GridAdapter extends BaseAdapter {
    Context context;
    ArrayList<Item> arrayList;

    public GridAdapter(Context context, int resource, ArrayList<Item> objects) {
        this.context = context;
        this.arrayList = objects;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid = inflater.inflate(R.layout.gridview_item, null);
        ImageView image;

        image = (ImageView) grid.findViewById(R.id.grid_image);

        // TextView moviewname = (TextView) grid.findViewById(R.id.moviewname);
        String url = "http://image.tmdb.org/t/p/" + "w342" + arrayList.get(position).getPoster_path();
        Picasso.with(context).load(url).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).noFade().into(image);


        return grid;
    }
}
