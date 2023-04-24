import Adapters.CharacterViewHolder
import DataClass.Characters
import DataClass.Location
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickl.databinding.ItemCharacterBinding

class CharacterAdapter(
    private val characters: MutableList<Characters>,
    private val onCharacterClicked: (Characters) -> Unit
) : RecyclerView.Adapter<CharacterViewHolder>() {

    private lateinit var context: Context
    private var charactersMap: MutableMap<Location, MutableList<Characters>> = mutableMapOf()
    private var selectedLocation: Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        context = parent.context
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        if (selectedLocation != null) {
            val characters = charactersMap[selectedLocation]
            val character = characters?.get(position)
            if (character != null) {
                holder.bind(character, onCharacterClicked)
            }
        }
    }

    override fun getItemCount(): Int {
        val characters = charactersMap[selectedLocation]
        return characters?.size ?: 0
    }

    fun setItems(characters: MutableMap<Location, MutableList<Characters>>) {
        charactersMap.clear()
        charactersMap.putAll(characters)
        selectedLocation = null
        notifyDataSetChanged()
    }

    fun addItems(characters: MutableMap<Location, MutableList<Characters>>) {
        characters.forEach { (location, charactersList) ->
            if (location in charactersMap) {
                charactersMap[location]?.addAll(charactersList)
            } else {
                charactersMap[location] = charactersList
            }
        }
        notifyDataSetChanged()
    }

    fun setSelectedLocation(location: Location) {
        selectedLocation = location
        notifyDataSetChanged()
    }
}
