import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("jacoco")
}

android {
    signingConfigs {
        val keystoreFile = readProperties("../keystore.properties")
        if (!keystoreFile.isNullOrEmpty()) {
            create("resamoConfig") {
                storeFile = file("$rootDir/${keystoreFile.getProperty("storeFile")}")
                storePassword = keystoreFile.getProperty("storePassword")
                keyAlias = keystoreFile.getProperty("keyAlias")
                keyPassword = keystoreFile.getProperty("keyPassword")
            }
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

        val versionPropsFile = readProperties("../version.properties")

        if (!versionPropsFile.isNullOrEmpty()) {
            val versionCodeMajor = versionPropsFile.getProperty("VERSION_CODE_MAJOR").toInt()
            val versionCodeMinor = versionPropsFile.getProperty("VERSION_CODE_MINOR").toInt()

            defaultConfig {
                versionCode = versionCodeMajor * 100 + versionCodeMinor
                versionName = versionPropsFile.getProperty("VERSION_NAME")
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
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        create("staging") {
            signingConfig = signingConfigs.getByName("resamoConfig")
            applicationIdSuffix = ".uat"
            isDebuggable = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

//  ./gradlew :app:jacocoTestReport
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val excludes = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*Binding.class",
        "**/*Binding*.*",
        "**/*Dao_Impl*.class",
        "**/*Args.class",
        "**/*Args.Builder.class",
        "**/*Directions*.class",
        "**/*Creator.class",
        "**/*Builder.class",
        "**/R$*.class",
        "**/*_MembersInjector.class",
        "**/Dagger*Component.class",
        "**/Dagger*Component*Builder.class",
        "**/*Module_*Factory.class",
        "**/*Module_*Provide*Factory.class",
        "**/di/**",
        "**/hilt/**",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/*Factory*",
        "**/*_MembersInjector*",
        "**/*Module*",
        "**/*Component*",
        "**android**",
        "**/BR.class",
        "**/model/**",
        "**/models/**",
        "**/*Dto.class",
        "**/*Model*.*",
        "**/*Entity*.*",
        "**/*State*.*",
        "**/*Event*.*"
    )

    val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/debug") {
        exclude(excludes)
    }

    val mainSrc = "$projectDir/src/main/kotlin"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(kotlinClasses))
    executionData.setFrom(fileTree(buildDir) {
        include(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "jacoco/testDebugUnitTest.exec"
        )
    })
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.gson)
    implementation(libs.coil.compose)

    implementation(libs.androidx.paging.compose)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.window)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.timber)
    implementation(libs.spark.utils)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.mpAndroidchart)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
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
    val versionPropsFile = readProperties("../version.properties")
    return if (!versionPropsFile.isNullOrEmpty()) {
        val versionCodeMajor = versionPropsFile.getProperty("VERSION_CODE_MAJOR").toInt()
        val versionCodeMinor = versionPropsFile.getProperty("VERSION_CODE_MINOR").toInt()
        return versionCodeMajor * 10000 + versionCodeMinor
    } else 4
}

fun getVersionName(): String {
    println("Getting version name......")
    val versionPropsFile = readProperties("../version.properties")
    return if(!versionPropsFile.isNullOrEmpty()) {
        versionPropsFile.getProperty("VERSION_NAME")
    } else "1.0.4"
}

fun getFile(filePath: String): File {
    return file(filePath)
}

fun readProperties(filePath: String): Properties? {
    val file = getFile(filePath)
    return if (file.exists()) {
        Properties().apply {
            file.inputStream().use { fis ->
                load(fis)
            }
        }
    } else {
        null
    }
}

afterEvaluate {
    tasks.withType<Test> {
        doLast {
            println("Test task ${this.name} completed")
        }
    }

    tasks.withType<JacocoReport> {
        doFirst {
            println("Generating JaCoCo report for ${project.name}")
            println("Execution data: ${executionData.files}")
            println("Class directories: ${classDirectories.files}")
            println("Source directories: ${sourceDirectories.files}")
        }
    }
}