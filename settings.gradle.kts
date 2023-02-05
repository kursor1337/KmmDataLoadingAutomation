pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "KmmDataLoadingAutomation"
include(":shared")


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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