package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;

/**
 * Edu-MPAndroidChart
 * com.github.mikephil.charting.data
 * SeatRadarChartAxis
 */

public class SeatRadarChartAxis {

  private Drawable drawable;

  private float drawX;

  private float drawY;

  private String description = "This is an Axis dude!";

  public SeatRadarChartAxis() {
  }

  public SeatRadarChartAxis(Drawable drawable) {
    this.drawable = drawable;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
