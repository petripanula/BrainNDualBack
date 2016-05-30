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
import com.google.android.gms.games.GamesActivityResultCodes;
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

    public static final int PROPABILITY = 5;

    String GameLeaderBoard;
    boolean PushLeaderScore = false;
    boolean ContinuePlaying = true;
    public static int GamePoints = 0;

    int Language;
    int Soundtheme;


    String[][][] nBackAchievementID = new String[nBacks][Areas][Levels];
    public static boolean[][][] mnBackAchievement = new boolean[nBacks][Areas][Levels];

    public static int[] increaseNback = new int[nBacks];
    public static int[] PlayResult = new int[nBacks];

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

    private static MediaPlayer mp;
    private static MediaPlayer mp_click;

    PopupWindow popupWindow;
    String PopUpmessage="NA";
    String playername;
    Boolean EnableClickSounds;
    Boolean showpopup;

    boolean manualmode;
    CountDownTimer ShowRedTimer;
    CountDownTimer ClearRedTimer;
    CountDownTimer ContinuePlayingTimerT;
    Boolean ShowRedTimerRunning = false;
    Boolean ClearRedTimerRunning = false;
    Boolean ContinuePlayingTimer = false;

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

    Random random = new Random();

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

        SetStringsArrays();
        ReadPreferences();
        NbrOfPictures_old = NbrOfPictures;

        saveLocal();
        loadLocal();

        mTracker = this.getDefaultTracker();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth = size.x;
        windowHeight = size.y;

        gridview = (GridView) findViewById(R.id.gridview);
        sizeofcubeside = (int)sqrt(NbrOfPictures);
        HeightOfGridArea = windowHeight*6/7;

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

        GamePoints = 0;

        if(!manualmode){
            InitPlayModeParam();
            nBack = 1;
            NbrOfPictures = 9;

            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = SP.edit();
            editor.putString("nback", "1");
            editor.putString("areasize", "3");
            editor.apply();
        }

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

        View b_out = findViewById(R.id.signin);
        b_out.setVisibility(View.GONE);
    }

    @Override
    public void onSignInFailed() {
        if(ENABLE_LOGS) Log.v("Pete", "In onSignInFailed...");

        View b_out = findViewById(R.id.signin);
        b_out.setVisibility(View.VISIBLE);

    }

    public void Start(View arg0){

        ShowRedTimer(5000);

        findViewById(R.id.horizontalview).setVisibility(View.GONE);
        findViewById(R.id.linearview).setVisibility(View.VISIBLE);
        findViewById(R.id.undergrid).setVisibility(View.VISIBLE);


        GamePoints = 0;

        if(!manualmode){
            InitPlayModeParam();
            ShowToastnBack();
        }
    }

    public void StopAllTimers(){

        if(ShowRedTimer!=null)
            ShowRedTimer.cancel();

        if(ClearRedTimer!=null)
            ClearRedTimer.cancel();

        if(ContinuePlayingTimerT!=null)
            ContinuePlayingTimerT.cancel();

        if (mp != null) {
            if (ENABLE_LOGS) Log.d("Pete", "StopAllTimers() - mp.release()");
            mp.release();
            mp = null;
        }

        if (mp_click != null) {
            if (ENABLE_LOGS) Log.d("Pete", "StopAllTimers() - mp_click.release()");
            mp_click.release();
            mp_click = null;
        }

    }

    public void SetInitUI(){
        findViewById(R.id.horizontalview).setVisibility(View.VISIBLE);
        findViewById(R.id.linearview).setVisibility(View.GONE);
        findViewById(R.id.undergrid).setVisibility(View.GONE);

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
        if(ENABLE_LOGS) Log.v("Pete", "Stop clicked...");

        findViewById(R.id.undergrid).setVisibility(View.GONE);

        //PrintLists();

        SetInitUI();
        StopAllTimers();
        InitAll();

        InitPlayModeParam();

        //PrintLists();
    }

    public void Restart(View arg0){
        if(ENABLE_LOGS) Log.v("Pete", "Restart clicked...");

        for(int l=0; l<mImageViews.length; l++) {
            mImageViews[l] = null;
        }

        SetInitUI();
        StopAllTimers();
        InitAll();
        InitPlayModeParam();

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
        PlayClick();

        if(ClickedSound)
            return;

        ClickedSound = true;

        if(GetFromSoundList(nBack - 1) == sound_id){
            ShowToast("CORRECT LETTER!");
            CorrectSoundClicked+=1;
            UpdatePlayerPoint(10);
        }else{
            ShowToast("WRONG LETTER!");
            WrongSoundClicked+=1;
            UpdatePlayerPoint(-10);
        }
    }

    public void MatchPic(View arg0) {
        PlayClick();

        if(ClickedPic)
            return;

        ClickedPic = true;

        if(GetFromVisualList(nBack - 1) == random_nbr){
            ShowToast("CORRECT POSITION!");
            CorrectPicClicked+=1;
            UpdatePlayerPoint(10);
        }else{
            ShowToast("WRONG POSITION!");
            WrongPicClicked+=1;
            UpdatePlayerPoint(-10);
        }
    }

    public void Info(View arg0) {
        if(ENABLE_LOGS) Log.v("Pete", "Info clicked...");

        //GamePoints = 110;
        //PushLeaderScore = true;
        //pushAccomplishments();
        //DatabaseHandler db = new DatabaseHandler(this);
        //db.addScore_game(playername,GamePoints++);
        //SetStringsArrays();
        //Games.Achievements.unlock(mHelper.getApiClient(), nBackAchievementID[0][0]);
        //Games.Achievements.increment(mHelper.getApiClient(), nBackAchievementID[0][1],1);
    }

    public void Settings(View arg0) {
        if(ENABLE_LOGS) Log.v("Pete", "Settings clicked...");
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Settings").build());

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onShowLocalHiScores(View arg0){
        if(ENABLE_LOGS) Log.v("Pete", "onShowLocalHiScores clicked...");

        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onShowAchievementsRequested(View arg0){
        Toast.makeText(MainActivity.this, "onShowAchievementsRequested clicked!!!!", Toast.LENGTH_SHORT).show();

        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
            View b_out = findViewById(R.id.signin);
            b_out.setVisibility(View.VISIBLE);
        }

    }

    //@Override
    public void onSignInButtonClicked(View view) {
        if(ENABLE_LOGS) Log.v("Pete", "In onSignInButtonClicked...");
        // start the sign-in flow
        beginUserInitiatedSignIn();
    }

    //@Override
    public void onSignOutButtonClicked(View view) {
        if(ENABLE_LOGS) Log.v("Pete", "In onSignOutButtonClicked...");

        if(isSignedIn()){
            if(ENABLE_LOGS) Log.v("Pete", "lets sign out...");
            signOut();
        }
        /*
        View b_out = findViewById(R.id.button_sign_out);
        b_out.setVisibility(View.GONE);

        View b_in = findViewById(R.id.button_sign_in);
        b_in.setVisibility(View.VISIBLE);
        */
    }

    public void onShowGlobalHiScores(View arg0){
        if(ENABLE_LOGS) Log.v("Pete", "onShowGlobalHiScores clicked...");

        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),RC_UNUSED);

        } else {
            showAlert(getString(R.string.leaderboards_not_available));
            View b_out = findViewById(R.id.signin);
            b_out.setVisibility(View.VISIBLE);
        }
    }

    public void turnOffScreen(){
        // turn off screen
        if(ENABLE_LOGS) Log.d ("Pete", "turnOffScreen...");
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void ShowRedTimer(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In ShowRedTimer...");

        int FontSize = 20;
        TextHeader = (TextView)findViewById(R.id.undergrid);
        TextHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, FontSize);
        TextHeader.setTypeface(TextHeader.getTypeface(), Typeface.BOLD);
        TextHeader.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextHeader.setTextColor(Color.WHITE);
        String message = "" + NumberOfPicturesToShow;
        TextHeader.setText(message);

        NumberOfPicturesToShow-=1;

        if(ENABLE_LOGS) Log.v("Pete", "NumberOfPicturesToShow: " + NumberOfPicturesToShow);

        if(NumberOfPicturesToShow<=0){
            if(ENABLE_LOGS) Log.v("Pete", "In ShowRedTimer - we should stop...");
            PopUpmessage="CorrectPicClicked: " + CorrectPicClicked + " CorrectSoundClicked: " + CorrectSoundClicked + " WrongPicClicked: " + WrongPicClicked + " WrongSoundClicked: " + WrongSoundClicked + " MissedPicClick: " + MissedPicClick + " MissedSoundClick: " + MissedSoundClick;

            int ResultPercent = (int)((double)(CorrectPicClicked + CorrectSoundClicked) / (double)(CorrectPicClicked + CorrectSoundClicked + WrongPicClicked + WrongSoundClicked + MissedPicClick + MissedSoundClick) * 100);

            DatabaseHandler db = new DatabaseHandler(this);
            db.addScore(playername,ResultPercent,nBack,NbrOfPictures);

            if(ResultPercent >= 50) {
                SetAchievement(nBack, areasizeInt, _50PERCENT);
            }
            if(ResultPercent >= 75) {
                PlayApplause();
                SetAchievement(nBack, areasizeInt, _75PERCENT);
            }
            if(ResultPercent == 100) {
                PlayFanfare();
                SetAchievement(nBack, areasizeInt, _100PERCENT);
            }

            if(!manualmode) {
                increaseNback[nBack]++;

                if (ResultPercent>=50){
                    PlayResult[nBack]++;
                }
            }

            ShowPopUp_OK(true);
            SetInitUI();
            InitAll();

            if(!manualmode) {
                //Played level only once...
                if (increaseNback[nBack] == 1) {
                    if (PlayResult[nBack] == 1) {
                        findViewById(R.id.undergrid).setVisibility(View.VISIBLE);
                    } else {
                        if(ENABLE_LOGS) Log.v("Pete", "Game over.....");
                        //DatabaseHandler db = new DatabaseHandler(this);
                        db.addScore_game(playername,GamePoints);
                        PushLeaderScore = true;
                        pushAccomplishments();
                    }
                }
                if (increaseNback[nBack] == 2) {
                    if (PlayResult[nBack] == 2) {
                        findViewById(R.id.undergrid).setVisibility(View.VISIBLE);
                    } else {
                        if(ENABLE_LOGS) Log.v("Pete", "Game over.....");
                        db.addScore_game(playername,GamePoints);
                        PushLeaderScore = true;
                        pushAccomplishments();
                    }
                }
            }

            return;
        }

        ShowRedTimerRunning = true;

        ShowRedTimer = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "ShowRedTimer onFinish...");
                ShowRedTimerRunning = false;

                if(GetFromSoundList(nBack - 1) == sound_id && !ClickedSound){
                    ShowToast("MISSED LETTER!");
                    MissedSoundClick+=1;
                    UpdatePlayerPoint(-10);
                }

                if(GetFromVisualList(nBack - 1) == random_nbr && !ClickedPic){
                    ShowToast("MISSED POSITION!");
                    MissedPicClick+=1;
                    UpdatePlayerPoint(-10);
                }

                ClickedPic = false;
                ClickedSound = false;

                addItemToVisualList(random_nbr);
                addItemToSoundList(sound_id);

                //Random random2 = new Random();
                int random2_nbr = random.nextInt(PROPABILITY);

                if(ENABLE_LOGS) Log.v("Pete", "random2_nbr: " + random2_nbr);

                if(PictureSteps - NumberOfPicturesToShow >  nBack && random2_nbr == 3) {
                    //if (ENABLE_LOGS) Log.v("Pete", "GetFromVisualList(nBack): " + GetFromVisualList(nBack));
                    random_nbr = GetFromVisualList(nBack-1);
                }else {

                    //Random random = new Random();
                    random_nbr = random.nextInt(NbrOfPictures);
                }

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

    public void ContinuePlayingTimer(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In ContinuePlayingTimer...");
        ContinuePlayingTimer =  true;

        ContinuePlayingTimerT = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "ContinuePlayingTimer onFinish...");
                ContinuePlayingTimer = false;

                SetInitUI();

                ShowRedTimer(5000);
                findViewById(R.id.horizontalview).setVisibility(View.GONE);
                findViewById(R.id.linearview).setVisibility(View.VISIBLE);

                ShowToastnBack();
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
            return -99;

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
            return -99;

        returnvalue = (int) mySoundList.get(index);

        if(ENABLE_LOGS) Log.v("Pete", "GetFromList: " + returnvalue);

        return returnvalue;
    }

    public void PrintLists(){
        if(ENABLE_LOGS) Log.v("Pete", "In PrintLists...");
        for(int l=0; l<mySoundList.size(); l++) {
            if(ENABLE_LOGS) Log.v("Pete", "mySoundList: " + mySoundList.get(l));
        }
        for(int l=0; l<mySoundList.size(); l++) {
            if(ENABLE_LOGS) Log.v("Pete", "myVisualList: " + myVisualList.get(l));
        }
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
        int nbr_of_pictures = 0;
        //Random random_sound = new Random();

        if(Language==1){
            if(Soundtheme==1)
                nbr_of_pictures = Sounds.EN_ABC_IDS.length;
            else
                nbr_of_pictures = Sounds.EN_NBR_IDS.length;
        }else {
            if(Soundtheme==1)
                nbr_of_pictures = Sounds.FI_ABC_IDS.length;
            else
                nbr_of_pictures = Sounds.FI_NBR_IDS.length;
        }

        //Random random3 = new Random();
        int random3_nbr = random.nextInt(PROPABILITY);

        if(ENABLE_LOGS) Log.v("Pete", "random3_nbr: " + random3_nbr);

        if(PictureSteps - NumberOfPicturesToShow >  nBack && random3_nbr == 3) {
            //if (ENABLE_LOGS) Log.v("Pete", "GetFromVisualList(nBack): " + GetFromVisualList(nBack));
            sound_id = GetFromSoundList(nBack-1);
        }else {

            int random_range = nbr_of_pictures - 1;
            sound_id = random.nextInt(random_range - 1 + 1) + 1;
            //Random random = new Random();
            //random_nbr = random.nextInt(NbrOfPictures);
        }

        //int random_range = nbr_of_pictures - 1;
        //sound_id = random_sound.nextInt(random_range - 1 + 1) + 1;

        if (mp != null) {
            if (ENABLE_LOGS) Log.d("Pete", "PlaySound() - mp.release()");
            mp.release();
            mp = null;
        }

        try {
            if(Language==1) {
                if(Soundtheme==1)
                    mp = MediaPlayer.create(this, Sounds.EN_ABC_IDS[sound_id]);
                else
                    mp = MediaPlayer.create(this, Sounds.EN_NBR_IDS[sound_id]);
            }else{
                if(Soundtheme==1)
                    mp = MediaPlayer.create(this, Sounds.FI_ABC_IDS[sound_id]);
                else
                    mp = MediaPlayer.create(this, Sounds.FI_NBR_IDS[sound_id]);
            }

            mp.start();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR: audio player not working.", Toast.LENGTH_LONG).show();
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ERROR: audio player not working.");
        }

    }

    public void InitAll(){

        int color = Color.parseColor("#FFFFFF");

        for (ImageView mImageView : mImageViews) {

            if (mImageView != null)
                mImageView.setColorFilter(color);
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
        random_nbr = -1;
        sound_id = -1;
    }

    public void ReadPreferences(){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        playername = SP.getString("playername", "NA");
        showpopup = SP.getBoolean("popup", false);
        manualmode = SP.getBoolean("manualmode",false);
        String areasize = SP.getString("areasize", "2");
        String nback = SP.getString("nback","2");
        EnableClickSounds = SP.getBoolean("clicksounds", false);
        String language = SP.getString("language", "1");
        String soundtheme = SP.getString("soundtheme","1");

        Language = Integer.parseInt(language);
        Soundtheme = Integer.parseInt(soundtheme);
        nBack = Integer.parseInt(nback);

        if(ENABLE_LOGS) Log.v("Pete", "playername: " + playername);
        if(ENABLE_LOGS) Log.v("Pete", "showpopup: " + showpopup);
        if(ENABLE_LOGS) Log.v("Pete", "manualmode: " + manualmode);
        if(ENABLE_LOGS) Log.v("Pete", "areasize: " + areasize);
        if(ENABLE_LOGS) Log.v("Pete", "nback: " + nback);
        if(ENABLE_LOGS) Log.v("Pete", "nback int: " + nBack);
        if(ENABLE_LOGS) Log.v("Pete", "Language int: " + Language);
        if(ENABLE_LOGS) Log.v("Pete", "Soundtheme int: " + Soundtheme);

        areasizeInt = Integer.parseInt(areasize);
        NbrOfPictures = Integer.parseInt(areasize) * Integer.parseInt(areasize);
    }

    public void ShowPopUp_OK(final boolean callShow){
        if (ENABLE_LOGS) Log.d("Pete", "ShowPopUp_OK....");

        ContinuePlaying = false;

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

        String message;

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

        message = "\n\n";

        final TextView lastRowTextView = new TextView(this);
        //rowTextView.setText(message);
        lastRowTextView.setText(message);
        lastRowTextView.setGravity(Gravity.CENTER);
        //rowTextView6.setShadowLayer((float)0.5, 0, 0, Color.BLACK);
        lastRowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        lastRowTextView.setTypeface(lastRowTextView.getTypeface(), Typeface.BOLD);
        // add the textview to the linearlayout
        ll.addView(lastRowTextView,params);

        if(!manualmode) {
            if (increaseNback[nBack] == 1) {
                if (PlayResult[nBack] == 1) {
                    message = " Nice Work - continue playing! Points: " + GamePoints + " ";
                    ContinuePlaying = true;
                } else {
                    message = " Game over! Points: " + GamePoints + " ";
                }
            }
            if (increaseNback[nBack] == 2) {
                if (PlayResult[nBack] == 2) {
                    message = " Nice Work - nBack will be increased! Points: " + GamePoints + " ";
                    nBack++;
                    ContinuePlaying = true;
                } else {
                    message = " Game over now! Points: " + GamePoints + " ";
                }
            }

            final TextView last2RowTextView = new TextView(this);
            //rowTextView.setText(message);
            last2RowTextView.setText(message);
            last2RowTextView.setGravity(Gravity.CENTER);
            last2RowTextView.setTextColor(Color.WHITE);
            last2RowTextView.setBackgroundColor(TextBackRoundColour);
            //rowTextView6.setShadowLayer((float)0.5, 0, 0, Color.BLACK);
            last2RowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            last2RowTextView.setTypeface(last2RowTextView.getTypeface(), Typeface.BOLD);
            // add the textview to the linearlayout
            ll.addView(last2RowTextView, params);

            final TextView last3RowTextView = new TextView(this);
            //rowTextView.setText(message);
            last3RowTextView.setText("\n");
            last3RowTextView.setGravity(Gravity.CENTER);
            //rowTextView6.setShadowLayer((float)0.5, 0, 0, Color.BLACK);
            last3RowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            last3RowTextView.setTypeface(last2RowTextView.getTypeface(), Typeface.BOLD);
            // add the textview to the linearlayout
            ll.addView(last3RowTextView, params);

        }

        message = "OK";

        Button btnDismiss = new Button(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(5, 5, 5, 20);
        btnDismiss.setLayoutParams(params2);
        btnDismiss.setText(message);
        btnDismiss.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        btnDismiss.setBackgroundResource(R.drawable.button_info_page);
        btnDismiss.setTextColor(Color.WHITE);

        ll.addView(btnDismiss, params2);

        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ENABLE_LOGS) Log.v("Pete", "ShowPopUp onClick - OK....");
                popupWindow.dismiss();

                if(ContinuePlaying && !manualmode){
                    ContinuePlayingTimer(10);
                }
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

        if(!showpopup)
            return;

        Toast toast= Toast.makeText(getApplicationContext(),
                mytoast, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void ShowToastnBack(){
        String mytoast = "nBack used now: " + nBack;
        Toast toast= Toast.makeText(getApplicationContext(),
                mytoast, Toast.LENGTH_LONG);
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
    public void SetAchievement(int nBack, int Level, int procent) {
        if(ENABLE_LOGS) Log.v("Pete", "SetAchievement: " +  " Level: " + Level + " nBack: " + nBack + " procent: " + procent);

        mnBackAchievement[nBack-1][Level-2][procent] = true;

        pushAccomplishments();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(ENABLE_LOGS) Log.v("Pete","onActivityResult: " + requestCode);

        if (requestCode == 5001
                && resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED) {
                 if(ENABLE_LOGS) Log.v("Pete","Disconnection from Play Services called from activity with code: " + requestCode);
                 mHelper.disconnect();
        } else {
            mHelper.onActivityResult(requestCode, resultCode, intent);
        }
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
            Games.Leaderboards.submitScore(mHelper.getApiClient(), GameLeaderBoard, GamePoints);
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

        nBackAchievementID[3][0][0] = getString(R.string.achievement_nback_4_area_2x2_result_50);
        nBackAchievementID[3][0][1] = getString(R.string.achievement_nback_4_area_2x2_result_75);
        nBackAchievementID[3][0][2] = getString(R.string.achievement_nback_4_area_2x2_result_100);
        nBackAchievementID[3][1][0] = getString(R.string.achievement_nback_4_area_3x3_result_50);
        nBackAchievementID[3][1][1] = getString(R.string.achievement_nback_4_area_3x3_result_75);
        nBackAchievementID[3][1][2] = getString(R.string.achievement_nback_4_area_3x3_result_100);
        nBackAchievementID[3][2][0] = getString(R.string.achievement_nback_4_area_4x4_result_50);
        nBackAchievementID[3][2][1] = getString(R.string.achievement_nback_4_area_4x4_result_75);
        nBackAchievementID[3][2][2] = getString(R.string.achievement_nback_4_area_4x4_result_100);
        nBackAchievementID[3][3][0] = getString(R.string.achievement_nback_4_area_5x5_result_50);
        nBackAchievementID[3][3][1] = getString(R.string.achievement_nback_4_area_5x5_result_75);
        nBackAchievementID[3][3][2] = getString(R.string.achievement_nback_4_area_5x5_result_100);

        nBackAchievementID[4][0][0] = getString(R.string.achievement_nback_5_area_2x2_result_50);
        nBackAchievementID[4][0][1] = getString(R.string.achievement_nback_5_area_2x2_result_75);
        nBackAchievementID[4][0][2] = getString(R.string.achievement_nback_5_area_2x2_result_100);
        nBackAchievementID[4][1][0] = getString(R.string.achievement_nback_5_area_3x3_result_50);
        nBackAchievementID[4][1][1] = getString(R.string.achievement_nback_5_area_3x3_result_75);
        nBackAchievementID[4][1][2] = getString(R.string.achievement_nback_5_area_3x3_result_100);
        nBackAchievementID[4][2][0] = getString(R.string.achievement_nback_5_area_4x4_result_50);
        nBackAchievementID[4][2][1] = getString(R.string.achievement_nback_5_area_4x4_result_75);
        nBackAchievementID[4][2][2] = getString(R.string.achievement_nback_5_area_4x4_result_100);
        nBackAchievementID[4][3][0] = getString(R.string.achievement_nback_5_area_5x5_result_50);
        nBackAchievementID[4][3][1] = getString(R.string.achievement_nback_5_area_5x5_result_75);
        nBackAchievementID[4][3][2] = getString(R.string.achievement_nback_5_area_5x5_result_100);

        nBackAchievementID[5][0][0] = getString(R.string.achievement_nback_6_area_2x2_result_50);
        nBackAchievementID[5][0][1] = getString(R.string.achievement_nback_6_area_2x2_result_75);
        nBackAchievementID[5][0][2] = getString(R.string.achievement_nback_6_area_2x2_result_100);
        nBackAchievementID[5][1][0] = getString(R.string.achievement_nback_6_area_3x3_result_50);
        nBackAchievementID[5][1][1] = getString(R.string.achievement_nback_6_area_3x3_result_75);
        nBackAchievementID[5][1][2] = getString(R.string.achievement_nback_6_area_3x3_result_100);
        nBackAchievementID[5][2][0] = getString(R.string.achievement_nback_6_area_4x4_result_50);
        nBackAchievementID[5][2][1] = getString(R.string.achievement_nback_6_area_4x4_result_75);
        nBackAchievementID[5][2][2] = getString(R.string.achievement_nback_6_area_4x4_result_100);
        nBackAchievementID[5][3][0] = getString(R.string.achievement_nback_6_area_5x5_result_50);
        nBackAchievementID[5][3][1] = getString(R.string.achievement_nback_6_area_5x5_result_75);
        nBackAchievementID[5][3][2] = getString(R.string.achievement_nback_6_area_5x5_result_100);

        GameLeaderBoard =  getString(R.string.leaderboard_global_nback_3x3_playmode_hiscores);
    }

    public void InitPlayModeParam(){
        for(int i=0;i<nBacks;i++) {
            increaseNback[i] = 0;
            PlayResult[i] = 0;
        }
    }

    public void UpdatePlayerPoint(int points){
        GamePoints = GamePoints + points;

        if(GamePoints < 0) GamePoints = 0;

        if(ENABLE_LOGS) Log.v("Pete", "GamePoints: " + GamePoints);
    }

    public void PlayClick() {

        if(EnableClickSounds){
            if(mp_click!=null){
                mp_click.release();
                mp_click=null;
            }

            mp_click = MediaPlayer.create(this, R.raw.click);
            mp_click.start();
        }

    }

}
