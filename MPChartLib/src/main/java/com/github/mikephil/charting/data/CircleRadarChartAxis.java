package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import java.text.DecimalFormat;

/**
 * Edu-MPAndroidChart
 * com.github.mikephil.charting.data
 * CircleRadarChartAxis
 */

public class CircleRadarChartAxis {

  private Drawable drawable;

  private float drawX;

  private float drawY;

  private float drawXMarker;

  private float drawYMarker;

  private String name;

  private String percent;

  public CircleRadarChartAxis() {
  }

  public CircleRadarChartAxis(Drawable drawable) {
    this.drawable = drawable;
  }

  public CircleRadarChartAxis(Drawable drawable, String name) {
    this.drawable = drawable;
    this.name = name;
  }

  public CircleRadarChartAxis(Drawable drawable, String name, String percent) {
    this.drawable = drawable;
    this.name = name;
    this.percent = percent;
  }

  public Drawable getDrawable() {
    return drawable;
  }

  public void setDrawable(Drawable drawable) {
    this.drawable = drawable;
  }

  public float getDrawX() {
    return drawX;
  }

  public void setDrawX(float drawX) {
    this.drawX = drawX;
  }

  public float getDrawY() {
    return drawY;
  }

  public void setDrawY(float drawY) {
    this.drawY = drawY;
  }

  public float getDrawXMarker() {
    return drawXMarker;
  }

  public void setDrawXMarker(float drawXMarker) {
    this.drawXMarker = drawXMarker;
  }

  public float getDrawYMarker() {
    return drawYMarker;
  }

  public void setDrawYMarker(float drawYMarker) {
    this.drawYMarker = drawYMarker;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPercent() {
    return percent;
  }

  public void setPercent(String percent) {
    this.percent = percent;
  }

  public int getWidth() {
    return getDrawable().getIntrinsicWidth();
  }

  public int getHeight() {
    return getDrawable().getIntrinsicHeight();
  }

  public static CircleRadarChartAxis createInitialItem(String name, float value,
      Drawable drawable) {
    CircleRadarChartAxis radarChartAxis = new CircleRadarChartAxis();

    radarChartAxis.setName(name);

    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    radarChartAxis.setPercent(df.format(value) + "/100");

    radarChartAxis.setDrawable(drawable);
    return radarChartAxis;
  }
}
