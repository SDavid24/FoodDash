package com.example.fooddash.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddash.R
import com.example.fooddash.models.FoodModel
import kotlinx.android.synthetic.main.viewholder_cart.view.*
import kotlin.math.roundToInt

class CartListAdapter(
    private val context: Context,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private var cartList =  emptyList<FoodModel>()

    var plusBtnListener : PlusBtnListener? = null
    var minusBtnListener : MinusBtnListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.viewholder_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = cartList[position]

        if (holder is CartViewHolder){
            holder.itemView.picCart.setImageResource(model.pic.toString().toInt())
            holder.itemView.titleTxt.text = model.title.toString()
            holder.itemView.numberItemTxt.text = model.numberInCart.toString()
            holder.itemView.feeEachItem.text = model.fee.toString()


            val totalPrice = (((model.fee.toString().toDouble() * model.numberInCart.toString().toInt()) * 100.0).roundToInt() / 100.0).toString()

            model.totalFee = totalPrice.toDouble()
            holder.itemView.totalEachItem.text = model.totalFee.toString()

            holder.itemView.minusCartBtn.setOnClickListener {
                Toast.makeText(context, "You clicked on Minus button", Toast.LENGTH_SHORT).show()

                if (holder.itemView.numberItemTxt.text.toString().toInt() == 0){
                    holder.itemView.numberItemTxt.text = 0.toString()
                }else{
                    holder.itemView.numberItemTxt.text.toString().toInt() - 1
                }
            }

            holder.itemView.plusCartBtn.setOnClickListener {
                if (plusBtnListener != null){
                    plusBtnListener!!.plusOnClick(model)
                }
            }

            holder.itemView.minusCartBtn.setOnClickListener {
                if (minusBtnListener != null){
                    minusBtnListener!!.minusOnClick(model)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun foodPosition(position: Int) : FoodModel{
        return cartList[position]
    }


    fun setdata(foodModel: List<FoodModel>){
        this.cartList = foodModel
        notifyDataSetChanged()
    }

    interface PlusBtnListener{
        fun plusOnClick(model: FoodModel)
    }

    interface MinusBtnListener{
        fun minusOnClick(model: FoodModel)
    }
}