package com.example.fooddash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fooddash.databinding.ActivityShowDetailsBinding
import com.example.fooddash.models.FoodModel
import com.example.fooddash.room.FoodCartViewModel
import com.example.fooddash.utils.Constants
import kotlinx.android.synthetic.main.activity_show_details.*
import kotlin.math.roundToInt


class ShowDetailsActivity : AppCompatActivity() {
    private var binding : ActivityShowDetailsBinding? = null
    var foodDetailsModel : FoodModel?  = null
    lateinit var viewModel: FoodCartViewModel

    var orderAmount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)

        /**Initializing the view model provider*/
        viewModel = ViewModelProvider(this).get(FoodCartViewModel::class.java)
        setContentView(binding?.root)

        getIntData()
        addToCart()

    }

    /**Method that gets and displays the information of the chosen food that was sent into this activity*/
    private fun getIntData(){
        if (intent.hasExtra(Constants.FOOD_DETAILS)){
            foodDetailsModel = intent.getParcelableExtra(Constants.FOOD_DETAILS)!!
        }

        binding?.showDetailsTitleTxt!!.text = foodDetailsModel!!.title
        binding?.showDetailsPriceTxt!!.text = ("$" + foodDetailsModel!!.fee.toString())
        binding?.showDetailsDescriptionTxt!!.text = foodDetailsModel!!.description.toString()
        binding?.showDetailsFoodImage!!.setImageResource(foodDetailsModel!!.pic!!)
        binding?.showDetailsNumberOfOrderTxt!!.text = foodDetailsModel!!.numberInCart.toString()
        binding?.showDetailsNumberOfOrderTxt!!.text = orderAmount.toString()

        //Click listener for the plus button so it can increase the number food amount when clicked
        binding?.showDetailsPlusBtn!!.setOnClickListener {
            orderAmount += 1
            binding?.showDetailsNumberOfOrderTxt!!.text = orderAmount.toString()
        }

        //Click listener for the minus button so it can reduce the number food amount when clicked
        binding?.showDetailsMinusBtn!!.setOnClickListener {
            if (orderAmount == 1){
                orderAmount = 1
            }else{
            orderAmount -= 1
            showDetails_numberOfOrderTxt.text = orderAmount.toString()
            }
        }

        //Click listener for the add to cart button
        binding?.showDetailsAddToCartBtn!!.setOnClickListener {
            val intent = Intent(this, CartListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**Method that gets the current details of the meals after it must have been edited before being sent to the cart activity*/
    fun newDetails(): FoodModel{

        val new = FoodModel(0,
            binding?.showDetailsTitleTxt!!.text.toString(),
            foodDetailsModel!!.pic!!,
            binding?.showDetailsDescriptionTxt!!. text.toString(),
            foodDetailsModel!!.fee,
            totalPriceOfFood(),
            binding?.showDetailsNumberOfOrderTxt!!.text.toString().toInt()
        )

        return new
    }

    /**Method that multiplies the total number of food by the price of each to it can be appended in the databse*/
    private fun totalPriceOfFood(): Double{
        val totalPrice =
        (foodDetailsModel!!.fee?.times(binding?.showDetailsNumberOfOrderTxt!!.text.toString().toInt())!!.times(100.0)).roundToInt() / 100.0

        return totalPrice
    }

    /**Method that adds the food the page to the cart database  and also takes it to the Cart list activity*/
    private fun addToCart(){
        binding?.showDetailsAddToCartBtn!!.setOnClickListener {

            viewModel.insertFoodIntoCart(newDetails())  // calling the view model function

            Toast.makeText(this, "Added to cart!", Toast.LENGTH_LONG).show()

            startActivity(Intent(this, CartListActivity::class.java))
        }

    }

}