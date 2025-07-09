package com.jumball.app

object ValueStore {
    private var value: Int = 0
    private var secondVlaue :Int=0
    fun setValue(newValue: Int) {
        value = newValue
    }
    fun getValue(): Int {
        return value
    }
    fun setValue1(newValue: Int) {
        secondVlaue = newValue
    }
    fun getValue1(): Int {
        return secondVlaue
    }
}