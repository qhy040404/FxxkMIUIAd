plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.qhy040404.fxxkmiuiad"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.qhy040404.fxxkmiuiad"
        minSdk = 29
        targetSdk = 33
        versionCode = 2
        versionName = "1.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
            packagingOptions.resources.excludes += setOf(
                "DebugProbesKt.bin",
                "META-INF/*.version"
            )
            dependenciesInfo.includeInApk = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation("com.google.android.material:material:1.7.0")
    implementation("dev.rikka.shizuku:api:12.2.0")
    implementation("dev.rikka.shizuku:provider:12.2.0")
}