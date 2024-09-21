package com.michelbarbosa.magictables.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michelbarbosa.magictables.data.cache.CacheJsonSerializer
import com.michelbarbosa.magictables.data.cache.CacheKeysConstants
import com.michelbarbosa.magictables.data.di.DefaultDispatcher
import com.michelbarbosa.magictables.data.usecase.GetListMainDataUseCase
import com.michelbarbosa.magictables.data.usecase.InsertMainDataUseCase
import com.michelbarbosa.magictables.data.usecase.UpdateListMainData
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import com.michelbarbosa.magictables.model.removeData
import com.michelbarbosa.magictables.ui.events.MainEvent
import com.michelbarbosa.magictables.ui.states.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val insertUseCase: InsertMainDataUseCase,
    private val updateListUseCase: UpdateListMainData,
    private val getListUseCase: GetListMainDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Initial)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    //todo: temporario quando funcionar colocar no repository
    var mMutableList: MutableList<MainData>? = null

    companion object {
        private var currentOrdenation: Ordenation? = null
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Start -> start(event.ordenation)
            is MainEvent.Add -> addItem(event.mainData)
            is MainEvent.Remove -> removeItem(event.position)
        }
    }

    fun start(ordenation: Ordenation) {
        currentOrdenation = ordenation
        viewModelScope.launch {
            updateState(state = MainUiState.Initial)
            mMutableList = startList()
            mMutableList?.let {
                updateState(state = MainUiState.Created(it.toList()))
            } ?: kotlin.run {
                updateState(state = MainUiState.EmptyList(listOf()))
            }
        }
    }

    private fun addItem(mainData: MainData) {
        viewModelScope.launch {
            mMutableList = getMutableList()
            mMutableList?.apply {
                val index = mainData.index
                set(index, mainData)

                if (!mainData.justUpdate){
                    add(
                        MainData.getDummy(
                            index = index.plus(1),
                            ordenation = Ordenation.HORIZONTAL
                        )
                    )
                }

                CacheJsonSerializer.saveList(CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, this)
                updateState(MainUiState.Updated(toList()))
            } ?: kotlin.run {
                updateState(MainUiState.EmptyList(listOf()))
            }
        }
    }

    private fun removeItem(position: Int) {
        viewModelScope.launch {
            mMutableList = getMutableList()
            mMutableList?.apply {
                removeData(position)
                CacheJsonSerializer.saveList(CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, this)

                if (size > 0) updateState(MainUiState.Updated(toList()))
                else updateState(MainUiState.Cleared)

            } ?: kotlin.run {
                updateState(MainUiState.EmptyList(listOf()))
            }
        }
    }

    // ---

    // TODO: colocar lista no repository

//    var mMutableList = getMutableList()
//
//
//

//
//    fun updateMutableList(list: MutableList<MainData>) {
//        mMutableList.clear()
//        mMutableList.addAll(list)
//        CacheJsonSerializer.saveList(CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, mMutableList)
//    }
//
//    fun addItem(data: MainData){
//        val index = data.index - 1
//        if (index > 0)
//            mMutableList[index] = data
//        else
//            mMutableList.add(data)
//
//        index + 1
//        mMutableList.add(MainData.getDummy(index, Ordenation.HORIZONTAL))
//        CacheJsonSerializer.saveList(CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, mMutableList)
//
//    }
//
//    fun removeItem(position: Int){
//        mMutableList.removeAt(position)
//        CacheJsonSerializer.saveList(CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, mMutableList)
//    }

    private fun getMutableList(): MutableList<MainData> {
        return CacheJsonSerializer.getObjList(
            CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, Array<MainData>::class.java
        ).toMutableList()
    }

    fun startList(): MutableList<MainData> {
        val list = CacheJsonSerializer.getObjList(
            CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, Array<MainData>::class.java
        ).toMutableList()
        if (list.isEmpty()) {
            list.add(MainData.getDummy(0, Ordenation.HORIZONTAL))
            CacheJsonSerializer.saveList(CacheKeysConstants.CACHE_MAIN_HORIZONTAL_LIST, list)
        }
        return list
    }

    private fun updateState(state: MainUiState) {
        _uiState.update { state }
    }

}