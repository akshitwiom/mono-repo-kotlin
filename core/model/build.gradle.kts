plugins {
    id("monorepo.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.monorepo.core.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections.immutable)
}
