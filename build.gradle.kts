import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
import com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD_PARALLEL

plugins {
    id("application")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.testLogger) apply true
}

group = "br.com.mrocigno"
version = "1.0.0"

application {
    mainClass.set("br.com.mrocigno.MainClassKt")
}

dependencies {

    implementation(libs.koin)
    implementation(libs.gson)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.koin)
}

testlogger {
    theme = MOCHA
}

tasks.test {
    useJUnitPlatform()
    testlogger {
        theme = STANDARD_PARALLEL
    }
}

kotlin {
    jvmToolchain(11)
}
