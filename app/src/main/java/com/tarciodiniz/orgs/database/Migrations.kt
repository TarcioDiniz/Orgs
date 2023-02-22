package com.tarciodiniz.orgs.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `User` 
            (`id` TEXT NOT NULL, 
            `name` TEXT NOT NULL, 
            `password` TEXT NOT NULL, 
            PRIMARY KEY(`id`))"""
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Product ADD COLUMN 'userID' TEXT")
    }
}


// Steps to change a Long type to String

// 1 - Create a table with all the data
// 2 - Copy data from the current table to the table
// 3 - Generate different and new id for the new table
// 4 - Remove the current table
// 5 - Rename the new table to the name of the current one

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 1 - Create a new table with all the data and the new id column
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `Product_new` 
            (`id` TEXT NOT NULL, 
            `name` TEXT NOT NULL, 
            `description` TEXT NOT NULL, 
            `value` REAL NOT NULL, 
            `image` TEXT, 
            `userID` TEXT, 
            PRIMARY KEY(`id`))"""
        )

        // 2 - Copy data from the current table to the new table with new id
        database.execSQL(
            """
            INSERT INTO `Product_new` (`id`, `name`, `description`, `value`, `image`, `userID`) 
            SELECT CAST(uuid() AS TEXT), `name`, `description`, `value`, `image`, `userID` FROM `Product`
            """
        )

        // 3 - Remove the current table
        database.execSQL("DROP TABLE `Product`")

        // 4 - Rename the new table to the name of the current one
        database.execSQL("ALTER TABLE `Product_new` RENAME TO `Product`")
    }
}

val MIGRATION_4_5 = object: Migration(4, 5){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Product ADD syncNow INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_5_6 = object: Migration(5, 6){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Product ADD deleteSyncNow INTEGER NOT NULL DEFAULT 0")
    }
}

