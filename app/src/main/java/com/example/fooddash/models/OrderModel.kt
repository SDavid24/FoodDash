package com.example.fooddash.models

import androidx.lifecycle.LiveData

data class OrderModel(
    val orderId : Int = 0,
    val foodOrdered : LiveData<List<FoodModel>>,
    val totalAmountToPay: Double? = null,
    val addressToDeliver : String = "",
    val customerId : String = ""

)
