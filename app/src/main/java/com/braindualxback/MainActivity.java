package com.braindualxback;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.braindualxback.util.IabBroadcastReceiver;
import com.braindualxback.util.IabHelper;
import com.braindualxback.util.IabHelper.IabAsyncInProgressException;
import com.braindualxback.util.IabResult;
import com.braindualxback.util.Inventory;
import com.braindualxback.util.Purchase;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.Achievements;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseGameActivity implements NumberPicker.OnValueChangeListener,RateMeMaybe.OnRMMUserChoiceListener,IabBroadcastReceiver.IabBroadcastListener {

    //TODO for release builds set to ENABLE_LOGS = false
    public static final boolean ENABLE_LOGS = false;
    public static final boolean ENABLE2_LOGS = false;
    public static final String TAG = "Pete";

    public static String ImageFilterColour = "#add8e6";

    boolean DisableAdds = false;
    boolean RestartRequired = false;
    int PlayDBInitialized = 0;

    static final String STATE_NBACK = "playernback";
    //static final String STATE_LEVEL = "playerLevel";

    public static final int nBacks = 6;
    public static final int Areas = 4;
    public static final int Levels = 3;

    public static final int _50PERCENT = 0;
    public static final int _75PERCENT = 1;
    public static final int _100PERCENT = 2;

    public static final int PROPABILITY = 5;
    public static int CollectedXPpoints = 0;

    public boolean isPaused = false;
    public static boolean MyisResumed = true;

    String GameLeaderBoard;
    String AchievementBoard;
    boolean PushLeaderScore = false;
    boolean ContinuePlaying = true;
    //public static int GamePoints = 0;
    public static int[][] GamePointsLevel = new int[nBacks+1][2];
    int round;
    int timestart;
    int timeend;

    int Language;
    int Soundtheme;

    int testint;
    int devstring_int = 0;

    public static Boolean WantToBuy = false;

    String[][][] nBackAchievementID = new String[nBacks][Areas][Levels];
    public static boolean[][][] mnBackAchievement = new boolean[nBacks][Areas][Levels];

    public static int[] increaseNback = new int[nBacks+1];
    public static int[] PlayResult = new int[nBacks+1];

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
    public static boolean PreventDoubleStart = false;
    public static CountDownTimer DoupleStartTimer;
    public static CountDownTimer ShowRedTimer;
    public static CountDownTimer ClearRedTimer;
    public static CountDownTimer ContinuePlayingTimerT;
    public static Boolean ShowRedTimerRunning = false;
    public static Boolean ClearRedTimerRunning = false;
    public static Boolean ContinuePlayingTimer = false;

    int random_nbr = -1;
    int sound_id = -1;
    int nBack = 2;
    int nBackWhenPlaying = 0;
    int NbrOfPictures;
    int NbrOfPictures_old;

    public static final int PictureSteps = 31;
    //public static final int PictureSteps = 5;
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

    @SuppressWarnings("unchecked")
    private  List<Integer> myVisualList = new ArrayList();
    @SuppressWarnings("unchecked")
    private  List<Integer> mySoundList = new ArrayList();

    GridView gridview;

    Random random = new Random();

    boolean RegisteredInAppServices = false;
    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;

    // Will the subscription auto-renew?
    boolean mAutoRenewEnabled = false;

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // Does the user have an active subscription to the infinite laugh?
    public static boolean mSubscribedToInfiniteLaugh = false;

    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    static final String SKU_PREMIUM = "premium";
    static final String SKU_GAS = "gas";

    // Tracks the currently owned infinite gas SKU, and the options in the Manage dialog
    String mInfiniteGasSku = "";
    //String mFirstChoiceSku = "";
    //String mSecondChoiceSku = "";

    // SKU for our subscription (infinite gas)
    //static final String SKU_INFINITE_LAUGH = "infinite_laughs";
    static final String SKU_INFINITE_GAS_MONTHLY = "infinite_gas_monthly";
    static final String SKU_INFINITE_GAS_YEARLY = "infinite_gas_yearly";

    //static final String SKU_INFINITE_LAUGH = "android.test.purchased";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 2;

    // How many units (1/4 tank is our unit) fill in the tank.
    static final int TANK_MAX = 4;

    // Current amount of gas in tank, in units
    int mTank;

    // The helper object
    IabHelper mIabHelper;
    //IabHelper mHelper;
    //private GoogleApiClient mGoogleApiClient;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    //Ads
    InterstitialAd mInterstitialAd;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getSupportActionBar().hide();

        if (savedInstanceState != null) {
            if (ENABLE_LOGS) Log.d("Pete", "ReCreate MainActivity...");
        }else {
            if (ENABLE_LOGS) Log.d("Pete", "Initial Create MainActivity...");
        }

        setContentView(R.layout.activity_main);

        RestartRequired = false;
        MyisResumed = true;

        ReadPreferences();

        if(devstring_int==666){
            testint = 666;
            saveLocal();
        }

        loadLocal();

        if(PlayDBInitialized==0){
            if(ENABLE_LOGS) Log.d(TAG, "PlayDBInitialize....");

            DatabaseHandler db = new DatabaseHandler(this);
            db.InitPlayTimeDB();
            db.close();

            PlayDBInitialized = 1;
            saveLocal();
        }

        if(ENABLE_LOGS) Log.d(TAG, "testint: " + testint);

        if(testint==666){
            if(ENABLE_LOGS) Log.d(TAG, "App is purchased!!!");
            mSubscribedToInfiniteLaugh=true;
            mSubscribedToInfiniteGas = true;
            mIsPremium = true;
            DisableAdds = true;
        }

        findViewById(R.id.adView).setVisibility(DisableAdds ? View.GONE : View.VISIBLE);
        findViewById(R.id.buy).setVisibility(DisableAdds ? View.GONE : View.VISIBLE);

        if(!DisableAdds) {

            MobileAds.initialize(getApplicationContext(), "ca-app-pub-7557334974231969~4312875732");
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-7557334974231969/2696541733");

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    if (ENABLE_LOGS) Log.d(TAG, "onAdClosed....");
                    requestNewInterstitial();
                    //StartNow();
                    StopAllTimers("onAdClosed");
                    ContinuePlayingTimer(1000,"onAdClosed");
                }
            });

            requestNewInterstitial();
        }

        SetStringsArrays();

        NbrOfPictures_old = NbrOfPictures;

        //String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0H8ToRVuOEnB6S02/ODLGP85IR+V9M6lH7WysSWbl64gPE32/OxtiNMyeMrppabt9Ywp4R0O620CJVzXowRc/WXzKbC8B5PwzRexqjitGir2dlHYQIWxWKzQXfuh4mCBciLiiAis8e+6Pxt/0hEKqv1J3yKfidc79Wc5z8FSgPKCD62S7MQB5rly3dMJEUJNqRcMrmdiPjuOPnyhMC7zcFHyrve/UV2UFDR2UEs8yObiizIgW+cjcWzi45V2iMu8TGa54goqaeKRF3ZFz5mRIdjoTBllC+B5dMq8wncqopHSYB3bP9GgG2GmtjlbAc67igm/kwQBvvNW4bL6/RluLQIDAQAB";
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhkYbOQaFqAp1n1IAvxLeYGepgh1NDM6ZmjlMPFbNsF+lBvzFx9gqfCbtu29QWyqalbS++WzixPLwMZiLDRhF+u/QmbCi1en9NpDQNw1NEbvpdNBeXeuHaWNxqCdAJ8fKOaukPQCM/5Jt6c/KzGFdal2DOyxkud6DhMeVTzTQP5wUJJo7oVylymQlboWVjl6AxL4j3+bAJMeTr4j6Rh3Owtb5J1eowJM95X4jIf7eToxE793FDcN2B03kK15ZEzrt7TM/VVDwhm9wygRE4vQa0+WChRogNjtEbrsgaknfiBQ71RQocae9LssnsEye7tHiJhBmO0LcM9TicdaMVJpTVQIDAQAB";
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        if(ENABLE_LOGS) Log.d(TAG, "Creating IAB helper.");
        mIabHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mIabHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        if(ENABLE_LOGS) Log.d(TAG, "Starting setup.");

        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if(ENABLE_LOGS) Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mIabHelper == null) return;

                RegisteredInAppServices = true;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                if(ENABLE_LOGS) Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mIabHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });

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

        //We do not need array here...
        for(int l=0; l<NbrOfPictures; l++) {
            NewArray[l] = Pictures.BACKGROUND_IDS[1];
        }

        gridview.setAdapter(new ImageAdapter(this, NbrOfPictures, picturewidth));


        if (ENABLE_LOGS) Log.v("Pete", "testing rate my app.....");
        //RateMeMaybe.resetData(this);
        RateMeMaybe rmm = new RateMeMaybe(this);
        rmm.setPromptMinimums(10, 2, 5, 2);
        //rmm.setPromptMinimums(0, 0, 0, 0);
        rmm.setRunWithoutPlayStore(true);
        rmm.setAdditionalListener(this);
        rmm.setDialogMessage("You seem to like this app, "
                + "since you have already used it %totalLaunchCount% times! "
                + "It would be great if you took a moment to rate it.");
        rmm.setDialogTitle("Rate this app");
        rmm.setPositiveBtn("Yes!");
        rmm.run();


        if(ENABLE_LOGS) Log.v("Pete", "onCreate MyisResumed: " + MyisResumed);

        //Just Zero to DB...
        //DatabaseHandler db = new DatabaseHandler(this);
        //db.insertOrUpdate(playername, 0);
        //db.close();

    }

    private void requestNewInterstitial() {
        if(ENABLE_LOGS) Log.d(TAG, "in requestNewInterstitial()....");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
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

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
        if(ENABLE_LOGS) Log.d(TAG, "in setWaitScreen - set: " + set);

        if(ClearRedTimerRunning || ShowRedTimerRunning){
            if(ENABLE_LOGS) Log.d(TAG, "in setWaitScreen - Game is ongoing!!! Should we skip main_activity set?");
        }

        //findViewById(R.id.main_activity).setVisibility(set ? View.GONE : View.VISIBLE);
        //findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
    }
    /*

    @Override
    public void onStart() {
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onStart...");
        super.onStart();

    }

    @Override
    public void onStop() {
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onStop...");
        super.onStop();
        // The rest of your onStop() code.
        //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
    */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onSaveInstanceState...");
        // Save the user's current game state
        savedInstanceState.putInt(STATE_NBACK, nBackWhenPlaying);
        savedInstanceState.putInt("round", round);

        savedInstanceState.putBoolean("ContinuePlayingTimer", ContinuePlayingTimer);
        savedInstanceState.putBoolean("ShowRedTimerRunning", ShowRedTimerRunning);
        savedInstanceState.putBoolean("ClearRedTimerRunning", ClearRedTimerRunning);
        savedInstanceState.putBoolean("PreventDoubleStart", PreventDoubleStart);


        for(int i=0;i<nBacks;i++) {
            savedInstanceState.putInt("increaseNback_" + i, increaseNback[i]);
            savedInstanceState.putInt("PlayResult_" + i, PlayResult[i]);
        }

        for(int l=0; l<GamePointsLevel.length; l++) {
            savedInstanceState.putInt("GamePointsLevel_" + l + "_0", GamePointsLevel[l][0]);
            savedInstanceState.putInt("GamePointsLevel_" + l + "_1", GamePointsLevel[l][1]);
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onRestoreInstanceState...");

        // Restore state members from saved instance
        nBackWhenPlaying = savedInstanceState.getInt(STATE_NBACK);
        round = savedInstanceState.getInt("round");

        if(ENABLE_LOGS) Log.v("Pete", "round: " + round);

        ContinuePlayingTimer = savedInstanceState.getBoolean("ContinuePlayingTimer");
        ShowRedTimerRunning = savedInstanceState.getBoolean("ShowRedTimerRunning");
        ClearRedTimerRunning = savedInstanceState.getBoolean("ClearRedTimerRunning");

        //Just if this is false....
        if(!PreventDoubleStart)
            PreventDoubleStart = savedInstanceState.getBoolean("PreventDoubleStart");

        if(ENABLE_LOGS) Log.v("Pete", "ContinuePlayingTimer: " + ContinuePlayingTimer);
        if(ENABLE_LOGS) Log.v("Pete", "ShowRedTimerRunning: " + ShowRedTimerRunning);
        if(ENABLE_LOGS) Log.v("Pete", "ClearRedTimerRunning: " + ClearRedTimerRunning);
        if(ENABLE_LOGS) Log.v("Pete", "PreventDoubleStart: " + PreventDoubleStart);

        for(int i=0;i<nBacks;i++) {
            increaseNback[i] = savedInstanceState.getInt("increaseNback_" + i);
            PlayResult[i] = savedInstanceState.getInt("PlayResult_" + i);
            if(ENABLE2_LOGS) Log.v("Pete", "increaseNback[i]: " + increaseNback[i]);
            if(ENABLE2_LOGS) Log.v("Pete", "PlayResult[i]: " + PlayResult[i]);
        }

        for(int l=0; l<GamePointsLevel.length; l++) {
            GamePointsLevel[l][0] = savedInstanceState.getInt("GamePointsLevel_" + l +"_0");
            GamePointsLevel[l][1] = savedInstanceState.getInt("GamePointsLevel_" + l +"_1");
            if(ENABLE2_LOGS) Log.v("Pete", "GamePointsLevel[l][0]: " + GamePointsLevel[l][0]);
            if(ENABLE2_LOGS) Log.v("Pete", "GamePointsLevel[l][1]: " + GamePointsLevel[l][1]);
        }

        if(ENABLE_LOGS) Log.v("Pete", "nBackWhenPlaying: " + nBackWhenPlaying);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onPause...");

        StopAllTimers("onPause");

        isPaused = true;
        MyisResumed = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ENABLE_LOGS) Log.v("Pete", "MainActivity onResume...");

        ReadPreferences();

        if(!ContinuePlayingTimer && !ShowRedTimerRunning && !ClearRedTimerRunning && !PreventDoubleStart) {
            if(ENABLE_LOGS) Log.v("Pete", "No Timers should be running so we call SetInitUI");
            SetInitUI("onResume");
        }

        if(PreventDoubleStart) {
            if (ENABLE_LOGS) Log.d("Pete", "PreventDoubleStart is true.. ");

            if(DoupleStartTimer!=null)
                DoupleStartTimer.cancel();

            StopAllTimers("onResume");
            PreventDoubleStart(10000);
            SetGameButtons("onResume");
            ContinuePlayingTimer(1000,"onResume");
        }

        InitAll("onResume");
        setWaitScreen(false);
        SetHeader(nBack);

        if(!manualmode){
            //InitPlayModeParam();
            nBack = 1;
            NbrOfPictures = 9;

            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = SP.edit();
            editor.putString("nback", "1");
            editor.putString("areasize", "3");
            editor.apply();
        }

        if((NbrOfPictures != NbrOfPictures_old) || RestartRequired) {
            if(ENABLE_LOGS) Log.v("Pete", "MainActivity onResume - recreate...");
            RestartRequired = false;

            for(int l=NbrOfPictures; l<mImageViews.length; l++) {
                mImageViews[l] = null;
            }

            if (Build.VERSION.SDK_INT >= 11) {
                if(ENABLE_LOGS) Log.v("Pete", "onResume - recreate()");
                recreate();
            } else {
                if(ENABLE_LOGS) Log.v("Pete", "onResume -(Build.VERSION.SDK_INT < 11");
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }

        isPaused = false;
        MyisResumed = true;
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

                SetHeader(nBack);
                //SetInitUI("onSignInSucceeded");
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

    public void StartNow(){
        ShowRedTimer(5000);

        for(int l=0; l<GamePointsLevel.length; l++) {
            GamePointsLevel[l][0] = 0;
            GamePointsLevel[l][1] = 0;
            round = 0;
        }

        if (!manualmode) {
            ShowToastnBack();
        }

        timestart = (int)((System.currentTimeMillis()/1000)%3600);
    }

    public void Start(View arg0){
        if (ENABLE_LOGS) Log.d("Pete", "Start...");

        SetScreenOn();

        //Just make sure we do not start with nBack zero???
        if(nBack==0) {
            nBack = 1;

            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = SP.edit();
            editor.putString("nback", "1");
            editor.apply();
        }

        if(PreventDoubleStart) {
            if (ENABLE_LOGS) Log.d("Pete", "Double Start Prevented......");
            return;
        }

        SetGameButtons("Start");

        PreventDoubleStart = true;
        PreventDoubleStart(10000);

        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Start Button Pressed").build());

        for(int l=0; l<GamePointsLevel.length; l++) {
            GamePointsLevel[l][0] = 0;
            GamePointsLevel[l][1] = 0;
            round = 0;
        }
        if (!manualmode) {
            //Gamemode always starts from 1
            nBack = 1;
            InitPlayModeParam();
        }
        nBackWhenPlaying = nBack;

        if(mInterstitialAd!=null) {
            if (mInterstitialAd.isLoaded()) {
                if (ENABLE_LOGS) Log.d("Pete", "mInterstitialAd.show()");
                mInterstitialAd.show();
            } else {
                if (ENABLE_LOGS) Log.d("Pete", "No Adds Loaded - start game...");
                StartNow();
            }
        }else{
            if (ENABLE_LOGS) Log.d("Pete", "mInterstitialAd==null...");
            StartNow();
        }
    }

    public void StopAllTimers(String caller){
        if (ENABLE_LOGS) Log.d("Pete", "StopAllTimers() - caller: " + caller);

        ShowRedTimerRunning = false;

        if(ShowRedTimer!=null)
            ShowRedTimer.cancel();

        ClearRedTimerRunning = false;

        if(ClearRedTimer!=null)
            ClearRedTimer.cancel();

        if(ContinuePlayingTimerT!=null) {
            if (ENABLE_LOGS) Log.d("Pete", "ContinuePlayingTimerT.cancel()");
            ContinuePlayingTimerT.cancel();
        }

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

    public void SetInitUI(String Caller){
        if(ENABLE_LOGS) Log.v("Pete", "in SetInitUI() - caller: " + Caller);
        findViewById(R.id.horizontalview).setVisibility(View.VISIBLE);
        findViewById(R.id.linearview).setVisibility(View.GONE);
        findViewById(R.id.undergrid).setVisibility(View.GONE);

        SetHeader(nBack);
    }

    public void Stop(View arg0){
        if(ENABLE_LOGS) Log.v("Pete", "Stop clicked...");

        ClearScreenOn();

        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Stop Button Pressed").build());

        PreventDoubleStart = false;

        if(DoupleStartTimer!=null)
            DoupleStartTimer.cancel();

        findViewById(R.id.undergrid).setVisibility(View.GONE);

        //PrintLists();

        SetInitUI("Stop");
        StopAllTimers("Stop");
        InitAll("Stop");

        InitPlayModeParam();

        nBackWhenPlaying = 0;
        //PrintLists();
    }

    public void Restart(View arg0){
        if(ENABLE_LOGS) Log.v("Pete", "Restart clicked...");
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Restart Button Pressed").build());

        for(int l=0; l<mImageViews.length; l++) {
            mImageViews[l] = null;
        }

        SetInitUI("Restart");
        StopAllTimers("Restart");
        InitAll("Restart");
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

        //finish();
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
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Info Button Pressed").build());


        //SetAchievement(nBack, areasizeInt, _50PERCENT);
        /*
        DatabaseHandler db = new DatabaseHandler(this);
        int ran = random.nextInt(1000);
        db.addScore_game(playername,ran);
        */

        /*
        //Just Zero to DB...
        DatabaseHandler db = new DatabaseHandler(this);
        db.InitPlayTimeDB();
        db.close();
        */

        /*
        DatabaseHandler db = new DatabaseHandler(this);
        db.insertOrUpdate("Pete", 200);
        db.close();
        */
        /*
        gridview = (GridView) findViewById(R.id.gridview);
        int HorizontalSpacing = gridview.getHorizontalSpacing();
        if (ENABLE_LOGS) Log.v("Pete", "HorizontalSpacing: " + HorizontalSpacing);
        gridview.setVerticalSpacing(10);
        //"gridviewname".getVerticalSpacing()
        */
        /*
        GamePoints = 300;
        Games.Leaderboards.submitScore(mHelper.getApiClient(), GameLeaderBoard, GamePoints);
        */

        //CollectAchievementPoints();

        /*
        DatabaseHandler db = new DatabaseHandler(this);
        db.insertOrUpdate(playername,10);
        db.close();
        */
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void Settings(View arg0) {
        if(ENABLE_LOGS) Log.v("Pete", "Settings clicked...");
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Settings Button Pressed").build());

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onShowLocalHiScores(View arg0){
        if(ENABLE_LOGS) Log.v("Pete", "onShowLocalHiScores clicked...");

        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }

    public void onShowAchievementsRequested(View arg0){
        if(ENABLE_LOGS) Log.v("Pete", "onShowAchievementsRequested clicked...");

        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
            View b_out = findViewById(R.id.signin);
            b_out.setVisibility(View.VISIBLE);
        }

    }

    public void ChartButtonClicked(View view) {
        if(ENABLE_LOGS) Log.v("Pete", "ChartButtonClicked...");
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Chart Button Pressed").build());

        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }

    public void TimeHistoryActivityClicked(View view) {
        if(ENABLE_LOGS) Log.v("Pete", "TimeHistoryActivityClicked...");
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Timer History Button Pressed").build());

        Intent intent = new Intent(this, TimeHistoryActivity.class);
        startActivity(intent);
    }

    //@Override
    public void onSignInButtonClicked(View view) {
        if(ENABLE_LOGS) Log.v("Pete", "In onSignInButtonClicked...");
        // start the sign-in flow
        beginUserInitiatedSignIn();
    }

    /*
    //@Override
    public void onSignOutButtonClicked(View view) {
        if(ENABLE_LOGS) Log.v("Pete", "In onSignOutButtonClicked...");

        if(isSignedIn()){
            if(ENABLE_LOGS) Log.v("Pete", "lets sign out...");
            signOut();
        }

        View b_out = findViewById(R.id.button_sign_out);
        b_out.setVisibility(View.GONE);

        View b_in = findViewById(R.id.button_sign_in);
        b_in.setVisibility(View.VISIBLE);

    }
    */

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

    /*
    public void turnOffScreen(){
        // turn off screen
        if(ENABLE_LOGS) Log.d ("Pete", "turnOffScreen...");
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    */

    public void ShowRedTimer(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In ShowRedTimer - nBack: " + nBack);

        View MyView = findViewById(R.id.horizontalview);

        if (MyView.getVisibility() == View.VISIBLE) {
            if(ENABLE_LOGS) Log.v("Pete", "In ShowRedTimer - This is ever be here - BUG!!!!!!");
            SetGameButtons("ShowRedTimer");
        }

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

            if(ENABLE_LOGS) Log.v("Pete", "Before: increaseNback["+nBack+"]: " + increaseNback[nBack]);
            if(ENABLE_LOGS) Log.v("Pete", "Before: PlayResult["+nBack+"]: " + PlayResult[nBack]);

            if(!manualmode) {
                //We Play only once the first level
                if(nBack==1)
                    increaseNback[nBack]++;

                increaseNback[nBack]++;

                if(ENABLE_LOGS) Log.v("Pete", "After: increaseNback["+nBack+"]: " + increaseNback[nBack]);

                if (ResultPercent>=50){
                    //We Play only once the first level
                    if(nBack==1)
                        PlayResult[nBack]++;

                    PlayResult[nBack]++;
                    round++;

                    if(ENABLE_LOGS) Log.v("Pete", "After: PlayResult["+nBack+"]: " + PlayResult[nBack]);
                }
            }

            ShowPopUp_OK();
            //SetInitUI("ShowRedTimer");
            InitAll("ShowRedTimer");

            if(!manualmode) {
                //Played level only once...
                if (increaseNback[nBack] == 1) {
                    if (PlayResult[nBack] == 1) {
                        if(ENABLE_LOGS) Log.v("Pete", "Game continues.....");
                    } else {
                        if(ENABLE_LOGS) Log.v("Pete", "Game over.....");
                        //DatabaseHandler db = new DatabaseHandler(this);
                        db.addScore_game(playername,GetPlayerPoint());
                        PushLeaderScore = true;
                        pushAccomplishments();
                    }
                }
                if (increaseNback[nBack] == 2) {
                    if (PlayResult[nBack] == 2) {
                        if(ENABLE_LOGS) Log.v("Pete", "Game continues 2.....");
                    } else {
                        if(ENABLE_LOGS) Log.v("Pete", "Game over.....");
                        db.addScore_game(playername,GetPlayerPoint());
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

                if(random_nbr>=0)
                    mImageViews[random_nbr].clearColorFilter();

                PlaySound();

                ClearRedTimer(2000);
            }
        }.start();
    }

    public void ClearRedTimer(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In ClearRedTimer...");
        ClearRedTimerRunning =  true;

        int testNumberOfPicturesToShow = NumberOfPicturesToShow - 1;
        int FontSize = ReturnFontSize();
        TextHeader = (TextView)findViewById(R.id.undergrid);
        TextHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        TextHeader.setTypeface(TextHeader.getTypeface(), Typeface.BOLD);
        TextHeader.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextHeader.setTextColor(Color.WHITE);
        String message = "" + testNumberOfPicturesToShow;
        TextHeader.setText(message);

        ClearRedTimer = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "ClearRedTimer onFinish...");
                ClearRedTimerRunning = false;

                int color = Color.parseColor(ImageFilterColour);

                if(random_nbr>=0)
                    mImageViews[random_nbr].setColorFilter(color);

                ShowRedTimer(2000);

                //if(ENABLE_LOGS) Log.v("Pete", "GetFromList: " + GetFromList(nBack-1));

            }
        }.start();
    }




    public void PreventDoubleStart(long startfromthis_ms) {
        if(ENABLE_LOGS) Log.v("Pete", "In PreventDoubleStart");

        DoupleStartTimer = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "DoupleStartTimer onFinish");
                PreventDoubleStart = false;
            }
        }.start();
    }


    public void ContinuePlayingTimer(long startfromthis_ms, String caller) {
        if(ENABLE_LOGS) Log.v("Pete", "In ContinuePlayingTimer - nBack: " + nBack + " caller: " + caller);

        SetScreenOn();

        PreventDoubleStart = true;
        PreventDoubleStart(15000);

        ContinuePlayingTimer =  true;

        ContinuePlayingTimerT = new CountDownTimer(startfromthis_ms, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(ENABLE_LOGS) Log.v("Pete", "ContinuePlayingTimer onFinish  - nBack: " + nBack);

                if(ENABLE_LOGS) Log.v("Pete", "MyisResumed: " +  ReturnIsResumed());

                if(!ReturnIsResumed()){
                    if(ENABLE_LOGS) Log.v("Pete", "ContinuePlayingTimer - MyisResumed is false...");
                    ContinuePlayingTimer(500,"ContinuePlayingTimer - MyisResumed is false");
                }else {

                    ContinuePlayingTimer = false;

                    nBack = nBackWhenPlaying;
                    SetHeader(nBack);
                    ShowToastnBack();

                    SetHeader(nBack);

                    ShowRedTimer(5000);

                    SetNbrPicturesLeft(NumberOfPicturesToShow);

                    //SetGameButtons("ContinuePlayingTimer");

                    timestart = (int) ((System.currentTimeMillis() / 1000) % 3600);
                }
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

        returnvalue = myVisualList.get(index);

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

        returnvalue = mySoundList.get(index);

        if(ENABLE_LOGS) Log.v("Pete", "GetFromList: " + returnvalue);

        return returnvalue;
    }

    /*
    public void PrintLists(){
        if(ENABLE_LOGS) Log.v("Pete", "In PrintLists...");
        for(int l=0; l<mySoundList.size(); l++) {
            if(ENABLE_LOGS) Log.v("Pete", "mySoundList: " + mySoundList.get(l));
        }
        for(int l=0; l<mySoundList.size(); l++) {
            if(ENABLE_LOGS) Log.v("Pete", "myVisualList: " + myVisualList.get(l));
        }
    }
    */

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
        int nbr_of_pictures;
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

    public void InitAll(String caller){
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "InitAll - Caller: " + caller);

        int color = Color.parseColor(ImageFilterColour);

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
        DisableAdds =  SP.getBoolean("addson", false);

        String devstring = SP.getString("devstring", "1");

        try{
            devstring_int = Integer.parseInt(devstring);
        }catch(NumberFormatException ex){ // handle your exception
            if (ENABLE_LOGS) Log.d("Pete", "ReadPreferences() - NumberFormatException when converting to int: " + devstring);
        }

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

    public void ShowPopUp_Buy(){
        if (ENABLE_LOGS) Log.d("Pete", "ShowPopUp_Buy....");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int FontSize = 16;
        int TextBackRoundColour = 0xaa000000;

        ImageButton justfind;

        // POPUP WINDOW STARTS //

        LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        //just avoiding compiler warning...
        final ViewGroup nullParent = null;
        View popupView = layoutInflater.inflate(R.layout.popup, nullParent);


        // final PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);

        LinearLayout ll = (LinearLayout)popupView.findViewById(R.id.popup_ll);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final TextView EmptyRowView = new TextView(this);
        EmptyRowView.setText("\n");
        EmptyRowView.setGravity(Gravity.CENTER);
        EmptyRowView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        EmptyRowView.setTypeface(EmptyRowView.getTypeface(), Typeface.BOLD);
        ll.addView(EmptyRowView,params);

        final TextView rowTextView = new TextView(this);
        String message = " Remove Ads And Enable Progress Chart! ";
        rowTextView.setText(message);
        rowTextView.setGravity(Gravity.CENTER);
        rowTextView.setTextColor(Color.WHITE);
        rowTextView.setBackgroundColor(TextBackRoundColour);
        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView.setTypeface(rowTextView.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView,params);

        final TextView rowTextView1 = new TextView(this);
        rowTextView1.setText("");
        rowTextView1.setGravity(Gravity.CENTER);
        rowTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView1.setTypeface(rowTextView1.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView1,params);

        final TextView rowTextView2 = new TextView(this);
        message = " Buy Premium And Support Developer? ";
        rowTextView2.setText(message);
        rowTextView2.setGravity(Gravity.CENTER);
        rowTextView2.setTextColor(Color.WHITE);
        rowTextView2.setBackgroundColor(TextBackRoundColour);
        rowTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView2.setTypeface(rowTextView2.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView2,params);

        final TextView EmptyRowView2 = new TextView(this);
        EmptyRowView2.setText("\n");
        EmptyRowView2.setGravity(Gravity.CENTER);
        EmptyRowView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        EmptyRowView2.setTypeface(EmptyRowView2.getTypeface(), Typeface.BOLD);
        ll.addView(EmptyRowView2,params);

        message = "CANCEL";
        Button btnDismiss = new Button(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(5, 5, 5, 10);
        btnDismiss.setLayoutParams(params2);
        btnDismiss.setText(message);
        btnDismiss.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        btnDismiss.setBackgroundResource(R.drawable.button_info_page);
        btnDismiss.setTextColor(Color.WHITE);

        ll.addView(btnDismiss, params2);

        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ENABLE_LOGS) Log.v("Pete", "ShowPopUp onClick - CANCEL....");
                popupWindow.dismiss();
                //called_show();
            }
        });

        message = "BUY";
        Button buy = new Button(this);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params3.setMargins(5,5,5,10);
        buy.setLayoutParams(params3);
        buy.setText(message);
        buy.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        buy.setBackgroundResource(R.drawable.button_info_page);
        buy.setTextColor(Color.WHITE);

        ll.addView(buy, params3);

        buy.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (ENABLE_LOGS) Log.v("Pete", "ShowPopUp onClick - buy....");
                MainActivity.SetBuyInWanted();
                popupWindow.dismiss();
                onUpgradePremium();

            }
        });

        //Just find some view where we can refer....
        justfind = (ImageButton)findViewById(R.id.settings);
        popupWindow.showAtLocation(justfind, Gravity.CENTER, 0, 0);
        // POPUP WINDOW ENDS //
    }

    public static void SetBuyInWanted(){
        WantToBuy = true;
    }

    public void ShowPopUp_OK(){
        if (ENABLE_LOGS) Log.d("Pete", "ShowPopUp_OK....");

        //Mark the endtime
        timeend = (int)((System.currentTimeMillis()/1000)%3600);
        int difference = (3600 + timeend - timestart)%3600;

        if(nBack>1) {
            DatabaseHandler db = new DatabaseHandler(this);
            db.insertOrUpdate(playername, difference);
            db.close();
            //Toast.makeText(this, "Seconds to DB: " + difference, Toast.LENGTH_LONG).show();
        }

        ContinuePlaying = false;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int FontSize = 16;
        int TextBackRoundColour = 0xaa000000;
        ImageButton justfind;

        // POPUP WINDOW STARTS //
        LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        //View popupView = layoutInflater.inflate(R.layout.popup2, null);

        //just avoiding compiler warning...
        final ViewGroup nullParent = null;
        View popupView = layoutInflater.inflate(R.layout.popup2, nullParent);

        // final PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);

        String message;
        String message2 = "";

        LinearLayout ll = (LinearLayout)popupView.findViewById(R.id.popup_ll);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final TextView rowTextView0 = new TextView(this);
        //rowTextView.setText(message);
        rowTextView0.setText("\n");
        rowTextView0.setGravity(Gravity.CENTER);
        rowTextView0.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView0.setTypeface(rowTextView0.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView0,params);

        final TextView rowTextView = new TextView(this);
        message = " Correct positions: " + CorrectPicClicked + " ";
        rowTextView.setText(message);
        rowTextView.setGravity(Gravity.CENTER);
        rowTextView.setTextColor(Color.WHITE);
        rowTextView.setBackgroundColor(TextBackRoundColour);
        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView.setTypeface(rowTextView.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView,params);

        final TextView rowTextView2 = new TextView(this);
        message = " Correct letters: " + CorrectSoundClicked + " ";
        rowTextView2.setText(message);
        rowTextView2.setGravity(Gravity.CENTER);
        rowTextView2.setTextColor(Color.WHITE);
        rowTextView2.setBackgroundColor(TextBackRoundColour);
        rowTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView2.setTypeface(rowTextView2.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView2,params);

        final TextView rowTextView3 = new TextView(this);
        message = " Wrong positions: " + WrongPicClicked + " ";
        rowTextView3.setText(message);
        rowTextView3.setGravity(Gravity.CENTER);
        rowTextView3.setTextColor(Color.WHITE);
        rowTextView3.setBackgroundColor(TextBackRoundColour);
        rowTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView3.setTypeface(rowTextView3.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView3,params);

        final TextView rowTextView4 = new TextView(this);
        message = " Wrong letters: " + WrongSoundClicked + " ";
        rowTextView4.setText(message);
        rowTextView4.setGravity(Gravity.CENTER);
        rowTextView4.setTextColor(Color.WHITE);
        rowTextView4.setBackgroundColor(TextBackRoundColour);
        rowTextView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView4.setTypeface(rowTextView4.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView4,params);

        final TextView rowTextView5 = new TextView(this);
        message = " Missed positions: " + MissedPicClick + " ";
        rowTextView5.setText(message);
        rowTextView5.setGravity(Gravity.CENTER);
        rowTextView5.setTextColor(Color.WHITE);
        rowTextView5.setBackgroundColor(TextBackRoundColour);
        rowTextView5.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView5.setTypeface(rowTextView5.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView5,params);

        final TextView rowTextView6 = new TextView(this);
        message = " Missed letters: " + MissedSoundClick + " ";
        rowTextView6.setText(message);
        rowTextView6.setGravity(Gravity.CENTER);
        rowTextView6.setTextColor(Color.WHITE);
        rowTextView6.setBackgroundColor(TextBackRoundColour);
        rowTextView6.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView6.setTypeface(rowTextView6.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView6,params);

        final TextView rowTextView7 = new TextView(this);
        rowTextView7.setText("\n");
        rowTextView7.setGravity(Gravity.CENTER);
        rowTextView7.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        rowTextView7.setTypeface(rowTextView7.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView7,params);

        int ResultPercent = (int)((double)(CorrectPicClicked + CorrectSoundClicked) / (double)(CorrectPicClicked + CorrectSoundClicked + WrongPicClicked + WrongSoundClicked + MissedPicClick + MissedSoundClick) * 100);

        int FontSizeResult = FontSize + 2;

        final TextView rowTextView8 = new TextView(this);
        message = " Result %: " + ResultPercent + " ";
        rowTextView8.setText(message);
        rowTextView8.setGravity(Gravity.CENTER);
        rowTextView8.setTextColor(Color.RED);
        rowTextView8.setBackgroundColor(TextBackRoundColour);
        rowTextView8.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeResult);
        rowTextView8.setTypeface(rowTextView7.getTypeface(), Typeface.BOLD);
        ll.addView(rowTextView8,params);

        message = "\n\n";

        final TextView lastRowTextView = new TextView(this);
        lastRowTextView.setText(message);
        lastRowTextView.setGravity(Gravity.CENTER);
        lastRowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        lastRowTextView.setTypeface(lastRowTextView.getTypeface(), Typeface.BOLD);
        ll.addView(lastRowTextView,params);

        if(!manualmode) {
            if (increaseNback[nBack] == 1) {
                if (PlayResult[nBack] == 1) {
                    message = " Nice Work - continue playing! ";
                    message2 = " Points from this level: " + GamePointsLevel[nBack][0] + " TotalPoints: " + GetPlayerPoint() + " ";
                    ContinuePlaying = true;
                } else {
                    message = " Game over! ";
                    message2 = " Points from this level: " + GamePointsLevel[nBack][0] + " TotalPoints: " + GetPlayerPoint() + " ";

                    ClearScreenOn();
                }
            }
            if (increaseNback[nBack] == 2) {
                if (PlayResult[nBack] == 2) {
                    message = " Nice Work - nBack increased! ";
                    message2 = " Points from this level: " + GamePointsLevel[nBack][1] + " TotalPoints: " + GetPlayerPoint() + " ";

                    if(nBack==1)
                        message2 = " Points from this level: " + GamePointsLevel[nBack][0] + " TotalPoints: " + GetPlayerPoint() + " ";

                    if(nBack<nBacks) {
                        nBack++;
                        nBackWhenPlaying = nBack;
                        ContinuePlaying = true;
                        round = 0;
                    }
                    else{
                        message = " Congratulation! Maximum Level Reached! ";
                        message2 = " Points from this level: " + GamePointsLevel[nBack][1] + " TotalPoints: " + GetPlayerPoint() + " ";
                        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Game Played Trough!!").build());
                    }

                } else {
                    message = " Game over now! ";
                    message2 = " Points from this level: " + GamePointsLevel[nBack][1] + " TotalPoints: " + GetPlayerPoint() + " ";

                    if(nBack==1)
                        message2 = " Points from this level: " + GamePointsLevel[nBack][0] + " TotalPoints: " + GetPlayerPoint() + " ";

                    ClearScreenOn();
                }
            }

            final TextView last2RowTextView = new TextView(this);
            last2RowTextView.setText(message);
            last2RowTextView.setGravity(Gravity.CENTER);
            last2RowTextView.setTextColor(Color.WHITE);
            last2RowTextView.setBackgroundColor(TextBackRoundColour);
            last2RowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            last2RowTextView.setTypeface(last2RowTextView.getTypeface(), Typeface.BOLD);
            ll.addView(last2RowTextView, params);

            final TextView last21RowTextView = new TextView(this);
            last21RowTextView.setText(message2);
            last21RowTextView.setGravity(Gravity.CENTER);
            last21RowTextView.setTextColor(Color.WHITE);
            last21RowTextView.setBackgroundColor(TextBackRoundColour);
            last21RowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            last21RowTextView.setTypeface(last2RowTextView.getTypeface(), Typeface.BOLD);
            ll.addView(last21RowTextView, params);

            final TextView last3RowTextView = new TextView(this);
            last3RowTextView.setText("\n");
            last3RowTextView.setGravity(Gravity.CENTER);
            last3RowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            last3RowTextView.setTypeface(last2RowTextView.getTypeface(), Typeface.BOLD);
            ll.addView(last3RowTextView, params);

        }else{

            if(ResultPercent<10)
                message = " Keep training!! ";

            if(ResultPercent>10 && ResultPercent<50)
                message = " You are getting there!! Just keep training!! ";

            if(ResultPercent>=50 && ResultPercent<90)
                message = " Nice Reslut!! ";

            if(ResultPercent>=90)
                message = " Well Done!! ";

            final TextView last2RowTextView = new TextView(this);
            last2RowTextView.setText(message);
            last2RowTextView.setGravity(Gravity.CENTER);
            last2RowTextView.setTextColor(Color.WHITE);
            last2RowTextView.setBackgroundColor(TextBackRoundColour);
            last2RowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            last2RowTextView.setTypeface(last2RowTextView.getTypeface(), Typeface.BOLD);
            ll.addView(last2RowTextView, params);

            final TextView last3RowTextView = new TextView(this);
            last3RowTextView.setText("\n");
            last3RowTextView.setGravity(Gravity.CENTER);
            last3RowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            last3RowTextView.setTypeface(last2RowTextView.getTypeface(), Typeface.BOLD);
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

                    if(mInterstitialAd!=null) {
                        if (mInterstitialAd.isLoaded()) {
                            if (ENABLE_LOGS) Log.d("Pete", "show ads...");
                            mInterstitialAd.show();
                        } else {
                            if (ENABLE_LOGS) Log.d("Pete", "No Ads Loaded - start game...");
                            ContinuePlayingTimer(10,"ShowPopUp_OK - No Ads Loaded");
                        }
                    }else{
                        if (ENABLE_LOGS) Log.d("Pete", "mInterstitialAd is null - start game...");
                        ContinuePlayingTimer(10,"ShowPopUp_OK - mInterstitialAd is null");
                    }

                    //ContinuePlayingTimer(10);
                }else
                    SetInitUI("ShowPopUp_OK");
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

        final Toast toast= Toast.makeText(getApplicationContext(),
                mytoast, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        new CountDownTimer(800, 10)
        {
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.cancel();}
        }.start();

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

        preferences.put("testint", String.valueOf(testint));
        preferences.put("playdbinitialized", String.valueOf(PlayDBInitialized));

    }

    public void loadLocal() {
        if (ENABLE_LOGS) Log.v("Pete", "loadLocal...");

        //Init
        preferences = new SecurePreferences(this, "my-preferences", "SometopSecretKey1235", true);

        testint = Integer.parseInt(preferences.getIntString("testint"));
        PlayDBInitialized = Integer.parseInt(preferences.getIntString("playdbinitialized"));

        if (ENABLE_LOGS) Log.v("Pete", "loadLocal - testint: " + testint);
        if (ENABLE_LOGS) Log.v("Pete", "loadLocal - PlayDBInitialized: " + PlayDBInitialized);
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
            mHelper.getApiClient().disconnect();
        } else {
            if(ENABLE_LOGS) Log.v("Pete","Else - onActivityResult requestCode: " + requestCode + " resultCode: " + resultCode);

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
            Games.Leaderboards.submitScore(mHelper.getApiClient(), GameLeaderBoard, GetPlayerPoint());
            PushLeaderScore = false;
        }

        CollectAchievementPoints();
    }

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
        AchievementBoard = getString(R.string.leaderboard_achievement_leaderboard);
    }

    public void InitPlayModeParam(){

        for(int i=0;i<nBacks;i++) {
            increaseNback[i] = 0;
            PlayResult[i] = 0;
        }
    }

    public void UpdatePlayerPoint(int points){
        GamePointsLevel[nBack][round] = GamePointsLevel[nBack][round] + points*nBack;

        if(GamePointsLevel[nBack][round] < 0) GamePointsLevel[nBack][round] = 0;

        if(ENABLE_LOGS) Log.v("Pete", "GamePoints: " + GamePointsLevel[nBack][round]);
    }

    public int GetPlayerPoint(){

        int points = 0;

        for (int[] aGamePointsLevel : GamePointsLevel) {
            points = points + aGamePointsLevel[0] + aGamePointsLevel[1];
        }
        //Below was earlier...
        /*
        for(int l=0; l<GamePointsLevel.length; l++) {
            points = points + GamePointsLevel[l][0] + GamePointsLevel[l][1];
        }
        */

        return points;
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


    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if(ENABLE_LOGS) Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mIabHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            if(ENABLE_LOGS) Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            if(ENABLE_LOGS) Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            if(mIsPremium){
                if(ENABLE_LOGS) Log.d(TAG, "Adding 666 to testint...");
                testint = 666;
                saveLocal();
            }

            // First find out which subscription is auto renewing
            Purchase gasMonthly = inventory.getPurchase(SKU_INFINITE_GAS_MONTHLY);
            Purchase gasYearly = inventory.getPurchase(SKU_INFINITE_GAS_YEARLY);
            if (gasMonthly != null && gasMonthly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_MONTHLY;
                mAutoRenewEnabled = true;
            } else if (gasYearly != null && gasYearly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_YEARLY;
                mAutoRenewEnabled = true;
            } else {
                mInfiniteGasSku = "";
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteGas = (gasMonthly != null && verifyDeveloperPayload(gasMonthly))
                    || (gasYearly != null && verifyDeveloperPayload(gasYearly));
            if(ENABLE_LOGS) Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                if(ENABLE_LOGS) Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mIabHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
                } catch (IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                }
                //return;
            }

            //updateUi();
            //setWaitScreen(false);
            //Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        if(ENABLE_LOGS) Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mIabHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    /*
    // User clicked the "Buy Gas" button
    public void onBuyGasButtonClicked(View arg0) {
        Log.d(TAG, "Buy gas button clicked.");

        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }

        if (mTank >= TANK_MAX) {
            complain("Your tank is full. Drive around a bit!");
            return;
        }

        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for gas.");

        //   for security, generate your payload here for verification. See the comments on
        // *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
        // *        an empty string, but on a production app you should carefully generate this.
        String payload = "";

        try {
            mIabHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }
    */

    public void BuyButton(View arg0) {
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Buy Button Pressed").build());
        ShowPopUp_Buy();
    }

    // User clicked the "Upgrade to Premium" button.
    public void onUpgradePremium() {
        if(ENABLE_LOGS) Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
        setWaitScreen(true);

        /*  for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        try {
            mIabHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            setWaitScreen(false);
        }
    }

    /*
    // "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
    // flow for subscription.
    public void onInfiniteGasButtonClicked(View arg0) {
        if (!mIabHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }

        CharSequence[] options;
        if (!mSubscribedToInfiniteGas || !mAutoRenewEnabled) {
            // Both subscription options should be available
            options = new CharSequence[2];
            options[0] = getString(R.string.subscription_period_monthly);
            options[1] = getString(R.string.subscription_period_yearly);
            mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
            mSecondChoiceSku = SKU_INFINITE_GAS_YEARLY;
        } else {
            // This is the subscription upgrade/downgrade path, so only one option is valid
            options = new CharSequence[1];
            if (mInfiniteGasSku.equals(SKU_INFINITE_GAS_MONTHLY)) {
                // Give the option to upgrade to yearly
                options[0] = getString(R.string.subscription_period_yearly);
                mFirstChoiceSku = SKU_INFINITE_GAS_YEARLY;
            } else {
                // Give the option to downgrade to monthly
                options[0] = getString(R.string.subscription_period_monthly);
                mFirstChoiceSku = SKU_INFINITE_GAS_MONTHLY;
            }
            mSecondChoiceSku = "";
        }

        int titleResId;
        if (!mSubscribedToInfiniteGas) {
            titleResId = R.string.subscription_period_prompt;
        } else if (!mAutoRenewEnabled) {
            titleResId = R.string.subscription_resignup_prompt;
        } else {
            titleResId = R.string.subscription_update_prompt;
        }

    }
    */
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
    */

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        if(ENABLE_LOGS) Log.d(TAG, "payload: " + payload);

        /*
         * verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if(ENABLE_LOGS) Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mIabHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            if(ENABLE_LOGS) Log.d(TAG, "Purchase successful.");

            switch (purchase.getSku()) {
                case SKU_GAS:
                    // bought 1/4 tank of gas. So consume it.
                    if(ENABLE_LOGS) Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                    try {
                        mIabHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    } catch (IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");
                        setWaitScreen(false);
                        return;
                    }
                    break;
                case SKU_PREMIUM:
                    // bought the premium upgrade!
                    if(ENABLE_LOGS) Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                    alert("Thank you for upgrading to premium!");
                    mIsPremium = true;
                    //updateUi();
                    setWaitScreen(false);

                    if (mIsPremium) {
                        if(ENABLE_LOGS) Log.d(TAG, "OnIabPurchaseFinishedListener - Adding 666 to testint...");
                        testint = 666;
                        RestartRequired = true;
                        saveLocal();
                    }
                    break;
                case SKU_INFINITE_GAS_MONTHLY:
                case SKU_INFINITE_GAS_YEARLY:
                    // bought the infinite gas subscription
                    if(ENABLE_LOGS) Log.d(TAG, "Infinite gas subscription purchased.");
                    alert("Thank you for subscribing to infinite gas!");
                    mSubscribedToInfiniteGas = true;
                    mAutoRenewEnabled = purchase.isAutoRenewing();
                    mInfiniteGasSku = purchase.getSku();
                    mTank = TANK_MAX;
                    //updateUi();
                    setWaitScreen(false);
                    break;
            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if(ENABLE_LOGS) Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mIabHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                if(ENABLE_LOGS) Log.d(TAG, "Consumption successful. Provisioning.");
                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;

                //
                //saveData();
                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
            //updateUi();
            setWaitScreen(false);
            if(ENABLE_LOGS) Log.d(TAG, "End consumption flow.");
        }
    };

    /*
    // Drive button clicked. Burn gas!
    public void onDriveButtonClicked(View arg0) {
        Log.d(TAG, "Drive button clicked.");
        if (!mSubscribedToInfiniteGas && mTank <= 0) alert("Oh, no! You are out of gas! Try buying some!");
        else {
            if (!mSubscribedToInfiniteGas) --mTank;
            //
            //saveData();
            alert("Vroooom, you drove a few miles.");
            //updateUi();
            Log.d(TAG, "Vrooom. Tank is now " + mTank);
        }
    }
    */

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ENABLE_LOGS) Log.d(TAG, "onDestroy......");

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        if(ENABLE_LOGS) Log.d(TAG, "Destroying helper.");
        if (mIabHelper != null && RegisteredInAppServices) {
            mIabHelper.disposeWhenFinished();
            mIabHelper = null;
        }
    }

   void complain(String message) {
       Log.e(TAG, "**** BrainDualXBack Error: " + message);

       if(!isFinishing())
            alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    public int ReturnFontSize(){

        int FontSize = 20;

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL){
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ReturnFontSize SCREENLAYOUT_SIZE_NORMAL...");
            FontSize = 20;
        }

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE){
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ReturnFontSize SCREENLAYOUT_SIZE_LARGE...");
            FontSize = 22;
        }

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE){
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ReturnFontSize SCREENLAYOUT_SIZE_XLARGE...");
            FontSize = 24;
        }

        return FontSize;
    }

    public void SetHeader(int n_back){
        int FontSize = 12;
        int playedToday;
        String Player;
        String HTMLsourceString;

        if(playername.length()>18) {
            Player = playername.substring(0, 15);
            Player = Player + "...";
        }else
            Player = playername;

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE){
            FontSize = 16;
        }

        DatabaseHandler db = new DatabaseHandler(this);
        playedToday = db.PlayTimeToday();
        playedToday = (playedToday / 60);
        db.close();

        TextHeader = (TextView)findViewById(R.id.headertext);
        TextHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        TextHeader.setTypeface(TextHeader.getTypeface(), Typeface.BOLD);
        TextHeader.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextHeader.setTextColor(Color.WHITE);

        //Less than 20min Played today
        if(playedToday<20)
            HTMLsourceString = "Player: " + Player + " - <font color=#FF30000><b>Training today: " + playedToday + " min</<b></font>" + "<br/>" + "Area: " + NbrOfPictures + " nBack: " + String.valueOf(n_back) + (manualmode ? " ManualMode" : " PlayMode");
        else
            HTMLsourceString = "Player: " + Player + " - <font color=#00FF00><b>Training today: " + playedToday + " min</<b></font>" + "<br/>" + "Area: " + NbrOfPictures + " nBack: " + String.valueOf(n_back) + (manualmode ? " ManualMode" : " PlayMode");

        TextHeader.setText(Html.fromHtml(HTMLsourceString));

    }

    public void CollectAchievementPoints() {
        if(ENABLE_LOGS) Log.v("Pete", "CollectAchievementPoints....");

        //Here we should push LeaderScores, Achievements and Events
        if (!isSignedIn()) {
            // can't push to the cloud, so save locally
            if(ENABLE_LOGS) Log.v("Pete", "CollectAchievementPoints - not isSignedIn");
            return;
        }

        boolean fullLoad = false;  // set to 'true' to reload all achievements (ignoring cache)

        // load achievements
        Games.Achievements.load(mHelper.getApiClient(), fullLoad).setResultCallback(new ResultCallback<Achievements.LoadAchievementsResult>() {
            @Override
            public void onResult(Achievements.LoadAchievementsResult loadAchievementsResult) {

                //Zero XP points...
                CollectedXPpoints = 0;

                for (Achievement achievement : loadAchievementsResult.getAchievements()) {

                    // here you can work with the achievement objects
                    // ...
                    boolean unlocked = (achievement.getState() == Achievement.STATE_UNLOCKED);

                    if (unlocked) {
                        if (MainActivity.ENABLE_LOGS)Log.v("Pete", "Value: " + achievement.getXpValue());
                        CollectedXPpoints = CollectedXPpoints + (int)achievement.getXpValue();

                    }
                }

                if (MainActivity.ENABLE_LOGS)Log.v("Pete", "CollectedXPpoints: " + CollectedXPpoints);

                if(CollectedXPpoints>0)
                    Games.Leaderboards.submitScore(mHelper.getApiClient(), AchievementBoard, CollectedXPpoints);
            }
        });
    }

    public boolean ReturnIsResumed(){
        if(!MyisResumed)
            return false;
        else
            return true;
    }

    public void SetGameButtons(String caller){
        if(ENABLE_LOGS) Log.v("Pete", "SetGameButtons - Caller: " + caller);

        findViewById(R.id.horizontalview).setVisibility(View.GONE);
        findViewById(R.id.linearview).setVisibility(View.VISIBLE);
        findViewById(R.id.undergrid).setVisibility(View.VISIBLE);

        int myNumberOfPicturesToShow = NumberOfPicturesToShow - 1;
        SetNbrPicturesLeft(myNumberOfPicturesToShow);
    }

    public void SetNbrPicturesLeft(int myNumberOfPicturesToShow){
        int FontSize = ReturnFontSize();
        TextHeader = (TextView)findViewById(R.id.undergrid);
        TextHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
        TextHeader.setTypeface(TextHeader.getTypeface(), Typeface.BOLD);
        TextHeader.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextHeader.setTextColor(Color.WHITE);
        String message = "" + myNumberOfPicturesToShow;
        TextHeader.setText(message);
    }

    public void SetScreenOn(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void ClearScreenOn(){
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
