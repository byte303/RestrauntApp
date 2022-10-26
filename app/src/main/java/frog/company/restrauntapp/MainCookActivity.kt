package frog.company.restrauntapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.databinding.ActivityMainCookBinding
import frog.company.restrauntapp.ui.ProfileFragment
import frog.company.restrauntapp.ui.cook_list_menu.CookListMenuFragment
import frog.company.restrauntapp.ui.main_cook.MainCookFragment


class MainCookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainCookBinding
    private lateinit var navView: BottomNavigationView

    private var sConn: ServiceConnection? = null
    private lateinit var myService : MyService
    private var bound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding = ActivityMainCookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navView.setOnNavigationItemSelectedListener(navListener)
        navView.selectedItemId = R.id.navigation_home

        navView.background = null

        UtilsDB.MY_ACTIVITY = this


        val intent = Intent(this, MyService::class.java)
        startService(intent)
        sConn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                Log.d("Test Log", "MainActivity onServiceConnected")
                myService = (binder as MyService.MyBinder).service
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                Log.d("Test Log", "MainActivity onServiceDisconnected")
                bound = false
            }
        }
    }

    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {item ->

            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> MainCookFragment()
                R.id.navigation_profile -> ProfileFragment()
                R.id.navigation_list -> CookListMenuFragment()
                else -> MainCookFragment()
            }

            supportFragmentManager.beginTransaction().replace(
                R.id.viewPager,
                selectedFragment
            ).commit()
            true
        }
}