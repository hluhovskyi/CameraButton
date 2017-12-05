package com.dewarder.camerabutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static com.dewarder.camerabutton.CameraButton.State.DEFAULT;
import static com.dewarder.camerabutton.CameraButton.State.EXPANDED;
import static com.dewarder.camerabutton.CameraButton.State.PRESSED;
import static com.dewarder.camerabutton.CameraButton.State.START_COLLAPSING;
import static com.dewarder.camerabutton.CameraButton.State.START_EXPANDING;
import static com.dewarder.camerabutton.TypedArrayHelper.getColor;
import static com.dewarder.camerabutton.TypedArrayHelper.getColors;
import static com.dewarder.camerabutton.TypedArrayHelper.getDimension;
import static com.dewarder.camerabutton.TypedArrayHelper.getInteger;

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

    public static final float DEFAULT_GRADIENT_ROTATION_MULTIPLIER = 1.75f;

    private static final int DEFAULT_MODE_INDEX = 0;
    private static final float START_ANGLE = -90f;
    private static final float SWEEP_ANGLE = 360f;

    private final Paint mMainCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //Sizes
    private int mMainCircleRadius;
    private int mMainCircleRadiusExpanded;
    private int mStrokeWidth;
    private int mProgressArcWidth;

    //Colors
    private int mMainCircleColor;
    private int mMainCircleColorPressed;
    private int mStrokeColor;
    private int mStrokeColorPressed;
    private int[] mProgressArcColors;

    //Durations
    private long mExpandDuration;
    private long mCollapseDuration;
    private long mExpandDelay;
    private long mHoldDuration;

    //Logic
    private Mode mCurrentMode;
    private State mCurrentState = DEFAULT;
    private float mGradientRotationMultiplier = 1.75f;
    private float mExpandingFactor = 0f;
    private float mProgressFactor = 0f;
    private RectF mProgressArcArea = null;

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

        mMainCircleColor = getColor(
                context, array,
                R.styleable.CameraButton_cb_main_circle_color,
                R.color.cb_main_circle_color_default);

        mMainCircleColorPressed = getColor(
                context, array,
                R.styleable.CameraButton_cb_main_circle_color_pressed,
                R.color.cb_main_circle_color_pressed_default);

        mStrokeColor = getColor(
                context, array,
                R.styleable.CameraButton_cb_stroke_color,
                R.color.cb_stroke_color_default);

        mStrokeColorPressed = getColor(
                context, array,
                R.styleable.CameraButton_cb_stroke_color_pressed,
                R.color.cb_stroke_color_pressed_default);

        mProgressArcColors = getColors(
                context, array,
                R.styleable.CameraButton_cb_progress_arc_colors,
                R.array.cb_progress_arc_colors_default);

        mExpandDuration = Constraints.checkDuration(
                getInteger(context, array,
                        R.styleable.CameraButton_cb_expand_duration,
                        R.integer.cb_expand_duration_default));

        mExpandDelay = Constraints.checkDuration(
                getInteger(context, array,
                        R.styleable.CameraButton_cb_expand_delay,
                        R.integer.cb_expand_delay_default));

        mCollapseDuration = Constraints.checkDuration(
                getInteger(context, array,
                        R.styleable.CameraButton_cb_collapse_duration,
                        R.integer.cb_collapse_duration_default));

        mHoldDuration = Constraints.checkDuration(
                getInteger(context, array,
                        R.styleable.CameraButton_cb_hold_duration,
                        R.integer.cb_hold_duration_default));

        mCurrentMode = Mode.fromValue(
                array.getInteger(
                        R.styleable.CameraButton_cb_mode,
                        DEFAULT_MODE_INDEX));

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
                if (isEnabled() && isTouched(event)) {
                    if (mCurrentMode.isHoldAllowed()) {
                        mExpandMessage = () -> {
                            mProgressFactor = 0f;
                            mExpandAnimator = createExpandingAnimator();
                            mExpandAnimator.start();
                        };

                        if (mCurrentMode.isTapAllowed()) {
                            postDelayed(mExpandMessage, mExpandDelay);
                        } else {
                            post(mExpandMessage);
                        }
                    }

                    makePaintColorsHovered(true);
                    invalidate();
                    dispatchStateChange(PRESSED);
                    return true;
                }
            }

            case MotionEvent.ACTION_UP: {
                if (mCurrentState == START_EXPANDING || mCurrentState == EXPANDED) {
                    if (mExpandAnimator != null) {
                        mExpandAnimator.cancel();
                    }
                    mCollapseAnimator = createCollapsingAnimator();
                    mCollapseAnimator.start();

                    makePaintColorsHovered(false);
                    invalidate();
                    return true;
                } else if (mCurrentState == PRESSED) {
                    removeCallbacks(mExpandMessage);
                    dispatchStateChange(DEFAULT);

                    makePaintColorsHovered(false);
                    invalidate();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTouched(MotionEvent e) {
        int radius = mMainCircleRadius + mStrokeWidth;
        return Math.abs(e.getX() - getWidth() / 2f) <= radius &&
                Math.abs(e.getY() - getHeight() / 2f) <= radius;
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
                makePaintColorsHovered(false);
                dispatchStateChange(START_COLLAPSING);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressFactor = 0f;
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
        animator.setInterpolator(Interpolators.getLinearInterpolator());
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
        int radius = Math.min(centerX, centerY);

        if (mProgressArcArea == null) {
            mProgressArcArea = new RectF();
            mProgressArcPaint.setShader(createGradient(width, height));
        }

        float strokeCollapsedRadius = mMainCircleRadius + mStrokeWidth;
        float currentStrokeRadius = strokeCollapsedRadius - (strokeCollapsedRadius - radius) * mExpandingFactor;
        canvas.drawCircle(centerX, centerY, currentStrokeRadius, mStrokePaint);

        float currentArcWidth = mProgressArcWidth * Interpolators.interpolateArcWidth(mExpandingFactor);
        if (currentArcWidth > 0f) {
            mProgressArcPaint.setStrokeWidth(currentArcWidth);

            //Rotate whole canvas and reduce rotation from start angle of progress arc.
            //It allows to rotate gradient shader without rotating arc in the end.
            canvas.save();
            float gradientRotation = SWEEP_ANGLE * mProgressFactor * mGradientRotationMultiplier;
            canvas.rotate(gradientRotation, centerX, centerY);
            invalidateProgressArcArea(centerX, centerY, currentStrokeRadius, currentArcWidth);
            canvas.drawArc(mProgressArcArea, START_ANGLE - gradientRotation, SWEEP_ANGLE * mProgressFactor, false, mProgressArcPaint);
            canvas.restore();
        }

        float mainCircleRadius = mMainCircleRadius - (mMainCircleRadius - mMainCircleRadiusExpanded) * mExpandingFactor;
        canvas.drawCircle(centerX, centerY, mainCircleRadius, mMainCirclePaint);
    }

    private void invalidateProgressArcArea(int centerX, int centerY, float strokeRadius, float arcWidth) {
        float expandedAreaOffset = arcWidth / 2f;
        mProgressArcArea.top = centerY - strokeRadius + expandedAreaOffset;
        mProgressArcArea.left = centerX - strokeRadius + expandedAreaOffset;
        mProgressArcArea.bottom = centerY + strokeRadius - expandedAreaOffset;
        mProgressArcArea.right = centerX + strokeRadius - expandedAreaOffset;
    }

    private Shader createGradient(int width, int height) {
        return new LinearGradient(0, 0, width, height,
                mProgressArcColors, null, Shader.TileMode.MIRROR);
    }

    private void dispatchStateChange(State state) {
        if (mStateListener != null) {
            mStateListener.onStateChanged(state);
        }
        if (mHoldListener != null && mCurrentMode.isHoldAllowed()) {
            if (state == EXPANDED) {
                mHoldListener.onStart();
            } else if (mCurrentState == EXPANDED && state == START_COLLAPSING) {
                mHoldListener.onFinish();
            }
        }
        if (mTapListener != null && mCurrentMode.isTapAllowed()) {
            if (mCurrentState == PRESSED && state == DEFAULT ||
                    mCurrentState == START_EXPANDING && state == START_COLLAPSING) {
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
        mMainCircleRadius = Constraints.checkDimension(radius);
        invalidate();
    }

    @Px
    public int getMainCircleRadiusExpanded() {
        return mMainCircleRadiusExpanded;
    }

    public void setMainCircleRadiusExpanded(@Px int radius) {
        mMainCircleRadiusExpanded = Constraints.checkDimension(radius);
        invalidate();
    }

    @Px
    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(@Px int width) {
        mStrokeWidth = Constraints.checkDimension(width);
        invalidate();
    }

    @Px
    public int getProgressArcWidth() {
        return mProgressArcWidth;
    }

    public void setProgressArcWidth(@Px int width) {
        mProgressArcWidth = Constraints.checkDimension(width);
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
        mProgressArcColors = Constraints.checkNonNull(colors).clone();
    }

    @IntRange(from = 1)
    public long getExpandDuration() {
        return mExpandDuration;
    }

    public void setExpandDuration(@IntRange(from = 1) long duration) {
        mExpandDuration = Constraints.checkDuration(duration);
    }

    @IntRange(from = 1)
    public long getCollapseDuration() {
        return mCollapseDuration;
    }

    public void setCollapseDuration(@IntRange(from = 1) long duration) {
        mCollapseDuration = Constraints.checkDuration(duration);
    }

    @IntRange(from = 1)
    public long getExpandDelay() {
        return mExpandDelay;
    }

    public void setExpandDelay(@IntRange(from = 1) long delay) {
        mExpandDelay = Constraints.checkDuration(delay);
    }

    @IntRange(from = 1)
    public long getHoldDuration() {
        return mHoldDuration;
    }

    public void setHoldDuration(@IntRange(from = 1) long duration) {
        mHoldDuration = Constraints.checkDuration(duration);
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

    @NonNull
    public State getState() {
        return mCurrentState;
    }

    @NonNull
    public Mode getMode() {
        return mCurrentMode;
    }

    public void setMode(@NonNull Mode mode) {
        mCurrentMode = Constraints.checkNonNull(mode);
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

    public enum Mode {
        ALL(true, true),
        TAP(true, false),
        HOLD(false, true);

        private final boolean mTapAllowed;
        private final boolean mHoldAllowed;

        Mode(boolean tapAllowed, boolean holdAllowed) {
            mTapAllowed = tapAllowed;
            mHoldAllowed = holdAllowed;
        }

        private static Mode fromValue(int value) {
            switch (value) {
                case 0:
                    return ALL;
                case 1:
                    return TAP;
                case 2:
                    return HOLD;
                default:
                    throw new IllegalStateException("No mode corresponding to value " + value);
            }
        }

        public boolean isTapAllowed() {
            return mTapAllowed;
        }

        public boolean isHoldAllowed() {
            return mHoldAllowed;
        }
    }
}
