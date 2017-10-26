package com.xxmassdeveloper.mpchartexample;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.data.SeatRadarChartAxis;
import com.github.mikephil.charting.formatter.IAxisImageFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.xxmassdeveloper.mpchartexample.custom.SeatRadarMarkerView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class RadarChartActivitry extends DemoBase {

  private RadarChart mChart;

  private static final int NUM_PARAMETERS = 6;

  private SeatRadarChartAxis[] axis = new SeatRadarChartAxis[NUM_PARAMETERS];

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_radarchart_noseekbar);

    TextView tv = (TextView) findViewById(R.id.textView);
    tv.setTypeface(mTfLight);
    tv.setTextColor(Color.WHITE);
    tv.setBackgroundColor(Color.rgb(60, 65, 82));

    mChart = (RadarChart) findViewById(R.id.chart1);
    mChart.setBackgroundColor(Color.rgb(25, 25, 25));

    mChart.getDescription().setEnabled(false);

    mChart.setWebLineWidth(1f);
    mChart.setWebColor(0xFF585858);
    mChart.setWebLineWidthInner(1f);
    mChart.setCircleColors( new int[] { 0xff1a1a1a, 0xff222222, 0xff191919, 0xff222222 });
    mChart.setPositionsCircleColors(new float[] { 0.3f, 0.6f, 0.7f, 0.9f });
    mChart.setWebColorInner(0xFF585858);
    mChart.setWebAlpha(100);
    mChart.setImageDrawMode(true);
    mChart.setRotationEnabled(false);
    mChart.setNumCircles(4);

    // create a custom MarkerView (extend MarkerView) and specify the layout
    // to use for it
    MarkerView mv = new SeatRadarMarkerView(this, R.layout.seat_radar_markerview);
    mv.setChartView(mChart); // For bounds control
    mChart.setMarker(mv); // Set the marker to the chart

    setData();

    mChart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad,
        Easing.EasingOption.EaseInOutQuad);

    XAxis xAxis = mChart.getXAxis();
    xAxis.setTypeface(mTfLight);
    xAxis.setTextSize(9f);
    xAxis.setYOffset(0f);
    xAxis.setXOffset(0f);
    if (mChart.isImageDrawMode()) {
      xAxis.setImageFormatter(new IAxisImageFormatter() {

        @Override public SeatRadarChartAxis getImage(int index) {
          return axis[index];
        }

        public SeatRadarChartAxis[] getParameters() {
          return axis;
        }
      });
    } else {
      xAxis.setValueFormatter(new IAxisValueFormatter() {

        private String[] mActivities =
            new String[] { "Burger", "Steak", "Salad", "Pasta", "Pizza", "Tuna" };

        @Override public String getFormattedValue(float value, AxisBase axis) {
          return mActivities[(int) value % mActivities.length];
        }
      });
    }
    xAxis.setTextColor(Color.WHITE);

    YAxis yAxis = mChart.getYAxis();
    yAxis.setTypeface(mTfLight);
    yAxis.setLabelCount(4, false);
    yAxis.setTextSize(9f);
    yAxis.setAxisMinimum(0f);
    yAxis.setAxisMaximum(80f);
    yAxis.setDrawLabels(false);

    Legend l = mChart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    l.setDrawInside(false);
    l.setTypeface(mTfLight);
    l.setXEntrySpace(7f);
    l.setYEntrySpace(5f);
    l.setTextColor(Color.WHITE);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.radar, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.actionToggleValues: {
        for (IDataSet<?> set : mChart.getData().getDataSets())
          set.setDrawValues(!set.isDrawValuesEnabled());

        mChart.invalidate();
        break;
      }
      case R.id.actionToggleHighlight: {
        if (mChart.getData() != null) {
          mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
          mChart.invalidate();
        }
        break;
      }
      case R.id.actionToggleRotate: {
        if (mChart.isRotationEnabled()) {
          mChart.setRotationEnabled(false);
        } else {
          mChart.setRotationEnabled(true);
        }
        mChart.invalidate();
        break;
      }
      case R.id.actionToggleFilled: {

        ArrayList<IRadarDataSet> sets = (ArrayList<IRadarDataSet>) mChart.getData().getDataSets();

        for (IRadarDataSet set : sets) {
          if (set.isDrawFilledEnabled()) {
            set.setDrawFilled(false);
          } else {
            set.setDrawFilled(true);
          }
        }
        mChart.invalidate();
        break;
      }
      case R.id.actionToggleHighlightCircle: {

        ArrayList<IRadarDataSet> sets = (ArrayList<IRadarDataSet>) mChart.getData().getDataSets();

        for (IRadarDataSet set : sets) {
          set.setDrawHighlightCircleEnabled(!set.isDrawHighlightCircleEnabled());
        }
        mChart.invalidate();
        break;
      }
      case R.id.actionSave: {
        if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
          Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT).show();
        }
        break;
      }
      case R.id.actionToggleXLabels: {
        mChart.getXAxis().setEnabled(!mChart.getXAxis().isEnabled());
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        break;
      }
      case R.id.actionToggleYLabels: {

        mChart.getYAxis().setEnabled(!mChart.getYAxis().isEnabled());
        mChart.invalidate();
        break;
      }
      case R.id.animateX: {
        mChart.animateX(1400);
        break;
      }
      case R.id.animateY: {
        mChart.animateY(1400);
        break;
      }
      case R.id.animateXY: {
        mChart.animateXY(1400, 1400);
        break;
      }
      case R.id.actionToggleSpin: {
        mChart.spin(2000, mChart.getRotationAngle(), mChart.getRotationAngle() + 360,
            Easing.EasingOption.EaseInCubic);
        break;
      }
    }
    return true;
  }

  public void setData() {

    Point size = new Point();
    WindowManager w = getWindowManager();
    w.getDefaultDisplay().getSize(size);
    float max = 100;
    float min = 0;

    ArrayList<RadarEntry> entries1 = new ArrayList<>();
    ArrayList<RadarEntry> entries2 = new ArrayList<>();

    // NOTE: The order of the entries when being added to the entries array determines their position around the center of
    // the chart.
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    for (int i = 0; i < NUM_PARAMETERS; i++) {
      float val1 = new Random().nextFloat() * (max - min) + min;
      entries1.add(new RadarEntry(val1));
      float val2 = new Random().nextFloat() * (max - min) + min;
      addAxiToList(i, "Axis " + i, val2,
          ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher));
      entries2.add(new RadarEntry(val2));
    }

    RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
    set1.setColor(Color.rgb(201, 201, 201));
    set1.setFillColor(Color.rgb(201, 201, 201));
    set1.setDrawFilled(true);
    set1.setFillAlpha(180);
    set1.setLineWidth(2f);
    set1.setDrawHighlightCircleEnabled(true);
    set1.setDrawHighlightIndicators(false);

    RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
    set2.setColor(Color.rgb(220, 48, 58));
    set2.setFillColor(Color.rgb(220, 48, 58));
    set2.setDrawFilled(true);
    set2.setFillAlpha(180);
    set2.setLineWidth(2f);
    set2.setDrawHighlightCircleEnabled(true);
    set2.setDrawHighlightIndicators(false);

    ArrayList<IRadarDataSet> sets = new ArrayList<>();
    sets.add(set1);
    sets.add(set2);

    RadarData data = new RadarData(sets);
    data.setValueTypeface(mTfLight);
    data.setValueTextSize(8f);
    data.setDrawValues(false);
    data.setValueTextColor(Color.WHITE);

    mChart.setData(data);
    mChart.invalidate();
  }

  private void addAxiToList(int position, String name, float value, Drawable drawable) {
    axis[position] = SeatRadarChartAxis.createInitialItem(name, value, drawable);
  }
}
