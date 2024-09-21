package com.michelbarbosa.magictables.data.repository.main

import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation

interface MainRepository {
    fun insertMainData(mainData: MainData?, ordenation: Ordenation)
    fun updateListMainData(list: List<MainData>, ordenation: Ordenation)
    fun getListMainHorizontal(): List<MainData>
    fun getListMainVertical(): List<MainData>
}