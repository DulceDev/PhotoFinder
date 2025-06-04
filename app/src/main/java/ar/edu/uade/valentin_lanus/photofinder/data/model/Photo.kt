package ar.edu.uade.valentin_lanus.photofinder.data.model

data class Photo ( // Datos de la Imagen
    val id: String,
    val description: String?,
    val likes: Int,
    val urls: Urls,
    val user: User
)

data class Urls( // Tamanios de la Imagen
    val small: String,
    val regular: String,
    val full: String
)

data class User( // Duenio de la Imagen
    val name: String
)