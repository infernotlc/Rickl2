package ApiService

import DataClass.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickandMortyApiService {

    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): Response<LocationResponse>

    @GET("location/{id}")
    suspend fun getLocationDetails(@Path("id") id: Int):Response<Location>

    @GET("character/{id}")
    suspend fun getCharacterDetails(@Path("id") id: Int):Response<Characters>

    @GET("location/{id}/")
    fun getCharactersByLocation(@Path("id") id: Int): Call<CharacterResponse>

    @GET("location/{id}/")
   suspend fun getCharacters(@Path("id") id: Int): CharacterResponse



    companion object{
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        fun create(): RickandMortyApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RickandMortyApiService::class.java)
        }
    }
}
