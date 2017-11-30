package com.dewarder.camerabutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class CameraButton extends View {

    private final Paint mMainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mProgressArcWidth = 8;
    private int mCollapsedRadius = 56;
    private int mCollapsedStrokeWidth = 24;
    private int mExpandedRadius = 48;

    private RectF mExpandedArea = null;

    private float mExpandingFactor = 0f;
    private float mProgressFactor = 0f;

    {
        mMainPaint.setColor(Color.WHITE);
        mStrokePaint.setColor(Color.parseColor("#33FFFFFF"));

        mProgressArcPaint.setStyle(Paint.Style.STROKE);
        mProgressArcPaint.setStrokeWidth(8);
        mProgressArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressArcPaint.setColor(Color.RED);
    }

    public CameraButton(Context context) {
        super(context);
        init();
    }

    public CameraButton(Context context,
                        @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraButton(Context context,
                        @Nullable AttributeSet attrs,
                        int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraButton(Context context,
                        @Nullable AttributeSet attrs,
                        int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressFactor = 0f;
                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mExpandingFactor = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ValueAnimator progressAnimator = ValueAnimator.ofFloat(0f, 1f);
                        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mProgressFactor = (float) animation.getAnimatedValue();
                                invalidate();
                            }
                        });
                        progressAnimator.setDuration(5000L);
                        progressAnimator.start();
                    }
                });
                animator.start();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        float strokeCollapsedRadius = mCollapsedRadius + mCollapsedStrokeWidth;
        if (mExpandedArea == null) {
            mExpandedArea = new RectF(
                    mProgressArcWidth,
                    mProgressArcWidth,
                    getWidth() - mProgressArcWidth,
                    getHeight() - mProgressArcWidth);
        }

        canvas.drawCircle(centerX, centerY, strokeCollapsedRadius - (strokeCollapsedRadius - Math.max(centerX, centerY)) * mExpandingFactor, mStrokePaint);
        canvas.drawArc(mExpandedArea, -90f, 480f * mProgressFactor, false, mProgressArcPaint);

        float radius = mCollapsedRadius - (mCollapsedRadius - mExpandedRadius) * mExpandingFactor;
        canvas.drawCircle(centerX, centerY, radius, mMainPaint);
    }

}
