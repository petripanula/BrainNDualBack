package com.braindualxback;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class TimeHistoryActivity extends AppCompatActivity {

    public static int[] Time_game;
    public static String[] Players;
    public static String[] Dates_game;

    SecurePreferences preferences;
    int testint;
    int DbSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (MainActivity.ENABLE_LOGS) Log.d("Pete", "TimeHistoryActivity onCreate...");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_time_history);

        DatabaseHandler db = new DatabaseHandler(this);
        Object[] arrayObjects_game = db.getAll_times_for_chart();

        preferences = new SecurePreferences(this, "my-preferences", "SometopSecretKey1235", true);
        testint = Integer.parseInt(preferences.getIntString("testint"));

        if (MainActivity.ENABLE_LOGS) Log.v("Pete", "testint: " + testint);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float HeaderFont = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, metrics);
        float AxisFont = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, metrics);
        float AnnotatFont = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, metrics);

        DbSize = db.get_number_of_days();
        String HTMLsourceString = "<font color=#00FF00><b> Nothing to show here yet. Do some training using nBack > 1! </b></font>";
        boolean DoNotShowCraph;

        if(DbSize<3){
            HTMLsourceString = "<font color=#00FF00><b> Nothing to show here yet. Do some training using nBack > 1! </b></font>";
            DoNotShowCraph = true;
        }else{
            DoNotShowCraph = false;
        }

        if(testint!=666 && DbSize>=10){
            HTMLsourceString = "<font color=#00FF00><b> Buy Premium Subscription to show more progress! </b></font>";
            DoNotShowCraph = true;
        }

        if(DoNotShowCraph){
            LinearLayout ll = (LinearLayout) findViewById(R.id.charttime);
            ll.setGravity(Gravity.CENTER);


            int TextBackRoundColour = 0xaa000000;
            int FontSize = 20;

            if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_SMALL){
                FontSize = 16;
            }

            if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_NORMAL){
                FontSize = 16;
            }

            if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_LARGE){
                FontSize = 20;
            }

            if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE){
                FontSize = 22;
            }

            final TextView ManualModeHeader = new TextView(this);
            ManualModeHeader.setText(Html.fromHtml(HTMLsourceString));
            ManualModeHeader.setGravity(Gravity.CENTER);
            ManualModeHeader.setTextColor(Color.WHITE);
            ManualModeHeader.setBackgroundColor(TextBackRoundColour);
            ManualModeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            // add the textview to the linearlayout
            ll.addView(ManualModeHeader);
        }else {
            Players = new String[DbSize];
            Dates_game = new String[DbSize];
            Time_game = new int[DbSize];

            Players = (String[]) arrayObjects_game[0];
            Time_game = (int[]) arrayObjects_game[1];
            Dates_game = (String[]) arrayObjects_game[2];

            int biggest_value = 0;

            for(int l=0; l<DbSize; l++) {
                if (Time_game[l] > biggest_value) {
                    biggest_value = Time_game[l];
                }
            }

            XYSeries series = new XYSeries("");
            XYSeries series2 = new XYSeries("");
            XYMultipleSeriesDataset mseries = new XYMultipleSeriesDataset();

            // Now we create the renderer
            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(2);
            renderer.setColor(Color.RED);
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(3);
            renderer.setAnnotationsTextSize(AnnotatFont);
            renderer.setAnnotationsColor(Color.RED);

            // Now we create the renderer
            XYSeriesRenderer renderer2 = new XYSeriesRenderer();
            renderer2.setLineWidth(2);
            renderer2.setColor(Color.GREEN);
            renderer2.setDisplayBoundingPoints(true);
            renderer2.setPointStyle(PointStyle.CIRCLE);
            renderer2.setPointStrokeWidth(3);
            renderer2.setAnnotationsTextSize(AnnotatFont);
            renderer2.setAnnotationsColor(Color.GREEN);


            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);
            mRenderer.addSeriesRenderer(renderer2);

            // We want to avoid black border
            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
            // Disable Pan on two axis
            mRenderer.setPanEnabled(true, false);
            mRenderer.setPanLimits(new double[]{0, DbSize, 0, 0});
            mRenderer.setShowLegend(false);
            mRenderer.setYAxisMax(biggest_value + 5);
            mRenderer.setYAxisMin(0);
            mRenderer.setShowGrid(false); // we show the grid
            mRenderer.setBarSpacing(0);
            mRenderer.setXAxisMin(-0.5);
            mRenderer.setInScroll(true);
            mRenderer.setZoomEnabled(true, false);
            mRenderer.setChartTitle("Daily Effective Playtime History In Minutes (nBack>1)");
            mRenderer.setChartTitleTextSize(HeaderFont);
            mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
            mRenderer.setAxisTitleTextSize(AxisFont);
            mRenderer.setLabelsTextSize(AxisFont);

            mRenderer.setBarWidth(100);

            if(DbSize>5)
                mRenderer.setXLabelsAngle(90);

            int Top = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50, metrics);
            int Bottom = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, metrics);
            int Left = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 40, metrics);
            int Right = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100, metrics);

            mRenderer.setMargins(new int[] { Top, Left, Bottom, Right });

            int point = 0;
            for (int j = 0; j < DbSize; j++) {
                if (MainActivity.ENABLE_LOGS)
                    Log.v("Pete", "add - time: " + Time_game[j]);

                if(Time_game[j]<20) {
                    series.add(point++, Time_game[j]);
                    series.addAnnotation(Integer.toString(Time_game[j]), j,  Time_game[j] + 1);
                }
                else {
                    series2.add(point++, Time_game[j]);
                    series2.addAnnotation(Integer.toString(Time_game[j]), j,  Time_game[j] + 1);
                }

                String ModDate = Dates_game[j].substring(0, Dates_game[j].length() - 5);
                mRenderer.addXTextLabel(j, ModDate);
                mRenderer.setXLabels(0);
            }

            mseries.addSeries(series);
            mseries.addSeries(series2);

            GraphicalView chartView = ChartFactory.getBarChartView(this, mseries, mRenderer, BarChart.Type.STACKED);

            LinearLayout ll = (LinearLayout) findViewById(R.id.charttime);

            if (ll != null) {
                ll.addView(chartView, 0);
            }
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
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "TimeHistoryActivity onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(MainActivity.ENABLE_LOGS) Log.v("Pete", "TimeHistoryActivity onPause...");
    }

}
