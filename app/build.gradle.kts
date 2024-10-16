plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dev.rikka.tools.refine")
}

android {
    namespace = "com.qhy040404.fxxkmiuiad"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.qhy040404.fxxkmiuiad"
        minSdk = 29
        targetSdk = 34
        versionCode = 7
        versionName = "1.2.3"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
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
            packagingOptions.resources.excludes += setOf(
                "DebugProbesKt.bin",
                "META-INF/*.version"
            )
            dependenciesInfo.includeInApk = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

configurations.all {
    exclude("androidx.appcompat", "appcompat")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
}

dependencies {
    compileOnly(project(":hidden-api"))

    implementation("androidx.annotation:annotation:1.9.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("dev.rikka.rikkax.appcompat:appcompat:1.6.1")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    implementation("dev.rikka.tools.refine:runtime:4.4.0")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
}