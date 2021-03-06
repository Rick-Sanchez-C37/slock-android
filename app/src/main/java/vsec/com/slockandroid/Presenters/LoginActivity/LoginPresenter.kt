package vsec.com.slockandroid.Presenters.LoginActivity

import android.os.AsyncTask
import vsec.com.slockandroid.Controllers.ApiController
import vsec.com.slockandroid.Controllers.Helpers
import vsec.com.slockandroid.Presenters.HomeActivity.HomeView
import vsec.com.slockandroid.generalModels.User

class LoginPresenter(private val view: View) {

    private var user: User = User()
    private var task: Task = Task(this.view)

    fun updateEmail(email: String) {
        user.setEmail(email)

    }

    fun updatePassword(password: String) {
        user.setHashedPassword(Helpers.makeSha512Hash(password,User.salt))
    }

    fun sendLoginRequestToApi(){
        this.task.execute(this.user)
        this.task = Task(this.view)
    }

    fun checkEmailValid(email: String): Boolean{
        return Helpers.checkEmailIsValid(email)
    }
    fun getuserObject(): String{
        return user.toJSON()
    }

    interface View {
        fun <T> changeActivity(toActivity: Class<T>, extras: Map<String, String> = HashMap())
        fun toastLong(message: String)
    }


    companion object{
        class Task(private var view: View) : AsyncTask<User, Void, String>() {

            override fun doInBackground(vararg params: User): String {
                return ApiController.loginUser(params[0])
            }

            override fun onPostExecute(result: String) {
                //local storage safe token
                super.onPostExecute(result)
                //if user is authenticated or
                when (result) {
                    "200" -> {
                        this.view.changeActivity(HomeView::class.java)
                    }
                    "412" -> {
                        this.view.toastLong("verifiy your email first")
                    }
                    else -> {
                        this.view.toastLong("Invalid login")
                    }
                }
            }
        }
    }
}

