package ar.edu.uade.valentin_lanus.photofinder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhotoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun likedPhotoDao(): LikedPhotoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "photo_finder_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}