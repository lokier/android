package com.rdm.common.ui.demo.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.rdm.common.util.DeviceUtils;


public class AbilityDetailView extends View
{
    private static final String TAG = "AbilityDetailView";


    private static final Paint ABILITY_PATH_PAINT;

    private static final int ABILITY_COLOR = 0xFFee6049;

    private static final float MAX_ABILITY_VALUE = 100.0f;


    /***
     * 多边形的初始旋转角度
     */
    private static int INIT_ROATE_VALUE = 0;

    private static float LABLE_TEXT_SIZE = 10f;
    private static float LABLE_TEXT_VALUE_SIZE = 12f;


    private static final int LABEL_COLOR = 0xFF9A9A9A;
    private static final int LABEL_COLOR_VALUE = 0xFF494949;

    private static final int LABEL_LINE_PADDING_DP = 5;

    private static float LABELS_PADDING;

    private static final TextPaint LABEL_PAINT;
    private static final TextPaint LABEL_PAINT_VALUE;

    static
    {
        ABILITY_PATH_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        ABILITY_PATH_PAINT.setColor(ABILITY_COLOR);
        ABILITY_PATH_PAINT.setStyle(Paint.Style.STROKE);

        LABEL_PAINT = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        LABEL_PAINT.setColor(LABEL_COLOR);

        LABEL_PAINT_VALUE = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        LABEL_PAINT_VALUE.setColor(LABEL_COLOR_VALUE);
        LABEL_PAINT_VALUE.setFakeBoldText(true);
       // LABEL_PAINT.setText;
    }

    private float ability_line_width;

    private float[] data;
    private String[] ABILITY_LABELS ; // = new String[]{"胜率", "S8连胜", "MVP", "场次", "战斗力"};
    private String[] mValueList ;//= new String[]{"80%", "34", "56", "565", "23434"};


    public AbilityDetailView(Context context)
    {
        super(context);
    }

    public AbilityDetailView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public AbilityDetailView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /***
     * scale的长度为5,值为1,15之间；
     *@param  name 顺序为： "胜率", "S8连胜", "MVP", "场次" ,"战斗力"
     *@param  name 顺序为： "胜率", "S8连胜", "MVP", "场次" ,"战斗力"
     * @param scale   顺序为： "胜率", "S8连胜", "MVP", "场次" ,"战斗力"
     */
    public void setData (String[] name, String value[], float[] scale)
    {
        float[] v = new float[scale.length];
        for(int i = 0 ;i < scale.length;i++){
            //转为0到100之间；
            v[i] = scale[i] * 100f / 15f;
            if(v[i] > 100f){
                v[i] = 100f;
            }
        }
        ABILITY_LABELS = name;
        mValueList = value;
        this.data = v;
      //  TLog.d(TAG, "setData " + Arrays.toString(data));
        invalidate();
    }

