package com.peter.music.di


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.peter.restaurantsmap.framwork.datasource.DirectionSource
import com.peter.restaurantsmap.framwork.datasource.DirectionSourceImpl
import com.peter.restaurantsmap.framwork.datasource.service.ApiService
import com.peter.restaurantsmap.framwork.datasource.service.DirectionService
import com.peter.restaurantsmap.framwork.datasource.SearchSource
import com.peter.restaurantsmap.framwork.datasource.SearchSourceImpl
import com.peter.restaurantsmap.framwork.datasource.Utils.BASE_URL
import com.peter.restaurantsmap.repository.LocationRepo
import com.peter.restaurantsmap.repository.LocationRepoImpl
import com.peter.restaurantsmap.service.FirestoreLocation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirestoreProviderModule {
    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirestoreLocation(db: FirebaseFirestore): FirestoreLocation =
        FirestoreLocation(db)

    @Singleton
    @Provides
    fun provideLocationRepo(store: FirestoreLocation, source: SearchSource,directionSource: DirectionSource): LocationRepo =
        LocationRepoImpl(store, source,directionSource)

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun provideDirectionService(retrofit: Retrofit): DirectionService = retrofit.create(
        DirectionService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

//    @Singleton
//    @Provides
//    private fun provideGeoContext()  =  GeoApiContext()
//            .setQueryRateLimit(3)
//            .setApiKey(BuildConfig.MAPS_API_KEY)
//            .setConnectTimeout(100, TimeUnit.SECONDS)
//            .setReadTimeout(100, TimeUnit.SECONDS)
//            .setWriteTimeout(100, TimeUnit.SECONDS)


    @Singleton
    @Provides
    fun provideSearchSource(api: ApiService): SearchSource = SearchSourceImpl(api)

    @Singleton
    @Provides
    fun provideDirectionSource(directionSource: DirectionService): DirectionSource = DirectionSourceImpl(directionSource)


}