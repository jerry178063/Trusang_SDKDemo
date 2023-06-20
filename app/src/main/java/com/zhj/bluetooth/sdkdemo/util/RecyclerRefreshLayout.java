package com.zhj.bluetooth.sdkdemo.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zhj.bluetooth.sdkdemo.R;


/**
 * Drop-down refresh pull-up load control, currently applicable to RecyclerView
 * From the code of open-source China project
 */
public class RecyclerRefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecycleView;

    private int mTouchSlop;

    private SuperRefreshLayoutListener listener;

    private boolean mIsOnLoading = false;

    private boolean mCanLoadMore = true;

    private boolean mHasMore = true;

    private int mYDown;

    private int mLastY;

    public RecyclerRefreshLayout(Context context) {
        this(context, null);
    }

    public RecyclerRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        if (listener != null && !mIsOnLoading) {
            listener.onRefreshing();
        } else
            setRefreshing(false);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Initialize ListView object
        if (mRecycleView == null) {
            getRecycleView();
        }
    }

    /**
     * Get RecyclerView, and support AbsListView later
     */
    private void getRecycleView() {
        if (getChildCount() > 0) {
            View childView = getChildAt(0);
            if (!(childView instanceof RecyclerView)) {
                //Find Recycleview
                childView = findViewById(R.id.refresh_recyclerView);
            }
            if (childView != null && childView instanceof RecyclerView) {
                mRecycleView = (RecyclerView) childView;
                mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (canLoad() && mCanLoadMore) {
                            loadData();
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = (int) event.getRawY();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Whether more can be loaded, if it reaches the bottom
     *
     * @return isCanLoad
     */
    private boolean canLoad() {
        return isScrollBottom() && !mIsOnLoading && isPullUp() && mHasMore;
    }

    /**
     * If you reach the bottom and pull up, execute the onLoad method
     */
    private void loadData() {
        if (listener != null) {
            setOnLoading(true);
            listener.onLoadMore();
        }
    }

    /**
     * Whether it is pull-up operation
     *
     * @return isPullUp
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * Settings loading
     *
     * @param loading Settings loading
     */
    public void setOnLoading(boolean loading) {
        if (!mIsOnLoading) {
            mYDown = 0;
            mLastY = 0;
        }
        mIsOnLoading = loading;
    }

    /**
     * Determine whether to reach the bottom
     */
    private boolean isScrollBottom() {
        return (mRecycleView != null && mRecycleView.getAdapter() != null)
                && getLastVisiblePosition() == (mRecycleView.getAdapter().getItemCount() - 1);
    }

    /**
     * Remember to call after loading
     */
    public void onComplete() {
        setOnLoading(false);
        setRefreshing(false);
        mHasMore = true;
    }

    /**
     * Can I load more
     *
     * @param mCanLoadMore Can I load more
     */
    public void setCanLoadMore(boolean mCanLoadMore) {
        this.mCanLoadMore = mCanLoadMore;
    }

    /**
     * Get the last item visible to RecyclerView
     *
     * @return The last visible position
     */
    public int getLastVisiblePosition() {
        int position;
        if (mRecycleView.getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecycleView.getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = mRecycleView.getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * Get the largest position
     *
     * @param positions Get the largest position
     * @return Get the largest position
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    /**
     * Add Load and Refresh
     *
     * @param listener add the listener for SuperRefreshLayout
     */
    public void setSuperRefreshLayoutListener(SuperRefreshLayoutListener listener) {
        this.listener = listener;
    }

    public interface SuperRefreshLayoutListener {
        void onRefreshing();

        void onLoadMore();
    }
}
