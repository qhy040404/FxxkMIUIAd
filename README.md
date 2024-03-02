# FuckMIUIAd

用最小的代价关闭 MIUI / 澎湃 OS 的广告。

## 最初的想法

主力机不想 root ，也不想刷面具，不如用最简单的 adb 权限来禁用掉几个毒瘤广告，既不破坏系统完整性，也能减少 MIUI 那些关不掉的广告

## 目前禁用的组件

- `com.miui.hybrid` 快应用框架
- `com.miui.systemAdSolution` 智能服务（开屏广告）

欢迎向项目提交更多的广告组件, 可以开 issue, 也可以直接修改 `Constants.FUCKLIST` 并提 PR

> 目前的宗旨是只禁用影响用户体验的，或者说与用户有交互的部分，所以没有管Analytics和其他几个。

## 实现

使用 [Shizuku](https://github.com/RikkaApps/Shizuku) 的 [API](https://github.com/RikkaApps/Shizuku-API) 实现

使用此 app 前需先安装 Shizuku 并激活, 或使用 Sui