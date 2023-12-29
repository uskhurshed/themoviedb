package tj.itservice.movie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tj.itservice.movie.request.ApiService
import tj.itservice.movie.utils.ApiHelper
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)

object Module {


    @Provides
    fun providesBaseUrl(): String {
        return ApiHelper.BASE_URL
    }


    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            HttpLoggingInterceptor.Level.BODY // Уровень логирования (BODY - логирование тела запроса и ответа)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Установка таймаута на подключение
            .readTimeout(30, TimeUnit.SECONDS) // Установка таймаута на чтение
            .writeTimeout(30, TimeUnit.SECONDS) // Установка таймаута на запись
            .build()
    }

    @Provides
    fun provideRetrofitClient(baseUrl: String, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideRestApiService(retrofit: Retrofit, okHttpClient: OkHttpClient): ApiService {
        return retrofit.newBuilder()
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

}
