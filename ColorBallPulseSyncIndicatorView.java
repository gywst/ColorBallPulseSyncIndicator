package com.example.ballpulseindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by Guy West on 17/11/2015
 *
 * based on the AVLoadingIndicatorView library
 * // https://github.com/81813780/AVLoadingIndicatorView/blob/master/library/src/main/java/com/wang/avi/indicator/BallPulseSyncIndicator.java
 */

public class ColorBallPulseSyncIndicatorView extends View {

    private static final int DEFAULT_SIZE = 45; // Sizes (with defaults in DP)
    private static final int CIRCLE_NUMBER = 3;

    private static final String COLOR_ONE = "#3B8EFF";
    private static final String COLOR_TWO = "#FFD81D";
    private static final String COLOR_THREE = "#FF4A4B";

    Paint mPaint;

    private int mIndicatorColor[] = {Color.parseColor(COLOR_ONE), Color.parseColor(COLOR_TWO), Color.parseColor(COLOR_THREE)};

    ArrayList<Paint> mCirclesPaint;

    public static final float SCALE = 1.0f;

    //scale x ,y
    private float[] scaleFloats = new float[]{SCALE, SCALE, SCALE};

    float[] translateYFloats = new float[3];

    private boolean mHasAnimation;

    public ColorBallPulseSyncIndicatorView(Context context) {
        super(context);
        initView();
    }

    public ColorBallPulseSyncIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ColorBallPulseSyncIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mCirclesPaint = new ArrayList<>();
        for (int i = 0; i < CIRCLE_NUMBER; i++) {
            mPaint = new Paint();
            mPaint.setColor(mIndicatorColor[i]);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mCirclesPaint.add(mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float circleSpacing = 4;
        float radius = (getWidth() - circleSpacing * 2) / 6;
        float x = getWidth() / 2 - (radius * 2 + circleSpacing);
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + circleSpacing * i;
            canvas.translate(translateX, translateYFloats[i]);
            canvas.drawCircle(0, 0, radius, mCirclesPaint.get(i));
            canvas.restore();
        }

        // redraw the view every 40 milliseconds and show the animation
        //postInvalidateDelayed(120);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!mHasAnimation) {
            mHasAnimation = true;
            createAnimation();
        }
    }

    private void createAnimation() {
        float circleSpacing = 4;
        float radius = (getWidth() - circleSpacing * 2) / 6;
        int[] delays = new int[]{70, 140, 210};
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(getHeight() / 2, getHeight() / 2 - radius * 2, getHeight() / 2);
            scaleAnim.setDuration(600);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateYFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();
        }
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }

}
