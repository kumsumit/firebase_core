import com.android.build.api.dsl.LibraryExtension

group = "io.flutter.plugins.firebase.core"
version = "1.0-SNAPSHOT"

plugins {
    id("com.android.library")
}

apply(from = "local-config.gradle.kts")

val rootCompileSdk = rootProject.extra["compileSdk"] as Int
val rootMinSdk = rootProject.extra["minSdk"] as Int
val rootJavaVersion = rootProject.extra["javaVersion"] as JavaVersion

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

extensions.configure<LibraryExtension> {
    namespace = "io.flutter.plugins.firebase.core"

    compileSdk = rootCompileSdk

    defaultConfig {
        minSdk = rootMinSdk

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = rootJavaVersion
        targetCompatibility = rootJavaVersion
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
