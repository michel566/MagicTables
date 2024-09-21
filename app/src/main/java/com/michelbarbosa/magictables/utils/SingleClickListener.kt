package com.michelbarbosa.magictables.utils

import android.os.SystemClock
import android.view.View

/**
 * Implementação do padrão debounce para evitar multiplos clicks simultâneos
 */
abstract class SingleClickListener : View.OnClickListener {
    /**
     * Guarda o momento do último click (em milisegundos)
     */
    private var mLastClickTime: Long = 0
    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return
        }
        onSingleClick(v)
    }

    /**
     * Abstração para implementação do click.
     */
    abstract fun onSingleClick(v: View?)

    companion object {
        /**
         * Tempo mínimo de intervalo entre os clicks (em milisegundos)
         */
        private const val MIN_CLICK_INTERVAL: Long = 1200
    }
}