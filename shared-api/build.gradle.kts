//import org.jetbrains.kotlin.gradle.dsl.JvmTarget
//import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
//
//plugins {
//    alias(libs.plugins.kotlinMultiplatform)
//    alias(libs.plugins.androidMultiplatformLibrary)
//    id("maven-publish")
//}
//
//val projectJavaVersion: JavaVersion = JavaVersion.toVersion(libs.versions.java.get())
//
//fun String.runCommand(): String {
//    return ProcessBuilder(*split(" ").toTypedArray())
//        .redirectErrorStream(true)
//        .start()
//        .inputStream
//        .bufferedReader()
//        .readText()
//}
//
//kotlin {
//
//    androidTarget {
//        publishLibraryVariants("release")
//        withSourcesJar(publish = true)
//
//        compilations.all{
//            compileTaskProvider{
//                compilerOptions{
//                    jvmTarget = JvmTarget.fromTarget(projectJavaVersion.toString())
//                    freeCompilerArgs.add("-Xjdk-release=${projectJavaVersion}")
//                }
//            }
//        }
//    }
//
//    val xcf = XCFramework("SharedApi")
//    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
//    iosTargets.forEach {
//        it.binaries.framework {
//
//            isStatic = false
//            linkerOpts.add("-lsqlite3")
//            baseName = "SharedApi"
//
//            xcf.add(this)
//        }
//    }
//
//    sourceSets {
//        commonMain.dependencies {}
//        iosMain.dependencies {}
//        iosArm64Main.dependencies {}
//        iosX64Main.dependencies {}
//        iosSimulatorArm64Main.dependencies {}
//    }
//}
//
//android {
//    namespace = "ru.europlan.mws.api"
//
//    compileOptions {
//        sourceCompatibility = projectJavaVersion
//        targetCompatibility = projectJavaVersion
//    }
//
//    publishing {
//        singleVariant("release") {
//            withSourcesJar()
////            withJavadocJar()
//        }
//    }
//}
//
//dependencies {
////    commonMainImplementation(libs.kotlinxDateTime)
////    commonMainImplementation(libs.coroutines)
//}
//
//// 🎯 TASK: Архивирует XCFramework в ZIP для SPM
//tasks.register("zipXCFramework") {
//    val xcodePath = "xcode-select -p".runCommand().trim()
//
//    dependsOn("assembleSharedApiXCFramework") // Убедиться, что XCFramework собран
//
//    doLast {
//        val outputDir = layout.buildDirectory.dir("XCFrameworks/release").get().asFile
//        val xcFrameworkDir = outputDir.resolve("SharedApi.xcframework")
//        val zipFile = outputDir.resolve("SharedApi.framework.zip")
//
//        require(xcFrameworkDir.exists()) { "❌ XCFramework не найден: $xcFrameworkDir" }
//
//        // Удаляем старый ZIP, если он есть
//        zipFile.delete()
//
//        println("📦 Архивация XCFramework в ZIP...")
//
//        // Создаём ZIP без лишних путей
//        exec {
//            workingDir(xcFrameworkDir.parentFile) // Чтобы путь начинался с XCFramework
//            environment("DEVELOPER_DIR", xcodePath)
//            commandLine("zip", "-r", zipFile.absolutePath, "SharedApi.xcframework")
//        }
//
//        println("✅ ZIP-архив создан: ${zipFile.absolutePath}")
//    }
//}