# Rx.Widgets
[![Release](https://jitpack.io/v/io.andref/Rx.Widgets.svg)](https://jitpack.io/#io.andref/Rx.Widgets)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

Materially inspired widgets and views.

## Use

### ExpandableButtonGroup

Add the widget to your view:

```xml
    <io.andref.rx.widgets.ExpandableButtonGroupLayout
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:padding="16dp"
        app:rxw_backgroundTint="@color/colorAccent"
        app:rxw_drawableLess="@drawable/ic_expand_less_black_24dp"
        app:rxw_drawableMore="@drawable/ic_expand_more_black_24dp"
        app:rxw_drawableTint="@android:color/white"
        app:rxw_itemsPerRow="4"
        app:rxw_lessText="@string/less"
        app:rxw_moreText="@string/more"/>

```

All the available attributes are above and should be self explanatory. Please open an issue if there's one you'd like to see.

Next, give the view some items to display:

```java
    List<ExpandableButtonGroup.Item> items = new ArrayList<>();
    items.add([...])

    final ExpandableButtonGroup expandableButtonGroup = (ExpandableButtonGroup) findViewById(R.id.expandable_button_group);
    expandableButtonGroup.setItems(items);
```

The view won't do anything until you subscribe to the exposed observables:

```java

        // Subscribe to "less" button clicks.
        expandableButtonGroup.lessItemClicks()
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        expandableButtonGroup.showLessItems();
                    }
                });

        // Subscribe to "more" button clicks.
        expandableButtonGroup.moreItemClicks()
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        expandableButtonGroup.showMoreItems();
                    }
                });


        // Subscribe to all the other item clicks.
        expandableButtonGroup.itemClicks()
                .subscribe(new Action1<ExpandableButtonGroup.Item>()
                {
                    @Override
                    public void call(ExpandableButtonGroup.Item item)
                    {
                        Toast.makeText(getBaseContext(), item.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
```

### ListViewCard

Add the widget to your view:

```xml
    <io.andref.rx.widgets.ListViewCard
        android:id="@+id/list_view_card"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:layout_width="match_parent"
        app:rxw_avatarAlpha="1"
        app:rxw_avatarTint="@color/colorAccent"
        app:rxw_denseLayout="true"
        app:rxw_iconAlpha=".27"/>
```

All the available attributes are above and should be self explanatory. Please open an issue if there's one you'd like to see.

Next, give the view some items to display:

```java
        List<ListViewCard.Item> listViewCardItems = new ArrayList<>();
        listViewCardItems.add([...]);

        mListViewCard = (ListViewCard) findViewById(R.id.list_view_card);
        mListViewCard.setButtonText("");
        mListViewCard.setItems(listViewCardItems);

```

To handle the clicks you must subscribe to the exposed observables:

```java

    listViewCard.itemClicks()
            .subscribe(new Action1<ListViewCard.Item>()
            {
                @Override
                public void call(ListViewCard.Item item)
                {
                    Toast.makeText(getBaseContext(), item.getLine1(), Toast.LENGTH_SHORT).show();
                }
            });


    listViewCard.iconClicks()
            .subscribe(new Action1<ListViewCard.Item>()
            {
                @Override
                public void call(ListViewCard.Item item)
                {
                    Toast.makeText(getBaseContext(), "Icon Clicked", Toast.LENGTH_SHORT).show();
                }
            });

    listViewCard.buttonClicks()
            .subscribe(new Action1<Void>()
            {
                @Override
                public void call(Void aVoid)
                {
                    Toast.makeText(getBaseContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                }
            });

```

> *Fun Fact:* Even though this widget is called `ListViewCard` it does not contain a `ListView`. So, why call it that? Because we felt it best conveyed what this widget is supposed to do: Display a list of items inside a card.

## Friendly Reminder

Don't forget to unsubscribe when you're done observing!

## Note

This library is under active development so expect some breaking changes as we ramp up. We'll note these changes in the release notes.

## Binaries

Add the JitPack repository to your root build.gradle at the end of repositories:

```groovy
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
   }
```

And then add this library to your project:

```groovy
   dependencies {
        compile 'io.andref:Rx.Widgets:1.1.0'
   }
```

Or if you want a specific revision:

```groovy
   dependencies {
        compile 'io.andref:Rx.Widgets:5dda428'
   }
```

## License

Copyright 2016 Michael De Soto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.