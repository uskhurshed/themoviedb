plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "tj.itservice.movie"
    compileSdk = 34

    defaultConfig {
        applicationId = "tj.itservice.movie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //Retrofit (барои запрос,ответ,get,post,delete)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //GSON (барои конвертация кардан json-а)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Scalars (барои конвертация кардан json-а - Типи string-ба)
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    //Загрузка изображение
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Жизненый цикл модели
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //DependencyInject
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")
    //Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    //Навигация
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    //Логирование ошибок запроса сети
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    //Слайдер
    implementation("com.github.smarteist:autoimageslider:1.4.0")
    //Анимация
    implementation("com.airbnb.android:lottie:6.2.0")
    //База данных
    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")

}
