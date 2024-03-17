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

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")

    implementation("com.google.protobuf:protobuf-javalite:3.23.4")
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.23.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")

    implementation("androidx.paging:paging-runtime-ktx:3.2.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
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