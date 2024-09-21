package com.michelbarbosa.magictables.data.usecase

import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import kotlinx.coroutines.flow.Flow

interface InsertMainDataUseCase {
    fun invoke(item: MainData?, ordenation: Ordenation)
}