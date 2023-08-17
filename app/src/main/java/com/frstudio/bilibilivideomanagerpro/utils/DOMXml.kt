package com.frstudio.bilibilivideomanagerpro.utils

import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList

operator fun NodeList.get(index: Int) = item(index)
val NodeList.first get() = this[0]
fun NodeList.forEach(each: (Node) -> Unit) {
    for (index in 0 until length) each(this[index])
}
operator fun NamedNodeMap.get(key: String) = getNamedItem(key)
operator fun NamedNodeMap.get(index: Int) = item(index)