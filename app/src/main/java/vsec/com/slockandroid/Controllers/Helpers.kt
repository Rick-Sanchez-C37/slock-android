package vsec.com.slockandroid.Controllers

import vsec.com.slockandroid.generalModels.PasswordScore
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

object Helpers {
    private var secureRandom: SecureRandom = SecureRandom()

    fun makeSha512Hash(payload: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest((payload + salt).toByteArray())
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun checkEmailIsValid(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkPasswordIsStrong(passwordScore: Int): PasswordScore{
        if(passwordScore in 0..12)
            return PasswordScore.WEAK
        if(passwordScore in 13..20)
            return PasswordScore.AVERAGE
        if(passwordScore in 21..33)
            return PasswordScore.STRONG
        if(passwordScore in 34..55)
            return PasswordScore.MARVELOUS

        return PasswordScore.WEAK
    }

    fun newBase64Uuid(): String{
        val input = UUID.randomUUID().toString().filterNot { it == '-' }
        val bytes = input.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        val uuid = Base64.getEncoder().encodeToString(bytes)

        return uuid.toString()
    }

    fun newBase64Token(size: Int = 16): String {
        val bytes: ByteArray = ByteArray(size)
        secureRandom.nextBytes(bytes)
        val token = Base64.getEncoder().encodeToString(bytes)
        return token
    }
}