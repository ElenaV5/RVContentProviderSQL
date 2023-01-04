package ru.synergy.rvcontentproviderwithsql.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import ru.synergy.rvcontentproviderwithsql.tablemoc.CustomSQLiteOpenHelper;
import ru.synergy.rvcontentproviderwithsql.tablemoc.TableItems;

public class RequestProvider extends ContentProvider {

    private static final String TAG = "RequestProvider";
    private SQLiteOpenHelper mSQLiteOpenHelper;
    private static final UriMatcher sUriMatcher;

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".db";

    public static final int TABLE_ITEMS = 0;

    static{
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TableItems.NAME + "/offset/" + "#", TABLE_ITEMS);
    }

    public static Uri urlForItems(int limit){
        return Uri.parse("Content://" + AUTHORITY + "/" + TableItems.NAME + "/offset/" + limit);
    }

    @Override
    public boolean onCreate() {
        mSQLiteOpenHelper = new CustomSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mSQLiteOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        Cursor c = null;
        String offset = null;

        switch (sUriMatcher.match(uri)){
            case TABLE_ITEMS:
                sqb.setTables(TableItems.NAME);
                offset = uri.getLastPathSegment();
                break;

            default: break;
        }

        int intOffset = Integer.parseInt(offset);
        String limitArg = intOffset + ", " + 30;
        Log.d(TAG, "query: " + limitArg);
        c = sqb.query(db, projection, selection, selectionArgs, null, null, sortOrder, limitArg);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return BuildConfig.APPLICATION_ID + ".item";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = "";
        switch (sUriMatcher.match(uri)) {
            case TABLE_ITEMS: {
                table = TableItems.NAME;
                break;
            }
        }
        long result = mSQLiteOpenHelper.getWritableDatabase().insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            throw new SQLException("Insert with conflict");
        }

        Uri retUri = ContentUris.withAppendedId(uri, result);
        return retUri;
    }

    @Override
    public int delete(Uri uri,  String selection, String[] selectionArgs) {
        return -1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return -1;
    }
}
