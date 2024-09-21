package com.michelbarbosa.magictables.ui.events

import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation

sealed interface MainEvent {
    data class Start(val ordenation: Ordenation): MainEvent
    data class Add(val mainData: MainData): MainEvent
    data class Remove(val position: Int): MainEvent
}