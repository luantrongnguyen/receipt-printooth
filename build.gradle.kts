// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.jetbrains.kotlin.android) apply false
//    alias(libs.plugins.android.library) apply false
    id("com.android.application") version "8.2.0" apply false // Use a stable version
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.2.0" apply false // Add library plugin
}
