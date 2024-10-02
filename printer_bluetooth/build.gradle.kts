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
            version = "1.0.7-release"

            // Use the releaseAar component for Android library
            artifact("$buildDir/outputs/aar/${project.name}-release.aar")

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
        vectorDrawables.useSupportLibrary = true
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

    api ("androidx.multidex:multidex:2.0.1")
    api ("io.github.pilgr:paperdb:2.7.2")
    api ("com.afollestad.assent:core:3.0.2")
    api ("androidx.appcompat:appcompat:1.6.1")
    api ("com.google.android.material:material:1.9.0")
    api ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}
tasks.named("publishReleasePublicationToMavenLocal") {
    dependsOn("bundleReleaseAar") // Declare dependency
}