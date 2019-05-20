
#import "React/RCTLog.h"

#import "RNDynamicAppIcon.h"

@implementation RNDynamicAppIcon

RCT_EXPORT_MODULE()

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(setAppIcon:(NSString *)name)
{
  [[UIApplication sharedApplication] setAlternateIconName:name completionHandler:^(NSError * _Nullable error) {
    if (error != nil) {
      RCTLog(@"%@", [error description]);
    }
  }];
}

RCT_REMAP_METHOD(getActiveIconName,
                 getActiveIconNameWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve([[UIApplication sharedApplication] alternateIconName]);
}

RCT_REMAP_METHOD(supportsDynamicAppIcon, resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  bool supported = [[UIApplication sharedApplication] supportsAlternateIcons];
  resolve(@(supported));
}
@end
