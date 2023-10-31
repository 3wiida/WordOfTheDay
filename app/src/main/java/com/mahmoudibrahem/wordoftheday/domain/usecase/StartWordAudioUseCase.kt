package com.mahmoudibrahem.wordoftheday.domain.usecase

import android.media.MediaPlayer
import android.util.Log
import javax.inject.Inject

class StartWordAudioUseCase @Inject constructor(
    private val mediaPlayer: MediaPlayer
) {
    operator fun invoke(audioUrl: String) {
        try {
            mediaPlayer.apply {
                reset()
                setDataSource(audioUrl)
                prepare()
                setVolume(1f, 1f)
                isLooping = false
                start()
            }
        } catch (e: Exception) {
            Log.d("```TAG```", "invoke: $e")
        }

    }
}