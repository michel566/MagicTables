package com.michelbarbosa.magictables.data.usecase

import com.michelbarbosa.magictables.data.repository.main.MainRepository
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import javax.inject.Inject

class GetListMainDataUseCaseImpl @Inject constructor(
    private val repository: MainRepository
) : GetListMainDataUseCase {

    override fun invoke(ordenation: Ordenation): List<MainData> {
        return when (ordenation) {
            Ordenation.VERTICAL -> repository.getListMainVertical()
            Ordenation.HORIZONTAL -> repository.getListMainHorizontal()
        }
    }

}