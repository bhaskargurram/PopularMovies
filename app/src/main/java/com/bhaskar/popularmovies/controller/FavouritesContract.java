package com.bhaskar.popularmovies.controller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by bhaskar on 1/2/16.
 * This class stores the miscellaneous database details.
 */
public class FavouritesContract {

    public static final String[] projection = {FavouritesEntry._ID,
            FavouritesEntry.COLUMN_MOVIE_ID,
            FavouritesEntry.COLUMN_ORIGINAL_TITLE,
            FavouritesEntry.COLUMN_TITLE,
            FavouritesEntry.COLUMN_OVERVIEW,
            FavouritesEntry.COLUMN_RELEASE_DATE,
            FavouritesEntry.COLUMN_BACKDROP_PATH,
            FavouritesEntry.COLUMN_ADULT,
            FavouritesEntry.COLUMN_ORIGINAL_LANGUAGE,
            FavouritesEntry.COLUMN_POSTER_PATH,
            FavouritesEntry.COLUMN_POPULARITY,
            FavouritesEntry.COLUMN_VOTE_AVERAGE,
            FavouritesEntry.COLUMN_VOTE_COUNT};

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_ORIGINAL_TITLE = 2;
    public static final int COL_TITLE = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_RELEASE_DATE = 5;
    public static final int COL_BACKDROP_PATH = 6;
    public static final int COL_ADULT = 7;
    public static final int COL_ORIGINAL_LANGUAGE = 8;
    public static final int COL_POSTER_PATH = 9;
    public static final int COL_POPULARITY = 10;
    public static final int COL_VOTE_AVERAGE = 11;
    public static final int COL_VOTE_COUNT = 12;


    public static final String CONTENT_AUTHORITY = "com.bhaskar.popularmovies";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITES = "favourites";

    public static class FavouritesEntry implements BaseColumns {
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_FAVOURITES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_FAVOURITES;
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static Uri buildFavouritesUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
