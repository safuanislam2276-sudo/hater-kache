package com.example.ui.components

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.ui.viewmodel.ActiveCallState
import java.util.Locale

@Composable
fun InAppMessengerCallDialog(
    callState: ActiveCallState,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onEndCall: () -> Unit
) {
    val context = LocalContext.current
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager }

    // Manage speakerphone & microphone audio hardware state
    LaunchedEffect(callState.isSpeakerOn) {
        try {
            audioManager?.let { am ->
                am.mode = AudioManager.MODE_IN_COMMUNICATION
                am.isSpeakerphoneOn = callState.isSpeakerOn
            }
        } catch (_: Exception) {}
    }

    LaunchedEffect(callState.isMuted) {
        try {
            audioManager?.isMicrophoneMute = callState.isMuted
        } catch (_: Exception) {}
    }

    // Play ringing tone while in CALLING state
    DisposableEffect(callState.status) {
        var toneGen: ToneGenerator? = null
        if (callState.status == "CALLING") {
            try {
                val stream = if (callState.isSpeakerOn) AudioManager.STREAM_MUSIC else AudioManager.STREAM_VOICE_CALL
                toneGen = ToneGenerator(stream, 75)
                toneGen.startTone(ToneGenerator.TONE_SUP_RINGTONE)
            } catch (_: Exception) {}
        }

        onDispose {
            try {
                toneGen?.stopTone()
                toneGen?.release()
            } catch (_: Exception) {}
        }
    }

    // Restore normal audio mode on exit
    DisposableEffect(Unit) {
        onDispose {
            try {
                audioManager?.mode = AudioManager.MODE_NORMAL
                audioManager?.isSpeakerphoneOn = false
                audioManager?.isMicrophoneMute = false
            } catch (_: Exception) {}
        }
    }

    // Pulsing animation for calling rings
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Format duration MM:SS
    val minutes = callState.durationSeconds / 60
    val seconds = callState.durationSeconds % 60
    val durationText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Dialog(
        onDismissRequest = onEndCall,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A), // Slate 900
                            Color(0xFF0B192C),
                            Color(0xFF020617)  // Deep midnight
                        )
                    )
                )
                .systemBarsPadding()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header Information
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0x22FFFFFF),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("🔒", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "মেসেঞ্জার ভয়েস কল • ইন-অ্যাপ সুরক্ষিত কল",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = callState.recipientName,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (callState.recipientRole == "MISTRI") {
                            "কারিগর মিস্ত্রি${if (!callState.recipientProfession.isNullOrBlank()) " • ${callState.recipientProfession}" else ""}"
                        } else {
                            "সম্মানিত গ্রাহক"
                        },
                        color = Color(0xFF60A5FA),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Status Indicator
                    when (callState.status) {
                        "CALLING" -> {
                            Text(
                                text = "কল করা হচ্ছে...",
                                color = Color(0xFF38BDF8),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        "CONNECTED" -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF22C55E))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "কথা চলছে: $durationText",
                                    color = Color(0xFF4ADE80),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        else -> {
                            Text(
                                text = "কল শেষ হয়েছে",
                                color = Color(0xFFF87171),
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                // Avatar with Pulse Aura
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(200.dp)
                ) {
                    if (callState.status == "CALLING") {
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(Color(0x2238BDF8))
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (callState.recipientRole == "MISTRI") Color(0xFFFEF3C7) else Color(0xFFDBEAFE),
                        border = BorderStroke(3.dp, if (callState.isSpeakerOn) Color(0xFF38BDF8) else Color(0xFF2563EB)),
                        modifier = Modifier.size(120.dp),
                        shadowElevation = 8.dp
                    ) {
                        if (!callState.recipientImage.isNullOrBlank()) {
                            AsyncImage(
                                model = callState.recipientImage,
                                contentDescription = callState.recipientName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = callState.recipientName.take(1).uppercase(),
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (callState.recipientRole == "MISTRI") Color(0xFFB45309) else Color(0xFF1E40AF)
                                )
                            }
                        }
                    }
                }

                // Controls Area
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Secondary action buttons (Speaker & Mute)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Loudspeaker Button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                shape = CircleShape,
                                color = if (callState.isSpeakerOn) Color(0xFF2563EB) else Color(0x33FFFFFF),
                                border = BorderStroke(1.5.dp, if (callState.isSpeakerOn) Color(0xFF60A5FA) else Color(0x44FFFFFF)),
                                onClick = onToggleSpeaker,
                                modifier = Modifier.size(60.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (callState.isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                                        contentDescription = "Speaker",
                                        tint = if (callState.isSpeakerOn) Color.White else Color(0xFFE2E8F0),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (callState.isSpeakerOn) "লাউডস্পিকার অন" else "লাউডস্পিকার",
                                color = if (callState.isSpeakerOn) Color(0xFF60A5FA) else Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = if (callState.isSpeakerOn) FontWeight.Bold else FontWeight.Normal
                            )
                        }

                        // Microphone Mute Button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                shape = CircleShape,
                                color = if (callState.isMuted) Color(0xFFDC2626) else Color(0x33FFFFFF),
                                border = BorderStroke(1.5.dp, if (callState.isMuted) Color(0xFFF87171) else Color(0x44FFFFFF)),
                                onClick = onToggleMute,
                                modifier = Modifier.size(60.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (callState.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                        contentDescription = "Mute",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (callState.isMuted) "মাইক্রোফোন মিউট" else "মিউট",
                                color = if (callState.isMuted) Color(0xFFF87171) else Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = if (callState.isMuted) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // End Call Button (Big Red Button)
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFEF4444),
                        shadowElevation = 10.dp,
                        onClick = onEndCall,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CallEnd,
                                contentDescription = "End Call",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "কল শেষ করুন",
                        color = Color(0xFFFCA5A5),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
