package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Dialog shown when user taps "বিজ্ঞাপন দিন / Advertise" button on the Home banner.
 */
@Composable
fun AdvertiseContactDialog(
    contactEmail: String,
    sponsorBrand: String,
    isBangladesh: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val emailToUse = contactEmail.ifBlank { "safuanislam2276@gmail.com" }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Megaphone / Advertising Icon Header
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEF3C7),
                    border = BorderStroke(2.dp, Color(0xFFF59E0B)),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Advertise",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isBangladesh) "বিজ্ঞাপন প্রচার ও পার্টনারশিপ" else "Advertise on Hater Kache",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isBangladesh)
                        "হাতের কাছে অ্যাপে প্রতিদিন হাজারো সম্মানিত গ্রাহক ও টেকনিশিয়ান সক্রিয় থাকেন। আপনার কোম্পানি, ব্যবসা বা সেবার বিজ্ঞাপন প্রচার করতে আমাদের সাথে সরাসরি যোগাযোগ করুন।"
                    else
                        "Reach thousands of active customers and verified technicians every day. Place your brand's promotional banner with us.",
                    fontSize = 12.5.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Display Box
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.5.dp, Color(0xFF0084FF).copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFF0084FF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBangladesh) "অফিসিয়াল বিজ্ঞাপন ইমেইল" else "Official Advertising Email",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0084FF)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = emailToUse,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action buttons: Copy Email & Send Mail
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Advertise Email", emailToUse)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, if (isBangladesh) "ইমেইল কপি করা হয়েছে!" else "Email copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("copy_ad_email_button"),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF0084FF))
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF0084FF), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBangladesh) "কপি করুন" else "Copy",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0084FF)
                        )
                    }

                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$emailToUse")
                                    putExtra(Intent.EXTRA_SUBJECT, if (isBangladesh) "হাতের কাছে অ্যাপে বিজ্ঞাপন প্রচার সংক্রান্ত" else "Advertising Inquiry - Hater Kache")
                                    putExtra(Intent.EXTRA_TEXT, if (isBangladesh) "আসসালামু আলাইকুম,\nআমি হাতের কাছে অ্যাপে বিজ্ঞাপন প্রচার করতে আগ্রহী। অনুগ্রহ করে বিস্তারিত বিজ্ঞাপন প্যাকেজ ও রেট প্রদান করুন।" else "Hello,\nI am interested in advertising on Hater Kache app. Please provide advertising rates.")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "ইমেইল অ্যাপ পাওয়া যায়নি। ইমেইল: $emailToUse", Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("send_ad_email_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0084FF))
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBangladesh) "ইমেইল পাঠান" else "Send Email",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_ad_dialog_button")
            ) {
                Text(if (isBangladesh) "ঠিক আছে" else "Close", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(18.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

/**
 * 2-3 Second Interstitial Ad shown when tapping Call or WhatsApp
 * Automatically connects when timer ends or if user taps skip.
 */
@Composable
fun ContactAdDialog(
    sponsorBrand: String,
    title: String,
    description: String,
    actionType: String, // "CALL" or "WHATSAPP"
    isBangladesh: Boolean,
    onProceed: () -> Unit,
    onDismiss: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(3) }
    var isProceeding by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
        if (!isProceeding) {
            isProceeding = true
            onProceed()
        }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(2.dp, Color(0xFFE5A93C)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("contact_ad_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header with Ad badge & Countdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE5A93C),
                    ) {
                        Text(
                            text = if (isBangladesh) "স্পনসরড বিজ্ঞাপন" else "Sponsored Ad",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Live Timer Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, Color(0xFF475569))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                tint = Color(0xFFFFDF00),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (secondsLeft > 0) "$secondsLeft সে..." else if (isBangladesh) "কানেক্ট হচ্ছে..." else "Connecting...",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Sponsor Brand Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = sponsorBrand.ifBlank { if (isBangladesh) "ওয়ালটন স্মার্ট অ্যাপ্লায়েন্স" else "Walton Electronics" },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Ad Headline & Description
                Text(
                    text = title.ifBlank { if (isBangladesh) "সেরা হোম অ্যাপ্লায়েন্স ও ইলেকট্রনিক্স!" else "Top Home Appliances & Electronics" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description.ifBlank {
                        if (isBangladesh)
                            "অনলাইন অর্ডারে পাচ্ছেন ১০% ক্যাশব্যাক ও দ্রুত হোম ডেলিভারি।"
                        else
                            "Order online now to get special discounts and fast home delivery."
                    },
                    fontSize = 13.sp,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { (3 - secondsLeft) / 3f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFE5A93C),
                    trackColor = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Direct Skip & Connect Button
                Button(
                    onClick = {
                        if (!isProceeding) {
                            isProceeding = true
                            onProceed()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("contact_ad_skip_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (actionType == "WHATSAPP") Color(0xFF25D366) else Color(0xFF0084FF)
                    )
                ) {
                    Icon(
                        imageVector = if (actionType == "WHATSAPP") Icons.Default.Chat else Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBangladesh) "সরাসরি যোগাযোগ করুন ›" else "Connect Directly ›",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("contact_ad_cancel_button")
                ) {
                    Text(
                        text = if (isBangladesh) "বাতিল করুন" else "Cancel",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}
