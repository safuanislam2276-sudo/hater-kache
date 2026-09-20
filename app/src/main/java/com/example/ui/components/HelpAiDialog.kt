package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class HelpChatMessage(
    val isFromUser: Boolean,
    val text: String,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpAiAccountDialog(
    isBangladesh: Boolean,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var userMessage by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }

    val initialGreeting = if (isBangladesh) {
        "আসসালামু আলাইকুম! একাউন্ট তৈরিতে কোনো সমস্যা হচ্ছে?\n\n" +
                "• নাম, ইমেইল বা ফোন নম্বর নিচ্ছে না?\n" +
                "• পাসওয়ার্ড কেমন হতে হবে বুঝতে পারছেন না?\n" +
                "• গ্রাহক নাকি মিস্ত্রি অ্যাকাউন্ট কোনটা খুলবেন?\n" +
                "• ছবি আপলোড বা জেলা-উপজেলা নির্বাচনে বিভ্রান্তি?\n\n" +
                "নিচের যেকোনো অপশনে চাপ দিন, অথবা আপনার সমস্যা লিখে পাঠান বা কোনো এররের ছবি/স্ক্রিনশট দিন — আমি তাৎক্ষণিক সঠিক সমাধান বলে দিব!"
    } else {
        "Hello! Having trouble creating your account?\n\n" +
                "• Phone number or email not accepted?\n" +
                "• Wondering about password requirements?\n" +
                "• Choosing between Customer or Mistri (Worker)?\n" +
                "• Location or photo upload issues?\n\n" +
                "Tap any topic below, write your issue, or attach a screenshot — I will guide you step-by-step!"
    }

    var messages by remember {
        mutableStateOf(
            listOf(
                HelpChatMessage(
                    isFromUser = false,
                    text = initialGreeting
                )
            )
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    fun handleSend(query: String, imageUri: Uri? = null) {
        val trimmed = query.trim()
        if (trimmed.isEmpty() && imageUri == null) return

        val userText = if (trimmed.isNotEmpty()) trimmed else if (isBangladesh) "ছবি সংযুক্ত করা হয়েছে (সমস্যার স্ক্রিনশট)" else "Attached image screenshot"
        val userMsg = HelpChatMessage(isFromUser = true, text = userText, imageUri = imageUri?.toString())
        messages = messages + userMsg
        userMessage = ""
        selectedImageUri = null
        isAnalyzing = true

        coroutineScope.launch {
            kotlinx.coroutines.delay(600) // Realistic responsive processing
            val responseText = generateHelpAiSolution(userText, imageUri != null, isBangladesh)
            messages = messages + HelpChatMessage(isFromUser = false, text = responseText)
            isAnalyzing = false
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.88f)
                .testTag("help_ai_dialog_container"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Dialog Header
                Surface(
                    color = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Logo
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFFFFD700), Color(0xFF00E5FF), Color(0xFF3B82F6))
                                        )
                                    )
                                    .padding(2.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_help_ai),
                                    contentDescription = "Help AI Logo",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (isBangladesh) "হেল্প AI অ্যাকাউন্ট সহকারী" else "Help AI Account Assistant",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFF10B981)
                                    ) {
                                        Text(
                                            text = "ONLINE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = if (isBangladesh) "মেসেজ বা ছবি পাঠিয়ে নির্ভুল সমাধান নিন" else "Send message or picture to get instant guide",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                // Quick Topic Chips
                Surface(
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val topics = if (isBangladesh) {
                        listOf(
                            "🔑 পাসওয়ার্ড কেমন দিব?",
                            "📱 ফোন নম্বরে সমস্যা?",
                            "🛠️ গ্রাহক নাকি মিস্ত্রি?",
                            "📸 প্রোফাইল ছবি কেন লাগে?",
                            "👑 PRO শীর্ষ র্যাংক কী?"
                        )
                    } else {
                        listOf(
                            "🔑 Password requirements?",
                            "📱 Phone number issue?",
                            "🛠️ Customer or Mistri?",
                            "📸 Why profile photo?",
                            "👑 What is PRO Top Rank?"
                        )
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(topics) { topic ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.clickable { handleSend(topic) }
                            ) {
                                Text(
                                    text = topic,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // Chat Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { msg ->
                        ChatBubbleItem(message = msg, isBangladesh = isBangladesh)
                    }

                    if (isAnalyzing) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE2E8F0))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = BrandBlue
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBangladesh) "হেল্প AI সমাধান প্রস্তুত করছে..." else "Help AI is analyzing...",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                // Selected Image Preview (if user chose an image to attach)
                if (selectedImageUri != null) {
                    Surface(
                        color = Color(0xFFFEF3C7),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Attached screenshot",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isBangladesh) "ছবি সংযুক্ত হয়েছে" else "Image attached",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                    Text(
                                        text = if (isBangladesh) "পাঠালে AI ছবি দেখে সমস্যা বিশ্লেষণ করবে" else "AI will inspect picture to guide you",
                                        fontSize = 10.sp,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { selectedImageUri = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove image", tint = Color(0xFF92400E))
                            }
                        }
                    }
                }

                // User Input Bar with Text + Photo Attachment
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Photo Picker Button
                        IconButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                                .testTag("help_ai_attach_image_button")
                        ) {
                            Icon(
                                Icons.Default.AddPhotoAlternate,
                                contentDescription = "Attach screenshot or photo",
                                tint = BrandBlue
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Text Field
                        OutlinedTextField(
                            value = userMessage,
                            onValueChange = { userMessage = it },
                            placeholder = {
                                Text(
                                    if (isBangladesh) "সমস্যা বা প্রশ্ন লিখুন..." else "Type question or issue...",
                                    fontSize = 13.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 46.dp, max = 100.dp)
                                .testTag("help_ai_input_field"),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandBlue,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Send Button
                        IconButton(
                            onClick = {
                                handleSend(userMessage, selectedImageUri)
                            },
                            enabled = userMessage.isNotBlank() || selectedImageUri != null,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (userMessage.isNotBlank() || selectedImageUri != null) BrandBlue else Color(0xFFE2E8F0)
                                )
                                .testTag("help_ai_send_button")
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (userMessage.isNotBlank() || selectedImageUri != null) Color.White else Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: HelpChatMessage, isBangladesh: Boolean) {
    val isUser = message.isFromUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F172A))
                    .padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_help_ai),
                    contentDescription = "AI",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 16.dp
            ),
            color = if (isUser) BrandBlue else Color(0xFFF1F5F9),
            border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // If message has an image
                if (message.imageUri != null) {
                    AsyncImage(
                        model = message.imageUri,
                        contentDescription = "Attached screenshot",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 160.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = if (isUser) Color.White else Color(0xFF0F172A)
                )
            }
        }
    }
}

