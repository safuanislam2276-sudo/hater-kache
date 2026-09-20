package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.country.CountryRepository
import com.example.ui.theme.*

/**
 * MultiCategorySelector allows a worker/mistri to select 1 up to 6 categories/professions.
 * Category 1 is the primary profession, and Categories 2 through 6 represent additional expertise.
 * Includes a real-time search box at the top of the picker dialog.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MultiCategorySelector(
    selectedCategories: List<String>,
    onCategoriesChanged: (List<String>) -> Unit,
    isBangladesh: Boolean = true,
    maxCategories: Int = 6,
    modifier: Modifier = Modifier
) {
    var showCategoryPickerDialog by remember { mutableStateOf(false) }

    // Dialog for picking another category
    if (showCategoryPickerDialog) {
        CategorySearchPickerDialog(
            alreadySelected = selectedCategories,
            isBangladesh = isBangladesh,
            onCategorySelected = { newCat ->
                if (!selectedCategories.contains(newCat) && selectedCategories.size < maxCategories) {
                    onCategoriesChanged(selectedCategories + newCat)
                }
                showCategoryPickerDialog = false
            },
            onDismissRequest = { showCategoryPickerDialog = false }
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isBangladesh) "কাজের পেশা / ক্যাটাগরি (১ হতে ৬টি) *" else "Professions / Categories (1 to 6) *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlueDark
                )
                Text(
                    text = if (isBangladesh) "অভিজ্ঞ হলে একাধিক ক্যাটাগরি সিলেক্ট করতে পারবেন" else "Select up to 6 categories of expertise",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BrandBlue.copy(alpha = 0.12f),
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Text(
                    text = "${selectedCategories.size}/$maxCategories",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlue,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedCategories.isEmpty()) {
            OutlinedCard(
                onClick = { showCategoryPickerDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("select_category_prominent_card"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(2.dp, BrandBlue),
                colors = CardDefaults.outlinedCardColors(containerColor = BrandBlue.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BrandBlue,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isBangladesh) "ক্যাটাগরি সিলেক্ট করুন *" else "Select Category *",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlueDark
                            )
                            Text(
                                text = if (isBangladesh) "+ ক্যাটাগরি যোগ করুন (পেশা নির্বাচন করতে ক্লিক করুন)" else "+ Add Category (Click to select profession)",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        } else {
            // Flow row displaying selected categories in order: ক্যাটাগরি ১, ক্যাটাগরি ২, ইত্যাদি
            FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedCategories.forEachIndexed { index, catName ->
                val isPrimary = index == 0
                val categoryNumberLabel = if (isBangladesh) {
                    val banglaNums = listOf("১", "২", "৩", "৪", "৫", "৬")
                    val bnNum = banglaNums.getOrElse(index) { "${index + 1}" }
                    if (isPrimary) "ক্যাটাগরি $bnNum (মূল)" else "ক্যাটাগরি $bnNum"
                } else {
                    if (isPrimary) "Category ${index + 1} (Main)" else "Category ${index + 1}"
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isPrimary) BrandBlue.copy(alpha = 0.12f) else Color(0xFFF1F5F9),
                    border = BorderStroke(1.2.dp, if (isPrimary) BrandBlue else Color(0xFFCBD5E1))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isPrimary) BrandBlue else Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isBangladesh) listOf("১", "২", "৩", "৪", "৫", "৬").getOrElse(index) { "${index + 1}" } else "${index + 1}",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Column {
                            Text(
                                text = categoryNumberLabel,
                                fontSize = 9.sp,
                                color = if (isPrimary) BrandBlue else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = CountryRepository.getProfessionDisplayName(catName, isBangladesh),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        if (!isPrimary || selectedCategories.size > 1) {
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = {
                                    val updated = selectedCategories.toMutableList()
                                    updated.removeAt(index)
                                    onCategoriesChanged(updated)
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Button to add more categories if limit not reached
            if (selectedCategories.size < maxCategories) {
                OutlinedCard(
                    onClick = { showCategoryPickerDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, BrandBlue),
                    colors = CardDefaults.outlinedCardColors(containerColor = BrandBlue.copy(alpha = 0.05f)),
                    modifier = Modifier.testTag("add_category_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = BrandBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val nextIndex = selectedCategories.size
                        val nextLabel = if (isBangladesh) {
                            val banglaNums = listOf("১", "২", "৩", "৪", "৫", "৬")
                            val bnNum = banglaNums.getOrElse(nextIndex) { "${nextIndex + 1}" }
                            "+ ক্যাটাগরি $bnNum যোগ করুন"
                        } else {
                            "+ Add Category ${nextIndex + 1}"
                        }
                        Text(
                            text = nextLabel,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandBlue
                        )
                    }
                }
            }
        }
    }
}
}

/**
 * CategorySearchPickerDialog provides real-time search box for professions/categories
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchPickerDialog(
    alreadySelected: List<String>,
    isBangladesh: Boolean,
    onCategorySelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val allProfessions = remember(isBangladesh) {
        CountryRepository.ALL_PROFESSION_ITEMS.drop(1).map {
            if (isBangladesh) it.nameBn else it.nameEn
        }
    }

    val filteredList = remember(searchQuery, allProfessions, alreadySelected) {
        val q = searchQuery.trim()
        allProfessions.filter { cat ->
            !alreadySelected.contains(cat) && (q.isEmpty() || cat.contains(q, ignoreCase = true))
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
                        text = if (isBangladesh) "ক্যাটাগরি বা পেশা নির্বাচন করুন" else "Select Category / Profession",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search box with instant filtering
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isBangladesh) "পেশা বা ক্যাটাগরি সার্চ করুন..." else "Search profession...") },
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
                        .testTag("category_search_input"),
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

                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isBangladesh) "কোনো ক্যাটাগরি পাওয়া যায়নি" else "No category found",
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
                        items(filteredList) { catName ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clickable {
                                        onCategorySelected(catName)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Work,
                                            contentDescription = null,
                                            tint = BrandBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = catName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    }
                                    Icon(
                                        Icons.Default.AddCircleOutline,
                                        contentDescription = "Select",
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
