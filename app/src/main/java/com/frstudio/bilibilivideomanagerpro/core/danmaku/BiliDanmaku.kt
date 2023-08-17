package com.frstudio.bilibilivideomanagerpro.core.danmaku

import com.frstudio.bilibilivideomanagerpro.utils.get
import org.w3c.dom.Node

data class BiliDanmaku(val data: String, val text: String) {
    constructor(node: Node): this(node.attributes["p"].nodeValue, node.textContent)
}