    {
// Core
        kotlin_version = '1.9.22'
        gradle_version = '8.2.2'

// AndroidX
        core_ktx_version = '1.12.0'
        appcompat_version = '1.6.1'
        material_version = '1.11.0'
        constraint_layout_version = '2.1.4'

// Architecture Components
        lifecycle_version = '2.7.0'
        navigation_version = '2.7.7'
        room_version = '2.6.1'

// Media & Audio
        media3_version = '1.2.1'
        audio_version = '1.2.0'

// ML/AI
        tensorflow_version = '2.15.0'
        ml_kit_version = '17.1.0'

// Firebase
        firebase_bom_version = '32.7.3'

// Networking
        retrofit_version = '2.9.0'
        okhttp_version = '4.12.0'

// Image Loading
        coil_version = '2.5.0'

// DI
        hilt_version = '2.50'

// Testing
        junit_version = '4.13.2'
        espresso_version = '3.5.1'
    }

    dependencies {
        classpath ;"com.android.tools.build:gradle:$gradle_version"
        classpath ;"org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath ;"androidx.navigation:navigation-safe-args-gradle-plugin:$navigation_version"
        classpath ;"com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
        classpath ;'com.google.gms:google-services:4.4.0'
    }


plugins
    id ;'com.android.application' version "$gradle_version" apply false
    id ;'com.android.library' version "$gradle_version" apply false
    id ;'org.jetbrains.kotlin.android' version "$kotlin_version" apply false
    id ;'com.google.dagger.hilt.android' version "$hilt_version" apply false


task clean (type: Delete)
    {
    delete rootProject.buildDir
    }