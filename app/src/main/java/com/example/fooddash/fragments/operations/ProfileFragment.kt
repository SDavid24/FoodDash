package com.example.fooddash.fragments.operations

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.fooddash.R
import com.example.fooddash.activity.NewMainActivity
import com.example.fooddash.databinding.FragmentProfileBinding
import com.example.fooddash.databinding.FragmentSignupBinding
import com.example.fooddash.firebase.FirestoreClass
import com.example.fooddash.fragments.intro.LoginFragment
import com.example.fooddash.models.User
import com.example.fooddash.utils.BaseFragment
import com.example.fooddash.utils.Constants
import com.example.fooddash.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException

class ProfileFragment : BaseFragment() {

    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageFileUri: String = ""
    val homeFragment = HomeFragment()
    private lateinit var mUserDetails : User
    lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as NewMainActivity).hideAppBar()
        //loading the user's data into the apps UI from firebase
        FirestoreClass().loadUserDataFragment(this)

        photoClickedOn()   //calling th photoClickedOn method
        updateProfile()
        //getBackStackTag()
    }

/*
    fun getBackStackTag() {
        val Home_TAG = HomeFragment()
        //val index = activity?.fragmentManager?.backStackEntryCount//?.minus(1)
        val index = activity?.fragmentManager?.backStackEntryCount//!! - 1
        val backEntry : FragmentManager.BackStackEntry= requireFragmentManager().getBackStackEntryAt(index!!)
        val tag = backEntry.name
        val frag: Fragment = parentFragmentManager.findFragmentByTag(tag)!!
        val fragment = requireFragmentManager().fragments[1]
        //val fragment = parentFragmentManager.findFragmentByTag(tag)
        //Toast.makeText(this, fragment, Toast.LENGTH_LONG).show()
        val name = requireFragmentManager().fragments[requireFragmentManager().fragments.size - 1]


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //val overviewFragment = ()
                Log.e("AppleFrag", fragment.toString())
                Log.e("AppleINDEX", index.toString())
                Log.e("AppleTAG", tag!!)
                Log.e("AppleNAME", name.toString())

                parentFragmentManager.beginTransaction().apply {
                    addToBackStack(null)
                    replace(R.id.flFragment, name)
                    commit()

                    (activity as NewMainActivity).showAppBar()
                }
            }
        })

    }
*/

    /**A function to set the existing details in UI.*/
    fun setUserDataUI(user: User) {
        // Initialize the user details variable
        mUserDetails = user

        //Load the user image in the ImageView.
        Glide
            .with(requireContext())
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(profileImage)

        profileBinding.etNameProfile.setText(user.name)
        profileBinding.etEmailProfile.setText(user.email)
        if (user.mobile != 0L) {
            profileBinding.etPhoneProfile.setText(user.mobile.toString())
        }
        profileBinding.etHomeAddressProfile.setText(user.homeAddress)
        profileBinding.etWorkAddressProfile.setText(user.workAddress)

        profileBinding.mainName.text = user.name
        profileBinding.mainEmail.text = user.email
    }


    /**A function to upload the selected user image to firebase cloud storage.*/
    fun uploadUserImage(){
        //showProgressDialog(resources.getString(R.string.please_wait))

        if(mSelectedImageFileUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance()
                .reference.child("USER_IMAGE"
                        + System.currentTimeMillis() + "."
                        + Constants.getFileExtension(requireActivity(), mSelectedImageFileUri!!)
                )

            //adding the file to reference
            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                // The image upload is success
                    taskSnapshot ->
                Log.i( "Firebase Image URL", taskSnapshot.metadata!!
                    .reference!!.downloadUrl.toString())

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri->
                    Log.i("Downloadable Image URL", uri.toString())

                    // assign the image url to the variable.
                    mProfileImageFileUri = uri.toString()

                    //hideProgressDialog()

                    // Call a function to update user details in the database.
                    updateUserProfileDataInProfAct()

                }
            }.addOnFailureListener{
                    exception -> Toast.makeText(requireContext(),
                exception.message, Toast.LENGTH_LONG).show()

                //hideProgressDialog()
            }
        }
    }

    /**Adds the click event for iv_profile_user_image*/
    fun photoClickedOn(){
        profileBinding.profileImage.setOnClickListener {

            if(ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ){
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_CODE)
            }
        }
    }

    /** This function will identify the result of runtime permission after the user allows or deny
     * permission based on the unique code.*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions:Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)  //Calling the image chooser function.)
            }else{
                Toast.makeText(
                    requireContext(),
                    "Oops you just denied the permission for storage. You can " +
                            "allow it in the settings", Toast.LENGTH_LONG
                ).show()
            }
        }
    }



    /** This gets the result of the image selection based on the constant code.*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null){

            //The uri of selected image from phone storage.
            mSelectedImageFileUri = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(profileBinding.profileImage)
            }catch (e: IOException){
                e.printStackTrace()
            }

        }
    }


    /**A function to update the user profile details into the database.*/
    fun updateUserProfileDataInProfAct(){
        val userHashMap = HashMap<String, Any>()
        var anyChangesMade = false

        if(mProfileImageFileUri.isNotEmpty()
            && mProfileImageFileUri != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageFileUri
            anyChangesMade = true
        }

        if(profileBinding.etNameProfile.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = profileBinding.etNameProfile.text.toString()
            anyChangesMade = true
        }

        if(profileBinding.etPhoneProfile.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = profileBinding.etPhoneProfile.text.toString().toLong()
            anyChangesMade = true
        }

        if(profileBinding.etHomeAddressProfile.text.toString() != mUserDetails.homeAddress){
            userHashMap[Constants.HOMEADDRESS] = profileBinding.etHomeAddressProfile.text.toString()
            anyChangesMade = true
        }

        if(profileBinding.etWorkAddressProfile.text.toString() != mUserDetails.workAddress){
            userHashMap[Constants.WORKADDRESS] = profileBinding.etWorkAddressProfile.text.toString()
            anyChangesMade = true
        }

        // Update the data in the database.
        if (anyChangesMade) {
            profileUpdateLoading()
            FirestoreClass().updateUserProfileData(this, userHashMap)

        }else{
            Toast.makeText(requireContext(), "No change was made", Toast.LENGTH_SHORT).show()
        }
    }

    /**Method that handles the immediate afterwards of the Profile update process*/
    fun profileUpdateSuccess(){
        Toast.makeText(requireContext(), "You have successfully updated your profile",
            Toast.LENGTH_LONG).show()

        //hideProgressDialog()
        //setResult(Activity.RESULT_OK)  //Setting the Result_Ok value when the profile updates


        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, homeFragment)
            commit()

            (activity as NewMainActivity).showAppBar()
        }
    }


    /**Click listener for the update button*/
    fun updateProfile(){
        profileBinding.btnUpdate.setOnClickListener {

            if(mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                //showProgressDialog(resources.getString(R.string.please_wait))

                updateUserProfileDataInProfAct()
            }
        }
    }


    /**Actions to be carried when the sign up process is on going*/
    private fun profileUpdateLoading(){
        profileBinding.btnUpdate.visibility = View.GONE
        profileBinding.profileUpdateProgressBar.visibility = View.VISIBLE
    }

    /**Actions to be carried out if an error occurs and the sign up process stops*/
    fun profileUpdateStopped(){
        profileBinding.btnUpdate.visibility = View.VISIBLE
        profileBinding.profileUpdateProgressBar.visibility = View.GONE
    }

}