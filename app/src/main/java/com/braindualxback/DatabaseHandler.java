package com.braindualxback;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by ppanula on 23.5.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "game";

    // Table name
    private static final String TABLE_SCORE = "score";

    // Score Table Columns names
    private static final String KEY_ID_SCORE = "_id";
    private static final String KEY_SCORE = "score_value";
    private static final String PLAYER_NAME = "player_name";
    private static final String DATE = "date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE + "("
                + KEY_ID_SCORE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLAYER_NAME + " TEXT,"
                + DATE + " TEXT,"
                + KEY_SCORE + " TEXT" + ")";

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "onCreate: " + CREATE_SCORE_TABLE);

        db.execSQL(CREATE_SCORE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);

        // Create tables again
        onCreate(db);
    }

    public void ClearDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCORE, null, null);
        db.close();
    }

    // Adding new score
    public void addScore(String Player, String Date, int score) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PLAYER_NAME, Player); // score value
        values.put(DATE, Date); // score value
        values.put(KEY_SCORE, score); // score value

        // Inserting Values
        db.insert(TABLE_SCORE, null, values);

        db.close();

    }

    public int getDBsize(){
        int db_size = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_SCORE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        db_size = cursor.getCount();
        cursor.close();
        db.close();

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "db_size: " + db_size);

        return  db_size;
    }

    // Getting All Scores
    //public String[] getAll() {
    public Object[] getAll() {

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "getAllScores()...");

        Object[] arrayObjects = new Object[3];
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_SCORE;
        String selectQuery = "SELECT * FROM " + TABLE_SCORE + " ORDER BY CAST("+ KEY_SCORE+" AS INTEGER) DESC";
        /*
        String selectQuery = "SELECT " + KEY_SCORE + " * "
                + KEY_SPONSOR + " AS result FROM " + DATABASE_TABLE + " ORDER BY "+ KEY_SWIMMERLAPS+" DESC", null);
         */
        //String selectQuery = MyQuery;

        //To filter out just top ten scores from the db, u may change the query in get method to :
        //String selectQuery = "SELECT  * FROM " + TABLE_SCORE + "LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        int i = 0;

        String[] player = new String[cursor.getCount()];
        String[] date = new String[cursor.getCount()];
        String[] score = new String[cursor.getCount()];


        while (cursor.moveToNext()) {

            player[i] = cursor.getString(1);
            date[i] = cursor.getString(2);
            score[i] = cursor.getString(3);

            /*
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(1): " + cursor.getString(1));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(2): " + cursor.getString(2));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(3): " + cursor.getString(3));
            */

            i++;

        }
        cursor.close();
        db.close();
        /*
        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "score: " + Arrays.toString(score));
        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "date: " + Arrays.toString(date));
        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "player: " + Arrays.toString(player));
        */
        arrayObjects [0] = player;
        arrayObjects [1] = date;
        arrayObjects [2] = score;

        return arrayObjects;
    }
}