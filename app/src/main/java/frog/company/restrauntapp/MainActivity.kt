package frog.company.restrauntapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import frog.company.restrauntapp.bottom_dialog.visitors.BottomDialogVisitors
import frog.company.restrauntapp.database.UtilsDB
import frog.company.restrauntapp.databinding.ActivityMainBinding
import frog.company.restrauntapp.ui.ProfileFragment
import frog.company.restrauntapp.ui.home.HomeFragment
import frog.company.restrauntapp.ui.select_table.SelectTableFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView

    private var sConn: ServiceConnection? = null
    private lateinit var myService : MyService
    private var bound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navView.setOnNavigationItemSelectedListener(navListener)

        navView.selectedItemId = R.id.navigation_home

        navView.background = null
        navView.menu.getItem(1).isEnabled = false


        binding.fab.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(
                R.id.viewPager,
                SelectTableFragment()
            ).addToBackStack(null).commit()
        }
/*
        val intent = Intent(this, MyServiceWaiter::class.java)
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
        }*/
    }

    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {item ->

            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> HomeFragment()
                R.id.navigation_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction().replace(
                R.id.viewPager,
                selectedFragment
            ).commit()
            true
        }
}