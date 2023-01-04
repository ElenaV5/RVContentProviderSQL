package ru.synergy.rvcontentproviderwithsql.tablemoc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "CustomSQLiteOpenHelper";

    public CustomSQLiteOpenHelper(Context context) {
        super(context, "db.db", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableItems.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TableItems.DROP_TABLE);
        onCreate(db);
    }
}
