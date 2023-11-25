package com.example.deaft

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var meuPagerAdapter: MeuPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        viewPager2 = findViewById(R.id.viewPager2)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        meuPagerAdapter = MeuPagerAdapter(this)
        viewPager2.adapter = meuPagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> meuPagerAdapter.setCurrentItem(viewPager2, 0)
                R.id.fragment_menu_translate -> meuPagerAdapter.setCurrentItem(viewPager2, 1)
                R.id.fragment_practice -> meuPagerAdapter.setCurrentItem(viewPager2, 2)
            }
            true
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigationView.menu.findItem(R.id.fragment_home).isChecked = true
                    1 -> bottomNavigationView.menu.findItem(R.id.fragment_menu_translate).isChecked = true
                    2 -> bottomNavigationView.menu.findItem(R.id.fragment_practice).isChecked = true
                }
            }
        })
    }
}

class MeuPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> MenuTranslateFragment()
            2 -> PracticeFragment()
            else -> HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3 // Número total de páginas
    }

    fun getCurrentFragment(viewPager: ViewPager2, position: Int): Fragment? {
        return (viewPager.adapter as? MeuPagerAdapter)?.createFragment(position)
    }

    fun setCurrentItem(viewPager: ViewPager2, position: Int) {
        viewPager.setCurrentItem(position, true)
    }
}

