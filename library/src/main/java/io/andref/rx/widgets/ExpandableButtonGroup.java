package io.andref.rx.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class ExpandableButtonGroup extends FrameLayout
{
    private static final String TAG = "ExpandableButtonGroup";
    private static final String SAVED_INSTANCE_STATE = "savedInstanceState";
    private static final String IS_VISIBLE = "isVisible";

    private List<Item> mItems = new ArrayList<>();

    private Observable<Item> mItemClicks;
    private Observable<Void> mLessItemsClicks;
    private Observable<Void> mMoreItemsClicks;

    private int mBackgroundTint;
    private int mDrawableLess;
    private int mDrawableMore;
    private int mDrawableTint;

    private int mItemsPerRow;

    private String mLessText;
    private String mMoreText;

    private FrameLayout mContainer1;
    private LinearLayout mContainer2;

    private View mMoreItemsButton;
    private View mLessItemsButton;
    private View mHiddenButton;

    LinearLayout.LayoutParams mWeightedLayoutParams;

    public ExpandableButtonGroup(@NonNull Context context)
    {
        super(context);
        initializeViews(context, null, 0, 0);
    }

    public ExpandableButtonGroup(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initializeViews(context, attrs, 0, 0);
    }

    public ExpandableButtonGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs, defStyleAttr, 0);

    }

    @TargetApi(21)
    public ExpandableButtonGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initializeViews(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
    {
        final TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ExpandableButtonGroup, defStyleAttr, 0);

        try
        {
            mBackgroundTint = a.getColor(R.styleable.ExpandableButtonGroup_backgroundTint, Color.BLUE);
            mDrawableLess = a.getResourceId(R.styleable.ExpandableButtonGroup_drawableLess, 0);
            mDrawableMore = a.getResourceId(R.styleable.ExpandableButtonGroup_drawableMore, 0);
            mDrawableTint = a.getColor(R.styleable.ExpandableButtonGroup_drawableTint, Color.WHITE);
            mItemsPerRow = a.getInt(R.styleable.ExpandableButtonGroup_itemsPerRow, 4);
            mLessText = a.getString(R.styleable.ExpandableButtonGroup_lessText);
            mMoreText = a.getString(R.styleable.ExpandableButtonGroup_moreText);
        }
        finally
        {
            a.recycle();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.expandable_button_group, this);

        mLessItemsButton = createItemView(getContext(), mLessText, mBackgroundTint, mDrawableLess, mDrawableTint);
        mLessItemsClicks = RxView.clicks(mLessItemsButton);

        mMoreItemsButton = createItemView(getContext(), mMoreText, mBackgroundTint, mDrawableMore, mDrawableTint);
        mMoreItemsClicks = RxView.clicks(mMoreItemsButton);

        // There are two containers. The first, the top row, is always visible. The second,
        // expandable rows, is what is shown or hidden depending on button clicks.
        mContainer1 = (FrameLayout) findViewById(R.id.container_1);
        mContainer2 = (LinearLayout) findViewById(R.id.container_2);

        mWeightedLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        super.onRestoreInstanceState(((Bundle) state).getParcelable(SAVED_INSTANCE_STATE));

        int visibility = ((Bundle) state).getInt(IS_VISIBLE);
        if (visibility == VISIBLE)
        {
            mContainer2.setVisibility(VISIBLE);
            mMoreItemsButton.setVisibility(GONE);
        }
        else
        {
            mContainer2.setVisibility(GONE);
            mMoreItemsButton.setVisibility(VISIBLE);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        super.onSaveInstanceState();

        Bundle bundle = new Bundle();
        bundle.putInt(IS_VISIBLE, mContainer2.getVisibility());
        bundle.putParcelable(SAVED_INSTANCE_STATE, super.onSaveInstanceState());

        return bundle;
    }

    private View createItemView(Context context, String text, int backgroundTint, int drawableId, int drawableTint)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.expandable_button_group_item, null);

        FloatingActionButton floatingActionButton = (FloatingActionButton) itemView.findViewById(R.id.floating_action_button);
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(backgroundTint));

        try
        {
            Drawable drawable = context.getResources().getDrawable(drawableId);
            if (drawable != null)
            {
                drawable.mutate().setColorFilter(drawableTint, PorterDuff.Mode.SRC_ATOP);
                floatingActionButton.setImageDrawable(drawable);
            }
        }
        catch (Resources.NotFoundException exception)
        {
            Log.e(TAG, "Drawable resource not found: " + exception.getMessage());
        }

        TextView textView = (TextView) itemView.findViewById(R.id.text_view);
        textView.setText(text);

        return itemView;
    }

    private void layoutViews()
    {
        LinearLayout rowView = new LinearLayout(getContext());
        rowView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        List<Observable<Item>> itemObservables = new ArrayList<>();

        for (int position = 0; position < mItems.size(); position++)
        {
            // Start a new row when the previous one is filled.
            if (position > 0 && position % mItemsPerRow == 0)
            {
                if (position <= mItemsPerRow)
                {
                    mContainer1.addView(rowView);
                }
                else
                {
                    mContainer2.addView(rowView);
                }

                rowView = new LinearLayout(getContext());
                rowView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            final Item item = mItems.get(position);
            if (item != null)
            {
                View itemView = createItemView(getContext(), item.getText(), mBackgroundTint, item.getImageDrawable(), mDrawableTint);

                // If this is the last item in the top row, and there are
                // more to display, we want to show a "more" button.
                if (position == mItemsPerRow - 1 && mItems.size() > mItemsPerRow)
                {
                    RelativeLayout relativeLayout = new RelativeLayout(getContext());
                    relativeLayout.setLayoutParams(mWeightedLayoutParams);
                    relativeLayout.setGravity(Gravity.CENTER);

                    mHiddenButton = itemView;
                    mHiddenButton.setVisibility(mContainer2.getVisibility());

                    relativeLayout.addView(mHiddenButton);
                    relativeLayout.addView(mMoreItemsButton);

                    rowView.addView(relativeLayout);
                }
                else
                {
                    rowView.addView(itemView, mWeightedLayoutParams);
                }

                itemObservables.add(
                        RxView.clicks(itemView)
                                .map(new Func1<Void, Item>()
                                {
                                    @Override
                                    public Item call(Void aVoid)
                                    {
                                        return item;
                                    }
                                })
                );
            }

            mItemClicks = Observable.merge(itemObservables);
        }

        // Just add the "less" button if the last row doesn't have maximum items per row yet.
        if (rowView.getChildCount() < mItemsPerRow)
        {
            rowView.addView(mLessItemsButton, mWeightedLayoutParams);
            mContainer2.addView(rowView);
        }

        // Otherwise, add the existing row, then make a new one to add the "less" button to.
        else
        {
            mContainer2.addView(rowView);

            rowView = new LinearLayout(getContext());
            rowView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            rowView.addView(mLessItemsButton, mWeightedLayoutParams);

            mContainer2.addView(rowView);
        }
    }

    // region Getters/Setters

    public void addItem(Item item)
    {
        mItems.add(item);
    }

    public List<Item> getItems()
    {
        return mItems;
    }

    public void setItems(List<Item> items)
    {
        mItems = items;
        layoutViews();
    }

    // endregion

    public Observable<Item> itemClicks()
    {
        return mItemClicks;
    }

    public Observable<Void> lessItemClicks()
    {
        return mLessItemsClicks;
    }

    public Observable<Void> moreItemClicks()
    {
        return mMoreItemsClicks;
    }

    public void showMoreItems()
    {
        mContainer2.setVisibility(VISIBLE);
        mHiddenButton.setVisibility(VISIBLE);
        mMoreItemsButton.setVisibility(GONE);
    }

    public void showLessItems()
    {
        mContainer2.setVisibility(GONE);
        mHiddenButton.setVisibility(GONE);
        mMoreItemsButton.setVisibility(VISIBLE);
    }

    public static class Item<T>
    {
        private int mImageDrawable;
        private String mText;
        private T mData;

        public Item(String text, int imageDrawable)
        {
            this(text, imageDrawable, null);
        }

        public Item(String text, int imageDrawable, T data)
        {
            mData = data;
            mImageDrawable = imageDrawable;
            mText = text;
        }

        // region Getters/Setters

        public T getData()
        {
            return mData;
        }

        public void setmData(T data)
        {
            mData = data;
        }

        public int getImageDrawable()
        {
            return mImageDrawable;
        }

        public void setImageDrawable(int imageDrawable)
        {
            mImageDrawable = imageDrawable;
        }

        public String getText()
        {
            return mText;
        }

        public void setText(String text)
        {
            mText = text;
        }

        // endregion
    }
}