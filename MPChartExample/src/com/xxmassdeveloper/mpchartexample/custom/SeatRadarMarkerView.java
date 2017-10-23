package com.xxmassdeveloper.mpchartexample.custom;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.SeatRadarChartAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.xxmassdeveloper.mpchartexample.R;

/**
 * Custom implementation of the MarkerView.
 */
public class SeatRadarMarkerView extends MarkerView {

  private TextView tvAxisPercent;
  private TextView tvAxisName;

  public SeatRadarMarkerView(Context context, int layoutResource) {
    super(context, layoutResource);
    tvAxisPercent = (TextView) findViewById(R.id.tvAxisPercent);
    tvAxisName = (TextView) findViewById(R.id.tvAxisName);
  }

  // callbacks everytime the MarkerView is redrawn, can be used to update the
  // content (user-interface)

  @Override public void refreshContent(SeatRadarChartAxis axis) {
    tvAxisName.setText(axis.getName());
    tvAxisPercent.setText(axis.getPercent());
    super.refreshContent(null, null);
  }

  @Override public MPPointF getOffset() {
    return new MPPointF(-(getWidth() / 2), -getHeight() - 10);
  }
}
