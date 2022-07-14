package com.example.fooddash.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foodCart-table")
data class FoodModel(
    @PrimaryKey(autoGenerate = true)
    val foodId : Int = 0,
    val title: String? = null,
    val pic: Int? = null,
    val description: String? = null,
    val fee: Double? = null,
    var totalFee: Double? = null,
    val numberInCart: Int = 1
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString()!!,
        source.readInt(),
        source.readString()!!,
        source.readDouble(),
        source.readDouble(),
        source.readInt(),
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(foodId)
        writeString(title)
        writeInt(pic!!)
        writeString(description)
        writeDouble(fee!!)
        writeDouble(totalFee!!)
        writeInt(numberInCart)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FoodModel> = object : Parcelable.Creator<FoodModel> {
            override fun createFromParcel(source: Parcel): FoodModel = FoodModel(source)
            override fun newArray(size: Int): Array<FoodModel?> = arrayOfNulls(size)
        }
    }
}
