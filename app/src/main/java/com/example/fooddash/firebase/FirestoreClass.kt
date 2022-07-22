package com.example.fooddash.firebase

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.fooddash.fragments.intro.LoginFragment
import com.example.fooddash.fragments.intro.SignupFragment
import com.example.fooddash.fragments.operations.HomeFragment
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

                }
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

}