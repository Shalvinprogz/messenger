pluginManagement {
    repositories {
        maven("https://jitpack.io") // Add JitPack repository here
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://jitpack.io") // Add JitPack repository here
        google()
        mavenCentral()
    }
}

rootProject.name = "My Application"
include(":app")
