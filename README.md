# Rapid
 Hello World!
 
 A library that automatically resize your XML layout with device width and height.

## SDK Information
minSDKVersion: 16 & maxSDKVersion: 30
[![](https://jitpack.io/v/fiftyonemoon/EmojiLibrary.svg)](https://jitpack.io/#fiftyonemoon/EmojiLibrary)
 
 ## Implementation

```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```groovy
implementation 'com.github.fiftyonemoon:Rapid:1.0.0'
```

## Device Sample

(1) 720x1280 	(2) 1080x1920

<img src="./samples/720 x 1280.png" alt="720x1280" width="270"> <img src="./samples/1080 x 1920.png" alt="1080x1920" width="270">

## Overview

This library have 5 modified views which is based on Android original views:

- [`RapidConstraintLayout`](resize/src/main/java/com/fom/rapid/resize/RapidConstraintLayout.java)
- [`RapidRelativeLayout`](resize/src/main/java/com/fom/rapid/resize/RapidRelativeLayout.java)
- [`RapidLinearLayout`](resize/src/main/java/com/fom/rapid/resize/RapidLinearLayout.java)
- [`RapidFrameLayout`](resize/src/main/java/com/fom/rapid/resize/RapidFrameLayout.java)
- [`RapidImageView`](resize/src/main/java/com/fom/rapid/resize/RapidImageView.java)

## XML Usage
This is sample of layout.

- [`activity_main`](app/src/main/res/layout/activity_main.xml)

(1) [`RapidConstraintLayout`]

```
<?xml version="1.0" encoding="utf-8"?>
<com.fom.rapid.resize.RapidConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal"
    android:gravity="center"
    android:background="@drawable/bg"
    app:measureWith="width"  //Measure view with width
    app:measureMargin="false" //Keep applied margin as it is, don't change on resize
    app:resizeChildren="true" //Resize this parent (RapidConstraintLayout) children
    tools:context=".MainActivity">

    <!--Add your views here-->

</com.fom.rapid.resize.RapidConstraintLayout>
```
