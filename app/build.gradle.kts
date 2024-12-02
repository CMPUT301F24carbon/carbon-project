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

// JUnit 5 Configuration (move this outside the android block)
tasks.withType<Test> {
    useJUnitPlatform() // This configures JUnit 5 to run the tests
}



dependencies {
    implementation(files("/Users/sarthak/Library/Android/sdk/platforms/android-34/android.jar"))
    // Firebase dependencies
    implementation(platform(libs.firebase.bom)) // Ensure libs.firebase.bom is defined
    implementation(libs.picasso)
    // Navigation bar dependencies
    implementation("com.google.android.material:material:1.9.0")

    // Dependencies for QR scanning and generating
    implementation(libs.zxing.embedded)
    implementation(libs.zxing.core)

    implementation("org.osmdroid:osmdroid-android:6.1.14")

    // Dependencies for testers
    implementation(libs.ext.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.crashlytics.buildtools)

    // JUnit 5 Dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    // Android Test Dependencies
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)

    // Core libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.functions)
}
