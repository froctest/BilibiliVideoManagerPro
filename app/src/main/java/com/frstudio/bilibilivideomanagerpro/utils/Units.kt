package com.frstudio.bilibilivideomanagerpro.utils

val Int.sec get(): Long = this.toLong()*1000
val Int.OneTenthSec get(): Long = this.toLong()*100
val Int.minute get(): Long = sec *60
val Int.hour get(): Long = minute*60

val Int.kb get() = this.toLong()*1024
val Int.mb get() = kb*1024
val Int.gb get() = mb*1024
val Int.tb get() = gb*1024