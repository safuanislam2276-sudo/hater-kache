package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.data.model.AdminSettingEntity
import com.example.data.model.MistriEntity
import com.example.data.model.getAllCategoriesList
import com.example.ui.components.MistriAvatar
import com.example.ui.components.ProBadge
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.*
import com.example.ui.util.Localization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.example.ui.components.MultiCategorySelector
import com.example.ui.components.SearchableLocationPickerDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MistriDashboardScreen(
    mistri: MistriEntity,
    adminSettings: List<AdminSettingEntity>,
    onBack: () -> Unit,
    onNavigateToMessenger: () -> Unit = {},
    onPurchaseSubscription: (plan: String, amount: String, method: String, trxId: String, onDone: () -> Unit, onError: (String) -> Unit) -> Unit,
    onUpdateProfile: (
        name: String,
        phone: String,
        profession: String,
        extraCategories: String,
        division: String,
        district: String,
        address: String,
        exp: Int,
        bio: String,
        skills: String,
        onDone: () -> Unit
    ) -> Unit
) {
    var showSubscriptionDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val isBangladesh = mistri.countryCode == "BD" ||
            mistri.countryCode == "+880" ||
            mistri.country.contains("বাংলাদেশ", ignoreCase = true) ||
            mistri.country.equals("Bangladesh", ignoreCase = true)
    val strings = Localization.getStrings(isBangladesh)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.dashboardButton, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("dashboard_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditProfileDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = strings.editProfileButton)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mistri Profile Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = if (mistri.isPro) androidx.compose.foundation.BorderStroke(2.dp, ProGold) else androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MistriAvatar(
                        name = mistri.name,
                        imageUri = mistri.profileImageUri,
                        size = 70,
                        isPro = mistri.isPro
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mistri.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            OutlinedButton(
                                onClick = { showEditProfileDialog = true },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.2.dp, BrandBlue),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.testTag("dashboard_edit_profile_button")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBangladesh) "এডিট" else "Edit",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandBlue
                                )
                            }
                        }
                        Text(
                            text = CountryRepository.getProfessionDisplayName(mistri.profession, isBangladesh),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${mistri.district}, ${mistri.division} (${mistri.country})",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        // Multi-Category Display (Category 1 through 6)
                        val allCategories = mistri.getAllCategoriesList()
                        if (allCategories.size > 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                allCategories.forEachIndexed { idx, cat ->
                                    val catLabel = if (isBangladesh) {
                                        listOf("১", "২", "৩", "৪", "৫", "৬").getOrElse(idx) { "${idx + 1}" }
                                    } else "${idx + 1}"
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (idx == 0) BrandBlue.copy(alpha = 0.12f) else Color(0xFFF1F5F9),
                                        border = BorderStroke(1.dp, if (idx == 0) BrandBlue else Color(0xFFCBD5E1))
                                    ) {
                                        Text(
                                            text = "$catLabel. ${CountryRepository.getProfessionDisplayName(cat, isBangladesh)}",
                                            fontSize = 10.sp,
                                            fontWeight = if (idx == 0) FontWeight.Bold else FontWeight.Medium,
                                            color = if (idx == 0) BrandBlueDark else TextPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (mistri.isVerified) VerifiedBadge()
                            if (mistri.isPro) ProBadge(tier = mistri.proTier)
                        }
                    }
                }
            }

            // Messenger Inbox Option for Workers
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToMessenger() }
                    .testTag("dashboard_messenger_inbox_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = BorderStroke(1.5.dp, Color(0xFF0084FF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF0084FF),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = "Messenger",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBangladesh) "গ্রাহকের মেসেঞ্জার ইনবক্স" else "Customer Messenger Inbox",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                        Text(
                            text = if (isBangladesh) "গ্রাহকদের পাঠানো বার্তা দেখুন ও সরাসরি উত্তর দিন" else "View and reply to messages from customers",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF0084FF)
                    )
                }
            }

            // Real-Time Interaction Stats (Call & WhatsApp Clicks)
            Text(
                text = if (isBangladesh) "লাইভ পারফরম্যান্স ও গ্রাহক প্রতিক্রিয়া" else "Live Performance & Reach",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Call clicks
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBangladesh) "কল আগ্রহ" else "Call Clicks",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${mistri.callClicks}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isBangladesh) "গ্রাহক নম্বর দেখেছেন" else "customer inquiries",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // WhatsApp clicks
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldGreenLight),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBangladesh) "হোয়াটসঅ্যাপ" else "WhatsApp",
                                fontSize = 12.sp,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${mistri.waClicks}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Text(
                            text = if (isBangladesh) "সরাসরি চ্যাট শুরু" else "chats started",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // PRO Membership Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (mistri.isPro) ProGoldLight.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = if (mistri.isPro) androidx.compose.foundation.BorderStroke(2.dp, ProGold) else androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Stars,
                                contentDescription = null,
                                tint = if (mistri.isPro) ProGoldDark else TextSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.proMembershipTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        if (mistri.isPro) {
                            ProBadge(tier = mistri.proTier)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (mistri.isPro) {
                        Text(
                            text = strings.proActiveMessage,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBangladesh) "প্ল্যান: PRO সদস্য | ৩০ দিন মেয়াদ" else "Plan: PRO Member | 30 Days Access",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = strings.proPromoMessage,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showSubscriptionDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ProGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("buy_subscription_button")
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.buyProButtonText, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Quick Edit Profile Button
            OutlinedButton(
                onClick = { showEditProfileDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(strings.editProfileButton)
            }
        }
    }

    // Subscription Purchase Dialog
    if (showSubscriptionDialog) {
        SubscriptionPurchaseDialog(
            mistri = mistri,
            adminSettings = adminSettings,
            isBangladesh = isBangladesh,
            onDismiss = { showSubscriptionDialog = false },
            onSubmit = { plan, amount, method, trxId ->
                onPurchaseSubscription(plan, amount, method, trxId, {
                    showSubscriptionDialog = false
                }, { /* error handled */ })
            }
        )
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        EditMistriProfileDialog(
            mistri = mistri,
            isBangladesh = isBangladesh,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, phone, prof, extraCats, div, dist, addr, exp, bio, skills ->
                onUpdateProfile(name, phone, prof, extraCats, div, dist, addr, exp, bio, skills) {
                    showEditProfileDialog = false
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPurchaseDialog(
    mistri: MistriEntity,
    adminSettings: List<AdminSettingEntity>,
    isBangladesh: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (plan: String, amount: String, method: String, trxId: String) -> Unit
) {
    // Dynamic price based on user country requirement:
    // Bangladesh = 100 Taka (৳১০০), All Other Countries = $2.00 USD ($2)
    val priceLabel = if (isBangladesh) "১০০ টাকা (৳১০০)" else "$2.00 USD"
    val durationLabel = if (isBangladesh) "৩০ দিন মেয়াদ | শীর্ষ র্যাংক ও PRO ব্যাজ" else "30 Days Validity | Top Ranking & PRO Badge"

    val settingsMap = remember(adminSettings) { adminSettings.associate { it.key to it.value } }

    val paymentMethods = if (isBangladesh) {
        listOf("bKash", "Nagad", "Rocket", "Bank")
    } else {
        listOf("Card", "Bank Wire", "PayPal / Wise")
    }

    var selectedMethod by remember { mutableStateOf(paymentMethods.first()) }
    var transactionId by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f),
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFCBD5E1))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBangladesh) "PRO সাবস্ক্রিপশন কিনুন" else "Upgrade to PRO Subscription",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Price display card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ProGoldLight),
                    border = androidx.compose.foundation.BorderStroke(2.dp, ProGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isBangladesh) "মাসিক PRO প্যাকেজ" else "Monthly PRO Package",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(text = durationLabel, fontSize = 11.sp, color = TextSecondary)
                        }
                        Text(text = priceLabel, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ProGoldDark)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isBangladesh) "পেমেন্ট মেথড নির্বাচন করুন:" else "Select Payment Method:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    paymentMethods.forEach { method ->
                        FilterChip(
                            selected = selectedMethod == method,
                            onClick = { selectedMethod = method },
                            label = { Text(method, fontWeight = FontWeight.Bold) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Instruction Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isBangladesh) "পেমেন্ট নির্দেশনা ($selectedMethod):" else "Payment Details ($selectedMethod):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        if (isBangladesh) {
                            when (selectedMethod) {
                                "bKash" -> {
                                    val num = settingsMap["bkash_number"] ?: "01812345678"
                                    Text("বিকাশ নম্বর: $num (Send Money)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("১০০ টাকা পাঠিয়ে নিচের বক্সে TrxID লিখুন।", fontSize = 12.sp, color = TextSecondary)
                                }
                                "Nagad" -> {
                                    val num = settingsMap["nagad_number"] ?: "01712345678"
                                    Text("নগদ নম্বর: $num (Send Money)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("১০০ টাকা পাঠিয়ে নিচের বক্সে TrxID লিখুন।", fontSize = 12.sp, color = TextSecondary)
                                }
                                "Rocket" -> {
                                    val num = settingsMap["rocket_number"] ?: "01912345678"
                                    Text("রকেট নম্বর: $num (Send Money)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("১০০ টাকা পাঠিয়ে নিচের বক্সে TrxID লিখুন।", fontSize = 12.sp, color = TextSecondary)
                                }
                                "Bank" -> {
                                    val bank = settingsMap["bd_bank_name"] ?: "City Bank Ltd"
                                    val accName = settingsMap["bd_bank_account_name"] ?: "Hater Kache Official"
                                    val accNum = settingsMap["bd_bank_account_number"] ?: "1502839201928"
                                    val branch = settingsMap["bd_bank_branch"] ?: "Dhaka"
                                    Text("ব্যাংক: $bank", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("হিসাব নাম: $accName", fontSize = 12.sp)
                                    Text("হিসাব নং: $accNum | শাখা: $branch", fontSize = 12.sp)
                                }
                            }
                        } else {
                            when (selectedMethod) {
                                "Card" -> {
                                    val note = settingsMap["intl_card_instructions"] ?: "Visa / Mastercard accepted"
                                    Text("Fee: $2.00 USD", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(note, fontSize = 12.sp, color = TextSecondary)
                                }
                                "Bank Wire" -> {
                                    val bank = settingsMap["intl_bank_name"] ?: "Standard Chartered"
                                    val bene = settingsMap["intl_bank_beneficiary"] ?: "Hater Kache Global Services Ltd"
                                    val iban = settingsMap["intl_bank_iban"] ?: "GB29NWBK60161331926819"
                                    val swift = settingsMap["intl_bank_swift"] ?: "SCBLBDDX"
                                    Text("Bank: $bank", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Beneficiary: $bene", fontSize = 12.sp)
                                    Text("IBAN: $iban", fontSize = 12.sp)
                                    Text("SWIFT: $swift", fontSize = 12.sp)
                                }
                                "PayPal / Wise" -> {
                                    val email = settingsMap["intl_paypal_email"] ?: "payments@haterkache.com"
                                    Text("Send $2.00 USD to: $email", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = transactionId,
                    onValueChange = {
                        transactionId = it
                        errorText = null
                    },
                    label = { Text(if (isBangladesh) "ট্রানজেকশন আইডি (TrxID) *" else "Transaction / Reference ID *") },
                    placeholder = { Text(if (isBangladesh) "যেমন: 8N7A6D5F9" else "TXN-987654321") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("trx_id_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                if (errorText != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = errorText!!, color = ErrorRed, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (transactionId.trim().isEmpty()) {
                            errorText = if (isBangladesh) "অনুগ্রহ করে TrxID প্রদান করুন" else "Please enter your Transaction ID"
                        } else {
                            onSubmit(
                                "Pro",
                                if (isBangladesh) "১০০ টাকা" else "$2.00 USD",
                                selectedMethod,
                                transactionId.trim()
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_subscription_button")
                ) {
                    Text(
                        text = if (isBangladesh) "পেমেন্ট সম্পন্ন করুন ($priceLabel)" else "Complete Payment ($priceLabel)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMistriProfileDialog(
    mistri: MistriEntity,
    isBangladesh: Boolean,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        phone: String,
        prof: String,
        extraCats: String,
        div: String,
        dist: String,
        addr: String,
        exp: Int,
        bio: String,
        skills: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(mistri.name) }
    var phone by remember { mutableStateOf(mistri.phone) }
    var division by remember { mutableStateOf(mistri.division) }
    var district by remember { mutableStateOf(mistri.district) }
    var address by remember { mutableStateOf(mistri.address) }

    var selectedCategories by remember {
        mutableStateOf(mistri.getAllCategoriesList().ifEmpty { listOf(mistri.profession) })
    }

    var expText by remember { mutableStateOf(mistri.experienceYears.toString()) }
    var bio by remember { mutableStateOf(mistri.bio) }
    var skills by remember { mutableStateOf(mistri.skills) }

    var showDivisionPicker by remember { mutableStateOf(false) }
    var showDistrictPicker by remember { mutableStateOf(false) }

    val countryInfo = remember(mistri.country, mistri.countryCode) {
        CountryRepository.ALL_COUNTRIES.find {
            it.code.equals(mistri.countryCode, ignoreCase = true) ||
                    it.nameBn.equals(mistri.country, ignoreCase = true) ||
                    it.nameEn.equals(mistri.country, ignoreCase = true)
        } ?: CountryRepository.PRIMARY_COUNTRIES.first()
    }

    val isCountryBd = countryInfo.code == "BD" || countryInfo.nameEn.equals("Bangladesh", ignoreCase = true)
    val showBn = isBangladesh && isCountryBd
    val divisionsMap = remember(countryInfo, showBn) {
        countryInfo.getDivisions(showBn)
    }
    val divisionList = remember(divisionsMap) { divisionsMap.keys.toList() }
    val districtList = remember(division, divisionsMap) {
        if (division.isNotEmpty() && divisionsMap.containsKey(division)) {
            divisionsMap[division] ?: emptyList()
        } else {
            divisionsMap.values.flatten().distinct()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f),
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBangladesh) "প্রোফাইল তথ্য ও ক্যাটাগরি এডিট করুন" else "Edit Profile & Categories",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isBangladesh) "আপনার নাম *" else "Full Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(if (isBangladesh) "ফোন নম্বর *" else "Phone Number *") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Searchable Division & District Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedCard(
                        onClick = { showDivisionPicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF64748B))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(if (isBangladesh) "বিভাগ *" else "Division *", fontSize = 11.sp, color = TextSecondary)
                            Text(
                                text = division.ifEmpty { if (isBangladesh) "নির্বাচন করুন" else "Select" },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (division.isEmpty()) Color(0xFF64748B) else TextPrimary,
                                maxLines = 1
                            )
                        }
                    }

                    OutlinedCard(
                        onClick = {
                            if (division.isNotEmpty()) {
                                showDistrictPicker = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF64748B))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(if (isBangladesh) "জেলা *" else "District *", fontSize = 11.sp, color = TextSecondary)
                            Text(
                                text = district.ifEmpty { if (isBangladesh) "নির্বাচন করুন" else "Select" },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (district.isEmpty()) Color(0xFF64748B) else TextPrimary,
                                maxLines = 1
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(if (isBangladesh) "বিস্তারিত এলাকা / ঠিকানা" else "Detailed Address / Area") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Multi-Category Selection (1 to 6 categories with live search)
                MultiCategorySelector(
                    selectedCategories = selectedCategories,
                    onCategoriesChanged = { updated ->
                        selectedCategories = if (updated.isEmpty()) {
                            listOf(mistri.profession)
                        } else updated
                    },
                    isBangladesh = isBangladesh,
                    maxCategories = 6
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = expText,
                    onValueChange = { expText = it.filter { ch -> ch.isDigit() } },
                    label = { Text(if (isBangladesh) "কাজের অভিজ্ঞতা (বছর) *" else "Experience (years) *") },
                    leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(if (isBangladesh) "কাজের পরিচিতি ও সেবা বিবরণ" else "Bio / Services description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 4
                )

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text(if (isBangladesh) "দক্ষতা (কমা দিয়ে আলাদা করুন)" else "Skills (comma separated)") },
                    leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (showDivisionPicker) {
                SearchableLocationPickerDialog(
                    title = if (isBangladesh) "বিভাগ নির্বাচন করুন" else "Select Division",
                    items = divisionList,
                    selectedItem = division,
                    searchPlaceholder = if (isBangladesh) "বিভাগ সার্চ করুন..." else "Search division...",
                    onItemSelected = { chosenDiv ->
                        division = chosenDiv
                        district = ""
                        showDivisionPicker = false
                    },
                    onDismissRequest = { showDivisionPicker = false }
                )
            }

            if (showDistrictPicker) {
                SearchableLocationPickerDialog(
                    title = if (isBangladesh) "জেলা নির্বাচন করুন" else "Select District",
                    items = districtList,
                    selectedItem = district,
                    searchPlaceholder = if (isBangladesh) "জেলা সার্চ করুন..." else "Search district...",
                    onItemSelected = { chosenDist ->
                        district = chosenDist
                        showDistrictPicker = false
                    },
                    onDismissRequest = { showDistrictPicker = false }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val exp = expText.toIntOrNull() ?: mistri.experienceYears
                    val primaryProf = selectedCategories.firstOrNull() ?: mistri.profession
                    val extraCats = selectedCategories.drop(1).joinToString(",")
                    onSave(
                        name.trim(),
                        phone.trim(),
                        primaryProf,
                        extraCats,
                        division.trim(),
                        district.trim(),
                        address.trim(),
                        exp,
                        bio.trim(),
                        skills.trim()
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.testTag("save_mistri_profile_button")
            ) {
                Text(if (isBangladesh) "সেভ করুন" else "Save Changes", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isBangladesh) "বাতিল" else "Cancel")
            }
        }
    )
}
