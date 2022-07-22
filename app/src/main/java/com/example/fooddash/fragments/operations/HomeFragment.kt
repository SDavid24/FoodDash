package com.example.fooddash.fragments.operations

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fooddash.activity.NewMainActivity
import com.example.fooddash.R
import com.example.fooddash.adapters.CategoryAdapter
import com.example.fooddash.adapters.PopularFoodsAdapter
import com.example.fooddash.databinding.FragmentHomeBinding
import com.example.fooddash.firebase.FirestoreClass
import com.example.fooddash.models.CategoryModel
import com.example.fooddash.models.FoodModel
import com.example.fooddash.models.User
import com.example.fooddash.room.FoodCartViewModel
import com.example.fooddash.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    val newFoodList: ArrayList<FoodModel> = ArrayList()
    lateinit var viewModel: FoodCartViewModel

    val showDetailsFragment = ShowDetailsFragment()
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        //binding.viewmodel = vm//attach your viewModel to xml
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**Initializing the view model provider*/
        viewModel = ViewModelProvider(this).get(FoodCartViewModel::class.java)

        //loading the user's data into the apps UI from firebase
        FirestoreClass().loadUserDataFragment(this)

        recyclerViewPopularInFrag()
        recyclerViewCategoryInFrag()
    }

    /**A function to get the current user details from firebase and display it in the UI.*/
    @SuppressLint("SetTextI18n")
    fun updateNavigationUserDetails(user: User){
        binding.greetNameTextFragment.text = "Hi ${user.name}"

        Glide.with(requireContext())
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(binding.userRoundImageFragment)
    }


    /**Method that adds a food in the popular food category to cart*/
    fun addToCart(foodModel: FoodModel){
        viewModel.insertFoodIntoCart(foodModel)
        Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_LONG).show()
    }

    /**Recycler view set up for the popular foods section*/
    private fun recyclerViewPopularInFrag() {

        newFoodList.add(
            FoodModel(0,
                "Pepperoni pizza", R.drawable.pizza1,
                "slices pepperoni,mozzerella cheese,fresh oregano, " +
                        "ground black pepper,pizza sauce",
                9.76, 9.76
            )
        )

        newFoodList.add(
            FoodModel(0,
                "Cheese Burger", R.drawable.burger,
                "beef, Gouda Cheese, Special Sauce, Lettuce, tomato",
                8.79, 8.79
            )
        )

        newFoodList.add(
            FoodModel(0,
                "Vegetable pizza", R.drawable.pizza2,
                "olive oil, Vegetable oil, pitted kalamata, " +
                        "cherry tomatoes, fresh oregano, basil",
                8.5, 8.5
            )
        )

        val adapter = PopularFoodsAdapter(requireContext(), newFoodList)

        popular_section_recyclerview_fragment.adapter = adapter

        popular_section_recyclerview_fragment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        popular_section_recyclerview_fragment.setHasFixedSize(true)

     /**Click listener for the whole recycler view interface that takes it to the details page*/


        adapter.onclickListener = object : PopularFoodsAdapter.OnclickListener {
            override fun onClick(position: Int, model: FoodModel) {
                val bundle = Bundle()
                bundle.putParcelable(Constants.FOOD_DETAILS, model)
                showDetailsFragment.arguments = bundle
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, showDetailsFragment)
                    addToBackStack(null)
                    commit()

                    //NewMainActivity().hideAppBar()
                    (activity as NewMainActivity?)!!.hideAppBar()

                }
               // val intent = Intent(this, ShowDetailsActivity::class.java)
                //intent.putExtra(Constants.FOOD_DETAILS, model)
            }
        }


        /**Click listener for the add to cart button*/
        adapter.addBtnClickListener = object : PopularFoodsAdapter.AddBtnClickListener{
            override fun addBtnOnClick(model: FoodModel) {
                addToCart(model)
            }
        }

    }

    /**Recycler view set up for the food category section*/
    private fun recyclerViewCategoryInFrag() {

        val category: ArrayList<CategoryModel> = ArrayList()
        category.add(CategoryModel("Pizza", "cat_1"))
        category.add(CategoryModel("Burger", "cat_2"))
        category.add(CategoryModel("Hotdog", "cat_3"))
        category.add(CategoryModel("Drink", "cat_4"))
        category.add(CategoryModel("Donut", "cat_5"))

        val newAdapter = CategoryAdapter(category, requireContext())
        categories_recyclerview_fragment.adapter = newAdapter

        categories_recyclerview_fragment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        categories_recyclerview_fragment.setHasFixedSize(true)
    }


}