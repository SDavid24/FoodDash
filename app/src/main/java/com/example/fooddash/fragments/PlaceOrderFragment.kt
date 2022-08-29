package com.example.fooddash.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fooddash.R
import com.example.fooddash.activity.NewMainActivity
import com.example.fooddash.databinding.FragmentPlaceOrderBinding
import com.example.fooddash.firebase.FirestoreClass
import com.example.fooddash.fragments.operations.CartListFragment
import com.example.fooddash.models.FoodModel
import com.example.fooddash.models.OrderModel
import com.example.fooddash.room.FoodCartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class PlaceOrderFragment : Fragment() {
   lateinit var placeOrderBinding: FragmentPlaceOrderBinding

    var viewModel2: FoodCartViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        placeOrderBinding = FragmentPlaceOrderBinding.inflate(layoutInflater, container, false)

        // Inflate the layout for this fragment
        return placeOrderBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as NewMainActivity?)!!.hideAppBar()
        (activity as NewMainActivity).cartAlreadyClicked = false

        viewModel2 = ViewModelProvider(requireActivity()).get(FoodCartViewModel::class.java)
        configPaymentAmount()
        uploadNewOrder()
    }

    @SuppressLint("SetTextI18n")
    private fun configPaymentAmount() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel2 = ViewModelProvider(requireActivity()).get(FoodCartViewModel::class.java)

            var totalExpense = viewModel2!!.getTotalPriceOfItems()

            //Setting the view configurations(the prices to be paid) from the coroutine thread
            withContext(Dispatchers.Main) {
                placeOrderBinding.totalPayment.text =
                    ((totalExpense * 100.0).roundToInt() / 100.0).toString()

                val initialTotalPaymentAmount =
                    (placeOrderBinding.totalPayment.text.toString().toDouble() + 11)

                placeOrderBinding.totalPayment.text =
                    ((initialTotalPaymentAmount * 100.0).roundToInt() / 100.0).toString()

                totalExpense = placeOrderBinding.totalPayment.text.toString().toDouble()

                Toast.makeText(
                    requireContext(), "New total amount it: $totalExpense",
                    Toast.LENGTH_LONG
                ).show()

                placeOrderBinding.btnPlaceOrder.setOnClickListener {
                    val cartItems = viewModel2!!.readAllData
                    val totalAmount = totalExpense
                    val address = placeOrderBinding.textAddress.text.toString()
                    val customerId = FirestoreClass().getCurrentUserId()

                    val orderModel = OrderModel(0, cartItems, totalAmount, address, customerId)

                    FirestoreClass().createNewFoodOrder(this@PlaceOrderFragment, orderModel)

                }
            }

        }
    }

    fun uploadNewOrder(){
        val cartItems = listOf(FoodModel())
        Log.e("List of Items:","$cartItems")
    }









}