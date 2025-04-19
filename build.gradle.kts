// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("com.android.library") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("jacoco")
}

// Configure JaCoCo version
jacoco {
    toolVersion = "0.8.11"
}

task<JacocoReport>("jacocoFullReport") {
    group = "Reporting"
    description = "Generates an aggregate report from all subprojects"

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

    dependsOn(
        ":app:testDebugUnitTest",
        ":data:testDebugUnitTest",
        ":domain:testDebugUnitTest",
        //":app:connectedDebugAndroidTest" // for Android tests
    )

//    additionalSourceDirs.setFrom(files(
//        project(":app").file("src/main/java"),
//        project(":app").file("src/main/kotlin"),
//        project(":data").file("src/main/java"),
//        project(":data").file("src/main/kotlin"),
//        project(":domain").file("src/main/java"),
//        project(":domain").file("src/main/kotlin")
//    ))

    sourceDirectories.setFrom(files(
        project(":app").file("src/main/java"),
        project(":app").file("src/main/kotlin"),
        project(":data").file("src/main/java"),
        project(":data").file("src/main/kotlin"),
        project(":domain").file("src/main/java"),
        project(":domain").file("src/main/kotlin")
    ))

    classDirectories.setFrom(files(
        fileTree(project(":app").buildDir) {
            include(
                "**/classes/**/main/**",
                "**/tmp/kotlin-classes/debug/**"
            )
            exclude(excludes)
        },
        fileTree(project(":data").buildDir) {
            include(
                "**/classes/**/main/**",
                "**/tmp/kotlin-classes/debug/**"
            )
            exclude(excludes)
        },
        fileTree(project(":domain").buildDir) {
            include(
                "**/classes/**/main/**",
                "**/tmp/kotlin-classes/debug/**"
            )
            exclude(excludes)
        }
    ))

    executionData.setFrom(fileTree(rootDir) {
        include(
            "app/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "data/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "domain/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
        )
    })


    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    doFirst {
        logger.lifecycle("Found execution files: ${executionData.files}")
        if (executionData.files.isEmpty()) {
            logger.lifecycle("WARNING: No execution data files found!")
        }
    }
}

// ./gradlew debugJacoco
task("debugJacoco") {
    doLast {
        println("Checking for execution data...")
        fileTree(rootDir).matching {
            include("**/*.exec", "**/*.ec")
        }.forEach { file ->
            println("Found execution data: ${file.absolutePath}")
        }

        println("\nChecking for class files...")
        listOf("app", "data", "domain").forEach { module ->
            val debugDir = project(module).buildDir.resolve("tmp/kotlin-classes/debug")
            println("$module classes exist: ${debugDir.exists()} at ${debugDir.absolutePath}")
        }
    }
}

// ./gradlew showCoveragePaths
task("showCoveragePaths") {
    doLast {
        subprojects.forEach { project ->
            println("Checking ${project.name}...")
            val jacocoDir = File(project.buildDir, "jacoco")
            println("  Jacoco dir exists: ${jacocoDir.exists()}")

            if (jacocoDir.exists()) {
                jacocoDir.listFiles()?.forEach { file ->
                    println("  Found: ${file.name}")
                }
            }
        }
    }
}

// ./gradlew jacocoTestCoverageVerification
tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
//tasks.withType<JacocoCoverageVerification> {
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal() // 80% coverage
            }
        }

        rule {
            element = "CLASS"
            excludes = listOf(
                "*.Fragment",
                "*.Activity",
                "*.ViewModel",
                "di.*"
            )
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
    }
}