package frog.company.restrauntapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import frog.company.restrauntapp.MainActivity
import frog.company.restrauntapp.MainCookActivity
import frog.company.restrauntapp.bottom_dialog.BottomDialogUrl
import frog.company.restrauntapp.database.UtilsCashe
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.database.UtilsShift
import frog.company.restrauntapp.database.UtilsUser
import frog.company.restrauntapp.databinding.ActivityLoginBinding
import frog.company.restrauntapp.enum.EnumShift
import frog.company.restrauntapp.enum.EnumUserRole
import frog.company.restrauntapp.helper.LoadingScreen
import frog.company.restrauntapp.helper.MyDate
import frog.company.restrauntapp.inter.IListenerShift
import frog.company.restrauntapp.inter.IListenerUser
import frog.company.restrauntapp.model.Shift
import frog.company.restrauntapp.model.User
import frog.company.restrauntapp.pincode.PinLockListener
import io.paperdb.Paper


class LoginActivity : AppCompatActivity(), IListenerUser, IListenerShift {

    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)
        val url = Paper.book().read(UtilsDB.URL, "")
        if(url == ""){
            val dialogFragment = BottomDialogUrl()
            dialogFragment.show(
                supportFragmentManager,
                dialogFragment.tag
            )
        }

        binding.mPinLockView.setPinLockListener(mPinLockListener)
        binding.mPinLockView.attachIndicatorDots(binding.mIndicatorDots)

        binding.imgSettings.setOnClickListener {
            runOnUiThread {
                val dialogFragment = BottomDialogUrl()
                dialogFragment.show(
                    supportFragmentManager,
                    dialogFragment.tag
                )
            }
        }
        UtilsCashe().onLoadCashe()
    }

    private var mPinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            Log.d("TAG", "Pin complete: $pin")

            LoadingScreen.displayLoadingWithText(applicationContext,  false)

            UtilsUser().onSelectPassword(pin, this@LoginActivity)
        }

        override fun onEmpty() {
            Log.d("TAG", "Pin empty")
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            Log.d("TAG", "Pin changed, new length $pinLength with intermediate pin $intermediatePin")
        }
    }

    override fun onSelectUser(user: User?) {
        if(user != null){
            Paper.book().write(UtilsDB.USER, user)

            UtilsShift().onSelectLast(user.user_id, this)
        }else{
            runOnUiThread {
                Toast.makeText(this, "Пароль введён неверно!", Toast.LENGTH_SHORT).show()
                binding.mPinLockView.resetPinLockView()
                LoadingScreen.hideLoading()
            }
        }

    }

    override fun onSelectAllUser(user: ArrayList<User>?) {
    }

    override fun onUserBooleanResult(boolean: Boolean) {

    }

    override fun onSelectShift(result: Shift?) {
        val user = Paper.book().read(UtilsDB.USER, User())!!

        LoadingScreen.hideLoading()

        if(result != null){

            user.shift = result
            Paper.book().write(UtilsDB.USER, user)

            if(user.user_role == EnumUserRole.Cook.num)
                startActivity(Intent(applicationContext, MainCookActivity::class.java))
            else
                startActivity(Intent(applicationContext, MainActivity::class.java))

            finish()
        }else{
            val shift = Shift(
                0,
                MyDate().onDateFormat(),
                MyDate().onDateFormat(),
                user.user_id,
                EnumShift.Open.num
            )
            UtilsShift().onInsert(shift, this)
        }
    }
}