package com.example.fooddash.room

import androidx.lifecycle.LiveData
import com.example.fooddash.models.FoodModel

class FoodCartRepository(private val foodDao: FoodDao) {

    suspend fun insert(foodModel: FoodModel) {
        foodDao.insert(foodModel)
    }

    suspend fun update(foodModel: FoodModel) {
        foodDao.update(foodModel)
    }

    suspend fun delete(foodModel: FoodModel) {
        foodDao.delete(foodModel)
    }

    suspend fun getTotalExpense() : Double{
        return foodDao.getTotalExpense()
    }

    fun fetchAllItemsInCart() : LiveData<List<FoodModel>> {
        return foodDao.fetchAllItemsInCart()
    }
}