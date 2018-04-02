package com.hluhovskyi.camerabutton.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hluhovskyi.camerabutton.CameraButton;

public final class CameraButtonRecyclerView {

    public static Builder newBuilder(@NonNull CameraButton button, @NonNull RecyclerView recycler) {
        return new Builder(button, recycler);
    }

    public static final class Builder {

        private final CameraButton mCameraButton;
        private final RecyclerView mRecyclerView;

        private boolean mSnap = false;

        Builder(CameraButton button, RecyclerView recycler) {
            mCameraButton = button;
            mRecyclerView = recycler;
        }

        public Builder snap() {
            mSnap = true;
            return this;
        }

        public RecyclerView.OnScrollListener attach() {
            if (mSnap) {
                new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
            }
            RecyclerView.OnScrollListener listener = new HorizontalCenterScrollListener(mCameraButton);
            mRecyclerView.addOnScrollListener(listener);
            return listener;
        }
    }

    static class HorizontalCenterScrollListener extends RecyclerView.OnScrollListener {

        private final CameraButton mCameraButton;

        private LinearLayoutManager mManager;

        HorizontalCenterScrollListener(CameraButton button) {
            mCameraButton = button;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            initLayoutManagerIfNeeded(recyclerView);

            int position = mManager.findFirstVisibleItemPosition();
            View view = mManager.findViewByPosition(position);

            if (view == null) {
                Log.v("Recycler", "I don't get a fuck what is going on here. Position=" + position);
                log(recyclerView, view);
                return;
            }

            int viewCenter = view.getWidth() / 2;

            final float paddingDiff;
            if (recyclerView.getPaddingLeft() != 0) {
                paddingDiff = Math.abs(recyclerView.getWidth() / 2f - recyclerView.getPaddingLeft() - viewCenter);
            } else {
                paddingDiff = 0;
            }

            float buttonPosition = position - 0.5f + Math.abs((float) (view.getLeft() - recyclerView.getPaddingLeft() + paddingDiff - viewCenter) / view.getWidth());
            if (buttonPosition < 0) {
                Log.v("Recycler", "Should be calculated better");
                log(recyclerView, view);
                return;
            }

            mCameraButton.setIconsPosition(buttonPosition);
        }

        private void initLayoutManagerIfNeeded(final RecyclerView recyclerView) {
            if (mManager == null) {
                if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                    throw new IllegalStateException("Only LinearLayoutManager is supported");
                }

                mManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (mManager == null) {
                    throw new NullPointerException("LayoutManager is null. Attach it before scroll");
                }

                int newPadding = recyclerView.getWidth() / 2 - 1;
                recyclerView.setPadding(
                        newPadding,
                        recyclerView.getPaddingTop(),
                        newPadding,
                        recyclerView.getPaddingBottom()
                );
            }
        }

        /**
         * Helps debug metrics of RecyclerView and first visible view
         */
        private void log(RecyclerView recyclerView, View view) {
            Log.v("Recycler", "BEGIN -------------->/n"
                    + "[left=" + view.getLeft()
                    + ",width=" + view.getWidth()
                    + ",paddingLeft=" + recyclerView.getPaddingLeft()
                    + "]"
                    + "/nEND <--------------"
            );
        }
    }
}
