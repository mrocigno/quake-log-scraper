rootProject.name = "quake-log-scrapper"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("./gradle/libraries.toml"))
        }
    }
}