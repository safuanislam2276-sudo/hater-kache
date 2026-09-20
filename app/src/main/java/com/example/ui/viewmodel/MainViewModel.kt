package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.country.CountryInfo
import com.example.data.country.CountryRepository
import com.example.data.model.*
import com.example.data.repository.AppRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class SearchFilterState(
    val query: String = "",
    val country: String = "বাংলাদেশ",
    val division: String = "সকল",
    val district: String = "সকল",
    val profession: String = "সকল পেশা"
)

data class BannerState(
    val active: Boolean = true,
    val titleBn: String = "হাতের কাছে মেগা অফার — ২০% ছাড়!",
    val titleEn: String = "Hater Kache Special Offer — 20% Off!",
    val noticeBn: String = "দক্ষ ও বিশ্বস্ত কারিগর এখন আপনার এলাকাতেই! সরাসরি কল বা হোয়াটসঅ্যাপ করুন।",
    val noticeEn: String = "Find verified & professional technicians nearby! Call or WhatsApp directly.",
    val imageUrl: String = "",
    val adContactEmail: String = "safuanislam2276@gmail.com",
    val adSponsorBrand: String = "ওয়ালটন / স্যামসাং / আরএফএল",
    val contactAdEnabled: Boolean = true,
    val contactAdBrand: String = "ওয়ালটন স্মার্ট অ্যাপ্লায়েন্স",
    val contactAdTitle: String = "সেরা ইলেকট্রনিক্স ও হোম অ্যাপ্লায়েন্স!",
    val contactAdDesc: String = "আজই অর্ডার করুন এবং উপভোগ করুন বিশেষ ছাড় ও ফ্রি ডেলিভারি।"
) {
    val sponsorBrand: String get() = adSponsorBrand
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    // Global selected country (top bar country selector)
    private val _globalCountry = MutableStateFlow(CountryRepository.PRIMARY_COUNTRIES.first())
    val globalCountry: StateFlow<CountryInfo> = _globalCountry.asStateFlow()

    // Independent search filters
    private val _searchFilters = MutableStateFlow(SearchFilterState())
    val searchFilters: StateFlow<SearchFilterState> = _searchFilters.asStateFlow()

    // Current logged-in user
    val currentUser: StateFlow<UserEntity?> = repository.currentUser

    // If current user is Mistri, observe their profile
    val currentMistriProfile: StateFlow<MistriEntity?> = currentUser.flatMapLatest { user ->
        if (user != null && user.userType == "MISTRI") {
            repository.getMistriByUserIdFlow(user.id)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Live counts
    val liveWorkersCount: StateFlow<Int> = repository.liveWorkersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val liveCustomerCount: StateFlow<Int> = repository.liveCustomerCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val liveReviewCount: StateFlow<Int> = repository.liveReviewCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Admin counts
    val totalWorkersCount: StateFlow<Int> = repository.totalWorkersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val blockedWorkersCount: StateFlow<Int> = repository.blockedWorkersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val proWorkersCount: StateFlow<Int> = repository.proWorkersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val allMistrisList: StateFlow<List<MistriEntity>> = repository.allMistris
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allUsersList: StateFlow<List<UserEntity>> = repository.allUsersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pendingSubscriptions: StateFlow<List<SubscriptionEntity>> = repository.pendingSubscriptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val adminSettings: StateFlow<List<AdminSettingEntity>> = repository.adminSettingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentThemeColor: StateFlow<String> = adminSettings.map { list ->
        list.find { it.key == "app_theme_color" }?.value ?: "#0284C7"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "#0284C7")

    val bannerState: StateFlow<BannerState> = adminSettings.map { list ->
        val map = list.associate { it.key to it.value }
        BannerState(
            active = map["banner_active"] != "false",
            titleBn = map["banner_title_bn"] ?: "হাতের কাছে মেগা অফার — ২০% ছাড়!",
            titleEn = map["banner_title_en"] ?: "Hater Kache Special Offer — 20% Off!",
            noticeBn = map["banner_notice_bn"] ?: "দক্ষ ও বিশ্বস্ত কারিগর এখন আপনার এলাকাতেই! সরাসরি কল বা হোয়াটসঅ্যাপ করুন।",
            noticeEn = map["banner_notice_en"] ?: "Find verified & professional technicians nearby! Call or WhatsApp directly.",
            imageUrl = map["banner_image_url"] ?: "",
            adContactEmail = map["ad_contact_email"] ?: "safuanislam2276@gmail.com",
            adSponsorBrand = map["ad_sponsor_brand"] ?: "ওয়ালটন / স্যামসাং / আরএফএল",
            contactAdEnabled = map["contact_ad_enabled"] != "false",
            contactAdBrand = map["contact_ad_brand"] ?: "ওয়ালটন স্মার্ট অ্যাপ্লায়েন্স",
            contactAdTitle = map["contact_ad_title"] ?: "সেরা ইলেকট্রনিক্স ও হোম অ্যাপ্লায়েন্স!",
            contactAdDesc = map["contact_ad_desc"] ?: "আজই অর্ডার করুন এবং উপভোগ করুন বিশেষ ছাড় ও ফ্রি ডেলিভারি।"
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BannerState())

    val isOwnerAdmin: StateFlow<Boolean> = currentUser.map { user ->
        user != null && (user.userType == "ADMIN" || user.email.equals("safuanislam2276@gmail.com", ignoreCase = true))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val viewsByCountry: StateFlow<List<CountryStat>> = repository.getViewsByCountry()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val signupsByCountry: StateFlow<List<CountryStat>> = repository.getSignupsByCountry()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin unlocked state
    private val _isAdminUnlocked = MutableStateFlow(false)
    val isAdminUnlocked: StateFlow<Boolean> = _isAdminUnlocked.asStateFlow()

    // Selected mistri for details
    private val _selectedMistriId = MutableStateFlow<String?>(null)
    val selectedMistriId: StateFlow<String?> = _selectedMistriId.asStateFlow()

    val selectedMistri: StateFlow<MistriEntity?> = _selectedMistriId.flatMapLatest { id ->
        if (id != null) repository.getMistriByIdFlow(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedMistriReviews: StateFlow<List<ReviewEntity>> = _selectedMistriId.flatMapLatest { id ->
        if (id != null) repository.getReviewsForMistri(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Mistri List with PRO FIRST ordering, then Nearby User Location
    val filteredMistris: StateFlow<List<MistriEntity>> = combine(
        repository.activeMistrisProFirst,
        _searchFilters,
        currentUser
    ) { mistris, filter, user ->
        val userDistrict = user?.district.orEmpty().trim()
        mistris.filter { m ->
            val matchCountry = filter.country.isEmpty() ||
                    filter.country == "সকল দেশ" ||
                    filter.country == "All Countries" ||
                    m.country.equals(filter.country, ignoreCase = true) ||
                    CountryRepository.getCountryByName(filter.country).nameBn.equals(m.country, ignoreCase = true) ||
                    CountryRepository.getCountryByName(filter.country).nameEn.equals(m.country, ignoreCase = true)

            val matchDivision = filter.division == "সকল" || filter.division == "All" || filter.division.isEmpty() || m.division.contains(filter.division, ignoreCase = true)
            val matchDistrict = filter.district == "সকল" || filter.district == "All" || filter.district.isEmpty() || m.district.contains(filter.district, ignoreCase = true)
            val allWorkerCategories = m.getAllCategoriesList()
            val matchProfession = CountryRepository.matchesProfession(m.profession, filter.profession) ||
                    allWorkerCategories.any { CountryRepository.matchesProfession(it, filter.profession) } ||
                    m.skills.contains(filter.profession, ignoreCase = true)
            val matchQuery = filter.query.trim().isEmpty() ||
                    m.name.contains(filter.query, ignoreCase = true) ||
                    m.profession.contains(filter.query, ignoreCase = true) ||
                    m.extraCategories.contains(filter.query, ignoreCase = true) ||
                    m.skills.contains(filter.query, ignoreCase = true) ||
                    m.district.contains(filter.query, ignoreCase = true) ||
                    m.bio.contains(filter.query, ignoreCase = true)

            matchCountry && matchDivision && matchDistrict && matchProfession && matchQuery
        }.sortedWith(
            compareByDescending<MistriEntity> { it.isPro }
                .thenByDescending { if (userDistrict.isNotBlank() && it.district.equals(userDistrict, ignoreCase = true)) 1 else 0 }
                .thenByDescending { it.averageRating }
                .thenByDescending { it.totalReviews }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setGlobalCountry(country: CountryInfo) {
        _globalCountry.value = country
        viewModelScope.launch {
            repository.logAnalyticsEvent("country_view", country.nameBn)
        }
    }

    // INDEPENDENT SEARCH FILTERS: Does NOT mutate user's profile location
    fun updateSearchQuery(query: String) {
        _searchFilters.value = _searchFilters.value.copy(query = query)
    }

    fun updateSearchCountry(countryName: String) {
        _searchFilters.value = _searchFilters.value.copy(
            country = countryName,
            division = "সকল",
            district = "সকল"
        )
    }

    fun updateSearchDivision(division: String) {
        _searchFilters.value = _searchFilters.value.copy(
            division = division,
            district = "সকল"
        )
    }

    fun updateSearchDistrict(district: String) {
        _searchFilters.value = _searchFilters.value.copy(district = district)
    }

    fun updateSearchProfession(profession: String) {
        _searchFilters.value = _searchFilters.value.copy(profession = profession)
    }

    fun resetSearchFilters() {
        _searchFilters.value = SearchFilterState(
            country = _globalCountry.value.nameBn
        )
    }

    fun selectMistri(id: String?) {
        _selectedMistriId.value = id
        if (id != null) {
            viewModelScope.launch {
                repository.recordMistriView(id, _globalCountry.value.nameBn)
            }
        }
    }

    // Contact actions with analytics tracking
    fun callMistri(context: Context, mistri: MistriEntity) {
        viewModelScope.launch {
            repository.recordCallClick(mistri.id, _globalCountry.value.nameBn)
        }
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${mistri.phone}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "ফোন ডায়াল খুলতে সমস্যা হয়েছে: ${mistri.phone}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, mistri: MistriEntity) {
        viewModelScope.launch {
            repository.recordWaClick(mistri.id, _globalCountry.value.nameBn)
        }
        try {
            val cleanPhone = mistri.phone.replace("+", "").replace("-", "").replace(" ", "").trim()
            val url = "https://wa.me/$cleanPhone?text=আসসালামু আলাইকুম, হাতের কাছে (Hater Kache) অ্যাপে আপনার প্রোফাইল দেখেছি। কাজের বিষয়ে কথা বলতে চাই।"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "হোয়াটসঅ্যাপ খুলতে পারেনি। নম্বর: ${mistri.phone}", Toast.LENGTH_SHORT).show()
        }
    }

    // Authentication
    fun registerCustomer(
        name: String,
        email: String,
        phone: String,
        password: String? = null,
        country: String,
        countryCode: String,
        division: String,
        district: String,
        address: String,
        profileImageUri: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (name.isBlank() || email.isBlank() || phone.isBlank()) {
            onError("দয়া করে নাম, ইমেইল ও ফোন নম্বর সঠিকভাবে দিন")
            return
        }
        viewModelScope.launch {
            try {
                repository.signUpCustomer(
                    name = name,
                    email = email,
                    phone = if (phone.startsWith("+")) phone else "$countryCode $phone",
                    password = password,
                    country = country,
                    countryCode = countryCode,
                    division = division,
                    district = district,
                    address = address,
                    profileImageUri = profileImageUri
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "সাইনআপ ব্যর্থ হয়েছে")
            }
        }
    }

    fun registerWorker(
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
        profileImageUri: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (name.isBlank() || email.isBlank() || phone.isBlank() || profession.isBlank()) {
            onError("দয়া করে নাম, ইমেইল, ফোন এবং পেশা সঠিকভাবে পূরণ করুন")
            return
        }
        viewModelScope.launch {
            try {
                repository.signUpWorker(
                    name = name,
                    email = email,
                    phone = if (phone.startsWith("+")) phone else "$countryCode $phone",
                    password = password,
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
                    profileImageUri = profileImageUri
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "সাইনআপ ব্যর্থ হয়েছে")
            }
        }
    }

    fun login(email: String, password: String? = null, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = repository.login(email.trim(), password)
                if (user != null) {
                    onSuccess()
                } else {
                    onError("এই ইমেইল দিয়ে কোনো অ্যাকাউন্ট পাওয়া যায়নি")
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "লগইন ব্যর্থ হয়েছে")
            }
        }
    }

    fun loginWithGoogle(
        email: String,
        name: String,
        photoUrl: String?,
        country: String,
        countryCode: String,
        division: String,
        district: String,
        userType: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.loginWithGoogle(
                    email = email,
                    name = name,
                    photoUrl = photoUrl,
                    country = country,
                    countryCode = countryCode,
                    division = division,
                    district = district,
                    userType = userType
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "গুগল সাইন-ইন ব্যর্থ হয়েছে")
            }
        }
    }

    fun updateCustomerProfile(
        name: String,
        phone: String,
        division: String,
        district: String,
        address: String,
        profileImageUri: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = currentUser.value
        if (user == null) {
            onError("অনুগ্রহ করে লগইন করুন")
            return
        }
        viewModelScope.launch {
            try {
                val updated = user.copy(
                    name = name.trim(),
                    phone = phone.trim(),
                    division = division.trim(),
                    district = district.trim(),
                    address = address.trim(),
                    profileImageUri = profileImageUri ?: user.profileImageUri
                )
                repository.updateUserProfile(updated)
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "প্রোফাইল আপডেট করতে ব্যর্থ হয়েছে")
            }
        }
    }

    fun logout() {
        repository.logout()
    }

    // Review Submission (Only logged-in customers)
    fun submitReview(
        mistriId: String,
        rating: Int,
        reviewText: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = currentUser.value
        if (user == null) {
            onError("রিভিউ দেওয়ার জন্য অনুগ্রহ করে লগইন করুন")
            return
        }
        if (reviewText.isBlank()) {
            onError("দয়া করে আপনার মতামত লিখুন")
            return
        }
        viewModelScope.launch {
            val ok = repository.submitReview(mistriId, rating, reviewText.trim())
            if (ok) onSuccess() else onError("রিভিউ সেভ করতে সমস্যা হয়েছে")
        }
    }

    // Subscription Purchase
    fun purchaseSubscription(
        mistri: MistriEntity,
        planName: String,
        amount: String,
        paymentMethod: String,
        transactionId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (transactionId.isBlank()) {
            onError("দয়া করে ট্রানজেকশন আইডি (TrxID) বা কার্ড বিবরণ লিখুন")
            return
        }
        viewModelScope.launch {
            try {
                repository.submitSubscription(
                    mistriId = mistri.id,
                    mistriName = mistri.name,
                    planName = planName,
                    amount = amount,
                    paymentMethod = paymentMethod,
                    transactionId = transactionId,
                    country = mistri.country
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "পেমেন্ট রিকোয়েস্ট পাঠাতে সমস্যা হয়েছে")
            }
        }
    }

    // Admin Operations
    fun unlockAdmin(codeOrEmail: String): Boolean {
        val trimmed = codeOrEmail.trim()
        if (trimmed == "HK@9934#BOSS" ||
            trimmed.equals("safuanislam2276@gmail.com", ignoreCase = true) ||
            currentUser.value?.email?.equals("safuanislam2276@gmail.com", ignoreCase = true) == true
        ) {
            _isAdminUnlocked.value = true
            return true
        }
        return false
    }

    fun lockAdmin() {
        _isAdminUnlocked.value = false
    }

    fun updateThemeColor(hex: String) {
        viewModelScope.launch {
            val validHex = if (hex.startsWith("#")) hex else "#$hex"
            repository.updateAdminSetting("app_theme_color", validHex)
        }
    }

    fun updateBannerSettings(
        active: Boolean,
        titleBn: String,
        titleEn: String,
        noticeBn: String,
        noticeEn: String,
        imageUrl: String
    ) {
        viewModelScope.launch {
            repository.updateAdminSetting("banner_active", active.toString())
            repository.updateAdminSetting("banner_title_bn", titleBn.trim())
            repository.updateAdminSetting("banner_title_en", titleEn.trim())
            repository.updateAdminSetting("banner_notice_bn", noticeBn.trim())
            repository.updateAdminSetting("banner_notice_en", noticeEn.trim())
            repository.updateAdminSetting("banner_image_url", imageUrl.trim())
        }
    }

    fun updateAdSettings(
        adContactEmail: String,
        adSponsorBrand: String,
        contactAdEnabled: Boolean,
        contactAdBrand: String,
        contactAdTitle: String,
        contactAdDesc: String
    ) {
        viewModelScope.launch {
            repository.updateAdminSetting("ad_contact_email", adContactEmail.trim())
            repository.updateAdminSetting("ad_sponsor_brand", adSponsorBrand.trim())
            repository.updateAdminSetting("contact_ad_enabled", contactAdEnabled.toString())
            repository.updateAdminSetting("contact_ad_brand", contactAdBrand.trim())
            repository.updateAdminSetting("contact_ad_title", contactAdTitle.trim())
            repository.updateAdminSetting("contact_ad_desc", contactAdDesc.trim())
        }
    }

    fun updateAdminSetting(key: String, value: String) {
        viewModelScope.launch {
            repository.updateAdminSetting(key, value.trim())
        }
    }

    fun approveSubscription(sub: SubscriptionEntity) {
        viewModelScope.launch {
            repository.approveSubscription(sub.id, sub.mistriId, sub.planName)
        }
    }

    fun rejectSubscription(subId: String) {
        viewModelScope.launch {
            repository.rejectSubscription(subId)
        }
    }

    fun toggleMistriBlock(mistri: MistriEntity) {
        viewModelScope.launch {
            repository.toggleBlockMistri(mistri.id, !mistri.isBlocked)
        }
    }

    fun toggleDirectPro(mistri: MistriEntity) {
        viewModelScope.launch {
            if (mistri.isPro) {
                // Remove PRO
                repository.updateMistriProfile(mistri.copy(isPro = false, proTier = null, proExpiresAt = null))
            } else {
                // Grant PRO for 30 days
                val expiry = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)
                repository.updateMistriProfile(mistri.copy(isPro = true, proTier = "Pro", proExpiresAt = expiry))
            }
        }
    }

    fun updatePaymentNumber(key: String, number: String) {
        viewModelScope.launch {
            repository.updateAdminSetting(key, number.trim())
        }
    }

    fun updateMistriProfile(
        mistri: MistriEntity,
        name: String,
        phone: String,
        profession: String,
        extraCategories: String? = null,
        experienceYears: Int,
        bio: String,
        skills: String,
        division: String? = null,
        district: String? = null,
        address: String? = null,
        profileImageUri: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val updated = mistri.copy(
                name = name.trim(),
                phone = phone.trim(),
                profession = profession.trim(),
                extraCategories = extraCategories ?: mistri.extraCategories,
                experienceYears = experienceYears,
                bio = bio.trim(),
                skills = skills.trim(),
                division = division ?: mistri.division,
                district = district ?: mistri.district,
                address = address ?: mistri.address,
                profileImageUri = profileImageUri ?: mistri.profileImageUri
            )
            repository.updateMistriProfile(updated)
            val user = currentUser.value
            if (user != null && user.id == mistri.userId) {
                repository.updateUserProfile(
                    user.copy(
                        name = name.trim(),
                        phone = phone.trim(),
                        division = division ?: user.division,
                        district = district ?: user.district,
                        address = address ?: user.address,
                        profileImageUri = profileImageUri ?: user.profileImageUri
                    )
                )
            }
            onSuccess()
        }
    }

    // ==========================================
    // MESSENGER CHAT FUNCTIONALITY
    // ==========================================
    private val _activeConversationId = MutableStateFlow<String?>(null)
    val activeConversationId: StateFlow<String?> = _activeConversationId.asStateFlow()

    private val _activeChatRecipient = MutableStateFlow<ChatRecipientInfo?>(null)
    val activeChatRecipient: StateFlow<ChatRecipientInfo?> = _activeChatRecipient.asStateFlow()

    val activeChatMessages: StateFlow<List<ChatMessageEntity>> = _activeConversationId.flatMapLatest { convId ->
        if (convId != null) repository.getMessagesForConversationFlow(convId) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _onlinePresenceMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val onlinePresenceMap: StateFlow<Map<String, Boolean>> = _onlinePresenceMap.asStateFlow()

    fun isUserOnline(userId: String): Boolean {
        return _onlinePresenceMap.value[userId] ?: (kotlin.math.abs(userId.hashCode()) % 4 != 1)
    }

    fun setUserOnlineStatus(userId: String, isOnline: Boolean) {
        _onlinePresenceMap.update { it + (userId to isOnline) }
    }

    val userConversations: StateFlow<List<ConversationSummary>> = currentUser.flatMapLatest { u ->
        val uid = u?.id ?: "u-customer-demo"
        val email = u?.email ?: "tanvir.customer@gmail.com"
        repository.getAllMessagesForUserFlow(uid, email).map { messages ->
            messages.groupBy { it.conversationId }.map { (convId, msgList) ->
                val latest = msgList.maxByOrNull { it.timestamp } ?: msgList.first()
                val isMeSender = latest.senderId == uid || (email.isNotBlank() && latest.senderEmail.equals(email, ignoreCase = true))
                val otherId = if (isMeSender) latest.receiverId else latest.senderId
                val otherName = if (isMeSender) latest.receiverName else latest.senderName
                val otherEmail = if (isMeSender) latest.receiverEmail else latest.senderEmail
                val otherRole = if (isMeSender) latest.receiverRole else latest.senderRole
                val unreadCount = msgList.count { !it.isRead && (it.receiverId == uid || (email.isNotBlank() && it.receiverEmail.equals(email, ignoreCase = true))) }
                val isOnline = _onlinePresenceMap.value[otherId] ?: (kotlin.math.abs(otherId.hashCode()) % 4 != 1)

                ConversationSummary(
                    conversationId = convId,
                    otherUserId = otherId,
                    otherUserName = otherName,
                    otherUserEmail = otherEmail,
                    otherUserRole = otherRole,
                    lastMessage = latest.messageText,
                    lastTimestamp = latest.timestamp,
                    unreadCount = unreadCount,
                    isOnline = isOnline
                )
            }.sortedByDescending { it.lastTimestamp }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadChatCount: StateFlow<Int> = currentUser.flatMapLatest { u ->
        val uid = u?.id ?: "u-customer-demo"
        val email = u?.email ?: "tanvir.customer@gmail.com"
        repository.getUnreadChatCountFlow(uid, email)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun openChatWithMistri(mistri: MistriEntity) {
        val user = currentUser.value
        val myId = user?.id ?: "u-customer-demo"
        val targetId = mistri.userId.ifBlank { mistri.id }
        val sortedIds = listOf(myId, targetId).sorted()
        val convId = "conv_${sortedIds[0]}_${sortedIds[1]}"
        val isOnline = _onlinePresenceMap.value[targetId] ?: (kotlin.math.abs(targetId.hashCode()) % 4 != 1)
        _activeConversationId.value = convId
        _activeChatRecipient.value = ChatRecipientInfo(
            id = targetId,
            name = mistri.name,
            email = mistri.email,
            role = "MISTRI",
            phone = mistri.phone,
            profession = mistri.profession,
            imageUri = mistri.profileImageUri,
            isOnline = isOnline
        )
        viewModelScope.launch {
            repository.markChatAsRead(convId, myId)
        }
    }

    fun openChatConversation(summary: ConversationSummary) {
        _activeConversationId.value = summary.conversationId
        _activeChatRecipient.value = ChatRecipientInfo(
            id = summary.otherUserId,
            name = summary.otherUserName,
            email = summary.otherUserEmail,
            role = summary.otherUserRole,
            isOnline = summary.isOnline
        )
        val myId = currentUser.value?.id ?: "u-customer-demo"
        viewModelScope.launch {
            repository.markChatAsRead(summary.conversationId, myId)
        }
    }

    fun openChatWithUser(
        targetId: String,
        targetName: String,
        targetEmail: String,
        targetRole: String,
        phone: String? = null,
        profession: String? = null,
        imageUri: String? = null
    ) {
        val user = currentUser.value
        val myId = user?.id ?: "u-customer-demo"
        val actualTargetId = targetId.ifBlank { targetEmail }
        val sortedIds = listOf(myId, actualTargetId).sorted()
        val convId = "conv_${sortedIds[0]}_${sortedIds[1]}"
        val isOnline = _onlinePresenceMap.value[actualTargetId] ?: (kotlin.math.abs(actualTargetId.hashCode()) % 4 != 1)
        _activeConversationId.value = convId
        _activeChatRecipient.value = ChatRecipientInfo(
            id = actualTargetId,
            name = targetName,
            email = targetEmail,
            role = targetRole,
            phone = phone,
            profession = profession,
            imageUri = imageUri,
            isOnline = isOnline
        )
        viewModelScope.launch {
            repository.markChatAsRead(convId, myId)
        }
    }

    suspend fun getUserAutoReply(): String? {
        val user = currentUser.value ?: return null
        return repository.getAutoReplyForUser(user.id, user.email)
    }

    fun saveUserAutoReply(message: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.setAutoReplyForUser(user.id, user.email, message)
        }
    }

    fun sendChatMessage(text: String) {
        val recipient = _activeChatRecipient.value ?: return
        val convId = _activeConversationId.value ?: return
        val user = currentUser.value
        val myId = user?.id ?: "u-customer-demo"
        val myName = user?.name ?: "সম্মানিত গ্রাহক"
        val myEmail = user?.email ?: "customer@gmail.com"
        val myRole = user?.userType ?: "CUSTOMER"

        viewModelScope.launch {
            repository.sendChatMessage(
                conversationId = convId,
                senderId = myId,
                senderName = myName,
                senderEmail = myEmail,
                senderRole = myRole,
                receiverId = recipient.id,
                receiverName = recipient.name,
                receiverEmail = recipient.email,
                receiverRole = recipient.role,
                text = text
            )
        }
    }

    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            repository.deleteConversation(conversationId)
            if (_activeConversationId.value == conversationId) {
                _activeConversationId.value = null
                _activeChatRecipient.value = null
            }
        }
    }

    fun deleteChatMessage(messageId: String) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }

    // ==========================================
    // IN-APP MESSENGER CALLING
    // ==========================================
    private val _activeCallState = MutableStateFlow<ActiveCallState?>(null)
    val activeCallState: StateFlow<ActiveCallState?> = _activeCallState.asStateFlow()

    private var callTimerJob: kotlinx.coroutines.Job? = null

    fun startInAppCall(
        name: String,
        role: String,
        phone: String,
        profession: String? = null,
        imageUri: String? = null,
        targetUserId: String? = null,
        targetEmail: String? = null,
        conversationId: String? = null
    ) {
        callTimerJob?.cancel()
        _activeCallState.value = ActiveCallState(
            recipientName = name,
            recipientRole = role,
            recipientPhone = phone,
            recipientProfession = profession,
            recipientImage = imageUri,
            targetUserId = targetUserId,
            targetEmail = targetEmail,
            conversationId = conversationId,
            status = "CALLING",
            durationSeconds = 0,
            isMuted = false,
            isSpeakerOn = false
        )

        callTimerJob = viewModelScope.launch {
            kotlinx.coroutines.delay(2500)
            _activeCallState.update { current ->
                current?.copy(status = "CONNECTED")
            }
            while (currentCoroutineContext().isActive) {
                kotlinx.coroutines.delay(1000)
                _activeCallState.update { current ->
                    if (current != null && current.status == "CONNECTED") {
                        current.copy(durationSeconds = current.durationSeconds + 1)
                    } else current
                }
            }
        }
    }

    fun toggleCallMute() {
        _activeCallState.update { current ->
            current?.copy(isMuted = !current.isMuted)
        }
    }

    fun toggleCallSpeaker() {
        _activeCallState.update { current ->
            current?.copy(isSpeakerOn = !current.isSpeakerOn)
        }
    }

    fun endInAppCall() {
        val callState = _activeCallState.value
        callTimerJob?.cancel()
        callTimerJob = null
        _activeCallState.update { current ->
            current?.copy(status = "ENDED")
        }

        if (callState != null) {
            val user = currentUser.value
            val myId = user?.id ?: "u-customer-demo"
            val myName = user?.name ?: "সম্মানিত ব্যবহারকারী"
            val myEmail = user?.email ?: "tanvir.customer@gmail.com"
            val myRole = user?.userType ?: "CUSTOMER"

            val targetId = callState.targetUserId 
                ?: _activeChatRecipient.value?.id 
                ?: "target_${callState.recipientName.hashCode()}"
            val targetName = callState.recipientName
            val targetEmail = callState.targetEmail ?: _activeChatRecipient.value?.email ?: ""
            val targetRole = callState.recipientRole

            val convId = callState.conversationId 
                ?: _activeConversationId.value 
                ?: run {
                    val sorted = listOf(myId, targetId).sorted()
                    "conv_${sorted[0]}_${sorted[1]}"
                }

            val duration = callState.durationSeconds
            val durationStr = if (duration > 0) {
                val mins = duration / 60
                val secs = duration % 60
                if (mins > 0) String.format(java.util.Locale.getDefault(), "%02d মিনিট %02d সেকেন্ড", mins, secs)
                else String.format(java.util.Locale.getDefault(), "%02d সেকেন্ড", secs)
            } else {
                "সমাপ্ত"
            }

            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy • hh:mm a", java.util.Locale.getDefault())
            val dateStr = dateFormat.format(java.util.Date())

            val callLogText = "📞 ভয়েস কল • $durationStr • $dateStr"

            viewModelScope.launch {
                repository.sendChatMessage(
                    conversationId = convId,
                    senderId = myId,
                    senderName = myName,
                    senderEmail = myEmail,
                    senderRole = myRole,
                    receiverId = targetId,
                    receiverName = targetName,
                    receiverEmail = targetEmail,
                    receiverRole = targetRole,
                    text = callLogText
                )
            }
        }

        viewModelScope.launch {
            kotlinx.coroutines.delay(800)
            _activeCallState.value = null
        }
    }
}

data class ActiveCallState(
    val recipientName: String,
    val recipientRole: String,
    val recipientPhone: String,
    val recipientImage: String? = null,
    val recipientProfession: String? = null,
    val targetUserId: String? = null,
    val targetEmail: String? = null,
    val conversationId: String? = null,
    val status: String = "CALLING", // "CALLING", "CONNECTED", "ENDED"
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = false
)

data class ChatRecipientInfo(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val phone: String? = null,
    val profession: String? = null,
    val imageUri: String? = null,
    val isOnline: Boolean = true
)
