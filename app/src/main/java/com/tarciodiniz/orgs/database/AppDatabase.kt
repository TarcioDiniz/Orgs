package com.tarciodiniz.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tarciodiniz.orgs.database.convert.ToConverts
import com.tarciodiniz.orgs.database.dao.ProductDao
import com.tarciodiniz.orgs.database.dao.UserDao
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.model.User

@Database(
    entities = [Product::class, User::class], version = 6)
@TypeConverters(ToConverts::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun UserDao(): UserDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6
            )
                .build().also {
                db = it
            }
        }
    }
}