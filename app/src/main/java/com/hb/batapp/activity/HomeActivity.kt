package com.hb.batapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hb.batapp.R
import com.hb.batapp.fragment.AddZoneFragment
import com.hb.batapp.fragment.HomeFragment
import com.hb.batapp.fragment.MapFragment
import com.hb.batapp.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    companion object {
        const val HOME = 0
        const val MAP = 1
        const val PROFILE = 2
        const val ZONE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setBottomMenu()
        showFragment(HomeFragment())
    }

    private fun setBottomMenu() {
        bottomBar.onItemSelected = { position ->
            when (position) {
                HOME -> {
                    showFragment(HomeFragment())
                }
                MAP -> {
                    showFragment(MapFragment())
                }
                PROFILE -> {
                    showFragment(ProfileFragment())
                }
                ZONE -> {
                    showFragment(AddZoneFragment())
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.menu_fragment_container, fragment, "")
            .commit()
    }
}