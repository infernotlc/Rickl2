import Adapters.LocationAdapter
import Adapters.OnLocationClickListener
import ApiService.LocationResponse
import ApiService.RickandMortyApiService
import DataClass.Characters
import DataClass.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickl.R
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationFragment : Fragment(), OnLocationClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocationAdapter
    public lateinit var characters: List<Characters>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location_detail, container, false)
        recyclerView = view.findViewById(R.id.rv_location_list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LocationAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(RickandMortyApiService::class.java)
        val locationsMapString = arguments?.getString(ARG_CHARACTERS)
        if (locationsMapString != null) {
            val gson = Gson()
            val locationsMap = gson.fromJson(locationsMapString, mutableMapOf<Location, MutableList<Characters>>().javaClass)
            adapter.setItems(locationsMap.keys.toList())
            characters = locationsMap.values.flatten()

        }

        lifecycleScope.launch {
            try {
                val response: Response<LocationResponse> = withContext(Dispatchers.IO) {
                    apiService.getLocations(page = 1)
                }
                if (response.isSuccessful) {
                    val locations = response.body()?.locations ?: emptyList()
                    adapter.setItems(locations)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationClicked(location: Location) {
        val filteredCharacters = characters.filter { it.location.name == location.name }
        adapter.setSelectedLocation(location)
        val locations = filteredCharacters.map { it.location }.distinct()
        adapter.setItems(locations)
    }

    companion object {
        const val ARG_CHARACTERS = "characters"


        fun newInstance(characters: List<Characters> = emptyList()): LocationFragment {
            val locationsMap = mutableMapOf<Location, MutableList<Characters>>()
            characters.forEach { character ->
                val location = character.location
                if (locationsMap.containsKey(location)) {
                    locationsMap[location]?.add(character)
                } else {
                    locationsMap[location] = mutableListOf(character)
                }
            }

            val gson = Gson()
            val locationsMapString = gson.toJson(locationsMap)

            val args = Bundle().apply {
                putString(ARG_CHARACTERS, locationsMapString)
            }

            val fragment = LocationFragment()
            fragment.arguments = args
            return fragment
        }

    }


}

fun CharacterAdapter.setItems(characters: List<Characters>) {
    val characterMap = mutableMapOf<Location, MutableList<Characters>>()
    characters.forEach { character ->
        if (characterMap.containsKey(character.location)) {
            characterMap[character.location]?.add(character)
        } else {
            characterMap[character.location] = mutableListOf(character)
        }
    }
    var items = characterMap
    notifyDataSetChanged()
}