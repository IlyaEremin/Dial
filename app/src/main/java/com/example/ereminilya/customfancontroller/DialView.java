package com.example.ereminilya.customfancontroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DialView extends android.view.View {

    private static final String TAG = "DialView";

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Calculate the radius from the width and height.
        mWidth = w;
        mHeight = h;
        mRadius = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
    }

    private float[] computeXYForPosition
        (final int pos, final float radius) {
        float[] result = mTempResult;
        Double startAngle = Math.PI * (9 / 8d);   // Angles are in radians.
        Double angle = startAngle + (pos * (Math.PI / 5));
        result[0] = (float) (radius * Math.cos(angle)) + (mWidth / 2);
        result[1] = (float) (radius * Math.sin(angle)) + (mHeight / 2);
        return result;
    }

    private static int SELECTION_COUNT = 5; // Total number of selections.
    private float mWidth;                   // Custom view width.
    private float mHeight;                  // Custom view height.
    private Paint mTextPaint;               // For text in the view.
    private Paint mDialPaint;               // For dial circle in the view.
    private float mRadius;                  // Radius of the circle.
    private int   mActiveSelection;           // The active selection.
    // String buffer for dial labels and float for ComputeXY result.
    private final StringBuffer mTempLabel  = new StringBuffer(8);
    private final float[]      mTempResult = new float[2];

    boolean mAltColor = false;

    public boolean isSetToAltColor() {
        return mAltColor;
    }

    public DialView(Context context) {
        super(context);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Collect the attributes into a typed array.
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.DialView, 0, 0);
        // Use mAltColor to represent the alternate color attribute.
        mAltColor = typedArray.getBoolean(R.styleable.DialView_alternateColor,
            false);
        // Recycle the array.
        typedArray.recycle();
        init();
    }


    private void init() {

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Initialize current selection.
        mActiveSelection = 0;
        AltColors();
        setBigCircleColor();
        // TODO: Set up onClick listener for this view.
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rotate selection to the next valid choice.
                mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT;
                if (mActiveSelection >= 0) {
                    setBigCircleColor();
                } else {
                    if (mAltColor) {
                        mDialPaint.setColor(Color.YELLOW);
                    } else {
                        mDialPaint.setColor(Color.GRAY);
                    }
                }
                Log.e(TAG, String.valueOf(mActiveSelection));
                // Set dial background color to green if selection is >= 1.
                setBigCircleColor();
                // Redraw the view.
                invalidate();
            }
        });
    }

    private void AltColors() {
        if (mAltColor) {
            mTextPaint.setColor(Color.RED);
            mDialPaint.setColor(Color.YELLOW);
        } else {
            mTextPaint.setColor(Color.BLACK);
            mDialPaint.setColor(Color.GRAY);
        }
    }

    private void setBigCircleColor() {
        if (mActiveSelection >= 1) {
            mDialPaint.setColor(Color.GREEN);
        } else {
            mDialPaint.setColor(Color.GRAY);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the dial.
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint);
        // Draw the text labels.
        final float labelRadius = mRadius + 20;
        StringBuffer label = mTempLabel;
        for (int i = 0; i < SELECTION_COUNT; i++) {
            float[] xyData = computeXYForPosition(i, labelRadius);
            float x = xyData[0];
            float y = xyData[1];
            label.setLength(0);
            label.append(i);
            canvas.drawText(label, 0, label.length(), x, y, mTextPaint);
        }
        // Draw the indicator mark.
        final float markerRadius = mRadius - 35;
        float[] xyData = computeXYForPosition(mActiveSelection,
            markerRadius);
        float x = xyData[0];
        float y = xyData[1];
        canvas.drawCircle(x, y, 20, mTextPaint);
    }

    public void setmAltColor(boolean mAltColor) {
        this.mAltColor = mAltColor;
    }
}
