package com.example.mvpmovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvpmovies.pojo.FavouriteMovie
import com.example.mvpmovies.pojo.Movie

@Database(entities = [Movie::class,FavouriteMovie::class], version = 14, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {
    companion object{
        private const val DB_NAME = "movies.db"
        private var database: MovieDatabase? = null
        private val LOCK = Object()

        fun getInstance(context: Context): MovieDatabase{
            synchronized(LOCK){
                if (database==null)
                    database = Room.databaseBuilder(context,MovieDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                return database as MovieDatabase
            }
        }
    }

    abstract fun moviesDao():MoviesDao
}