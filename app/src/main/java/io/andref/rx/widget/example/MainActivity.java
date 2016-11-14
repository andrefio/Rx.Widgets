package io.andref.rx.widget.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.andref.rx.widgets.ExpandableButtonGroup;
import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ExpandableButtonGroup.Item> items = new ArrayList<>();

        items.add(new ExpandableButtonGroup.Item("Restaurants", R.drawable.ic_local_dining_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Gas Stations", R.drawable.ic_local_gas_station_black_24dp));
        items.add(new ExpandableButtonGroup.Item("ATMs", R.drawable.ic_local_atm_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Coffee", R.drawable.ic_local_cafe_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Pharmacies", R.drawable.ic_local_pharmacy_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Grocery Stores", R.drawable.ic_local_grocery_store_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Hotels", R.drawable.ic_local_hotel_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Bars", R.drawable.ic_local_bar_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Department Stores", R.drawable.ic_local_mall_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Post Offices", R.drawable.ic_local_post_office_black_24dp));
        items.add(new ExpandableButtonGroup.Item("Parking", R.drawable.ic_local_parking_black_24dp));

        final ExpandableButtonGroup expandableButtonGroup = (ExpandableButtonGroup) findViewById(R.id.expandable_button_group);
        expandableButtonGroup.setItems(items);

        expandableButtonGroup.lessItemClicks()
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        expandableButtonGroup.showLessItems();
                    }
                });

        expandableButtonGroup.moreItemClicks()
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        expandableButtonGroup.showMoreItems();
                    }
                });


        for (Observable<ExpandableButtonGroup.Item> itemss : expandableButtonGroup.itemClicks())
        {
            itemss.subscribe(new Action1<ExpandableButtonGroup.Item>()
            {
                @Override
                public void call(ExpandableButtonGroup.Item item)
                {
                    Toast.makeText(getBaseContext(), item.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
