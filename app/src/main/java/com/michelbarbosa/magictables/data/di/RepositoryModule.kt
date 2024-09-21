package com.michelbarbosa.magictables.data.di

import com.michelbarbosa.magictables.data.repository.main.MainRepository
import com.michelbarbosa.magictables.data.repository.main.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @ViewModelScoped
    @Binds
    fun bindMainRepository(repositoryImpl: MainRepositoryImpl): MainRepository
}