package com.braindualxback;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.NumberPicker;
import android.widget.Toast;

import static java.lang.Math.sqrt;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseGameActivity implements NumberPicker.OnValueChangeListener,RateMeMaybe.OnRMMUserChoiceListener {

    //TODO for release builds set to ENABLE_LOGS = false
    public static final boolean ENABLE_LOGS = true;
    public static final String TAG = "Pete";

    private MediaPlayer mp;

    CountDownTimer ShowRedTimer;
    CountDownTimer ClearRedTimer;
    ImageView imageView;
    Boolean ShowRedTimerRunning = false;
    Boolean ClearRedTimerRunning = false;
    int random_nbr = -1;
    int sound_id = -1;
    int nBack = 2;
    public static final ImageView[] mImageViews = new ImageView[10];

    public static int[] NewArray;

    private  List myVisualList = new ArrayList();
    private  List mySoundList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        int windowWidth,windowHeight,NbrOfPictures,sizeofcubeside,HeightOfGridArea=0;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth = size.x;
        windowHeight = size.y;
        NbrOfPictures = 9;

        GridView gridview = (GridView) findViewById(R.id.gridview);
        sizeofcubeside = (int)sqrt(NbrOfPictures);
        HeightOfGridArea = windowHeight*6/7;

        /*
        imageView = new ImageView(this);
        BitmapDrawable ob = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(imageView.getResources(), Pictures.BACKGROUND_IDS[pic_id], windowWidth, windowHeight));

        LinearLayout rLayout = (LinearLayout) findViewById (R.id.memory_activity);
        rLayout.setBackground(ob);
        */

        gridview.setNumColumns(sizeofcubeside);

        int NbrOfVerticalPictures = NbrOfPictures/sizeofcubeside;
        int TotalPadding = 16*NbrOfVerticalPictures;
        if (ENABLE_LOGS) Log.d("Pete", "NbrOfVerticalPictures: " + NbrOfVerticalPictures);
        if (ENABLE_LOGS) Log.d("Pete", "TotalPadding: " + TotalPadding);

        int picturewidth;
        picturewidth = (windowWidth / sizeofcubeside);

        if (ENABLE_LOGS) Log.d("Pete", "picturewidth: " + picturewidth);

        if((NbrOfVerticalPictures*picturewidth+TotalPadding)>HeightOfGridArea) {
            if (ENABLE_LOGS) Log.d("Pete", "Pictures does't fit to Y: " + windowHeight);

            picturewidth = HeightOfGridArea/NbrOfVerticalPictures - TotalPadding;
            if (ENABLE_LOGS) Log.d("Pete", "new picturewidth: " + picturewidth);
        }else{
            picturewidth = picturewidth - 50;
        }

        NewArray = new int[NbrOfPictures];

        int j=0,k=0;
        for(int l=0; l<NbrOfPictures; l++) {
            NewArray[l] = Pictures.BACKGROUND_IDS[k];
            j++;
            if(j==2){
                j=0;
                k++;
            }
        }

        gridview.setAdapter(new ImageAdapter(this, 9, picturewidth));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (ENABLE_LOGS) Log.d("Pete", "onItemClick - v.getId(): " + v.getId());

                imageView = (ImageView) v;
                imageView.clearColorFilter();


            }

        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //setContentView(R.layout.activity_memory_game);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onStart...");
        super.onStart();
        // The rest of your onStart() code.
        //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() {
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onStop...");
        super.onStop();
        // The rest of your onStop() code.
        //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }

    @Override
    public void onPause() {
        super.onPause();
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onPause...");

        StopAllTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onResume...");
        SetInitUI();
        ReadPreferences();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if(ENABLE_LOGS) Log.v("Pete", "onValueChange - newVal: " + newVal);
        if(ENABLE_LOGS) Log.v("Pete", "onValueChange - oldVal: " + oldVal);
    }

    @Override
    public void handlePositive() {
        Toast.makeText(this, "Positive", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleNeutral() {
        Toast.makeText(this, "Neutral", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleNegative() {
        Toast.makeText(this, "Negative", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void RatingStarted() {
        Toast.makeText(this, "RatingStarted", Toast.LENGTH_SHORT).show();
        //easyTracker.send(MapBuilder.createEvent("RatingStarted", "rating", "5", null).build());

    }

    @Override
    public void onSignInSucceeded() {
        if(ENABLE_LOGS) Log.v("Pete", "In onSignInSucceeded...");

    }

    @Override
    public void onSignInFailed() {
        if(ENABLE_LOGS) Log.v("Pete", "In onSignInFailed...");

    }

    public void Start(View arg0){
        Toast.makeText(MainActivity.this, "Start!!!!", Toast.LENGTH_SHORT).show();

        ShowRedTimer(5000);

        findViewById(R.id.settings).setVisibility(View.GONE);
        findViewById(R.id.achievements).setVisibility(View.GONE);
        findViewById(R.id.start).setVisibility(View.GONE);
        findViewById(R.id.info).setVisibility(View.GONE);
        findViewById(R.id.matchpic).setVisibility(View.VISIBLE);
        findViewById(R.id.matchsound).setVisibility(View.VISIBLE);
        findViewById(R.id.stop).setVisibility(View.VISIBLE);

    }

    public void StopAllTimers(){

        if(ShowRedTimer!=null)
            ShowRedTimer.cancel();

        if(ClearRedTimer!=null)
            ClearRedTimer.cancel();

    }

    public void SetInitUI(){
        findViewById(R.id.settings).setVisibility(View.VISIBLE);
        findViewById(R.id.achievements).setVisibility(View.VISIBLE);
        findViewById(R.id.start).setVisibility(View.VISIBLE);
        findViewById(R.id.info).setVisibility(View.VISIBLE);
        findViewById(R.id.matchpic).setVisibility(View.GONE);
        findViewById(R.id.matchsound).setVisibility(View.GONE);
        findViewById(R.id.stop).setVisibility(View.GONE);
    }

    public void Stop(View arg0){
        Toast.makeText(MainActivity.this, "Stop!!!!", Toast.LENGTH_SHORT).show();

        SetInitUI();
        StopAllTimers();

        InitAll();
    }

    public void MatchSound(View arg0) {
        if( GetFromSoundList(nBack - 1) == sound_id){
            Toast.makeText(MainActivity.this, "CORRECT SOUND!!!!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "WRONG SOUND!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void MatchPic(View arg0) {
        if( GetFromVisualList(nBack - 1) == random_nbr){
            Toast.makeText(MainActivity.this, "CORRECT PICTURE!!!!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "WRONG PICTURE!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Info(View arg0) {
        Toast.makeText(MainActivity.this, "Info clicked!!!!", Toast.LENGTH_SHORT).show();
    }

    public void Settings(View arg0) {
        Toast.makeText(MainActivity.this, "Settings clicked!!!!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onShowAchievementsRequested(View arg0){
        Toast.makeText(MainActivity.this, "onShowAchievementsRequested clicked!!!!", Toast.LENGTH_SHORT).show();
    }

    public void turnOffScreen(){
        // turn off screen
        if(ENABLE_LOGS) Log.d ("Pete", "turnOffScreen...");
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void ShowRedTimer(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In ShowRedTimer...");

        ShowRedTimerRunning = true;

        ShowRedTimer = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "ShowRedTimer onFinish...");
                ShowRedTimerRunning = false;

                addItemToVisualList(random_nbr);
                addItemToSoundList(sound_id);

                Random random = new Random();
                random_nbr = random.nextInt(9);

                if(ENABLE_LOGS) Log.v("Pete", "random_nbr: " + random_nbr);

                mImageViews[random_nbr].clearColorFilter();

                PlaySound();

                ClearRedTimer(2000);
            }
        }.start();
    }

    public void ClearRedTimer(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In ClearRedTimer...");
        ClearRedTimerRunning =  true;

        ClearRedTimer = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "ClearRedTimer onFinish...");
                ClearRedTimerRunning = false;

                int color = Color.parseColor("#FFFFFF");
                mImageViews[random_nbr].setColorFilter(color);

                ShowRedTimer(2000);

                //if(ENABLE_LOGS) Log.v("Pete", "GetFromList: " + GetFromList(nBack-1));

            }
        }.start();
    }


    private void addItemToVisualList(int value){
        if(myVisualList.size()<10){
            myVisualList.add(0,value);
        }else{
            myVisualList.add(0,value);
            myVisualList.remove(10);
        }
    }

    private int GetFromVisualList(int index){
        int returnvalue;

        if(myVisualList.size()<nBack)
            return 10;

        returnvalue = (int) myVisualList.get(index);

        if(ENABLE_LOGS) Log.v("Pete", "GetFromList: " + returnvalue);

        return returnvalue;
    }

    private void addItemToSoundList(int value){
        if(mySoundList.size()<10){
            mySoundList.add(0,value);
        }else{
            mySoundList.add(0,value);
            mySoundList.remove(10);
        }
    }

    private int GetFromSoundList(int index){
        int returnvalue;

        if(mySoundList.size()<nBack)
            return 10;

        returnvalue = (int) mySoundList.get(index);

        if(ENABLE_LOGS) Log.v("Pete", "GetFromList: " + returnvalue);

        return returnvalue;
    }

    public void PlaySound() {

        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "in PlaySound....");

        Random random_sound = new Random();

        int nbr_of_pictures = Sounds.FI_ABC_IDS.length;
        int random_range = nbr_of_pictures - 1;
        sound_id = random_sound.nextInt(random_range - 1 + 1) + 1;

        try {
            mp = MediaPlayer.create(this, Sounds.FI_ABC_IDS[sound_id]);
            mp.start();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR: audio player not working.", Toast.LENGTH_LONG).show();
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ERROR: audio player not working.");
        }

    }

    public void InitAll(){

        int color = Color.parseColor("#FFFFFF");
        for(int l=0; l<mImageViews.length; l++) {

            if(mImageViews[l] != null)
                mImageViews[l].setColorFilter(color);
        }

        myVisualList.clear();
        mySoundList.clear();
    }

    public void ReadPreferences(){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String playername = SP.getString("playername", "NA");
        boolean answersounds = SP.getBoolean("answersounds", false);
        boolean manualmode = SP.getBoolean("manualmode",false);
        String areasize = SP.getString("areasize","2");

        if(ENABLE_LOGS) Log.v("Pete", "playername: " + playername);
        if(ENABLE_LOGS) Log.v("Pete", "answersounds: " + answersounds);
        if(ENABLE_LOGS) Log.v("Pete", "manualmode: " + manualmode);
        if(ENABLE_LOGS) Log.v("Pete", "areasize: " + areasize);

    }
}
