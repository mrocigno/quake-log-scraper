plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadowJar)
}

group = "br.com.mrocigno"
version = "1.0.0"

dependencies {

    testImplementation(kotlin("test"))

    implementation(libs.gson)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "br.com.mrocigno._mainKt"
    }
}