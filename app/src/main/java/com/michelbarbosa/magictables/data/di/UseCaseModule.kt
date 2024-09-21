package com.michelbarbosa.magictables.data.di

import com.michelbarbosa.magictables.data.usecase.GetListMainDataUseCase
import com.michelbarbosa.magictables.data.usecase.GetListMainDataUseCaseImpl
import com.michelbarbosa.magictables.data.usecase.InsertMainDataUseCase
import com.michelbarbosa.magictables.data.usecase.InsertMainDataUseCaseImpl
import com.michelbarbosa.magictables.data.usecase.UpdateListMainData
import com.michelbarbosa.magictables.data.usecase.UpdateListMainDataImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @ViewModelScoped
    @Binds
    fun bindInsertMainDataUseCase(
        insertMainDataUseCaseImpl: InsertMainDataUseCaseImpl
    ) : InsertMainDataUseCase

    @ViewModelScoped
    @Binds
    fun bindRemoveMainDataUseCase(
        removeMainDataUseCaseImpl: UpdateListMainDataImpl
    ) : UpdateListMainData

    @ViewModelScoped
    @Binds
    fun bindGetListMainDataUseCase(
        getListMainDataUseCaseImpl: GetListMainDataUseCaseImpl
    ) : GetListMainDataUseCase
}