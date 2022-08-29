package com.example.fooddash.fragments.operations

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fooddash.activity.NewMainActivity
import com.example.fooddash.R
import com.example.fooddash.models.FoodModel
import com.example.fooddash.room.FoodCartViewModel
import com.example.fooddash.utils.Constants
import kotlinx.android.synthetic.main.fragment_show_details.*
import kotlin.math.roundToInt

class ShowDetailsFragment : Fragment(R.layout.fragment_show_details) {

    var foodDetailsModel : FoodModel? = null
    var orderAmount = 1
    var cartListFragment = CartListFragment()
    lateinit var viewModel: FoodCartViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressed()
        /**Initializing the view model provider*/
        viewModel = ViewModelProvider(this).get(FoodCartViewModel::class.java)

        getIntData()
        addToCart()
    }

    /**Customizing the back press function so it can show the */
    private fun onBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val homeFragment = HomeFragment()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, homeFragment)
                    commit()

                    //(activity as NewMainActivity?)!!.showAppBar()
                }
            }

        })

    }

    /**Method that gets and displays the information of the chosen food that was sent into this activity*/
    private fun getIntData(){
        val bundle = this.arguments
        foodDetailsModel = bundle!!.getParcelable(Constants.FOOD_DETAILS)

        showDetails_titleTxt_fragment!!.text = foodDetailsModel!!.title
        showDetails_priceTxt_fragment!!.text = ("$" + foodDetailsModel!!.fee.toString())
        showDetails_descriptionTxt_fragment!!.text = foodDetailsModel!!.description.toString()
        showDetails_foodImage_fragment!!.setImageResource(foodDetailsModel!!.pic!!)
        showDetails_numberOfOrderTxt_fragment!!.text = foodDetailsModel!!.numberInCart.toString()
        showDetails_numberOfOrderTxt_fragment!!.text = orderAmount.toString()

        //Click listener for the plus button so it can increase the number food amount when clicked
        showDetails_plusBtn_fragment!!.setOnClickListener {
            orderAmount += 1
            showDetails_numberOfOrderTxt_fragment!!.text = orderAmount.toString()
        }

        //Click listener for the minus button so it can reduce the number food amount when clicked
        showDetails_minusBtn_fragment!!.setOnClickListener {
            if (orderAmount == 1){
                orderAmount = 1
            }else{
                orderAmount -= 1
                showDetails_numberOfOrderTxt_fragment.text = orderAmount.toString()
            }
        }

        //Click listener for the add to cart button
        showDetails_addToCartBtn_fragment!!.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, cartListFragment)
                //addToBackStack(homeFragment.toString())
                commit()
            }
        }
    }


    /**Method that gets the current details of the meals after it must have been edited before being sent to the cart activity*/
    fun newDetails(): FoodModel{

        val new = FoodModel(0,
            showDetails_titleTxt_fragment!!.text.toString(),
            foodDetailsModel!!.pic!!,
            showDetails_descriptionTxt_fragment!!. text.toString(),
            foodDetailsModel!!.fee,
            totalPriceOfFood(),
            showDetails_numberOfOrderTxt_fragment!!.text.toString().toInt()
        )
        return new
    }


    /**Method that multiplies the total number of food by the price of each to it can be appended in the databse*/
    private fun totalPriceOfFood(): Double{
        val totalPrice =
            (foodDetailsModel!!.fee?.times(showDetails_numberOfOrderTxt_fragment!!.text.toString().toInt())!!.times(100.0)).roundToInt() / 100.0

        return totalPrice
    }


    /**Method that adds the food the page to the cart database  and also takes it to the Cart list activity*/
    private fun addToCart(){
        showDetails_addToCartBtn_fragment!!.setOnClickListener {

            viewModel.insertFoodIntoCart(newDetails())  // calling the view model function

            Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_LONG).show()

            //startActivity(Intent(requireContext(), CartListActivity::class.java))


            parentFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, cartListFragment)
                commit()

                (activity as NewMainActivity?)!!.showAppBar()  //bringing back the B.Nav view

            }
        }

    }
}