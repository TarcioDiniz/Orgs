package com.tarciodiniz.orgs.database.dao

import androidx.room.*
import com.tarciodiniz.orgs.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getAll(): List<Product>

    @Insert
    fun save(vararg product: Product)

    @Delete
    fun delete(product: Product)

    @Update
    fun update(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun getFromID(id: Long): Product?
}