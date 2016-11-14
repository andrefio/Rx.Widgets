# Rx.Widgets

Materially inspired widgets and views.

## Use

### ExpandableButtonGroup

Add the widget to your view:

```xml
    <io.andref.rx.widgets.ExpandableButtonGroup
        android:id="@+id/expandable_button_group"
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:padding="16dp"
        app:backgroundTint="@color/colorAccent"
        app:drawableLess="@drawable/ic_expand_less_black_24dp"
        app:drawableMore="@drawable/ic_expand_more_black_24dp"
        app:drawableTint="@android:color/white"
        app:itemsPerRow="4"
        app:lessText="@string/less"
        app:moreText="@string/more"/>

```

All the available attributes are above and should be self explainatory. Please open an issue if there's one you'd like to see.

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
        for (Observable<ExpandableButtonGroup.Item> item : expandableButtonGroup.itemClicks())
        {
            item.subscribe(new Action1<ExpandableButtonGroup.Item>()
            {
                @Override
                public void call(ExpandableButtonGroup.Item item)
                {
                    Toast.makeText(getBaseContext(), item.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
```

> *NOTE*: Please don't forget to unsubscribe when you're done observing.

## Binaries

[![Release](https://jitpack.io/v/andrefio/Rx.Widgets.svg)](https://jitpack.io/#andrefio/Rx.Widgets)

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
        compile 'com.github.andrefio:Rx.Widgets:1.0.0'
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