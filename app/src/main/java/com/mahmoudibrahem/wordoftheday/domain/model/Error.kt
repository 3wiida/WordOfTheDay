package com.mahmoudibrahem.wordoftheday.domain.model

data class Error(
    val message: String,
    val resolution: String,
    val title: String
)