
package com.example.rickl
import ApiService.RickandMortyApiService
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.rickl.R
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        const val ARG_LOCATION_ID = "location_id"
        const val BASE_URL = "https://rickandmortyapi.com/api/"

    }

    private lateinit var apiService: RickandMortyApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tab)

        // Initialize the Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(RickandMortyApiService::class.java)

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val locationFragment = LocationFragment.newInstance()
        val characterFragment = CharacterFragment.newInstance(1)
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return 2
            }

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        locationFragment
                    }
                    1 -> {
                        characterFragment
                    }
                    else -> throw IllegalArgumentException("Invalid position $position")
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> "Locations"
                    1 -> "Characters"
                    else -> throw IllegalArgumentException("Invalid position $position")
                }
            }
        }

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 1) {
                    val locationId = (viewPager.adapter as FragmentPagerAdapter)
                        .getItem(0)
                        .arguments?.getInt(ARG_LOCATION_ID)
                    if (locationId == null) {
                        Toast.makeText(
                            applicationContext,
                            "Please select a location first",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewPager.currentItem = 0
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            try {
                                val characterResponse = apiService.getCharacters(locationId)
                                val characters = characterResponse.results
                                characterFragment.updateCharacters(characters)
                            } catch (e: Exception) {
                                Log.e("MainActivity", "Error fetching characters", e)
                                Toast.makeText(
                                    applicationContext,
                                    "Error fetching characters",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
