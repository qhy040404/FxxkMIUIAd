plugins {
    id("com.android.library")
}

android {
    namespace = "com.qhy040404.fxxkmiuiad.hidden_api"
    compileSdk = 35

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.9.1")
}
