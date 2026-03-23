plugins {
    id("monorepo.android.application.compose")
    id("monorepo.android.hilt")
}

android {
    namespace = "com.monorepo.rohit"

    defaultConfig {
        applicationId = "com.monorepo.rohit"
        versionCode = 1
        versionName = "1.0.0"
    }
}

dependencies {
    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))

    // Feature modules (no :feature:team or :feature:settings for Rohit)
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:tickets"))

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    // Splash screen
    implementation(libs.androidx.splashscreen)

    // Logging
    implementation(libs.timber)

    // Debug
    debugImplementation(libs.leakcanary)
}
