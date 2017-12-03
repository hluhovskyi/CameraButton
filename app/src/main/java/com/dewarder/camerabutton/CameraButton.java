package com.dewarder.camerabutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
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
import static com.dewarder.camerabutton.TypedArrayHelper.getDimension;

@SuppressWarnings("unused")
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

    public interface OnProgressChangeListener {
        void onProgressChanged(@FloatRange(from = 0, to = 1) float progress);
    }

    private static final float START_ANGLE = -90f;
    private static final float SWEEP_ANGLE = 360f;

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private final Paint mMainCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //Sizes
    private int mMainCircleRadius;
    private int mMainCircleRadiusExpanded;
    private int mStrokeWidth;
    private int mProgressArcWidth;

    //Colors
    private int mMainCircleColor = Color.WHITE;
    private int mMainCircleColorPressed = Color.parseColor("#eeeeee");
    private int mStrokeColor = Color.parseColor("#66FFFFFF");
    private int mStrokeColorPressed = Color.parseColor("#44FFFFFF");
    private int[] mProgressArcColors = {
            Color.parseColor("#feda75"),
            Color.parseColor("#fa7e1e"),
            Color.parseColor("#d62976"),
            Color.parseColor("#962fbf"),
            Color.parseColor("#4f5bd5")
    };

    //Durations
    private long mExpandDuration = 200;
    private long mCollapseDuration = 200;
    private long mExpandDelay = 400;
    private long mHoldDuration = 15000;

    //Logic
    private State mCurrentState = DEFAULT;
    private float mGradientRotationMultiplier = 1.75f;
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
    private OnProgressChangeListener mProgressListener;

    public CameraButton(Context context) {
        this(context, null);
    }

    public CameraButton(Context context,
                        @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraButton(Context context,
                        @Nullable AttributeSet attrs,
                        int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraButton(Context context,
                        @Nullable AttributeSet attrs,
                        int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context,
                      AttributeSet attrs,
                      int defStyleAttr,
                      int defStyleRes) {

        TypedArray array = context.obtainStyledAttributes(
                attrs, R.styleable.CameraButton, defStyleAttr, defStyleRes);

        mMainCircleRadius = getDimension(
                context, array,
                R.styleable.CameraButton_cb_main_circle_radius,
                R.dimen.cb_main_circle_radius_default);

        mMainCircleRadiusExpanded = getDimension(
                context, array,
                R.styleable.CameraButton_cb_main_circle_radius_expanded,
                R.dimen.cb_main_circle_radius_expanded_default);

        mStrokeWidth = getDimension(
                context, array,
                R.styleable.CameraButton_cb_stroke_width,
                R.dimen.cb_stroke_width_default);

        mProgressArcWidth = getDimension(
                context, array,
                R.styleable.CameraButton_cb_progress_arc_width,
                R.dimen.cb_progress_arc_width_default);

        array.recycle();

        mMainCirclePaint.setColor(mMainCircleColor);
        mStrokePaint.setColor(mStrokeColor);

        mProgressArcPaint.setStyle(Paint.Style.STROKE);
        mProgressArcPaint.setStrokeWidth(mProgressArcWidth);
        mProgressArcPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mExpandMessage = () -> {
                    mProgressFactor = 0f;
                    mExpandAnimator = createExpandingAnimator();
                    mExpandAnimator.start();
                };
                postDelayed(mExpandMessage, mExpandDelay);
                makePaintColorsHovered(true);
                invalidate();
                dispatchStateChange(PRESSED);
                return true;
            }

            case MotionEvent.ACTION_UP: {
                if (mCurrentState == START_EXPANDING || mCurrentState == EXPANDED) {
                    if (mExpandAnimator != null) {
                        mExpandAnimator.cancel();
                    }
                    mCollapseAnimator = createCollapsingAnimator();
                    mCollapseAnimator.start();
                } else if (mCurrentState == PRESSED) {
                    removeCallbacks(mExpandMessage);
                    dispatchStateChange(DEFAULT);
                }
                makePaintColorsHovered(false);
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
                mProgressAnimator = createProgressAnimator();
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
                makePaintColorsHovered(false);
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

    private ValueAnimator createProgressAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(LINEAR_INTERPOLATOR);
        animator.addUpdateListener(animation -> {
            mProgressFactor = (float) animation.getAnimatedValue();
            dispatchProgressChange(mProgressFactor);
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCollapseAnimator = createCollapsingAnimator();
                mCollapseAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }
        });
        animator.setDuration(mHoldDuration);
        return animator;
    }

    private void makePaintColorsHovered(boolean hovered) {
        if (hovered) {
            mMainCirclePaint.setColor(mMainCircleColorPressed);
            mStrokePaint.setColor(mStrokeColorPressed);
        } else {
            mMainCirclePaint.setColor(mMainCircleColor);
            mStrokePaint.setColor(mStrokeColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        if (mExpandedArea == null) {
            mExpandedArea = calculateExpandedArea(width, height);
            mProgressArcPaint.setShader(createGradient(width, height));
        }

        float strokeCollapsedRadius = mMainCircleRadius + mStrokeWidth;
        canvas.drawCircle(centerX, centerY, strokeCollapsedRadius - (strokeCollapsedRadius - Math.max(centerX, centerY)) * mExpandingFactor, mStrokePaint);

        //Rotate whole canvas and reduce rotation from start angle of progress arc.
        //It allows to rotate gradient shader without rotating arc in the end.
        canvas.save();
        float gradientRotation = SWEEP_ANGLE * mProgressFactor * mGradientRotationMultiplier;
        canvas.rotate(gradientRotation, centerX, centerY);
        canvas.drawArc(mExpandedArea, START_ANGLE - gradientRotation, SWEEP_ANGLE * mProgressFactor, false, mProgressArcPaint);
        canvas.restore();

        float radius = mMainCircleRadius - (mMainCircleRadius - mMainCircleRadiusExpanded) * mExpandingFactor;
        canvas.drawCircle(centerX, centerY, radius, mMainCirclePaint);
    }

    private RectF calculateExpandedArea(int width, int height) {
        float expandedAreaOffset = mProgressArcWidth / 2f;
        return new RectF(
                expandedAreaOffset,
                expandedAreaOffset,
                width - expandedAreaOffset,
                height - expandedAreaOffset);
    }

    private Shader createGradient(int width, int height) {
        return new LinearGradient(0, 0, width, height,
                                  mProgressArcColors, null, Shader.TileMode.MIRROR);
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

    private void dispatchProgressChange(float progress) {
        if (mProgressListener != null) {
            mProgressListener.onProgressChanged(progress);
        }
    }

    //==============================
    //       Getters/Setters
    //==============================

    public void setOnStateChangeListener(@Nullable OnStateChangeListener listener) {
        mStateListener = listener;
    }

    public void setOnTapEventListener(@Nullable OnTapEventListener listener) {
        mTapListener = listener;
    }

    public void setOnHoldEventListener(@Nullable OnHoldEventListener listener) {
        mHoldListener = listener;
    }

    public void setOnProgressChangeListener(@Nullable OnProgressChangeListener listener) {
        mProgressListener = listener;
    }

    @Px
    public int getMainCircleRadius() {
        return mMainCircleRadius;
    }

    public void setMainCircleRadius(@Px int radius) {
        mMainCircleRadius = radius;
        invalidate();
    }

    @Px
    public int getMainCircleRadiusExpanded() {
        return mMainCircleRadiusExpanded;
    }

    public void setMainCircleRadiusExpanded(@Px int radius) {
        mMainCircleRadiusExpanded = radius;
        invalidate();
    }

    @Px
    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(@Px int width) {
        mStrokeWidth = width;
        invalidate();
    }

    @Px
    public int getProgressArcWidth() {
        return mProgressArcWidth;
    }

    public void setProgressArcWidth(@Px int width) {
        mProgressArcWidth = width;
        invalidate();
    }

    @ColorInt
    public int getMainCircleColor() {
        return mMainCircleColor;
    }

    public void setMainCircleColor(@ColorInt int color) {
        mMainCircleColor = color;
    }

    @ColorInt
    public int getMainCircleColorPressed() {
        return mMainCircleColorPressed;
    }

    public void setMainCircleColorPressed(@ColorInt int color) {
        mMainCircleColorPressed = color;
    }

    @ColorInt
    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(@ColorInt int color) {
        mStrokeColor = color;
    }

    @ColorInt
    public int getStrokeColorPressed() {
        return mStrokeColorPressed;
    }

    public void setStrokeColorPressed(@ColorInt int color) {
        mStrokeColorPressed = color;
    }

    @ColorInt
    @NonNull
    public int[] getProgressArcColors() {
        return mProgressArcColors.clone();
    }

    public void setProgressArcColors(@ColorInt @NonNull int[] colors) {
        mProgressArcColors = Objects.requireNonNull(colors).clone();
    }

    public long getExpandDuration() {
        return mExpandDuration;
    }

    public void setExpandDuration(long duration) {
        mExpandDuration = duration;
    }

    public long getCollapseDuration() {
        return mCollapseDuration;
    }

    public void setCollapseDuration(long duration) {
        mCollapseDuration = duration;
    }

    public long getExpandDelay() {
        return mExpandDelay;
    }

    public void setExpandDelay(long delay) {
        mExpandDelay = delay;
    }

    public long getHoldDuration() {
        return mHoldDuration;
    }

    public void setHoldDuration(long duration) {
        mHoldDuration = duration;
    }

    @FloatRange(from = 0, fromInclusive = false)
    public float getGradientRotationMultiplier() {
        return mGradientRotationMultiplier;
    }

    public void setGradientRotationMultiplier(
            @FloatRange(from = 0, fromInclusive = false) float multiplier) {

        if (multiplier <= 0) {
            throw new IllegalStateException("Multiplier should be greater than 0");
        }
        mGradientRotationMultiplier = multiplier;
    }


    //=================================
    //       Additional classes
    //=================================

    public enum State {
        DEFAULT,
        PRESSED,
        START_EXPANDING,
        EXPANDED,
        START_COLLAPSING
    }
}
