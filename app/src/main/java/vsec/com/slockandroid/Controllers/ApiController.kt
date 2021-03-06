package vsec.com.slockandroid.Controllers

import android.content.Context
import android.content.SharedPreferences
import vsec.com.slockandroid.generalModels.*
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection
const val SESSION_TOKEN_PREFERENCE = "session-token-preference"

object ApiController {
    private val apiDomain: String =  "slock.wtf"
    private val apiPort: Int = 443//54319
    private var sessionToken: String = ""
    private lateinit var slockPreferences: SharedPreferences

    fun initApiController(context: Context){
        this.slockPreferences = context.getSharedPreferences("slock",Context.MODE_PRIVATE)
        this.sessionToken = this.slockPreferences.getString(SESSION_TOKEN_PREFERENCE,"") as String
    }

    fun LogoutUser(): String {
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/logout")

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "GET"

            setRequestProperty("charset", "utf-8")
            setRequestProperty("token", ApiController.sessionToken )

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                ApiController.clearSession()
            }
            return responseCode.toString()
        }
    }

    fun loginUser(user: User): String {
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/login")

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "POST"

            val postData: ByteArray = user.toJSON().toByteArray(StandardCharsets.UTF_8)
            setRequestProperty("charset", "utf-8")
            setRequestProperty("content-length", postData.size.toString())
            setRequestProperty("Content-Type", "application/json")
            try{
                val outputStream: DataOutputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            }catch (exeption: Exception){
                exeption.printStackTrace()
            }

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                try {
                    val reader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
                    ApiController.setSessionToken(reader.readLine())
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
            return responseCode.toString()
        }
    }

    fun registerUser(user: User): String {
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/register")

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "POST"

            val postData: ByteArray = user.toJSON().toByteArray(StandardCharsets.UTF_8)
            setRequestProperty("charset", "utf-8")
            setRequestProperty("content-length", postData.size.toString())
            setRequestProperty("Content-Type", "application/json")
            try{
                val outputStream: DataOutputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            }catch (exeption: Exception){
                exeption.printStackTrace()
            }

            return responseCode.toString()
        }
    }

    fun registerLock(lock: Lock): String {
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/locks/activate")

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "POST"

            val postData: ByteArray = lock.toJSON().toByteArray(StandardCharsets.UTF_8)
            setRequestProperty("charset", "utf-8")
            setRequestProperty("content-length", postData.size.toString())
            setRequestProperty("Content-Type", "application/json")
            try{
                val outputStream: DataOutputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            }catch (exeption: Exception){
                exeption.printStackTrace()
            }

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                try {
                    val reader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
                    ApiController.setSessionToken(reader.readLine())
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
            return responseCode.toString()
        }
    }

    fun ChangeDetails(changeObject: ChangePasswordModel): String? {
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/changeDetails")
        val json: String = changeObject.toJSON()
        val postData: ByteArray = json.toByteArray(StandardCharsets.UTF_8)

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "POST"

            setRequestProperty("charset", "utf-8")
            setRequestProperty("content-length", postData.size.toString())
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("token", ApiController.sessionToken )

            try{
                val outputStream: DataOutputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            }catch (exception: Exception){
                exception.printStackTrace()
            }
            return responseCode.toString()
        }
    }

    fun clearSession() {
        this.sessionToken = ""
        this.slockPreferences.edit().remove(SESSION_TOKEN_PREFERENCE).apply()
    }

    private fun setSessionToken(token: String){
        this.sessionToken = token
        this.slockPreferences.edit().putString(SESSION_TOKEN_PREFERENCE, token).apply()
    }

    fun hasSessionToken(): Boolean{
        return this.sessionToken.isNotEmpty()
    }

    fun GetAccessibleLocks(path: String): String {
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/me" + path)
        var jsonData: String? = null
        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "GET"

            setRequestProperty("charset", "utf-8")
            setRequestProperty("token", ApiController.sessionToken )

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                try {
                    val reader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
                    jsonData = reader.readLine().toString()

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }

            if(jsonData != null){
                return jsonData as String
            }
            return responseCode.toString()
        }
    }
    

    fun GetLockToken(lock: Lock, command: Int?): String {
        if(lock.getId() == null || command == null){
            return "500"
        }
        var response: String? = null
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/locks/" + lock.getId() + "/token")

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "GET"

            setRequestProperty("charset", "utf-8")
            setRequestProperty("token", ApiController.sessionToken )

            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                try {
                    val reader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
                    response = reader.readLine().toString()

                } catch (exception: Exception) {
                    exception.printStackTrace()

                }
            }
            if(response != null){
                return response as String + ";" + command
            }
                return responseCode.toString()
        }
    }

    fun doRatchetTick(lockId: Int): String {
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/locks/" + lockId + "/ratchettick")

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "GET"

            setRequestProperty("charset", "utf-8")
            setRequestProperty("token", ApiController.sessionToken )

            return responseCode.toString()
        }
    }

    fun resyncRatchet(lockId: Int, ratchetSyncBody: RatchetSyncBody): String{
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/locks/" + lockId + "/ratchetsync")
        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "POST"
            var json = ratchetSyncBody.toJSON()
            val postData: ByteArray = json.toByteArray(StandardCharsets.UTF_8)
            setRequestProperty("charset", "utf-8")
            setRequestProperty("content-length", postData.size.toString())
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("token", ApiController.sessionToken)
            try{
                val outputStream: DataOutputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            }catch (exeption: Exception){
                exeption.printStackTrace()
            }

            return responseCode.toString()
        }
    }

    fun rentAlock(lockRentBody: LockRentBody): String{
        val url = URL("https://" + this.apiDomain + ":" + this.apiPort + "/v1/locks/" + lockRentBody.getId() + "/share")

        with(url.openConnection() as HttpsURLConnection) {
            sslSocketFactory = KeyStoreController.sslContext.socketFactory
            requestMethod = "POST"
            var json = lockRentBody.toJSON()
            val postData: ByteArray = json.toByteArray(StandardCharsets.UTF_8)
            setRequestProperty("charset", "utf-8")
            setRequestProperty("content-length", postData.size.toString())
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("token", ApiController.sessionToken)
            try{
                val outputStream: DataOutputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            }catch (exeption: Exception){
                exeption.printStackTrace()
            }

            return responseCode.toString()
        }
    }
}

