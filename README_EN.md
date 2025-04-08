# FuckMIUIAd

[中文](./README.md) | English | [日本語](./README_JA.md)

Disable ads in MIUI / Hyper OS with minimal effort.

## Note

On some devices, the system may mark the following components as system apps, making it impossible
to disable them using `pm`.

The best solution currently available is to mark them as suspended.

After enabling ADB debugging, you can execute the following commands on your computer via ADB:

```shell
adb shell pm suspend com.miui.hybrid
adb shell pm suspend com.miui.systemAdSolution
```

Alternatively, use ShizukuRunner to execute the following commands:

```shell
pm suspend com.miui.hybrid
pm suspend com.miui.systemAdSolution
```

## Initial Idea

I didn't want to root or install Magisk on my primary device, so I decided to use the simplest ADB
permissions to disable a few intrusive ads. This approach avoids compromising the system's integrity
while reducing the unavoidable ads in MIUI.

## Currently Disabled Components

- `com.miui.hybrid`: Quick Apps Framework
- `com.miui.systemAdSolution`: Smart Services (Splash Screen Ads)

- `com.miui.msa.global`: Not sure what this is in the global version

Feel free to contribute more ad-related components to the project. You can open an issue or directly
modify `Constants.FUCKLIST` and submit a PR.

> The current principle is to only disable components that affect user experience or involve user
> interaction. Analytics and a few others are not addressed.

## Implementation

Implemented using [Shizuku](https://github.com/RikkaApps/Shizuku) and
its [API](https://github.com/RikkaApps/Shizuku-API).

Before using this app, you need to install and activate Shizuku or use Sui.
