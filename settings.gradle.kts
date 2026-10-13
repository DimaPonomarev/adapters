rootProject.name = "adapters"

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://nexus.europlan.ru/repository/maven/")}
        maven { url = uri("https://nexus.europlan.ru/repository/maven-google/")}
        maven { url = uri("https://nexus.europlan.ru/repository/maven-jitpack/")}
        maven { url = uri("https://proget.europlan.ru/maven2/maven")
            credentials {
                username = "publisher"
                password = "EuroplanPublisher"
            }
        }
    }
}

include(":androidApp")
include(":sharedLogic")
include(":sharedUI")
