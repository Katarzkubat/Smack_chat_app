package com.example.katarzkubat.smack.Model

/**
 * Created by katarz.kubat on 05.02.2018.
 */
class Channel(val name: String, val description: String, val id: String) {
    override fun toString(): String {
        return "#$name"
    }
}