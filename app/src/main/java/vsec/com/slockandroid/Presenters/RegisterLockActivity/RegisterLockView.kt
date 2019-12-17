package vsec.com.slockandroid.Presenters.RegisterLockActivity

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import vsec.com.slockandroid.R

class RegisterLockView : Activity(), RegisterLockPresenter.View{

    private lateinit var presenter: RegisterLockPresenter
    private lateinit var lock: BluetoothDevice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_lock)

        this.presenter = RegisterLockPresenter(this)
        this.presenter.lookForRegistrableLock()

        btn_register_lock.setOnClickListener{
            this.presenter.registerLock(this.lock)
        }
    }

    override fun changeActivity(toActivity: Class<Activity>, extra: Map<String, String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRegisterableLockFound(lock: BluetoothDevice){
        this.lock = lock
        btn_register_lock.isEnabled = true
    }
}