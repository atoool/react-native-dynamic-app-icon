package co.feeld.reactnative.dynamicAppIcon;

import in.myinnos.library.AppIconNameChanger;

import java.util.List;
import java.util.ArrayList;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

public class FLDDynamicAppIconModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    private ArrayList<String> iconNames;

    public FLDDynamicAppIconModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.iconNames = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "FLDDynamicAppIconModule";
    }

    @ReactMethod
    public void defineAllIconNames(ReadableArray iconNames) {
      if (iconNames == null) {
        return;
      }
      for(Object objectUncastedIconName: iconNames.toArrayList()) {
          this.iconNames.add((String)objectUncastedIconName);
      }
    }

    @ReactMethod
    public void setAlternateIconName(String iconName) {
      if (iconNames == null || iconNames.size() == 0) {
        return;
      }
      if (iconName == null) {
        iconName = iconNames.get(0);
      }

      String activeName = "";
      List<String> disableNames = new ArrayList<String>();

      for (String candidateIconName : iconNames) {
        if (candidateIconName == iconName) {
          activeName = iconName;
        } else {
          disableNames.add(candidateIconName);
        }
      }

      if (activeName == "") {
        return;
      }

      new AppIconNameChanger.Builder(MainActivity.this)
        .activeName(activeName) // String
        .disableNames(disableNames) // List<String>
        .packageName(BuildConfig.APPLICATION_ID)
        .build()
        .setNow();
    }
}
