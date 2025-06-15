plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.aiartservice"
    compileSdk = 35
    val publicKeyProduct = file("../app/key/public_key_product").readText().trim().replace(Regex("-----BEGIN PUBLIC KEY-----|-----END PUBLIC KEY-----|\\n"), "").trim()
    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField ("String", "PUBLIC_KEY", "\"${publicKeyProduct}\"")
        buildConfigField ("String", "URL_TIME_STAMP", "\"https://api-img-gen-wrapper.apero.vn\"")
        buildConfigField ("String", "URL_AI_STYLE", "\"https://api-style-manager.apero.vn/\"")
        buildConfigField ("String", "URL_AI_ART", "\"https://api-img-gen-wrapper.apero.vn\"")
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
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.api.signature)
    implementation (libs.glide)
    implementation("androidx.lifecycle:lifecycle-process:2.8.7")

}