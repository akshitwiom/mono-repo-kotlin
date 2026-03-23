plugins {
    id("monorepo.android.library.compose")
}

android {
    namespace = "com.monorepo.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}
