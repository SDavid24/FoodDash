package com.example.fooddash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddash.adapters.CategoryAdapter
import com.example.fooddash.adapters.PopularFoodsAdapter
import com.example.fooddash.databinding.ActivityMainBinding
import com.example.fooddash.models.CategoryModel
import com.example.fooddash.models.FoodModel
import com.example.fooddash.room.FoodCartViewModel
import com.example.fooddash.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: FoodCartViewModel
    var foodDetailsModel : FoodModel?  = null

    val foodList: ArrayList<FoodModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**Initializing the view model provider*/

        viewModel = ViewModelProvider(this).get(FoodCartViewModel::class.java)

        recyclerViewCategory()

        recyclerViewPopular()
        bottomNavigation()


    }


    private fun bottomNavigation() {
        binding.homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.fabCartBtn.setOnClickListener {
            startActivity(Intent(this, CartListActivity::class.java))
        }
    }

    /**Recycler view set up for the food category section*/
    private fun recyclerViewCategory() {

        val category: ArrayList<CategoryModel> = ArrayList()
        category.add(CategoryModel("Pizza", "cat_1"))
        category.add(CategoryModel("Burger", "cat_2"))
        category.add(CategoryModel("Hotdog", "cat_3"))
        category.add(CategoryModel("Drink", "cat_4"))
        category.add(CategoryModel("Donut", "cat_5"))

        val adapter = CategoryAdapter(category,this)
        categories_recyclerview.adapter = adapter

        categories_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        categories_recyclerview.setHasFixedSize(true)
    }


    /**Recycler view set up for the popular foods section*/
    private fun recyclerViewPopular() {

        foodList.add(
            FoodModel(0,
                "Pepperoni pizza", R.drawable.pizza1,
                "slices pepperoni,mozzerella cheese,fresh oregano, " +
                        "ground black pepper,pizza sauce",
                9.76, 9.76
            )
        )
        foodList.add(
            FoodModel(0,
                "Cheese Burger", R.drawable.burger,
                "beef, Gouda Cheese, Special Sauce, Lettuce, tomato",
                8.79, 8.79
            )
        )
        foodList.add(
            FoodModel(0,
                "Vegetable pizza", R.drawable.pizza2,
                "olive oil, Vegetable oil, pitted kalamata, " +
                        "cherry tomatoes, fresh oregano, basil",
                8.5, 8.5
            )
        )

        val adapter = PopularFoodsAdapter(this, foodList)

        binding.popularSectionRecyclerview.adapter = adapter

        binding.popularSectionRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.popularSectionRecyclerview.setHasFixedSize(true)


        /**Click listener for the whole recyclr view interface that takes it to the details page*/
        adapter.onclickListener = object : PopularFoodsAdapter.OnclickListener {
            override fun onClick(position: Int, model: FoodModel) {
                val intent = Intent(this@MainActivity, ShowDetailsActivity::class.java)
                intent.putExtra(Constants.FOOD_DETAILS, model)
                startActivity(intent)
            }
        }

        /**Click listener for the add to cart button*/
        adapter.addBtnClickListener = object : PopularFoodsAdapter.AddBtnClickListener{
            override fun addBtnOnClick(model: FoodModel) {
                addToCart(model)
            }
        }
    }

    /**Method that adds a food in the popular food category to cart*/
    fun addToCart(foodModel: FoodModel){
        viewModel.insertFoodIntoCart(foodModel)
        Toast.makeText(this, "Added to cart", Toast.LENGTH_LONG).show()
    }



}