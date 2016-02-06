package com.bhaskar.popularmovies.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bhaskar on 1/2/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favourites.db";

    public DatabaseHelper(Context mcontext) {
        super(mcontext, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String creatFav = "CREATE TABLE " + FavouritesContract.FavouritesEntry.TABLE_NAME
                + " ( "
                + FavouritesContract.FavouritesEntry._ID + " INTEGER PRIMARY KEY, "
                + FavouritesContract.FavouritesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_ADULT + " INTEGER NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_POPULARITY + " REAL NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
                + FavouritesContract.FavouritesEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(creatFav);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesContract.FavouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
