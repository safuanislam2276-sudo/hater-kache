package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users WHERE userType = 'CUSTOMER'")
    fun getCustomerCountFlow(): Flow<Int>

    @Query("SELECT * FROM users WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' LIMIT 30")
    fun searchUsersFlow(query: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsersFlow(): Flow<List<UserEntity>>
}

@Dao
interface MistriDao {
    // PRO First, then by rating, then by review count
    @Query("""
        SELECT * FROM mistris 
        WHERE isBlocked = 0 
        ORDER BY isPro DESC, averageRating DESC, totalReviews DESC
    """)
    fun getActiveMistrisProFirst(): Flow<List<MistriEntity>>

    @Query("SELECT * FROM mistris ORDER BY isPro DESC, averageRating DESC")
    fun getAllMistris(): Flow<List<MistriEntity>>

    @Query("SELECT * FROM mistris WHERE id = :id LIMIT 1")
    suspend fun getMistriById(id: String): MistriEntity?

    @Query("SELECT * FROM mistris WHERE id = :id LIMIT 1")
    fun getMistriByIdFlow(id: String): Flow<MistriEntity?>

    @Query("SELECT * FROM mistris WHERE userId = :userId LIMIT 1")
    suspend fun getMistriByUserId(userId: String): MistriEntity?

    @Query("SELECT * FROM mistris WHERE email = :email LIMIT 1")
    suspend fun getMistriByEmail(email: String): MistriEntity?

    @Query("SELECT * FROM mistris WHERE userId = :userId LIMIT 1")
    fun getMistriByUserIdFlow(userId: String): Flow<MistriEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistri(mistri: MistriEntity)

    @Update
    suspend fun updateMistri(mistri: MistriEntity)

    @Query("UPDATE mistris SET isBlocked = :isBlocked WHERE id = :id")
    suspend fun setBlockedStatus(id: String, isBlocked: Boolean)

    @Query("UPDATE mistris SET isPro = :isPro, proTier = :tier, proExpiresAt = :expiresAt WHERE id = :id")
    suspend fun updateProStatus(id: String, isPro: Boolean, tier: String?, expiresAt: Long?)

    @Query("UPDATE mistris SET callClicks = callClicks + 1 WHERE id = :id")
    suspend fun incrementCallClick(id: String)

    @Query("UPDATE mistris SET waClicks = waClicks + 1 WHERE id = :id")
    suspend fun incrementWaClick(id: String)

    @Query("UPDATE mistris SET averageRating = :avgRating, totalReviews = :totalReviews WHERE id = :id")
    suspend fun updateRatingAndReviews(id: String, avgRating: Double, totalReviews: Int)

    @Query("SELECT COUNT(*) FROM mistris WHERE isBlocked = 0")
    fun getActiveWorkerCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mistris")
    fun getTotalWorkerCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mistris WHERE isBlocked = 1")
    fun getBlockedWorkerCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mistris WHERE isPro = 1")
    fun getProWorkerCountFlow(): Flow<Int>
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE mistriId = :mistriId ORDER BY createdAt DESC")
    fun getReviewsForMistri(mistriId: String): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Query("SELECT COUNT(*) FROM reviews")
    fun getTotalReviewCountFlow(): Flow<Int>

    @Query("SELECT AVG(rating) FROM reviews WHERE mistriId = :mistriId")
    suspend fun getAverageRatingForMistri(mistriId: String): Double?

    @Query("SELECT COUNT(*) FROM reviews WHERE mistriId = :mistriId")
    suspend fun getReviewCountForMistri(mistriId: String): Int
}

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions ORDER BY createdAt DESC")
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE status = 'pending' ORDER BY createdAt DESC")
    fun getPendingSubscriptions(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE mistriId = :mistriId ORDER BY createdAt DESC")
    fun getSubscriptionsForMistri(mistriId: String): Flow<List<SubscriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(sub: SubscriptionEntity)

    @Query("UPDATE subscriptions SET status = :status, confirmedAt = :confirmedAt WHERE id = :id")
    suspend fun updateSubscriptionStatus(id: String, status: String, confirmedAt: Long?)
}

@Dao
interface AdminSettingDao {
    @Query("SELECT * FROM admin_settings")
    fun getAllSettingsFlow(): Flow<List<AdminSettingEntity>>

    @Query("SELECT value FROM admin_settings WHERE key = :key LIMIT 1")
    suspend fun getSettingValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: AdminSettingEntity)
}

@Dao
interface AnalyticsDao {
    @Insert
    suspend fun insertEvent(event: AnalyticsEventEntity)

    @Query("""
        SELECT country, COUNT(*) as count 
        FROM analytics_events 
        WHERE eventType = 'country_view' OR eventType = 'app_open' 
        GROUP BY country 
        ORDER BY count DESC
    """)
    fun getViewsByCountry(): Flow<List<CountryStat>>

    @Query("""
        SELECT country, COUNT(*) as count 
        FROM analytics_events 
        WHERE eventType = 'signup_completed' 
        GROUP BY country 
        ORDER BY count DESC
    """)
    fun getSignupsByCountry(): Flow<List<CountryStat>>

    @Query("SELECT * FROM analytics_events ORDER BY timestamp DESC LIMIT 100")
    fun getRecentEvents(): Flow<List<AnalyticsEventEntity>>
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversationFlow(conversationId: String): Flow<List<ChatMessageEntity>>

    @Query("""
        SELECT * FROM chat_messages 
        WHERE senderId = :userId OR receiverId = :userId 
           OR (userEmail != '' AND (senderEmail = :userEmail OR receiverEmail = :userEmail))
        ORDER BY timestamp DESC
    """)
    fun getAllMessagesForUserFlow(userId: String, userEmail: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("UPDATE chat_messages SET isRead = 1 WHERE conversationId = :conversationId AND receiverId = :userId")
    suspend fun markAsRead(conversationId: String, userId: String)

    @Query("SELECT COUNT(*) FROM chat_messages WHERE (receiverId = :userId OR (receiverEmail = :userEmail AND receiverEmail != '')) AND isRead = 0")
    fun getUnreadCountFlow(userId: String, userEmail: String): Flow<Int>

    @Query("DELETE FROM chat_messages WHERE conversationId = :conversationId")
    suspend fun deleteConversation(conversationId: String)

    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String)
}

