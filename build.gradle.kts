import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
}

group = "srb.aki"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/releases/")
    }
}

dependencies {
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp

    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("org.bouncycastle:bcpkix-fips:1.0.7")
    implementation("org.bouncycastle:bctls-fips:1.0.17")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("com.nixxcode.jvmbrotli:jvmbrotli:0.2.0")
    implementation("com.nixxcode.jvmbrotli:jvmbrotli-linux-x86-amd64:0.2.0")
    implementation("com.nixxcode.jvmbrotli:jvmbrotli-parent:0.2.0")


    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}


