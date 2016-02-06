package com.bhaskar.popularmovies.controller;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by bhaskar on 1/2/16.
 */
public class FavouritesProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = buildUriMatcher();
    DatabaseHelper dbhelper;
    static final int favourites = 100;

    @Override
    public boolean onCreate() {
        dbhelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Cursor returnCursor = null;
        switch (match) {
            case favourites:
                returnCursor = db.query(FavouritesContract.FavouritesEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case favourites:
                return FavouritesContract.FavouritesEntry.CONTENT_TYPE;


        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri = null;
        switch (match) {
            case favourites:
                long id = db.insert(FavouritesContract.FavouritesEntry.TABLE_NAME, null, contentValues);
                if (id > 0)
                    returnUri = FavouritesContract.FavouritesEntry.buildFavouritesUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        int returnVal = 0;
        int match = uriMatcher.match(uri);
        switch (match) {
            case favourites:
                returnVal = db.delete(FavouritesContract.FavouritesEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (returnVal != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return returnVal;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        int returnVal = 0;
        int match = uriMatcher.match(uri);
        switch (match) {
            case favourites:
                returnVal = db.update(FavouritesContract.FavouritesEntry.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (returnVal != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return returnVal;
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = FavouritesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FavouritesContract.PATH_FAVOURITES, favourites);

        return matcher;
    }
}
