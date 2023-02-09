pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmm-data-loading-automation"
include(":kmm-data-loading-automation")


dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            library("coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.6.4")
        }
    }

}