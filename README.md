
# React Native Dynamic App Icon

Since iOS 10.3 Apple supports alternate App Icons to be set programmatically. This package integrates this functionality as React Native module. Android is not (yet?) supported.

## Table of Contents

- [Install](#install)
- [Add alternate icons](#add-alternate-icons)
- [Usage](#usage)
- [API](#api)
- [License](#license)

## Install

```
$ npm install react-native-dynamic-app-icon
```

### Mostly automatic installation

```
$ react-native link react-native-dynamic-app-icon
```

### Manual installation

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-dynamic-app-icon` and add `RNDynamicAppIcon.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNDynamicAppIcon.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project

## Add alternate icons

Alternate icons have to be placed directly in your Xcode project rather than inside an asset catalogue. The `@2x` and `@3x` naming convention is supported as usual.

### Adjust `info.plist`

Copy the following to your `info.plist` and adjust it as needed. Omit the file extension (and `@2x`) part, Xcode will pick them accordingly. You can add more alternate icons by copying the an alternate block.

```
<key>CFBundleIcons</key>
<dict>
  <key>CFBundleAlternateIcons</key>
  <dict>
    <key>alternate</key>
      <dict>
        <key>CFBundleIconFiles</key>
        <array>
          <string>AppIcon_alternate</string>
        </array>
        <key>UIPrerenderedIcon</key>
        <false/>
      </dict>
      <key>alternate2</key>
      <dict>
        <key>CFBundleIconFiles</key>
        <array>
          <string>AppIcon_alternate2</string>
        </array>
        <key>UIPrerenderedIcon</key>
        <false/>
      </dict>
    <key>CFBundlePrimaryIcon</key>
    <dict>
      <key>CFBundleIconFiles</key>
      <array>
        <string>FILENAME</string>
      </array>
    </dict>
  </dict>
</dict>
```

## Usage

```javascript
import AppIcon from 'react-native-dynamic-app-icon';

AppIcon.setAppIcon('alternate');
```

### Android

On your `AndroidManifest.xml` file, add the activity aliases for the different icons.

```xml
<activity android:name=".MainActivity" />

<activity-alias
  android:name=".Default"
  android:icon="@mipmap/ic_default"
  android:targetActivity=".MainActivity" />

<activity-alias
  android:name=".IconA"
  android:icon="@mipmap/ic_default"
  android:targetActivity=".MainActivity" />

<activity-alias
  android:name=".IconB"
  android:icon="@mipmap/ic_default"
  android:targetActivity=".MainActivity" />
```

On the JS side:

```javascript
import AppIcon from 'react-native-dynamic-app-icon';

// Set the available icons
AppIcon.configure(['.Default', '.IconA', '.IconB']).then(() =>
  // Change the icon
  AppIcon.setAppIcon('.IconA')
);

```


## Api

### setAppIcon(key: string)

To change the app icon call this method with one of the alternate app icons keys specified in your `plist.info`. To reset to the default app icon pass `null`.

### getActiveIconName(): Promise\<string\>

To retrieve the current active alternative icon name.

### supportsDynamicAppIcon()

Returns a promise which resolves to a boolean.

### configure(alternativeIcons: string[]): Promise\<bool\> (Only Android)

Sets the available alternative icons for the Android app. The icons must be set as a group of [`activity-alias`](https://developer.android.com/guide/topics/manifest/activity-alias-element) on the `AndroidManifest.xml` file.

## License

[MIT](https://github.com/idearockers/react-native-dynamic-app-icon/blob/master/LICENSE) © [idearockers](https://www.idearockers.com/)
