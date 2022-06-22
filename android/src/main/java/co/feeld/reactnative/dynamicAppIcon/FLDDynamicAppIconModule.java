package co.feeld.reactnative.dynamicAppIcon;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import in.myinnos.library.AppIconNameChanger;

import java.util.List;
import java.util.ArrayList;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;

public class FLDDynamicAppIconModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;
    private final String ACTIVE_ICON_NAME = "ACTIVE_ICON_NAME";

    private ArrayList<String> iconNames;

    public FLDDynamicAppIconModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.iconNames = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "RNDynamicAppIcon";
    }

    @ReactMethod
    public void supportsDynamicAppIcon(Promise promise) {
        if (iconNames.isEmpty()) {
            promise.reject(new Exception("Icon names must be set first"));
            return;
        }

        // Activity aliases were introduced on API 1 so this should be available for everyone
        // https://developer.android.com/guide/topics/manifest/activity-alias-element
        promise.resolve(true);
    }

    @ReactMethod
    public void configure(ReadableArray iconNames, Promise promise) {

        try {
            String activeIconName = getCurrentActivityAliasName();
            defineAllIconNames(iconNames);

            if(!iconNames.toArrayList().contains(activeIconName)){
              // we use the first icon as the default one
              setAppIcon(this.iconNames.get(0));
            }else {
              persistActiveIconName(activeIconName);
            }


            promise.resolve(true);
        }  catch(PackageManager.NameNotFoundException e) {
            try {
              defineAllIconNames(iconNames);
              setAppIcon(this.iconNames.get(0));
              promise.resolve(true);
            } catch (Exception e2) {
              promise.reject(e2);
            }
        } catch(Exception e) {
            promise.reject(e);
        }
    }

    private String getCurrentActivityAliasName() throws PackageManager.NameNotFoundException {
        ActivityInfo activityInfo = reactContext.getPackageManager().getActivityInfo(
                reactContext.getCurrentActivity().getComponentName(), PackageManager.GET_META_DATA);
        return activityInfo.name;
    }

    private void persistActiveIconName(String activeIconName)  {
        reactContext.getSharedPreferences(reactContext.getApplicationInfo().name, Context.MODE_PRIVATE)
                .edit()
                .putString(ACTIVE_ICON_NAME, activeIconName)
                .apply();
    }

    private void defineAllIconNames(ReadableArray iconNames) throws Exception {
      if (iconNames == null || iconNames.toArrayList().isEmpty()) {
        throw new Exception("Icon names cannot be null or empty");
      }
      for(Object objectUncastedIconName: iconNames.toArrayList()) {
          this.iconNames.add((String)objectUncastedIconName);
      }
    }

    @ReactMethod
    public void getActiveIconName(Promise promise) {
        if (iconNames.isEmpty()) {
            promise.reject(new Exception("Module not initialized, call the configure method first."));
            return;
        }

        String activeIconName = reactContext
                .getSharedPreferences(reactContext.getApplicationInfo().name, Context.MODE_PRIVATE)
                .getString(ACTIVE_ICON_NAME, iconNames.get(0));
        promise.resolve(activeIconName);
    }

    @ReactMethod
    public void setAppIcon(String iconName) {
      if (iconNames == null || iconNames.size() == 0) {
        return;
      }
      if (iconName == null) {
        iconName = iconNames.get(0);
      }

      String activeName = "";
      List<String> disableNames = new ArrayList<String>();

      for (String candidateIconName : iconNames) {
        if (candidateIconName.equals(iconName)) {
          activeName = iconName;
        } else {
          disableNames.add(candidateIconName);
        }
      }

      if (activeName.isEmpty()) {
        return;
      }

      new AppIconNameChanger.Builder(this.reactContext.getCurrentActivity())
        .activeName(activeName) // String
        .disableNames(disableNames) // List<String>
        .packageName(this.reactContext.getPackageName())
        .build()
        .setNow();
      persistActiveIconName(activeName);
    }
}
