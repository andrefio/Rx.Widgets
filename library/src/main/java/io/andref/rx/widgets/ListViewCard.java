package io.andref.rx.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class ListViewCard extends FrameLayout
{
    private static final String TAG = "ListViewCard";

    private List<Item> mItems = new ArrayList<>();

    private PublishSubject<Void> mButtonClicks = PublishSubject.create();
    private PublishSubject<Item> mItemClicks = PublishSubject.create();
    private PublishSubject<Item> mIconClicks = PublishSubject.create();

    private CompositeSubscription mCompositeSubscription;

    private TextView mButton;
    private LinearLayout mContainer;

    private float mAvatarAlpha;
    private int mAvatarTint;
    private String mButtonText;
    private boolean mDenseListItem;
    private float mIconAlpha;

    public ListViewCard(@NonNull Context context)
    {
        super(context);
        initializeViews(context, null, 0, 0);
    }

    public ListViewCard(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initializeViews(context, attrs, 0, 0);
    }

    public ListViewCard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs, defStyleAttr, 0);

    }

    @TargetApi(21)
    public ListViewCard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        mCompositeSubscription.unsubscribe();
        super.onDetachedFromWindow();
    }

    private void initializeViews(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
    {
        String buttonText;

        final TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ListViewCard, defStyleAttr, 0);

        try
        {
            mAvatarAlpha = a.getFloat(R.styleable.ListViewCard_rxw_avatarAlpha, 1f);
            mAvatarTint = a.getColor(R.styleable.ListViewCard_rxw_avatarTint, Color.BLACK);
            mDenseListItem = a.getBoolean(R.styleable.ListViewCard_rxw_denseLayout, false);
            mIconAlpha = a.getFloat(R.styleable.ListViewCard_rxw_iconAlpha, .54f);

            buttonText = a.getString(R.styleable.ListViewCard_rxw_buttonText);
        }
        finally
        {
            a.recycle();
        }

        View cardView = inflate(context, R.layout.rxw_list_view_card, this);

        FrameLayout frameLayout = (FrameLayout) cardView.findViewById(R.id.button);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = (int) (mDenseListItem ? getResources().getDimension(R.dimen.rxw_dense_avatar_with_two_lines_and_icon_tile_height)
                : getResources().getDimension(R.dimen.rxw_avatar_with_two_lines_and_icon_tile_height));
        frameLayout.setLayoutParams(layoutParams);

        mButton = (TextView) cardView.findViewById(R.id.button_text);
        mButton.setText(buttonText);
        mCompositeSubscription.add(RxView.clicks(frameLayout)
                .subscribe(mButtonClicks));

        mContainer = (LinearLayout) cardView.findViewById(R.id.container);
        mContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void layoutViews()
    {
        mContainer.removeAllViews();

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = (int) (mDenseListItem ? getResources().getDimension(R.dimen.rxw_dense_avatar_with_two_lines_and_icon_tile_height)
                : getResources().getDimension(R.dimen.rxw_avatar_with_two_lines_and_icon_tile_height));

        for (int position = 0; position < mItems.size(); position++)
        {
            final Item item = mItems.get(position);
            if (item != null)
            {
                View view = inflate(getContext(), R.layout.rxw_avatar_with_two_lines_and_icon, null);
                view.setLayoutParams(layoutParams);

                TextView textView1 = (TextView) view.findViewById(R.id.text_view_1);
                textView1.setText(item.getLine1());

                TextView textView2 = (TextView) view.findViewById(R.id.text_view_2);
                textView2.setText(item.getLine2());

                ImageView imageView = (ImageView) view.findViewById(R.id.image_view_1);
                ImageButton imageButton = (ImageButton) view.findViewById(R.id.image_button);

                try
                {
                    if (item.getAvatarResourceId() != 0)
                    {
                        imageView.setAlpha(mAvatarAlpha);

                        Drawable drawable = getResources().getDrawable(item.getAvatarResourceId());
                        if (drawable != null)
                        {
                            drawable.mutate().setColorFilter(mAvatarTint, PorterDuff.Mode.SRC_ATOP);
                            imageView.setImageDrawable(drawable);
                        }
                    }
                    else
                    {
                        imageView.setVisibility(INVISIBLE);
                    }
                }
                catch (Resources.NotFoundException exception)
                {
                    imageView.setVisibility(INVISIBLE);
                    Log.e(TAG, "Drawable resource not found: " + exception.getMessage());
                }

                try
                {
                    if (item.getIconResourceId() != 0)
                    {
                        imageButton.setImageResource(item.getIconResourceId());
                        imageButton.setAlpha(mIconAlpha);
                    }
                    else
                    {
                        imageButton.setVisibility(INVISIBLE);
                        imageButton.setClickable(false);
                    }
                }
                catch (Resources.NotFoundException exception)
                {
                    imageView.setVisibility(INVISIBLE);
                    Log.e(TAG, "Drawable resource not found: " + exception.getMessage());
                }

                mCompositeSubscription.add(RxView.clicks(view)
                    .map(new Func1<Void, Item>()
                    {
                        @Override
                        public Item call(Void aVoid)
                        {
                            return item;
                        }
                    })
                    .subscribe(mItemClicks));

                mCompositeSubscription.add(RxView.clicks(imageButton)
                        .map(new Func1<Void, Item>()
                        {
                            @Override
                            public Item call(Void aVoid)
                            {
                                return item;
                            }
                        })
                        .subscribe(mIconClicks));

                mContainer.addView(view);
            }
        }
    }

    // region Getters/Setters

    public CharSequence getButtonText()
    {
        return mButton.getText();
    }

    public void setButtonText(CharSequence text)
    {
        mButton.setText(text);
    }

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

    public Observable<Void> buttonClicks()
    {
        return mButtonClicks;
    }

    public Observable<Item> iconClicks()
    {
        return mIconClicks;
    }

    public Observable<Item> itemClicks()
    {
        return mItemClicks;
    }

    public static class Item<T>
    {
        private int mAvatarResourceId;
        private int mIconResourceId;
        private String mLine1;
        private String mLine2;
        private T mData;

        public Item(String line1, String line2, int avatarResourceId, int iconResourceId)
        {
            this(line1, line2, avatarResourceId, iconResourceId, null);
        }

        public Item(String line1, String line2, int avatarResourceId, int iconResourceId, T data)
        {
            mData = data;
            mAvatarResourceId = avatarResourceId;
            mIconResourceId = iconResourceId;
            mLine1 = line1;
            mLine2 = line2;
        }

        // region Getters/Setters

        public T getData()
        {
            return mData;
        }

        public void setData(T data)
        {
            mData = data;
        }

        public int getAvatarResourceId()
        {
            return mAvatarResourceId;
        }

        public void setAvatarResourceId(int avatarResourceId)
        {
            mAvatarResourceId = avatarResourceId;
        }

        public int getIconResourceId()
        {
            return mIconResourceId;
        }

        public void setIconResourceId(int iconResourceId)
        {
            mIconResourceId = iconResourceId;
        }

        public String getLine1()
        {
            return mLine1;
        }

        public void setLine1(String line1)
        {
            mLine1 = line1;
        }

        public String getLine2()
        {
            return mLine2;
        }

        public void setLine2(String line2)
        {
            mLine2 = line2;
        }

        // endregion
    }
}
