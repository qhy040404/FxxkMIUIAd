plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.qhy04.gradle.android.res_opt")
}

android {
    namespace = "com.qhy040404.fxxkmiuiad"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.qhy040404.fxxkmiuiad"
        minSdk = 29
        targetSdk = 36
        versionCode = 12
        versionName = "1.4.2"

        @Suppress("UnstableApiUsage")
        androidResources.localeFilters.addAll(
            setOf(
                "en",
                "ja",
                "zh-rCN"
            )
        )
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk.abiFilters.add("")
            packagingOptions.resources.excludes += setOf(
                "DebugProbesKt.bin",
                "META-INF/*.version",
                "**LICENSE**"
            )
            dependenciesInfo.includeInApk = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
}

configurations.all {
    exclude("androidx.appcompat", "appcompat")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
}

dependencies {
    compileOnly(project(":hidden-api"))

    implementation("androidx.annotation:annotation:1.9.1")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:6.1")

    implementation(platform("androidx.compose:compose-bom:2025.03.01"))
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")

    implementation("androidx.activity:activity-compose:1.10.1")
}
