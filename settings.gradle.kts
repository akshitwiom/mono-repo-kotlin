pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "mono-repo-kotlin"

// ── Apps ──
include(":app:partner")
include(":app:rohit")

// ── Core modules ──
include(":core:common")
include(":core:model")
include(":core:data")
include(":core:network")
include(":core:database")
include(":core:designsystem")
include(":core:ui")

// ── Feature modules ──
include(":feature:auth")
include(":feature:home")
include(":feature:tickets")
include(":feature:settings")
include(":feature:team")
