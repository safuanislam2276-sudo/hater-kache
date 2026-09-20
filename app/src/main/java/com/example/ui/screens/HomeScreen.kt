package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.country.CountryInfo
import com.example.data.country.CountryRepository
import com.example.data.model.MistriEntity
import com.example.data.model.UserEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.util.Localization
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.SearchFilterState
import com.example.ui.viewmodel.BannerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentGlobalCountry: CountryInfo,
    searchFilters: SearchFilterState,
    filteredMistris: List<MistriEntity>,
    currentUser: UserEntity?,
    liveWorkersCount: Int,
    liveCustomersCount: Int,
    liveReviewsCount: Int,
    bannerState: BannerState = BannerState(),
    onCountryChange: (CountryInfo) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchFilterApply: (country: String, division: String, district: String, profession: String) -> Unit,
    onResetSearchFilters: () -> Unit,
    onSelectMistri: (MistriEntity) -> Unit,
    onCallClick: (MistriEntity) -> Unit,
    onWaClick: (MistriEntity) -> Unit,
    onChatClick: (MistriEntity) -> Unit = {},
    unreadChatCount: Int = 0,
    onNavigateToMessenger: () -> Unit = {},
    onNavigateToAuth: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToAdmin: () -> Unit = {},
    onLogout: () -> Unit,
    onUpdateCustomerProfile: (name: String, phone: String, division: String, district: String, address: String, imageUri: String?, onDone: () -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _, _, _, _, d, _ -> d() }
) {
    var showCountryDialog by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showUserMenu by remember { mutableStateOf(false) }
    var showCustomerProfileDialog by remember { mutableStateOf(false) }
    var showAdvertiseDialog by remember { mutableStateOf(false) }
    var pendingContactAd by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    val handleCallMistri = { mistri: MistriEntity ->
        if (bannerState.contactAdEnabled) {
            pendingContactAd = Pair("CALL") { onCallClick(mistri) }
        } else {
            onCallClick(mistri)
        }
    }

    val handleWaMistri = { mistri: MistriEntity ->
        if (bannerState.contactAdEnabled) {
            pendingContactAd = Pair("WHATSAPP") { onWaClick(mistri) }
        } else {
            onWaClick(mistri)
        }
    }

    val isBangladesh = currentGlobalCountry.code == "BD" ||
            currentGlobalCountry.nameEn.equals("Bangladesh", ignoreCase = true) ||
            currentGlobalCountry.nameBn.equals("বাংলাদেশ", ignoreCase = true)

    val strings = Localization.getStrings(isBangladesh)

    val isFilterActive = remember(searchFilters) {
        (searchFilters.country.isNotEmpty() && searchFilters.country != "সকল দেশ" && searchFilters.country != "All Countries") ||
                (searchFilters.division != "সকল" && searchFilters.division != "All" && searchFilters.division.isNotEmpty()) ||
                (searchFilters.district != "সকল" && searchFilters.district != "All" && searchFilters.district.isNotEmpty()) ||
                (searchFilters.profession != "সকল পেশা" && searchFilters.profession != "All Professions" && searchFilters.profession.isNotEmpty())
    }

    if (showCountryDialog) {
        CountryPickerDialog(
            selectedCountry = currentGlobalCountry,
            isBangladesh = isBangladesh,
            onCountrySelected = { newCountry ->
                onCountryChange(newCountry)
            },
            onDismissRequest = { showCountryDialog = false }
        )
    }

    if (showFilterSheet) {
        IndependentSearchFilterSheet(
            currentCountry = searchFilters.country,
            currentDivision = searchFilters.division,
            currentDistrict = searchFilters.district,
            currentProfession = searchFilters.profession,
            isBangladesh = isBangladesh,
            onApplyFilter = onSearchFilterApply,
            onReset = onResetSearchFilters,
            onDismissRequest = { showFilterSheet = false }
        )
    }

    if (showCustomerProfileDialog && currentUser != null) {
        CustomerProfileDialog(
            user = currentUser,
            isBangladesh = isBangladesh,
            onDismiss = { showCustomerProfileDialog = false },
            onSaveProfile = onUpdateCustomerProfile
        )
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    // Top App Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Brand Info (Tapping opens Login/Profile, Triple-tap secretly opens Owner Admin Portal)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    if (currentUser == null) {
                                        onNavigateToAuth()
                                    } else if (currentUser.userType == "MISTRI") {
                                        onNavigateToDashboard()
                                    } else {
                                        showCustomerProfileDialog = true
                                    }
                                }
                                .testTag("top_logo_brand_button")
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF070F1E),
                                shadowElevation = 3.dp,
                                modifier = Modifier
                                    .size(42.dp)
                                    .border(
                                        width = 1.5.dp,
                                        brush = Brush.linearGradient(
                                            listOf(Color(0xFFFFDF00), Color(0xFFE5A93C), Color(0xFF00E5FF))
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_hater_kache_logo),
                                    contentDescription = "Hater Kache Logo",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = strings.appName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "TRUST • WORK • SERVICE",
                                    fontSize = 10.sp,
                                    color = Color(0xFFD97706),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        // Right actions: Country selector & User Profile Menu
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Country Chip
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier
                                    .clickable { showCountryDialog = true }
                                    .testTag("top_country_selector_chip")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = currentGlobalCountry.flag, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isBangladesh) currentGlobalCountry.nameBn else currentGlobalCountry.nameEn,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            // Messenger Icon Button (Direct entry to Facebook-style Messenger inbox)
                            Box {
                                IconButton(
                                    onClick = onNavigateToMessenger,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("top_messenger_button")
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF0084FF),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.Forum,
                                                contentDescription = "Messenger",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                                if (unreadChatCount > 0) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFFEF4444),
                                        modifier = Modifier
                                            .size(16.dp)
                                            .align(Alignment.TopEnd)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$unreadChatCount",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // User Profile / Menu Button (with live unread badge)
                            Box {
                                IconButton(
                                    onClick = {
                                        showUserMenu = true
                                    },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("user_profile_button")
                                ) {
                                    if (currentUser != null) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = currentUser.name.take(1),
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = "User Menu",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                                if (unreadChatCount > 0) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFFEF4444),
                                        modifier = Modifier
                                            .size(16.dp)
                                            .align(Alignment.TopEnd)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$unreadChatCount",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                DropdownMenu(
                                    expanded = showUserMenu,
                                    onDismissRequest = { showUserMenu = false }
                                ) {
                                    if (currentUser != null) {
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(currentUser.name, fontWeight = FontWeight.Bold)
                                                    Text(
                                                        if (currentUser.userType == "MISTRI") {
                                                            if (isBangladesh) "কারিগর অ্যাকাউন্ট" else "Mistri / Worker Account"
                                                        } else {
                                                            if (isBangladesh) "গ্রাহক অ্যাকাউন্ট" else "Customer Account"
                                                        },
                                                        fontSize = 11.sp,
                                                        color = TextSecondary
                                                    )
                                                }
                                            },
                                            onClick = {}
                                        )
                                        HorizontalDivider()
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(if (isBangladesh) "মেসেঞ্জার চ্যাট বক্স" else "Messenger Chat")
                                                    if (unreadChatCount > 0) {
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Surface(
                                                            shape = CircleShape,
                                                            color = Color(0xFF0084FF),
                                                            modifier = Modifier.size(16.dp)
                                                        ) {
                                                            Box(contentAlignment = Alignment.Center) {
                                                                Text("$unreadChatCount", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            leadingIcon = { Icon(Icons.Default.Forum, contentDescription = null, tint = Color(0xFF0084FF)) },
                                            onClick = {
                                                showUserMenu = false
                                                onNavigateToMessenger()
                                            }
                                        )
                                        if (currentUser.userType == "CUSTOMER") {
                                            DropdownMenuItem(
                                                text = { Text(if (isBangladesh) "আমার প্রোফাইল ও এডিট" else "My Profile & Edit") },
                                                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = BrandBlueDark) },
                                                onClick = {
                                                    showUserMenu = false
                                                    showCustomerProfileDialog = true
                                                }
                                            )
                                        }
                                        if (currentUser.userType == "MISTRI") {
                                            DropdownMenuItem(
                                                text = { Text(strings.dashboardButton) },
                                                leadingIcon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                                                onClick = {
                                                    showUserMenu = false
                                                    onNavigateToDashboard()
                                                }
                                            )
                                        }
                                        DropdownMenuItem(
                                            text = { Text(strings.logoutButton) },
                                            leadingIcon = { Icon(Icons.Default.Logout, contentDescription = null) },
                                            onClick = {
                                                showUserMenu = false
                                                onLogout()
                                            }
                                        )
                                    } else {
                                        DropdownMenuItem(
                                            text = { Text(strings.loginButton) },
                                            leadingIcon = { Icon(Icons.Default.Login, contentDescription = null) },
                                            onClick = {
                                                showUserMenu = false
                                                onNavigateToAuth()
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(if (isBangladesh) "মেসেঞ্জার চ্যাট বক্স" else "Messenger Chat") },
                                            leadingIcon = { Icon(Icons.Default.Forum, contentDescription = null, tint = Color(0xFF0084FF)) },
                                            onClick = {
                                                showUserMenu = false
                                                onNavigateToMessenger()
                                            }
                                        )
                                        HorizontalDivider()
                                        DropdownMenuItem(
                                            text = { Text(if (isBangladesh) "মালিক লগইন (Owner Portal)" else "Owner Admin Login") },
                                            leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null) },
                                            onClick = {
                                                showUserMenu = false
                                                onNavigateToAdmin()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Live Real-Time Platform Statistics
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCounterItem(
                            title = strings.workersCountLabel,
                            count = liveWorkersCount,
                            icon = Icons.Default.Build,
                            iconColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        StatCounterItem(
                            title = strings.customersCountLabel,
                            count = liveCustomersCount,
                            icon = Icons.Default.People,
                            iconColor = EmeraldGreen,
                            modifier = Modifier.weight(1f)
                        )
                        StatCounterItem(
                            title = strings.reviewsCountLabel,
                            count = liveReviewsCount,
                            icon = Icons.Default.Star,
                            iconColor = ProGold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // App Announcement / Offer Banner (Admin Controlled)
            if (bannerState.active) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .testTag("home_hero_banner_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                            if (bannerState.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = bannerState.imageUrl,
                                    contentDescription = "Ad Banner",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.hero_banner),
                                    contentDescription = "Hero Banner",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Gradient Overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                                            listOf(
                                                NavyDark.copy(alpha = 0.92f),
                                                NavyDark.copy(alpha = 0.70f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ProGold
                                    ) {
                                        Text(
                                            text = if (bannerState.sponsorBrand.isNotBlank()) {
                                                "📢 ${bannerState.sponsorBrand}"
                                            } else {
                                                if (isBangladesh) "অফার ও আপডেট" else "Special Update"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White.copy(alpha = 0.22f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                                        modifier = Modifier.clickable { showAdvertiseDialog = true }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Campaign,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isBangladesh) "বিজ্ঞাপন দিন" else "Advertise",
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                Column {
                                    Text(
                                        text = if (isBangladesh) bannerState.titleBn else bannerState.titleEn,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isBangladesh) bannerState.noticeBn else bannerState.noticeEn,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Dedicated Attractive "বিজ্ঞাপন দিন" (Advertise With Us) Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { showAdvertiseDialog = true }
                        .testTag("home_advertise_cta_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF86EFAC)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Campaign,
                                        contentDescription = null,
                                        tint = Color(0xFF16A34A),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBangladesh) "আপনার ব্যবসার বিজ্ঞাপন প্রচার করুন" else "Advertise Your Business",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF14532D)
                                )
                                Text(
                                    text = if (isBangladesh) "হাজারো সক্রিয় গ্রাহক ও টেকনিশিয়ানের কাছে পৌঁছান" else "Reach thousands of customers & technicians",
                                    fontSize = 11.sp,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF16A34A)
                        ) {
                            Text(
                                text = if (isBangladesh) "বিজ্ঞাপন দিন ›" else "Advertise ›",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // Search Bar & Filter Trigger
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchFilters.query,
                            onValueChange = onSearchQueryChange,
                            placeholder = { Text(strings.searchPlaceholder) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingIcon = {
                                if (searchFilters.query.isNotEmpty()) {
                                    IconButton(onClick = { onSearchQueryChange("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_search_bar_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedBorderColor = Color(0xFF94A3B8),
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        // Filter Button
                        Box {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF94A3B8)),
                                color = Color.Transparent
                            ) {
                                FilledTonalIconButton(
                                    onClick = { showFilterSheet = true },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .size(52.dp)
                                        .testTag("open_filter_sheet_button"),
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = if (isFilterActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Filters",
                                        tint = if (isFilterActive) MaterialTheme.colorScheme.primary else TextPrimary
                                    )
                                }
                            }
                            if (isFilterActive) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(ProGold)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }

                    // Independent Location Active Filter Notice
                    if (searchFilters.country != currentGlobalCountry.nameBn && searchFilters.country.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.LocationSearching, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${if (isBangladesh) "সার্চ লোকেশন:" else "Search Location:"} ${if (searchFilters.district != "সকল" && searchFilters.district != "All") searchFilters.district + ", " else ""}${searchFilters.country}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear Location Filter",
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onResetSearchFilters() },
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Profession Category Chips (Bilingual)
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CountryRepository.ALL_PROFESSION_ITEMS) { profItem ->
                        val isSelected = if (profItem.id == "all") {
                            searchFilters.profession.isEmpty() || searchFilters.profession == "সকল পেশা" || searchFilters.profession == "All Professions"
                        } else {
                            searchFilters.profession == profItem.nameBn || searchFilters.profession == profItem.nameEn ||
                            (profItem.id == "electrician" && (
                                searchFilters.profession.contains("ইলেকট্রিক") ||
                                searchFilters.profession.contains("বিদ্যুৎ") ||
                                searchFilters.profession.contains("বৈদ্যুতিক") ||
                                searchFilters.profession.contains("ইলেকট্রিশিয়ান") ||
                                searchFilters.profession.contains("electric", ignoreCase = true)
                            ))
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val nextProf = if (isSelected) {
                                    if (isBangladesh) "সকল পেশা" else "All Professions"
                                } else {
                                    if (isBangladesh) profItem.nameBn else profItem.nameEn
                                }
                                onSearchFilterApply(searchFilters.country, searchFilters.division, searchFilters.district, nextProf)
                            },
                            label = {
                                Text(
                                    text = if (isBangladesh) profItem.nameBn else profItem.nameEn,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF94A3B8),
                                borderWidth = 1.2.dp
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // PRO First Ranking Info Notice
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = ProGoldLight.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ProGold.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⭐", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangladesh) "PRO মিস্ত্রিরা লিস্টের শীর্ষে • রেটিং ও অভিজ্ঞতা অনুযায়ী সাজানো"
                            else "PRO Technicians at the top • Sorted by rating & experience",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ProGoldDark
                        )
                    }
                }
            }

            // Nearby Location Prioritization Banner
            if (currentUser?.district?.isNotBlank() == true) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF0FDF4),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.NearMe, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBangladesh) "📍 আপনার এলাকা: ${currentUser.district} • আশেপাশের মিস্ত্রিদের তালিকায় অগ্রাধিকার দেওয়া হয়েছে"
                                else "📍 Your location: ${currentUser.district} • Nearby technicians prioritized",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF166534)
                            )
                        }
                    }
                }
            }

            // List of Mistris
            if (filteredMistris.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isBangladesh) "কোনো মিস্ত্রি পাওয়া যায়নি" else "No technician found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isBangladesh) "অনুগ্রহ করে অন্য দেশ, জেলা বা পেশা নির্বাচন করে চেষ্টা করুন"
                                else "Please try selecting another country, district, or category",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(onClick = onResetSearchFilters) {
                                Text(if (isBangladesh) "ফিল্টার রিসেট করুন" else "Reset Filter")
                            }
                        }
                    }
                }
            } else {
                items(filteredMistris, key = { it.id }) { mistri ->
                    MistriCard(
                        mistri = mistri,
                        isBangladesh = isBangladesh,
                        callLabel = strings.callButton,
                        waLabel = strings.waButton,
                        onClick = { onSelectMistri(mistri) },
                        onCallClick = { handleCallMistri(mistri) },
                        onWaClick = { handleWaMistri(mistri) },
                        onChatClick = { onChatClick(mistri) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }

    if (showCustomerProfileDialog && currentUser != null) {
        CustomerProfileEditDialog(
            user = currentUser,
            isBangladesh = isBangladesh,
            onDismiss = { showCustomerProfileDialog = false },
            onOpenMessenger = {
                showCustomerProfileDialog = false
                onNavigateToMessenger()
            },
            onUpdate = { name, phone, div, dist, addr, img, onDone, onErr ->
                onUpdateCustomerProfile(name, phone, div, dist, addr, img, onDone, onErr)
            }
        )
    }

    if (showAdvertiseDialog) {
        AdvertiseContactDialog(
            contactEmail = bannerState.adContactEmail,
            sponsorBrand = bannerState.sponsorBrand,
            isBangladesh = isBangladesh,
            onDismiss = { showAdvertiseDialog = false }
        )
    }

    if (pendingContactAd != null) {
        ContactAdDialog(
            sponsorBrand = bannerState.contactAdBrand,
            title = bannerState.contactAdTitle,
            description = bannerState.contactAdDesc,
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

@Composable
fun MistriCard(
    mistri: MistriEntity,
    isBangladesh: Boolean,
    callLabel: String,
    waLabel: String,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    onWaClick: () -> Unit,
    onChatClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("mistri_card_${mistri.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (mistri.isPro) 3.dp else 1.5.dp),
        border = if (mistri.isPro) androidx.compose.foundation.BorderStroke(2.dp, ProGold) else androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                MistriAvatar(
                    name = mistri.name,
                    imageUri = mistri.profileImageUri,
                    size = 62,
                    isPro = mistri.isPro
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mistri.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (mistri.isPro) {
                            ProBadge(tier = mistri.proTier)
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = CountryRepository.getProfessionDisplayName(mistri.profession, isBangladesh),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                            Text(
                                text = "${mistri.district}, ${mistri.division}",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                maxLines = 1
                            )
                        }
                        Text("•", color = TextMuted)
                        Text(
                            text = if (isBangladesh) "${mistri.experienceYears} বছর অভিজ্ঞতা" else "${mistri.experienceYears} yrs experience",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        RatingStarsBar(rating = mistri.averageRating, totalReviews = mistri.totalReviews)
                        if (mistri.isVerified) {
                            VerifiedBadge()
                        }
                    }
                }
            }

            if (mistri.bio.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mistri.bio,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(10.dp))

            // Contact Buttons Row with real click counts + Messenger Chat button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0084FF).copy(alpha = 0.5f)),
                    modifier = Modifier
                        .weight(0.95f)
                        .height(42.dp)
                        .clickable(onClick = onChatClick)
                        .testTag("mistri_card_chat_${mistri.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Forum, contentDescription = null, tint = Color(0xFF0084FF), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBangladesh) "মেসেজ" else "Chat",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0084FF)
                        )
                    }
                }

                Box(modifier = Modifier.weight(2.05f)) {
                    CallWhatsAppButtons(
                        onCallClick = onCallClick,
                        onWaClick = onWaClick,
                        callClicks = mistri.callClicks,
                        waClicks = mistri.waClicks,
                        callLabel = callLabel,
                        waLabel = waLabel
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerProfileEditDialog(
    user: UserEntity,
    isBangladesh: Boolean,
    onDismiss: () -> Unit,
    onOpenMessenger: () -> Unit = {},
    onUpdate: (name: String, phone: String, division: String, district: String, address: String, imageUri: String?, onDone: () -> Unit, onError: (String) -> Unit) -> Unit
) {
    var editName by remember { mutableStateOf(user.name) }
    var editPhone by remember { mutableStateOf(user.phone) }
    var editAddress by remember { mutableStateOf(user.address) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = BorderStroke(2.dp, BrandBlueDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BrandBlueLight,
                    border = BorderStroke(1.5.dp, BrandBlueDark)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlueDark, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBangladesh) "গ্রাহক প্রোফাইল" else "Customer Profile",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BrandBlueDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar
                Surface(
                    shape = CircleShape,
                    color = BrandBlueDark,
                    border = BorderStroke(2.5.dp, Color(0xFF93C5FD)),
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = editName.take(1).uppercase().ifBlank { "C" },
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user.email,
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text(if (isBangladesh) "আপনার নাম" else "Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = editPhone,
                    onValueChange = { editPhone = it },
                    label = { Text(if (isBangladesh) "মোবাইল নম্বর" else "Phone Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = editAddress,
                    onValueChange = { editAddress = it },
                    label = { Text(if (isBangladesh) "ঠিকানা / এলাকা" else "Address / Area") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMsg ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Direct Messenger Chat Button inside Profile
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onOpenMessenger()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("profile_open_messenger_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.2.dp, Color(0xFF0084FF))
                ) {
                    Icon(Icons.Default.Forum, contentDescription = null, tint = Color(0xFF0084FF), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBangladesh) "আমার মেসেঞ্জার চ্যাট বক্স" else "My Messenger Chat",
                        color = Color(0xFF0084FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF94A3B8))
                    ) {
                        Text(if (isBangladesh) "বাতিল" else "Cancel")
                    }

                    Button(
                        onClick = {
                            if (editName.isBlank()) {
                                errorMsg = if (isBangladesh) "নাম আবশ্যক" else "Name is required"
                                return@Button
                            }
                            isSubmitting = true
                            onUpdate(
                                editName.trim(),
                                editPhone.trim(),
                                user.division,
                                user.district,
                                editAddress.trim(),
                                user.profileImageUri,
                                {
                                    isSubmitting = false
                                    onDismiss()
                                },
                                { err ->
                                    isSubmitting = false
                                    errorMsg = err
                                }
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlueDark),
                        enabled = !isSubmitting
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                        } else {
                            Text(if (isBangladesh) "সংরক্ষণ" else "Save")
                        }
                    }
                }
            }
        }
    }
}

