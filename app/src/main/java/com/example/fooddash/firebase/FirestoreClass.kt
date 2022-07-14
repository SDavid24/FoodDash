package com.example.fooddash.firebase

import android.app.Activity
import com.example.fooddash.SignupActivity
import com.example.fooddash.models.User
import com.example.fooddash.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass {
    val mFirestoreClass = FirebaseFirestore.getInstance()

    /**Method that handles adding user info into the database during signup using the generated UID*/
    fun registerUser(activity: Activity, userInfo: User){
        mFirestoreClass.collection(Constants.USERS).document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener{
                when(activity){
                    is SignupActivity ->
                        activity.goToMainActivity()
            }
        }
    }

    fun getCurrentUserId() : String{
        val userId = FirebaseAuth.getInstance().currentUser
        var currentUser = ""

        if (userId != null){
            currentUser = userId.uid
        }

        return currentUser
    }

}