package com.example.fooddash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.fooddash.databinding.ActivitySignupBinding
import com.example.fooddash.firebase.FirestoreClass
import com.example.fooddash.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : BaseActivity() {
    lateinit var binding: ActivitySignupBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_signup)

        btnSignUp.setOnClickListener {
            //startActivity(Intent(this, LoginActivity::class.java))
            signupUser()
        }
    }

    fun signupUser(){
        val firstName = binding.signUpFirstName.toString().trim { it <= ' ' }
        val lastName = binding.signUpLastName.toString().trim { it <= ' ' }
        val email = binding.signUpEmail.toString().trim { it <= ' ' }
        val mobile = binding.signUpMobile.toString().trim { it <= ' ' }
        val password= binding.signUpPassword.toString().trim { it <= ' ' }
        val confirmPassword = binding.signUpConfirmPassword.toString().trim { it <= ' ' }

        signUploading()
        if (checkForm(firstName, lastName, email, mobile.toLong(), password, confirmPassword)){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task->

                if (task.isSuccessful){
                    val firebaseUser : FirebaseUser = task.result.user!!
                    val newEmail = firebaseUser.email!!
                    val user = User(firebaseUser.uid,firstName, lastName, newEmail, mobile.toLong()  )

                    FirestoreClass().registerUser(this, user)
                } else {
                    Toast.makeText(this, "Registration failed",
                        Toast.LENGTH_SHORT).show()
                    signUploadingStopped()
                }
                
            }
        }

    }

    fun checkForm(firstName: String, lastName: String, email: String, mobile: Long, password: String, confirmPassword: String, ) : Boolean{

        return if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(
                mobile.toString()
            ) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)){
            if (password == confirmPassword){
                true
            }else{
                Toast.makeText(this, "Password is not consistent. Confirm again! ", Toast.LENGTH_LONG).show()
                false
            }

        }else{
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_LONG).show()
            false

        }
    }


    fun signUploading(){
        binding.btnSignUp.visibility = View.GONE
        binding.signUpProgressBar.visibility = View.VISIBLE
    }

    fun signUploadingStopped(){
        binding.btnSignUp.visibility = View.VISIBLE
        binding.signUpProgressBar.visibility = View.GONE
    }

    fun goToMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}