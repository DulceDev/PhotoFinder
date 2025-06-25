package ar.edu.uade.valentin_lanus.photofinder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LikedPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity)

    @Query("SELECT * FROM liked_photos")
    suspend fun getAllLikedPhotos(): List<PhotoEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM liked_photos WHERE id = :photoId)")
    suspend fun isLiked(photoId: String): Boolean

    @Query("DELETE FROM liked_photos WHERE id = :photoId")
    suspend fun delete(photoId: String)
}