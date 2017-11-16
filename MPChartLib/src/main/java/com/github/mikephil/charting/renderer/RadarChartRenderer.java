package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.CircleRadarChartAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class RadarChartRenderer extends LineRadarRenderer {

  protected RadarChart mChart;

  /**
   * paint for drawing the web
   */
  private Paint mWebPaint;
  private Paint mHighlightCirclePaint;

  public RadarChartRenderer(RadarChart chart, ChartAnimator animator,
      ViewPortHandler viewPortHandler) {
    super(animator, viewPortHandler);
    mChart = chart;

    mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mHighlightPaint.setStyle(Paint.Style.STROKE);
    mHighlightPaint.setStrokeWidth(2f);
    mHighlightPaint.setColor(Color.rgb(255, 187, 115));

    mWebPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mWebPaint.setStyle(Paint.Style.STROKE);

    mHighlightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  }

  public Paint getWebPaint() {
    return mWebPaint;
  }

  @Override public void initBuffers() {
    // TODO Auto-generated method stub

  }

  @Override public void drawData(Canvas c) {

    RadarData radarData = mChart.getData();

    int mostEntries = radarData.getMaxEntryCountSet().getEntryCount();

    for (IRadarDataSet set : radarData.getDataSets()) {

      if (set.isVisible()) {
        draw3DDataSet(c, set, mostEntries);
      }
    }
  }

  protected void draw3DDataSet(Canvas c, IRadarDataSet dataSet, int mostEntries) {

    float phaseX = mAnimator.getPhaseX();
    float sliceangle = mChart.getSliceAngle();

    MPPointF center = mChart.getCenterOffsets();
    MPPointF pOut = MPPointF.getInstance(0, 0);

    for (int j = 0; j < dataSet.getEntryCount(); j++) {
      Path surface = new Path();
      surface.reset();
      surface.moveTo(center.x, center.y);
      mRenderPaint.setColor(dataSet.getColor(j));

      RadarEntry e1 = dataSet.getEntryForIndex(j);
      RadarEntry e2;
      int indexNextEntry = j + 1;
      if (j + 1 >= dataSet.getEntryCount()) {
        indexNextEntry = 0;
        e2 = dataSet.getEntryForIndex(0);
      } else {
        e2 = dataSet.getEntryForIndex(j + 1);
      }

      float screenDistance = getMaxDistanceCenterPoint();
      float dist1 = (e1.getY() * screenDistance) / 100;
      float dist2 = (e2.getY() * screenDistance) / 100;

      Utils.getPosition(center, dist1, sliceangle * j * phaseX + mChart.getRotationAngle(), pOut);
      surface.lineTo(pOut.x, pOut.y);
      surface.moveTo(pOut.x, pOut.y);
      Utils.getPosition(center, dist2,
          sliceangle * indexNextEntry * phaseX + mChart.getRotationAngle(), pOut);
      surface.lineTo(pOut.x, pOut.y);
      surface.lineTo(center.x, center.y);

      if (dataSet.getFillColorArray() != null && dataSet.getFillColorArray().length > j) {
        drawFilledPath(c, surface, dataSet.getFillColorArray()[j], dataSet.getFillAlpha());
      } else {
        drawFilledPath(c, surface, dataSet.getFillColor(), dataSet.getFillAlpha());
      }
      surface.close();
    }

    MPPointF.recycleInstance(center);
    MPPointF.recycleInstance(pOut);
  }

  protected void drawFilledPath(Canvas c, Path filledPath, int fillColor, int fillAlpha) {

    if (clipPathSupported()) {

      int save = c.save();

      c.clipPath(filledPath);

      c.drawColor(fillColor);
      c.restoreToCount(save);
    } else {

      // save
      Paint.Style previous = mRenderPaint.getStyle();
      int previousColor = mRenderPaint.getColor();

      // set
      mRenderPaint.setStyle(Paint.Style.FILL);
      mRenderPaint.setColor(fillColor);

      c.drawPath(filledPath, mRenderPaint);

      // restore
      mRenderPaint.setColor(previousColor);
      mRenderPaint.setStyle(previous);
    }
  }

  @Override public void drawValues(Canvas c) {

    float phaseX = mAnimator.getPhaseX();
    float phaseY = mAnimator.getPhaseY();

    float sliceangle = mChart.getSliceAngle();

    // calculate the factor that is needed for transforming the value to
    // pixels
    float factor = mChart.getFactor();

    MPPointF center = mChart.getCenterOffsets();
    MPPointF pOut = MPPointF.getInstance(0, 0);
    MPPointF pIcon = MPPointF.getInstance(0, 0);

    float yoffset = Utils.convertDpToPixel(5f);

    for (int i = 0; i < mChart.getData().getDataSetCount(); i++) {

      IRadarDataSet dataSet = mChart.getData().getDataSetByIndex(i);

      if (!shouldDrawValues(dataSet)) {
        continue;
      }

      // apply the text-styling defined by the DataSet
      applyValueTextStyle(dataSet);

      MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
      iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
      iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

      for (int j = 0; j < dataSet.getEntryCount(); j++) {

        RadarEntry entry = dataSet.getEntryForIndex(j);

        Utils.getPosition(center, (entry.getY() - mChart.getYChartMin()) * factor * phaseY,
            sliceangle * j * phaseX + mChart.getRotationAngle(), pOut);

        if (dataSet.isDrawValuesEnabled()) {
          drawValue(c, dataSet.getValueFormatter(), entry.getY(), entry, i, pOut.x,
              pOut.y - yoffset, dataSet.getValueTextColor(j));
        }

        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

          Drawable icon = entry.getIcon();

          Utils.getPosition(center, (entry.getY()) * factor * phaseY + iconsOffset.y,
              sliceangle * j * phaseX + mChart.getRotationAngle(), pIcon);

          //noinspection SuspiciousNameCombination
          pIcon.y += iconsOffset.x;

          Utils.drawImage(c, icon, (int) pIcon.x, (int) pIcon.y, icon.getIntrinsicWidth(),
              icon.getIntrinsicHeight());
        }
      }

      MPPointF.recycleInstance(iconsOffset);
    }

    MPPointF.recycleInstance(center);
    MPPointF.recycleInstance(pOut);
    MPPointF.recycleInstance(pIcon);
  }

  @Override public void drawExtras(Canvas c) {
    drawWeb(c);
  }

  private float getMaxDistanceCenterPoint() {
    float maxDistanceCenterPoint;
    if (mChart.isAndroidAutoScreen()) {
      maxDistanceCenterPoint = mChart.getMinimumWidth() * 0.26f;
    } else {
      maxDistanceCenterPoint = Utils.getScreenWidth();
    }
    return maxDistanceCenterPoint;
  }

  protected void drawWeb(Canvas canvas) {

    float sliceangle = mChart.getSliceAngle();

    // calculate the factor that is needed for transforming the value to
    // pixels
    float rotationangle = mChart.getRotationAngle();
    MPPointF center = mChart.getCenterOffsets();

    // draw the inner-web
    mWebPaint.setStrokeWidth(mChart.getWebLineWidthInner());
    mWebPaint.setColor(mChart.getWebColorInner());
    mWebPaint.setAlpha(mChart.getWebAlpha());

    //-------------------------------
    //Circles that represents the radar using labelCount
    int numCircles = mChart.getNumCircles();

    float maxDistanceCenterPoint = getMaxDistanceCenterPoint();
    int spaceCircle = 120;
    if (mChart.isAndroidAutoScreen()) {
      spaceCircle = 30;
    }

    for (int j = 0; j < numCircles; j++) {
      float circleRadius = maxDistanceCenterPoint;
      circleRadius -= j * spaceCircle;
      if (j == 0 && mChart.getCircleColors() != null && mChart.getPositionsCircleColors() != null) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        int[] colors = mChart.getCircleColors();
        float[] positions = mChart.getPositionsCircleColors();
        paint.setShader(new RadialGradient(center.x, center.y, circleRadius, colors, positions,
            Shader.TileMode.CLAMP));
        canvas.drawCircle(center.x, center.y, circleRadius, paint);
      }
      canvas.drawCircle(center.x, center.y, circleRadius, mWebPaint);
    }
    //-------------------------------

    // draw the web lines that come from the center
    mWebPaint.setStrokeWidth(mChart.getWebLineWidth());
    mWebPaint.setColor(mChart.getWebColor());
    mWebPaint.setAlpha(mChart.getWebAlpha());
    final int xIncrements = 1 + mChart.getSkipWebLineCount();
    int maxEntryCount = mChart.getData().getMaxEntryCountSet().getEntryCount();
    MPPointF p = MPPointF.getInstance(0, 0);
    for (int i = 0; i < maxEntryCount; i += xIncrements) {
      Utils.getPosition(center, maxDistanceCenterPoint, sliceangle * i + rotationangle, p);
      canvas.drawLine(center.x, center.y, p.x, p.y, mWebPaint);
      //Draw image parameter
      CircleRadarChartAxis circleAxis = mChart.getXAxis().getImageFormatter().getImage(i);
      Drawable drawable = circleAxis.getDrawable();
      int drawableWidth = drawable.getIntrinsicWidth();
      int drawableHeight = drawable.getIntrinsicHeight();
      Utils.setCircleMarketPosition(circleAxis, center, p);
      if (mChart.isAndroidAutoScreen()) {
        Utils.getPosition(center, maxDistanceCenterPoint + drawableWidth / 1.3f,
            sliceangle * i + rotationangle, p);
      } else {
        Utils.getPosition(center, maxDistanceCenterPoint + (drawableWidth + 30) / 1.5f,
            sliceangle * i + rotationangle, p);
      }
      circleAxis.setDrawX(p.x);
      circleAxis.setDrawY(p.y);
      Utils.drawImage(canvas, drawable, (int) p.x, (int) p.y, drawableWidth, drawableHeight);
    }
    MPPointF.recycleInstance(p);
  }

  @Override public void drawHighlighted(Canvas c, Highlight[] indices) {

    float sliceangle = mChart.getSliceAngle();

    // calculate the factor that is needed for transforming the value to
    // pixels
    float factor = mChart.getFactor();

    MPPointF center = mChart.getCenterOffsets();
    MPPointF pOut = MPPointF.getInstance(0, 0);

    RadarData radarData = mChart.getData();

    for (Highlight high : indices) {

      IRadarDataSet set = radarData.getDataSetByIndex(high.getDataSetIndex());

      if (set == null || !set.isHighlightEnabled()) {
        continue;
      }

      RadarEntry e = set.getEntryForIndex((int) high.getX());

      if (!isInBoundsX(e, set)) {
        continue;
      }

      float y = (e.getY() - mChart.getYChartMin());

      Utils.getPosition(center, y * factor * mAnimator.getPhaseY(),
          sliceangle * high.getX() * mAnimator.getPhaseX() + mChart.getRotationAngle(), pOut);

      high.setDraw(pOut.x, pOut.y);

      // draw the lines
      drawHighlightLines(c, pOut.x, pOut.y, set);

      if (set.isDrawHighlightCircleEnabled()) {

        if (!Float.isNaN(pOut.x) && !Float.isNaN(pOut.y)) {

          int strokeColor = set.getHighlightCircleStrokeColor();
          if (strokeColor == ColorTemplate.COLOR_NONE) {
            strokeColor = set.getColor(0);
          }

          if (set.getHighlightCircleStrokeAlpha() < 255) {
            strokeColor =
                ColorTemplate.colorWithAlpha(strokeColor, set.getHighlightCircleStrokeAlpha());
          }

          drawHighlightCircle(c, pOut, set.getHighlightCircleInnerRadius(),
              set.getHighlightCircleOuterRadius(), set.getHighlightCircleFillColor(), strokeColor,
              set.getHighlightCircleStrokeWidth());
        }
      }
    }

    MPPointF.recycleInstance(center);
    MPPointF.recycleInstance(pOut);
  }

  public void drawHighlightCircle(Canvas c, MPPointF point, float innerRadius, float outerRadius,
      int fillColor, int strokeColor, float strokeWidth) {
    c.save();

    outerRadius = Utils.convertDpToPixel(outerRadius);
    innerRadius = Utils.convertDpToPixel(innerRadius);

    if (fillColor != ColorTemplate.COLOR_NONE) {
      Path p = new Path();
      p.reset();
      p.addCircle(point.x, point.y, outerRadius, Path.Direction.CW);
      if (innerRadius > 0.f) {
        p.addCircle(point.x, point.y, innerRadius, Path.Direction.CCW);
      }
      mHighlightCirclePaint.setColor(fillColor);
      mHighlightCirclePaint.setStyle(Paint.Style.FILL);
      c.drawPath(p, mHighlightCirclePaint);
    }

    if (strokeColor != ColorTemplate.COLOR_NONE) {
      mHighlightCirclePaint.setColor(strokeColor);
      mHighlightCirclePaint.setStyle(Paint.Style.STROKE);
      mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(strokeWidth));
      c.drawCircle(point.x, point.y, outerRadius, mHighlightCirclePaint);
    }

    c.restore();
  }
}
