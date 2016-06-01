package com.braindualxback;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
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

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ChartActivity extends AppCompatActivity {

    public static String[] Hiscores_game;
    public static String[] Players_game;
    public static long[] Dates_game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (MainActivity.ENABLE_LOGS) Log.d("Pete", "ChartActivity onCreate...");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_chart);

        DatabaseHandler db = new DatabaseHandler(this);
        Object[] arrayObjects_game = db.getAll_game_for_chart();

        if(db.getDBsize_game()<3){
            LinearLayout ll = (LinearLayout) findViewById(R.id.chart);
            ll.setGravity(Gravity.CENTER);

            String HTMLsourceString;
            int TextBackRoundColour = 0xaa000000;
            int FontSize = 20;

            final TextView ManualModeHeader = new TextView(this);
            HTMLsourceString = "<font color=#00FF00><b> Nothing to show here yet. Do some training in PlayMode! </b></font>";
            ManualModeHeader.setText(Html.fromHtml(HTMLsourceString));
            ManualModeHeader.setGravity(Gravity.CENTER);
            ManualModeHeader.setTextColor(Color.WHITE);
            ManualModeHeader.setBackgroundColor(TextBackRoundColour);
            ManualModeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontSize);
            // add the textview to the linearlayout
            ll.addView(ManualModeHeader);
        }else {
            Hiscores_game = new String[db.getDBsize_game()];
            Players_game = new String[db.getDBsize_game()];
            Dates_game = new long[db.getDBsize_game()];

            Players_game = (String[]) arrayObjects_game[0];
            Dates_game = (long[]) arrayObjects_game[1];
            Hiscores_game = (String[]) arrayObjects_game[2];

            int biggest_value = 0;

            for (String aHiscores_game : Hiscores_game) {
                if (Integer.parseInt(aHiscores_game) > biggest_value) {
                    biggest_value = Integer.parseInt(aHiscores_game);
                }
            }

            //XYSeries series = new XYSeries("Testi graafi");
            XYSeries series = new XYSeries("");
            XYMultipleSeriesDataset mseries = new XYMultipleSeriesDataset();

            // Now we create the renderer
            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(2);
            renderer.setColor(Color.RED);
            // Include low and max value
            renderer.setDisplayBoundingPoints(true);
            // we add point markers
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(3);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            //XYSeriesRenderer mRenderer = new XYSeriesRenderer();

            mRenderer.addSeriesRenderer(renderer);

            // We want to avoid black border
            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
            // Disable Pan on two axis
            mRenderer.setPanEnabled(true, false);
            mRenderer.setPanLimits(new double[]{0, Hiscores_game.length, 0, 0});

            mRenderer.setYAxisMax(biggest_value + 10);
            mRenderer.setYAxisMin(0);
            mRenderer.setShowGrid(false); // we show the grid
            mRenderer.setBarSpacing(1);
            mRenderer.setXAxisMin(-0.5);
            mRenderer.setInScroll(true);
            //mRenderer.setAxisTitleTextSize(50);
            mRenderer.setZoomEnabled(true, false);
            mRenderer.setChartTitle("PlayMode progress (X-Date,Y-Points)");
            mRenderer.setChartTitleTextSize(60);

            mRenderer.setAxisTitleTextSize(60);
            //mRenderer.setXTitle("Date");
            //mRenderer.setYTitle("Points");

            mRenderer.setLabelsTextSize(60);

            mRenderer.setXLabelsAngle(90);

            //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

            int hour = 0;
            for (int j = 0; j < Hiscores_game.length; j++) {
                if (MainActivity.ENABLE_LOGS)
                    Log.v("Pete", "add - Hiscores_game: " + Integer.parseInt(Hiscores_game[j]));
                series.add(hour++, Integer.parseInt(Hiscores_game[j]));
                series.addAnnotation(Hiscores_game[j], j, Integer.parseInt(Hiscores_game[j]) + 5);
                renderer.setAnnotationsTextSize(50);
                renderer.setAnnotationsColor(Color.RED);
                Date resultdate = new Date(Dates_game[j]);
                mRenderer.addXTextLabel(j, sdf.format(resultdate));
                mRenderer.setXLabels(0);
            }

            mseries.addSeries(series);

            //GraphicalView chartView = ChartFactory.getLineChartView(this, mseries, mRenderer);
            GraphicalView chartView = ChartFactory.getBarChartView(this, mseries, mRenderer, BarChart.Type.DEFAULT);

            LinearLayout ll = (LinearLayout) findViewById(R.id.chart);

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
