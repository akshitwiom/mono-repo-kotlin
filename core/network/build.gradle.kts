plugins {
    id("monorepo.android.library")
    id("monorepo.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.monorepo.core.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.bundles.networking)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
}
