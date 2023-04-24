package ApiService

import DataClass.InfoModel
import DataClass.Location
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("results") val locations: List<Location>,
    @SerializedName("info") val info: InfoModel
)
