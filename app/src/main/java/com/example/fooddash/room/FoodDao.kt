package com.example.fooddash.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fooddash.models.FoodModel


@Dao
interface FoodDao {

    @Insert
    suspend fun insert(foodModel: FoodModel)

    @Update
    suspend fun update(foodModel: FoodModel)

    @Delete
    suspend fun delete(foodModel: FoodModel)

    @Query("SELECT SUM(totalfee) FROM `foodcart-table`")
    suspend fun getTotalExpense(): Double

    @Query("SELECT * FROM `foodcart-table` ORDER BY foodId ASC")
    fun fetchAllItemsInCart(): LiveData<List<FoodModel>>

 /*   @Query("SELECT * FROM `foodcart-table` where id =:id")
    fun fetchEmployeesById(id: Int): Flow<FoodModel>
*/

}