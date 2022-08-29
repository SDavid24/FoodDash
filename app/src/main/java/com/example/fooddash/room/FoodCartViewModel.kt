package com.example.fooddash.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fooddash.models.FoodModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodCartViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<FoodModel>>
    private var repository: FoodCartRepository

    init {
        val foodDao = FoodCartDatabase.getInstance((getApplication())).foodDao()
        repository = FoodCartRepository(foodDao)
        readAllData = repository.fetchAllItemsInCart()
    }

    fun insertFoodIntoCart(foodModel: FoodModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(foodModel)
            //itemAdded.postValue(foodModel)
        }
    }

    fun updateFoodNoInCart(foodModel: FoodModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(foodModel)
            //itemAdded.postValue(foodModel)
        }
    }

    fun deleteFoodNoInCart(foodModel: FoodModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(foodModel)
            //itemAdded.postValue(foodModel)
        }
    }

    suspend fun getTotalPriceOfItems() : Double {
        return repository.getTotalExpense()
    }

}

