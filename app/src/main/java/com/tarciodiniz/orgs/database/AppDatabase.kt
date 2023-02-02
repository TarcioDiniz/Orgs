package com.tarciodiniz.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tarciodiniz.orgs.database.convert.ToConverts
import com.tarciodiniz.orgs.database.dao.ProductDao
import com.tarciodiniz.orgs.model.Product

@Database(entities = [Product::class], version = 1)
@TypeConverters(ToConverts::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        fun getInstance(context: Context): AppDatabase{
            return Room.databaseBuilder(
                context, AppDatabase::class.java, "orgs.db"
            ).build()
        }
    }

}