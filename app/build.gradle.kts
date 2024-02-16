plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    //kotlin("kapt")
}

android {
    namespace = "com.sisifos.rickyandmortypaging3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sisifos.rickyandmortypaging3"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"




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

    buildFeatures {
        viewBinding = true
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
    //kapt("groupId:artifactId:version")

    val lifecycle_version = "2.4.0"
    val room_version = "2.4.0"

    //Carousel RecyclerView
    implementation ("com.github.sparrow007:carouselrecyclerview:1.2.6")

    //Android gif drawable
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    // Android Core
    implementation("androidx.core:core-ktx:1.9.0")

    // Android AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")

    // ViewModel Ktx
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    //implementation("androidx.room:room-runtime:$room_version")
    //implementation("androidx.room:room-ktx:$room_version")

    implementation ("androidx.paging:paging-runtime-ktx:3.1.0")
    implementation("androidx.room:room-paging:$room_version")
    //kapt("androidx.room:room-compiler:$room_version")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    // Material
    implementation("com.google.android.material:material:1.9.0")

    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")


    // Moshi
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")


    // Picasso
    implementation("com.squareup.picasso:picasso:2.8")



    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    debugImplementation ("com.github.chuckerteam.chucker:library:3.4.0")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:3.4.0")



    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}