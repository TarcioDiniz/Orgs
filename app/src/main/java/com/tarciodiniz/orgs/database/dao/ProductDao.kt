package com.tarciodiniz.orgs.database.dao

import androidx.room.*
import com.tarciodiniz.orgs.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userID = :userID")
    fun searchAllFromUser(userID: String): Flow<List<Product>>

    @Insert
    suspend fun save(vararg product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Update
    suspend fun update(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getFromID(id: Long): Product?

    @Query("SELECT * FROM Product ORDER BY name ASC")
    suspend fun searchAllOrderByNameAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY name DESC")
    suspend fun searchAllOrderByNameDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description ASC")
    suspend fun searchAllOrderByDescriptionAsc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY description DESC")
    suspend fun searchAllOrderByDescriptionDesc(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value ASC")
    suspend fun searchAllOrderByAscValue(): List<Product>

    @Query("SELECT * FROM Product ORDER BY value DESC")
    suspend fun searchAllOrderByValueDesc(): List<Product>
}