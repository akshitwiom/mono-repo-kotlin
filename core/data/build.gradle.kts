plugins {
    id("monorepo.android.library")
    id("monorepo.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.monorepo.core.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.datastore.preferences)
}
