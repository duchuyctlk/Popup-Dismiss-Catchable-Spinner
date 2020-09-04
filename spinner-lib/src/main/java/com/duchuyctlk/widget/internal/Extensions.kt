package com.duchuyctlk.widget.internal

fun <E> MutableList<E>.addIfNotContain(element: E): Boolean {
    if (this.contains(element)) {
        return false
    }
    return this.add(element)
}
