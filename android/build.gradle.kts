group = "io.flutter.plugins.firebase.core"
version = "1.0-SNAPSHOT"

plugins {
    id("com.android.library")
}

apply(from = "local-config.gradle.kts")

val compileSdk: Int by rootProject.extra
val minSdk: Int by rootProject.extra
val targetSdk: Int by rootProject.extra
val javaVersion: JavaVersion by rootProject.extra

fun getRootProjectExtOrDefaultProperty(name: String): String {
    val extra = rootProject.extensions.extraProperties

    if (!extra.has("FlutterFire")) {
        return project.findProperty(name)?.toString()
            ?: error("Property '$name' not found")
    }

    val flutterFire = extra["FlutterFire"] as? Map<*, *>

    return flutterFire?.get(name)?.toString()
        ?: project.findProperty(name)?.toString()
        ?: error("Property '$name' not found")
}

android {
    namespace = "io.flutter.plugins.firebase.core"

    compileSdk = compileSdk

    defaultConfig {
        minSdk = minSdk
        targetSdk = targetSdk

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    buildFeatures {
        buildConfig = true
    }

    lint {
        disable += "InvalidPackage"
    }
}

dependencies {
    implementation(
        platform(
            "com.google.firebase:firebase-bom:${
                getRootProjectExtOrDefaultProperty("FirebaseSDKVersion")
            }"
        )
    )

    implementation("com.google.firebase:firebase-common")
    implementation("androidx.annotation:annotation:1.7.0")
}

apply(from = "user-agent.gradle")