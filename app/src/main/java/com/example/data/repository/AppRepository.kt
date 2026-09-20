package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.remote.FirebaseAuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class AppRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()
    private val mistriDao = db.mistriDao()
    private val reviewDao = db.reviewDao()
    private val subscriptionDao = db.subscriptionDao()
    private val adminSettingDao = db.adminSettingDao()
    private val analyticsDao = db.analyticsDao()
    private val chatDao = db.chatDao()
    private val firebaseAuth = FirebaseAuthService()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    val activeMistrisProFirst: Flow<List<MistriEntity>> = mistriDao.getActiveMistrisProFirst()
    val allMistris: Flow<List<MistriEntity>> = mistriDao.getAllMistris()

    // Live dynamic counters
    val liveWorkersCount: Flow<Int> = mistriDao.getActiveWorkerCountFlow()
    val liveCustomerCount: Flow<Int> = userDao.getCustomerCountFlow()
    val liveReviewCount: Flow<Int> = reviewDao.getTotalReviewCountFlow()

    // Admin overview counts
    val totalWorkersCount: Flow<Int> = mistriDao.getTotalWorkerCountFlow()
    val blockedWorkersCount: Flow<Int> = mistriDao.getBlockedWorkerCountFlow()
    val proWorkersCount: Flow<Int> = mistriDao.getProWorkerCountFlow()
    val pendingSubscriptions: Flow<List<SubscriptionEntity>> = subscriptionDao.getPendingSubscriptions()
    val allSubscriptions: Flow<List<SubscriptionEntity>> = subscriptionDao.getAllSubscriptions()
    val adminSettingsFlow: Flow<List<AdminSettingEntity>> = adminSettingDao.getAllSettingsFlow()
    val allUsersFlow: Flow<List<UserEntity>> = userDao.getAllUsersFlow()
    fun searchUsers(query: String): Flow<List<UserEntity>> = userDao.searchUsersFlow(query)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialSettings()
            seedOwnerAdmin()
            seedInitialWorkers()
            seedInitialChats()
            logAnalyticsEvent("app_open", "Bangladesh")
        }
    }

    private suspend fun seedInitialChats() {
        val welcomeMsg = ChatMessageEntity(
            id = "chat-seed-1",
            conversationId = "conv_demo_electrician",
            senderId = "m-seed-1",
            senderName = "মোঃ রফিকুল ইসলাম",
            senderEmail = "rafiq.electrician@gmail.com",
            senderRole = "MISTRI",
            receiverId = "u-customer-demo",
            receiverName = "তানভীর আহমেদ",
            receiverEmail = "tanvir.customer@gmail.com",
            receiverRole = "CUSTOMER",
            messageText = "আসসালামু আলাইকুম! আপনার বাসার যেকোনো বৈদ্যুতিক কাজ, ওয়্যারিং বা সার্কিট মেরামতের জন্য সরাসরি মেসেজ দিন। আমি দ্রুত সেবা দিতে প্রস্তুত।",
            timestamp = System.currentTimeMillis() - 3600000L * 2,
            isRead = false,
            userEmail = "tanvir.customer@gmail.com"
        )
        chatDao.insertMessage(welcomeMsg)

        val welcomeMsg2 = ChatMessageEntity(
            id = "chat-seed-2",
            conversationId = "conv_demo_support",
            senderId = "m-seed-2",
            senderName = "আব্দুর রহিম (প্লাম্বার)",
            senderEmail = "rahim.plumber@gmail.com",
            senderRole = "MISTRI",
            receiverId = "u-customer-demo",
            receiverName = "তানভীর আহমেদ",
            receiverEmail = "tanvir.customer@gmail.com",
            receiverRole = "CUSTOMER",
            messageText = "নমস্কার! পানির লাইন, পাইপ লিকেজ বা স্যানিটারি কাজের জন্য যখন প্রয়োজন আমাকে মেসেঞ্জারে জানাতে পারেন।",
            timestamp = System.currentTimeMillis() - 3600000L * 5,
            isRead = true,
            userEmail = "tanvir.customer@gmail.com"
        )
        chatDao.insertMessage(welcomeMsg2)
    }

    private suspend fun seedOwnerAdmin() {
        val ownerEmail = "safuanislam2276@gmail.com"
        val existing = userDao.getUserByEmail(ownerEmail)
        if (existing == null) {
            val adminUser = UserEntity(
                id = "u-owner-safuan",
                email = ownerEmail,
                name = "Safuan Islam (Owner)",
                phone = "+8801700000000",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "ঢাকা",
                district = "ঢাকা",
                address = "Headquarters",
                userType = "ADMIN"
            )
            userDao.insertUser(adminUser)
        }
    }

    private suspend fun seedInitialSettings() {
        val theme = adminSettingDao.getSettingValue("app_theme_color")
        if (theme == null) {
            adminSettingDao.setSetting(AdminSettingEntity("app_theme_color", "#0284C7", "App Primary Theme Color"))
            adminSettingDao.setSetting(AdminSettingEntity("bkash_number", "01812345678", "bKash মার্চেন্ট/ব্যক্তিগত নম্বর"))
            adminSettingDao.setSetting(AdminSettingEntity("nagad_number", "01712345678", "Nagad মার্চেন্ট/ব্যক্তিগত নম্বর"))
            adminSettingDao.setSetting(AdminSettingEntity("rocket_number", "01912345678", "Rocket মার্চেন্ট/ব্যক্তিগত নম্বর"))
            adminSettingDao.setSetting(AdminSettingEntity("bd_bank_name", "City Bank Ltd / Dutch Bangla Bank", "বাংলাদেশি ব্যাংক নাম"))
            adminSettingDao.setSetting(AdminSettingEntity("bd_bank_account_name", "Hater Kache Official", "বাংলাদেশি হিসাবের নাম"))
            adminSettingDao.setSetting(AdminSettingEntity("bd_bank_account_number", "1502839201928", "বাংলাদেশি হিসাব নম্বর"))
            adminSettingDao.setSetting(AdminSettingEntity("bd_bank_branch", "Gulshan Branch, Dhaka", "শাখা ও রাউটিং নম্বর"))

            adminSettingDao.setSetting(AdminSettingEntity("intl_bank_name", "Standard Chartered / JP Morgan Chase", "International Bank Name"))
            adminSettingDao.setSetting(AdminSettingEntity("intl_bank_beneficiary", "Hater Kache Global Services Ltd", "Beneficiary / Account Name"))
            adminSettingDao.setSetting(AdminSettingEntity("intl_bank_iban", "GB29NWBK60161331926819", "IBAN / Account Number"))
            adminSettingDao.setSetting(AdminSettingEntity("intl_bank_swift", "SCBLBDDX", "SWIFT / BIC Code"))
            adminSettingDao.setSetting(AdminSettingEntity("intl_paypal_email", "payments@haterkache.com", "PayPal / Wise Email"))
            adminSettingDao.setSetting(AdminSettingEntity("intl_card_instructions", "Visa, MasterCard, American Express accepted. Enter transaction confirmation receipt below.", "International Card Instructions"))

            adminSettingDao.setSetting(AdminSettingEntity("sub_price_bd", "100", "মাসিক সাবস্ক্রিপশন ফি (বাংলাদেশ - টাকা)"))
            adminSettingDao.setSetting(AdminSettingEntity("sub_price_intl", "2.00", "Monthly Subscription Fee (International - USD)"))

            adminSettingDao.setSetting(AdminSettingEntity("banner_active", "true", "Banner Active"))
            adminSettingDao.setSetting(AdminSettingEntity("banner_title_bn", "হাতের কাছে মেগা অফার — ২০% ছাড়!", "Banner Title BN"))
            adminSettingDao.setSetting(AdminSettingEntity("banner_title_en", "Hater Kache Special Offer — 20% Off!", "Banner Title EN"))
            adminSettingDao.setSetting(AdminSettingEntity("banner_notice_bn", "দক্ষ ও বিশ্বস্ত কারিগর এখন আপনার এলাকাতেই! সরাসরি কল বা হোয়াটসঅ্যাপ করুন।", "Banner Notice BN"))
            adminSettingDao.setSetting(AdminSettingEntity("banner_notice_en", "Find verified & professional technicians nearby! Call or WhatsApp directly.", "Banner Notice EN"))
            adminSettingDao.setSetting(AdminSettingEntity("banner_image_url", "", "Banner Image URL"))

            // Advertisement Settings (Admin Managed)
            adminSettingDao.setSetting(AdminSettingEntity("ad_contact_email", "safuanislam2276@gmail.com", "বিজ্ঞাপন দেওয়ার জন্য যোগাযোগের ইমেইল"))
            adminSettingDao.setSetting(AdminSettingEntity("ad_sponsor_brand", "ওয়ালটন / স্যামসাং / আরএফএল", "হোম ব্যানার স্পনসর ব্র্যান্ড"))
            adminSettingDao.setSetting(AdminSettingEntity("contact_ad_enabled", "true", "কল বা হোয়াটসঅ্যাপে ২-৩ সেকেন্ডের এড সক্রিয়"))
            adminSettingDao.setSetting(AdminSettingEntity("contact_ad_brand", "ওয়ালটন স্মার্ট অ্যাপ্লায়েন্স", "কল এড ব্র্যান্ডের নাম"))
            adminSettingDao.setSetting(AdminSettingEntity("contact_ad_title", "সেরা ইলেকট্রনিক্স ও হোম অ্যাপ্লায়েন্স!", "কল এড হেডলাইন"))
            adminSettingDao.setSetting(AdminSettingEntity("contact_ad_desc", "আজই অর্ডার করুন এবং উপভোগ করুন বিশেষ ছাড় ও ফ্রি ডেলিভারি।", "কল এড বিবরণ"))
        }
    }

    private suspend fun seedInitialWorkers() {
        val existing = mistriDao.getMistriById("m-1")
        if (existing == null) {
            // Dhaka PRO Electrician
            val m1 = MistriEntity(
                id = "m-1",
                userId = "u-m1",
                name = "মোঃ রফিকুল ইসলাম",
                email = "rafiq.electric@gmail.com",
                phone = "+8801711223344",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "ঢাকা",
                district = "ঢাকা",
                address = "মিরপুর-১০, ঢাকা",
                profession = "ইলেকট্রিশিয়ান",
                extraCategories = "এসি মেকানিক, ফ্রিজ মেকানিক, আইপিএস টেকনিশিয়ান, সিসিটিভি টেকনিশিয়ান",
                experienceYears = 8,
                bio = "৮ বছরের অভিজ্ঞ হাউস ওয়্যারিং, শর্ট সার্কিট সমাধান ও ইন্ডাস্ট্রিয়াল ইলেকট্রিক কাজের মাস্টার টেকনিশিয়ান। বিশ্বস্ত ও দ্রুত সেবা প্রদান করি।",
                skills = "হাউস ওয়্যারিং, ডিবি বোর্ড স্থাপন, সার্কিট ব্রেকার, ফ্যান ও লাইট ফিটিং, আইপিএস ইনস্টলেশন",
                isVerified = true,
                isPro = true,
                proTier = "Premium",
                proExpiresAt = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
                averageRating = 4.9,
                totalReviews = 14,
                callClicks = 28,
                waClicks = 19
            )
            mistriDao.insertMistri(m1)

            // Kishoreganj Plumber
            val m2 = MistriEntity(
                id = "m-2",
                userId = "u-m2",
                name = "কামাল হোসেন",
                email = "kamal.plumber@gmail.com",
                phone = "+8801819876543",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "ঢাকা",
                district = "কিশোরগঞ্জ",
                address = "বটতলা মোড়, কিশোরগঞ্জ সদর",
                profession = "প্লাম্বার ও স্যানিটারি",
                extraCategories = "প্লাম্বার, পানির মোটর মেকানিক, গ্যাস লাইন টেকনিশিয়ান, স্যানিটারি মিস্ত্রি",
                experienceYears = 6,
                bio = "সকল ধরনের পাইপ লিকেজ, বাথরুম ফিটিংস, পানির পাম্প ও রিজার্ভ ট্যাংক পরিষ্কারের দক্ষ কারিগর।",
                skills = "পাইপ ফিটিংস, পানির মোটর রিপেয়ার, গিজার ইনস্টল, বাথরুম স্যানিটারি, ড্রেনেজ ক্লিনিং",
                isVerified = true,
                isPro = false,
                averageRating = 4.8,
                totalReviews = 9,
                callClicks = 15,
                waClicks = 11
            )
            mistriDao.insertMistri(m2)

            // Dhaka AC Specialist (PRO)
            val m3 = MistriEntity(
                id = "m-3",
                userId = "u-m3",
                name = "ইঞ্জিঃ তানভীর আহমেদ",
                email = "tanvir.cool@gmail.com",
                phone = "+8801912334455",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "ঢাকা",
                district = "ঢাকা",
                address = "ধানমন্ডি ২৭, ঢাকা",
                profession = "এসি মেকানিক",
                extraCategories = "ফ্রিজ মেকানিক, ওয়াশিং মেশিন মেকানিক, মাইক্রোওয়েভ ওভেন মেকানিক, ইলেকট্রিশিয়ান",
                experienceYears = 7,
                bio = "ইনভার্টার ও নন-ইনভার্টার সকল ব্র্যান্ডের এসি সার্ভিসিং, গ্যাস চার্জিং, কম্প্রেসার মেরামত ও লিকেজ টেস্টার।",
                skills = "এসি গ্যাস চার্জ, কম্প্রেসার মেরামত, মাস্টার সার্ভিসিং, ইনভার্টার পিসিবি রিপেয়ার",
                isVerified = true,
                isPro = true,
                proTier = "Pro",
                proExpiresAt = System.currentTimeMillis() + (25L * 24 * 60 * 60 * 1000),
                averageRating = 4.9,
                totalReviews = 21,
                callClicks = 42,
                waClicks = 33
            )
            mistriDao.insertMistri(m3)

            // Chittagong Carpenter
            val m4 = MistriEntity(
                id = "m-4",
                userId = "u-m4",
                name = "আব্দুল কাদের",
                email = "kader.woodwork@gmail.com",
                phone = "+8801615556677",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "চট্টগ্রাম",
                district = "চট্টগ্রাম",
                address = "আগ্রাবাদ, চট্টগ্রাম",
                profession = "কাঠমিস্ত্রি",
                experienceYears = 12,
                bio = "সেগুন, মেহগনি ও বোর্ডের আধুনিক ফার্নিচার ডিজাইন, ডোর ফিটিংস ও কিচেন ক্যাবিনেট তৈরি করি।",
                skills = "মডুলার কিচেন, দরজার লক পরিবর্তন, কাঠের পলিশ, ড্রেসিং টেবিল ও খাট মেরামত",
                isVerified = true,
                isPro = true,
                proTier = "Starter",
                proExpiresAt = System.currentTimeMillis() + (15L * 24 * 60 * 60 * 1000),
                averageRating = 4.7,
                totalReviews = 8,
                callClicks = 18,
                waClicks = 14
            )
            mistriDao.insertMistri(m4)

            // Dubai UAE Technician
            val m5 = MistriEntity(
                id = "m-5",
                userId = "u-m5",
                name = "শাকিল চৌধুরী",
                email = "shakil.dubai@gmail.com",
                phone = "+971501234567",
                country = "সংযুক্ত আরব আমিরাত",
                countryCode = "+971",
                division = "আমিরাত",
                district = "দুবাই (Dubai)",
                address = "Deira, Dubai, UAE",
                profession = "ইলেকট্রিশিয়ান",
                experienceYears = 5,
                bio = "দুবাই ও শারজাহতে আবাসিক ও বাণিজ্যিক ইলেকট্রিক্যাল সার্ভিস এবং সিসিটিভি ইনস্টলেশন।",
                skills = "Villa Electrical, CCTV Camera, Short Circuit, Maintenance",
                isVerified = true,
                isPro = true,
                proTier = "Premium",
                proExpiresAt = System.currentTimeMillis() + (28L * 24 * 60 * 60 * 1000),
                averageRating = 5.0,
                totalReviews = 6,
                callClicks = 22,
                waClicks = 27
            )
            mistriDao.insertMistri(m5)

            // Riyadh Saudi Arabia Technician
            val m6 = MistriEntity(
                id = "m-6",
                userId = "u-m6",
                name = "আব্দুর রহমান",
                email = "rahman.riyadh@gmail.com",
                phone = "+966551234567",
                country = "সৌদি আরব",
                countryCode = "+966",
                division = "রিয়াদ প্রদেশ",
                district = "রিয়াদ (Riyadh)",
                address = "Al Batha, Riyadh",
                profession = "প্লাম্বার ও স্যানিটারি",
                experienceYears = 9,
                bio = "রিয়াদে বিশ্বস্ত প্লাম্বিং, পানির ফিল্টার ও পাইপ ফিটিং সেবা প্রদানকারী।",
                skills = "Plumbing, Water Heater, Leakage Repair, Pipe Fitting",
                isVerified = true,
                isPro = false,
                averageRating = 4.6,
                totalReviews = 5,
                callClicks = 12,
                waClicks = 9
            )
            mistriDao.insertMistri(m6)

            // Seed initial reviews
            reviewDao.insertReview(
                ReviewEntity(
                    id = "rev-1",
                    mistriId = "m-1",
                    customerId = "cust-1",
                    customerName = "জাহিদ হাসান",
                    rating = 5,
                    reviewText = "খুবই দক্ষ ও ভদ্র মানুষ। খুব দ্রুত এসে বাড়ির পুরো ওয়্যারিং চেক করে সমস্যা সমাধান করেছেন।"
                )
            )
            reviewDao.insertReview(
                ReviewEntity(
                    id = "rev-2",
                    mistriId = "m-1",
                    customerId = "cust-2",
                    customerName = "নুসরাত জাহান",
                    rating = 5,
                    reviewText = "সময়মত এসেছিলেন এবং সঠিক মূল্যে কাজ করে দিয়েছেন। কাজের মান দারুণ।"
                )
            )
            reviewDao.insertReview(
                ReviewEntity(
                    id = "rev-3",
                    mistriId = "m-3",
                    customerId = "cust-3",
                    customerName = "সাকিব মাহমুদ",
                    rating = 5,
                    reviewText = "এসি ঠাণ্ডা হচ্ছিল না। তানভীর ভাই এসে আধা ঘন্টার মধ্যে গ্যাস লিক ঠিক করে দিলেন!"
                )
            )

            // Seed initial customer
            val demoCustomer = UserEntity(
                id = "cust-demo",
                email = "customer@gmail.com",
                name = "আহমেদ সাদমান",
                phone = "+8801811998877",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "ঢাকা",
                district = "কিশোরগঞ্জ",
                address = "কিশোরগঞ্জ সদর",
                userType = "CUSTOMER"
            )
            userDao.insertUser(demoCustomer)
            _currentUser.value = demoCustomer
        }
    }

    suspend fun signUpCustomer(
        name: String,
        email: String,
        phone: String,
        password: String? = null,
        country: String,
        countryCode: String,
        division: String,
        district: String,
        address: String,
        profileImageUri: String?
    ): UserEntity {
        logAnalyticsEvent("signup_started", country)
        if (!password.isNullOrBlank()) {
            // Attempt Firebase Auth sign-up
            try {
                firebaseAuth.signUpWithEmail(email, password)
            } catch (ignored: Exception) {}
        }
        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            email = email,
            name = name,
            phone = phone,
            country = country,
            countryCode = countryCode,
            division = division,
            district = district,
            address = address,
            userType = "CUSTOMER",
            profileImageUri = profileImageUri
        )
        userDao.insertUser(user)
        _currentUser.value = user
        logAnalyticsEvent("signup_completed", country, user.id)
        return user
    }

    suspend fun signUpWorker(
        name: String,
        email: String,
        phone: String,
        password: String? = null,
        country: String,
        countryCode: String,
        division: String,
        district: String,
        address: String,
        profession: String,
        extraCategories: String = "",
        experienceYears: Int,
        bio: String,
        skills: String,
        profileImageUri: String?
    ): MistriEntity {
        logAnalyticsEvent("signup_started", country)
        if (!password.isNullOrBlank()) {
            // Attempt Firebase Auth sign-up
            try {
                firebaseAuth.signUpWithEmail(email, password)
            } catch (ignored: Exception) {}
        }
        val userId = UUID.randomUUID().toString()
        val user = UserEntity(
            id = userId,
            email = email,
            name = name,
            phone = phone,
            country = country,
            countryCode = countryCode,
            division = division,
            district = district,
            address = address,
            userType = "MISTRI",
            profileImageUri = profileImageUri
        )
        userDao.insertUser(user)

        val mistri = MistriEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            name = name,
            email = email,
            phone = phone,
            country = country,
            countryCode = countryCode,
            division = division,
            district = district,
            address = address,
            profession = profession,
            extraCategories = extraCategories,
            experienceYears = experienceYears,
            bio = bio,
            skills = skills,
            profileImageUri = profileImageUri,
            isVerified = true,
            isPro = false
        )
        mistriDao.insertMistri(mistri)
        _currentUser.value = user
        logAnalyticsEvent("signup_completed", country, mistri.id)
        return mistri
    }

    suspend fun login(email: String, password: String? = null): UserEntity? {
        val cleanEmail = email.trim()

        if (!password.isNullOrBlank()) {
            val fbResult = firebaseAuth.signInWithEmail(cleanEmail, password)
            if (fbResult.isFailure) {
                // If it's the owner email, allow bypass
                val isOwner = cleanEmail.equals("safuanislam2276@gmail.com", ignoreCase = true)
                val localUser = userDao.getUserByEmail(cleanEmail)
                if (!isOwner && localUser == null) {
                    val err = fbResult.exceptionOrNull()?.message ?: "লগইন ব্যর্থ হয়েছে"
                    throw Exception(err)
                }
            }
        }

        var user = userDao.getUserByEmail(cleanEmail)
        val existingMistri = mistriDao.getMistriByEmail(cleanEmail)

        if (user == null && cleanEmail.equals("safuanislam2276@gmail.com", ignoreCase = true)) {
            val adminUser = UserEntity(
                id = "u-owner-safuan",
                email = "safuanislam2276@gmail.com",
                name = "Safuan Islam (Owner)",
                phone = "+8801700000000",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "ঢাকা",
                district = "ঢাকা",
                address = "Headquarters",
                userType = "ADMIN"
            )
            userDao.insertUser(adminUser)
            user = adminUser
        } else if (existingMistri != null) {
            // Restore Worker Profile
            val restoredUser = UserEntity(
                id = existingMistri.userId,
                email = cleanEmail,
                name = existingMistri.name,
                phone = existingMistri.phone,
                country = existingMistri.country,
                countryCode = existingMistri.countryCode,
                division = existingMistri.division,
                district = existingMistri.district,
                address = existingMistri.address,
                userType = "MISTRI",
                profileImageUri = existingMistri.profileImageUri
            )
            userDao.insertUser(restoredUser)
            user = restoredUser
        } else if (user == null) {
            // User authenticated, create/restore customer profile representation
            val newUser = UserEntity(
                id = UUID.randomUUID().toString(),
                email = cleanEmail,
                name = cleanEmail.substringBefore("@").replace(".", " ").capitalize(),
                phone = "",
                country = "বাংলাদেশ",
                countryCode = "+880",
                division = "ঢাকা",
                district = "ঢাকা",
                userType = "CUSTOMER"
            )
            userDao.insertUser(newUser)
            user = newUser
        }

        if (user != null) {
            _currentUser.value = user
        }
        return user
    }

    suspend fun loginWithGoogle(
        email: String,
        name: String,
        photoUrl: String?,
        country: String,
        countryCode: String,
        division: String,
        district: String,
        userType: String = "CUSTOMER"
    ): UserEntity {
        val cleanEmail = email.trim()
        var user = userDao.getUserByEmail(cleanEmail)
        val existingMistri = mistriDao.getMistriByEmail(cleanEmail)
        val isOwner = cleanEmail.equals("safuanislam2276@gmail.com", ignoreCase = true)

        if (user == null) {
            val resolvedType = when {
                isOwner -> "ADMIN"
                existingMistri != null -> "MISTRI"
                else -> userType
            }
            user = UserEntity(
                id = existingMistri?.userId ?: UUID.randomUUID().toString(),
                email = cleanEmail,
                name = if (name.isNotBlank()) name else (existingMistri?.name ?: cleanEmail.substringBefore("@")),
                phone = existingMistri?.phone ?: "",
                country = existingMistri?.country ?: country,
                countryCode = existingMistri?.countryCode ?: countryCode,
                division = existingMistri?.division ?: division,
                district = existingMistri?.district ?: district,
                address = existingMistri?.address ?: "",
                userType = resolvedType,
                profileImageUri = photoUrl ?: existingMistri?.profileImageUri
            )
            userDao.insertUser(user)
        } else {
            // If the user already had a mistri profile, make sure userType is MISTRI
            if (existingMistri != null && user.userType != "MISTRI") {
                user = user.copy(userType = "MISTRI")
                userDao.updateUser(user)
            } else if (isOwner && user.userType != "ADMIN") {
                user = user.copy(userType = "ADMIN")
                userDao.updateUser(user)
            }
        }
        _currentUser.value = user
        return user
    }

    suspend fun updateUserProfile(user: UserEntity) {
        userDao.updateUser(user)
        if (_currentUser.value?.id == user.id) {
            _currentUser.value = user
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun setCurrentUser(user: UserEntity?) {
        _currentUser.value = user
    }

    fun getMistriByIdFlow(id: String): Flow<MistriEntity?> = mistriDao.getMistriByIdFlow(id)

    fun getMistriByUserIdFlow(userId: String): Flow<MistriEntity?> = mistriDao.getMistriByUserIdFlow(userId)

    fun getReviewsForMistri(mistriId: String): Flow<List<ReviewEntity>> = reviewDao.getReviewsForMistri(mistriId)

    suspend fun recordMistriView(mistriId: String, country: String) {
        logAnalyticsEvent("mistri_view", country, mistriId)
    }

    suspend fun recordCallClick(mistriId: String, country: String) {
        mistriDao.incrementCallClick(mistriId)
        logAnalyticsEvent("call_click", country, mistriId)
    }

    suspend fun recordWaClick(mistriId: String, country: String) {
        mistriDao.incrementWaClick(mistriId)
        logAnalyticsEvent("wa_click", country, mistriId)
    }

    suspend fun submitReview(mistriId: String, rating: Int, reviewText: String): Boolean {
        val user = _currentUser.value ?: return false
        val review = ReviewEntity(
            id = UUID.randomUUID().toString(),
            mistriId = mistriId,
            customerId = user.id,
            customerName = user.name,
            rating = rating,
            reviewText = reviewText
        )
        reviewDao.insertReview(review)

        // Recalculate average rating & total reviews
        val avg = reviewDao.getAverageRatingForMistri(mistriId) ?: rating.toDouble()
        val count = reviewDao.getReviewCountForMistri(mistriId)
        val roundedAvg = Math.round(avg * 10.0) / 10.0
        mistriDao.updateRatingAndReviews(mistriId, roundedAvg, count)
        return true
    }

    suspend fun submitSubscription(
        mistriId: String,
        mistriName: String,
        planName: String,
        amount: String,
        paymentMethod: String,
        transactionId: String,
        country: String,
        autoApprove: Boolean = true
    ): SubscriptionEntity {
        val status = if (autoApprove) "completed" else "pending"
        val sub = SubscriptionEntity(
            id = UUID.randomUUID().toString(),
            mistriId = mistriId,
            mistriName = mistriName,
            planName = planName,
            amount = amount,
            paymentMethod = paymentMethod,
            transactionId = transactionId,
            status = status,
            createdAt = System.currentTimeMillis()
        )
        subscriptionDao.insertSubscription(sub)
        if (autoApprove) {
            approveSubscriptionByMistri(mistriId, planName)
        }
        logAnalyticsEvent("payment_initiated", country, sub.id)
        return sub
    }

    suspend fun approveSubscriptionByMistri(mistriId: String, planName: String) {
        val tier = when {
            planName.contains("Platinum", ignoreCase = true) || planName.contains("১ বছর", ignoreCase = true) -> "Platinum"
            planName.contains("VIP", ignoreCase = true) || planName.contains("৩ মাস", ignoreCase = true) -> "VIP"
            else -> "PRO"
        }
        val durationDays = when {
            planName.contains("১ বছর", ignoreCase = true) || planName.contains("Platinum", ignoreCase = true) -> 365L
            planName.contains("৩ মাস", ignoreCase = true) || planName.contains("VIP", ignoreCase = true) -> 90L
            else -> 30L
        }
        val expiresAt = System.currentTimeMillis() + (durationDays * 24L * 60 * 60 * 1000)
        mistriDao.updateProStatus(mistriId, true, tier, expiresAt)
    }

    suspend fun approveSubscription(subscriptionId: String, mistriId: String, planName: String) {
        subscriptionDao.updateSubscriptionStatus(subscriptionId, "completed", System.currentTimeMillis())
        approveSubscriptionByMistri(mistriId, planName)
    }

    suspend fun rejectSubscription(subscriptionId: String) {
        subscriptionDao.updateSubscriptionStatus(subscriptionId, "rejected", System.currentTimeMillis())
    }

    suspend fun toggleBlockMistri(mistriId: String, isBlocked: Boolean) {
        mistriDao.setBlockedStatus(mistriId, isBlocked)
    }

    suspend fun updateAdminSetting(key: String, value: String) {
        adminSettingDao.setSetting(AdminSettingEntity(key, value))
    }

    suspend fun getAdminSetting(key: String): String? {
        return adminSettingDao.getSettingValue(key)
    }

    suspend fun logAnalyticsEvent(eventType: String, country: String, targetId: String? = null) {
        analyticsDao.insertEvent(
            AnalyticsEventEntity(
                eventType = eventType,
                country = country,
                targetId = targetId,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getViewsByCountry(): Flow<List<CountryStat>> = analyticsDao.getViewsByCountry()
    fun getSignupsByCountry(): Flow<List<CountryStat>> = analyticsDao.getSignupsByCountry()
    fun getRecentAnalyticsEvents(): Flow<List<AnalyticsEventEntity>> = analyticsDao.getRecentEvents()

    suspend fun updateMistriProfile(mistri: MistriEntity) {
        mistriDao.updateMistri(mistri)
    }

    // Messenger Chat Operations
    fun getMessagesForConversationFlow(conversationId: String): Flow<List<ChatMessageEntity>> =
        chatDao.getMessagesForConversationFlow(conversationId)

    fun getAllMessagesForUserFlow(userId: String, userEmail: String): Flow<List<ChatMessageEntity>> =
        chatDao.getAllMessagesForUserFlow(userId, userEmail)

    fun getUnreadChatCountFlow(userId: String, userEmail: String): Flow<Int> =
        chatDao.getUnreadCountFlow(userId, userEmail)

    suspend fun markChatAsRead(conversationId: String, userId: String) {
        chatDao.markAsRead(conversationId, userId)
    }

    suspend fun deleteConversation(conversationId: String) {
        chatDao.deleteConversation(conversationId)
    }

    suspend fun deleteMessage(messageId: String) {
        chatDao.deleteMessage(messageId)
    }

    suspend fun sendChatMessage(
        conversationId: String,
        senderId: String,
        senderName: String,
        senderEmail: String,
        senderRole: String,
        receiverId: String,
        receiverName: String,
        receiverEmail: String,
        receiverRole: String,
        text: String
    ): ChatMessageEntity {
        val userEmailForRestore = if (senderRole == "CUSTOMER") senderEmail else receiverEmail
        val msg = ChatMessageEntity(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            senderId = senderId,
            senderName = senderName,
            senderEmail = senderEmail,
            senderRole = senderRole,
            receiverId = receiverId,
            receiverName = receiverName,
            receiverEmail = receiverEmail,
            receiverRole = receiverRole,
            messageText = text.trim(),
            timestamp = System.currentTimeMillis(),
            isRead = false,
            userEmail = userEmailForRestore
        )
        chatDao.insertMessage(msg)

        // Custom auto-reply logic (User requested: only auto-replies if recipient configured a custom auto-message in Messenger options)
        CoroutineScope(Dispatchers.IO).launch {
            val customAutoReply = adminSettingDao.getSettingValue("auto_reply_$receiverId")
                ?: if (receiverEmail.isNotBlank()) adminSettingDao.getSettingValue("auto_reply_$receiverEmail") else null

            if (!customAutoReply.isNullOrBlank()) {
                kotlinx.coroutines.delay(1200L)
                val replyMsg = ChatMessageEntity(
                    id = UUID.randomUUID().toString(),
                    conversationId = conversationId,
                    senderId = receiverId,
                    senderName = receiverName,
                    senderEmail = receiverEmail,
                    senderRole = receiverRole,
                    receiverId = senderId,
                    receiverName = senderName,
                    receiverEmail = senderEmail,
                    receiverRole = senderRole,
                    messageText = customAutoReply,
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    userEmail = userEmailForRestore
                )
                chatDao.insertMessage(replyMsg)
            }
        }

        return msg
    }

    suspend fun getAutoReplyForUser(userId: String, email: String?): String? {
        val byId = adminSettingDao.getSettingValue("auto_reply_$userId")
        if (!byId.isNullOrBlank()) return byId
        if (!email.isNullOrBlank()) {
            return adminSettingDao.getSettingValue("auto_reply_$email")
        }
        return null
    }

    suspend fun setAutoReplyForUser(userId: String, email: String?, message: String) {
        val trimmed = message.trim()
        adminSettingDao.setSetting(
            AdminSettingEntity(
                key = "auto_reply_$userId",
                value = trimmed,
                description = "User Custom Auto Reply"
            )
        )
        if (!email.isNullOrBlank()) {
            adminSettingDao.setSetting(
                AdminSettingEntity(
                    key = "auto_reply_$email",
                    value = trimmed,
                    description = "User Custom Auto Reply"
                )
            )
        }
    }
}
