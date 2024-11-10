import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    signingConfigs {
        create("resamoConfig") {
            storeFile = file("$rootDir/resamo.jks")
            storePassword = "FkhHU4QKxvbS"
            keyAlias = "resamoKey"
            keyPassword = "FkhHU4QKxvbS"
        }
    }

    namespace = "com.dennytech.resamopro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dennytech.resamopro"
        minSdk = 21
        targetSdk = 34
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val versionPropsFile = file("../version.properties")

        if (versionPropsFile.exists()) {
            val versionProps = Properties()
            versionProps.load(versionPropsFile.inputStream())

            val versionCodeMajor = versionProps.getProperty("VERSION_CODE_MAJOR").toInt()
            val versionCodeMinor = versionProps.getProperty("VERSION_CODE_MINOR").toInt()

            defaultConfig {
                versionCode = versionCodeMajor * 100 + versionCodeMinor
                versionName = versionProps.getProperty("VERSION_NAME")
            }
        }

        // Build a named APK
        applicationVariants.all {
            outputs.all {
                this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
                val type = if (buildType.name == "release") "prod" else "staging"
                val apkName = "resamo-$type-$versionName-($versionCode).apk"
                outputFileName = apkName
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("resamoConfig")
            applicationIdSuffix = ".uat"

            resValue("string", "app_name", "Resamo Pro Dev")

        }

        create("staging") {
            signingConfig = signingConfigs.getByName("resamoConfig")
            applicationIdSuffix = ".uat"
            isDebuggable = false
            resValue("string", "app_name", "Resamo Pro Dev")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            signingConfig = signingConfigs.getByName("resamoConfig")
            resValue("string", "app_name", "Resamo Pro")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =  "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    tasks.getByName("preBuild").dependsOn("incrementVersion")
}

tasks.register("printVersionName") {
    doLast {
        println android.defaultConfig.versionName
    }
}

tasks.register("printVersionCode") {
    doLast {
        println android.defaultConfig.versionCode
    }
}

tasks.register("incrementVersion") {
    doLast {
        try {
            val versionPropsFile = file("../version.properties")
            if (versionPropsFile.exists()) {
                val versionProps = Properties()
                versionProps.load(versionPropsFile.inputStream())

                val versionCodeMajor = versionProps.getProperty("VERSION_CODE_MAJOR").toInt()
                val versionCodeMinor = versionProps.getProperty("VERSION_CODE_MINOR").toInt() + 1
//                val versionName = versionProps.getProperty("VERSION_NAME")

                versionProps.setProperty("VERSION_CODE_MINOR", versionCodeMinor.toString())
                versionProps.setProperty("VERSION_NAME", "$versionCodeMajor.$versionCodeMinor")
                versionProps.store(versionPropsFile.writer(), null)

                println("Version code incremented to $versionCodeMajor.$versionCodeMinor")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun getVersionCode(): Int {
    val versionPropsFile = file("../version.properties")
    if (versionPropsFile.exists()) {
        val versionProps = Properties()
        versionProps.load(versionPropsFile.inputStream())

        val versionCodeMajor = versionProps.getProperty("VERSION_CODE_MAJOR").toInt()
        val versionCodeMinor = versionProps.getProperty("VERSION_CODE_MINOR").toInt()

        return versionCodeMajor * 10000 + versionCodeMinor
    }
    return 4 // Default version code if version properties file doesn't exist
}


fun getVersionName(): String {
    println("Getting version name......")
    val versionPropsFile = file("../version.properties")
//    val properties = readProperties(file("../version.properties"))
    if (versionPropsFile.exists()) {
        val versionProps = Properties()
        versionProps.load(versionPropsFile.inputStream())
        return versionProps.getProperty("VERSION_NAME")
    }
//    println("Getting version name: NAme exists.... ${properties["VERSION_NAME"]}")
    return "1.0.4" // Default version name if version properties file doesn't exist
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")

    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("androidx.paging:paging-compose:3.2.1")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    implementation("androidx.window:window:1.2.0")

    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-work:1.1.0")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

//    implementation("com.patrykandpatrick.vico:core:2.0.0-alpha.21")
//    implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.21")
//    implementation("com.github.tehras:charts:0.2.4-alpha")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}