package com.braindualxback;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
    private static final String TABLE_SCORE_GAME = "score_game";
    private static final String TABLE_PLAYTIME = "playtime";

    // Score Table Columns names
    private static final String KEY_ID_SCORE = "_id";
    private static final String KEY_SCORE = "score_value";
    private static final String KEY_SCORE_GAME = "score_value_game";
    private static final String PLAYER_NAME = "player_name";
    private static final String DATE = "date";
    private static final String DATE_SHORT = "date_short";
    private static final String NBACK = "nback";
    private static final String AREA = "area";
    private static final String PLAYTIME = "playtime";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE + "("
                + KEY_ID_SCORE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLAYER_NAME + " TEXT,"
                + DATE + " int,"
                + NBACK + " int,"
                + AREA + " int,"
                + KEY_SCORE + " TEXT" + ")";

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "onCreate: " + CREATE_SCORE_TABLE);

        String CREATE_SCORE_TABLE_GAME = "CREATE TABLE " + TABLE_SCORE_GAME + "("
                + KEY_ID_SCORE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLAYER_NAME + " TEXT,"
                + DATE + " int,"
                + KEY_SCORE_GAME + " TEXT" + ")";

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "onCreate: " + CREATE_SCORE_TABLE_GAME);

        String CREATE_PLAYTIME_TABLE_GAME = "CREATE TABLE " + TABLE_PLAYTIME + "("
                + KEY_ID_SCORE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLAYER_NAME + " TEXT,"
                + PLAYTIME + " int,"
                + DATE_SHORT + " TEXT UNIQUE" + ")";


        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "onCreate: " + CREATE_PLAYTIME_TABLE_GAME);


        db.execSQL(CREATE_SCORE_TABLE);
        db.execSQL(CREATE_SCORE_TABLE_GAME);
        db.execSQL(CREATE_PLAYTIME_TABLE_GAME);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE_GAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYTIME);

        // Create tables again
        onCreate(db);
    }

    public void ClearDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCORE, null, null);
        db.close();
    }

    public int[] getAllTimes (String date){
        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "in getAllTimes: " + date);

        int[] returnint = new int[2];
        returnint[0] = 0;
        returnint[1] = 0;

        String selectQuery = "SELECT * FROM " + TABLE_PLAYTIME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        int[] time = new int[cursor.getCount()];
        int[] id = new int[cursor.getCount()];
        String[] dbdate = new String[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {

            id[i] = cursor.getInt(0);
            time[i] = cursor.getInt(2);
            dbdate[i] = cursor.getString(3);

            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getInt(0) id: " + cursor.getInt(0));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getInt(2) timr: " + cursor.getInt(2));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(3) dbdate: " + cursor.getString(3));

            if(cursor.getString(3).equals(date)){
                if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "Match");
                returnint[0] = cursor.getInt(0);
                returnint[1] = cursor.getInt(2);

                cursor.close();
                //db.close();
                return returnint;
            }

            i++;
        }

        cursor.close();
        //db.close();
        return returnint;
    }

    public void insertOrUpdate(String Player, int playtime){

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "in insertOrUpdate - Player: " + Player + " playtime: " + playtime);

        String playdate;
        int[] FromDb = new int[2];

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(KEY_ID_SCORE,1);
        values.put(PLAYER_NAME,Player);
        values.put(PLAYTIME, playtime);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        Date resultdate = new Date(System.currentTimeMillis());
        playdate = sdf.format(resultdate);
        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "in insertOrUpdate - playdate: " + playdate);
        values.put(DATE_SHORT, playdate); // date

        int id = (int) db.insertWithOnConflict(TABLE_PLAYTIME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "insertOrUpdate - id: " + id);

        if (id == -1) {
            //Unique Data conflicts so we have update the entry.. Let's find the ID and value..

            FromDb  = getAllTimes(playdate);

            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "FromDb[0] " + FromDb[0]);
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "FromDb[1] " + FromDb[1]);

            values.put(PLAYTIME, playtime + FromDb[1]);

            db.update(TABLE_PLAYTIME, values, "_id=?", new String[] {Integer.toString(FromDb[0])});  // number 1 is the _id here, update to variable for your code
        }

        db.close();
    }

    // Adding new score
    public void addScore(String Player, int score, int nback, int area) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PLAYER_NAME, Player); // name
        values.put(DATE, System.currentTimeMillis());
        values.put(KEY_SCORE, score); // score value
        values.put(NBACK, nback);
        values.put(AREA, area);
        // Inserting Values
        db.insert(TABLE_SCORE, null, values);

        db.close();
    }

    // Adding new score
    public void addScore_game(String Player, int score) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PLAYER_NAME, Player); // name
        values.put(DATE, System.currentTimeMillis());
        values.put(KEY_SCORE_GAME, score); // score value
        // Inserting Values
        db.insert(TABLE_SCORE_GAME, null, values);

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

    public int getDBsize_game(){
        int db_size = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_SCORE_GAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        db_size = cursor.getCount();
        cursor.close();
        db.close();

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "db_size_game: " + db_size);

        return  db_size;
    }

    // Getting All Scores
    //public String[] getAll() {
    public Object[] getAll() {

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "getAllScores()...");

        Object[] arrayObjects = new Object[6];
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_SCORE;
        String selectQuery = "SELECT * FROM " + TABLE_SCORE + " ORDER BY CAST("+ KEY_SCORE+" AS INTEGER) DESC, " + NBACK + " DESC, " + AREA + " DESC";
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
        //String[] date = new String[cursor.getCount()];
        long[] date = new long[cursor.getCount()];
        String[] score = new String[cursor.getCount()];

        int[] area = new int[cursor.getCount()];
        int[] nback = new int[cursor.getCount()];

        while (cursor.moveToNext()) {

            player[i] = cursor.getString(1);
            date[i] = cursor.getLong(2);
            nback[i] = cursor.getInt(3);
            area[i] = cursor.getInt(4);

            score[i] = cursor.getString(5);


            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(1) player: " + cursor.getString(1));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getLong(2) date: " + cursor.getLong(2));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getInt(3) nback: " + cursor.getInt(3));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getInt(4) area: " + cursor.getInt(4));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(3) score: " + cursor.getString(5));


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
        arrayObjects [2] = nback;
        arrayObjects [3] = area;
        arrayObjects [4] = score;

        return arrayObjects;
    }

    public Object[] getAll_game() {

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "getAll_game()...");

        Object[] arrayObjects = new Object[3];
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SCORE_GAME + " ORDER BY CAST("+ KEY_SCORE_GAME+" AS INTEGER) DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        int i = 0;

        String[] player = new String[cursor.getCount()];
        long[] date = new long[cursor.getCount()];
        String[] score = new String[cursor.getCount()];

        while (cursor.moveToNext()) {

            player[i] = cursor.getString(1);
            date[i] = cursor.getLong(2);
            score[i] = cursor.getString(3);

            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(1) player: " + cursor.getString(1));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getLong(2) date: " + cursor.getLong(2));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(3) score: " + cursor.getString(3));

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

    public Object[] getAll_game_for_chart() {

        if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "getAll_game()...");

        Object[] arrayObjects = new Object[3];
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SCORE_GAME + " ORDER BY CAST("+ DATE +" AS INTEGER) ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        int i = 0;

        String[] player = new String[cursor.getCount()];
        long[] date = new long[cursor.getCount()];
        String[] score = new String[cursor.getCount()];

        while (cursor.moveToNext()) {

            player[i] = cursor.getString(1);
            date[i] = cursor.getLong(2);
            score[i] = cursor.getString(3);

            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(1) player: " + cursor.getString(1));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getLong(2) date: " + cursor.getLong(2));
            if(MainActivity.ENABLE_LOGS) Log.d(MainActivity.TAG, "cursor.getString(3) score: " + cursor.getString(3));

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