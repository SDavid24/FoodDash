package com.example.fooddash.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment

object Constants {

    const val USERS = "Users"
    const val FOOD_DETAILS = "food_details"
    const val NAME : String = "name"
    const val EMAIL : String  = "email"
    const val MOBILE : String  = "mobile"
    const val IMAGE : String = "image"
    const val HOMEADDRESS: String = "homeAddress"
    const val WORKADDRESS: String = "workAddress"
    const val  TITLE = "title"
    const val  PIC = "pic"
    const val DESCRIPTION = "description"
    const val FEE = "fee"

    const val FOODORDERS = "Food Orders"
    const val ORDER_DETAILS = "order_details"

    //Create a variable for GALLERY Selection which will be later used in the onActivityResult method
    const val READ_STORAGE_PERMISSION_CODE = 1
    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2

    const val CREATE_BOARD_REQUEST_CODE = 10

    /**A function for user profile image selection from phone storage*/
    fun showImageChooser(fragment: Fragment) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // Launches the image selection of phone storage using the constant code.
        fragment.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**A function to get the extension of selected image.*/
    fun getFileExtension(activity: Activity, uri: Uri) : String?{
        return  MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri))
    }
}