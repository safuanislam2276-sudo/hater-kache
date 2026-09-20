package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.InAppMessengerCallDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.ui.graphics.Color.White
                ) {
                    HaterKacheApp()
                }
            }
        }
    }
}

@Composable
fun HaterKacheApp(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // State flows collected safely with lifecycle
    val globalCountry by viewModel.globalCountry.collectAsStateWithLifecycle()
    val searchFilters by viewModel.searchFilters.collectAsStateWithLifecycle()
    val filteredMistris by viewModel.filteredMistris.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentMistriProfile by viewModel.currentMistriProfile.collectAsStateWithLifecycle()

    val liveWorkersCount by viewModel.liveWorkersCount.collectAsStateWithLifecycle()
    val liveCustomerCount by viewModel.liveCustomerCount.collectAsStateWithLifecycle()
    val liveReviewCount by viewModel.liveReviewCount.collectAsStateWithLifecycle()

    val selectedMistri by viewModel.selectedMistri.collectAsStateWithLifecycle()
    val selectedMistriReviews by viewModel.selectedMistriReviews.collectAsStateWithLifecycle()

    val isAdminUnlocked by viewModel.isAdminUnlocked.collectAsStateWithLifecycle()
    val totalWorkersCount by viewModel.totalWorkersCount.collectAsStateWithLifecycle()
    val blockedWorkersCount by viewModel.blockedWorkersCount.collectAsStateWithLifecycle()
    val proWorkersCount by viewModel.proWorkersCount.collectAsStateWithLifecycle()
    val allMistrisList by viewModel.allMistrisList.collectAsStateWithLifecycle()
    val pendingSubscriptions by viewModel.pendingSubscriptions.collectAsStateWithLifecycle()
    val adminSettings by viewModel.adminSettings.collectAsStateWithLifecycle()
    val bannerState by viewModel.bannerState.collectAsStateWithLifecycle()
    val viewsByCountry by viewModel.viewsByCountry.collectAsStateWithLifecycle()
    val signupsByCountry by viewModel.signupsByCountry.collectAsStateWithLifecycle()
    val unreadChatCount by viewModel.unreadChatCount.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                durationMillis = 1100L, // 1.1s loading (0.5s - 1.5s range)
                onTimeout = {
                    navController.navigate("auth") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                currentGlobalCountry = globalCountry,
                searchFilters = searchFilters,
                filteredMistris = filteredMistris,
                currentUser = currentUser,
                liveWorkersCount = liveWorkersCount,
                liveCustomersCount = liveCustomerCount,
                liveReviewsCount = liveReviewCount,
                bannerState = bannerState,
                onCountryChange = { newCountry ->
                    viewModel.setGlobalCountry(newCountry)
                    // If user hasn't explicitly set a custom independent search filter, default search country too
                    viewModel.updateSearchCountry(newCountry.nameBn)
                    val isBn = newCountry.code == "BD" || newCountry.nameEn.equals("Bangladesh", ignoreCase = true)
                    val curProf = viewModel.searchFilters.value.profession
                    if (curProf.isNotEmpty()) {
                        viewModel.updateSearchProfession(com.example.data.country.CountryRepository.getProfessionDisplayName(curProf, isBn))
                    }
                },
                onSearchQueryChange = { query ->
                    viewModel.updateSearchQuery(query)
                },
                onSearchFilterApply = { country, division, district, profession ->
                    viewModel.updateSearchCountry(country)
                    viewModel.updateSearchDivision(division)
                    viewModel.updateSearchDistrict(district)
                    viewModel.updateSearchProfession(profession)
                },
                onResetSearchFilters = {
                    viewModel.resetSearchFilters()
                },
                onSelectMistri = { mistri ->
                    viewModel.selectMistri(mistri.id)
                    navController.navigate("detail")
                },
                onCallClick = { mistri ->
                    viewModel.callMistri(context, mistri)
                },
                onWaClick = { mistri ->
                    viewModel.openWhatsApp(context, mistri)
                },
                onChatClick = { mistri ->
                    viewModel.openChatWithMistri(mistri)
                    navController.navigate("chat")
                },
                unreadChatCount = unreadChatCount,
                onNavigateToMessenger = {
                    navController.navigate("messenger")
                },
                onNavigateToAuth = {
                    navController.navigate("auth")
                },
                onNavigateToDashboard = {
                    if (currentMistriProfile != null) {
                        navController.navigate("dashboard")
                    } else {
                        Toast.makeText(context, "কারিগর হিসেবে লগইন করা নেই", Toast.LENGTH_SHORT).show()
                        navController.navigate("auth")
                    }
                },
                onLogout = {
                    viewModel.logout()
                    Toast.makeText(context, "লগআউট সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show()
                },
                onUpdateCustomerProfile = { name, phone, division, district, address, img, onDone, onError ->
                    viewModel.updateCustomerProfile(
                        name = name,
                        phone = phone,
                        division = division,
                        district = district,
                        address = address,
                        profileImageUri = img,
                        onSuccess = {
                            Toast.makeText(context, "প্রোফাইল সফলভাবে আপডেট হয়েছে!", Toast.LENGTH_SHORT).show()
                            onDone()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                            onError(err)
                        }
                    )
                }
            )
        }

        composable("detail") {
            val mistri = selectedMistri
            if (mistri != null) {
                MistriDetailScreen(
                    mistri = mistri,
                    reviews = selectedMistriReviews,
                    currentUser = currentUser,
                    onBack = { navController.popBackStack() },
                    onCallClick = { viewModel.callMistri(context, mistri) },
                    onWaClick = { viewModel.openWhatsApp(context, mistri) },
                    onChatClick = {
                        viewModel.openChatWithMistri(mistri)
                        navController.navigate("chat")
                    },
                    onSubmitReview = { rating, text, onDone ->
                        viewModel.submitReview(
                            mistriId = mistri.id,
                            rating = rating,
                            reviewText = text,
                            onSuccess = {
                                Toast.makeText(context, "রিভিউ সফলভাবে জমা হয়েছে!", Toast.LENGTH_SHORT).show()
                                onDone()
                            },
                            onError = { err ->
                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onNavigateToAuth = {
                        navController.navigate("auth")
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }

        composable("auth") {
            AuthScreen(
                initialCountry = globalCountry,
                onCountryChanged = { newCountry ->
                    viewModel.setGlobalCountry(newCountry)
                    viewModel.updateSearchCountry(newCountry.nameBn)
                },
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate("home")
                    }
                },
                onContinueAsGuest = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onLoginSubmit = { email, password, onDone, onError ->
                    viewModel.login(
                        email = email,
                        password = password,
                        onSuccess = {
                            Toast.makeText(context, "লগইন সফল হয়েছে!", Toast.LENGTH_SHORT).show()
                            onDone()
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        },
                        onError = onError
                    )
                },
                onGoogleAuth = { email, name, photo, country, countryCode, div, dist, userType, onDone, onError ->
                    viewModel.loginWithGoogle(
                        email = email,
                        name = name,
                        photoUrl = photo,
                        country = country,
                        countryCode = countryCode,
                        division = div,
                        district = dist,
                        userType = userType,
                        onSuccess = {
                            Toast.makeText(context, "গুগল সাইন-ইন সফল হয়েছে!", Toast.LENGTH_SHORT).show()
                            onDone()
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        },
                        onError = onError
                    )
                },
                onRegisterCustomer = { name, email, phone, password, country, countryCode, division, district, address, img, onDone, onError ->
                    viewModel.registerCustomer(
                        name = name,
                        email = email,
                        phone = phone,
                        password = password,
                        country = country,
                        countryCode = countryCode,
                        division = division,
                        district = district,
                        address = address,
                        profileImageUri = img,
                        onSuccess = {
                            Toast.makeText(context, "গ্রাহক অ্যাকাউন্ট তৈরি হয়েছে!", Toast.LENGTH_SHORT).show()
                            onDone()
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        },
                        onError = onError
                    )
                },
                onRegisterWorker = { name, email, phone, password, country, countryCode, division, district, address, prof, extraCats, exp, bio, skills, img, onDone, onError ->
                    viewModel.registerWorker(
                        name = name,
                        email = email,
                        phone = phone,
                        password = password,
                        country = country,
                        countryCode = countryCode,
                        division = division,
                        district = district,
                        address = address,
                        profession = prof,
                        extraCategories = extraCats,
                        experienceYears = exp,
                        bio = bio,
                        skills = skills,
                        profileImageUri = img,
                        onSuccess = {
                            Toast.makeText(context, "কারিগর অ্যাকাউন্ট তৈরি হয়েছে!", Toast.LENGTH_SHORT).show()
                            onDone()
                            navController.navigate("dashboard")
                        },
                        onError = onError
                    )
                }
            )
        }

        composable("dashboard") {
            val mistri = currentMistriProfile
            if (mistri != null) {
                MistriDashboardScreen(
                    mistri = mistri,
                    adminSettings = adminSettings,
                    onBack = { navController.popBackStack() },
                    onNavigateToMessenger = { navController.navigate("messenger") },
                    onPurchaseSubscription = { plan, amount, method, trxId, onDone, onError ->
                        viewModel.purchaseSubscription(
                            mistri = mistri,
                            planName = plan,
                            amount = amount,
                            paymentMethod = method,
                            transactionId = trxId,
                            onSuccess = {
                                Toast.makeText(context, "পেমেন্ট রিকোয়েস্ট সফলভাবে পাঠানো হয়েছে! অ্যাডমিন অনুমোদনের পর PRO চালু হবে।", Toast.LENGTH_LONG).show()
                                onDone()
                            },
                            onError = { err ->
                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                onError(err)
                            }
                        )
                    },
                    onUpdateProfile = { name, phone, prof, extraCats, div, dist, addr, exp, bio, skills, onDone ->
                        viewModel.updateMistriProfile(
                            mistri = mistri,
                            name = name,
                            phone = phone,
                            profession = prof,
                            extraCategories = extraCats,
                            division = div,
                            district = dist,
                            address = addr,
                            experienceYears = exp,
                            bio = bio,
                            skills = skills,
                            onSuccess = {
                                Toast.makeText(context, "প্রোফাইল তথ্য সফলভাবে সেভ হয়েছে!", Toast.LENGTH_SHORT).show()
                                onDone()
                            }
                        )
                    }
                )
            } else {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "কারিগর প্রোফাইল পাওয়া যায়নি", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }

        composable("messenger") {
            val conversations by viewModel.userConversations.collectAsStateWithLifecycle()
            val allWorkers by viewModel.allMistrisList.collectAsStateWithLifecycle()
            val allUsers by viewModel.allUsersList.collectAsStateWithLifecycle()
            MessengerListScreen(
                conversations = conversations,
                currentUser = currentUser,
                allWorkers = allWorkers,
                allUsers = allUsers,
                onBack = { navController.popBackStack() },
                onOpenConversation = { conv ->
                    viewModel.openChatConversation(conv)
                    navController.navigate("chat")
                },
                onOpenUserChat = { targetId, targetName, targetEmail, targetRole, phone, profession, imageUri ->
                    viewModel.openChatWithUser(
                        targetId = targetId,
                        targetName = targetName,
                        targetEmail = targetEmail,
                        targetRole = targetRole,
                        phone = phone,
                        profession = profession,
                        imageUri = imageUri
                    )
                    navController.navigate("chat")
                },
                onBrowseMistris = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onSaveAutoReply = { message ->
                    viewModel.saveUserAutoReply(message)
                },
                getAutoReply = {
                    viewModel.getUserAutoReply()
                },
                onDeleteConversation = { convId ->
                    viewModel.deleteConversation(convId)
                },
                onStartCall = { conv ->
                    viewModel.startInAppCall(
                        name = conv.otherUserName,
                        role = conv.otherUserRole,
                        phone = "",
                        profession = null,
                        imageUri = null,
                        targetUserId = conv.otherUserId,
                        targetEmail = conv.otherUserEmail,
                        conversationId = conv.conversationId
                    )
                }
            )
        }

        composable("chat") {
            val messages by viewModel.activeChatMessages.collectAsStateWithLifecycle()
            val recipient by viewModel.activeChatRecipient.collectAsStateWithLifecycle()
            MessengerChatScreen(
                recipient = recipient,
                messages = messages,
                currentUser = currentUser,
                onBack = { navController.popBackStack() },
                onSendMessage = { text ->
                    viewModel.sendChatMessage(text)
                },
                onCallClick = {
                    val target = recipient ?: return@MessengerChatScreen
                    viewModel.startInAppCall(
                        name = target.name,
                        role = target.role,
                        phone = target.phone.orEmpty(),
                        profession = target.profession,
                        imageUri = target.imageUri,
                        targetUserId = target.id,
                        targetEmail = target.email,
                        conversationId = viewModel.activeConversationId.value
                    )
                },
                onWaClick = {
                    val phone = recipient?.phone
                    if (!phone.isNullOrBlank()) {
                        val cleanPhone = phone.filter { it.isDigit() }
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://wa.me/$cleanPhone"))
                        context.startActivity(intent)
                    }
                },
                onDeleteMessage = { msgId ->
                    viewModel.deleteChatMessage(msgId)
                },
                onDeleteChat = {
                    val convId = viewModel.activeConversationId.value
                    if (convId != null) {
                        viewModel.deleteConversation(convId)
                    }
                    navController.popBackStack()
                }
            )
        }
    }

    // Fullscreen in-app VoIP Messenger Voice Call Dialog with Loudspeaker
    val activeCallState by viewModel.activeCallState.collectAsStateWithLifecycle()
    if (activeCallState != null) {
        InAppMessengerCallDialog(
            callState = activeCallState!!,
            onToggleMute = { viewModel.toggleCallMute() },
            onToggleSpeaker = { viewModel.toggleCallSpeaker() },
            onEndCall = { viewModel.endInAppCall() }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}

