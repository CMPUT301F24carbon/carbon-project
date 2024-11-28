plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.carbon_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.carbon_project"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    implementation("com.squareup.picasso:picasso:2.71828")



    implementation(libs.appcompat)
    implementation(libs.material)
//    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    /*    implementation(libs.car.ui.lib)
        implementation(libs.firebase.firestore)
        implementation(libs.firebase.auth)
        implementation(libs.firebase.storage)
        implementation(libs.recyclerview)*/
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}