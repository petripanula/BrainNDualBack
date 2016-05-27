package com.braindualxback;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.sqrt;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseGameActivity implements NumberPicker.OnValueChangeListener,RateMeMaybe.OnRMMUserChoiceListener {

    //TODO for release builds set to ENABLE_LOGS = false
    public static final boolean ENABLE_LOGS = true;
    public static final boolean ENABLE2_LOGS = false;
    public static final String TAG = "Pete";

    public static final int nBacks = 6;
    public static final int Areas = 4;
    public static final int Levels = 3;

    public static final int _50PERCENT = 0;
    public static final int _75PERCENT = 1;
    public static final int _100PERCENT = 2;

    String GameLeaderBoard;
    boolean PushLeaderScore = false;

    String[][][] nBackAchievementID = new String[nBacks][Areas][Levels];
    public static boolean[][][] mnBackAchievement = new boolean[nBacks][Areas][Levels];

    //public static boolean[][] nBackAchievementOnServer = new boolean[nBacks][Areas];
    //boolean ShowGlobalAchievemwntClicked = false;

    // request codes we use when invoking an external activity
    //final int RC_RESOLVE = 5000;
    final int RC_UNUSED = 5001;

    SecurePreferences preferences;

    int areasizeInt = 0;

    TextView TextHeader;

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

    boolean manualmode;
    CountDownTimer ShowRedTimer;
    CountDownTimer ClearRedTimer;
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

    private  List myVisualList = new ArrayList();
    private  List mySoundList = new ArrayList();

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (ENABLE_LOGS) Log.d("Pete", "MainActivity onCreate...");

        //Init all mImageViews....
        for(int l=0; l<mImageViews.length; l++) {
            mImageViews[l] = null;
        }

        int windowWidth,windowHeight,sizeofcubeside,HeightOfGridArea;

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

        SetStringsArrays();
        ReadPreferences();
        NbrOfPictures_old = NbrOfPictures;

        saveLocal();
        loadLocal();

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
        //int sid = item.getItemId();


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
        ReadPreferences();
        SetInitUI();
        InitAll();

        if(NbrOfPictures != NbrOfPictures_old) {
            if(ENABLE_LOGS) Log.v("Pete", "MainActivity onResume - recreate...");

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

        Player p = Games.Players.getCurrentPlayer(getApiClient());
        String displayName;
        if (p == null) {
            if(ENABLE_LOGS) Log.v("Pete", "getCurrentPlayer() is NULL!");
            //displayName = "???";
        } else {
            displayName = p.getDisplayName();
            if (ENABLE_LOGS) Log.v("Pete", "getCurrentPlayer() is " + displayName);

            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            playername = SP.getString("playername", "NA");

            if (playername.equals("NA")) {
                playername = displayName;
                SharedPreferences.Editor editor = SP.edit();
                editor.putString("playername", displayName);
                editor.apply();

                SetInitUI();
            }
        }
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

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL){
            //TODO
            if (ENABLE_LOGS) Log.d("Pete", "SCREENLAYOUT_SIZE_NORMAL");
        }

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE){
            //TODO
            if (ENABLE_LOGS) Log.d("Pete", "SCREENLAYOUT_SIZE_LARGE");
        }

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE){
            //TODO
            if (ENABLE_LOGS) Log.d("Pete", "SCREENLAYOUT_SIZE_XLARGE");
        }

        int FontSize = 12;

        TextHeader = (TextView)findViewById(R.id.headertext);
        TextHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, FontSize);
        TextHeader.setTypeface(TextHeader.getTypeface(), Typeface.BOLD);
        TextHeader.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextHeader.setTextColor(Color.WHITE);
        String message = "Player: " + playername + "\n" + "Area: " + NbrOfPictures + " nBack: " + String.valueOf(nBack) + (manualmode ? " ManualMode" : " PlayMode");
        TextHeader.setText(message);
    }

    public void Stop(View arg0){
        Toast.makeText(MainActivity.this, "Stop!!!!", Toast.LENGTH_SHORT).show();

        SetInitUI();
        StopAllTimers();
        InitAll();
    }

    public void Restart(View arg0){
        Toast.makeText(MainActivity.this, "Restart!!!!", Toast.LENGTH_SHORT).show();

        for(int l=0; l<mImageViews.length; l++) {
            mImageViews[l] = null;
        }

        SetInitUI();
        StopAllTimers();
        InitAll();

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

        SetStringsArrays();
        //Games.Achievements.unlock(mHelper.getApiClient(), nBackAchievementID[0][0]);
        //Games.Achievements.increment(mHelper.getApiClient(), nBackAchievementID[0][1],1);
    }

    public void Settings(View arg0) {
        Toast.makeText(MainActivity.this, "Settings clicked!!!!", Toast.LENGTH_SHORT).show();
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Settings").build());

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onShowLocalHiScores(View arg0){
        Toast.makeText(MainActivity.this, "onShowLocalHiScores clicked!!!!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onShowAchievementsRequested(View arg0){
        Toast.makeText(MainActivity.this, "onShowAchievementsRequested clicked!!!!", Toast.LENGTH_SHORT).show();

        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }

    }

    public void onShowGlobalHiScores(View arg0){
        Toast.makeText(MainActivity.this, "onShowGlobalHiScores clicked!!!!", Toast.LENGTH_SHORT).show();

        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),RC_UNUSED);

            //Get just one leaderboard...
            //startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), PicLeaderScoreString[0][0]),RC_UNUSED);
        } else {
            showAlert(getString(R.string.leaderboards_not_available));
        }
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

            if(ResultPercent >= 50) {
                SetAchievement(areasizeInt, nBack, _50PERCENT);
            }
            if(ResultPercent >= 75) {
                PlayApplause();
                SetAchievement(areasizeInt, nBack, _75PERCENT);
            }
            if(ResultPercent == 100) {
                PlayFanfare();
                SetAchievement(areasizeInt, nBack, _100PERCENT);
            }

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

    public void PlayApplause(){
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "in PlayApplause....");

        if (mp != null) {
            if (ENABLE_LOGS) Log.d("Pete", "PlaySound() - mp.release()");
            mp.release();
            mp = null;
        }

        try {
            mp = MediaPlayer.create(this, Sounds.APPLAUSE[0]);
            mp.start();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR: audio player not working.", Toast.LENGTH_LONG).show();
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ERROR: audio player not working.");
        }
    }

    public void PlayFanfare(){
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "in PlayFanfare....");

        if (mp != null) {
            if (ENABLE_LOGS) Log.d("Pete", "PlaySound() - mp.release()");
            mp.release();
            mp = null;
        }

        try {
            mp = MediaPlayer.create(this, Sounds.APPLAUSE[1]);
            mp.start();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR: audio player not working.", Toast.LENGTH_LONG).show();
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ERROR: audio player not working.");
        }
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
        manualmode = SP.getBoolean("manualmode",false);
        String areasize = SP.getString("areasize", "2");
        String nback = SP.getString("nback","2");

        nBack = Integer.parseInt(nback);

        if(ENABLE_LOGS) Log.v("Pete", "playername: " + playername);
        if(ENABLE_LOGS) Log.v("Pete", "showpopup: " + showpopup);
        if(ENABLE_LOGS) Log.v("Pete", "manualmode: " + manualmode);
        if(ENABLE_LOGS) Log.v("Pete", "areasize: " + areasize);
        if(ENABLE_LOGS) Log.v("Pete", "nback: " + nback);
        if(ENABLE_LOGS) Log.v("Pete", "nback int: " + nBack);

        areasizeInt = Integer.parseInt(areasize);
        NbrOfPictures = Integer.parseInt(areasize) * Integer.parseInt(areasize);
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

        String message = "NA";

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
        message = " Correct positions: " + CorrectPicClicked + " ";
        rowTextView.setText(message);
        rowTextView.setGravity(Gravity.CENTER);
        rowTextView.setTextColor(Color.WHITE);
        rowTextView.setBackgroundColor(TextBackRoundColour);
        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView.setTypeface(rowTextView.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView,params);

        final TextView rowTextView2 = new TextView(this);
        message = " Correct letters: " + CorrectSoundClicked + " ";
        rowTextView2.setText(message);
        rowTextView2.setGravity(Gravity.CENTER);
        rowTextView2.setTextColor(Color.WHITE);
        rowTextView2.setBackgroundColor(TextBackRoundColour);
        rowTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView2.setTypeface(rowTextView2.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView2,params);

        final TextView rowTextView3 = new TextView(this);
        message = " Wrong positions: " + WrongPicClicked + " ";
        rowTextView3.setText(message);
        rowTextView3.setGravity(Gravity.CENTER);
        rowTextView3.setTextColor(Color.WHITE);
        rowTextView3.setBackgroundColor(TextBackRoundColour);
        rowTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView3.setTypeface(rowTextView3.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView3,params);

        final TextView rowTextView4 = new TextView(this);
        message = " Wrong letters: " + WrongSoundClicked + " ";
        rowTextView4.setText(message);
        rowTextView4.setGravity(Gravity.CENTER);
        rowTextView4.setTextColor(Color.WHITE);
        rowTextView4.setBackgroundColor(TextBackRoundColour);
        rowTextView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView4.setTypeface(rowTextView4.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView4,params);

        final TextView rowTextView5 = new TextView(this);
        message = " Missed positions: " + MissedPicClick + " ";
        rowTextView5.setText(message);
        rowTextView5.setGravity(Gravity.CENTER);
        rowTextView5.setTextColor(Color.WHITE);
        rowTextView5.setBackgroundColor(TextBackRoundColour);
        rowTextView5.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView5.setTypeface(rowTextView5.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(rowTextView5,params);

        final TextView rowTextView6 = new TextView(this);
        message = " Missed letters: " + MissedSoundClick + " ";
        rowTextView6.setText(message);
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
        message = " Result %: " + ResultPercent + " ";
        rowTextView8.setText(message);
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

    public void saveLocal() {
        //if(ENABLE_LOGS) Log.v("Pete", "AccomplishmentsOutbox saveLocal...");

        preferences = new SecurePreferences(this, "my-preferences", "SometopSecretKey1235", true);

        int testint = 668;
        preferences.put("testint", String.valueOf(testint));
    }

    public void loadLocal() {
        if (ENABLE_LOGS) Log.v("Pete", "AccomplishmentsOutbox loadLocal...");

        //Init
        preferences = new SecurePreferences(this, "my-preferences", "SometopSecretKey1235", true);

        int testint = Integer.parseInt(preferences.getIntString("testint"));
        if (ENABLE_LOGS) Log.v("Pete", "AccomplishmentsOutbox loadLocal - testint: " + testint);
    }

    // Input area and nBack....
    public void SetAchievement(int Level, int nBack, int procent) {
        if(ENABLE_LOGS) Log.v("Pete", "SetAchievement: " +  " Level: " + Level + " nBack: " + nBack + " procent: " + procent);

        mnBackAchievement[Level-2][nBack-1][procent] = true;
        //TODO
        //submitPicMemoryEvent(Level, Time);

        pushAccomplishments();
    }

    void pushAccomplishments() {
        if(ENABLE_LOGS) Log.v("Pete", "pushAccomplishments....");

        //Here we should push LeaderScores, Achievements and Events
        if (!isSignedIn()) {
            // can't push to the cloud, so save locally
            if(ENABLE_LOGS) Log.v("Pete", "pushAccomplishments - not isSignedIn");
            return;
        }

        //For Achievements...
        for(int i=0;i<nBacks;i++) {
            for (int j = 0; j < Areas; j++) {
                for (int k = 0; k < Levels; k++) {

                    if (mnBackAchievement[i][j][k]) {
                        if (ENABLE_LOGS) Log.v("Pete", "pushAccomplishments - not unlockImmediate - i: " + i + " j: " + j + " k " + k);

                        Games.Achievements.unlock(mHelper.getApiClient(), nBackAchievementID[i][j][k]);
                        mnBackAchievement[i][j][k] = false;
                    }
                }
            }
        }

        //For LeaderBoard

        if(PushLeaderScore) {
            Games.Leaderboards.submitScore(mHelper.getApiClient(), GameLeaderBoard, 1);
            PushLeaderScore = false;
        }

        //From new Doc.... No need for callbacks etc???
        //Games.Achievements.unlock(mHelper.getApiClient(), nBackAchievementID[0][0]);
    }

    /*
    class myPicMemoryAchievementResultCallback implements ResultCallback<Achievements.UpdateAchievementResult> {

        @Override
        public void onResult(Achievements.UpdateAchievementResult res) {
            if (res.getStatus().getStatusCode() == 0) {
                if(ENABLE_LOGS) Log.v("Pete", "PicMemoryAchievement delivered to server!!!!");

                mnBackAchievement[nBackAchievementAck_i][nBackAchievementAck_j] = false;

                //This is used for prevent future push for this.....
                nBackAchievementOnServer[nBackAchievementAck_i][nBackAchievementAck_j] = true;

                nBackAchievementSent = false;
                nBackAchievementAck_i = -1;
                nBackAchievementAck_j = -1;
            }
        }
    }
    */
    public void SetStringsArrays(){
        nBackAchievementID[0][0][0] = getString(R.string.achievement_nback_1_area_2x2_result_50);
        nBackAchievementID[0][0][1] = getString(R.string.achievement_nback_1_area_2x2_result_75);
        nBackAchievementID[0][0][2] = getString(R.string.achievement_nback_1_area_2x2_result_100);
        nBackAchievementID[0][1][0] = getString(R.string.achievement_nback_1_area_3x3_result_50);
        nBackAchievementID[0][1][1] = getString(R.string.achievement_nback_1_area_3x3_result_75);
        nBackAchievementID[0][1][2] = getString(R.string.achievement_nback_1_area_3x3_result_100);
        nBackAchievementID[0][2][0] = getString(R.string.achievement_nback_1_area_4x4_result_50);
        nBackAchievementID[0][2][1] = getString(R.string.achievement_nback_1_area_4x4_result_75);
        nBackAchievementID[0][2][2] = getString(R.string.achievement_nback_1_area_4x4_result_100);
        nBackAchievementID[0][3][0] = getString(R.string.achievement_nback_1_area_5x5_result_50);
        nBackAchievementID[0][3][1] = getString(R.string.achievement_nback_1_area_5x5_result_75);
        nBackAchievementID[0][3][2] = getString(R.string.achievement_nback_1_area_5x5_result_100);

        nBackAchievementID[1][0][0] = getString(R.string.achievement_nback_2_area_2x2_result_50);
        nBackAchievementID[1][0][1] = getString(R.string.achievement_nback_2_area_2x2_result_75);
        nBackAchievementID[1][0][2] = getString(R.string.achievement_nback_2_area_2x2_result_100);
        nBackAchievementID[1][1][0] = getString(R.string.achievement_nback_2_area_3x3_result_50);
        nBackAchievementID[1][1][1] = getString(R.string.achievement_nback_2_area_3x3_result_75);
        nBackAchievementID[1][1][2] = getString(R.string.achievement_nback_2_area_3x3_result_100);
        nBackAchievementID[1][2][0] = getString(R.string.achievement_nback_2_area_4x4_result_50);
        nBackAchievementID[1][2][1] = getString(R.string.achievement_nback_2_area_4x4_result_75);
        nBackAchievementID[1][2][2] = getString(R.string.achievement_nback_2_area_4x4_result_100);
        nBackAchievementID[1][3][0] = getString(R.string.achievement_nback_2_area_5x5_result_50);
        nBackAchievementID[1][3][1] = getString(R.string.achievement_nback_2_area_5x5_result_75);
        nBackAchievementID[1][3][2] = getString(R.string.achievement_nback_2_area_5x5_result_100);

        nBackAchievementID[2][0][0] = getString(R.string.achievement_nback_3_area_2x2_result_50);
        nBackAchievementID[2][0][1] = getString(R.string.achievement_nback_3_area_2x2_result_75);
        nBackAchievementID[2][0][2] = getString(R.string.achievement_nback_3_area_2x2_result_100);
        nBackAchievementID[2][1][0] = getString(R.string.achievement_nback_3_area_3x3_result_50);
        nBackAchievementID[2][1][1] = getString(R.string.achievement_nback_3_area_3x3_result_75);
        nBackAchievementID[2][1][2] = getString(R.string.achievement_nback_3_area_3x3_result_100);
        nBackAchievementID[2][2][0] = getString(R.string.achievement_nback_3_area_4x4_result_50);
        nBackAchievementID[2][2][1] = getString(R.string.achievement_nback_3_area_4x4_result_75);
        nBackAchievementID[2][2][2] = getString(R.string.achievement_nback_3_area_4x4_result_100);
        nBackAchievementID[2][3][0] = getString(R.string.achievement_nback_3_area_5x5_result_50);
        nBackAchievementID[2][3][1] = getString(R.string.achievement_nback_3_area_5x5_result_75);
        nBackAchievementID[2][3][2] = getString(R.string.achievement_nback_3_area_5x5_result_100);

        GameLeaderBoard =  getString(R.string.leaderboard_global_nback_3x3_playmode_hiscores);
        /*
        <string name="achievement_nback_1_area_2x2_result_50">CgkI5LrG0eIBEAIQCg</string>
        <string name="achievement_nback_1_area_2x2_result_75">CgkI5LrG0eIBEAIQCQ</string>
        <string name="achievement_nback_1_area_2x2_result_100">CgkI5LrG0eIBEAIQAw</string>
        <string name="achievement_nback_2_area_2x2_result_50">CgkI5LrG0eIBEAIQDA</string>
        <string name="achievement_nback_2_area_2x2_result_75">CgkI5LrG0eIBEAIQCw</string>
        <string name="achievement_nback_2_area_2x2_result_100">CgkI5LrG0eIBEAIQBg</string>
        <string name="achievement_nback_1_area_3x3_result_50">CgkI5LrG0eIBEAIQDQ</string>
        <string name="achievement_nback_1_area_3x3_result_75">CgkI5LrG0eIBEAIQDg</string>
        <string name="achievement_nback_1_area_3x3_result_100">CgkI5LrG0eIBEAIQDw</string>
        <string name="achievement_nback_2_area_3x3_result_50">CgkI5LrG0eIBEAIQEA</string>
        <string name="achievement_nback_2_area_3x3_result_75">CgkI5LrG0eIBEAIQEQ</string>
        <string name="achievement_nback_2_area_3x3_result_100">CgkI5LrG0eIBEAIQEg</string>
        <string name="achievement_nback_1_area_4x4_result_50">CgkI5LrG0eIBEAIQEw</string>
        <string name="achievement_nback_1_area_4x4_result_75">CgkI5LrG0eIBEAIQFA</string>
        <string name="achievement_nback_1_area_4x4_result_100">CgkI5LrG0eIBEAIQFQ</string>
        <string name="achievement_nback_2_area_4x4_result_50">CgkI5LrG0eIBEAIQFg</string>
        <string name="achievement_nback_2_area_4x4_result_75">CgkI5LrG0eIBEAIQFw</string>
        <string name="achievement_nback_2_area_4x4_result_100">CgkI5LrG0eIBEAIQGA</string>
        <string name="achievement_nback_1_area_5x5_result_50">CgkI5LrG0eIBEAIQGQ</string>
        <string name="achievement_nback_1_area_5x5_result_75">CgkI5LrG0eIBEAIQGg</string>
        <string name="achievement_nback_1_area_5x5_result_100">CgkI5LrG0eIBEAIQGw</string>
        <string name="achievement_nback_2_area_5x5_result_50">CgkI5LrG0eIBEAIQHA</string>
        <string name="achievement_nback_2_area_5x5_result_75">CgkI5LrG0eIBEAIQHQ</string>
        <string name="achievement_nback_2_area_5x5_result_100">CgkI5LrG0eIBEAIQHg</string>
        <string name="achievement_nback_3_area_2x2_result_50">CgkI5LrG0eIBEAIQHw</string>
        <string name="achievement_nback_3_area_3x3_result_50">CgkI5LrG0eIBEAIQIA</string>
        <string name="achievement_nback_3_area_4x4_result_50">CgkI5LrG0eIBEAIQIQ</string>
        <string name="achievement_nback_3_area_5x5_result_50">CgkI5LrG0eIBEAIQIg</string>
        <string name="achievement_nback_3_area_2x2_result_75">CgkI5LrG0eIBEAIQIw</string>
        <string name="achievement_nback_3_area_3x3_result_75">CgkI5LrG0eIBEAIQJA</string>
        <string name="achievement_nback_3_area_4x4_result_75">CgkI5LrG0eIBEAIQJQ</string>
        <string name="achievement_nback_3_area_5x5_result_75">CgkI5LrG0eIBEAIQJg</string>
        <string name="achievement_nback_3_area_2x2_result_100">CgkI5LrG0eIBEAIQJw</string>
        <string name="achievement_nback_3_area_3x3_result_100">CgkI5LrG0eIBEAIQKA</string>
        <string name="achievement_nback_3_area_4x4_result_100">CgkI5LrG0eIBEAIQKQ</string>
        <string name="achievement_nback_3_area_5x5_result_100">CgkI5LrG0eIBEAIQKg</string>
        <string name="achievement_nback_4_area_2x2_result_50">CgkI5LrG0eIBEAIQKw</string>
        <string name="achievement_nback_4_area_3x3_result_50">CgkI5LrG0eIBEAIQLA</string>
        <string name="achievement_nback_4_area_4x4_result_50">CgkI5LrG0eIBEAIQLQ</string>
        <string name="achievement_nback_4_area_5x5_result_50">CgkI5LrG0eIBEAIQLg</string>
        <string name="achievement_nback_4_area_2x2_result_75">CgkI5LrG0eIBEAIQLw</string>
        <string name="achievement_nback_4_area_3x3_result_75">CgkI5LrG0eIBEAIQMA</string>
        <string name="achievement_nback_4_area_4x4_result_75">CgkI5LrG0eIBEAIQMQ</string>
        <string name="achievement_nback_4_area_5x5_result_75">CgkI5LrG0eIBEAIQMg</string>
        <string name="achievement_nback_4_area_2x2_result_100">CgkI5LrG0eIBEAIQMw</string>
        <string name="achievement_nback_4_area_3x3_result_100">CgkI5LrG0eIBEAIQNA</string>
        <string name="achievement_nback_4_area_4x4_result_100">CgkI5LrG0eIBEAIQNQ</string>
        <string name="achievement_nback_4_area_5x5_result_100">CgkI5LrG0eIBEAIQNg</string>
        <string name="achievement_nback_5_area_2x2_result_50">CgkI5LrG0eIBEAIQNw</string>
        <string name="achievement_nback_5_area_3x3_result_50">CgkI5LrG0eIBEAIQOA</string>
        <string name="achievement_nback_5_area_4x4_result_50">CgkI5LrG0eIBEAIQOQ</string>
        <string name="achievement_nback_5_area_5x5_result_50">CgkI5LrG0eIBEAIQOg</string>
        <string name="achievement_nback_5_area_2x2_result_75">CgkI5LrG0eIBEAIQOw</string>
        <string name="achievement_nback_5_area_3x3_result_75">CgkI5LrG0eIBEAIQPA</string>
        <string name="achievement_nback_5_area_4x4_result_75">CgkI5LrG0eIBEAIQPQ</string>
        <string name="achievement_nback_5_area_5x5_result_75">CgkI5LrG0eIBEAIQPg</string>
        <string name="achievement_nback_5_area_2x2_result_100">CgkI5LrG0eIBEAIQPw</string>
        <string name="achievement_nback_5_area_3x3_result_100">CgkI5LrG0eIBEAIQQA</string>
        <string name="achievement_nback_5_area_4x4_result_100">CgkI5LrG0eIBEAIQQQ</string>
        <string name="achievement_nback_5_area_5x5_result_100">CgkI5LrG0eIBEAIQQg</string>
        <string name="achievement_nback_6_area_2x2_result_50">CgkI5LrG0eIBEAIQQw</string>
        <string name="achievement_nback_6_area_3x3_result_50">CgkI5LrG0eIBEAIQRA</string>
        <string name="achievement_nback_6_area_4x4_result_50">CgkI5LrG0eIBEAIQRQ</string>
        <string name="achievement_nback_6_area_5x5_result_50">CgkI5LrG0eIBEAIQRg</string>
        <string name="achievement_nback_6_area_2x2_result_75">CgkI5LrG0eIBEAIQRw</string>
        <string name="achievement_nback_6_area_3x3_result_75">CgkI5LrG0eIBEAIQSA</string>
        <string name="achievement_nback_6_area_4x4_result_75">CgkI5LrG0eIBEAIQSQ</string>
        <string name="achievement_nback_6_area_5x5_result_75">CgkI5LrG0eIBEAIQSg</string>
        <string name="achievement_nback_6_area_2x2_result_100">CgkI5LrG0eIBEAIQSw</string>
        <string name="achievement_nback_6_area_3x3_result_100">CgkI5LrG0eIBEAIQTA</string>
        <string name="achievement_nback_6_area_4x4_result_100">CgkI5LrG0eIBEAIQTQ</string>
        <string name="achievement_nback_6_area_5x5_result_100">CgkI5LrG0eIBEAIQTg</string>
        <string name="leaderboard_global_nback_3x3_playmode_hiscores">CgkI5LrG0eIBEAIQBw</string>
        <string name="event_nback_1_area_2x2_100">CgkI5LrG0eIBEAIQCA</string>
         */
    }
}
