plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    /* HTTP */
    kotlin("plugin.serialization") version "1.9.0"

    /* Room */
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.pokexplore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokexplore"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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

    /* HTTP */
    val ktorVersion = "2.3.8"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")

    /* Room */
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation ("com.google.code.gson:gson:2.8.8") //Array Converter
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0") //To make .collectAsStateWithLifecycle() work

    /* Navigation */
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation ("androidx.compose.material:material-icons-extended")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    /* GPS */
    implementation("com.google.android.gms:play-services-location:21.2.0")

    /* Pokemon Card Elements*/
    implementation ("io.coil-kt:coil:2.0.0")
    implementation ("io.coil-kt:coil-compose:2.0.0")
    implementation ("com.google.accompanist:accompanist-flowlayout:0.24.8-beta")

    /* Camera */
    implementation ("androidx.camera:camera-camera2:1.2.2")
    implementation ("androidx.camera:camera-lifecycle:1.2.2")
    implementation ("androidx.camera:camera-view:1.2.2")
    implementation ("com.google.mlkit:barcode-scanning:17.1.0")

    /* Biometric */
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
}