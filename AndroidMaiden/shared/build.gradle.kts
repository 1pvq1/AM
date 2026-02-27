import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp) // new: for room

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosArm64()
    iosSimulatorArm64()
    
    jvm()

    /*Room does not support Web yet*/
//    js {
//        browser()
//    }
//
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//    }
    
    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.androidx.datastore.preferences) // UI Settings
            implementation(libs.androidx.room.runtime) // Caching
            implementation(libs.androidx.sqlite.bundled) // Required for shared driver
            implementation(libs.kotlinx.coroutines.core) // Repository needs this
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    // 1. Android Compiler
    // This is the critical missing piece
    add("kspAndroid", libs.androidx.room.compiler)

    // 2. iOS Compilers (Needed for each iOS target)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)

    // 3. Desktop Compiler
    add("kspJvm", libs.androidx.room.compiler)
}


android {
    namespace = "com.example.androidmaiden.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
