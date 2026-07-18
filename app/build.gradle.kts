import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val localProperties = Properties()

localProperties.load(rootProject.file("local.properties").inputStream())

val weatherApiKey =
    localProperties.getProperty("WEATHER_API_KEY")
        ?: error("WEATHER_API_KEY is missing from local.properties")

android {
    namespace = "com.example.weathernow"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.weathernow"
        minSdk = 37
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "WEATHER_API_KEY", "\"$weatherApiKey\"")

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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.fragment)
    implementation(libs.material)
    implementation(libs.recyclerview)

    // Assignment 2: OkHttp for WeatherAPI requests
    implementation(libs.okhttp)

    // Assignment 2: ViewModel and LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    implementation(libs.glide)
}