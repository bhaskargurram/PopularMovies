package com.bhaskar.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.model.TrailerItem;

import java.util.ArrayList;

/**
 * Created by bhaskar on 4/2/16.
 * This is an adapter to get trailers of a movie.
 */
public class TrailerAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<TrailerItem> arrayList;

    public TrailerAdapter(Context context, ArrayList<TrailerItem> arrayList) {

        mContext = context;
        this.arrayList = arrayList;
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootview = inflater.inflate(R.layout.videos_details, null);

        TextView text = (TextView) rootview.findViewById(R.id.text_trailer);
        text.setText(arrayList.get(i).getName());
        return rootview;
    }
}
