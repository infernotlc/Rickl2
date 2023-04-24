package DataClass

import com.google.gson.annotations.SerializedName

data class Characters(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String?,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val origin: Location,
    @SerializedName("originUrl")
    val originUrl: String,
    @SerializedName("location")
    val location: Location,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("episode")
    val episode: List<String>,
    @SerializedName("episodeUrls")
    val episodeUrls: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val createdDate: String
)
