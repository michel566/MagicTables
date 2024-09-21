package com.michelbarbosa.magictables.data.usecase

import com.michelbarbosa.magictables.data.repository.main.MainRepository
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import javax.inject.Inject

class InsertMainDataUseCaseImpl @Inject constructor(
    private val repository: MainRepository
) : InsertMainDataUseCase {

    override fun invoke(item: MainData?, ordenation: Ordenation){
        repository.insertMainData(item, ordenation)
    }
}