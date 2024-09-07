package com.practicum.playlistmaker.data.dto

open class Response() {
    var resultCode = 0
    fun isSuccessful(): Boolean {
        if (resultCode==200){
            return true
        } else{
            return false
        }
    }
}