package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*

@Composable
fun ProBadge(modifier: Modifier = Modifier, tier: String? = null) {
    val text = if (tier.isNullOrBlank()) "★ PRO" else "★ PRO $tier"
    Surface(
        modifier = modifier.testTag("pro_badge"),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFDF00).copy(alpha = 0.8f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFFE5A93C), Color(0xFFD97706), Color(0xFFB45309))
                    )
                )
                .padding(horizontal = 9.dp, vertical = 3.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun VerifiedBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.testTag("verified_badge"),
        shape = RoundedCornerShape(12.dp),
        color = Color(0x1F00E5FF),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00B4D8).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "যাচাইকৃত",
                tint = Color(0xFF0284C7),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "যাচাইকৃত",
                color = Color(0xFF0284C7),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MistriAvatar(
    name: String,
    imageUri: String?,
    modifier: Modifier = Modifier,
    size: Int = 60,
    isPro: Boolean = false
) {
    val borderColor = if (isPro) ProGold else BrandBlueDark.copy(alpha = 0.3f)
    val borderWidth = if (isPro) 2.5.dp else 1.5.dp

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUri.isNullOrBlank()) {
            AsyncImage(
                model = imageUri,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            val initials = name.trim().split(" ").filter { it.isNotEmpty() }
                .take(2).map { it.first() }.joinToString("")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(BrandBlueDark, BrandBlue)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.ifEmpty { "মি" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size / 2.6).sp
                )
            }
        }
    }
}

@Composable
fun RatingStarsBar(rating: Double, totalReviews: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = ProGold,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = String.format(java.util.Locale.US, "%.1f", rating),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "($totalReviews রিভিউ)",
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun StatCounterItem(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "$count",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CallWhatsAppButtons(
    onCallClick: () -> Unit,
    onWaClick: () -> Unit,
    modifier: Modifier = Modifier,
    callClicks: Int? = null,
    waClicks: Int? = null,
    callLabel: String = "কল",
    waLabel: String = "হোয়াটসঅ্যাপ"
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onCallClick,
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("action_call_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandBlueDark
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Call",
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            val displayCall = if (callClicks != null && callClicks > 0) "$callLabel ($callClicks)" else callLabel
            Text(
                text = displayCall,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Button(
            onClick = onWaClick,
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("action_whatsapp_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF25D366)
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            val displayWa = if (waClicks != null && waClicks > 0) "$waLabel ($waClicks)" else waLabel
            Text(
                text = displayWa,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
