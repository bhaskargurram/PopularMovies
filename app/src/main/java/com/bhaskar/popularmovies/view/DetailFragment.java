package com.bhaskar.popularmovies.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.adapters.ReviewAdapter;
import com.bhaskar.popularmovies.adapters.TrailerAdapter;
import com.bhaskar.popularmovies.controller.FavouritesContract;
import com.bhaskar.popularmovies.model.ReviewItem;
import com.bhaskar.popularmovies.model.TrailerItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by bhaskar on 5/2/16.
 */

/**
 * This is a fragment which will be displayed on DetailActivity
 */
public class DetailFragment extends Fragment {
    String poster_path, overview, release_date, original_title, original_language, title, backdrop_path;
    int adult;
    double popularity, vote_average;
    long vote_count, id;
    final String OVERVIEW = "Overview: ",
            RELEASE_DATE = "Release Date: ",
            ORIGINAL_TITLE = "Original Title: ",
            ORIGINAL_LANGUAGE = "Original Language: ",
            TITLE = "Title: ",
            ADULT = "Adult Movie: ",
            POPULARITY = "Popularity: ",
            VOTE_AVERAGE = "Vote Average: ",
            VOTE_COUNT = "Vote Count: ";
    TextView overview_text, release_date_text, original_title_text, original_language_text, title_text, adult_text, popularity_text, vote_average_text, vote_count_text;
    ImageView backdrop_image;
    ListView trailers, reviews;
    ArrayList<TrailerItem> arrayList;
    ArrayList<ReviewItem> arrayList2;
    View view;
    boolean favourite = false;
/**
 * This function onCreateView will be called when the UI is first created. So I have overridden it to inflate the appropriate view.
 * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflating view from activity_detail_frag
        View rootView = inflater.inflate(R.layout.activity_detail_frag, container, false);
        setHasOptionsMenu(true);
        //taking the arguments passsed from FragmentActivity which contains details of all movies
        Bundle bundle = getArguments();

        if (bundle != null) {
            //putting appropriate data into the variables
            initialize(bundle);
            //initializing the display views
            initializeUI(rootView);
            //fetching trailers and reviews
            new LoadData().execute(String.valueOf(id));

            //updating the toolbar
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            //setting appropriate data into the views
            setData();

        }

        view = rootView.findViewById(R.id.snack);
        return rootView;
    }


    /*
    * setData() will set the data into the views
    * */
    public void setData() {


        overview_text.setText(overview);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        release_date_text.setText(Html.fromHtml("<b>" + RELEASE_DATE + "</b>" + release_date));
        original_title_text.setText(Html.fromHtml("<b>" + ORIGINAL_TITLE + "</b>" + original_title));
        original_language_text.setText(Html.fromHtml("<b>" + ORIGINAL_LANGUAGE + "</b>" + original_language));
        //title_text.setText(title);
        if (adult == 1)
            adult_text.setText(Html.fromHtml("<b>" + ADULT + "</b>" + "Yes"));
        else
            adult_text.setText(Html.fromHtml("<b>" + ADULT + "</b>" + "NO"));
        popularity_text.setText(Html.fromHtml("<b>" + POPULARITY + "</b>" + popularity));
        vote_average_text.setText(Html.fromHtml("<b>" + VOTE_AVERAGE + "</b>" + vote_average));
        vote_count_text.setText(Html.fromHtml("<b>" + VOTE_COUNT + "</b>" + vote_count));
        Picasso.with(getActivity()).load(backdrop_path).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).noFade().into(backdrop_image);

    }
/*
 * initializeUI() will take the rootview inflated and will initialize all the views in its subtree to be used in this fragment
 * It also puts an onClickListener to the floating action button
 */
    private void initializeUI(View view) {
        overview_text = (TextView) view.findViewById(R.id.overview_text);
        release_date_text = (TextView) view.findViewById(R.id.release_date_text);
        original_title_text = (TextView) view.findViewById(R.id.original_title_text);
        original_language_text = (TextView) view.findViewById(R.id.original_language_text);

        adult_text = (TextView) view.findViewById(R.id.adult_text);
        popularity_text = (TextView) view.findViewById(R.id.popularity_text);
        vote_average_text = (TextView) view.findViewById(R.id.vote_average_text);
        vote_count_text = (TextView) view.findViewById(R.id.vote_count_text);
        backdrop_image = (ImageView) view.findViewById(R.id.background_image);

        reviews = (ListView) view.findViewById(R.id.listview_reviews);
        reviews.setFocusable(false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(send());
            }
        });


    }
/*
* put the data from the bundle into appropriate variables
* */
    public void initialize(Bundle bundle) {
        poster_path = bundle.getString("poster_path");
        overview = bundle.getString("overview");
        release_date = bundle.getString("release_date");
        original_title = bundle.getString("original_title");
        original_language = bundle.getString("original_language");

        title = bundle.getString("title");
        backdrop_path = bundle.getString("backdrop_path");
        adult = (bundle.getBoolean("adult", false) == true) ? 1 : 0;
        popularity = bundle.getDouble("popularity", 0);
        vote_average = bundle.getDouble("vote_average", 0);
        vote_count = bundle.getLong("vote_count", 0);
        id = bundle.getLong("id", 0);
        favourite = bundle.getBoolean("favourites", false);
        Log.d("", "poster_path=" + poster_path);
        Log.d("bhaskar", "favourite=" + favourite);

    }
/*
* send() function is use for sharing text to other applications.
* */
    private Intent send() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, title + ": " + overview + " #PopularMovies" + " Watch the trailer here http://www.youtube.com/watch?v=\"" + arrayList.get(0).getKey());
        return intent;

    }

