import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
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
    implementation(group="org.bouncycastle", name= "bcpkix-fips", version= "1.0.5")
    implementation( group= "org.bouncycastle", name= "bctls-fips", version= "1.0.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("com.nixxcode.jvmbrotli:jvmbrotli:0.2.0")
    implementation("com.nixxcode.jvmbrotli:jvmbrotli-linux-x86-amd64:0.2.0")
    implementation("com.nixxcode.jvmbrotli:jvmbrotli-parent:0.2.0")


    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}


