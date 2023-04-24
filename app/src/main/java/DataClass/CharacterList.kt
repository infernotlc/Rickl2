package DataClass

import com.google.gson.annotations.SerializedName

data class CharacterList(
    @SerializedName("info") val info: InfoModel,
    @SerializedName("results") val characters: List<Character>
)
