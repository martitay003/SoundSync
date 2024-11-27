dependencies {
    // Core Android
    implementation "androidx.core:core-ktx:$core_ktx_version"
    implementation "androidx.appcompat:appcompat:$appcompat_version"
    implementation "com.google.android.material:material:$material_version"
    implementation "androidx.constraintlayout:constraintlayout:$constraint_layout_version"

    // Architecture Components
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    implementation "androidx.navigation:navigation-ui-ktx:$navigation_version"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // Media & Audio
    implementation "androidx.media3:media3-exoplayer:$media3_version"
    implementation "androidx.media3:media3-session:$media3_version"
    implementation "androidx.media3:media3-ui:$media3_version"

    // ML/AI
    implementation "org.tensorflow:tensorflow-lite:$tensorflow_version"
    implementation "org.tensorflow:tensorflow-lite-support:$tensorflow_version"
    implementation "com.google.mlkit:audio:$ml_kit_version"

    // Firebase
    implementation platform("com.google.firebase:firebase-bom:$firebase_bom_version")
    implementation 'com.google.firebase:firebase-analytics-ktx'
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'

    // Networking
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"
    implementation "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

    // Dependency Injection
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-compiler:$hilt_version"

    // Image Loading
    implementation "io.coil-kt:coil:$coil_version"
    implementation "io.coil-kt:coil-compose:$coil_version"

    // WebRTC for real-time communication
    implementation 'org.webrtc:google-webrtc:1.0.32006'

    // Testing
    testImplementation "junit:junit:$junit_version"
    androidTestImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.espresso:espresso-core:$espresso_version"
}