plugins {
    id("monorepo.android.library")
    id("monorepo.android.hilt")
}

android {
    namespace = "com.monorepo.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
}
