package vsec.com.slockandroid.Presenters.RegisterLockActivity

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_register_lock
import kotlinx.android.synthetic.main.activity_register_lock.*
import vsec.com.slockandroid.R
import vsec.com.slockandroid.generalModels.ButtonState
import java.util.*

class RegisterLockView : Activity(), RegisterLockPresenter.View {
    private var buttonState: EnumSet<ButtonState> = EnumSet.noneOf(ButtonState::class.java)
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

        in_lock_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(!p0.isNullOrEmpty()){
                    buttonState.add(ButtonState.LOCK_NAME_VALID)
                }else{
                    buttonState.remove(ButtonState.LOCK_NAME_VALID)
                }
                updateButtonState()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        in_lock_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(!p0.isNullOrEmpty()){
                    buttonState.add(ButtonState.LOCK_DESCRIPTION_VALID)
                }else{
                    buttonState.remove(ButtonState.LOCK_DESCRIPTION_VALID)
                }
                updateButtonState()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun updateButtonState(){
        btn_register_lock.isEnabled = (
                buttonState.contains(ButtonState.LOCK_NAME_VALID) &&
                buttonState.contains(ButtonState.LOCK_DESCRIPTION_VALID) &&
                buttonState.contains(ButtonState.REGISTERABLE_LOCK_FOUND)
        )
    }

    override fun changeActivity(toActivity: Class<Activity>, extras: Map<String, String>) {
        var intent: Intent = Intent(this, toActivity)
        for(extra in extras){
            intent.putExtra(extra.key, extra.value)
        }
        startActivity(intent)
    }

    override fun onRegisterableLockFound(lock: BluetoothDevice){
        this.lock = lock
        Log.e("lock found", this.lock.name)
        buttonState.add(ButtonState.REGISTERABLE_LOCK_FOUND)
    }
}
