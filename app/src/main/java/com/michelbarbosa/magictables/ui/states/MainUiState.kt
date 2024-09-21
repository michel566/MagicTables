package com.michelbarbosa.magictables.ui.states

import com.michelbarbosa.magictables.model.MainData

sealed interface MainUiState {

    /** Estado inicial da view sem item */
    object Initial: MainUiState

    /** Estado de processamento da view */
    object Loading: MainUiState

    /** Estado se a view ja foi atualizada, e teve a lista limpa*/
    object Cleared: MainUiState

    /** Retorna se a lista estiver com somente um item */
    data class Created(val list: List<MainData>) : MainUiState
    /** Retorna se em algum momento houve atualização de uma lista existente */
    data class Updated(val list: List<MainData>): MainUiState
    /** Retorna se a lista estiver vazia */
    data class EmptyList(val list: List<Any>?): MainUiState

}