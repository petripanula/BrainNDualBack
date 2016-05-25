package com.braindualxback;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoresActivity extends AppCompatActivity {

    String Player = "Pete";
    public static String[] Hiscores;
    public static String[] Players;
    public static long[] Dates;
    public static int[] nBacks;
    public static int[] areas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scores);

        String HTMLsourceString;
        int FontSize = 12;
        int tablesize = 10;

        DatabaseHandler db = new DatabaseHandler(this);
        Object[] arrayObjects = db.getAll();

        Hiscores = new String[db.getDBsize()];
        Players = new String[db.getDBsize()];
        Dates = new long[db.getDBsize()];
        nBacks = new int[db.getDBsize()];
        areas = new int[db.getDBsize()];

        Players = (String[])arrayObjects[0];
        Dates = (long[])arrayObjects[1];
        nBacks = (int[])arrayObjects[2];
        areas = (int[])arrayObjects[3];
        Hiscores = (String[])arrayObjects[4];

        LinearLayout ll = (LinearLayout) findViewById(R.id.my_hiscore);

        if (db.getDBsize()<tablesize){
            tablesize = db.getDBsize();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for(int j = 0; j < tablesize; j++){
            // create a new textview
            final TextView rowTextView = new TextView(this);

            Date resultdate = new Date(Dates[j]);

            HTMLsourceString = "<font color=#00FF00> PLAYER <b>" + Players[j] + "</b> AREA (<b>" + areas[j] + "</b>) N-BACK (<b>" + nBacks[j] + "</b>) SCORE (<b>" + Hiscores[j] + "</b>) DATE: <b>" + sdf.format(resultdate) + "</b></font>";

            rowTextView.setText(Html.fromHtml(HTMLsourceString));
            rowTextView.setGravity(Gravity.CENTER);
            rowTextView.setTextColor(Color.WHITE);
            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            // add the textview to the linearlayout
            ll.addView(rowTextView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ScoresActivity onResume...");

        /*
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
        */

    }

    @Override
    public void onPause() {
        super.onPause();
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ScoresActivity onPause...");
    }

    @Override
    public void onStart() {
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ScoresActivity onStart...");
        super.onStart();
    }

    @Override
    public void onStop() {
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ScoresActivity onStop...");
        super.onStop();
    }
}
