package com.bhaskar.popularmovies.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bhaskar.popularmovies.R;
import com.bhaskar.popularmovies.adapters.GridAdapter;
import com.bhaskar.popularmovies.model.Communicator;
import com.bhaskar.popularmovies.model.Item;

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
import java.util.Locale;

/**
 * Created by bhaskar on 5/2/16.
 */
public class MainFragment extends Fragment {

    GridView gridView;
    ArrayList<Item> arrayList;
    String toggle;
    long pageno = 1;
    int total_pages = 0;
    private int preLast;
    boolean twopane;
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_frag_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        arrayList = new ArrayList<Item>();
        SharedPreferences preferences = getActivity().getSharedPreferences("toggle", getActivity().MODE_PRIVATE);
        toggle = preferences.getString("toggle", "popularity.desc");
        SharedPreferences preferences1 = getActivity().getSharedPreferences("twopane_pref", getActivity().MODE_PRIVATE);
        twopane = preferences1.getBoolean("twopane", false);
        Log.d("", "inside on create of Main Fragment, twopane=" + twopane);
        checkAndLoad();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.size() > 0) {
                    Item item = arrayList.get(i);
                    if (!twopane) {
                        Log.d("", "Main fragment single pane onclick");
                        Intent intent = new Intent(getActivity(), DetailActivity.class);

                        intent.putExtra("poster_path", item.getPoster_path());
                        intent.putExtra("title", item.getTitle());
                        intent.putExtra("id", item.getId());
                        intent.putExtra("overview", item.getOverview());
                        intent.putExtra("release_date", item.getRelease_date());
                        intent.putExtra("original_title", item.getOriginal_title());
                        intent.putExtra("original_language", item.getOriginal_language());
                        intent.putExtra("backdrop_path", "http://image.tmdb.org/t/p/" + "w185" + item.getBackdrop_path());
                        intent.putExtra("adult", item.isAdult());
                        intent.putExtra("popularity", item.getPopularity());
                        intent.putExtra("vote_average", item.getVote_average());
                        intent.putExtra("vote_count", item.getVote_count());

                        startActivity(intent);
                    } else {
                        Log.d("", "Main fragment twopane on click");
                        Bundle bundle = new Bundle();
                        bundle.putString("poster_path", item.getPoster_path());
                        bundle.putString("overview", item.getOverview());
                        bundle.putString("release_date", item.getRelease_date());
                        bundle.putString("original_title", item.getOriginal_title());

                        bundle.putString("original_language", new Locale(item.getOriginal_language()).getDisplayLanguage());
                        bundle.putString("title", item.getTitle());
                        bundle.putString("backdrop_path", "http://image.tmdb.org/t/p/" + "w185" + item.getBackdrop_path());
                        bundle.putBoolean("adult", item.isAdult());
                        bundle.putDouble("popularity", item.getPopularity());
                        bundle.putDouble("vote_average", item.getVote_average());
                        bundle.putLong("vote_count", item.getVote_count());
                        bundle.putLong("id", item.getId());
                        Log.d("", "inside gridview on click");
                        communicator.respond(bundle);
                    }
                }
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                //Algorithm to check if the last item is visible or not
                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    if (lastItem == totalItemCount) {
                        if (preLast != lastItem) { //to avoid multiple calls for last item
                            pageno += 1;
                            if (pageno < total_pages) {
                                checkAndLoad();
                            }
                            preLast = lastItem;
                        }
                        // you have reached end of list, load more data

                    }

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

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void checkAndLoad() {
        if (isConnectingToInternet()) {

            new LoadData().execute(toggle);
        } else {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public class LoadData extends AsyncTask<String, Void, Void> {

        String forecastJsonStr = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

            // Will contain the raw JSON response as a string.


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecas
                // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=ad7ee1290bf0b011cd28f6e3707f17de
                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String APIPARAM = "api_key";
                final String PAGE_NO = "page";
                //"q=94043&mode=json&units=metric&cnt=7&APPID=db6097a4ead74440ce42856c663f3fd1");


                Uri builtURI = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, strings[0])
                        .appendQueryParameter(PAGE_NO, String.valueOf(pageno))
                        .appendQueryParameter(APIPARAM, "ad7ee1290bf0b011cd28f6e3707f17de")
                        .build();

                URL url = new URL(builtURI.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                parseString(forecastJsonStr);
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
            String poster_path, overview, release_date, original_title, original_language, title, backdrop_path;
            boolean adult, video;
            double popularity, vote_average;
            long vote_count, id;

            try {
                JSONObject outerObject = new JSONObject(forecastJsonStr);
                total_pages = outerObject.getInt("total_pages");
                JSONArray results = outerObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movies = results.getJSONObject(i);
                    poster_path = movies.getString("poster_path");
                    id = movies.getLong("id");
                    overview = movies.getString("overview");
                    release_date = movies.getString("release_date");
                    original_title = movies.getString("original_title");
                    original_language = movies.getString("original_language");
                    title = movies.getString("title");
                    backdrop_path = movies.getString("backdrop_path");
                    adult = movies.getBoolean("adult");
                    video = movies.getBoolean("video");
                    popularity = movies.getDouble("popularity");
                    vote_average = movies.getDouble("vote_average");
                    vote_count = movies.getLong("vote_count");
                    Item item = new Item(poster_path, overview, release_date, original_title, original_language, title, backdrop_path, adult, video, popularity, vote_average, vote_count, id);
                    arrayList.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pageno == 1)
                gridView.setAdapter(new GridAdapter(getActivity(), R.layout.gridview_item, arrayList));
            else
                gridView.deferNotifyDataSetChanged();

        }
    }

}