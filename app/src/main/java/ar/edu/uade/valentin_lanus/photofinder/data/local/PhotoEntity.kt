package ar.edu.uade.valentin_lanus.photofinder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "liked_photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val imageUrlSmall: String,
    val imageUrlRegular: String,
    val imageUrlFull: String,
    val description: String?,
    val userName: String,
    val likes: Int
)