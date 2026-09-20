package com.example.ui.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * MessengerAudioEffects provides Facebook Messenger-style sound effects
 * and haptic feedback when sending and receiving chat messages.
 */
object MessengerAudioEffects {
    private var toneGen: ToneGenerator? = null

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
        } catch (_: Exception) {
            // Gracefully ignore if audio device is unavailable
        }
    }

    /**
     * Crisp, upbeat acknowledgment tone when user sends a message.
     */
    fun playMessageSent() {
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
        } catch (_: Exception) {
            try {
                toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
                toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
            } catch (_: Exception) {}
        }
    }

    /**
     * Pleasant notification chime when a new message is received.
     */
    fun playMessageReceived() {
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
        } catch (_: Exception) {
            try {
                toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
            } catch (_: Exception) {}
        }
    }

    /**
     * Subtle haptic feedback for a realistic messaging feel.
     */
    fun playHapticFeedback(context: Context) {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(35)
                }
            }
        } catch (_: Exception) {}
    }
}
