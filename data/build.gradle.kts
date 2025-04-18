import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization") version "1.9.0"
    id("dagger.hilt.android.plugin")
    id("com.google.protobuf") version "0.9.4"
    id("com.google.devtools.ksp")
//    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.dennytech.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        debug {
            val baseUrl = getProperty("DEV_URL")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        }

        create("staging") {
            isMinifyEnabled = false
            val baseUrl = getProperty("DEV_URL")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = false
            
            val baseUrl = getProperty("PROD_URL")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(project(":domain"))

    implementation(libs.timber)

    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.protobuf.javalite)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.paging.runtime.ktx)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.test.core.ktx)

    implementation(libs.androidx.security.crypto.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.23.4"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

fun localProperties(): Properties {
    val properties = Properties()
    return try {
        val inputStream = FileInputStream("local.properties")
        properties.load(inputStream)

        properties
    } catch (exception: Exception) {
       exception.printStackTrace()
        properties
    }
}

fun getProperty(key: String): String {
    val properties = localProperties()
    val res = properties.getProperty(key)
    return if (res != null) res else ""
}