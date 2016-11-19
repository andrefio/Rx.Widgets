package io.andref.rx.widget.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.andref.rx.widgets.ExpandableButtonGroup;
import io.andref.rx.widgets.ListViewCard;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity
{
    CompositeSubscription mSubscriptions;

    ExpandableButtonGroup mExpandableButtonGroup;
    ListViewCard mListViewCard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* ExpandableButtonGroup */

        List<ExpandableButtonGroup.Item> expandableButtonGroupItems = new ArrayList<>();

        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Restaurants", R.drawable.ic_local_dining_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Gas Stations", R.drawable.ic_local_gas_station_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("ATMs", R.drawable.ic_local_atm_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Coffee", R.drawable.ic_local_cafe_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Pharmacies", R.drawable.ic_local_pharmacy_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Grocery Stores", R.drawable.ic_local_grocery_store_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Hotels", R.drawable.ic_local_hotel_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Bars", R.drawable.ic_local_bar_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Department Stores", R.drawable.ic_local_mall_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Post Offices", R.drawable.ic_local_post_office_black_24dp));
        expandableButtonGroupItems.add(new ExpandableButtonGroup.Item("Parking", R.drawable.ic_local_parking_black_24dp));

        mExpandableButtonGroup = (ExpandableButtonGroup) findViewById(R.id.expandable_button_group);
        mExpandableButtonGroup.setItems(expandableButtonGroupItems);


        /* ListViewCard **/

        List<ListViewCard.Item> listViewCardItems = new ArrayList<>();

        listViewCardItems.add(new ListViewCard.Item("(804) 555-1234", "Home Phone", R.drawable.ic_phone_black_24dp, R.drawable.ic_sms_black_24dp));
        listViewCardItems.add(new ListViewCard.Item("andrefio@example.com", "Office E-mail", R.drawable.ic_email_black_24dp, 0));

        mListViewCard = (ListViewCard) findViewById(R.id.list_view_card);
        mListViewCard.setButtonText("Button to click");
        mListViewCard.setItems(listViewCardItems);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mSubscriptions = new CompositeSubscription();

        /* ExpandableButtonGroup */

        mSubscriptions.add(
                mExpandableButtonGroup.lessItemClicks()
                        .subscribe(new Action1<Void>()
                        {
                            @Override
                            public void call(Void aVoid)
                            {
                                mExpandableButtonGroup.showLessItems();
                            }
                        })
        );

        mSubscriptions.add(
                mExpandableButtonGroup.moreItemClicks()
                        .subscribe(new Action1<Void>()
                        {
                            @Override
                            public void call(Void aVoid)
                            {
                                mExpandableButtonGroup.showMoreItems();
                            }
                        })
        );

        mSubscriptions.add(
                mExpandableButtonGroup.itemClicks()
                        .subscribe(new Action1<ExpandableButtonGroup.Item>()
                        {
                            @Override
                            public void call(ExpandableButtonGroup.Item item)
                            {
                                Toast.makeText(getBaseContext(), item.getText(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );


        /* ListViewCard **/

        mSubscriptions.add(
                mListViewCard.itemClicks()
                        .subscribe(new Action1<ListViewCard.Item>()
                        {
                            @Override
                            public void call(ListViewCard.Item item)
                            {
                                Toast.makeText(getBaseContext(), item.getLine1(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );

        mSubscriptions.add(
                mListViewCard.iconClicks()
                        .subscribe(new Action1<ListViewCard.Item>()
                        {
                            @Override
                            public void call(ListViewCard.Item item)
                            {
                                Toast.makeText(getBaseContext(), "Icon Clicked", Toast.LENGTH_SHORT).show();
                            }
                        })
        );

        mSubscriptions.add(
                mListViewCard.buttonClicks()
                        .subscribe(new Action1<Void>()
                        {
                            @Override
                            public void call(Void aVoid)
                            {
                                Toast.makeText(getBaseContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                            }
                        })
        );

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSubscriptions.unsubscribe();
    }
}
