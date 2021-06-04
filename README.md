# Rapid
 Hello World!
 
 A library that automatically resize your XML layout with device width and height.
 No matter your device orientation is protrait or landscape.
 
## Important
- Create your design with default 1080x1920 screen size.
- For better experience hide navigation and status/notification bar.

## SDK Information
minSDKVersion: 16 & maxSDKVersion: 30
[![](https://jitpack.io/v/fiftyonemoon/EmojiLibrary.svg)](https://jitpack.io/#fiftyonemoon/EmojiLibrary)
 
 ## Implementation

```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

```groovy
implementation 'com.github.fiftyonemoon:Rapid:1.0.0'
```
## Overview

This library have 5 modified views which is based on Android original views:

- [`RapidConstraintLayout`](resize/src/main/java/com/fom/rapid/resize/RapidConstraintLayout.java)
- [`RapidRelativeLayout`](resize/src/main/java/com/fom/rapid/resize/RapidRelativeLayout.java)
- [`RapidLinearLayout`](resize/src/main/java/com/fom/rapid/resize/RapidLinearLayout.java)
- [`RapidFrameLayout`](resize/src/main/java/com/fom/rapid/resize/RapidFrameLayout.java)
- [`RapidImageView`](resize/src/main/java/com/fom/rapid/resize/RapidImageView.java)

## Attrs Usage

There are 4 main attributes will be used:

- [`measureWith`](resize/src/main/res/values/attrs.xml) Which will resize your view with device width and height.
- [`measureMargin`](resize/src/main/res/values/attrs.xml) Set true will resize your applied margin and false will keep applied margin as it is.
- [`measurePadding`](resize/src/main/res/values/attrs.xml) Set true will resize your applied padding and false will keep applied padding as it is.
- [`resizeChildren`](resize/src/main/res/values/attrs.xml) Set true will resize your parent view childs automatically and false will resize only parent view.

- [`attrs`](resize/src/main/res/values/attrs.xml) For more info check out the attrs and their usage.

## XML Usage

- [`rapid_constraint_layout`](app/src/main/res/layout/rapid_constraint_layout.xml) RapidConstraintLayout sample.
- [`rapid_relative_layout`](app/src/main/res/layout/rapid_relative_layout.xml) RapidReplativeLayout sample.
- [`rapid_image_view`](app/src/main/res/layout/rapid_image_view.xml) RapidImageView sample.


## Device Sample

(1) 720x1280 	(2) 1080x1920

<img src="./samples/720 x 1280.png" alt="720x1280" width="270"> <img src="./samples/1080 x 1920.png" alt="1080x1920" width="270">

(3) Landscape 720x1280

<img src="./samples/landscape.png" alt="720x1280" width="545">
