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

    @Query("SELECT * FROM Product ORDER BY name ASC")
    fun searchAllOrderByNameAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name DESC")
    fun searchAllOrderByNameDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description ASC")
    fun searchAllOrderByDescriptionAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description DESC")
    fun searchAllOrderByDescriptionDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value ASC")
    fun searchAllOrderByAscValue(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value DESC")
    fun searchAllOrderByValueDesc(): List<Product>
}