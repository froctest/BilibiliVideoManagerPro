package com.frstudio.bilibilivideomanagerpro.utils

val Any.allStorage: MutableMap<Any, MutableMap<Any, Any>> by lazy { mutableMapOf() }
val Any.saveStorage: MutableMap<Any, Any> get() {
    return allStorage[this]?: run {
        allStorage[this] = mutableMapOf()
        allStorage[this]!!
    }
}
fun <T: Any> Any.getSave(key: Any, default: () -> T): T {
    if (!saveStorage.containsKey(key)) saveStorage[key] = default()
    return saveStorage[key] as T
}
fun main() {
    val uri = "URI"
    val savePos: Long = uri.getSave("savePos") { Long.MAX_VALUE }
    println(savePos)
}