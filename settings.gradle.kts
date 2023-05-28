rootProject.name = "quake-log-scraper"

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