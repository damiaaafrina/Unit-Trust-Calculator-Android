pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.4.1" apply false
        id("org.jetbrains.kotlin.android") version "1.9.22" apply false
        id("org.jetbrains.kotlin.plugin.compose") version "1.6.2" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Unit Trust Calculator"
include(":app")
