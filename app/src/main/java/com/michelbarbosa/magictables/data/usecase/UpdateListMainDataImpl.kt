package com.michelbarbosa.magictables.data.usecase

import com.michelbarbosa.magictables.data.repository.main.MainRepository
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import javax.inject.Inject

class UpdateListMainDataImpl @Inject constructor(
    private val repository: MainRepository
) : UpdateListMainData {

    override fun invoke(list: List<MainData>, ordenation: Ordenation) {
        repository.updateListMainData(list, ordenation)
    }
}