//creaing the Options Menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem item = menu.findItem(R.id.like_btn);
        //It checks if the movie is a favorite movie. If yes change the favorite icon to display as favorite
        Cursor cursor = getActivity().getContentResolver().query(FavouritesContract.FavouritesEntry.CONTENT_URI
                , new String[]{FavouritesContract.FavouritesEntry._ID},
                FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null);
        if (cursor.moveToFirst()) {
            item.setIcon(android.R.drawable.btn_star_big_on);

        } else {


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idinside = item.getItemId();

        switch (idinside) {


            case R.id.home:
                //this part is implemented in DetailActivity. So returning false
                return false;


            case R.id.like_btn:
                //the user clicked the like icon. So updating into the database that this movie is liked.
                Cursor cursor = getActivity().getContentResolver().query(FavouritesContract.FavouritesEntry.CONTENT_URI
                        , new String[]{FavouritesContract.FavouritesEntry._ID},
                        FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null);
                if (!cursor.moveToFirst()) {
                    ContentValues cv = new ContentValues();
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID, id);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_POSTER_PATH, poster_path);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_OVERVIEW, overview);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_RELEASE_DATE, release_date);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_ORIGINAL_TITLE, original_title);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_ORIGINAL_LANGUAGE, original_language);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_TITLE, title);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_BACKDROP_PATH, backdrop_path);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_ADULT, adult);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_POPULARITY, popularity);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_VOTE_AVERAGE, vote_average);
                    cv.put(FavouritesContract.FavouritesEntry.COLUMN_VOTE_COUNT, vote_count);

                    getActivity().getContentResolver().insert(FavouritesContract.FavouritesEntry.CONTENT_URI, cv);
                    item.setIcon(android.R.drawable.btn_star_big_on);
                } else {

                    getActivity().getContentResolver().delete(FavouritesContract.FavouritesEntry.CONTENT_URI, FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(id)});
                    item.setIcon(android.R.drawable.btn_star_big_off);


                }
                return true;


            case R.id.share_trailer:
                //the user clicked the share trailer option
                if (arrayList.size() > 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey watchout this trailer of " + title + " http://www.youtube.com/watch?v=" + arrayList.get(0).getKey());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "No trailers available for this movie.", Toast.LENGTH_LONG).show();
                }
                return true;


            case R.id.fav:
                //user wants to see all the favorite movies
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("favourite", true);
                startActivity(intent);

                return true;


            case R.id.play:
                //user wants to play the trailer. Checking if trailer exists. else displaying no trailer message
                if (arrayList.size() > 0)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + arrayList.get(0).getKey())));
                else
                    Toast.makeText(getActivity(), "No trailer available for this movie.", Toast.LENGTH_LONG).show();
                return true;
            default:
                break;

        }


        return false;
    }

/*
* LoadData is an inner class which is used to fetch the movie reviews and the trailers asynchronously.
* */
    public class LoadData extends AsyncTask<String, Void, Void> {

        String forecastJsonStr = null;
        String reviewsJsonStr = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //creating 2 arraylists
            arrayList = new ArrayList<TrailerItem>();
            arrayList2 = new ArrayList<ReviewItem>();
        }

        @Override
        protected Void doInBackground(String... strings) {

            if (strings.length == 0) {
                Log.d("", "string length =0");
                return null;
            }
            Log.d("", "doinbg");

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            HttpURLConnection urlConnection2 = null;
            // Will contain the raw JSON response as a string.


            try {

                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/" + strings[0] + "/videos?";
                final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/" + strings[0] + "/reviews?";
                final String APIPARAM = "api_key";


                Uri builtURI = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APIPARAM, getString(R.string.api_param))
                        .build();

                Uri builtURI2 = Uri.parse(REVIEWS_BASE_URL).buildUpon()
                        .appendQueryParameter(APIPARAM, getString(R.string.api_param))
                        .build();
                URL url = new URL(builtURI.toString());
                URL url2 = new URL(builtURI2.toString());
                // Creating the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Reading the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                parseString(forecastJsonStr);

                urlConnection2 = (HttpURLConnection) url2.openConnection();
                urlConnection2.setRequestMethod("GET");
                urlConnection2.connect();

                InputStream inputStream2 = urlConnection2.getInputStream();
                StringBuffer buffer2 = new StringBuffer();
                if (inputStream2 == null) {
                    // Nothing to do.
                    Log.d("", "inputstream2 null");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream2));

                String line2;
                while ((line2 = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer2.append(line2 + "\n");
                }

                if (buffer2.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                reviewsJsonStr = buffer2.toString();
                parseReviewString(reviewsJsonStr);


                Log.d("", forecastJsonStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }


            return null;
        }

        private void parseString(String forecastJsonStr) {

            try {
                JSONObject outerObject = new JSONObject(forecastJsonStr);
                JSONArray results = outerObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    TrailerItem item = new TrailerItem(jsonObject.getString("name"), jsonObject.getString("key"));
                    arrayList.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
/**
 * parseReviewString function taked input a string in Json format and parses it.
 */
        private void parseReviewString(String reviewsJsonStr) {
            try {
                JSONObject outerObject = new JSONObject(reviewsJsonStr);
                JSONArray results = outerObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    ReviewItem item = new ReviewItem(jsonObject.getString("author"), jsonObject.getString("content"));
                    arrayList2.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
/**
 *
 * onPostExecute will be called when the details are fetched.
 * So set the adapter of the listview
 * */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  trailers.setAdapter(new TrailerAdapter(getActivity(), arrayList));
            reviews.setAdapter(new ReviewAdapter(getActivity(), arrayList2));

        }
    }


}
