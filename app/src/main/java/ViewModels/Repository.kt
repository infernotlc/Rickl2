package ViewModels

import ApiService.RickandMortyApiService
import DataClass.Characters
import DataClass.Location
import android.util.Log
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository {
    private val apiService: RickandMortyApiService = RickandMortyApiService.create()

    suspend fun getLocations(page: Int): List<Location> {
        val response = apiService.getLocations(page)
        if (response.isSuccessful) {
            val locationResponse = response.body()
            return locationResponse?.locations ?: emptyList()
        } else {
            throw Exception("Failed to fetch locations")
        }
    }

    suspend fun getLocation(locationId: Int): Location? {
        val response = apiService.getLocationDetails(locationId)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Failed to fetch location")
        }
    }

    suspend fun getCharactersByLocation(locationId: Int): List<Characters> {
        val response = apiService.getCharactersByLocation(locationId).execute()
        if (response.isSuccessful) {
            val characterResponse = response.body()
            val characters = mutableListOf<Characters>()
            characterResponse?.results?.forEach { characters.add(it) }
            return characters
        } else {
            throw Exception("Failed to fetch characters by location")
        }
    }


    fun getCharacter(characterId: Int): LiveData<Characters> {
        val characterLiveData = MutableLiveData<Characters>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val characters = apiService.getCharacterDetails(characterId).body()
                characters?.let {

                    withContext(Dispatchers.Main) {
                        characterLiveData.value = it
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch character with id $characterId", e)
            }
        }
        return characterLiveData
    }
}

