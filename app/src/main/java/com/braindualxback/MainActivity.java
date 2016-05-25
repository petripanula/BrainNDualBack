package com.braindualxback;

import android.app.ActionBar;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.sqrt;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Application;

public class MainActivity extends BaseGameActivity implements NumberPicker.OnValueChangeListener,RateMeMaybe.OnRMMUserChoiceListener {

    //TODO for release builds set to ENABLE_LOGS = false
    public static final boolean ENABLE_LOGS = true;
    public static final boolean ENABLE2_LOGS = false;
    public static final String TAG = "Pete";

    /*****DATABASE TESTIGN********/
    private static final String DATABASE_NAME = "game";
    private static final String TABLE_SCORE = "score";
    private static final String KEY_ID_SCORE = "_id";
    private static final String KEY_SCORE = "score_value";
    private static final String PLAYER_NAME = "player_name";
    private static final String DATE = "date";
    /********DATABASE TESTIGN********/


    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            //mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker = analytics.newTracker(R.xml.analytics);
        }
        return mTracker;
    }
    //private EasyTracker easyTracker = null;

    private static MediaPlayer mp;
    PopupWindow popupWindow;
    String PopUpmessage="NA";
    String playername;

    CountDownTimer ShowRedTimer;
    CountDownTimer ClearRedTimer;
    ImageView imageView;
    Boolean ShowRedTimerRunning = false;
    Boolean ClearRedTimerRunning = false;
    int random_nbr = -1;
    int sound_id = -1;
    int nBack = 2;
    int NbrOfPictures;
    int NbrOfPictures_old;
    public static final int PictureSteps = 30;
    int NumberOfPicturesToShow = PictureSteps;
    boolean ClickedPic = false;
    boolean ClickedSound = false;
    int CorrectPicClicked = 0;
    int CorrectSoundClicked = 0;
    int WrongPicClicked = 0;
    int WrongSoundClicked = 0;
    int MissedPicClick = 0;
    int MissedSoundClick = 0;

    public static final ImageView[] mImageViews = new ImageView[37];

    public static int[] NewArray;

    public static String[] Hiscores;
    public static String[] Players;
    //public static String[] Dates;
    public static long[] Dates;

    private  List myVisualList = new ArrayList();
    private  List mySoundList = new ArrayList();

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (ENABLE_LOGS) Log.d("Pete", "MainActivity onCreate...");

        int windowWidth,windowHeight,sizeofcubeside,HeightOfGridArea=0;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //easyTracker = EasyTracker.getInstance(MainActivity.this);
        ///easyTracker.send(MapBuilder.createEvent("Application","open", "1", null).build());
        // Obtain the shared Tracker instance.
        //AnalyticsApplication application = (AnalyticsApplication) getApplication();
        //application = this.ggetApplication();
        //mTracker = application.getDefaultTracker();

        ReadPreferences();
        NbrOfPictures_old = NbrOfPictures;

        mTracker = this.getDefaultTracker();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        /*
        DatabaseHandler db = new DatabaseHandler(this);

        //Removing all enteries....
        //db.ClearDB();

        db.addScore("Pete",200);
        db.addScore("Pete",660);
        db.addScore("NA",7);

        Hiscores = new String[db.getDBsize()];
        Players = new String[db.getDBsize()];
        //Dates = new String[db.getDBsize()];
        Dates = new long[db.getDBsize()];

        Object[] arrayObjects = db.getAll();
        Players = (String[])arrayObjects[0];
        //Dates = (String[])arrayObjects[1];
        Dates = (long[])arrayObjects[1];
        Hiscores = (String[])arrayObjects[2];
        */
        //Hiscores = db.getAll();

        //if (ENABLE_LOGS) Log.d("Pete", "Players: " + Arrays.toString(Players));
        //if (ENABLE_LOGS) Log.d("Pete", "Dates: " + Arrays.toString(Dates));
        //if (ENABLE_LOGS) Log.d("Pete", "Hiscores: " + Arrays.toString(Hiscores));

        /*
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for( int i = 0; i < Dates.length - 1; i++)
        {
            Date resultdate = new Date(Dates[i]);
            if (ENABLE_LOGS) Log.d("Pete", "Converted Date: " + sdf.format(resultdate));
        }
        */

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth = size.x;
        windowHeight = size.y;

        gridview = (GridView) findViewById(R.id.gridview);
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
        if (ENABLE2_LOGS) Log.d("Pete", "NbrOfVerticalPictures: " + NbrOfVerticalPictures);
        if (ENABLE2_LOGS) Log.d("Pete", "TotalPadding: " + TotalPadding);

        int picturewidth;
        picturewidth = (windowWidth / sizeofcubeside);

        if (ENABLE2_LOGS) Log.d("Pete", "picturewidth: " + picturewidth);

        if((NbrOfVerticalPictures*picturewidth+TotalPadding)>HeightOfGridArea) {
            if (ENABLE2_LOGS) Log.d("Pete", "Pictures does't fit to Y: " + windowHeight);

            picturewidth = HeightOfGridArea/NbrOfVerticalPictures - TotalPadding;
            if (ENABLE2_LOGS) Log.d("Pete", "new picturewidth: " + picturewidth);
        }else{
            picturewidth = picturewidth - 50;
        }

        NewArray = new int[NbrOfPictures];

        for(int l=0; l<NbrOfPictures; l++) {
            NewArray[l] = Pictures.BACKGROUND_IDS[1];
        }

        gridview.setAdapter(new ImageAdapter(this, NbrOfPictures, picturewidth));

        /*
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (ENABLE_LOGS) Log.d("Pete", "onItemClick - v.getId(): " + v.getId());

                imageView = (ImageView) v;
                imageView.clearColorFilter();


            }

        });
        */
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
        InitAll();
        ReadPreferences();

        if(NbrOfPictures != NbrOfPictures_old) {

            for(int l=0; l<mImageViews.length; l++) {
                mImageViews[l] = null;
            }

            if (Build.VERSION.SDK_INT >= 11) {
                recreate();
            } else {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
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

        findViewById(R.id.horizontalview).setVisibility(View.GONE);
        findViewById(R.id.linearview).setVisibility(View.VISIBLE);
        /*
        findViewById(R.id.settings).setVisibility(View.GONE);
        findViewById(R.id.achievements).setVisibility(View.GONE);
        findViewById(R.id.start).setVisibility(View.GONE);
        findViewById(R.id.info).setVisibility(View.GONE);
        */
        /*
        findViewById(R.id.matchpic).setVisibility(View.VISIBLE);
        findViewById(R.id.matchsound).setVisibility(View.VISIBLE);
        findViewById(R.id.stop).setVisibility(View.VISIBLE);
        */
    }

    public void StopAllTimers(){

        if(ShowRedTimer!=null)
            ShowRedTimer.cancel();

        if(ClearRedTimer!=null)
            ClearRedTimer.cancel();

        if (mp != null) {
            if (ENABLE_LOGS) Log.d("Pete", "StopAllTimers() - mp.release()");
            mp.release();
            mp = null;
        }

    }

    public void SetInitUI(){
        findViewById(R.id.horizontalview).setVisibility(View.VISIBLE);
        findViewById(R.id.linearview).setVisibility(View.GONE);
        /*
        findViewById(R.id.settings).setVisibility(View.VISIBLE);
        findViewById(R.id.achievements).setVisibility(View.VISIBLE);
        findViewById(R.id.start).setVisibility(View.VISIBLE);
        findViewById(R.id.info).setVisibility(View.VISIBLE);
        findViewById(R.id.matchpic).setVisibility(View.GONE);
        findViewById(R.id.matchsound).setVisibility(View.GONE);
        findViewById(R.id.stop).setVisibility(View.GONE);
        */
    }

    public void Stop(View arg0){
        Toast.makeText(MainActivity.this, "Stop!!!!", Toast.LENGTH_SHORT).show();

        SetInitUI();
        StopAllTimers();
        InitAll();
    }

    public void MatchSound(View arg0) {

        ClickedSound = true;

        if( GetFromSoundList(nBack - 1) == sound_id){
            ShowToast("CORRECT LETTER!");
            CorrectSoundClicked+=1;
        }else{
            ShowToast("WRONG LETTER!");
            WrongSoundClicked+=1;
        }
    }

    public void MatchPic(View arg0) {

        ClickedPic = true;

        if( GetFromVisualList(nBack - 1) == random_nbr){
            ShowToast("CORRECT POSITION!");
            CorrectPicClicked+=1;
        }else{
            //Toast.makeText(MainActivity.this, "WRONG POSITION!", Toast.LENGTH_SHORT).show();
            ShowToast("WRONG POSITION!");
            WrongPicClicked+=1;
        }
    }

    public void Info(View arg0) {
        Toast.makeText(MainActivity.this, "Info clicked!!!!", Toast.LENGTH_SHORT).show();
    }

    public void Settings(View arg0) {
        Toast.makeText(MainActivity.this, "Settings clicked!!!!", Toast.LENGTH_SHORT).show();
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Settings").build());

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onShowAchievementsRequested(View arg0){
        Toast.makeText(MainActivity.this, "onShowAchievementsRequested clicked!!!!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void turnOffScreen(){
        // turn off screen
        if(ENABLE_LOGS) Log.d ("Pete", "turnOffScreen...");
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void ShowRedTimer(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In ShowRedTimer...");

        NumberOfPicturesToShow-=1;
        if(ENABLE_LOGS) Log.v("Pete", "NumberOfPicturesToShow: " + NumberOfPicturesToShow);

        if(NumberOfPicturesToShow<=0){
            if(ENABLE_LOGS) Log.v("Pete", "In ShowRedTimer - we should stop...");
            PopUpmessage="CorrectPicClicked: " + CorrectPicClicked + " CorrectSoundClicked: " + CorrectSoundClicked + " WrongPicClicked: " + WrongPicClicked + " WrongSoundClicked: " + WrongSoundClicked + " MissedPicClick: " + MissedPicClick + " MissedSoundClick: " + MissedSoundClick;

            int ResultPercent = (int)((double)(CorrectPicClicked + CorrectSoundClicked) / (double)(CorrectPicClicked + CorrectSoundClicked + WrongPicClicked + WrongSoundClicked + MissedPicClick + MissedSoundClick) * 100);

            DatabaseHandler db = new DatabaseHandler(this);
            db.addScore(playername,ResultPercent,nBack,NbrOfPictures);

            ShowPopUp_OK(true);
            SetInitUI();
            InitAll();
            return;
        }

        ShowRedTimerRunning = true;

        ShowRedTimer = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "ShowRedTimer onFinish...");
                ShowRedTimerRunning = false;

                if( GetFromSoundList(nBack - 1) == sound_id && !ClickedSound){
                    ShowToast("MISSED LETTER!");
                    MissedSoundClick+=1;
                }

                if( GetFromVisualList(nBack - 1) == random_nbr && !ClickedPic){
                    ShowToast("MISSED POSITION!");
                    MissedPicClick+=1;
                }

                ClickedPic = false;
                ClickedSound = false;

                addItemToVisualList(random_nbr);
                addItemToSoundList(sound_id);

                Random random = new Random();
                random_nbr = random.nextInt(NbrOfPictures);

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

        if (mp != null) {
            if (ENABLE_LOGS) Log.d("Pete", "PlaySound() - mp.release()");
            mp.release();
            mp = null;
        }

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

        NumberOfPicturesToShow = PictureSteps;
        ClickedPic = false;
        ClickedSound = false;
        CorrectPicClicked = 0;
        CorrectSoundClicked = 0;
        WrongPicClicked = 0;
        WrongSoundClicked = 0;
        MissedPicClick = 0;
        MissedSoundClick = 0;
    }

    public void ReadPreferences(){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        playername = SP.getString("playername", "NA");
        boolean showpopup = SP.getBoolean("popup", false);
        boolean manualmode = SP.getBoolean("manualmode",false);
        String areasize = SP.getString("areasize", "2");
        String nback = SP.getString("nback","2");

        nBack = Integer.parseInt(nback);

        if(ENABLE_LOGS) Log.v("Pete", "playername: " + playername);
        if(ENABLE_LOGS) Log.v("Pete", "showpopup: " + showpopup);
        if(ENABLE_LOGS) Log.v("Pete", "manualmode: " + manualmode);
        if(ENABLE_LOGS) Log.v("Pete", "areasize: " + areasize);
        if(ENABLE_LOGS) Log.v("Pete", "nback: " + nback);
        if(ENABLE_LOGS) Log.v("Pete", "nback int: " + nBack);

        int picturesFromConfig = Integer.parseInt(areasize) * Integer.parseInt(areasize);
        NbrOfPictures = picturesFromConfig;
    }

    public void ShowPopUp_OK(final boolean callShow){
        if (ENABLE_LOGS) Log.d("Pete", "ShowPopUp_OK....");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //int windowWidth = size.x;
        //int windowHeight = size.y;

        int FontSize = 16;
        int TextBackRoundColour = 0xaa000000;
        ImageButton justfind;

        // POPUP WINDOW STARTS //
        LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup2, null);

        // final PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        //popupWindow.setWidth(windowWidth * 2 / 3);
        //popupWindow.setHeight(windowWidth*2/3);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);

        String message = "\n" + PopUpmessage + "\n\n";

        LinearLayout ll = (LinearLayout)popupView.findViewById(R.id.popup_ll);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final TextView rowTextView0 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView0.setText("\n");
        rowTextView0.setGravity(Gravity.CENTER);
        rowTextView0.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView0.setTypeface(rowTextView0.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView0,params);

        final TextView rowTextView = new TextView(this);
        //rowTextView.setText(message);
        rowTextView.setText(" Correct positions: " + CorrectPicClicked + " ");
        rowTextView.setGravity(Gravity.CENTER);
        rowTextView.setTextColor(Color.WHITE);
        rowTextView.setBackgroundColor(TextBackRoundColour);
        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView.setTypeface(rowTextView.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView,params);

        final TextView rowTextView2 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView2.setText(" Correct letters: " + CorrectSoundClicked + " ");
        rowTextView2.setGravity(Gravity.CENTER);
        rowTextView2.setTextColor(Color.WHITE);
        rowTextView2.setBackgroundColor(TextBackRoundColour);
        rowTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView2.setTypeface(rowTextView2.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView2,params);

        final TextView rowTextView3 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView3.setText(" Wrong positions: " + WrongPicClicked + " ");
        rowTextView3.setGravity(Gravity.CENTER);
        rowTextView3.setTextColor(Color.WHITE);
        rowTextView3.setBackgroundColor(TextBackRoundColour);
        rowTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView3.setTypeface(rowTextView3.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView3,params);

        final TextView rowTextView4 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView4.setText(" Wrong letters: " + WrongSoundClicked + " ");
        rowTextView4.setGravity(Gravity.CENTER);
        rowTextView4.setTextColor(Color.WHITE);
        rowTextView4.setBackgroundColor(TextBackRoundColour);
        rowTextView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView4.setTypeface(rowTextView4.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView4,params);

        final TextView rowTextView5 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView5.setText(" Missed positions: " + MissedPicClick + " ");
        rowTextView5.setGravity(Gravity.CENTER);
        rowTextView5.setTextColor(Color.WHITE);
        rowTextView5.setBackgroundColor(TextBackRoundColour);
        rowTextView5.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView5.setTypeface(rowTextView5.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView5,params);

        final TextView rowTextView6 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView6.setText(" Missed letters: " + MissedSoundClick + " ");
        rowTextView6.setGravity(Gravity.CENTER);
        rowTextView6.setTextColor(Color.WHITE);
        rowTextView6.setBackgroundColor(TextBackRoundColour);
        //rowTextView6.setShadowLayer((float)0.5, 0, 0, Color.BLACK);
        rowTextView6.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView6.setTypeface(rowTextView6.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView6,params);

        final TextView rowTextView7 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView7.setText("\n");
        rowTextView7.setGravity(Gravity.CENTER);
        //rowTextView6.setShadowLayer((float)0.5, 0, 0, Color.BLACK);
        rowTextView7.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView7.setTypeface(rowTextView7.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView7,params);

        int ResultPercent = (int)((double)(CorrectPicClicked + CorrectSoundClicked) / (double)(CorrectPicClicked + CorrectSoundClicked + WrongPicClicked + WrongSoundClicked + MissedPicClick + MissedSoundClick) * 100);

        final TextView rowTextView8 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView8.setText(" Result %: " + ResultPercent + " ");
        rowTextView8.setGravity(Gravity.CENTER);
        rowTextView8.setTextColor(Color.WHITE);
        rowTextView8.setBackgroundColor(TextBackRoundColour);
        //rowTextView6.setShadowLayer((float)0.5, 0, 0, Color.BLACK);
        rowTextView8.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView8.setTypeface(rowTextView7.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView8,params);

        final TextView lastRowTextView = new TextView(this);
        //rowTextView.setText(message);
        lastRowTextView.setText("\n\n");
        lastRowTextView.setGravity(Gravity.CENTER);
        //rowTextView6.setShadowLayer((float)0.5, 0, 0, Color.BLACK);
        lastRowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        lastRowTextView.setTypeface(lastRowTextView.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(lastRowTextView,params);

        Button btnDismiss = new Button(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(5, 5, 5, 20);
        btnDismiss.setLayoutParams(params2);
        btnDismiss.setText("OK");
        btnDismiss.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        btnDismiss.setBackgroundResource(R.drawable.button_info_page);
        btnDismiss.setTextColor(Color.WHITE);

        ll.addView(btnDismiss, params2);

        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ENABLE_LOGS) Log.v("Pete", "ShowPopUp onClick - OK....");
                popupWindow.dismiss();

                //if(callShow)
                //    called_show();
            }
        });

        //Just find some view where we can refer....
        justfind = (ImageButton)findViewById(R.id.settings);
        popupWindow.showAtLocation(justfind, Gravity.CENTER, 0, 0);
        // POPUP WINDOW ENDS //

    }

    public void ShowToast(String mytoast){
        Toast toast= Toast.makeText(getApplicationContext(),
                mytoast, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
