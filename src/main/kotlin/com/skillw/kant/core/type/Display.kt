package com.skillw.kant.core.type

interface Display {
    val name:String
    fun fullDisplay(): String


    fun display():String = fullDisplay()
}