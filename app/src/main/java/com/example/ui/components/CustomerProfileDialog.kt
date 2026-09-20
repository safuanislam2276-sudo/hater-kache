package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.country.CountryRepository
import com.example.data.model.UserEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileDialog(
    user: UserEntity,
    isBangladesh: Boolean,
    onDismiss: () -> Unit,
    onSaveProfile: (
        name: String,
        phone: String,
        division: String,
        district: String,
        address: String,
        imageUri: String?,
        onDone: () -> Unit,
        onError: (String) -> Unit
    ) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(user.name) }
    var editPhone by remember { mutableStateOf(user.phone) }
    var editDivision by remember { mutableStateOf(user.division) }
    var editDistrict by remember { mutableStateOf(user.district) }
    var editAddress by remember { mutableStateOf(user.address) }
    var editImageUri by remember { mutableStateOf(user.profileImageUri) }

    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var divisionExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }

    val countryInfo = remember(user.country, user.countryCode) {
        CountryRepository.ALL_COUNTRIES.find {
            it.code.equals(user.countryCode, ignoreCase = true) ||
                    it.nameBn.equals(user.country, ignoreCase = true) ||
                    it.nameEn.equals(user.country, ignoreCase = true)
        } ?: CountryRepository.PRIMARY_COUNTRIES.first()
    }

    val isCountryBd = countryInfo.code == "BD" || countryInfo.nameEn.equals("Bangladesh", ignoreCase = true)
    val showBn = isBangladesh && isCountryBd
    val divisionsMap = remember(countryInfo, showBn) {
        countryInfo.getDivisions(showBn)
    }
    val divisionList = remember(divisionsMap) { divisionsMap.keys.toList() }
    val districtList = remember(editDivision, divisionsMap) {
        if (editDivision.isNotEmpty() && divisionsMap.containsKey(editDivision)) {
            divisionsMap[editDivision] ?: emptyList()
        } else {
            divisionsMap.values.flatten().distinct()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            editImageUri = uri.toString()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFCBD5E1)),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBangladesh) "আমার গ্রাহক প্রোফাইল" else "Customer Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Photo Avatar
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(BrandBlueLight)
                            .border(2.dp, BrandBlue, CircleShape)
                            .clickable(enabled = isEditing) {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val currentUri = if (isEditing) editImageUri else user.profileImageUri
                        if (!currentUri.isNullOrBlank()) {
                            AsyncImage(
                                model = currentUri,
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = user.name.take(1).uppercase(),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlueDark
                            )
                        }

                        if (isEditing) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.35f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Change photo", tint = Color.White)
                            }
                        }
                    }

                    if (isEditing) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBangladesh) "ছবি পরিবর্তনে ক্লিক করুন (ঐচ্ছিক)" else "Tap to change photo (Optional)",
                            fontSize = 11.sp,
                            color = BrandBlueDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Role Badge
                    Surface(
                        color = BrandBlueLight,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BrandBlue.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = if (isBangladesh) "গ্রাহক অ্যাকাউন্ট (Customer)" else "Customer Account",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandBlueDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (errorMessage != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ErrorRedLight),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = errorMessage ?: "",
                                color = ErrorRed,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    if (!isEditing) {
                        // View Profile Mode
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                ProfileInfoRow(
                                    icon = Icons.Default.Person,
                                    label = if (isBangladesh) "পূর্ণ নাম" else "Full Name",
                                    value = user.name
                                )
                                ProfileInfoRow(
                                    icon = Icons.Default.Email,
                                    label = if (isBangladesh) "ইমেইল" else "Email",
                                    value = user.email
                                )
                                ProfileInfoRow(
                                    icon = Icons.Default.Phone,
                                    label = if (isBangladesh) "ফোন নম্বর" else "Phone Number",
                                    value = if (user.phone.isNotBlank()) user.phone else "—"
                                )
                                ProfileInfoRow(
                                    icon = Icons.Default.Public,
                                    label = if (isBangladesh) "দেশ" else "Country",
                                    value = "${countryInfo.flag} ${if (isBangladesh) countryInfo.nameBn else countryInfo.nameEn}"
                                )
                                ProfileInfoRow(
                                    icon = Icons.Default.LocationOn,
                                    label = if (showBn) "বিভাগ ও জেলা" else "${countryInfo.getDivisionLabel(false)} & ${countryInfo.getDistrictLabel(false)}",
                                    value = "${user.division}, ${user.district}"
                                )
                                if (user.address.isNotBlank()) {
                                    ProfileInfoRow(
                                        icon = Icons.Default.Home,
                                        label = if (isBangladesh) "বিস্তারিত ঠিকানা" else "Address",
                                        value = user.address
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                isEditing = true
                                errorMessage = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("edit_customer_profile_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isBangladesh) "তথ্য পরিবর্তন করুন (Edit Profile)" else "Edit Profile", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Edit Mode
                        val profileFieldColors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandBlueDark,
                            unfocusedBorderColor = Color(0xFF1E293B), // High-contrast, solid dark slate border on all 4 sides
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = BrandBlueDark,
                            unfocusedLabelColor = Color(0xFF0F172A),
                            focusedLeadingIconColor = BrandBlueDark,
                            unfocusedLeadingIconColor = Color(0xFF334155)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text(if (isBangladesh) "পূর্ণ নাম *" else "Full Name *") },
                                placeholder = { Text(if (isBangladesh) "এখানে পূর্ণ নাম লিখুন" else "Enter full name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = profileFieldColors
                            )

                            OutlinedTextField(
                                value = user.email,
                                onValueChange = {},
                                label = { Text(if (isBangladesh) "ইমেইল (অপরিবর্তনীয়)" else "Email (Read-only)") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false,
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = profileFieldColors
                            )

                            OutlinedTextField(
                                value = editPhone,
                                onValueChange = { editPhone = it },
                                label = { Text(if (isBangladesh) "ফোন নম্বর *" else "Phone Number *") },
                                placeholder = { Text(if (isBangladesh) "এখানে মোবাইল নম্বর লিখুন" else "Enter phone number") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = profileFieldColors
                            )

                            // Division Selector
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = editDivision,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("${countryInfo.getDivisionLabel(showBn)} *") },
                                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                                    trailingIcon = {
                                        IconButton(onClick = { divisionExpanded = true }) {
                                            Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlueDark)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().clickable { divisionExpanded = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = profileFieldColors
                                )
                            }

                            if (divisionExpanded) {
                                SearchableLocationPickerDialog(
                                    title = if (showBn) "${countryInfo.getDivisionLabel(true)} নির্বাচন করুন" else "Select ${countryInfo.getDivisionLabel(false)}",
                                    items = divisionList,
                                    selectedItem = editDivision,
                                    searchPlaceholder = if (showBn) "বিভাগ সার্চ করুন..." else "Search division...",
                                    onItemSelected = { chosenDiv ->
                                        editDivision = chosenDiv
                                        editDistrict = ""
                                        divisionExpanded = false
                                    },
                                    onDismissRequest = { divisionExpanded = false }
                                )
                            }

                            // District Selector
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = editDistrict,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("${countryInfo.getDistrictLabel(showBn)} *") },
                                    leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
                                    trailingIcon = {
                                        IconButton(onClick = { districtExpanded = true }) {
                                            Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlueDark)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().clickable { districtExpanded = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = profileFieldColors
                                )
                            }

                            if (districtExpanded) {
                                SearchableLocationPickerDialog(
                                    title = if (showBn) "${countryInfo.getDistrictLabel(true)} নির্বাচন করুন" else "Select ${countryInfo.getDistrictLabel(false)}",
                                    items = districtList,
                                    selectedItem = editDistrict,
                                    searchPlaceholder = if (showBn) "জেলা সার্চ করুন..." else "Search district...",
                                    onItemSelected = { chosenDist ->
                                        editDistrict = chosenDist
                                        districtExpanded = false
                                    },
                                    onDismissRequest = { districtExpanded = false }
                                )
                            }

                            OutlinedTextField(
                                value = editAddress,
                                onValueChange = { editAddress = it },
                                label = { Text(if (isBangladesh) "বিস্তারিত ঠিকানা (বাড়ি/রাস্তা - ঐচ্ছিক)" else "Detailed Address (Optional)") },
                                placeholder = { Text(if (isBangladesh) "এখানে ঠিকানা লিখুন" else "Enter address") },
                                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = profileFieldColors
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        isEditing = false
                                        editName = user.name
                                        editPhone = user.phone
                                        editDivision = user.division
                                        editDistrict = user.district
                                        editAddress = user.address
                                        editImageUri = user.profileImageUri
                                        errorMessage = null
                                    },
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(if (isBangladesh) "বাতিল" else "Cancel")
                                }

                                Button(
                                    onClick = {
                                        if (editName.isBlank()) {
                                            errorMessage = if (isBangladesh) "পূর্ণ নাম লিখুন" else "Please enter full name"
                                            return@Button
                                        }
                                        if (editPhone.isBlank()) {
                                            errorMessage = if (isBangladesh) "ফোন নম্বর লিখুন" else "Please enter phone number"
                                            return@Button
                                        }
                                        if (editDivision.isBlank() || editDistrict.isBlank()) {
                                            errorMessage = if (isBangladesh) "বিভাগ ও জেলা নির্বাচন করুন" else "Please select division & district"
                                            return@Button
                                        }
                                        isSaving = true
                                        errorMessage = null
                                        onSaveProfile(
                                            editName,
                                            editPhone,
                                            editDivision,
                                            editDistrict,
                                            editAddress,
                                            editImageUri,
                                            {
                                                isSaving = false
                                                isEditing = false
                                            },
                                            { err ->
                                                isSaving = false
                                                errorMessage = err
                                            }
                                        )
                                    },
                                    enabled = !isSaving,
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                                ) {
                                    if (isSaving) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                                    } else {
                                        Text(if (isBangladesh) "সংরক্ষণ করুন" else "Save Changes", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, fontSize = 11.sp, color = TextSecondary)
                Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            }
        }
    }
}
