plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") version
            "4.4.0" apply false
}

android {
    namespace = "com.example.carbon_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.carbon_project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.constraintlayout)

    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation(libs.ext.junit)
    implementation(libs.androidx.espresso.core)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")

    // Libraries for QR scanning and generating
    implementation(libs.zxing.embedded)
    implementation(libs.zxing.core)

}