plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mycampuscompanion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mycampuscompanion"
        minSdk = 24
        targetSdk = 36
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common.jvm)
    implementation(libs.room.runtime)
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
// ========== ANDROID CORE ==========
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ========== ROOM (SQLite) ==========
    implementation(libs.room.runtime)
    implementation(libs.room.common.jvm)
    annotationProcessor(libs.room.compiler)

    // ========== RETROFIT (API REST) ==========
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // OkHttp (client HTTP)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // ========== LIFECYCLE (ViewModel + LiveData) ==========
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.common.java8)

    // ========== GOOGLE MAPS ==========
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    // OSMDroid (OpenStreetMap)
    implementation("org.osmdroid:osmdroid-android:6.1.18")

    // ========== GLIDE (Images) ==========
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // ========== SÉCURITÉ ==========
    implementation(libs.security.crypto)

    // ========== GSON ==========
    implementation(libs.gson)

    // ========== TESTS ==========
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}