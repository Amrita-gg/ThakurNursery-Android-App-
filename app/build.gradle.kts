plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.thakur_nursery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.thakur_nursery"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        packagingOptions {
            resources {
                excludes.add("META-INF/DEPENDENCIES")
                excludes.add("META-INF/LICENSE")
                excludes.add("META-INF/LICENSE.txt")
                excludes.add("META-INF/NOTICE")
                excludes.add("META-INF/NOTICE.txt")
            }
        }
    }

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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    // Firebase dependencies
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")

    // ML Kit Vision (for plant recognition)
    implementation ("com.google.firebase:firebase-ml-vision:24.0.3")

    // Navigation component
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // PDF generation library (iText)
    implementation ("com.itextpdf:itext7-core:7.2.5")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // HTTP client for plant recognition
    implementation ("com.konghq:unirest-java:3.11.09")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // WorkManager (for fertilizer reminders)
    implementation("androidx.work:work-runtime:2.8.0")

    //sliderimage
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.0")

    // CameraX dependencies
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("androidx.camera:camera-extensions:1.3.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")

    //for glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation("com.google.android.material:material:1.9.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.karumi:dexter:6.2.3")

    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("com.google.firebase:firebase-firestore:24.0.0") // Update to the latest version

    implementation ("com.google.android.gms:play-services-auth:20.2.0") // Example, replace with the latest version
    implementation ("com.google.firebase:firebase-auth:22.0.0") // Example




}
