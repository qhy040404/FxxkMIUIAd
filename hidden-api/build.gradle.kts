plugins {
    id("com.android.library")
}

android {
    namespace = "com.qhy040404.fxxkmiuiad.hidden_api"
    compileSdk = 34
}

dependencies {
    annotationProcessor("dev.rikka.tools.refine:annotation-processor:4.4.0")
    compileOnly("dev.rikka.tools.refine:annotation:4.4.0")
}
