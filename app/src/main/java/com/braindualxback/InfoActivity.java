package com.braindualxback;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_info);

        String HTMLsourceString;
        int FontSizeHeader = 14;
        int FontSizeSpace = 6;
        int FontSize = 12;
        int TextBackRoundColour = 0xaa000000;

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL){
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ScoresActivity SCREENLAYOUT_SIZE_NORMAL...");
            FontSizeHeader = 14;
            FontSize = 13;
        }

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE){
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ScoresActivity SCREENLAYOUT_SIZE_LARGE...");
            FontSizeHeader = 14;
            FontSize = 13;
        }

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE){
            if(MainActivity.ENABLE_LOGS) Log.v("Pete", "ScoresActivity SCREENLAYOUT_SIZE_XLARGE...");
            FontSizeHeader = 16;
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.my_info);

        final TextView Header = new TextView(this);
        HTMLsourceString = "<font color=#00FF00><b> <u>INFO</u> </b></font>";
        Header.setText(Html.fromHtml(HTMLsourceString));
        Header.setGravity(Gravity.CENTER);
        Header.setTextColor(Color.WHITE);
        Header.setBackgroundColor(TextBackRoundColour);
        Header.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        // add the textview to the linearlayout
        ll.addView(Header);

        final TextView HeaderSpace = new TextView(this);
        HeaderSpace.setText("\n");
        HeaderSpace.setBackgroundColor(TextBackRoundColour);
        HeaderSpace.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(HeaderSpace);


        final TextView Body1 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  The n-back task is a continuous performance task that is commonly used as an assessment in cognitive neuroscience to measure a part of working memory.The n-back was introduced by Wayne Kirchner in 1958. </font>";
        Body1.setText(Html.fromHtml(HTMLsourceString));
        Body1.setGravity(Gravity.CENTER);
        Body1.setTextColor(Color.WHITE);
        Body1.setBackgroundColor(TextBackRoundColour);
        Body1.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        // add the textview to the linearlayout
        ll.addView(Body1);

        final TextView SpacesTextView = new TextView(this);
        SpacesTextView.setText("\n");
        SpacesTextView.setBackgroundColor(TextBackRoundColour);
        SpacesTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView);

        final TextView Body2 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  In the game the subject is presented with a sequence of stimuli, and the task consists of indicating when the current stimulus matches the one from n steps earlier in the sequence. The load factor n can be adjusted to make the task more or less difficult. </font>";
        Body2.setText(Html.fromHtml(HTMLsourceString));
        Body2.setGravity(Gravity.CENTER);
        Body2.setTextColor(Color.WHITE);
        Body2.setBackgroundColor(TextBackRoundColour);
        Body2.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body2);

        final TextView SpacesTextView2 = new TextView(this);
        SpacesTextView2.setText("\n");
        SpacesTextView2.setBackgroundColor(TextBackRoundColour);
        SpacesTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView2);

        final TextView Body3 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  To clarify, the visual n-back test is similar to the classic memory game of \"Concentration\". However, instead of different items that are in a fixed location on the game board, there is only one item, that appears in different positions on the game board during each turn. 1-N means that you have to remember the position of the item, ONE turn back. 2-N means that you have to remember the position of the item TWO turns back, and so on. </font>";
        Body3.setText(Html.fromHtml(HTMLsourceString));
        Body3.setGravity(Gravity.CENTER);
        Body3.setTextColor(Color.WHITE);
        Body3.setBackgroundColor(TextBackRoundColour);
        Body3.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body3);

        final TextView SpacesTextView3 = new TextView(this);
        SpacesTextView3.setText("\n");
        SpacesTextView3.setBackgroundColor(TextBackRoundColour);
        SpacesTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView3);

        final TextView Body4 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  For example, an auditory three-back test could consist of the experimenter reading the following list of letters to the test subject: </font>";
        Body4.setText(Html.fromHtml(HTMLsourceString));
        Body4.setGravity(Gravity.CENTER);
        Body4.setTextColor(Color.WHITE);
        Body4.setBackgroundColor(TextBackRoundColour);
        Body4.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body4);

        final TextView SpacesTextView4 = new TextView(this);
        SpacesTextView4.setText("\n");
        SpacesTextView4.setBackgroundColor(TextBackRoundColour);
        SpacesTextView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView4);

        final TextView Body5 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  T L H C H O <b><u>C</u></b> Q L <b><u>C</u></b> K <b><u>L</u></b> H C Q T R R K C H R </font>";
        Body5.setText(Html.fromHtml(HTMLsourceString));
        Body5.setGravity(Gravity.CENTER);
        Body5.setTextColor(Color.WHITE);
        Body5.setBackgroundColor(TextBackRoundColour);
        Body5.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body5);

        final TextView SpacesTextView5 = new TextView(this);
        SpacesTextView5.setText("\n");
        SpacesTextView5.setBackgroundColor(TextBackRoundColour);
        SpacesTextView5.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView5);

        final TextView Body6 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  The subject is supposed to indicate when the letters marked in bold and underline are read, because those correspond to the letters that were read three steps earlier (3-Back). </font>";
        Body6.setText(Html.fromHtml(HTMLsourceString));
        Body6.setGravity(Gravity.CENTER);
        Body6.setTextColor(Color.WHITE);
        Body6.setBackgroundColor(TextBackRoundColour);
        Body6.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body6);

        final TextView SpacesTextView6 = new TextView(this);
        SpacesTextView6.setText("\n");
        SpacesTextView6.setBackgroundColor(TextBackRoundColour);
        SpacesTextView6.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView6);

        final TextView Body7 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  The n-back task captures the active part of working memory. When n equals 2 or more, it is not enough to simply keep a representation of recently presented items in mind; the working memory buffer also needs to be updated continuously to keep track of what the current stimulus must be compared to. To accomplish this task, the subject needs to both maintain and manipulate information in working memory. </font>";
        Body7.setText(Html.fromHtml(HTMLsourceString));
        Body7.setGravity(Gravity.CENTER);
        Body7.setTextColor(Color.WHITE);
        Body7.setBackgroundColor(TextBackRoundColour);
        Body7.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body7);

        final TextView SpacesTextView7 = new TextView(this);
        SpacesTextView7.setText("\n");
        SpacesTextView7.setBackgroundColor(TextBackRoundColour);
        SpacesTextView7.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView7);

        final TextView Body8 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  The dual-task n-back task is a variation that was proposed by Susanne Jaeggi et al. in 2003. In the dual-task paradigm, two independent sequences are presented simultaneously, typically using different modalities of stimuli, such as one auditory and one visual. This game is based on this dual-task n-back. </font>";
        Body8.setText(Html.fromHtml(HTMLsourceString));
        Body8.setGravity(Gravity.CENTER);
        Body8.setTextColor(Color.WHITE);
        Body8.setBackgroundColor(TextBackRoundColour);
        Body8.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body8);

        final TextView SpacesTextView8 = new TextView(this);
        SpacesTextView8.setText("\n");
        SpacesTextView8.setBackgroundColor(TextBackRoundColour);
        SpacesTextView8.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView8);

        final TextView Body9 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  <b><u>How To Start</u></b> </font>";
        Body9.setText(Html.fromHtml(HTMLsourceString));
        Body9.setGravity(Gravity.CENTER);
        Body9.setTextColor(Color.WHITE);
        Body9.setBackgroundColor(TextBackRoundColour);
        Body9.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body9);

        final TextView SpacesTextView9 = new TextView(this);
        SpacesTextView9.setText("\n");
        SpacesTextView9.setBackgroundColor(TextBackRoundColour);
        SpacesTextView9.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView9);

        final TextView Body10 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  The Dual N-Back game field is a square separated to smaller squares with lines (like 3x3 cross-zero game field). </font>";
        Body10.setText(Html.fromHtml(HTMLsourceString));
        Body10.setGravity(Gravity.CENTER);
        Body10.setTextColor(Color.WHITE);
        Body10.setBackgroundColor(TextBackRoundColour);
        Body10.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body10);

        final TextView Body11 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  During the game the image is shown in one of the game squares. </font>";
        Body11.setText(Html.fromHtml(HTMLsourceString));
        Body11.setGravity(Gravity.CENTER);
        Body11.setTextColor(Color.WHITE);
        Body11.setBackgroundColor(TextBackRoundColour);
        Body11.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body11);

        final TextView Body12 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  Your task is to find a match of the current position of the image to the position shown one (or two, or N) step earlier. If there was a match, press the corresponding match key. Same thing for the auditory letters. Just listen and try to remember the letters from the previous steps. </font>";
        Body12.setText(Html.fromHtml(HTMLsourceString));
        Body12.setGravity(Gravity.CENTER);
        Body12.setTextColor(Color.WHITE);
        Body12.setBackgroundColor(TextBackRoundColour);
        Body12.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body12);

        final TextView SpacesTextView10 = new TextView(this);
        SpacesTextView10.setText("\n");
        SpacesTextView10.setBackgroundColor(TextBackRoundColour);
        SpacesTextView10.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView10);

        final TextView Body13 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  <b><u>Tips For Players</u></b> </font>";
        Body13.setText(Html.fromHtml(HTMLsourceString));
        Body13.setGravity(Gravity.CENTER);
        Body13.setTextColor(Color.WHITE);
        Body13.setBackgroundColor(TextBackRoundColour);
        Body13.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body13);

        final TextView SpacesTextView11 = new TextView(this);
        SpacesTextView11.setText("\n");
        SpacesTextView11.setBackgroundColor(TextBackRoundColour);
        SpacesTextView11.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView11);

        final TextView Body14 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - do it at least 20 minutes a day to have significant results</font>";
        Body14.setText(Html.fromHtml(HTMLsourceString));
        Body14.setGravity(Gravity.CENTER);
        Body14.setTextColor(Color.WHITE);
        Body14.setBackgroundColor(TextBackRoundColour);
        Body14.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body14);

        final TextView Body15 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - do it at least 5 days a week</font>";
        Body15.setText(Html.fromHtml(HTMLsourceString));
        Body15.setGravity(Gravity.CENTER);
        Body15.setTextColor(Color.WHITE);
        Body15.setBackgroundColor(TextBackRoundColour);
        Body15.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body15);

        final TextView Body16 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - if you get stuck on one level for two or three weeks don't worry, just keep going, but don't forget to push your limits. If you get 75% success rate on the task, move up to the next level. Don't try to stabilize a level you are already doing good. Going out of your comfort zone is the only way to grow</font>";
        Body16.setText(Html.fromHtml(HTMLsourceString));
        Body16.setGravity(Gravity.CENTER);
        Body16.setTextColor(Color.WHITE);
        Body16.setBackgroundColor(TextBackRoundColour);
        Body16.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body16);

        final TextView SpacesTextView12 = new TextView(this);
        SpacesTextView12.setText("\n");
        SpacesTextView12.setBackgroundColor(TextBackRoundColour);
        SpacesTextView12.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView12);

        final TextView Body17 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00>  <b><u>Real Life Benefits</u></b> </font>";
        Body17.setText(Html.fromHtml(HTMLsourceString));
        Body17.setGravity(Gravity.CENTER);
        Body17.setTextColor(Color.WHITE);
        Body17.setBackgroundColor(TextBackRoundColour);
        Body17.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body17);

        final TextView SpacesTextView13 = new TextView(this);
        SpacesTextView13.setText("\n");
        SpacesTextView13.setBackgroundColor(TextBackRoundColour);
        SpacesTextView13.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView13);

        final TextView Body18 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> A lot of people report numerous benefits after doing the N-Back Task, such as:</font>";
        Body18.setText(Html.fromHtml(HTMLsourceString));
        Body18.setGravity(Gravity.CENTER);
        Body18.setTextColor(Color.WHITE);
        Body18.setBackgroundColor(TextBackRoundColour);
        Body18.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body18);

        final TextView Body19 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - easier to get a hold of a discussion</font>";
        Body19.setText(Html.fromHtml(HTMLsourceString));
        Body19.setGravity(Gravity.CENTER);
        Body19.setTextColor(Color.WHITE);
        Body19.setBackgroundColor(TextBackRoundColour);
        Body19.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body19);

        final TextView Body20 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - better verbal fluency</font>";
        Body20.setText(Html.fromHtml(HTMLsourceString));
        Body20.setGravity(Gravity.CENTER);
        Body20.setTextColor(Color.WHITE);
        Body20.setBackgroundColor(TextBackRoundColour);
        Body20.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body20);

        final TextView Body21 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - faster reading with better understanding</font>";
        Body21.setText(Html.fromHtml(HTMLsourceString));
        Body21.setGravity(Gravity.CENTER);
        Body21.setTextColor(Color.WHITE);
        Body21.setBackgroundColor(TextBackRoundColour);
        Body21.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body21);

        final TextView Body22 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - better concentration and focus</font>";
        Body22.setText(Html.fromHtml(HTMLsourceString));
        Body22.setGravity(Gravity.CENTER);
        Body22.setTextColor(Color.WHITE);
        Body22.setBackgroundColor(TextBackRoundColour);
        Body22.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body22);

        final TextView Body23 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - better dream recall</font>";
        Body23.setText(Html.fromHtml(HTMLsourceString));
        Body23.setGravity(Gravity.CENTER);
        Body23.setTextColor(Color.WHITE);
        Body23.setBackgroundColor(TextBackRoundColour);
        Body23.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body23);

        final TextView Body24 = new TextView(this);
        HTMLsourceString = "<font color=#00FF00> - improvements in piano playing</font>";
        Body24.setText(Html.fromHtml(HTMLsourceString));
        Body24.setGravity(Gravity.CENTER);
        Body24.setTextColor(Color.WHITE);
        Body24.setBackgroundColor(TextBackRoundColour);
        Body24.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body24);

        final TextView SpacesTextView14 = new TextView(this);
        SpacesTextView14.setText("\n\n");
        SpacesTextView14.setBackgroundColor(TextBackRoundColour);
        SpacesTextView14.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView14);

        final TextView Body25 = new TextView(this);
        Body25.setAutoLinkMask(15);
        HTMLsourceString = "<font color=#00FF00>  <b><u>ManualMode vs PlayMode</u></b> </font>";
        Body25.setText(Html.fromHtml(HTMLsourceString));
        Body25.setGravity(Gravity.CENTER);
        Body25.setTextColor(Color.WHITE);
        Body25.setBackgroundColor(TextBackRoundColour);
        Body25.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body25);

        final TextView SpacesTextView141 = new TextView(this);
        SpacesTextView141.setText("\n\n");
        SpacesTextView141.setBackgroundColor(TextBackRoundColour);
        SpacesTextView141.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView141);

        final TextView Body251 = new TextView(this);
        Body251.setAutoLinkMask(15);
        HTMLsourceString = "<font color=#00FF00>  In ManualMode you are able to set the nBack number and the area size used in training. Try to catch all Achievements :) In PlayMode you play in 3x3 area and start from nBack 1. If you reach 50% score or more twice using the same nBack number game will increase the nBack. Results below 50% will end up to 'GAME OVER' </font>";
        Body251.setText(Html.fromHtml(HTMLsourceString));
        Body251.setGravity(Gravity.CENTER);
        Body251.setTextColor(Color.WHITE);
        Body251.setBackgroundColor(TextBackRoundColour);
        Body251.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body251);


        final TextView SpacesTextView15 = new TextView(this);
        SpacesTextView15.setText("\n\n");
        SpacesTextView15.setBackgroundColor(TextBackRoundColour);
        SpacesTextView15.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView15);

        final TextView Body26 = new TextView(this);
        Body26.setAutoLinkMask(15);
        HTMLsourceString = "<font color=#00FF00>  <b>Contact developer: pete.panula@gmail.com</b> </font>";
        Body26.setText(Html.fromHtml(HTMLsourceString));
        Body26.setGravity(Gravity.CENTER);
        Body26.setTextColor(Color.WHITE);
        Body26.setBackgroundColor(TextBackRoundColour);
        Body26.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeHeader);
        ll.addView(Body26);

        final TextView SpacesTextView16 = new TextView(this);
        SpacesTextView16.setText("\n\n");
        SpacesTextView16.setBackgroundColor(TextBackRoundColour);
        SpacesTextView16.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSizeSpace);
        ll.addView(SpacesTextView16);






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
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "InfoActivity onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "InfoActivity onPause...");
    }

    @Override
    public void onStart() {
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "InfoActivity onStart...");
        super.onStart();
    }

    @Override
    public void onStop() {
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "InfoActivity onStop...");
        super.onStop();
    }
}