    @Override
    protected void onDraw (Canvas canvas)
    {
        if (ABILITY_LABELS == null || data == null || mValueList == null) {
            return;
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ability_line_width = 1.5f * metrics.density;
        LABELS_PADDING = 10 * metrics.density;

        float textSize = LABLE_TEXT_VALUE_SIZE * metrics.scaledDensity;

        Rect labelBounds = new Rect();
        LABEL_PAINT_VALUE.setTextSize(textSize);
        LABEL_PAINT_VALUE.getTextBounds(ABILITY_LABELS[0], 0, ABILITY_LABELS[0].length(), labelBounds);

        float maxRadius = (getHeight() - 2 * labelBounds.height() - 3f * textSize) / 2.0f;

        drawGridBg(maxRadius, canvas);

        drawLabels(maxRadius, canvas);

        if (data == null || data.length < ABILITY_LABELS.length) {
           // data = new float[]{100,60,50,40,30,20};
            return;
        }

        drawAbilityPath(maxRadius, canvas);
    }

    private void drawGridBg (float maxRadius, Canvas canvas)
    {
        int gridCount = 4;

        float eachRadius = maxRadius / gridCount;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        for (int index = gridCount - 1; index > 0; index--)
        {
           /* int colorFill = 0;

            switch (index)
            {
                case 0:
                    colorFill = 0x00000000;
                    break;
                case 1:
                    colorFill = 0x00000000;
                    break;
                case 2:
                    colorFill = 0x00000000;
                    break;
                case 3:
                    colorFill = 0x00000000;
                    break;
                default:
                    colorFill = 0x00000000;
                    break;
            }*/
            Path grid = makeGrid((index + 1) * eachRadius);
           // paint.setStyle(Paint.Style.FILL);
           // paint.setColor(colorFill);
            //canvas.drawPath(grid, paint);

            int colorLine = 0;
            switch (index)
            {
                case 0:
                    colorLine = 0xFFDDDDDD;
                    break;
                case 1:
                    colorLine = 0xFFDDDDDD;
                    break;
                case 2:
                    colorLine = 0xFFDDDDDD;
                    break;
                case 3:
                    colorLine = 0xFFDDDDDD;
                    break;
                default:
                    colorLine = 0xFFDDDDDD;
                    break;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dip2px(getContext(),1));
            paint.setColor(colorLine);
            canvas.drawPath(grid, paint);
        }

        float[] gridLines = makeGridLines(maxRadius);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(dip2px(getContext(),1.5f));
        p.setColor(0xFFDDDDDD);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawLines(gridLines, p);

    }

    private static float dip2px(Context context,float value){
        return DeviceUtils.dip2px(context,value);
    }


    private float[] makeGridLines (float maxRadius)
    {
        float[] lines = new float[4 * ABILITY_LABELS.length];

        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;

        Matrix matrix = new Matrix();


        float eachRotate = 360.0f / ABILITY_LABELS.length;

        matrix.postRotate(INIT_ROATE_VALUE, centerX, centerY);

        for (int index = 0; index < ABILITY_LABELS.length; index++)
        {
            float[] point = new float[] { centerX, centerY - maxRadius };

            matrix.postRotate(eachRotate, centerX, centerY);

            matrix.mapPoints(point);

            lines[index * 4] = centerX;
            lines[index * 4 + 1] = centerY;
            lines[index * 4 + 2] = point[0];
            lines[index * 4 + 3] = point[1];
        }

        return lines;
    }

    private void drawAbilityPath (float maxRadius, Canvas canvas)
    {

        float[][] abilityPoints = new float[ABILITY_LABELS.length][2];

        calAbilityPositions(maxRadius, abilityPoints);

        Path abilityPath = new Path();
        for (int index = 0; index < ABILITY_LABELS.length; index++)
        {
            if (index == 0)
            {
                abilityPath.moveTo(abilityPoints[index][0], abilityPoints[index][1]);
            }
            else
            {
                abilityPath.lineTo(abilityPoints[index][0], abilityPoints[index][1]);
            }
        }

        abilityPath.close();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFE8c489);
        paint.setStrokeWidth(ability_line_width);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(abilityPath, paint);

        //上: D9e8c489, 下: D9c88c51
        paint.setColor(0xD9c88c51);
        paint.setStyle(Paint.Style.FILL);


        float[] labelPoints = makeGridPoints(maxRadius);

        float startX = labelPoints[4* 2 + 0];
        float startY = labelPoints[4 * 2 + 1];

        float endX = (labelPoints[1* 2 + 0] + labelPoints[2 * 2 + 0])  / 2f;
        float endY = labelPoints[1 * 2 + 1];
        LinearGradient  linearGradient = new LinearGradient(startX, startY, endX, endY, new int[]{0XD9e8c489, 0XD9c88c51}, null,  Shader.TileMode.REPEAT);
        paint.setShader(linearGradient);
        canvas.drawPath(abilityPath, paint);

    }

