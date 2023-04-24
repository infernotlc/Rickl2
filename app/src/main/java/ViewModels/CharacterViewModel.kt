import DataClass.Characters
import DataClass.Location
import ViewModels.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterViewModel(private val repository: Repository) : ViewModel() {

    private val _originUrl = MutableLiveData<String>()
    val originUrl: LiveData<String> = _originUrl

    private val _episodeUrls = MutableLiveData<List<String>>()
    val episodeUrls: LiveData<List<String>> = _episodeUrls

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl


    private val _characters = MutableLiveData<List<Characters>>()
    val characters: LiveData<List<Characters>> = _characters

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _charactersByLocation = MutableLiveData<MutableMap<Location, MutableList<Characters>>>()
    val charactersByLocation: LiveData<MutableMap<Location, MutableList<Characters>>> = _charactersByLocation

    fun loadCharactersByLocation(locationId: Int) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiCharacters = repository.getCharactersByLocation(locationId)
                val characters = apiCharacters.map { apiCharacter ->
                    Characters(
                        id = apiCharacter.id,
                        name = apiCharacter.name,
                        status = apiCharacter.status,
                        species = apiCharacter.species,
                        type = apiCharacter.type,
                        gender = apiCharacter.gender,
                        origin = apiCharacter.origin,
                        originUrl = apiCharacter.origin.url,
                        location = apiCharacter.location,
                        image = apiCharacter.imageUrl,
                        episodeUrls = apiCharacter.episodeUrls,
                        url = apiCharacter.url,
                        createdDate = apiCharacter.createdDate,
                        episode = apiCharacter.episode,
                        imageUrl = apiCharacter.imageUrl
                    )
                }

                withContext(Dispatchers.Main) {
                    _characters.value = characters
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun getCharacter(characterId: Int): LiveData<Characters> {
        return repository.getCharacter(characterId)
    }

    fun groupCharactersByLocation(characters: List<Characters>) {
        val charactersByLocation = mutableMapOf<Location, MutableList<Characters>>()
        characters.forEach { character ->
            val location = character.location
            if (location != null) {
                val characterList = charactersByLocation[location]
                if (characterList != null) {
                    characterList.add(character)
                } else {
                    charactersByLocation[location] = mutableListOf(character)
                }
            }
        }
        _charactersByLocation.value = charactersByLocation
    }
}