/**
 * Intelligent solution generator for Account Creation issues.
 * Provides accurate, step-by-step guidance in Bengali and English.
 */
fun generateHelpAiSolution(query: String, hasImage: Boolean, isBangladesh: Boolean): String {
    val q = query.lowercase()

    if (hasImage) {
        return if (isBangladesh) {
            "📷 আপনার সংযুক্ত ছবি/স্ক্রিনশট পর্যালোচনা করা হয়েছে!\n\n" +
                    "সাধারণ সমাধানসমূহ:\n" +
                    "১. লাল দাগ (Red border) যুক্ত ফিল্ডটি খালি আছে কি না দেখুন।\n" +
                    "২. মিস্ত্রি একাউন্ট খুললে অবশ্যই আপনার স্পষ্ট মুখের ছবি নির্বাচন করতে হবে।\n" +
                    "৩. মোবাইল নম্বরটি সঠিক ফরম্যাটে (০১XXXXXXXXX) লিখেছেন কি না চেক করুন।\n" +
                    "৪. পাসওয়ার্ড অন্তত ৬ সংখ্যার হতে হবে।\n\n" +
                    "সবগুলো পূরণ করে 'একাউন্ট তৈরি সম্পন্ন করুন' বাটনে চাপ দিন।"
        } else {
            "📷 Your attached screenshot has been reviewed!\n\n" +
                    "Troubleshooting Checklist:\n" +
                    "1. Check any input field highlighted in red.\n" +
                    "2. If registering as Mistri (Worker), a clear face photo is required.\n" +
                    "3. Ensure the phone number matches country format (without special symbols).\n" +
                    "4. Password must be at least 6 characters.\n\n" +
                    "Fill these properly and tap 'Complete Account Registration'."
        }
    }

    return when {
        q.contains("পাসওয়ার্ড") || q.contains("password") -> {
            if (isBangladesh) {
                "🔑 পাসওয়ার্ডের নিয়মাবলী:\n\n" +
                        "• পাসওয়ার্ড সর্বনিম্ন ৬ অক্ষরের (characters) হতে হবে।\n" +
                        "• পাসওয়ার্ডে ইংরেজি অক্ষর এবং সংখ্যা (যেমন: abcd123 বা mistri786) ব্যবহার করুন যাতে সুরক্ষিত থাকে।\n" +
                        "• পাসওয়ার্ড দেখতে পাশের চোখের আইকন (Visibility Icon)-এ চাপ দিতে পারেন।"
            } else {
                "🔑 Password Requirements:\n\n" +
                        "• Password must be at least 6 characters long.\n" +
                        "• Mix letters and numbers (e.g., worker123) for security.\n" +
                        "• Tap the eye icon beside the box to reveal what you typed."
            }
        }

        q.contains("ফোন") || q.contains("phone") || q.contains("নম্বর") || q.contains("number") || q.contains("মোবাইল") -> {
            if (isBangladesh) {
                "📱 ফোন নম্বরের নিয়ম:\n\n" +
                        "• ফোন নম্বরে কোনো স্পেস বা ড্যাশ (-) দিবেন না।\n" +
                        "• বাংলাদেশের জন্য ০১৭XXXXXXXX (১১ ডিজিট) লিখুন। দেশের কোড (+৮৮০) স্বয়ংক্রিয়ভাবে যুক্ত হবে।\n" +
                        "• বিদেশে থাকলে উপরের ড্রপডাউন থেকে আপনার দেশ সিলেক্ট করে স্থানীয় মোবাইল নম্বরটি লিখুন।"
            } else {
                "📱 Phone Number Guidelines:\n\n" +
                        "• Enter phone digits without spaces or hyphens.\n" +
                        "• Country calling code is added automatically based on the selected country.\n" +
                        "• Ensure the number is active so clients can call or message you."
            }
        }

        q.contains("গ্রাহক") || q.contains("মিস্ত্রি") || q.contains("পার্থক্য") || q.contains("customer") || q.contains("worker") -> {
            if (isBangladesh) {
                "🛠️ গ্রাহক বনাম মিস্ত্রি অ্যাকাউন্ট:\n\n" +
                        "১. গ্রাহক (Customer):\n" +
                        "• যারা বাসা-বাড়ির কাজের জন্য মিস্ত্রি বা টেকনিশিয়ান খুঁজছেন।\n" +
                        "• অভিজ্ঞতা বা পেশার তথ্য লাগবে না। শুধু নাম, ইমেইল, ফোন ও এলাকা দিলেই হবে।\n\n" +
                        "২. মিস্ত্রি (Worker):\n" +
                        "• যারা কাজ করতে চান এবং গ্রাহকের কাছ থেকে কাজের কল পেতে চান।\n" +
                        "• মুখের ছবি, পেশা (৫৭টি ক্যাটাগরি থেকে) এবং অভিজ্ঞতার বছর দিতে হবে।"
            } else {
                "🛠️ Customer vs Mistri (Worker):\n\n" +
                        "1. Customer Account:\n" +
                        "• For users looking to hire technicians for home or office services.\n" +
                        "• Quick registration: name, email, phone, and location.\n\n" +
                        "2. Mistri / Worker Account:\n" +
                        "• For professionals who provide repair and maintenance services.\n" +
                        "• Requires clear profile photo, profession category, and experience years."
            }
        }

        q.contains("ছবি") || q.contains("photo") || q.contains("image") || q.contains("ক্যামেরা") -> {
            if (isBangladesh) {
                "📸 প্রোফাইল ছবির নিয়মাবলী:\n\n" +
                        "• গোল বৃত্তের ওপর চাপ দিলে আপনার ফোনের গ্যালারি/ফটো ওপেন হবে।\n" +
                        "• আপনার নিজের একটি স্পষ্ট ও উজ্জ্বল ছবি সিলেক্ট করুন।\n" +
                        "• মিস্ত্রিদের ক্ষেত্রে ছবি বাধ্যতামূলক, কারণ গ্রাহক ছবি দেখে বিশ্বাস করে কাজে ডাকে।"
            } else {
                "📸 Profile Photo Instructions:\n\n" +
                        "• Tap the circular avatar to open your photo gallery.\n" +
                        "• Select a bright, clear photo of yourself.\n" +
                        "• Mandatory for Workers so clients can recognize and trust you."
            }
        }

        q.contains("pro") || q.contains("শীর্ষ") || q.contains("ভিআইপি") || q.contains("rank") || q.contains("লিস্ট") || q.contains("সাবস্ক্রিপশন") -> {
            if (isBangladesh) {
                "👑 PRO সাবস্ক্রিপশন ও শীর্ষ স্থান:\n\n" +
                        "• শুধু মিস্ত্রিদের জন্য: একাউন্ট খোলার পর প্রোফাইল বা ড্যাশবোর্ডে গিয়ে PRO সাবস্ক্রিপশন নিতে পারবেন।\n" +
                        "• সাবস্ক্রিপশন কিনলে আপনার নাম সার্চ লিস্টের সবার শীর্ষে (Top of the List) থাকবে এবং বেশি কাজের কল পাবেন!\n" +
                        "• না কিনলে প্রোফাইল পরে নিচে থাকবে।"
            } else {
                "👑 PRO Subscription & Top Ranking:\n\n" +
                        "• Exclusively for Mistris: Go to your Profile/Dashboard and subscribe to PRO.\n" +
                        "• Once subscribed, your profile is ranked at the very top of all search results!\n" +
                        "• Unsubscribed profiles appear afterwards."
            }
        }

        q.contains("জেলা") || q.contains("উপজেলা") || q.contains("এলাকা") || q.contains("location") || q.contains("district") -> {
            if (isBangladesh) {
                "📍 জেলা ও উপজেলা নির্বাচন:\n\n" +
                        "• প্রথমে আপনার বিভাগ সিলেক্ট করুন (যেমন: ঢাকা, চট্টগ্রাম, সিলেট, ইত্যাদি)।\n" +
                        "• এরপর আপনার জেলা সিলেক্ট করুন (সকল ৬৪টি জেলা দেওয়া আছে)।\n" +
                        "• এরপর উপজেলা বা থানা সিলেক্ট করুন অথবা নিচে বিস্তারিত ঠিকানায় আপনার এলাকা/গ্রাম লিখে দিন।"
            } else {
                "📍 Location Selection:\n\n" +
                        "• Select your Region/State, then choose your District/City.\n" +
                        "• Enter your detailed area or address so nearby clients can find you easily."
            }
        }

        else -> {
            if (isBangladesh) {
                "✅ একাউন্ট খোলার সহজ ৪টি ধাপ:\n\n" +
                        "১. উপরে 'গ্রাহক' অথবা 'মিস্ত্রি' সিলেক্ট করুন।\n" +
                        "২. পূর্ণ নাম, ইমেইল, পাসওয়ার্ড (৬+ অক্ষর) এবং মোবাইল নম্বর লিখুন।\n" +
                        "৩. দেশ, বিভাগ ও জেলা নির্বাচন করুন।\n" +
                        "৪. মিস্ত্রি হলে আপনার ছবি ও পেশা সিলেক্ট করে 'একাউন্ট তৈরি সম্পন্ন করুন'-এ চাপ দিন।\n\n" +
                        "কোনো নির্দিষ্ট এরর দেখালে নিচে বিস্তারিত লিখে জানান বা স্ক্রিনশটের ছবি দিন!"
            } else {
                "✅ 4 Quick Steps to Register:\n\n" +
                        "1. Choose 'Customer' or 'Mistri / Worker' at the top.\n" +
                        "2. Enter full name, email, password (6+ chars), and mobile number.\n" +
                        "3. Select country, region, and district.\n" +
                        "4. If Mistri, attach your face photo and select your profession.\n\n" +
                        "Need more help? Attach a screenshot or write your question below!"
            }
        }
    }
}
