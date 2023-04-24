package ApiService

import DataClass.Characters

data class CharacterResponse(
    val info: Info,
    val results: List<Characters>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)


