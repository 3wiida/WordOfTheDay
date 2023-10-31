package com.mahmoudibrahem.wordoftheday.domain.usecase

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import javax.inject.Inject

class CopyWordUseCase @Inject constructor() {
    operator fun invoke(context: Context, word: String) {
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("word", word)
        clipboard.setPrimaryClip(clip)
    }
}