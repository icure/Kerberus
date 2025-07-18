@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.github.jk1.license.render.CsvReportRenderer
import com.github.jk1.license.render.ReportRenderer
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotestMultiplatform)
    alias(libs.plugins.androidLibrary)
//    id("module.publication")
    id("maven-publish")
    signing
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("com.github.jk1.dependency-license-report") version ("2.0")
}

licenseReport {
    renderers = arrayOf<ReportRenderer>(CsvReportRenderer())
}

group = "com.icure"

val version = "1.1.6"
project.version = version

kotlin {
    val localProperties = getLocalProperties()

    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    val frameworkName = rootProject.name.replaceFirstChar { it.uppercase() }.let { "${it}Kotlin" }
    val xcf = XCFramework(frameworkName)
    val iosSimulators = listOf(
        iosX64(),
        iosSimulatorArm64()
    )
    val iosAll = iosSimulators + iosArm64()
    iosAll.forEach { target ->
        target.binaries.framework {
            baseName = frameworkName
            xcf.add(this)
        }
    }
    iosSimulators.forEach { target ->
        target.testRuns.forEach { testRun ->
            (localProperties["ios.simulator"] as? String)?.let { testRun.deviceId = it }
        }
    }
    macosX64()
    macosArm64()

	val linuxArm64Target = linuxArm64()
    val linuxX64Target = linuxX64()
    listOf(
        linuxX64Target,
        linuxArm64Target
    ).onEach { target ->
        target.binaries {
            all {
                freeCompilerArgs += listOf("-linker-option", "--allow-shlib-undefined")
                localProperties["cinteropsLibsDir"]?.also { allDirs ->
                    (allDirs as String).split(";").forEach {
                        linkerOpts.add(0, "-L$it")
                    }
                }
            }
        }
    }
    mingwX64()
    applyDefaultHierarchyTemplate()

    js(IR) {
        outputModuleName = rootProject.name
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()
                }
            }
        }
        nodejs { }
        binaries.library()
        generateTypeScriptDefinitions()
        useEsModules()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.serialization.protobuf)
                implementation(libs.kotlin.serialization.json)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.bignum)
                implementation(libs.kotlin.bignum.serialization)
                implementation(libs.icure.kryptom)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.property)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit)
            }
        }
    }
}

android {
    namespace = "com.icure.kerberus"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
dependencies {
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

android.testOptions {
    unitTests.all {
        it.useJUnitPlatform()
    }
}

private fun Project.getLocalProperties() =
    Properties().apply {
        kotlin.runCatching {
            load(rootProject.file("local.properties").reader())
        }
    }

fun projectHasSignatureProperties() =
    project.hasProperty("signing.keyId") && project.hasProperty("signing.secretKeyRingFile") && project.hasProperty("signing.password")

if (projectHasSignatureProperties()) {
    signing {
        useInMemoryPgpKeys(
            file(project.property("signing.secretKeyRingFile") as String).readText(),
            project.property("signing.password") as String
        )
        sign(publishing.publications)
    }
}

mavenPublishing {
    coordinates(group as String, rootProject.name, project.version as String)

    pom {
        name.set("Kerberus")
        description.set("""
            Kotlin Multiplatform Proof of Work Captcha library 
		""".trimIndent())
        url.set("https://github.com/icure/Kerberus")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://choosealicense.com/licenses/mit/")
                distribution.set("https://choosealicense.com/licenses/mit/")
            }
        }
        developers {
            developer {
                id.set("icure")
                name.set("iCure")
                url.set("https://github.com/iCure/")
            }
        }
        scm {
            url.set("https://github.com/icure/Kerberus")
            connection.set("scm:git:git://github.com/icure/Kerberus.git")
            developerConnection.set("scm:git:ssh://git@github.com:icure/Kerberus.git")
        }
    }

    publishToMavenCentral(automaticRelease = true)

    if (projectHasSignatureProperties()) {
        signAllPublications()
    }
}

// Configure all publishing tasks
if (!projectHasSignatureProperties()) {
    tasks.withType<PublishToMavenRepository> {
        doFirst {
            throw IllegalStateException("Cannot publish to Maven Central without signing properties")
        }
    }
}
