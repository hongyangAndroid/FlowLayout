package com.zhy.view.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.text.TextUtilsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private static final int LEFT = -1;
    private static final int CENTER = 0;
    private static final int RIGHT = 1;

    protected List<List<View>> mAllViews = new ArrayList<List<View>>();
    protected List<Integer> mLineHeight = new ArrayList<Integer>();
    protected List<Integer> mLineWidth = new ArrayList<Integer>();
    private int mGravity;
    private List<View> lineViews = new ArrayList<>();
    private int mTagVerticalPadding, mTagHorizontalPadding;
    private boolean mAutoStretch;

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mGravity = ta.getInt(R.styleable.TagFlowLayout_tag_gravity, LEFT);
        mTagVerticalPadding = ta.getDimensionPixelSize(R.styleable.TagFlowLayout_vertical_padding, 0);
        mTagHorizontalPadding = ta.getDimensionPixelSize(R.styleable.TagFlowLayout_horizontal_padding, 0);
        mAutoStretch = ta.getBoolean(R.styleable.TagFlowLayout_auto_stretch, false);
        int layoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault());
        if (layoutDirection == LayoutDirection.RTL) {
            if (mGravity == LEFT) {
                mGravity = RIGHT;
            } else {
                mGravity = LEFT;
            }
        }
        ta.recycle();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                if (i == cCount - 1) {
                    lineWidth -= mTagHorizontalPadding;
                    width = Math.max(lineWidth, width);
                    height += lineHeight - mTagVerticalPadding;
                }
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin + mTagVerticalPadding;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                lineWidth -= mTagHorizontalPadding;
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth + mTagHorizontalPadding;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {
                lineWidth -= mTagHorizontalPadding;
                width = Math.max(lineWidth, width);
                height += lineHeight - mTagVerticalPadding;
            }
        }
        setMeasuredDimension(
                //
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()//
        );

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();
        lineViews.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) continue;
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin + mTagVerticalPadding;

            if (childWidth + lineWidth > width - getPaddingLeft() - getPaddingRight()) {
                lineWidth -= mTagHorizontalPadding;
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                mLineWidth.add(lineWidth);

                lineWidth = 0;
                lineHeight = childHeight;
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth + mTagHorizontalPadding;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);

        }
        lineWidth -= mTagHorizontalPadding;
        lineHeight -= mTagVerticalPadding;
        mLineHeight.add(lineHeight);
        mLineWidth.add(lineWidth);
        mAllViews.add(lineViews);


        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mAllViews.size();

        if (mAutoStretch) {
            for (int i = 0; i < lineNum; i++) {
                lineViews = mAllViews.get(i);
                lineHeight = mLineHeight.get(i);

                int cTotalWidth = 0;
                final int lineChildCount = lineViews.size();
                for (int j = 0; j < lineChildCount; j++) {
                    View child = lineViews.get(j);
                    if (child.getVisibility() == View.GONE) {
                        continue;
                    }
                    cTotalWidth += child.getMeasuredWidth();
                }
                int gap = 0;
                if (lineChildCount > 1) {
                    gap = (width - cTotalWidth) / (lineChildCount - 1);
                }
                left = getPaddingLeft();
                if (this.mGravity == RIGHT) {
                    Collections.reverse(lineViews);
                }
                for (int j = 0; j < lineChildCount; j++) {
                    View child = lineViews.get(j);
                    if (child.getVisibility() == View.GONE) {
                        continue;
                    }
                    int lc = left;
                    int rc = lc + child.getMeasuredWidth();
                    int bc = top + child.getMeasuredHeight();

                    child.layout(lc, top, rc, bc);

                    left += child.getMeasuredWidth() + gap;
                }
                top += lineHeight;
            }
        } else {
            for (int i = 0; i < lineNum; i++) {
                lineViews = mAllViews.get(i);
                lineHeight = mLineHeight.get(i);

                // set gravity
                int currentLineWidth = this.mLineWidth.get(i);
                switch (this.mGravity) {
                    case LEFT:
                        left = getPaddingLeft();
                        break;
                    case CENTER:
                        left = (width - currentLineWidth) / 2 + getPaddingLeft();
                        break;
                    case RIGHT:
                        //  适配了rtl，需要补偿一个padding值
                        left = width - (currentLineWidth + getPaddingLeft()) - getPaddingRight();
                        //  适配了rtl，需要把lineViews里面的数组倒序排
                        Collections.reverse(lineViews);
                        break;
                }

                for (int j = 0; j < lineViews.size(); j++) {
                    View child = lineViews.get(j);
                    if (child.getVisibility() == View.GONE) {
                        continue;
                    }

                    MarginLayoutParams lp = (MarginLayoutParams) child
                            .getLayoutParams();

                    int lc = left + lp.leftMargin;
                    int tc = top + lp.topMargin;
                    int rc = lc + child.getMeasuredWidth();
                    int bc = tc + child.getMeasuredHeight();

                    child.layout(lc, tc, rc, bc);

                    left += child.getMeasuredWidth() + lp.leftMargin
                            + lp.rightMargin + mTagHorizontalPadding;
                }
                top += lineHeight;
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
