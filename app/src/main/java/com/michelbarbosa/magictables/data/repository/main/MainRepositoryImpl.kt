package com.michelbarbosa.magictables.data.repository.main

import com.michelbarbosa.magictables.data.cache.CacheJsonSerializer
import com.michelbarbosa.magictables.data.cache.CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST
import com.michelbarbosa.magictables.data.cache.CacheKeysConstants.CACHE_MAIN_VERTICAL_LIST
import com.michelbarbosa.magictables.data.di.IoDispatcher
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MainRepository {

    companion object {
        const val REMOVE_ITEM = 1
        const val ADD_ITEM = 2
    }

    override fun insertMainData(item: MainData?, ordenation: Ordenation) {
        manageItem(ADD_ITEM, item, ordenation)
    }

    override fun updateListMainData(list: List<MainData>, ordenation: Ordenation) {
        val storeList = mutableListOf<MainData>()
        storeList.addAll(list)
        CacheJsonSerializer.saveList(CACHE_MAIN_HORIZONTAL_LIST, list)
    }

    override fun getListMainHorizontal() =
        CacheJsonSerializer.getObjList(CACHE_MAIN_HORIZONTAL_LIST, Array<MainData>::class.java)

    override fun getListMainVertical() =
        CacheJsonSerializer.getObjList(CACHE_MAIN_VERTICAL_LIST, Array<MainData>::class.java)

    private fun manageItem(operation: Int, mainData: MainData?, ordenation: Ordenation) {
        val storeList = mutableListOf<MainData>()
        val KEY: String
        when (ordenation) {
            Ordenation.VERTICAL -> {
                KEY = CACHE_MAIN_VERTICAL_LIST
                storeList.addAll(getListMainVertical())
            }

            Ordenation.HORIZONTAL -> {
                KEY = CACHE_MAIN_HORIZONTAL_LIST
                storeList.addAll(getListMainHorizontal())
            }
        }

        mainData?.let { item ->
            storeList.toMutableList().let { list ->
                when (operation) {
                    REMOVE_ITEM -> {
                    }

                    ADD_ITEM -> {
                        if (list.size == 1 && list[0].name.isEmpty()) {
                            val dummy = list[0].apply { index = 1 }
                            list[0] = item.apply { index = 0 }
                            list.add(dummy)
                        } else {
                            val dummy = list[list.size - 1].apply { index = list.size}
                            list.removeAt(list.size - 1)
                            list.add(item.apply { index = list.size })
                            list.add(dummy)
                        }
                    }

                }
                CacheJsonSerializer.saveList(KEY, list.toList())
            }
        } ?: kotlin.run {
            storeList.toMutableList().let { list ->
                list.add(
                    MainData.getDummy(
                        index = 0,
                        ordenation = ordenation
                    )
                )
                CacheJsonSerializer.saveList(KEY, list)
            }
        }
    }
}