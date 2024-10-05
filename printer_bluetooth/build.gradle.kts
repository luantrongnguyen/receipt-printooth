plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
    id("maven-publish")
}
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.luantrongnguyen"
            artifactId = "receipt-printooth"
            version = "1.1.1-release"

            // Use the releaseAar component for Android library
            artifact("$buildDir/outputs/aar/${project.name}-release.aar")
//            from(components["java"])
            pom {
                withXml {
                    asNode().appendNode("dependencies").apply {
                        appendNode("dependency").apply {
                            appendNode("groupId", "io.github.pilgr")
                            appendNode("artifactId", "paperdb")
                            appendNode("version", "2.7.2")
                        }
                        appendNode("dependency").apply {
                            appendNode("groupId", "com.afollestad.assent")
                            appendNode("artifactId", "core")
                            appendNode("version", "3.0.2")
                        }
                        // Dependency 3: androidx.appcompat
                        appendNode("dependency").apply {
                            appendNode("groupId", "androidx.appcompat")
                            appendNode("artifactId", "appcompat")
                            appendNode("version", "1.6.1")
                        }
                        // Dependency 4: google material
                        appendNode("dependency").apply {
                            appendNode("groupId", "com.google.android.material")
                            appendNode("artifactId", "material")
                            appendNode("version", "1.9.0")
                        }
                        // Dependency 5: androidx.swiperefreshlayout
                        appendNode("dependency").apply {
                            appendNode("groupId", "androidx.swiperefreshlayout")
                            appendNode("artifactId", "swiperefreshlayout")
                            appendNode("version", "1.1.0")
                        }
                    }
                }
            }
        }
    }
}

android {
    namespace = "vn.herosoft.printer_bitmap_bluetooth"
    compileSdk = 34
    android {
        publishing {
            multipleVariants {
                allVariants()
                withJavadocJar()
            }
        }
    }
    defaultConfig {
        minSdk = 23
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
//        productFlavors {
//            register("foo") {
//
//                aarMetadata {
//                    minCompileSdk = 30
//                }
//            }
//        }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("io.github.pilgr:paperdb:2.7.2")
    implementation ("com.afollestad.assent:core:3.0.2")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}

tasks.named("publishReleasePublicationToMavenLocal") {
    dependsOn("bundleReleaseAar") // Declare dependency
}