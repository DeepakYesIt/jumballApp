package com.yesitlabs.jumballapp

object ValueStore {
    private var value: Int = 0
    private var secondVlaue :Int=0
    private var extraTime :Int=0
    private var extraHalfTime :Int=0

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

    fun setextraTime(newValue: Int) {
        extraTime = newValue
    }

    fun getextraTime(): Int {
        return extraTime
    }

    fun setextraHalfTime(newValue: Int) {
        extraHalfTime = newValue
    }

    fun getextraHalfTime(): Int {
        return extraHalfTime
    }

}