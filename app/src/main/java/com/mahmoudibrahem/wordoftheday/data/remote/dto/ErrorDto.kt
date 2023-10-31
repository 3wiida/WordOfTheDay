package com.mahmoudibrahem.wordoftheday.data.remote.dto

import com.mahmoudibrahem.wordoftheday.domain.model.Error

data class ErrorDto(
    val message: String,
    val resolution: String,
    val title: String
) {
    fun toError() = Error(message = message, resolution = resolution, title = title)
}