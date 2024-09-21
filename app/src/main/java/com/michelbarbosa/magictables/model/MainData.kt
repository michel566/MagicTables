package com.michelbarbosa.magictables.model

data class MainData(
    var index: Int,
    var name: String,
    var textColor: Int,
    var bgColor: Int,
    var justUpdate: Boolean,
    var isDummy: Boolean,
    var ordenation: Ordenation
) {

    companion object {
        fun getDummy(index: Int?, ordenation: Ordenation) = MainData(
            index = index ?: 0,
            name = "",
            textColor = 0,
            bgColor = 0,
            justUpdate = false,
            isDummy = true,
            ordenation = ordenation
        )
    }

}

fun MutableList<MainData>.removeData(position: Int): MutableList<MainData> =
    removeData(get(position))

fun MutableList<MainData>.removeData(item: MainData): MutableList<MainData> {
    if (contains(item)) {
        var i = 0
        removeAt(item.index)
        replaceAll {
            it.apply {
                index = i++
            }
        }
    }
    return this
}