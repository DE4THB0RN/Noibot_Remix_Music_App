plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.noibot_remix_z"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.noibot_remix_z"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.1"

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val media3_version = "1.4.1"

    implementation("androidx.media3:media3-session:1.4.1")
    implementation("androidx.media3:media3-common:1.4.1")
    implementation("androidx.media3:media3-container:1.4.1")
    implementation("androidx.media3:media3-database:1.4.1")
    implementation("androidx.media3:media3-datasource:1.4.1")
    implementation("androidx.media3:media3-decoder:1.4.1")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-extractor:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.5")


    implementation("com.google.dagger:hilt-android:2.54")
    kapt("com.google.dagger:hilt-android-compiler:2.54")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.54")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.54")

    testImplementation("com.google.dagger:hilt-android-testing:2.54")
    kaptTest("com.google.dagger:hilt-android-compiler:2.54")


    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("androidx.navigation:navigation-runtime:2.8.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")
    implementation("ir.mahozad.multiplatform:wavy-slider:2.0.0")
    implementation("androidx.appcompat:appcompat:1.7.0")


}

kapt{
    correctErrorTypes = true
}