    private void drawLabels (float maxRadius, Canvas canvas)
    {
        float[] labelPoints = makeGridPoints(maxRadius);

        int textPadding = (int) dip2px(getContext(), LABEL_LINE_PADDING_DP);

        double eachRadians = Math.toRadians(360.0f / ABILITY_LABELS.length);

        LABEL_PAINT_VALUE.setTextSize(dip2px(getContext(),LABLE_TEXT_VALUE_SIZE));
        LABEL_PAINT.setTextSize(dip2px(getContext(),LABLE_TEXT_SIZE));

        for (int index = 0; index < ABILITY_LABELS.length; index++)
        {
            Rect labelBounds = new Rect();
            LABEL_PAINT.getTextBounds(ABILITY_LABELS[index], 0, ABILITY_LABELS[index].length(), labelBounds);
            labelBounds.right -= labelBounds.left;
            labelBounds.bottom -= labelBounds.top;
            labelBounds.top = 0;
            labelBounds.left = 0;

            float labelX = labelPoints[index * 2];
            float labelY = labelPoints[index * 2 + 1];

            double sin = Math.sin(eachRadians);
            double cos = Math.cos(eachRadians);

            switch (index)
            {

                case 0:
                    labelX += LABELS_PADDING;
                   // labelY ;
                    break;
                case 1:
                    //labelX += LABELS_PADDING;
                    // labelY ;
                    labelY += LABELS_PADDING * cos;
                    labelY += labelBounds.height();
                    break;
                case 2:
                    labelX -= labelBounds.width();
                    labelX -= LABELS_PADDING;
                    labelY += LABELS_PADDING * cos;
                    labelY += labelBounds.height();
                    break;
                case 3:
                    labelX -= labelBounds.width();
                    labelX -= LABELS_PADDING;
                    // labelY += LABELS_PADDING * cos;
                    //labelY += LABELS_PADDING * cos;
                    break;
                case 4:
                    labelX -= labelBounds.width() / 2.0f;
                   // labelX -= LABELS_PADDING * sin;
                    // labelY += LABELS_PADDING * cos;
                    labelY -= labelBounds.height();
                    //labelY -= LABELS_PADDING;

                    break;
                case 5:
                    labelX += LABELS_PADDING * sin;
                   // labelY -= labelBounds.height();
                    labelY -= LABELS_PADDING;

                    break;
                default:
                    break;

           /*     case 0:
                    labelX -= labelBounds.width() / 2.0f;
                    labelY -= LABELS_PADDING;
                    break;
                case 1:
                    labelX += LABELS_PADDING * sin;
                    labelY -= LABELS_PADDING * cos;
                    break;
                case 2:
                    labelX += LABELS_PADDING * sin;
                    labelY += LABELS_PADDING * cos;
                    break;
                case 3:
                    labelY += labelBounds.height();
                    labelX += LABELS_PADDING * sin;
                    labelY += LABELS_PADDING * cos;
                    break;
                case 4:
                    labelX -= labelBounds.width();
                    labelY += labelBounds.height();
                    labelX -= LABELS_PADDING * sin;
                    labelY += LABELS_PADDING * cos;
                    break;
                case 5:
                    labelX -= labelBounds.width();
                    labelX -= LABELS_PADDING * sin;
                    labelY += LABELS_PADDING * cos;
                    break;
                case 6:
                    labelX -= labelBounds.width();
                    labelX -= LABELS_PADDING * sin;
                    labelY -= LABELS_PADDING * cos;
                    break;*/
            }

            String message = mValueList[index];
            Rect numberBound = new Rect();
            LABEL_PAINT.getTextBounds(message, 0, message.length(), numberBound);

            float offsetX = (labelBounds.width() - numberBound.width()) / 2f;
            float offsetY = (numberBound.height() + textPadding) / 2f;

            canvas.drawText(message, labelX + offsetX, labelY - offsetY, LABEL_PAINT_VALUE);

            canvas.drawText(ABILITY_LABELS[index], labelX, labelY + offsetY, LABEL_PAINT);

        }
    }

    private void calAbilityPositions (double radius, float[][] abilityPoints)
    {
        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;

        Matrix matrix = new Matrix();

        float eachRotate = 360.0f / ABILITY_LABELS.length;

        matrix.postRotate(INIT_ROATE_VALUE, centerX, centerY);

        int smallAbilityCount = 0;
        float[] abilityData = data;
        for (int index = 0; index < ABILITY_LABELS.length; index++)
        {
            if (data[index] <= 2)
            {
                smallAbilityCount++;
            }
        }

        if (smallAbilityCount > 1) //都偏移一点避免拉不开
        {
            abilityData = new float[ABILITY_LABELS.length];

            for (int index = 0; index < ABILITY_LABELS.length; index++)
            {
                abilityData[index] = data[index] + 5;
            }
        }

        for (int index = 0; index < ABILITY_LABELS.length; index++)
        {
            float vectorValue = abilityData[index];

            float rate = vectorValue / MAX_ABILITY_VALUE;
            if (rate > 1.0f)
            {
                rate = 1.0f;
            }

            float[] startPoint = new float[] { centerX, (float) (centerY - radius * rate) };

            matrix.postRotate(eachRotate, centerX, centerY);

            float[] finalPoint = new float[2];

            matrix.mapPoints(finalPoint, startPoint);

            abilityPoints[index][0] = finalPoint[0];
            abilityPoints[index][1] = finalPoint[1];
        }
    }

    private Path makeGrid (float radius)
    {
        Path path = new Path();

        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;

        Matrix matrix = new Matrix();

        float eachRotate = 360.0f / ABILITY_LABELS.length;

        matrix.postRotate(INIT_ROATE_VALUE, centerX, centerY);

        for (int index = 0; index < ABILITY_LABELS.length; index++)
        {
            float[] point = new float[] { centerX, centerY - radius };

            matrix.postRotate(eachRotate, centerX, centerY);

            matrix.mapPoints(point);

            if (index == 0)
            {
                path.moveTo(point[0], point[1]);
            }
            else
            {
                path.lineTo(point[0], point[1]);
            }
        }

        path.close();
        return path;
    }

    private float[] makeGridPoints (float radius)
    {
        float[] points = new float[2 * ABILITY_LABELS.length];

        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;

        Matrix matrix = new Matrix();

        float eachRotate = 360.0f / ABILITY_LABELS.length;

        matrix.postRotate(INIT_ROATE_VALUE, centerX, centerY);

        for (int index = 0; index < ABILITY_LABELS.length; index++)
        {
            float[] point = new float[] { centerX, centerY - radius };

            matrix.postRotate(eachRotate, centerX, centerY);

            matrix.mapPoints(point);

            points[index * 2] = point[0];
            points[index * 2 + 1] = point[1];
        }

        return points;
    }

}