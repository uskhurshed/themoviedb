package tj.itservice.movie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tj.itservice.movie.db.AppDatabase
import tj.itservice.movie.db.MovieDao
import tj.itservice.movie.request.ApiService
import tj.itservice.movie.utils.ApiHelper
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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
            .connectTimeout(30, TimeUnit.SECONDS) //  таймаут на подключение
            .readTimeout(30, TimeUnit.SECONDS) //  таймаут на чтение
            .writeTimeout(30, TimeUnit.SECONDS) //  таймаут на запись
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

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "my_database").build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

}
