package com.example.fooddash.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.fooddash.fragments.operations.HomeFragment
import com.example.fooddash.R
import com.example.fooddash.models.User
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment(R.layout.fragment_base) {
    private lateinit var mProgressDialog : Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    /**Method that handles the immediate afterwards of the sign up process*/
    fun userRegisteredSuccess(context: Context){
        Toast.makeText(context, "You have successfully registered",
            Toast.LENGTH_LONG).show()
    }

    fun signInSuccess(user: User) {
        val homeFragment = HomeFragment()

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, homeFragment)
            commit()

            //finish() //Finishes the activity
        }
    }


    /**Method to show the circling progress dialog when something is loading*/
    fun showProgressDialog(){

        /*Set the screen content from a layout resource
        The resource will be inflated, adding all top-level views to the screen
         */
        // mProgressDialog.(R.id.signInProgressBar)  //Setting the circling progress icon
        // mProgressDialog.tv_progress_text.text = text   //Setting the text

        //Starts the dialog and displays is on the screen
        mProgressDialog.show()
    }

    /**Method to dismiss dialog*/
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    /**Method for the snack bar that'll display throughout the app*/
    fun showErrorSnackBar(message: String){
        val snackBar = Snackbar.make(
           // binding!!.root.findViewById(
            requireActivity().findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        snackBarView.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                R.color.snackBar_error_color))  //To set background color of snack bar

        snackBar.show()
        //    <color name="snackBar_error_color">#F72400</color>
    }

}