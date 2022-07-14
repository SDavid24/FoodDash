package com.example.fooddash

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {
    private lateinit var mProgressDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }


    /**Method that handles the immediate afterwards of the sign up process*/
    fun userRegisteredSuccess(){
        Toast.makeText(this, "You have successfully registered",
            Toast.LENGTH_LONG).show()
        //hideProgressDialog()

        //FirebaseAuth.getInstance().signOut()
        finish()
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
}