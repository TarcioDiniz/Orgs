package com.tarciodiniz.orgs.database.dao

import androidx.room.*
import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.webclient.dto.ProductDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userID = :userID AND deleteSyncNow = 0")
    fun searchAllFromUser(userID: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Update
    suspend fun update(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id AND deleteSyncNow = 0")
    suspend fun getFromID(id: String): Product?

    @Query("SELECT * FROM Product WHERE userID = :id AND deleteSyncNow = 0 ORDER BY name ASC")
    fun searchAllOrderByNameAsc(id: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userID = :id AND deleteSyncNow = 0 ORDER BY name DESC")
    fun searchAllOrderByNameDesc(id: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userID = :id AND deleteSyncNow = 0 ORDER BY description ASC")
    fun searchAllOrderByDescriptionAsc(id: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userID = :id AND deleteSyncNow = 0 ORDER BY description DESC")
    fun searchAllOrderByDescriptionDesc(id: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userID = :id AND deleteSyncNow = 0 ORDER BY value ASC")
    fun searchAllOrderByAscValue(id: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userID = :id AND deleteSyncNow = 0 ORDER BY value DESC")
    fun searchAllOrderByValueDesc(id: String): Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE syncNow = 0")
    fun getAllNotSync(): Flow<List<Product>>

    @Query("UPDATE Product SET deleteSyncNow = 1 WHERE id = :id")
    suspend fun deactivateNote(id: String)

    @Query("SELECT * FROM Product WHERE deleteSyncNow = 1")
    fun deactivate(): Flow<List<Product>>
}