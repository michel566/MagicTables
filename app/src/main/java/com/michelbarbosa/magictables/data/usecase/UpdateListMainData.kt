package com.michelbarbosa.magictables.data.usecase

import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation

interface UpdateListMainData {
    fun invoke(list: List<MainData>, ordenation: Ordenation)
}