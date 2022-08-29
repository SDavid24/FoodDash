package com.example.fooddash.fragments.operations

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddash.R
import com.example.fooddash.activity.NewMainActivity
import com.example.fooddash.adapters.CartListAdapter
import com.example.fooddash.databinding.FragmentCartListBinding
import com.example.fooddash.fragments.PlaceOrderFragment
import com.example.fooddash.models.FoodModel
import com.example.fooddash.models.OrderModel
import com.example.fooddash.room.FoodCartViewModel
import com.example.fooddash.utils.Constants
import com.example.fooddash.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_cart_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class CartListFragment : Fragment() {
    var viewModel: FoodCartViewModel? = null
    lateinit var cartListBinding: FragmentCartListBinding
    private val placeOrderFragment = PlaceOrderFragment()
    private val orderModel : OrderModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        cartListBinding = FragmentCartListBinding.inflate(inflater,container,false)
        return cartListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NewMainActivity?)!!.showAppBar()
        (activity as NewMainActivity).homeAlreadyClicked = false

        setupCartRecyclerviewFrag()

        cartListBinding.checkOutBtn.setOnClickListener {
            val bundle = Bundle()
            //bundle.putParcelable(Constants.ORDER_DETAILS, orderModel!!)
            //placeOrderFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, placeOrderFragment)
                .addToBackStack(null)
                .commit()
        }
    }


    /**Method that sets up the recycler view of the cart list*/
    private fun setupCartRecyclerviewFrag(){
        val adapter = CartListAdapter(requireContext())
        rv_cartView_fragment.adapter = adapter
        rv_cartView_fragment.layoutManager = LinearLayoutManager(requireContext())
        rv_cartView_fragment.setHasFixedSize(true)
        viewModel = ViewModelProvider(requireActivity()).get(FoodCartViewModel::class.java)

        viewModel!!.readAllData.observe(requireActivity(), Observer { foodModel->

            //Conditional on which view should display based on if there is an item or not
            if(foodModel.isNotEmpty()) {
                cartListBinding.cartIsEmptyTxt.visibility = View.GONE
                cartListBinding.cartListScrollViewFragment.visibility = View.VISIBLE
                adapter.setdata(foodModel)
                configureScreen()

            }else{
                cartListBinding.cartIsEmptyTxt.visibility = View.VISIBLE
                cartListBinding.cartListScrollViewFragment.visibility = View.GONE
            }
        })

        //Onclick listener for add button
        adapter.plusBtnListener = object : CartListAdapter.PlusBtnListener {
            override fun plusOnClick(model: FoodModel) {
                increaseNoOfFood(model)
            }
        }

        //Onclick listener for minus button
        adapter.minusBtnListener = object : CartListAdapter.MinusBtnListener {
            override fun minusOnClick(model: FoodModel) {
                decreaseNoOfFood(model)
            }
        }

        // This binds the delete feature class to recyclerview)
        val deleteSwipeHandler = object : SwipeToDeleteCallback(requireContext()){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // This below Calls the adapter function when it is swiped for delete
                val adapter = rv_cartView_fragment.adapter as CartListAdapter
                var model = FoodModel()
                /**adapter.removeAt(viewHolder.adapterPosition)*/
                viewModel!!.deleteFoodNoInCart(adapter.foodPosition(viewHolder.bindingAdapterPosition))
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_cartView_fragment)
    }

    /**Method that carries out the increase of food amount when the add button in the recycler view is clicked on*/
    fun increaseNoOfFood(model: FoodModel){
        val newNumber = model.numberInCart + 1
        val newFoodCart = FoodModel(model.foodId, model.title,
            model.pic, model.description,
            model.fee, newNumber * model.fee!!, newNumber )

        viewModel!!.updateFoodNoInCart(newFoodCart) //updating the food amount into the Room DB
        configureScreen()
    }

    /**Method that carries out the decrease of food amount when the add button in the recycler view is clicked on*/
    fun decreaseNoOfFood(model: FoodModel){
        if (model.numberInCart > 1) {
            val newNumber = model.numberInCart - 1

            val newFoodCart = FoodModel(
                model.foodId, model.title,
                model.pic, model.description,
                model.fee, newNumber * model.fee!!, newNumber
            )

            viewModel!!.updateFoodNoInCart(newFoodCart) //updating the food amount into the Room DB
            configureScreen()
        }else{
            //
        }
    }

    /**Method that immediately carries out the display when any button in the recycler view is clicked on*/
    @SuppressLint("SetTextI18n")
    private fun configureScreen()  {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel = ViewModelProvider(requireActivity()).get(FoodCartViewModel::class.java)

            var totalExpense = viewModel!!.getTotalPriceOfItems()

            //Setting the view configurations(the prices to be paid) from the coroutine thread
            withContext(Dispatchers.Main) {
                cartListBinding.totalFoodFee.text = ((totalExpense * 100.0).roundToInt() / 100.0).toString()

                val initialTotalPaymentAmount = (cartListBinding.totalFoodFee.text.toString().toDouble()
                        + cartListBinding.deliveryPrice.text.toString().toDouble()
                        + cartListBinding.taxPrice.text.toString().toDouble())

                cartListBinding.totalPaymentAmount.text = ((initialTotalPaymentAmount * 100.0).roundToInt() / 100.0).toString()

                totalExpense = cartListBinding.totalPaymentAmount.text.toString().toDouble()

                Toast.makeText(requireContext(), "New total amount it: $totalExpense",
                    Toast.LENGTH_LONG).show()

                Log.e("Total1:", viewModel!!.getTotalPriceOfItems().toString())
            }

           /* withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), ${viewModel!!.getTotalPriceOfItems().toString()},
                Toast.LENGTH_LONG).show()
            }*/
            Log.e("Total2:", viewModel!!.getTotalPriceOfItems().toString())
        }

    }

}