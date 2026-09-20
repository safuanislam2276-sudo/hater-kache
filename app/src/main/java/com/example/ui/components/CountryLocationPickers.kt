package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.country.CountryInfo
import com.example.data.country.CountryRepository
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerDialog(
    selectedCountry: CountryInfo,
    isBangladesh: Boolean = true,
    onCountrySelected: (CountryInfo) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCountries = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            CountryRepository.ALL_COUNTRIES
        } else {
            CountryRepository.ALL_COUNTRIES.filter {
                it.nameBn.contains(searchQuery, ignoreCase = true) ||
                        it.nameEn.contains(searchQuery, ignoreCase = true) ||
                        it.phoneCode.contains(searchQuery)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth(0.95f),
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBangladesh) "দেশ নির্বাচন করুন (১০০ দেশ)" else "Select Country (100 Countries)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isBangladesh) "দেশ বা ফোন কোড খুঁজুন..." else "Search country or phone code...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlue) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("country_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandBlue,
                        unfocusedBorderColor = Color(0xFF1E293B),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                ) {
                    items(filteredCountries, key = { it.code + it.phoneCode }) { country ->
                        val isSelected = country.code == selectedCountry.code
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) BrandBlue.copy(alpha = 0.12f) else Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clickable {
                                    onCountrySelected(country)
                                    onDismissRequest()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = country.flag, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isBangladesh) country.nameBn else country.nameEn,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (isBangladesh) country.nameEn else country.nameBn,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = country.phoneCode,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandBlue
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = BrandBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndependentSearchFilterSheet(
    currentCountry: String,
    currentDivision: String,
    currentDistrict: String,
    currentProfession: String,
    isBangladesh: Boolean = true,
    onApplyFilter: (country: String, division: String, district: String, profession: String) -> Unit,
    onReset: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedCountryName by remember { mutableStateOf(currentCountry) }
    var selectedDivision by remember { mutableStateOf(currentDivision) }
    var selectedDistrict by remember { mutableStateOf(currentDistrict) }
    var selectedProfession by remember { mutableStateOf(currentProfession) }

    val countryObj = remember(selectedCountryName) {
        CountryRepository.getCountryByName(selectedCountryName)
    }
    val countryIsBd = countryObj.code == "BD" || countryObj.nameEn.equals("Bangladesh", ignoreCase = true)
    val showInBn = isBangladesh && countryIsBd
    val divisionsMap = countryObj.getDivisions(showInBn)
    val allWord = if (showInBn) "সকল" else "All"
    val divisionNames = remember(divisionsMap, allWord) {
        listOf(allWord) + divisionsMap.keys.toList()
    }
    val districtNames = remember(selectedDivision, divisionsMap, allWord) {
        if (selectedDivision == "সকল" || selectedDivision == "All" || !divisionsMap.containsKey(selectedDivision)) {
            val allDistricts = divisionsMap.values.flatten().distinct()
            if (allDistricts.isEmpty()) listOf(allWord) else listOf(allWord) + allDistricts
        } else {
            listOf(allWord) + (divisionsMap[selectedDivision] ?: emptyList())
        }
    }

    var showCountryPicker by remember { mutableStateOf(false) }
    var showDivisionPicker by remember { mutableStateOf(false) }
    var showDistrictPicker by remember { mutableStateOf(false) }

    if (showCountryPicker) {
        CountryPickerDialog(
            selectedCountry = countryObj,
            isBangladesh = isBangladesh,
            onCountrySelected = { chosen ->
                val chosenIsBd = chosen.code == "BD" || chosen.nameEn.equals("Bangladesh", ignoreCase = true)
                val willBeBn = isBangladesh && chosenIsBd
                selectedCountryName = if (willBeBn) chosen.nameBn else chosen.nameEn
                val newAll = if (willBeBn) "সকল" else "All"
                selectedDivision = newAll
                selectedDistrict = newAll
            },
            onDismissRequest = { showCountryPicker = false }
        )
    }

    if (showDivisionPicker) {
        SearchableLocationPickerDialog(
            title = if (showInBn) "${countryObj.getDivisionLabel(true)} নির্বাচন করুন" else "Select ${countryObj.getDivisionLabel(false)}",
            items = divisionNames,
            selectedItem = selectedDivision,
            searchPlaceholder = if (showInBn) "বিভাগ সার্চ করুন..." else "Search division...",
            onItemSelected = { chosenDiv ->
                selectedDivision = chosenDiv
                selectedDistrict = allWord
            },
            onDismissRequest = { showDivisionPicker = false }
        )
    }

    if (showDistrictPicker) {
        SearchableLocationPickerDialog(
            title = if (showInBn) "${countryObj.getDistrictLabel(true)} নির্বাচন করুন" else "Select ${countryObj.getDistrictLabel(false)}",
            items = districtNames,
            selectedItem = selectedDistrict,
            searchPlaceholder = if (showInBn) "জেলা সার্চ করুন..." else "Search district...",
            onItemSelected = { chosenDist ->
                selectedDistrict = chosenDist
            },
            onDismissRequest = { showDistrictPicker = false }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBangladesh) "স্বাধীন সার্চ ফিল্টার" else "Independent Search Filter",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isBangladesh) "প্রোফাইলের হোম লোকেশন অপরিবর্তিত থাকবে" else "Profile home location remains unchanged",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextButton(onClick = {
                    onReset()
                    onDismissRequest()
                }) {
                    Text(if (isBangladesh) "রিসেট করুন" else "Reset")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Country row
            Text(
                text = if (isBangladesh) "সার্চের দেশ:" else "Search Country:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedCard(
                onClick = { showCountryPicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("filter_country_selector"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = countryObj.flag, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (showInBn) countryObj.nameBn else countryObj.nameEn,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Division & District (if available)
            if (divisionsMap.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Division button (with search dialog)
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(
                                text = countryObj.getDivisionLabel(showInBn) + ":",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedCard(
                                onClick = { showDivisionPicker = true },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("filter_division_selector")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedDivision,
                                        fontSize = 13.sp,
                                        maxLines = 1
                                    )
                                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp), tint = BrandBlue)
                                }
                            }
                        }
                    }

                    // District button (with search dialog)
                    Box(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(
                                text = countryObj.getDistrictLabel(showInBn) + ":",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedCard(
                                onClick = { showDistrictPicker = true },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("filter_district_selector")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedDistrict,
                                        fontSize = 13.sp,
                                        maxLines = 1
                                    )
                                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp), tint = BrandBlue)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Profession selector
            var profExpanded by remember { mutableStateOf(false) }
            Text(
                text = if (isBangladesh) "পেশা / ক্যাটাগরি:" else "Profession / Category:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedCard(
                    onClick = { profExpanded = true },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = CountryRepository.getProfessionDisplayName(selectedProfession, isBangladesh),
                            fontSize = 14.sp
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
                DropdownMenu(
                    expanded = profExpanded,
                    onDismissRequest = { profExpanded = false }
                ) {
                    CountryRepository.ALL_PROFESSION_ITEMS.forEach { profItem ->
                        val displayName = if (isBangladesh) profItem.nameBn else profItem.nameEn
                        DropdownMenuItem(
                            text = { Text(displayName) },
                            onClick = {
                                selectedProfession = if (isBangladesh) profItem.nameBn else profItem.nameEn
                                profExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onApplyFilter(selectedCountryName, selectedDivision, selectedDistrict, selectedProfession)
                    onDismissRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("apply_search_filter_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.FilterList, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBangladesh) "ফিল্টার প্রয়োগ করুন" else "Apply Filter",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

/**
 * SearchableLocationPickerDialog provides an immediate search box at the top
 * of the dialog to filter Divisions or Districts automatically in real-time.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableLocationPickerDialog(
    title: String,
    items: List<String>,
    selectedItem: String,
    searchPlaceholder: String = "সার্চ করুন...",
    onItemSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = remember(searchQuery, items) {
        if (searchQuery.isBlank()) {
            items
        } else {
            items.filter { it.contains(searchQuery.trim(), ignoreCase = true) }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth(0.95f),
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(searchPlaceholder) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlue) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("searchable_location_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandBlue,
                        unfocusedBorderColor = Color(0xFF1E293B),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (filteredItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "কোনো ফলাফল পাওয়া যায়নি",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp)
                    ) {
                        items(filteredItems) { item ->
                            val isSelected = item == selectedItem
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) BrandBlue.copy(alpha = 0.12f) else Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clickable {
                                        onItemSelected(item)
                                        onDismissRequest()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 15.sp,
                                        color = if (isSelected) BrandBlue else TextPrimary
                                    )
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = BrandBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
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

