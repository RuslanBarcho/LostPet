package io.vinter.lostpet.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem

import java.util.Objects

import io.vinter.lostpet.R
import io.vinter.lostpet.ui.create.CreateActivity
import io.vinter.lostpet.ui.list.AllPetsFragment
import io.vinter.lostpet.ui.profile.ProfileFragment
import io.vinter.lostpet.utils.config.StyleApplicator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    private val mOnNavigationItemSelectedListener = { item: MenuItem ->
        when (item.itemId) {
            R.id.navigation_list -> showFragment("all", "profile")
            R.id.navigation_profile -> showFragment("profile", "all")
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StyleApplicator.style(this)

        fragmentManager = supportFragmentManager
        if ((fragmentManager.findFragmentByTag("all") == null) and (fragmentManager.findFragmentByTag("profile") == null)) {
            fragmentManager.beginTransaction()
                    .add(R.id.content_container, ProfileFragment(), "profile")
                    .add(R.id.content_container, AllPetsFragment(), "all")
                    .commit()
        }

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        floatingActionButton.setOnClickListener {
            startActivityForResult(Intent(this, CreateActivity::class.java), 11)
        }
    }

    private fun showFragment(tagShow: String, tagHide: String) {
        fragmentManager.beginTransaction()
                .show(Objects.requireNonNull<Fragment>(fragmentManager.findFragmentByTag(tagShow)))
                .hide(Objects.requireNonNull<Fragment>(fragmentManager.findFragmentByTag(tagHide)))
                .commit()
    }

    fun changeProfilePage(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                .remove(Objects.requireNonNull<Fragment>(fragmentManager.findFragmentByTag("profile")))
                .add(R.id.content_container, fragment, "profile")
                .commit()
    }

    override fun onBackPressed() {
        if ((Objects.requireNonNull<Fragment>(fragmentManager.findFragmentByTag("profile")) !is ProfileFragment) and (bottom_navigation.selectedItemId == R.id.navigation_profile)) {
            changeProfilePage(ProfileFragment())
        } else
            super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode){
            22 -> (fragmentManager.findFragmentByTag("all") as AllPetsFragment).update()
        }
    }

}
