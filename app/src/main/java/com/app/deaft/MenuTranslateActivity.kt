package com.app.deaft

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView


class MenuTranslateActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var meuPagerAdapter: MeuPagerAdapterMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_translate)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES)

        viewPager2 = findViewById(R.id.viewPager2)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        meuPagerAdapter = MeuPagerAdapterMenu(this)
        viewPager2.adapter = meuPagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_vibration -> meuPagerAdapter.setCurrentItem(viewPager2, 0)
                R.id.fragment_libras -> meuPagerAdapter.setCurrentItem(viewPager2, 1)
            }
            true
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigationView.menu.findItem(R.id.fragment_vibration).isChecked = true
                    1 -> bottomNavigationView.menu.findItem(R.id.fragment_libras).isChecked = true
                }
            }
        })
    }
}

class MeuPagerAdapterMenu(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LanguageSelectionFragment()
            1 -> LibraFragment()
            else -> LanguageSelectionFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2 // Número total de páginas
    }

    fun getCurrentFragment(viewPager: ViewPager2, position: Int): Fragment? {
        return (viewPager.adapter as? MeuPagerAdapter)?.createFragment(position)
    }

    fun setCurrentItem(viewPager: ViewPager2, position: Int) {
        viewPager.setCurrentItem(position, true)
    }
}

