package com.example.fooddash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddash.adapters.CartListAdapter
import com.example.fooddash.models.FoodModel
import com.example.fooddash.room.FoodCartViewModel
import com.example.fooddash.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class CartListActivity : AppCompatActivity() {
    var viewModel: FoodCartViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        //viewModel = ViewModelProvider(this).get(FoodCartViewModel::class.java)

        setupCartRecyclerview()

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun setupCartRecyclerview(){
        val adapter = CartListAdapter(this)
        rv_cartView.adapter = adapter
        rv_cartView.layoutManager = LinearLayoutManager(this)
        rv_cartView.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(FoodCartViewModel::class.java)

        viewModel!!.readAllData.observe(this, Observer { foodModel->
            if(adapter.itemCount > 0) {
                emptyTxt.visibility = View.GONE
                cart_list_scrollView.visibility = View.VISIBLE
                adapter.setdata(foodModel)
            }else{
                emptyTxt.visibility = View.VISIBLE
                cart_list_scrollView.visibility = View.GONE
            }
        })

            //adapter.setdata(foodModel)



        adapter.plusBtnListener = object : CartListAdapter.PlusBtnListener {
            override fun plusOnClick(model: FoodModel) {
                increaseNoOfFood(model)
            }
        }

        adapter.minusBtnListener = object : CartListAdapter.MinusBtnListener {
            override fun minusOnClick(model: FoodModel) {
                decreaseNoOfFood(model)
            }
        }



        // This binds the delete feature class to recyclerview)
        val deleteSwipeHandler = object : SwipeToDeleteCallback(this){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // This below Calls the adapter function when it is swiped for delete
                val adapter = rv_cartView.adapter as CartListAdapter
                var model = FoodModel()
                /**adapter.removeAt(viewHolder.adapterPosition)*/
                viewModel!!.deleteFoodNoInCart(adapter.foodPosition(viewHolder.bindingAdapterPosition))
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_cartView)

        configureScreen()
    }

    fun increaseNoOfFood(model: FoodModel){
        val newNumber = model.numberInCart + 1
        val newFoodCart = FoodModel(model.foodId, model.title,
            model.pic, model.description,
            model.fee, newNumber * model.fee!!, newNumber )

        viewModel!!.updateFoodNoInCart(newFoodCart)
        configureScreen()
    }

    fun decreaseNoOfFood(model: FoodModel){
        if (model.numberInCart > 1) {
            val newNumber = model.numberInCart - 1

            val newFoodCart = FoodModel(
                model.foodId, model.title,
                model.pic, model.description,
                model.fee, newNumber * model.fee!!, newNumber
            )

            viewModel!!.updateFoodNoInCart(newFoodCart)
            configureScreen()
        }else{
            //
        }
    }


    @SuppressLint("SetTextI18n")
    private fun configureScreen()  {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel = ViewModelProvider(this@CartListActivity).get(FoodCartViewModel::class.java)

            val totalExpense = viewModel!!.getTotalPriceOfItems()

            withContext(Main) {
                totalFoodFee.text = ((totalExpense * 100.0).roundToInt() / 100.0).toString()

                Toast.makeText(this@CartListActivity, "the total is $totalExpense", Toast.LENGTH_SHORT).show()
                totalPaymentAmount.text = "$" + (totalFoodFee.text.toString().toDouble()
                        + deliveryPrice.text.toString().toDouble()
                        + taxPrice.text.toString().toDouble()).toString()
                      //  * 100.0).roundToInt() / 100.0).toString()
            }
        }

    }


}