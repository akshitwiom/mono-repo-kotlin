plugins {
    id("monorepo.android.application.compose")
    id("monorepo.android.hilt")
}

android {
    namespace = "com.monorepo.partner"

    defaultConfig {
        applicationId = "com.monorepo.partner"
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

    // Feature modules
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:tickets"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:team"))

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
