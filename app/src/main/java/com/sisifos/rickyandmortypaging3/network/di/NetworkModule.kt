package com.sisifos.rickyandmortypaging3.network.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.sisifos.rickyandmortypaging3.common.Constants.REST_API_BASE_URL
import com.sisifos.rickyandmortypaging3.domain.repository.RickAndMortyRepository
import com.sisifos.rickyandmortypaging3.network.RickAndMortyService
import com.sisifos.rickyandmortypaging3.network.repository.RickAndMortyRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {




    @Provides
    @Singleton
    fun provideRickyAndMortyApi(okHttpClient: OkHttpClient): RickAndMortyService {
        return Retrofit.Builder()
            .baseUrl(REST_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RickAndMortyService::class.java)
    }



    @Provides
    @Singleton
    fun provideCharactersRepository(
        api: RickAndMortyService
    ): RickAndMortyRepository {
        return RickAndMortyRepositoryImpl(api)
    }




    @Provides
    @Singleton
    fun provideLoggingHttpClient(application: Application): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })

        builder.addInterceptor(
            ChuckerInterceptor.Builder(application)
                .collector(ChuckerCollector(application))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()
        )

        return builder.build()
    }




}