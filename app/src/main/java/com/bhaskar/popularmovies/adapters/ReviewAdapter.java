package com.bhaskar.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by bhaskar on 6/2/16.
 */
public class ReviewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ReviewItem> arrayList;

    public ReviewAdapter(Context mContext, ArrayList<ReviewItem> arrayList) {
        this.mContext = mContext;
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootview = inflater.inflate(R.layout.reviews_item, null);
        TextView author = (TextView) rootview.findViewById(R.id.author);
        TextView comment = (TextView) rootview.findViewById(R.id.comment);
        author.setText(arrayList.get(i).getAuthor());
        comment.setText(arrayList.get(i).getComment());
        return rootview;
    }
}
