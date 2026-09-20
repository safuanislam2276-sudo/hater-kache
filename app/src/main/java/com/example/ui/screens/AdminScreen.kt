package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminSettingEntity
import com.example.data.model.CountryStat
import com.example.data.model.MistriEntity
import com.example.data.model.SubscriptionEntity
import com.example.ui.components.MistriAvatar
import com.example.ui.components.ProBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    isUnlocked: Boolean,
    totalWorkers: Int,
    blockedWorkers: Int,
    proWorkers: Int,
    allMistris: List<MistriEntity>,
    pendingSubscriptions: List<SubscriptionEntity>,
    adminSettings: List<AdminSettingEntity>,
    viewsByCountry: List<CountryStat>,
    signupsByCountry: List<CountryStat>,
    onBack: () -> Unit,
    onUnlock: (String) -> Boolean,
    onLock: () -> Unit,
    onApproveSubscription: (SubscriptionEntity) -> Unit,
    onRejectSubscription: (String) -> Unit,
    onToggleBlock: (MistriEntity) -> Unit,
    onToggleDirectPro: (MistriEntity) -> Unit,
    onUpdateThemeColor: (String) -> Unit,
    onUpdateBanner: (active: Boolean, titleBn: String, titleEn: String, noticeBn: String, noticeEn: String, imgUrl: String) -> Unit,
    onUpdateSetting: (key: String, value: String) -> Unit
) {
    var emailOrCodeInput by remember { mutableStateOf("") }
    var unlockError by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) }

    // Colors presets
    val presetColors = listOf(
        Pair("Sky Blue (আকাশী নীল)", "#0284C7"),
        Pair("Royal Navy (রয়্যাল নেভি)", "#0369A1"),
        Pair("Emerald Trust (সবুজ)", "#059669"),
        Pair("Crimson Ruby (লাল)", "#BE123C"),
        Pair("Golden Amber (অ্যাম্বার)", "#D97706"),
        Pair("Deep Indigo (ইন্ডিগো)", "#4338CA"),
        Pair("Deep Teal (টিল)", "#0D9488"),
        Pair("Midnight Slate (ডার্ক স্লেট)", "#0F172A")
    )

    val currentThemeHex = adminSettings.find { it.key == "app_theme_color" }?.value ?: "#0284C7"
    var themeInput by remember(currentThemeHex) { mutableStateOf(currentThemeHex) }

    // Gateways state
    var bkashNum by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "bkash_number" }?.value ?: "01812345678") }
    var nagadNum by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "nagad_number" }?.value ?: "01712345678") }
    var rocketNum by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "rocket_number" }?.value ?: "01912345678") }
    var bdBankName by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "bd_bank_name" }?.value ?: "City Bank Ltd / Dutch Bangla Bank") }
    var bdAccName by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "bd_bank_account_name" }?.value ?: "Hater Kache Official") }
    var bdAccNum by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "bd_bank_account_number" }?.value ?: "1502839201928") }
    var bdBranch by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "bd_bank_branch" }?.value ?: "Gulshan Branch, Dhaka") }

    var intlBankName by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "intl_bank_name" }?.value ?: "Standard Chartered / JP Morgan Chase") }
    var intlBeneficiary by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "intl_bank_beneficiary" }?.value ?: "Hater Kache Global Services Ltd") }
    var intlIban by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "intl_bank_iban" }?.value ?: "GB29NWBK60161331926819") }
    var intlSwift by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "intl_bank_swift" }?.value ?: "SCBLBDDX") }
    var intlPaypal by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "intl_paypal_email" }?.value ?: "payments@haterkache.com") }
    var intlCardNote by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "intl_card_instructions" }?.value ?: "Visa, MasterCard, American Express accepted. Enter confirmation code.") }
    var subPriceBd by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "sub_price_bd" }?.value ?: "100") }
    var subPriceIntl by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "sub_price_intl" }?.value ?: "2.00") }

    // Banner state
    var bannerActive by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "banner_active" }?.value != "false") }
    var bannerTitleBn by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "banner_title_bn" }?.value ?: "হাতের কাছে মেগা অফার — ২০% ছাড়!") }
    var bannerTitleEn by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "banner_title_en" }?.value ?: "Hater Kache Special Offer — 20% Off!") }
    var bannerNoticeBn by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "banner_notice_bn" }?.value ?: "দক্ষ ও বিশ্বস্ত কারিগর এখন আপনার এলাকাতেই! সরাসরি কল বা হোয়াটসঅ্যাপ করুন।") }
    var bannerNoticeEn by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "banner_notice_en" }?.value ?: "Find verified & professional technicians nearby! Call or WhatsApp directly.") }
    var bannerImgUrl by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "banner_image_url" }?.value ?: "") }

    // Advertisement Settings
    var adContactEmail by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "ad_contact_email" }?.value ?: "safuanislam2276@gmail.com") }
    var adSponsorBrand by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "ad_sponsor_brand" }?.value ?: "ওয়ালটন / স্যামসাং") }
    var contactAdEnabled by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "contact_ad_enabled" }?.value != "false") }
    var contactAdBrand by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "contact_ad_brand" }?.value ?: "ওয়ালটন স্মার্ট অ্যাপ্লায়েন্স") }
    var contactAdTitle by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "contact_ad_title" }?.value ?: "সেরা হোম অ্যাপ্লায়েন্স ও ইলেকট্রনিক্স!") }
    var contactAdDesc by remember(adminSettings) { mutableStateOf(adminSettings.find { it.key == "contact_ad_desc" }?.value ?: "অনলাইন অর্ডারে পাচ্ছেন ১০% ক্যাশব্যাক ও দ্রুত হোম ডেলিভারি।") }

    var saveToast by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "মালিক অ্যাডমিন প্যানেল",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        if (isUnlocked) {
                            Text(
                                text = "safuanislam2276@gmail.com (Owner Master)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isUnlocked) {
                        IconButton(onClick = onLock) {
                            Icon(Icons.Default.Lock, contentDescription = "Lock Admin")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (!isUnlocked) {
            // Dedicated Owner Login Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "মালিক অ্যাডমিন পোর্টাল",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "শুধুমাত্র মালিকের জন্য সুরক্ষিত পোর্টাল। আপনার আসল ইমেইল (safuanislam2276@gmail.com) অথবা অ্যাডমিন পাসকোড দিয়ে প্রবেশ করুন।",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = emailOrCodeInput,
                    onValueChange = {
                        emailOrCodeInput = it
                        unlockError = null
                    },
                    label = { Text("ইমেইল বা গোপন কোড লিখুন") },
                    placeholder = { Text("safuanislam2276@gmail.com বা HK@9934#BOSS") },
                    leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_secret_code_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                if (unlockError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = unlockError!!,
                        color = ErrorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val ok = onUnlock(emailOrCodeInput)
                        if (!ok) {
                            unlockError = "ভুল ইমেইল বা পাসকোড! শুধুমাত্র অনুমোদিত মালিক অ্যাকাউন্ট প্রবেশ করতে পারবে।"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_unlock_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("লগইন ও পোর্টাল আনলক করুন", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Owner Shortcut Button
                OutlinedButton(
                    onClick = {
                        emailOrCodeInput = "safuanislam2276@gmail.com"
                        val ok = onUnlock("safuanislam2276@gmail.com")
                        if (!ok) {
                            unlockError = "অ্যাক্সেস প্রত্যাখ্যান করা হয়েছে।"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("safuanislam2276@gmail.com দিয়ে দ্রুত প্রবেশ", fontSize = 13.sp)
                }
            }
        } else {
            // Unlocked Admin Panel with Tabs
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Scrollable Navigation Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 16.dp
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("কালার ও থিম") },
                        icon = { Icon(Icons.Default.Palette, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("ব্যাংক ও পেমেন্ট") },
                        icon = { Icon(Icons.Default.AccountBalance, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("বিজ্ঞাপন ও ব্যানার") },
                        icon = { Icon(Icons.Default.Campaign, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("সাবস্ক্রিপশন (${pendingSubscriptions.size})") },
                        icon = { Icon(Icons.Default.Payment, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        text = { Text("কারিগর নিয়ন্ত্রণ ($totalWorkers)") },
                        icon = { Icon(Icons.Default.Engineering, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 5,
                        onClick = { selectedTab = 5 },
                        text = { Text("অ্যানালিটিক্স") },
                        icon = { Icon(Icons.Default.Analytics, contentDescription = null) }
                    )
                }

                if (saveToast != null) {
                    Surface(
                        color = EmeraldGreen,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = saveToast!!,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp)
                        )
                    }
                }

                when (selectedTab) {
                    0 -> AdminThemeColorTab(
                        currentHex = currentThemeHex,
                        themeInput = themeInput,
                        presetColors = presetColors,
                        onThemeInputChange = { themeInput = it },
                        onApplyTheme = { hex ->
                            onUpdateThemeColor(hex)
                            saveToast = "অ্যাপ থিম কালার সফলভাবে আপডেট হয়েছে!"
                        }
                    )
                    1 -> AdminBankAccountsTab(
                        bkash = bkashNum,
                        nagad = nagadNum,
                        rocket = rocketNum,
                        bdBankName = bdBankName,
                        bdAccName = bdAccName,
                        bdAccNum = bdAccNum,
                        bdBranch = bdBranch,
                        intlBankName = intlBankName,
                        intlBeneficiary = intlBeneficiary,
                        intlIban = intlIban,
                        intlSwift = intlSwift,
                        intlPaypal = intlPaypal,
                        intlCardNote = intlCardNote,
                        subPriceBd = subPriceBd,
                        subPriceIntl = subPriceIntl,
                        onBkashChange = { bkashNum = it },
                        onNagadChange = { nagadNum = it },
                        onRocketChange = { rocketNum = it },
                        onBdBankNameChange = { bdBankName = it },
                        onBdAccNameChange = { bdAccName = it },
                        onBdAccNumChange = { bdAccNum = it },
                        onBdBranchChange = { bdBranch = it },
                        onIntlBankNameChange = { intlBankName = it },
                        onIntlBeneficiaryChange = { intlBeneficiary = it },
                        onIntlIbanChange = { intlIban = it },
                        onIntlSwiftChange = { intlSwift = it },
                        onIntlPaypalChange = { intlPaypal = it },
                        onIntlCardNoteChange = { intlCardNote = it },
                        onSubPriceBdChange = { subPriceBd = it },
                        onSubPriceIntlChange = { subPriceIntl = it },
                        onSave = {
                            onUpdateSetting("bkash_number", bkashNum)
                            onUpdateSetting("nagad_number", nagadNum)
                            onUpdateSetting("rocket_number", rocketNum)
                            onUpdateSetting("bd_bank_name", bdBankName)
                            onUpdateSetting("bd_bank_account_name", bdAccName)
                            onUpdateSetting("bd_bank_account_number", bdAccNum)
                            onUpdateSetting("bd_bank_branch", bdBranch)
                            onUpdateSetting("intl_bank_name", intlBankName)
                            onUpdateSetting("intl_bank_beneficiary", intlBeneficiary)
                            onUpdateSetting("intl_bank_iban", intlIban)
                            onUpdateSetting("intl_bank_swift", intlSwift)
                            onUpdateSetting("intl_paypal_email", intlPaypal)
                            onUpdateSetting("intl_card_instructions", intlCardNote)
                            onUpdateSetting("sub_price_bd", subPriceBd)
                            onUpdateSetting("sub_price_intl", subPriceIntl)
                            saveToast = "সকল পেমেন্ট ও সাবস্ক্রিপশন মূল্য সংরক্ষিত হয়েছে!"
                        }
                    )
                    2 -> AdminBannerTab(
                        bannerActive = bannerActive,
                        titleBn = bannerTitleBn,
                        titleEn = bannerTitleEn,
                        noticeBn = bannerNoticeBn,
                        noticeEn = bannerNoticeEn,
                        imgUrl = bannerImgUrl,
                        adContactEmail = adContactEmail,
                        adSponsorBrand = adSponsorBrand,
                        contactAdEnabled = contactAdEnabled,
                        contactAdBrand = contactAdBrand,
                        contactAdTitle = contactAdTitle,
                        contactAdDesc = contactAdDesc,
                        onActiveToggle = { bannerActive = it },
                        onTitleBnChange = { bannerTitleBn = it },
                        onTitleEnChange = { bannerTitleEn = it },
                        onNoticeBnChange = { bannerNoticeBn = it },
                        onNoticeEnChange = { bannerNoticeEn = it },
                        onImgUrlChange = { bannerImgUrl = it },
                        onAdContactEmailChange = { adContactEmail = it },
                        onAdSponsorBrandChange = { adSponsorBrand = it },
                        onContactAdEnabledToggle = { contactAdEnabled = it },
                        onContactAdBrandChange = { contactAdBrand = it },
                        onContactAdTitleChange = { contactAdTitle = it },
                        onContactAdDescChange = { contactAdDesc = it },
                        onSave = {
                            onUpdateBanner(bannerActive, bannerTitleBn, bannerTitleEn, bannerNoticeBn, bannerNoticeEn, bannerImgUrl)
                            onUpdateSetting("ad_contact_email", adContactEmail)
                            onUpdateSetting("ad_sponsor_brand", adSponsorBrand)
                            onUpdateSetting("contact_ad_enabled", contactAdEnabled.toString())
                            onUpdateSetting("contact_ad_brand", contactAdBrand)
                            onUpdateSetting("contact_ad_title", contactAdTitle)
                            onUpdateSetting("contact_ad_desc", contactAdDesc)
                            saveToast = "সকল ব্যানার ও বিজ্ঞাপনের তথ্য সফলভাবে আপডেট হয়েছে!"
                        }
                    )
                    3 -> AdminSubscriptionsTab(
                        pendingSubscriptions = pendingSubscriptions,
                        onApprove = onApproveSubscription,
                        onReject = onRejectSubscription
                    )
                    4 -> AdminWorkersTab(
                        workers = allMistris,
                        onToggleBlock = onToggleBlock,
                        onToggleDirectPro = onToggleDirectPro
                    )
                    5 -> AdminAnalyticsTab(
                        totalWorkers = totalWorkers,
                        proWorkers = proWorkers,
                        blockedWorkers = blockedWorkers,
                        viewsByCountry = viewsByCountry,
                        signupsByCountry = signupsByCountry
                    )
                }
            }
        }
    }
}

@Composable
fun AdminThemeColorTab(
    currentHex: String,
    themeInput: String,
    presetColors: List<Pair<String, String>>,
    onThemeInputChange: (String) -> Unit,
    onApplyTheme: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.2.dp, Color(0xFFCBD5E1))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "অ্যাপের লাইভ কালার কাস্টমাইজেশন",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "এখানে যেকোনো কালার নির্বাচন করলে পুরো অ্যাপ্লিকেশনের বাটন, আইকন ও হেডার সাথে সাথে সেই কালারে পরিবর্তিত হবে।",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("জনপ্রিয় কালার প্যালেট নির্বাচন করুন:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))

                presetColors.forEach { (name, hex) ->
                    val isSelected = themeInput.equals(hex, ignoreCase = true)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onThemeInputChange(hex)
                                onApplyTheme(hex)
                            }
                            .padding(vertical = 6.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(parseHexColor(hex, BrandBlue))
                                    .border(1.dp, BorderLight, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                        if (isSelected) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("কাস্টম HEX কোড দিন:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = themeInput,
                        onValueChange = onThemeInputChange,
                        placeholder = { Text("#0284C7") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Button(
                        onClick = { onApplyTheme(themeInput) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("সেভ করুন")
                    }
                }
            }
        }
    }
}

@Composable
fun AdminBankAccountsTab(
    bkash: String,
    nagad: String,
    rocket: String,
    bdBankName: String,
    bdAccName: String,
    bdAccNum: String,
    bdBranch: String,
    intlBankName: String,
    intlBeneficiary: String,
    intlIban: String,
    intlSwift: String,
    intlPaypal: String,
    intlCardNote: String,
    subPriceBd: String,
    subPriceIntl: String,
    onBkashChange: (String) -> Unit,
    onNagadChange: (String) -> Unit,
    onRocketChange: (String) -> Unit,
    onBdBankNameChange: (String) -> Unit,
    onBdAccNameChange: (String) -> Unit,
    onBdAccNumChange: (String) -> Unit,
    onBdBranchChange: (String) -> Unit,
    onIntlBankNameChange: (String) -> Unit,
    onIntlBeneficiaryChange: (String) -> Unit,
    onIntlIbanChange: (String) -> Unit,
    onIntlSwiftChange: (String) -> Unit,
    onIntlPaypalChange: (String) -> Unit,
    onIntlCardNoteChange: (String) -> Unit,
    onSubPriceBdChange: (String) -> Unit,
    onSubPriceIntlChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bangladesh Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.2.dp, Color(0xFFCBD5E1))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🇧🇩", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("বাংলাদেশ পেমেন্ট গেটওয়ে ও সাবস্ক্রিপশন ফি", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Text("কারিগররা PRO প্যাকেজ কেনার সময় এই সাবস্ক্রিপশন মূল্য, নম্বর ও ব্যাংক একাউন্ট দেখতে পাবেন।", fontSize = 12.sp, color = TextSecondary)

                OutlinedTextField(
                    value = subPriceBd,
                    onValueChange = onSubPriceBdChange,
                    label = { Text("বাংলাদেশ মাসিক সাবস্ক্রিপশন ফি (টাকা) *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = bkash,
                    onValueChange = onBkashChange,
                    label = { Text("bKash নম্বর") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = nagad,
                    onValueChange = onNagadChange,
                    label = { Text("Nagad নম্বর") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = rocket,
                    onValueChange = onRocketChange,
                    label = { Text("Rocket নম্বর") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = bdBankName,
                    onValueChange = onBdBankNameChange,
                    label = { Text("ব্যাংক নাম") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = bdAccName,
                    onValueChange = onBdAccNameChange,
                    label = { Text("হিসাবের নাম (Account Name)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = bdAccNum,
                    onValueChange = onBdAccNumChange,
                    label = { Text("হিসাব নম্বর (Account Number)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = bdBranch,
                    onValueChange = onBdBranchChange,
                    label = { Text("শাখা ও রাউটিং (Branch & Routing)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }
        }

        // International Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.2.dp, Color(0xFFCBD5E1))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌍", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("আন্তর্জাতিক পেমেন্ট গেটওয়ে ও সাবস্ক্রিপশন ফি", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Text("অন্যান্য দেশের কারিগররা PRO সাবস্ক্রিপশন কেনার সময় এই মূল্য ও বিবরণ দেখতে পাবেন।", fontSize = 12.sp, color = TextSecondary)

                OutlinedTextField(
                    value = subPriceIntl,
                    onValueChange = onSubPriceIntlChange,
                    label = { Text("International Monthly Subscription Fee (USD) *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = intlBankName,
                    onValueChange = onIntlBankNameChange,
                    label = { Text("International Bank Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = intlBeneficiary,
                    onValueChange = onIntlBeneficiaryChange,
                    label = { Text("Beneficiary Account Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = intlIban,
                    onValueChange = onIntlIbanChange,
                    label = { Text("IBAN / Account Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = intlSwift,
                    onValueChange = onIntlSwiftChange,
                    label = { Text("SWIFT / BIC Code") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = intlPaypal,
                    onValueChange = onIntlPaypalChange,
                    label = { Text("PayPal / Wise Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = intlCardNote,
                    onValueChange = onIntlCardNoteChange,
                    label = { Text("Card Payment Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("সকল পেমেন্ট তথ্য সংরক্ষণ করুন", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdminBannerTab(
    bannerActive: Boolean,
    titleBn: String,
    titleEn: String,
    noticeBn: String,
    noticeEn: String,
    imgUrl: String,
    adContactEmail: String,
    adSponsorBrand: String,
    contactAdEnabled: Boolean,
    contactAdBrand: String,
    contactAdTitle: String,
    contactAdDesc: String,
    onActiveToggle: (Boolean) -> Unit,
    onTitleBnChange: (String) -> Unit,
    onTitleEnChange: (String) -> Unit,
    onNoticeBnChange: (String) -> Unit,
    onNoticeEnChange: (String) -> Unit,
    onImgUrlChange: (String) -> Unit,
    onAdContactEmailChange: (String) -> Unit,
    onAdSponsorBrandChange: (String) -> Unit,
    onContactAdEnabledToggle: (Boolean) -> Unit,
    onContactAdBrandChange: (String) -> Unit,
    onContactAdTitleChange: (String) -> Unit,
    onContactAdDescChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: Home Page Hero Sponsored Ad Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.2.dp, Color(0xFFCBD5E1))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("১. হোম পেজ স্পনসরড অ্যাড ব্যানার", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("অ্যাপের হোম পেজের শীর্ষে স্পনসরড ব্যানার ও বিজ্ঞাপন দিন বাটন দেখান", fontSize = 12.sp, color = TextSecondary)
                    }
                    Switch(
                        checked = bannerActive,
                        onCheckedChange = onActiveToggle
                    )
                }

                HorizontalDivider()

                OutlinedTextField(
                    value = adSponsorBrand,
                    onValueChange = onAdSponsorBrandChange,
                    label = { Text("স্পনসর কোম্পানি/ব্র্যান্ড নাম (যেমন: ওয়ালটন)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = adContactEmail,
                    onValueChange = onAdContactEmailChange,
                    label = { Text("বিজ্ঞাপন যোগাযোগের অফিসিয়াল ইমেইল *") },
                    placeholder = { Text("safuanislam2276@gmail.com") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = titleBn,
                    onValueChange = onTitleBnChange,
                    label = { Text("বিজ্ঞাপনের শিরোনাম (বাংলা)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = titleEn,
                    onValueChange = onTitleEnChange,
                    label = { Text("Title (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = noticeBn,
                    onValueChange = onNoticeBnChange,
                    label = { Text("বিজ্ঞাপনের অফার বিবরণ (বাংলা)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 3
                )
                OutlinedTextField(
                    value = noticeEn,
                    onValueChange = onNoticeEnChange,
                    label = { Text("Description (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 3
                )
                OutlinedTextField(
                    value = imgUrl,
                    onValueChange = onImgUrlChange,
                    label = { Text("কাস্টম ব্যানার ইমেজ লিংক (ঐচ্ছিক)") },
                    placeholder = { Text("https://example.com/banner.jpg") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }
        }

        // Section 2: 2-3 Second Interstitial Ad for Call / WhatsApp Contact
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.2.dp, Color(0xFFE5A93C).copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⭐", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("২. কল ও হোয়াটসঅ্যাপ ২-৩ সেকেন্ডের বিজ্ঞাপন", fontWeight = FontWeight.Bold, fontSize = 15.5.sp)
                        }
                        Text("গ্রাহক যখন মিস্ত্রিকে কল বা WhatsApp করবেন, তখন ২-৩ সেকেন্ডের পপআপ এড দেখানো হবে", fontSize = 11.5.sp, color = TextSecondary)
                    }
                    Switch(
                        checked = contactAdEnabled,
                        onCheckedChange = onContactAdEnabledToggle
                    )
                }

                HorizontalDivider()

                OutlinedTextField(
                    value = contactAdBrand,
                    onValueChange = onContactAdBrandChange,
                    label = { Text("বিজ্ঞাপনদাতা ব্র্যান্ড নাম (যেমন: ওয়ালটন স্মার্ট অ্যাপ্লায়েন্স)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = contactAdTitle,
                    onValueChange = onContactAdTitleChange,
                    label = { Text("বিজ্ঞাপনের হেডলাইন (যেমন: সেরা হোম অ্যাপ্লায়েন্স!)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = contactAdDesc,
                    onValueChange = onContactAdDescChange,
                    label = { Text("বিজ্ঞাপনের মেসেজ/অফার (যেমন: অনলাইন অর্ডারে পাচ্ছেন ১০% ক্যাশব্যাক)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 2
                )
            }
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("সকল ব্যানার ও বিজ্ঞাপন সেটিংস সেভ করুন", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdminSubscriptionsTab(
    pendingSubscriptions: List<SubscriptionEntity>,
    onApprove: (SubscriptionEntity) -> Unit,
    onReject: (String) -> Unit
) {
    if (pendingSubscriptions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("কোনো পেন্ডিং সাবস্ক্রিপশন নেই", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("নতুন কোনো কারিগর পেমেন্ট করলে এখানে অনুমোদন বা বাতিলের অপশন পাবেন।", fontSize = 12.sp, color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pendingSubscriptions) { sub ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "প্ল্যান: ${sub.planName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                text = sub.amount,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "মেথড: ${sub.paymentMethod} | স্ট্যাটাস: ${sub.status}", fontSize = 13.sp, color = TextPrimary)
                        Text(text = "TrxID: ${sub.transactionId}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { onApprove(sub) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("অনুমোদন (Approve)")
                            }
                            OutlinedButton(
                                onClick = { onReject(sub.id) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("বাতিল (Reject)")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminWorkersTab(
    workers: List<MistriEntity>,
    onToggleBlock: (MistriEntity) -> Unit,
    onToggleDirectPro: (MistriEntity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = remember(workers, searchQuery) {
        if (searchQuery.isBlank()) workers
        else workers.filter { it.name.contains(searchQuery, ignoreCase = true) || it.profession.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("নাম বা পেশা দিয়ে খুঁজুন...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { mistri ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MistriAvatar(name = mistri.name, imageUri = mistri.profileImageUri, size = 48, isPro = mistri.isPro)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = mistri.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    if (mistri.isPro) ProBadge()
                                }
                                Text(text = "${mistri.profession} • ${mistri.district}, ${mistri.country}", fontSize = 12.sp, color = TextSecondary)
                                Text(text = "কল ক্লিক: ${mistri.callClicks} | হোয়াটসঅ্যাপ: ${mistri.waClicks}", fontSize = 11.sp, color = TextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onToggleDirectPro(mistri) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (mistri.isPro) "PRO বন্ধ করুন" else "সরাসরি PRO দিন", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { onToggleBlock(mistri) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (mistri.isBlocked) EmeraldGreen else ErrorRed
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (mistri.isBlocked) "আনব্লক করুন" else "ব্লক করুন", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminAnalyticsTab(
    totalWorkers: Int,
    proWorkers: Int,
    blockedWorkers: Int,
    viewsByCountry: List<CountryStat>,
    signupsByCountry: List<CountryStat>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("মোট কারিগর", fontSize = 12.sp)
                    Text("$totalWorkers", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ProGoldLight)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("PRO মেম্বার", fontSize = 12.sp, color = ProGoldDark)
                    Text("$proWorkers", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ProGoldDark)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ErrorRedLight)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("ব্লক করা", fontSize = 12.sp, color = ErrorRed)
                    Text("$blockedWorkers", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                }
            }
        }

        // Country Views
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("দেশভিত্তিক প্রোফাইল ভিউ (Views by Country)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                if (viewsByCountry.isEmpty()) {
                    Text("এখনো কোনো ভিউ ডেটা নেই", fontSize = 12.sp, color = TextSecondary)
                } else {
                    viewsByCountry.forEach { stat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stat.country, fontSize = 13.sp)
                            Text("${stat.count} বার", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Country Signups
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("দেশভিত্তিক রেজিস্ট্রেশন (Signups by Country)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                if (signupsByCountry.isEmpty()) {
                    Text("এখনো কোনো রেজিস্ট্রেশন ডেটা নেই", fontSize = 12.sp, color = TextSecondary)
                } else {
                    signupsByCountry.forEach { stat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stat.country, fontSize = 13.sp)
                            Text("${stat.count} জন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
