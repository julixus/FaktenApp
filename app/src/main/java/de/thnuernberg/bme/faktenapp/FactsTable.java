package de.thnuernberg.bme.faktenapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;

public class FactsTable extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "facts.db";
    private static int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "facts";
    public static final String FACT_TITLE = "fact_title";
    public static final String FACT_TEXT = "fact_text";
    public static final String IMAGE_PATH = "image_path";

    public FactsTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FACT_TITLE + " TEXT NOT NULL, "
                + FACT_TEXT + " TEXT NOT NULL, "
                + IMAGE_PATH + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addFact(String title, String text, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FACT_TITLE, title);
        values.put(FACT_TEXT, text);
        values.put(IMAGE_PATH, imagePath);
        db.insert(TABLE_NAME, null, values);
        //db.close();
    }
}
