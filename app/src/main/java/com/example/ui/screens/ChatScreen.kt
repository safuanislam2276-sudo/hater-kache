package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ConversationSummary
import com.example.data.model.MistriEntity
import com.example.data.model.UserEntity
import com.example.ui.components.MistriAvatar
import com.example.ui.theme.*
import com.example.ui.util.MessengerAudioEffects
import com.example.ui.viewmodel.ChatRecipientInfo
import java.text.SimpleDateFormat
import java.util.*

private val MessengerBlue = Color(0xFF0084FF)
private val MessengerLightBg = Color(0xFFF0F2F5)
private val MessengerDarkText = Color(0xFF050505)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerListScreen(
    conversations: List<ConversationSummary>,
    currentUser: UserEntity?,
    allWorkers: List<MistriEntity> = emptyList(),
    allUsers: List<UserEntity> = emptyList(),
    onBack: () -> Unit,
    onOpenConversation: (ConversationSummary) -> Unit,
    onOpenUserChat: (targetId: String, targetName: String, targetEmail: String, targetRole: String, phone: String?, profession: String?, imageUri: String?) -> Unit,
    onBrowseMistris: () -> Unit,
    onSaveAutoReply: (String) -> Unit = {},
    getAutoReply: suspend () -> String? = { null },
    onDeleteConversation: (String) -> Unit = {},
    onStartCall: (ConversationSummary) -> Unit = {}
) {
    val isBangladesh = currentUser?.countryCode == "+880" || currentUser?.country?.contains("বাংলাদেশ") == true
    var searchQuery by remember { mutableStateOf("") }
    var showAutoReplyDialog by remember { mutableStateOf(false) }
    var showMenuDropdown by remember { mutableStateOf(false) }
    var conversationToDelete by remember { mutableStateOf<ConversationSummary?>(null) }
    var autoReplyInput by remember { mutableStateOf("") }
    var autoReplyLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(showAutoReplyDialog) {
        if (showAutoReplyDialog) {
            val saved = getAutoReply()
            autoReplyInput = saved ?: ""
            autoReplyLoaded = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MessengerBlue,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Forum, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBangladesh) "মেসেঞ্জার চ্যাট বক্স" else "Messenger Chats",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (isBangladesh) "গ্রাহক ও কারিগর সরাসরি বার্তালাপ" else "Direct Customer & Worker Chat",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("messenger_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Three-line menu for automatic message settings as requested by user
                    Box {
                        IconButton(
                            onClick = { showMenuDropdown = true },
                            modifier = Modifier.testTag("messenger_three_line_menu_button")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF1E293B))
                        }
                        DropdownMenu(
                            expanded = showMenuDropdown,
                            onDismissRequest = { showMenuDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = MessengerBlue, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = if (isBangladesh) "অটোমেটিক মেসেজ অপশন" else "Automatic Message",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = if (isBangladesh) "স্বয়ংক্রিয় প্রত্যুত্তর লিখে সেভ করুন" else "Set your custom auto-reply",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    showMenuDropdown = false
                                    showAutoReplyDialog = true
                                },
                                modifier = Modifier.testTag("messenger_menu_auto_reply_option")
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
        ) {
            // First item inside: Search box for searching profile names to message (User requested: ভিতরে ঢুকিলেয় প্রথম একটি সাচ বক্স থাকবে সেই বক্সসে প্রোফাইলের নাম সাচ দিয়ে মেসেজ দিতে পারবে)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        if (isBangladesh) "প্রোফাইলের নাম সার্চ করে মেসেজ দিন..." else "Search profile name to message...",
                        fontSize = 13.5.sp,
                        color = Color(0xFF64748B)
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = MessengerBlue)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("messenger_profile_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MessengerBlue,
                    unfocusedBorderColor = Color(0xFF475569),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                ),
                singleLine = true
            )

            // When searching by profile name
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim()
                val matchedWorkers = remember(q, allWorkers, currentUser) {
                    allWorkers.filter { w ->
                        w.userId != currentUser?.id && (
                            w.name.contains(q, ignoreCase = true) ||
                            w.profession.contains(q, ignoreCase = true) ||
                            w.phone.contains(q)
                        )
                    }
                }
                val matchedCustomerUsers = remember(q, allUsers, currentUser) {
                    allUsers.filter { u ->
                        u.id != currentUser?.id && u.userType == "CUSTOMER" && (
                            u.name.contains(q, ignoreCase = true) ||
                            u.phone.contains(q)
                        )
                    }
                }

                if (matchedWorkers.isEmpty() && matchedCustomerUsers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SearchOff, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isBangladesh) "\"$q\" নামে কোনো প্রোফাইল পাওয়া যায়নি" else "No profile found matching \"$q\"",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Section header for workers
                        if (matchedWorkers.isNotEmpty()) {
                            item {
                                Text(
                                    text = if (isBangladesh) "কারিগর ও মিস্ত্রি প্রোফাইল (${matchedWorkers.size})" else "Mistri Profiles (${matchedWorkers.size})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                                )
                            }
                            items(matchedWorkers, key = { "w_${it.id}" }) { worker ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onOpenUserChat(
                                                worker.userId,
                                                worker.name,
                                                worker.email,
                                                "MISTRI",
                                                worker.phone,
                                                worker.profession,
                                                worker.profileImageUri
                                            )
                                        }
                                        .testTag("search_profile_worker_${worker.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        MistriAvatar(
                                            name = worker.name,
                                            imageUri = worker.profileImageUri,
                                            size = 46,
                                            isPro = worker.isPro
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = worker.name,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.5.sp,
                                                    color = Color(0xFF0F172A)
                                                )
                                                if (worker.isPro) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Surface(
                                                        shape = RoundedCornerShape(4.dp),
                                                        color = Color(0xFFFEF3C7)
                                                    ) {
                                                        Text("PRO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFB45309), modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                                    }
                                                }
                                            }
                                            Text(
                                                text = "কারিগর মিস্ত্রি • ${worker.profession}",
                                                fontSize = 12.sp,
                                                color = Color(0xFF2563EB)
                                            )
                                            if (worker.district.isNotBlank()) {
                                                Text(
                                                    text = "${worker.division}, ${worker.district}",
                                                    fontSize = 11.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                onOpenUserChat(
                                                    worker.userId,
                                                    worker.name,
                                                    worker.email,
                                                    "MISTRI",
                                                    worker.phone,
                                                    worker.profession,
                                                    worker.profileImageUri
                                                )
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MessengerBlue),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                            modifier = Modifier.testTag("chat_with_worker_${worker.id}")
                                        ) {
                                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (isBangladesh) "মেসেজ" else "Chat", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Section header for customers
                        if (matchedCustomerUsers.isNotEmpty()) {
                            item {
                                Text(
                                    text = if (isBangladesh) "গ্রাহক প্রোফাইল (${matchedCustomerUsers.size})" else "Customer Profiles (${matchedCustomerUsers.size})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF),
                                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                                )
                            }
                            items(matchedCustomerUsers, key = { "u_${it.id}" }) { cust ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onOpenUserChat(
                                                cust.id,
                                                cust.name,
                                                cust.email,
                                                cust.userType,
                                                cust.phone,
                                                null,
                                                cust.profileImageUri
                                            )
                                        }
                                        .testTag("search_profile_customer_${cust.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFFDBEAFE),
                                            modifier = Modifier.size(46.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = cust.name.take(1).uppercase(),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp,
                                                    color = Color(0xFF1E40AF)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = cust.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.5.sp,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = if (isBangladesh) "সম্মানিত গ্রাহক" else "Customer",
                                                fontSize = 12.sp,
                                                color = Color(0xFF166534)
                                            )
                                            if (cust.district.isNotBlank()) {
                                                Text(
                                                    text = "${cust.division}, ${cust.district}",
                                                    fontSize = 11.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                onOpenUserChat(
                                                    cust.id,
                                                    cust.name,
                                                    cust.email,
                                                    cust.userType,
                                                    cust.phone,
                                                    null,
                                                    cust.profileImageUri
                                                )
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MessengerBlue),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                            modifier = Modifier.testTag("chat_with_customer_${cust.id}")
                                        ) {
                                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (isBangladesh) "মেসেজ" else "Chat", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // When search box is empty: shows all conversations, with the one messaged currently at the very top (Messenger style)
                if (conversations.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.2.dp, Color(0xFFCBD5E1)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFEFF6FF),
                                    modifier = Modifier.size(72.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Forum, contentDescription = null, tint = MessengerBlue, modifier = Modifier.size(36.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (isBangladesh) "কোন মেসেজ বা চ্যাট নেই" else "No Messages Yet",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isBangladesh)
                                        "উপরে সার্চ বক্সে নাম লিখে সরাসরি যেকোনো গ্রাহক বা কারিগরের সাথে কথা বলুন।"
                                    else
                                        "Use the search box above to find any customer or worker by profile name.",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = onBrowseMistris,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MessengerBlue)
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (isBangladesh) "কারিগর খুঁজুন ও চ্যাট করুন" else "Find Mistri to Chat")
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            // Sync & Restore notice banner
                            Surface(
                                color = Color(0xFFF0FDF4),
                                border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isBangladesh)
                                            "চ্যাট ও মেসেজ সারা জীবনের জন্য সুরক্ষিত। চাপ দিয়ে ধরে রাখলে বা ডিলিট আইকনে চাপলে ডিলিট অপশন আসবে।"
                                        else
                                            "Conversations stay saved permanently. Long-press or tap trash icon to delete.",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF166534),
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }

                        items(conversations, key = { it.conversationId }) { conv ->
                            ConversationItemRow(
                                conversation = conv,
                                onClick = { onOpenConversation(conv) },
                                onDelete = { conversationToDelete = conv },
                                onCall = { onStartCall(conv) }
                            )
                            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }

    // Delete Conversation Confirmation Dialog
    if (conversationToDelete != null) {
        val targetConv = conversationToDelete!!
        AlertDialog(
            onDismissRequest = { conversationToDelete = null },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEE2E2),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            title = {
                Text(
                    text = if (isBangladesh) "চ্যাট ও প্রোফাইল ডিলিট করতে চান?" else "Delete Chat Profile?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Text(
                    text = if (isBangladesh)
                        "আপনি কি \"${targetConv.otherUserName}\"-এর সাথে সমস্ত কথোপকথন ও মেসেজ ডিলিট করতে চান? আপনি ডিলিট না করা পর্যন্ত এটি সারা জীবনের জন্য মেসেঞ্জারে সংরক্ষিত থাকবে।"
                    else
                        "Do you want to delete all messages with \"${targetConv.otherUserName}\"? Unless deleted, this profile stays saved permanently.",
                    fontSize = 13.5.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConversation(targetConv.conversationId)
                        conversationToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    modifier = Modifier.testTag("confirm_delete_conversation_button")
                ) {
                    Text(if (isBangladesh) "ডিলিট করুন" else "Delete")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { conversationToDelete = null },
                    modifier = Modifier.testTag("cancel_delete_conversation_button")
                ) {
                    Text(if (isBangladesh) "বাতিল" else "Cancel")
                }
            }
        )
    }

    // Automatic Message Settings Dialog (Triggered from 3-line menu)
    if (showAutoReplyDialog) {
        AlertDialog(
            onDismissRequest = { showAutoReplyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFEFF6FF),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, tint = MessengerBlue, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isBangladesh) "অটোমেটিক মেসেজ অপশন" else "Automatic Message",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (isBangladesh) "স্বয়ংক্রিয় প্রত্যুত্তর সেটআপ" else "Auto-Reply Setup",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isBangladesh)
                            "এখানে আপনি যে মেসেজ লিখে সেভ করবেন, আপনাকে কেউ মেসেজ দিলেই এই মেসেজটি স্বয়ংক্রিয়ভাবে তার কাছে চলে যাবে। যদি কিছু না লিখে মুছে ফেলেন তবে কাউকে কোনো অটো-মেসেজ যাবে না।"
                        else
                            "The text saved here will be sent as an automatic reply whenever someone messages you. If cleared, no automatic message is sent.",
                        fontSize = 12.5.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = autoReplyInput,
                        onValueChange = { autoReplyInput = it },
                        placeholder = {
                            Text(
                                if (isBangladesh)
                                    "যেমন: আসসালামু আলাইকুম! আপনার মেসেজ পেয়েছি, কিছুক্ষণ পর যোগাযোগ করছি।"
                                else
                                    "e.g. Hello! I received your message and will get back to you shortly.",
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("auto_reply_message_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MessengerBlue,
                            unfocusedBorderColor = Color(0xFF64748B)
                        ),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveAutoReply(autoReplyInput.trim())
                        showAutoReplyDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MessengerBlue),
                    modifier = Modifier.testTag("save_auto_reply_button")
                ) {
                    Text(if (isBangladesh) "সেভ করুন" else "Save")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        autoReplyInput = ""
                        onSaveAutoReply("")
                        showAutoReplyDialog = false
                    },
                    modifier = Modifier.testTag("clear_auto_reply_button")
                ) {
                    Text(if (isBangladesh) "মুছে ফেলুন / বন্ধ" else "Clear / Disable")
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationItemRow(
    conversation: ConversationSummary,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onCall: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeStr = remember(conversation.lastTimestamp) { timeFormat.format(Date(conversation.lastTimestamp)) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onDelete
            )
            .testTag("conversation_row_${conversation.conversationId}"),
        color = if (conversation.unreadCount > 0) Color(0xFFF8FAFC) else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar with Status Indicator
            Box {
                Surface(
                    shape = CircleShape,
                    color = if (conversation.otherUserRole == "MISTRI") Color(0xFFFEF3C7) else Color(0xFFDBEAFE),
                    border = BorderStroke(1.5.dp, if (conversation.otherUserRole == "MISTRI") Color(0xFFF59E0B) else MessengerBlue),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = conversation.otherUserName.take(1).uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (conversation.otherUserRole == "MISTRI") Color(0xFFB45309) else Color(0xFF1E40AF)
                        )
                    }
                }
                // Online Dot (Green if online, Yellow if offline)
                val statusDotColor = if (conversation.isOnline) Color(0xFF22C55E) else Color(0xFFEAB308)
                Box(
                    modifier = Modifier
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(statusDotColor)
                        .border(1.5.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.otherUserName,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = timeStr,
                        fontSize = 11.sp,
                        color = if (conversation.unreadCount > 0) MessengerBlue else TextSecondary,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 13.sp,
                        color = if (conversation.unreadCount > 0) TextPrimary else TextSecondary,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = MessengerBlue,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${conversation.unreadCount}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Quick In-App Voice Call Button
            IconButton(
                onClick = onCall,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("call_conv_${conversation.conversationId}")
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "In-App Voice Call",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerChatScreen(
    recipient: ChatRecipientInfo?,
    messages: List<ChatMessageEntity>,
    currentUser: UserEntity?,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onCallClick: () -> Unit,
    onWaClick: () -> Unit,
    onDeleteMessage: ((String) -> Unit)? = null,
    onDeleteChat: (() -> Unit)? = null
) {
    val myId = currentUser?.id ?: "u-customer-demo"
    val myEmail = currentUser?.email.orEmpty()
    val isBangladesh = currentUser?.countryCode == "+880" || currentUser?.country?.contains("বাংলাদেশ") == true
    var showDeleteChatConfirm by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    var previousMessagesCount by remember { mutableIntStateOf(messages.size) }

    // Auto-scroll to bottom on new message and play sound for incoming message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
        if (messages.size > previousMessagesCount) {
            val latest = messages.lastOrNull()
            val isFromMe = latest?.senderId == myId || (myEmail.isNotBlank() && latest?.senderEmail.equals(myEmail, ignoreCase = true))
            if (!isFromMe) {
                MessengerAudioEffects.playMessageReceived()
                MessengerAudioEffects.playHapticFeedback(context)
            }
        }
        previousMessagesCount = messages.size
    }

    val quickReplies = remember(recipient?.role) {
        if (recipient?.role == "MISTRI") {
            listOf(
                "কখন আসতে পারবেন?",
                "কাজের আনুমানিক খরচ কত?",
                "জরুরি সার্ভিস প্রয়োজন!",
                "আমার লোকেশনে কাজ করতে পারবেন?"
            )
        } else {
            listOf(
                "ঠিকানা ও লোকেশন দিন",
                "আমি আধা ঘণ্টার মধ্যে আসছি",
                "কোন ধরনের সমস্যা বিস্তারিত বলুন",
                "কল দিন কথা বলি"
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val isOnline = recipient?.isOnline ?: false
                        val statusColor = if (isOnline) Color(0xFF22C55E) else Color(0xFFEAB308)

                        Box {
                            Surface(
                                shape = CircleShape,
                                color = if (recipient?.role == "MISTRI") Color(0xFFFEF3C7) else Color(0xFFDBEAFE),
                                border = BorderStroke(1.5.dp, if (recipient?.role == "MISTRI") Color(0xFFF59E0B) else MessengerBlue),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = (recipient?.name ?: "M").take(1).uppercase(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = if (recipient?.role == "MISTRI") Color(0xFFB45309) else Color(0xFF1E40AF)
                                    )
                                }
                            }
                            // Active status dot (Green if online, Yellow if offline)
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                                    .border(1.5.dp, Color.White, CircleShape)
                                    .align(Alignment.BottomEnd)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = recipient?.name ?: if (isBangladesh) "কারিগর" else "Technician",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(statusColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isOnline) {
                                        if (isBangladesh) "অনলাইনে আছেন" else "Active Now"
                                    } else {
                                        if (isBangladesh) "অফলাইন" else "Offline"
                                    },
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isOnline) Color(0xFF16A34A) else Color(0xFFD97706)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("chat_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Direct in-app voice call button
                    IconButton(onClick = onCallClick, modifier = Modifier.testTag("chat_call_button")) {
                        Icon(Icons.Default.Phone, contentDescription = "Messenger Voice Call", tint = Color(0xFF10B981))
                    }
                    if (!recipient?.phone.isNullOrBlank()) {
                        IconButton(onClick = onWaClick, modifier = Modifier.testTag("chat_wa_button")) {
                            Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color(0xFF25D366))
                        }
                    }
                    if (onDeleteChat != null) {
                        IconButton(onClick = { showDeleteChatConfirm = true }, modifier = Modifier.testTag("chat_delete_button")) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Chat", tint = Color(0xFF64748B))
                        }
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
                        .navigationBarsPadding()
                        .padding(bottom = 6.dp)
                ) {
                    // Quick Action Reply Chips
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickReplies) { chipText ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFF1F5F9),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.clickable {
                                    MessengerAudioEffects.playMessageSent()
                                    MessengerAudioEffects.playHapticFeedback(context)
                                    onSendMessage(chipText)
                                }
                            ) {
                                Text(
                                    text = chipText,
                                    fontSize = 12.sp,
                                    color = MessengerDarkText,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // Input Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text(
                                    text = if (isBangladesh) "মেসেঞ্জারে বার্তা লিখুন..." else "Type a message...",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 46.dp, max = 110.dp)
                                .testTag("chat_input_field"),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MessengerBlue,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = CircleShape,
                            color = if (inputText.isNotBlank()) MessengerBlue else Color(0xFFE2E8F0),
                            modifier = Modifier
                                .size(46.dp)
                                .clickable(enabled = inputText.isNotBlank()) {
                                    val t = inputText.trim()
                                    if (t.isNotEmpty()) {
                                        MessengerAudioEffects.playMessageSent()
                                        MessengerAudioEffects.playHapticFeedback(context)
                                        onSendMessage(t)
                                        inputText = ""
                                    }
                                }
                                .testTag("chat_send_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = if (inputText.isNotBlank()) Color.White else Color(0xFF94A3B8),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Messenger encrypted & synced security notice
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangladesh)
                                "গ্রাহক ও কারিগরের সরাসরি বার্তালাপ। আপনার অ্যাকাউন্ট ডিলিট হলেও আগের ইমেইল দিয়ে লগইন করলে সমস্ত চ্যাট ফিরে পাবেন।"
                            else
                                "Direct conversation. Reinstalling or logging in with your email restores all chat history.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 15.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(messages, key = { it.id }) { msg ->
                val isMe = msg.senderId == myId || (myEmail.isNotBlank() && msg.senderEmail.equals(myEmail, ignoreCase = true))
                ChatMessageBubble(
                    message = msg,
                    isMe = isMe,
                    isBangladesh = isBangladesh,
                    onDeleteMessage = { onDeleteMessage?.invoke(msg.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    if (showDeleteChatConfirm && onDeleteChat != null) {
        AlertDialog(
            onDismissRequest = { showDeleteChatConfirm = false },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEE2E2),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            title = {
                Text(
                    text = if (isBangladesh) "এই চ্যাট ডিলিট করতে চান?" else "Delete this chat?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Text(
                    text = if (isBangladesh)
                        "আপনি কি এই প্রোফাইল এবং এর সমস্ত বার্তা ডিলিট করতে চান? আপনি ডিলিট না করা পর্যন্ত এটি সারা জীবনের জন্য মেসেঞ্জারে সংরক্ষিত থাকবে।"
                    else
                        "Do you want to delete this chat and all messages? Unless deleted, this conversation remains saved permanently.",
                    fontSize = 13.5.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteChatConfirm = false
                        onDeleteChat()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    modifier = Modifier.testTag("confirm_delete_current_chat_btn")
                ) {
                    Text(if (isBangladesh) "ডিলিট করুন" else "Delete")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteChatConfirm = false },
                    modifier = Modifier.testTag("cancel_delete_current_chat_btn")
                ) {
                    Text(if (isBangladesh) "বাতিল" else "Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatMessageBubble(
    message: ChatMessageEntity,
    isMe: Boolean,
    isBangladesh: Boolean = true,
    onDeleteMessage: () -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeStr = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }
    val isCallLog = message.messageText.startsWith("📞") || message.messageText.contains("ভয়েস কল")

    if (isCallLog) {
        // Voice Call Record inside Chat History (visible in both profiles)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF1F5F9),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                modifier = Modifier
                    .widthIn(max = 330.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { showDeleteConfirm = true }
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Voice Call Record",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = message.messageText,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B),
                        lineHeight = 17.sp
                    )
                }
            }
        }
    } else {
        // Standard Message Bubble with Long-Press Delete ("মেসেজ সিম সিস্টেমে ডিলেট করবে")
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
            if (!isMe) {
                Text(
                    text = message.senderName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                )
            }

            Surface(
                shape = if (isMe) {
                    RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 4.dp)
                } else {
                    RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp)
                },
                color = if (isMe) MessengerBlue else MessengerLightBg,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { showDeleteConfirm = true }
                    )
            ) {
                Text(
                    text = message.messageText,
                    color = if (isMe) Color.White else MessengerDarkText,
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }

            Row(
                modifier = Modifier.padding(top = 2.dp, start = 6.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeStr,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                if (isMe) {
                    Spacer(modifier = Modifier.width(4.dp))
                    if (message.isRead) {
                        // Message seen by receiver: turns into circular seen icon
                        Box(
                            modifier = Modifier
                                .size(13.dp)
                                .clip(CircleShape)
                                .background(MessengerBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Seen",
                                tint = Color.White,
                                modifier = Modifier.size(9.dp)
                            )
                        }
                    } else {
                        // Sent / Delivered: Two checkmarks (DoneAll / ✓✓)
                        Icon(
                            Icons.Default.DoneAll,
                            contentDescription = "Sent",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }

    // SIM-style individual message deletion dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEE2E2),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
            title = {
                Text(
                    text = if (isBangladesh) "মেসেজটি ডিলিট করতে চান?" else "Delete this message?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = if (isBangladesh)
                        "এই মেসেজটি আপনার চ্যাট হিস্ট্রি থেকে স্থায়ীভাবে মুছে ফেলা হবে।"
                    else
                        "This message will be permanently deleted from the chat history.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteMessage()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(if (isBangladesh) "ডিলিট করুন" else "Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text(if (isBangladesh) "বাতিল" else "Cancel")
                }
            }
        )
    }
}
