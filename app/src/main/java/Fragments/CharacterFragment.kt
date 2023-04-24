import ApiService.CharacterResponse
import ApiService.RickandMortyApiService
import DataClass.Characters
import DataClass.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickl.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharacterFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character_detail, container, false)
        recyclerView = view.findViewById(R.id.rv_character_details)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = CharacterAdapter(mutableListOf()) { characters ->
            onCharacterClicked(characters)
        }
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationId = arguments?.getInt(ARG_LOCATION_ID)

        if (locationId != null) {
            loadCharacters(locationId)
        }
    }

    private fun onCharacterClicked(character: Characters) {
        Log.d(TAG, "Clicked character: ${character.name}")
    }

   fun updateCharacters(characters: List<Characters>) {
        val charactersMap: MutableMap<Location, MutableList<Characters>> = mutableMapOf()
        characters.forEach { character ->
            val location = character.location
            if (location in charactersMap) {
                charactersMap[location]?.add(character)
            } else {
                charactersMap[location] = mutableListOf(character)
            }
        }
        adapter.setItems(charactersMap)
    }

    private fun loadCharacters(locationId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RickandMortyApiService::class.java)

        service.getCharactersByLocation(locationId).enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    val characters = response.body()?.results ?: emptyList()
                    updateCharacters(characters)
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                Log.e(TAG, "Error loading characters", t)
            }
        })
    }

    companion object {
        private const val ARG_LOCATION_ID = "locationId"
        private const val TAG = "CharacterFragment"

        fun newInstance(locationId: Int): CharacterFragment {
            val fragment = CharacterFragment()
            val args = Bundle()
            args.putInt(ARG_LOCATION_ID, locationId)
            fragment.arguments = args
            return fragment
        }
    }
}
