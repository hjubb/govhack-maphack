package maphack.qutcode.navlights;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelp extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "accidentsandroid.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }
    public Cursor getAccidents(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"0 _id","LAT","LONG","COUNT_FATALITY","COUNT_HOSPITAL","COUNT_MAJOR","COUNT_MINOR"};
        String sqlTable = "accidents";
        qb.setTables(sqlTable);

        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        //c.moveToFirst();
        return c;

    }
}