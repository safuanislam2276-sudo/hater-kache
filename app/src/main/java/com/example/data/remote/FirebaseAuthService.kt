package com.example.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class FirebaseUserResult(
    val localId: String,
    val email: String,
    val idToken: String
)

class FirebaseAuthService {

    companion object {
        const val FIREBASE_API_KEY = "AIzaSyAsan9kd-93p302brYhVxa9nWvn-f6GYy0"
        const val FIREBASE_PROJECT_ID = "hater-kache-15889"
        const val AUTH_DOMAIN = "hater-kache-15889.firebaseapp.com"
        private const val BASE_URL = "https://identitytoolkit.googleapis.com/v1"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUserResult> = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/accounts:signInWithPassword?key=$FIREBASE_API_KEY"
            val payload = JSONObject().apply {
                put("email", email.trim())
                put("password", password.trim())
                put("returnSecureToken", true)
            }.toString()

            val request = Request.Builder()
                .url(url)
                .post(payload.toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val json = JSONObject(bodyString)
                    Result.success(
                        FirebaseUserResult(
                            localId = json.optString("localId"),
                            email = json.optString("email"),
                            idToken = json.optString("idToken")
                        )
                    )
                } else {
                    val errMsg = parseFirebaseError(bodyString)
                    Result.failure(Exception(errMsg))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUserResult> = withContext(Dispatchers.IO) {
        try {
            val url = "$BASE_URL/accounts:signUp?key=$FIREBASE_API_KEY"
            val payload = JSONObject().apply {
                put("email", email.trim())
                put("password", password.trim())
                put("returnSecureToken", true)
            }.toString()

            val request = Request.Builder()
                .url(url)
                .post(payload.toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val json = JSONObject(bodyString)
                    Result.success(
                        FirebaseUserResult(
                            localId = json.optString("localId"),
                            email = json.optString("email"),
                            idToken = json.optString("idToken")
                        )
                    )
                } else {
                    val errMsg = parseFirebaseError(bodyString)
                    Result.failure(Exception(errMsg))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseFirebaseError(responseBody: String): String {
        return try {
            val json = JSONObject(responseBody)
            val errObj = json.optJSONObject("error")
            val message = errObj?.optString("message") ?: ""
            when {
                message.contains("EMAIL_NOT_FOUND") -> "এই ইমেইল দিয়ে কোনো অ্যাকাউন্ট পাওয়া যায়নি।"
                message.contains("INVALID_PASSWORD") || message.contains("INVALID_LOGIN_CREDENTIALS") -> "ভুল পাসওয়ার্ড দেওয়া হয়েছে।"
                message.contains("EMAIL_EXISTS") -> "এই ইমেইলটি ইতোমধ্যে নিবন্ধিত রয়েছে।"
                message.contains("WEAK_PASSWORD") -> "পাসওয়ার্ড অন্তত ৬ অক্ষরের হতে হবে।"
                message.contains("INVALID_EMAIL") -> "সঠিক ফরম্যাটে ইমেইল লিখুন।"
                message.contains("USER_DISABLED") -> "অ্যাডমিন কর্তৃক এই অ্যাকাউন্টটি স্থগিত রাখা হয়েছে।"
                message.isNotEmpty() -> "Firebase Auth ত্রুটি: $message"
                else -> "প্রমাণীকরণ ব্যর্থ হয়েছে।"
            }
        } catch (e: Exception) {
            "অনলাইন সার্ভারের সাথে সংযোগ স্থাপন করা সম্ভব হয়নি।"
        }
    }
}
