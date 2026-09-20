package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val phone: String,
    val country: String,
    val countryCode: String,
    val division: String,
    val district: String,
    val address: String = "",
    val userType: String, // "CUSTOMER", "MISTRI", "ADMIN"
    val profileImageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "mistris")
data class MistriEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val country: String,
    val countryCode: String,
    val division: String,
    val district: String,
    val address: String = "",
    val profession: String, // e.g., "ইলেকট্রিশিয়ান", "প্লাম্বার", "কাঠমিস্ত্রি" (Primary Category 1)
    val extraCategories: String = "", // Comma-separated Category 2, Category 3, Category 4, Category 5, Category 6
    val experienceYears: Int,
    val bio: String,
    val skills: String, // comma-separated tags
    val profileImageUri: String? = null,
    val isVerified: Boolean = true,
    val isPro: Boolean = false,
    val proTier: String? = null, // "Starter", "Pro", "Premium"
    val proExpiresAt: Long? = null,
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val callClicks: Int = 0,
    val waClicks: Int = 0,
    val isBlocked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

fun MistriEntity.getAllCategoriesList(): List<String> {
    val list = mutableListOf<String>()
    if (profession.isNotBlank()) list.add(profession.trim())
    if (extraCategories.isNotBlank()) {
        extraCategories.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() && !list.contains(it) }
            .forEach { list.add(it) }
    }
    return list
}

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val mistriId: String,
    val customerId: String,
    val customerName: String,
    val rating: Int,
    val reviewText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val senderEmail: String = "",
    val senderRole: String = "CUSTOMER", // "CUSTOMER", "MISTRI", "ADMIN"
    val receiverId: String,
    val receiverName: String,
    val receiverEmail: String = "",
    val receiverRole: String = "MISTRI", // "CUSTOMER", "MISTRI", "ADMIN"
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val userEmail: String = "" // For restoring all chats when logging in with email
)

data class ConversationSummary(
    val conversationId: String,
    val otherUserId: String,
    val otherUserName: String,
    val otherUserEmail: String,
    val otherUserRole: String,
    val lastMessage: String,
    val lastTimestamp: Long,
    val unreadCount: Int,
    val isOnline: Boolean = true
)

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val id: String,
    val mistriId: String,
    val mistriName: String,
    val planName: String,
    val amount: String,
    val paymentMethod: String, // "bKash", "Nagad", "Rocket", "Card"
    val transactionId: String,
    val status: String, // "pending", "completed", "rejected"
    val createdAt: Long = System.currentTimeMillis(),
    val confirmedAt: Long? = null
)

@Entity(tableName = "admin_settings")
data class AdminSettingEntity(
    @PrimaryKey val key: String,
    val value: String,
    val description: String = ""
)

@Entity(tableName = "analytics_events")
data class AnalyticsEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventType: String, // "app_open", "country_view", "signup_started", "signup_completed", "mistri_view", "call_click", "wa_click", "payment_initiated"
    val country: String,
    val targetId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class CountryStat(
    val country: String,
    val count: Int
)
