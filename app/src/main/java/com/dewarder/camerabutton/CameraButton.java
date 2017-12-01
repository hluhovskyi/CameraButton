package com.dewarder.camerabutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import static com.dewarder.camerabutton.CameraButton.State.DEFAULT;
import static com.dewarder.camerabutton.CameraButton.State.EXPANDED;
import static com.dewarder.camerabutton.CameraButton.State.PRESSED;
import static com.dewarder.camerabutton.CameraButton.State.START_COLLAPSING;
import static com.dewarder.camerabutton.CameraButton.State.START_EXPANDING;

public class CameraButton extends View {

    public interface OnStateChangeListener {
        void onStateChanged(@NonNull State state);
    }

    public interface OnTapEventListener {
        void onTap();
    }

    public interface OnHoldEventListener {
        void onStart();

        void onFinish();

        void onCancel();
    }

    private static final float START_ANGLE = -90f;
    private static final float SWEEP_ANGLE = 360f;

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private final Paint mMainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //Sizes
    private int mProgressArcWidth = 8;
    private int mCollapsedRadius = 56;
    private int mCollapsedStrokeWidth = 24;
    private int mExpandedRadius = 48;

    //Colors
    private int mMainColor = Color.WHITE;
    private int mMainColorHovered = Color.parseColor("#eeeeee");
    private int mStrokeColor = Color.parseColor("#66FFFFFF");
    private int mStrokeColorHovered = Color.parseColor("#44FFFFFF");

    //Durations
    private long mExpandDuration = 2000;
    private long mCollapseDuration = 2000;
    private long mExpandDelay = 400;
    private long mMaxDuration = 15000;

    //Logic
    private State mCurrentState = DEFAULT;
    private boolean mExpanded = false;
    private float mExpandingFactor = 0f;
    private float mProgressFactor = 0f;
    private RectF mExpandedArea = null;

    //Cancellable
    private ValueAnimator mExpandAnimator = null;
    private ValueAnimator mCollapseAnimator = null;
    private ValueAnimator mProgressAnimator = null;
    private Runnable mExpandMessage = null;

    //Listeners
    private OnStateChangeListener mStateListener;
    private OnTapEventListener mTapListener;
    private OnHoldEventListener mHoldListener;

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
        mMainPaint.setColor(mMainColor);
        mStrokePaint.setColor(mStrokeColor);

        mProgressArcPaint.setStyle(Paint.Style.STROKE);
        mProgressArcPaint.setStrokeWidth(mProgressArcWidth);
        mProgressArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressArcPaint.setShader(new LinearGradient(0, 0, 400, 400, Color.RED, Color.GREEN, Shader.TileMode.MIRROR));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mExpandMessage = () -> {
                    mExpanded = true;
                    mProgressFactor = 0f;

                    mExpandAnimator = createExpandingAnimator();
                    mExpandAnimator.start();
                };
                postDelayed(mExpandMessage, mExpandDelay);

                mMainPaint.setColor(mMainColorHovered);
                mStrokePaint.setColor(mStrokeColorHovered);
                invalidate();
                dispatchStateChange(PRESSED);
                return true;
            }

            case MotionEvent.ACTION_UP: {
                if (mExpanded) {
                    if (mExpandAnimator != null) {
                        mExpandAnimator.cancel();
                    }

                    mCollapseAnimator = createCollapsingAnimator();
                    mCollapseAnimator.start();
                } else {
                    removeCallbacks(mExpandMessage);
                    dispatchStateChange(DEFAULT);
                }
                mExpanded = false;

                mMainPaint.setColor(mMainColor);
                mStrokePaint.setColor(mStrokeColor);
                invalidate();
                return true;
            }
        }
        return false;
    }

    private ValueAnimator createExpandingAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(animation -> {
            mExpandingFactor = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                dispatchStateChange(START_EXPANDING);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressAnimator = ValueAnimator.ofFloat(0f, 1f);
                mProgressAnimator.setInterpolator(LINEAR_INTERPOLATOR);
                mProgressAnimator.addUpdateListener(animation1 -> {
                    mProgressFactor = (float) animation1.getAnimatedValue();
                    invalidate();
                });
                mProgressAnimator.setDuration(mMaxDuration);
                mProgressAnimator.start();
                dispatchStateChange(EXPANDED);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }
        });
        animator.setDuration(mExpandDuration);
        return animator;
    }

    private ValueAnimator createCollapsingAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.addUpdateListener(animation -> {
            mExpandingFactor = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mProgressAnimator != null) {
                    mProgressAnimator.cancel();
                }
                mProgressFactor = 0f;
                dispatchStateChange(START_COLLAPSING);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchStateChange(DEFAULT);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }
        });
        animator.setDuration(mCollapseDuration);
        return animator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        float strokeCollapsedRadius = mCollapsedRadius + mCollapsedStrokeWidth;
        if (mExpandedArea == null) {
            float expandedAreaOffset = mProgressArcWidth / 2f;
            mExpandedArea = new RectF(
                    expandedAreaOffset,
                    expandedAreaOffset,
                    getWidth() - expandedAreaOffset,
                    getHeight() - expandedAreaOffset);
        }

        canvas.drawCircle(centerX, centerY, strokeCollapsedRadius - (strokeCollapsedRadius - Math.max(centerX, centerY)) * mExpandingFactor, mStrokePaint);
        canvas.drawArc(mExpandedArea, START_ANGLE, SWEEP_ANGLE * mProgressFactor, false, mProgressArcPaint);

        float radius = mCollapsedRadius - (mCollapsedRadius - mExpandedRadius) * mExpandingFactor;
        canvas.drawCircle(centerX, centerY, radius, mMainPaint);
    }

    public void setOnStateChangeListener(@Nullable OnStateChangeListener listener) {
        mStateListener = listener;
    }

    public void setOnTapEventListener(@Nullable OnTapEventListener listener) {
        mTapListener = listener;
    }

    public void setOnHoldEventListener(@Nullable OnHoldEventListener listener) {
        mHoldListener = listener;
    }

    private void dispatchStateChange(State state) {
        if (mStateListener != null) {
            mStateListener.onStateChanged(state);
        }

        if (mHoldListener != null) {
            if (state == EXPANDED) {
                mHoldListener.onStart();
            } else if (mCurrentState == EXPANDED && state == START_COLLAPSING) {
                mHoldListener.onFinish();
            }
        }

        if (mTapListener != null) {
            if (mCurrentState == PRESSED && state == DEFAULT) {
                mTapListener.onTap();
            }
        }

        mCurrentState = state;
    }

    public enum State {
        DEFAULT,
        PRESSED,
        START_EXPANDING,
        EXPANDED,
        START_COLLAPSING
    }
}
