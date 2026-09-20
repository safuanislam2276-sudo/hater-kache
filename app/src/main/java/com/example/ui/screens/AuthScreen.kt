package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.R
import com.example.data.country.CountryInfo
import com.example.data.country.CountryRepository
import com.example.ui.components.CountryPickerDialog
import com.example.ui.components.HelpAiAccountDialog
import com.example.ui.components.MultiCategorySelector
import com.example.ui.components.SearchableLocationPickerDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    initialCountry: CountryInfo,
    onCountryChanged: (CountryInfo) -> Unit = {},
    onBack: () -> Unit,
    onContinueAsGuest: () -> Unit = onBack,
    onLoginSubmit: (email: String, password: String?, onDone: () -> Unit, onError: (String) -> Unit) -> Unit,
    onGoogleAuth: (email: String, name: String, photoUrl: String?, country: String, countryCode: String, division: String, district: String, userType: String, onDone: () -> Unit, onError: (String) -> Unit) -> Unit,
    onRegisterCustomer: (
        name: String, email: String, phone: String, password: String?, country: String, countryCode: String,
        division: String, district: String, address: String, imageUri: String?,
        onDone: () -> Unit, onError: (String) -> Unit
    ) -> Unit,
    onRegisterWorker: (
        name: String, email: String, phone: String, password: String?, country: String, countryCode: String,
        division: String, district: String, address: String, profession: String, extraCategories: String,
        experienceYears: Int, bio: String, skills: String, imageUri: String?,
        onDone: () -> Unit, onError: (String) -> Unit
    ) -> Unit
) {
    var selectedUserType by remember { mutableStateOf("CUSTOMER") } // "CUSTOMER" or "MISTRI"
    var showHelpAiDialog by remember { mutableStateOf(false) }
    var showLegacyLoginDialog by remember { mutableStateOf(false) }

    // Common fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<String?>(null) }

    // Country & location
    var selectedCountry by remember { mutableStateOf(initialCountry) }
    var showCountryDialog by remember { mutableStateOf(false) }
    var selectedDivision by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }
    var showDivisionPickerDialog by remember { mutableStateOf(false) }
    var showDistrictPickerDialog by remember { mutableStateOf(false) }

    val isBangladesh = selectedCountry.code == "BD" ||
            selectedCountry.nameEn.equals("Bangladesh", ignoreCase = true) ||
            selectedCountry.nameBn.equals("বাংলাদেশ", ignoreCase = true)

    // Worker fields: multi-category support (up to 6 categories - starts empty per user request)
    var selectedCategories by remember {
        mutableStateOf(emptyList<String>())
    }
    var profession by remember {
        mutableStateOf("")
    }

    LaunchedEffect(isBangladesh) {
        if (profession.isNotBlank()) {
            profession = CountryRepository.getProfessionDisplayName(profession, isBangladesh)
        }
        selectedCategories = selectedCategories.map { CountryRepository.getProfessionDisplayName(it, isBangladesh) }
    }

    var experienceYearsText by remember { mutableStateOf("3") }
    var bio by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showGoogleDialog by remember { mutableStateOf(false) }

    // Photo picker (Android Photo Picker - Google Play policy compliant, zero-permission)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageUri = uri.toString()
        }
    }

    val divisionsMap = remember(selectedCountry, isBangladesh) {
        selectedCountry.getDivisions(isBangladesh)
    }
    val divisionList = remember(divisionsMap) { divisionsMap.keys.toList() }
    val districtList = remember(selectedDivision, divisionsMap) {
        if (selectedDivision.isNotEmpty() && divisionsMap.containsKey(selectedDivision)) {
            divisionsMap[selectedDivision] ?: emptyList()
        } else {
            divisionsMap.values.flatten().distinct()
        }
    }

    if (showCountryDialog) {
        CountryPickerDialog(
            selectedCountry = selectedCountry,
            isBangladesh = isBangladesh,
            onCountrySelected = { c ->
                selectedCountry = c
                selectedDivision = ""
                selectedDistrict = ""
                onCountryChanged(c)
            },
            onDismissRequest = { showCountryDialog = false }
        )
    }

    if (showGoogleDialog) {
        GoogleSignInDialog(
            isBangladesh = isBangladesh,
            onDismiss = { showGoogleDialog = false },
            onConfirm = { gEmail, gName ->
                showGoogleDialog = false
                isLoading = true
                errorMessage = null
                val div = if (selectedDivision.isNotBlank()) selectedDivision else selectedCountry.nameBn
                val dist = if (selectedDistrict.isNotBlank()) selectedDistrict else selectedCountry.nameBn
                onGoogleAuth(
                    gEmail,
                    gName,
                    null,
                    if (isBangladesh) selectedCountry.nameBn else selectedCountry.nameEn,
                    selectedCountry.phoneCode,
                    div,
                    dist,
                    selectedUserType,
                    {
                        isLoading = false
                        onBack()
                    },
                    { err ->
                        isLoading = false
                        errorMessage = err
                    }
                )
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isBangladesh) "একাউন্ট তৈরি করুন" else "Create Account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("auth_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Pre-login country selector - visible and accessible at top right!
                    Surface(
                        onClick = { showCountryDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("auth_country_selector_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = selectedCountry.flag, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBangladesh) selectedCountry.nameBn else selectedCountry.nameEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select country",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Logo (Luxury 3D Squircle Logo) & Firebase Indicator
            Surface(
                color = Color(0xFF070F1E),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(76.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFFDF00), Color(0xFFE5A93C), Color(0xFF00E5FF))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_hater_kache_logo),
                    contentDescription = "Hater Kache Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isBangladesh) "হাতের কাছে মিস্ত্রি" else "Hater Kache Mistri",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = if (isBangladesh) "বিশ্বস্ত কারিগর ও গ্রাহকের নির্ভরযোগ্য প্ল্যাটফর্ম" else "Trusted Platform for Workers & Customers",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Main Header: একাউন্ট তৈরি ও সংরক্ষণ with Help AI on the right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangladesh) "একাউন্ট তৈরি ও সংরক্ষণ" else "Create & Restore Account",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                // Help AI Button with Logo
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.5.dp, Color(0xFF2563EB)),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .clickable { showHelpAiDialog = true }
                        .testTag("auth_help_ai_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_help_ai),
                            contentDescription = "Help AI",
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isBangladesh) "হেল্প AI" else "Help AI",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("✨", fontSize = 10.sp)
                            }
                            Text(
                                text = if (isBangladesh) "সাহায্য নিন" else "Get Help",
                                fontSize = 9.sp,
                                color = Color(0xFF3B82F6),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
                // একাউন্ট তৈরি করুন এর নিচে গ্রাহক ও মিস্ত্রি সিলেকশন (স্পষ্ট বর্ডার সহ)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Customer Tab (গ্রাহক প্রোফাইল - স্পষ্ট বর্ডার সহ)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedUserType = "CUSTOMER"
                                errorMessage = null
                            }
                            .testTag("auth_customer_profile_tab"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedUserType == "CUSTOMER") BrandBlueLight else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            width = if (selectedUserType == "CUSTOMER") 2.5.dp else 1.8.dp,
                            color = if (selectedUserType == "CUSTOMER") BrandBlueDark else Color(0xFF64748B)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = if (selectedUserType == "CUSTOMER") BrandBlueDark else TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBangladesh) "গ্রাহক প্রোফাইল" else "Customer Profile",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = if (selectedUserType == "CUSTOMER") BrandBlueDark else TextPrimary
                            )
                            Text(
                                text = if (isBangladesh) "সেবা খুঁজছি" else "Looking for service",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Mistri / Worker Tab (মিস্ত্রি প্রোফাইল - স্পষ্ট বর্ডার সহ)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedUserType = "MISTRI"
                                errorMessage = null
                            }
                            .testTag("auth_mistri_profile_tab"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedUserType == "MISTRI") Color(0xFFFFF8E1) else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            width = if (selectedUserType == "MISTRI") 2.5.dp else 1.8.dp,
                            color = if (selectedUserType == "MISTRI") Color(0xFFE65100) else Color(0xFF64748B)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Handyman,
                                contentDescription = null,
                                tint = if (selectedUserType == "MISTRI") Color(0xFFE65100) else TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBangladesh) "মিস্ত্রি প্রোফাইল" else "Mistri Profile",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = if (selectedUserType == "MISTRI") Color(0xFFE65100) else TextPrimary
                            )
                            Text(
                                text = if (isBangladesh) "কাজ করতে চাই" else "Offer services",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image Selection Box
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .background(BrandBlueLight)
                            .border(
                                width = if (selectedUserType == "MISTRI" && profileImageUri == null) 2.dp else 1.dp,
                                color = if (selectedUserType == "MISTRI" && profileImageUri == null) Color(0xFFE65100) else BrandBlue,
                                shape = CircleShape
                            )
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = "Upload Photo",
                                    tint = BrandBlueDark,
                                    modifier = Modifier.size(26.dp)
                                )
                                Text(
                                    text = if (selectedUserType == "MISTRI") "ছবি *" else "ছবি (ঐচ্ছিক)",
                                    fontSize = 10.sp,
                                    color = BrandBlueDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (selectedUserType == "MISTRI") {
                            if (isBangladesh) "প্রোফাইল ছবি * (বাধ্যতামূলক)" else "Profile Photo * (Mandatory)"
                        } else {
                            if (isBangladesh) "প্রোফাইল ছবি (ঐচ্ছিক)" else "Profile Photo (Optional)"
                        },
                        fontSize = 11.sp,
                        fontWeight = if (selectedUserType == "MISTRI") FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedUserType == "MISTRI") Color(0xFFE65100) else TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                val activeBrandColor = if (selectedUserType == "MISTRI") Color(0xFFEA580C) else Color(0xFF2563EB)
                val inputFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = activeBrandColor,
                    unfocusedBorderColor = Color(0xFF1E293B), // High-contrast, solid dark slate border on all 4 sides so users clearly see where to type
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedLabelColor = activeBrandColor,
                    unfocusedLabelColor = Color(0xFF0F172A),
                    focusedLeadingIconColor = activeBrandColor,
                    unfocusedLeadingIconColor = Color(0xFF334155),
                    focusedTrailingIconColor = activeBrandColor,
                    unfocusedTrailingIconColor = Color(0xFF334155),
                    focusedPlaceholderColor = Color(0xFF64748B),
                    unfocusedPlaceholderColor = Color(0xFF94A3B8),
                    cursorColor = activeBrandColor
                )

                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isBangladesh) "পূর্ণ নাম *" else "Full Name *") },
                    placeholder = { Text(if (isBangladesh) "এখানে আপনার পূর্ণ নাম লিখুন" else "Enter your full name here") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("signup_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = inputFieldColors,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(if (isBangladesh) "ইমেইল অ্যাড্রেস *" else "Email Address *") },
                    placeholder = { Text(if (isBangladesh) "এখানে ইমেইল দিন (যেমন: name@gmail.com)" else "e.g. name@gmail.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("auth_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = inputFieldColors,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(if (isBangladesh) "পাসওয়ার্ড *" else "Password *") },
                    placeholder = { Text(if (isBangladesh) "এখানে অন্তত ৬ অক্ষরের পাসওয়ার্ড লিখুন" else "Enter password (min 6 chars)") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Password"
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = inputFieldColors,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Phone with country prefix
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = {
                        Text(
                            if (selectedUserType == "MISTRI") {
                                if (isBangladesh) "মোবাইল নম্বর * (গ্রাহক সরাসরি কল করবে)" else "Phone Number * (For Client Calls)"
                            } else {
                                if (isBangladesh) "মোবাইল নম্বর *" else "Phone Number *"
                            }
                        )
                    },
                    placeholder = { Text(if (isBangladesh) "এখানে মোবাইল নম্বর লিখুন (০১XXXXXXXXX)" else "Enter phone number here") },
                    prefix = { Text("${selectedCountry.phoneCode} ") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("signup_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = inputFieldColors,
                    singleLine = true
                )

                // Division & District Pickers with live Search box
                if (divisionsMap.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedCard(
                            onClick = { showDivisionPickerDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, Color(0xFF64748B)),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFF8FAFC))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = selectedCountry.getDivisionLabel(isBangladesh),
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = if (selectedDivision.isEmpty()) (if (isBangladesh) "বিভাগ বাছুন" else "Select") else selectedDivision,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        color = if (selectedDivision.isEmpty()) Color(0xFF64748B) else TextPrimary,
                                        fontWeight = if (selectedDivision.isEmpty()) FontWeight.Normal else FontWeight.Medium
                                    )
                                }
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF475569), modifier = Modifier.size(18.dp))
                            }
                        }

                        OutlinedCard(
                            onClick = {
                                showDistrictPickerDialog = true
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, Color(0xFF64748B)),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFF8FAFC))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = selectedCountry.getDistrictLabel(isBangladesh),
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = if (selectedDistrict.isEmpty()) (if (isBangladesh) "জেলা বাছুন" else "Select") else selectedDistrict,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        color = if (selectedDistrict.isEmpty()) Color(0xFF64748B) else TextPrimary,
                                        fontWeight = if (selectedDistrict.isEmpty()) FontWeight.Normal else FontWeight.Medium
                                    )
                                }
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF475569), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                // Upazila / Area Picker for districts
                val upazilaSuggestions = remember(selectedDistrict, isBangladesh) {
                    if (selectedDistrict.isNotBlank()) {
                        CountryRepository.getUpazilasForDistrict(selectedDistrict, isBangladesh)
                    } else emptyList()
                }

                if (selectedDistrict.isNotBlank() && upazilaSuggestions.isNotEmpty()) {
                    var upazilaExpanded by remember { mutableStateOf(false) }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedCard(
                            onClick = { upazilaExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, Color(0xFF1E293B)),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isBangladesh) "উপজেলা / থানা / এলাকা" else "Area / Sub-district",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = if (address.isEmpty()) (if (isBangladesh) "উপজেলা বা এলাকা নির্বাচন করুন" else "Select Area / Sub-district") else address,
                                        fontSize = 13.sp,
                                        color = if (address.isEmpty()) Color(0xFF64748B) else TextPrimary,
                                        fontWeight = if (address.isEmpty()) FontWeight.Normal else FontWeight.Medium
                                    )
                                }
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF1E293B))
                            }
                        }

                        DropdownMenu(
                            expanded = upazilaExpanded,
                            onDismissRequest = { upazilaExpanded = false }
                        ) {
                            upazilaSuggestions.forEach { upa ->
                                DropdownMenuItem(
                                    text = { Text(upa) },
                                    onClick = {
                                        address = upa
                                        upazilaExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Address field
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = {
                        Text(
                            if (selectedUserType == "CUSTOMER") {
                                if (isBangladesh) "বিস্তারিত ঠিকানা (বাড়ি, রোড - ঐচ্ছিক)" else "Detailed Address (Optional)"
                            } else {
                                if (isBangladesh) "কর্মস্থল / গ্যারেজ / দোকানের ঠিকানা" else "Workshop / Garage / Area Address"
                            }
                        )
                    },
                    placeholder = { Text(if (isBangladesh) "এখানে আপনার ঠিকানা লিখুন..." else "Enter address here...") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = inputFieldColors,
                    singleLine = true
                )

                // Worker Specific Fields (Profession, Experience, Bio, Skills)
                if (selectedUserType == "MISTRI") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isBangladesh) "কারিগর / মিস্ত্রি প্রোফাইল তথ্য" else "Worker Profile Information",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandBlueDark
                        )
                    }
                    Text(
                        text = if (isBangladesh) "(বাকি তথ্য পরে ড্যাশবোর্ড থেকেও এডিট করতে পারবেন)" else "(You can also edit this later in your dashboard)",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Multi-Category Selection for Mistri (Primary & Secondary professions)
                    MultiCategorySelector(
                        selectedCategories = selectedCategories,
                        onCategoriesChanged = { updated ->
                            selectedCategories = if (updated.isEmpty()) {
                                listOf(profession)
                            } else updated
                            profession = selectedCategories.first()
                        },
                        isBangladesh = isBangladesh,
                        maxCategories = 6
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = experienceYearsText,
                        onValueChange = { experienceYearsText = it.filter { char -> char.isDigit() } },
                        label = { Text(if (isBangladesh) "কাজের অভিজ্ঞতা (বছর) *" else "Experience (years) *") },
                        placeholder = { Text(if (isBangladesh) "এখানে অভিজ্ঞতার বছর লিখুন (যেমন: ৫)" else "e.g. 5") },
                        leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = inputFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = skills,
                        onValueChange = { skills = it },
                        label = { Text(if (isBangladesh) "কাজের ধরন / স্কিল ট্যাগসমূহ (ঐচ্ছিক)" else "Skills Tags (Optional)") },
                        placeholder = { Text(if (isBangladesh) "যেমন: ওয়্যারিং, সার্কিট, মোটর রিপেয়ার" else "Wiring, Circuit repair") },
                        leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = inputFieldColors
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text(if (isBangladesh) "নিজের সম্পর্কে ২-৩ লাইন (ঐচ্ছিক)" else "Short Bio / Description (Optional)") },
                        placeholder = { Text(if (isBangladesh) "এখানে আপনার কাজের বিবরণ ও অভিজ্ঞতা লিখুন..." else "Write short bio here...") },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = inputFieldColors,
                        maxLines = 3
                    )
                }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage!!,
                        color = ErrorRed,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (successMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = successMessage!!,
                        color = SuccessGreen,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Data Transparency Note
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF0D9488), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBangladesh) "তথ্য ব্যবহারের স্বচ্ছতা (Data Transparency)" else "Data Usage & Privacy",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F766E)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (isBangladesh)
                            "আপনার নাম, ফোন নম্বর ও এলাকা শুধুমাত্র গ্রাহক ও মিস্ত্রির মধ্যে সরাসরি যোগাযোগের জন্য ব্যবহৃত হয়। কোনো তথ্য অননুমোদিত তৃতীয় পক্ষের সাথে শেয়ার করা হয় না।"
                        else
                            "Your name, contact number, and location are solely used for direct communication between customers and workers. We never share your data with unauthorized third parties.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Action Button (Create Account)
            Button(
                onClick = {
                    errorMessage = null
                    if (name.isBlank()) {
                        errorMessage = if (isBangladesh) "দয়া করে পূর্ণ নাম লিখুন" else "Please enter full name"
                        return@Button
                    }
                    if (email.isBlank()) {
                        errorMessage = if (isBangladesh) "দয়া করে ইমেইল অ্যাড্রেস লিখুন" else "Please enter email"
                        return@Button
                    }
                    if (phone.isBlank()) {
                        errorMessage = if (isBangladesh) "দয়া করে মোবাইল নম্বর লিখুন" else "Please enter phone number"
                        return@Button
                    }
                    if (password.isNotBlank() && password.length < 6) {
                        errorMessage = if (isBangladesh) "পাসওয়ার্ড অন্তত ৬ অক্ষরের হতে হবে" else "Password must be at least 6 characters"
                        return@Button
                    }
                    if (selectedUserType == "MISTRI" && profileImageUri.isNullOrBlank()) {
                        errorMessage = if (isBangladesh) "মিস্ত্রি প্রোফাইলে ছবি বাধ্যতামূলক — অনুগ্রহ করে একটি ছবি নির্বাচন করুন" else "Profile photo is mandatory for workers"
                        return@Button
                    }
                    if (selectedUserType == "MISTRI" && selectedCategories.isEmpty()) {
                        errorMessage = if (isBangladesh) "অনুগ্রহ করে কাজের ক্যাটাগরি সিলেক্ট করুন" else "Please select at least one category"
                        return@Button
                    }

                    val div = if (selectedDivision.isNotBlank()) selectedDivision else selectedCountry.nameBn
                    val dist = if (selectedDistrict.isNotBlank()) selectedDistrict else selectedCountry.nameBn
                    isLoading = true

                    if (selectedUserType == "CUSTOMER") {
                        onRegisterCustomer(
                            name, email, phone, password.ifBlank { null },
                            if (isBangladesh) selectedCountry.nameBn else selectedCountry.nameEn,
                            selectedCountry.phoneCode,
                            div, dist, address, profileImageUri,
                            {
                                isLoading = false
                                onBack()
                            },
                            { err ->
                                isLoading = false
                                errorMessage = err
                            }
                        )
                    } else {
                        val exp = experienceYearsText.toIntOrNull() ?: 1
                        val primaryProf = selectedCategories.firstOrNull() ?: profession
                        val extraCats = selectedCategories.drop(1).joinToString(",")
                        onRegisterWorker(
                            name, email, phone, password.ifBlank { null },
                            if (isBangladesh) selectedCountry.nameBn else selectedCountry.nameEn,
                            selectedCountry.phoneCode,
                            div, dist, address, primaryProf, extraCats, exp, bio, skills, profileImageUri,
                            {
                                isLoading = false
                                onBack()
                            },
                            { err ->
                                isLoading = false
                                errorMessage = err
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("auth_submit_button"),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedUserType == "MISTRI") Color(0xFFE65100) else MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text(
                        text = if (selectedUserType == "CUSTOMER") {
                            if (isBangladesh) "গ্রাহক একাউন্ট তৈরি করুন" else "Create Customer Account"
                        } else {
                            if (isBangladesh) "মিস্ত্রি একাউন্ট তৈরি করুন" else "Create Mistri Account"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFCBD5E1))
                Text(
                    text = if (isBangladesh) " পুরাতন একাউন্ট ফিরে পেতে " else " To restore previous account ",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFCBD5E1))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bordered Google Sign-in Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { showGoogleDialog = true }
                    .testTag("auth_google_signup_button"),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.8.dp, Color(0xFF4285F4)),
                shadowElevation = 1.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4285F4),
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "G",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isBangladesh) "Google দিয়ে সাইন ইন করুন" else "Sign in with Google",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Direct Guest Entrance Button
            OutlinedButton(
                onClick = onContinueAsGuest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("continue_as_guest_button"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, Color(0xFFE5A93C)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0x14E5A93C)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isBangladesh) "অতিথি হিসেবে সরাসরি অ্যাপে প্রবেশ করুন ›" else "Browse Mistris as Guest ›",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
            }
        }
    }

    if (showDivisionPickerDialog) {
        SearchableLocationPickerDialog(
            title = if (isBangladesh) "বিভাগ নির্বাচন করুন" else "Select Division",
            items = divisionList,
            selectedItem = selectedDivision,
            searchPlaceholder = if (isBangladesh) "বিভাগ সার্চ করুন..." else "Search division...",
            onItemSelected = { chosen ->
                selectedDivision = chosen
                val districtsInDiv = divisionsMap[chosen] ?: emptyList()
                if (selectedDistrict.isNotEmpty() && !districtsInDiv.contains(selectedDistrict)) {
                    selectedDistrict = ""
                }
                showDivisionPickerDialog = false
            },
            onDismissRequest = { showDivisionPickerDialog = false }
        )
    }

    if (showDistrictPickerDialog) {
        val availableDistricts = if (selectedDivision.isNotEmpty() && divisionsMap.containsKey(selectedDivision)) {
            divisionsMap[selectedDivision] ?: divisionsMap.values.flatten().distinct()
        } else {
            divisionsMap.values.flatten().distinct()
        }
        SearchableLocationPickerDialog(
            title = if (isBangladesh) "জেলা নির্বাচন করুন" else "Select District",
            items = availableDistricts,
            selectedItem = selectedDistrict,
            searchPlaceholder = if (isBangladesh) "জেলা সার্চ করুন..." else "Search district...",
            onItemSelected = { chosen ->
                selectedDistrict = chosen
                // If division was not selected yet, find and set the division for this district
                if (selectedDivision.isEmpty()) {
                    val matchingDiv = divisionsMap.entries.firstOrNull { it.value.contains(chosen) }?.key
                    if (matchingDiv != null) {
                        selectedDivision = matchingDiv
                    }
                }
                showDistrictPickerDialog = false
            },
            onDismissRequest = { showDistrictPickerDialog = false }
        )
    }

    if (showLegacyLoginDialog) {
        var loginEmailInput by remember { mutableStateOf("") }
        var loginPasswordInput by remember { mutableStateOf("") }
        var loginDialogError by remember { mutableStateOf<String?>(null) }
        var isLoggingIn by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showLegacyLoginDialog = false },
            title = {
                Text(
                    text = if (isBangladesh) "ইমেইল ও পাসওয়ার্ড দিয়ে লগইন" else "Login with Email & Password",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (loginDialogError != null) {
                        Text(loginDialogError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                    OutlinedTextField(
                        value = loginEmailInput,
                        onValueChange = { loginEmailInput = it; loginDialogError = null },
                        label = { Text(if (isBangladesh) "ইমেইল" else "Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = loginPasswordInput,
                        onValueChange = { loginPasswordInput = it; loginDialogError = null },
                        label = { Text(if (isBangladesh) "পাসওয়ার্ড" else "Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (loginEmailInput.isBlank() || loginPasswordInput.isBlank()) {
                            loginDialogError = if (isBangladesh) "সব তথ্য পূরণ করুন" else "Please fill all fields"
                            return@Button
                        }
                        isLoggingIn = true
                        onLoginSubmit(
                            loginEmailInput.trim(),
                            loginPasswordInput.trim(),
                            {
                                isLoggingIn = false
                                showLegacyLoginDialog = false
                                onBack()
                            },
                            { err ->
                                isLoggingIn = false
                                loginDialogError = err
                            }
                        )
                    },
                    enabled = !isLoggingIn
                ) {
                    if (isLoggingIn) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                    } else {
                        Text(if (isBangladesh) "লগইন করুন" else "Login")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showLegacyLoginDialog = false }) {
                    Text(if (isBangladesh) "বাতিল" else "Cancel")
                }
            }
        )
    }

    if (showHelpAiDialog) {
        HelpAiAccountDialog(
            isBangladesh = isBangladesh,
            onDismiss = { showHelpAiDialog = false }
        )
    }
}

@Composable
fun GoogleSignInDialog(
    isBangladesh: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (email: String, name: String) -> Unit
) {
    var googleEmail by remember { mutableStateOf("") }
    var googleName by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFF4285F4)),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "G",
                            fontWeight = FontWeight.Black,
                            fontSize = 26.sp,
                            color = Color(0xFF4285F4)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isBangladesh) "Google অ্যাকাউন্ট দিয়ে সাইন-ইন" else "Sign in with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = if (isBangladesh) "আপনার Gmail অ্যাড্রেস ও নাম প্রদান করুন" else "Enter your Gmail address to authenticate",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                val dialogFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlueDark,
                    unfocusedBorderColor = Color(0xFF64748B),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedLabelColor = BrandBlueDark,
                    unfocusedLabelColor = Color(0xFF334155),
                    focusedLeadingIconColor = BrandBlueDark,
                    unfocusedLeadingIconColor = Color(0xFF475569)
                )

                OutlinedTextField(
                    value = googleEmail,
                    onValueChange = { googleEmail = it },
                    label = { Text("Gmail Address *") },
                    placeholder = { Text(if (isBangladesh) "এখানে Gmail ইমেইল লিখুন" else "example@gmail.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = dialogFieldColors
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = googleName,
                    onValueChange = { googleName = it },
                    label = { Text(if (isBangladesh) "আপনার নাম (ঐচ্ছিক)" else "Name (Optional)") },
                    placeholder = { Text(if (isBangladesh) "এখানে নাম লিখুন" else "Enter your name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = dialogFieldColors
                )

                if (inputError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = inputError!!, color = ErrorRed, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isBangladesh) "বাতিল" else "Cancel")
                    }

                    Button(
                        onClick = {
                            if (googleEmail.isBlank() || !googleEmail.contains("@")) {
                                inputError = if (isBangladesh) "সঠিক Gmail অ্যাড্রেস লিখুন" else "Enter a valid Gmail"
                                return@Button
                            }
                            onConfirm(googleEmail.trim(), googleName.trim())
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                    ) {
                        Text(if (isBangladesh) "চালিয়ে যান" else "Continue", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
