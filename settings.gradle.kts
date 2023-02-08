pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "kmm-data-loading-automation"
include(":shared")


dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            library("coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.6.4")
        }
    }

}