plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "pl.artsoft.rozmowki"
    compileSdk = 34

    defaultConfig {
        applicationId = "pl.artsoft.rozmowki"
        minSdk = 26
        targetSdk = 34
        versionCode = 8
        versionName = "1.7"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // Podpis debug, żeby APK dało się od razu zainstalować
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
