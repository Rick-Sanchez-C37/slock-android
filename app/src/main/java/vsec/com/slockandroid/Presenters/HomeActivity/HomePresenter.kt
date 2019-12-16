package vsec.com.slockandroid.Presenters.HomeActivity

import android.app.Activity

class HomePresenter(private val view: View) {


    interface View {
        fun changeActivity(toActivity: Class<Activity>, extra: Map<String, String>)
    }

}
