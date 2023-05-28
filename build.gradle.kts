plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadowJar)
}

group = "br.com.mrocigno"
version = "1.0.0"

dependencies {

    implementation(libs.koin)
    implementation(libs.gson)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.koin)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "br.com.mrocigno.MainClassKt"
    }
}

task("execute", JavaExec::class) {
    mainClass.set("br.com.mrocigno.MainClassKt")
    classpath = sourceSets["main"].runtimeClasspath
}