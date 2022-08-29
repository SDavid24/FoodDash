package com.example.fooddash.firebase

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.fooddash.fragments.PlaceOrderFragment
import com.example.fooddash.fragments.intro.LoginFragment
import com.example.fooddash.fragments.intro.SignupFragment
import com.example.fooddash.fragments.operations.CartListFragment
import com.example.fooddash.fragments.operations.HomeFragment
import com.example.fooddash.fragments.operations.ProfileFragment
import com.example.fooddash.models.OrderModel
import com.example.fooddash.models.User
import com.example.fooddash.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private val mFirestoreClass = FirebaseFirestore.getInstance()

    /**Method that handles adding user info into the database during signup using the generated UID*/
    fun registerUser(context: Context, fragment: Fragment, userInfo: User){
        mFirestoreClass.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener{
                when(fragment){
                    is SignupFragment ->
                        fragment.userRegisteredSuccess(context)
            }
        }
    }

    fun loadUserDataFragment(fragment: Fragment){
        mFirestoreClass.collection(Constants.USERS)
            .document(getCurrentUserId()).get()
            .addOnSuccessListener { document->

                val loggedInUser = document.toObject(User::class.java)!!
                when (fragment){
                    is LoginFragment ->{
                        fragment.signInSuccess(loggedInUser)
                    }

                    is HomeFragment ->{
                        fragment.updateNavigationUserDetails(loggedInUser)
                    }

                    is ProfileFragment -> {
                        fragment.setUserDataUI(loggedInUser)
                    }
                }
            }
    }


    /** A function for creating a new expense and making an entry in the database.*/
    fun createNewFoodOrder(fragment: PlaceOrderFragment, order: OrderModel){
        mFirestoreClass.collection(Constants.FOODORDERS)
            .document()   //Creating a database document for each boards that's created by using their UIDs which is gotten from the authentication side
            .set(order, SetOptions.merge()) //This sets & merges all the user Info that's passed
            .addOnSuccessListener { //This runs a code of our wish if the registration is successful

                //fragment.addUpdateExpenseListSuccess()
                Log.e(fragment.javaClass.simpleName, "Food ordered successfully")

            }.addOnFailureListener {
                    exception ->
                //fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,
                    "Error while ordering food",
                    exception)
            }
    }


    /**Function to to get the user current  Id*/
    fun getCurrentUserId() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if (currentUser != null){
            currentUserId = currentUser.uid
        }

        return currentUserId
    }


    /**A function to update the user profile data into the database.*/
    fun updateUserProfileData(fragment: Fragment, userHashMap: HashMap<String, Any>){
        mFirestoreClass.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {  //This runs a code of our wish if the registration is successful
                Log.i(fragment.javaClass.simpleName, "Profile Data update")

                when (fragment){
                    is ProfileFragment-> {
                        fragment.profileUpdateSuccess()
                    }

                }

            }.addOnFailureListener{
                    e->
                when (fragment){

                    is ProfileFragment->{
                        fragment.hideProgressDialog()
                    }
                }

                Log.i(fragment.javaClass.simpleName, "Error while creating a user", e)
               // Toast.makeText(, "Error while updating the profile!",
                   // Toast.LENGTH_LONG).show()
            }
    }
}