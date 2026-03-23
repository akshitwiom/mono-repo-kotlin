import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun CommonExtension<*, *, *, *, *, *>.configureKotlinAndroid(project: Project) {
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    project.extensions.configure<KotlinAndroidProjectExtension> {
        jvmToolchain(17)
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.configureProductFlavors() {
    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://dev-api.example.com/v1/\"")
        }
        create("mock") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://mock-api.example.com/v1/\"")
            buildConfigField("boolean", "USE_MOCK", "true")
        }
        create("qa") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://qa-api.example.com/v1/\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/v1/\"")
        }
    }
}
