package com.example.fooddash.fragments.intro

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fooddash.R
import com.example.fooddash.activity.NewMainActivity
import com.example.fooddash.databinding.FragmentLoginBinding
import com.example.fooddash.firebase.FirestoreClass
import com.example.fooddash.utils.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {
    private val auth = FirebaseAuth.getInstance()
    lateinit var loginBinding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        loginBinding = FragmentLoginBinding.inflate(inflater,container,false)

        //binding.viewmodel = vm//attach your viewModel to xml
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signupFragment = SignupFragment()

        //On click listener for the sign in button
        loginBinding.btnSignInFragment.setOnClickListener {
            signInUserWithDetails()
        }

        //On click listener for the highlighted sign up text
        loginBinding.textSignUpFragment.setOnClickListener{
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, signupFragment)
                commit()
            }
        }
    }

    /**Method that carries out the sign in process by first calling the form validity function and then calling the Firestore sign in function*/
    private fun signInUserWithDetails() {
        val email = signInEmail_fragment.text.toString().trim { it <= ' ' }
        val password = signInPassword_fragment.text.toString().trim { it <= ' ' }

        //Checking the form validity
        if (checkForm(email, password)){
            signInLoading()

            //Signing in the user using the firebase auth inbuilt method
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
                if (task.isSuccessful){

                    //Loading the signed in user's data from Firestore using the method from Firestore class
                    FirestoreClass().loadUserDataFragment(this)

                    (activity as NewMainActivity).showAppBar()

                }else{
                    signInLoadingStopped()
                    Toast.makeText(
                        requireContext(), "Sign in failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**Method that first checks the validity of the form if it is ready to be processed*/
    private fun checkForm(email: String, password: String) : Boolean{

        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter your email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter your password")
                false
            }

            else -> {
                true
            }
        }
    }

    /**Actions to be carried when the sign in process is on going*/
    private fun signInLoading(){
        loginBinding.btnSignInFragment.visibility = View.GONE
        loginBinding.signInProgressBarFragment.visibility = View.VISIBLE
    }

    /**Actions to be carried out if an error occurs and the sign in process stops*/
    private fun signInLoadingStopped(){
        loginBinding.btnSignInFragment.visibility = View.VISIBLE
        loginBinding.signInProgressBarFragment.visibility = View.GONE
    }

}