package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    durationMillis: Long = 1200L, // 1.2s (inside the 0.5s - 1.5s range requested by user)
    onTimeout: () -> Unit
) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.82f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logoScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = LinearEasing
        ),
        label = "logoAlpha"
    )

    // Glowing halo infinite pulse
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(durationMillis)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable { onTimeout() } // Allow instant skip on tap
            .testTag("splash_screen_container"),
        contentAlignment = Alignment.Center
    ) {
        // Decorative background glow
        Box(
            modifier = Modifier
                .size(320.dp)
                .scale(glowScale)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x1400E5FF),
                            Color(0x14E5A93C),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
                .padding(32.dp)
        ) {
            // App Logo Squircle with Golden Glowing Border
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 12.dp,
                modifier = Modifier
                    .size(140.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFDF00),
                                Color(0xFFE5A93C),
                                Color(0xFF00B4D8),
                                Color(0xFF996515)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .testTag("splash_logo_image")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_hater_kache_logo),
                    contentDescription = "Hater Kache App Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Brand Title (HATER KACHE / হাতের কাছে)
            Text(
                text = "HATER KACHE",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.5.sp,
                color = BrandBlueDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "হাতের কাছে মিস্ত্রি",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Trust • Work • Service Badge
            Surface(
                color = Color(0xFFE0F7FA),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00B4D8).copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRUST  •  WORK  •  SERVICE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF007791)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Sleek Circular Loading Indicator
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = BrandBlueDark,
                    trackColor = Color(0xFFE2E8F0),
                    strokeWidth = 3.dp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "লোড হচ্ছে...",
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }

        // Bottom skip indicator
        Text(
            text = "সরাসরি অ্যাপে প্রবেশ করতে ট্যাপ করুন ›",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}
