package com.example.fooddash.fragments.intro

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fooddash.R
import com.example.fooddash.databinding.FragmentSignupBinding
import com.example.fooddash.firebase.FirestoreClass
import com.example.fooddash.fragments.operations.HomeFragment
import com.example.fooddash.models.User
import com.example.fooddash.utils.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignupFragment : BaseFragment() {
    private val auth = FirebaseAuth.getInstance()
    lateinit var signUpBinding: FragmentSignupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        signUpBinding = FragmentSignupBinding.inflate(inflater,container,false)

        //binding.viewmodel = vm//attach your viewModel to xml
        return signUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpBinding.btnSignUpFragment.setOnClickListener {
            signupUser()
        }
        signUpBinding.txtSignInFragment.setOnClickListener {
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, loginFragment)
                commit()
            }
        }
    }


    /**Method that carries out the sign up process by first calling the form validity function and then calling the Firestore sign in function*/
    private fun signupUser(){
        //Initialising the text views
        val name = signUpBinding.signUpFirstNameFragment.text.toString().trim { it <= ' ' }
        val email = signUpBinding.signUpEmailFragment.text.toString().trim { it <= ' ' }
        val mobile = signUpBinding.signUpMobileFragment.text.toString().trim { it <= ' ' }
        val password= signUpBinding.signUpPasswordFragment.text.toString().trim { it <= ' ' }
        val confirmPassword = signUpBinding.signUpConfirmPasswordFragment.text.toString().trim { it <= ' ' }

        //Checking the form validity
        if (checkForm(name, email, mobile, password, confirmPassword)){
            if(password == confirmPassword ) {
                signUploading()

                //Creating the user using the firebase auth inbuilt method
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result.user!!
                        val newEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name,
                            newEmail, mobile.toLong()
                        )

                        //Updating the new user's data to Firestore using the
                        // method from Firestore class
                        FirestoreClass().registerUser(requireContext(), this , user)
                        signUploadingStopped()
                        goToHomeFragment()

                    } else {
                        Toast.makeText(
                            requireContext(), "Registration failed", Toast.LENGTH_SHORT
                        ).show()
                        signUploadingStopped()
                    }
                }

            }else{
                signUploadingStopped()

                Toast.makeText(requireContext(), "Password is not consistent." +
                        " Confirm again! ", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**Method that first checks the validity of the form if it is ready to be processed*/
    private fun checkForm(firstName: String, email: String, mobile: String, password: String, confirmPassword: String) : Boolean{

        return when {
            TextUtils.isEmpty(firstName) -> {
                showErrorSnackBar("Please enter a firstName")
                false
            }
            TextUtils.isEmpty(mobile) -> {
                showErrorSnackBar("Please enter a mobile no.")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email")
                return false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter an password")
                return false
            }
            TextUtils.isEmpty(confirmPassword) -> {
                showErrorSnackBar("Please confirm password")
                return false
            }

            else -> {
                return true
            }
        }
    }

    /**Actions to be carried when the sign up process is on going*/
    private fun signUploading(){
        signUpBinding.btnSignUpFragment.visibility = View.GONE
        signUpBinding.signUpProgressBarFragment.visibility = View.VISIBLE
    }

    /**Actions to be carried out if an error occurs and the sign up process stops*/
    fun signUploadingStopped(){
        signUpBinding.btnSignUpFragment.visibility = View.VISIBLE
        signUpBinding.signUpProgressBarFragment.visibility = View.GONE
    }

    private fun goToHomeFragment(){
        val homeFragment = HomeFragment()
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, homeFragment)
            commit()
        }
    }

}