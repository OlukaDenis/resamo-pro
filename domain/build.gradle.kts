plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("jacoco")
//    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.dennytech.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }


        create("staging") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
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
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

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

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.timber)

    implementation(libs.androidx.paging.runtime.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
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