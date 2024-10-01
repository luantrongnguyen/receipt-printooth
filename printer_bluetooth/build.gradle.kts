plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
}
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.luantrongnguyen"
            artifactId = "receipt-printooth"
            version = "1.0.0-release"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.github.mazenrashed:Printooth:1.3.1")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}

