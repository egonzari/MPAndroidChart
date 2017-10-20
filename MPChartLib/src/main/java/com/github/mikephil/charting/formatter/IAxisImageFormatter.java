package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.SeatRadarChartAxis;

/**
 * Created by Philipp Jahoda on 20/09/15.
 * Custom formatter interface that allows formatting of
 * axis labels before they are being drawn.
 */
public interface IAxisImageFormatter {

  /**
   * Called when a value from an axis is to be formatted
   * before being drawn. For performance reasons, avoid excessive calculations
   * and memory allocations inside this method.
   *
   * @param value the value to be formatted
   * @param axis the axis the value belongs to
   */
  SeatRadarChartAxis getImage(float value, AxisBase axis);

  SeatRadarChartAxis[] getParameters();
}
