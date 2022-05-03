package com.tasks.viemed.di

import com.tasks.viemed.api.ApolloInstance
import com.tasks.viemed.data.ViemedApiRepositoryImpl
import com.tasks.viemed.domain.ViemedApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ViemedModule {

    @Provides
    @Singleton
    fun provideViemedApi() = ApolloInstance.Companion

    @Provides
    @Singleton
    fun provideViemedRepository(apiService: ApolloInstance.Companion): ViemedApiRepository {
        return ViemedApiRepositoryImpl(apiService)
    }

}