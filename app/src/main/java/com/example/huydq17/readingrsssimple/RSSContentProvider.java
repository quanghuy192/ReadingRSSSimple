package com.example.huydq17.readingrsssimple;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by HuyDQ17 on 10/12/2015.
 */
public class RSSContentProvider extends ContentProvider {

    private static final String RSS_NAME = "com.simple.provider.rss";
    private static final String URL = "content://" + RSS_NAME + "/RSSs";
    static final Uri CONTENT_URI = Uri.parse(URL);
    private HashMap<String, String> projectName;

    private SQLiteDatabase db;
    private RSSSqliteHelper rssSqliteHelper;
    private static final UriMatcher uriMatcher;

    private static final int RSS = 0;
    private static final int RSS_ID = 1;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RSS_NAME, "/RSSs", RSS);
        uriMatcher.addURI(RSS_NAME, "/rss/#", RSS_ID);

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        rssSqliteHelper = new RSSSqliteHelper(context);
        openToRead();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(RSSSqliteHelper.RSS_TABLE_NAME);
        projectName = new HashMap<>();
        switch (uriMatcher.match(uri)) {
            case RSS: {
                sqLiteQueryBuilder.setProjectionMap(projectName);
                break;
            }
            case RSS_ID: {
                sqLiteQueryBuilder.appendWhere(RSSSqliteHelper.RSS_KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if ("".equalsIgnoreCase(sortOrder) || sortOrder == null) {
            sortOrder = RSSSqliteHelper.RSS_TITLE;
        } else {

        }
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case RSS: {
                return "vnd.android.cursor.dir/vnd.example.rss";
            }
            case RSS_ID: {
                return "vnd.android.cursor.item/vnd.example.rss";
            }
            default:
                throw new IllegalArgumentException("Unsupported Uri :" + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db.insert(RSSSqliteHelper.RSS_TABLE_NAME, "", values);
        if (rowId > 0) {
            Uri _Uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(_Uri, null);
            return _Uri;
        } else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case RSS: {
                count = db.delete(RSSSqliteHelper.RSS_TABLE_NAME, selection, selectionArgs);
                break;
            }
            case RSS_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(RSSSqliteHelper.RSS_TABLE_NAME, RSSSqliteHelper.RSS_KEY_ID + "=" + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case RSS: {
                count = db.update(RSSSqliteHelper.RSS_TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case RSS_ID: {
                count = db.update(RSSSqliteHelper.RSS_TABLE_NAME, values, RSSSqliteHelper.RSS_KEY_ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public void openToRead() {
        db = rssSqliteHelper.getReadableDatabase();
    }

    public void openToWrite() {
        db = rssSqliteHelper.getWritableDatabase();
    }

    public void close() {
        rssSqliteHelper.close();
        db.close();
    }

    static class RSSSqliteHelper extends SQLiteOpenHelper {

        private static final String RSS_DATABASE_NAME = "dataRss.db";
        private static final int RSS_DATABASE_VERSION = 1;
        public static final String RSS_TABLE_NAME = "rss";

        public static final String RSS_KEY_ID = "_id";
        public static final String RSS_TITLE = "title";
        public static final String RSS_LINK = "link";
        public static final String RSS_DESCRIPTION = "description";
        public static final String RSS_CATEGORY = "category";

        private static final String CREATE_TABLE = "create table "
                + RSS_TABLE_NAME + "(" + RSS_KEY_ID + " integer primary key autoincrement, "
                + RSS_TITLE + " text not null, " + RSS_LINK + " text not null, "
                + RSS_DESCRIPTION + " text not null, " + RSS_CATEGORY + " text not null);";


        public RSSSqliteHelper(Context context) {
            super(context, RSS_DATABASE_NAME, null, RSS_DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exist" + RSS_TABLE_NAME);
            onCreate(db);
        }
    }
}
