# FuckMIUIAd

[中国語](./README.md) | 日本語

MIUI / HyperOS の広告を低コストで遮断します。

## 注意

一部のモデルのシステムは、次のコンポーネントがシステムアプリとしてマークされているため、pm で無効化することができません。

これまでに見つかった最善の解決策は、それらをサスペンドとしてマークすることです。

ADB デバッグを有効化後にADB がインストールされているコンピューターで以下のコマンドを実行します:
```shell
adb shell pm suspend com.miui.hybrid
adb shell pm suspend com.miui.systemAdSolution
```

または ShizukuRunner を使用して以下のコマンドを実行します:
```shell
pm suspend com.miui.hybrid
pm suspend com.miui.systemAdSolution
```

## 最初のアイデア

メインデバイスについては、root 化やマスクを Flash したくない場合は、最も単純な ADB 権限を使用していくつかの有害な広告を無効化することもできます。これにより、システムの整合性を損なうこともなく MIUI で OFF にできない広告を減らすことができます。

## 無効化すべきコンポーネント

- `com.miui.hybrid` クイックアプリフレームワーク
- `com.miui.systemAdSolution` スマートサービス (オープンスクリーン広告)

- `com.miui.msa.global` グローバル版のため不明

プロジェクトにさらに多くの広告コンポーネントの提出を歓迎します。Issue で報告や `Constants.FUCKLIST` を直接変更して PR を提出できます。

> 現在の目標はユーザーエクスペリエンスに影響を与える部分や、またはユーザーと対話する部分を無効化することであり、Analytics などは無視されます。

## 実現

[Shizuku](https://github.com/RikkaApps/Shizuku) の [API](https://github.com/RikkaApps/Shizuku-API) を使用して実現しています。

このアプリを使用する前に Shizuku をインストールして有効化するか、Sui を使用する必要があります。
