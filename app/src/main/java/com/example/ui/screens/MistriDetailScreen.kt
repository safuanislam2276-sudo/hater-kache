package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.country.CountryRepository
import com.example.data.model.MistriEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.UserEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.util.Localization
import com.example.ui.viewmodel.BannerState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MistriDetailScreen(
    mistri: MistriEntity,
    reviews: List<ReviewEntity>,
    currentUser: UserEntity?,
    bannerState: BannerState? = null,
    onBack: () -> Unit,
    onCallClick: () -> Unit,
    onWaClick: () -> Unit,
    onChatClick: () -> Unit = {},
    onSubmitReview: (rating: Int, text: String, onDone: () -> Unit) -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val isBangladesh = mistri.countryCode == "BD" ||
            mistri.country.contains("বাংলাদেশ", ignoreCase = true) ||
            mistri.country.equals("Bangladesh", ignoreCase = true)
    val strings = Localization.getStrings(isBangladesh)

    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewRating by remember { mutableStateOf(5) }
    var reviewText by remember { mutableStateOf("") }
    var reviewError by remember { mutableStateOf<String?>(null) }
    var pendingContactAd by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    val handleCall = {
        if (bannerState?.contactAdEnabled == true) {
            pendingContactAd = Pair("CALL", onCallClick)
        } else {
            onCallClick()
        }
    }

    val handleWa = {
        if (bannerState?.contactAdEnabled == true) {
            pendingContactAd = Pair("WHATSAPP", onWaClick)
        } else {
            onWaClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = mistri.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onChatClick, modifier = Modifier.testTag("detail_top_chat_button")) {
                        Icon(Icons.Default.Forum, contentDescription = "Chat", tint = Color(0xFF0084FF))
                    }
                    if (mistri.isPro) {
                        ProBadge(tier = mistri.proTier, modifier = Modifier.padding(end = 8.dp))
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    // Facebook Messenger style chat button
                    Button(
                        onClick = onChatClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("detail_chat_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0084FF))
                    ) {
                        Icon(Icons.Default.Forum, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangladesh) "মেসেঞ্জারে সরাসরি চ্যাট করুন" else "Chat on Messenger",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CallWhatsAppButtons(
                        onCallClick = handleCall,
                        onWaClick = handleWa,
                        callClicks = mistri.callClicks,
                        waClicks = mistri.waClicks,
                        callLabel = strings.callButton,
                        waLabel = strings.waButton
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = if (mistri.isPro) androidx.compose.foundation.BorderStroke(2.dp, ProGold) else androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MistriAvatar(
                            name = mistri.name,
                            imageUri = mistri.profileImageUri,
                            size = 84,
                            isPro = mistri.isPro
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = mistri.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CountryRepository.getProfessionDisplayName(mistri.profession, isBangladesh),
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (mistri.isVerified) {
                                VerifiedBadge()
                            }
                            if (mistri.isPro) {
                                ProBadge(tier = mistri.proTier)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = ProGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = String.format(java.util.Locale.US, "%.1f", mistri.averageRating),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                                Text(
                                    text = if (isBangladesh) "${mistri.totalReviews} রিভিউ" else "${mistri.totalReviews} Reviews",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (isBangladesh) "${mistri.experienceYears} বছর" else "${mistri.experienceYears} Yrs",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (isBangladesh) "অভিজ্ঞতা" else "Experience",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${mistri.callClicks + mistri.waClicks}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = EmeraldGreen
                                )
                                Text(
                                    text = if (isBangladesh) "যোগাযোগ সফল" else "Contacts Made",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Location & Address
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isBangladesh) "সার্ভিস লোকেশন" else "Service Location",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "${mistri.district}, ${mistri.division} (${mistri.country})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            if (mistri.address.isNotBlank()) {
                                Text(
                                    text = mistri.address,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Bio
            if (mistri.bio.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isBangladesh) "কারিগর সম্পর্কে" else "About Technician",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = mistri.bio,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // Skills tags
            if (mistri.skills.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isBangladesh) "দক্ষতা ও সেবা সমূহ" else "Skills & Services",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                mistri.skills.split(",").forEach { skill ->
                                    val trimmed = skill.trim()
                                    if (trimmed.isNotEmpty()) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                        ) {
                                            Text(
                                                text = trimmed,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Reviews Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isBangladesh) "গ্রাহকদের রিভিউ (${reviews.size})" else "Customer Reviews (${reviews.size})",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${if (isBangladesh) "গড় রেটিং:" else "Average:"} ${String.format(java.util.Locale.US, "%.1f", mistri.averageRating)} ★",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = {
                            if (currentUser == null) {
                                onNavigateToAuth()
                            } else {
                                showReviewDialog = true
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_review_button")
                    ) {
                        Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBangladesh) "রিভিউ দিন" else "Write Review",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (reviews.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isBangladesh) "এখনও কোনো রিভিউ দেওয়া হয়নি" else "No reviews submitted yet",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = if (isBangladesh) "এই মিস্ত্রির সাথে কাজ করে থাকলে প্রথম রিভিউ দিন!" else "Be the first to share your experience with this technician!",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(reviews, key = { it.id }) { rev ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = rev.customerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Row {
                                    repeat(5) { i ->
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = if (i < rev.rating) ProGold else TextMuted.copy(alpha = 0.3f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = rev.reviewText,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Write Review Dialog
    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text(if (isBangladesh) "রিভিউ ও রেটিং দিন" else "Write Rating & Review") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isBangladesh) "রেটিং নির্বাচন করুন:" else "Select Rating:",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        (1..5).forEach { star ->
                            IconButton(onClick = { reviewRating = star }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star Stars",
                                    tint = if (star <= reviewRating) ProGold else TextMuted.copy(alpha = 0.3f),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = {
                            reviewText = it
                            reviewError = null
                        },
                        label = { Text(if (isBangladesh) "আপনার কাজের অভিজ্ঞতা লিখুন..." else "Describe your work experience...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
                            .testTag("review_input_field"),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4
                    )
                    if (reviewError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = reviewError!!, color = ErrorRed, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reviewText.isBlank()) {
                            reviewError = if (isBangladesh) "দয়া করে রিভিউ লিখুন" else "Please enter your review"
                        } else {
                            onSubmitReview(reviewRating, reviewText) {
                                showReviewDialog = false
                                reviewText = ""
                            }
                        }
                    },
                    modifier = Modifier.testTag("submit_review_confirm_button")
                ) {
                    Text(if (isBangladesh) "জমা দিন" else "Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) {
                    Text(if (isBangladesh) "বাতিল" else "Cancel")
                }
            }
        )
    }

    if (pendingContactAd != null) {
        ContactAdDialog(
            sponsorBrand = bannerState?.contactAdBrand ?: "ওয়ালটন স্মার্ট অ্যাপ্লায়েন্স",
            title = bannerState?.contactAdTitle ?: "সেরা হোম অ্যাপ্লায়েন্স ও ইলেকট্রনিক্স!",
            description = bannerState?.contactAdDesc ?: "অনলাইন অর্ডারে পাচ্ছেন ১০% ক্যাশব্যাক ও দ্রুত হোম ডেলিভারি।",
            actionType = pendingContactAd!!.first,
            isBangladesh = isBangladesh,
            onProceed = {
                val action = pendingContactAd!!.second
                pendingContactAd = null
                action()
            },
            onDismiss = {
                pendingContactAd = null
            }
        )
    }
}
