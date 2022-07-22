package com.example.fooddash.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddash.R
import com.example.fooddash.models.FoodModel

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.viewholder_popular.view.*

class PopularFoodsAdapter(
    private val context: Context,
    private val popularFoodList: ArrayList<FoodModel>

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PopularFoodViewHolder(view: View): RecyclerView.ViewHolder(view)

    var onclickListener : OnclickListener? = null
    var addBtnClickListener : AddBtnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFoodViewHolder {
        return PopularFoodViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_popular, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = popularFoodList[position]
        if (holder is PopularFoodViewHolder){

            holder.itemView.rv_popular_title_text.text = model
                .title.toString()

            holder.itemView.rv_popular_price.text =
                model.fee!!.toString().toDouble().toString()

            holder.itemView.rv_popular_addBtn.setOnClickListener {
                //MainActivity().addToCart(model)
                if(addBtnClickListener != null){
                    addBtnClickListener!!.addBtnOnClick(model)
                }
            }

            Glide.with(holder.itemView.context)
                .load(model.pic)
                .into(holder.itemView.rv_popular_image)

            holder.itemView.setOnClickListener {
                if (onclickListener != null){
                    onclickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return popularFoodList.size
    }

    interface OnclickListener{
        fun onClick(position: Int, model: FoodModel)
    }

    interface AddBtnClickListener{
        fun addBtnOnClick(model: FoodModel)
    }



}