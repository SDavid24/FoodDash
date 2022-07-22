package com.example.fooddash.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddash.R
import com.example.fooddash.models.CategoryModel
import kotlinx.android.synthetic.main.viewholder_category.view.*

class CategoryAdapter(
    private val list : ArrayList<CategoryModel>,
    private val context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder){
            holder.itemView.categoryName.text = list[position].title.toString()

            var picUrl = ""
            when {
                position % 5 == 0 -> {
                    picUrl = "cat_1"

                    holder.itemView.background =
                        ContextCompat.getDrawable(holder.itemView.context,
                            R.drawable.cat_background1
                        )
                }
                position % 5 == 1 -> {
                    picUrl = "cat_2"

                    holder.itemView.background =
                        ContextCompat.getDrawable(holder.itemView.context,
                            R.drawable.cat_background2
                        )
                }
                position % 5 == 2 -> {
                    picUrl = "cat_3"

                    holder.itemView.background =
                        ContextCompat.getDrawable(holder.itemView.context,
                            R.drawable.cat_background3
                        )

                }
                position % 5 == 3 -> {
                    picUrl = "cat_4"
                    holder.itemView.background =
                        ContextCompat.getDrawable(holder.itemView.context,
                            R.drawable.cat_background4
                        )

                }
                position % 5 == 4 -> {
                    picUrl = "cat_5"
                    holder.itemView.background =
                        ContextCompat.getDrawable(holder.itemView.context,
                            R.drawable.cat_background5
                        )
                }

            }

            val drawableResourceId = holder.itemView.context.resources.getIdentifier(
                picUrl,
                "drawable",
                holder.itemView.context.packageName
            )

            Glide.with(holder.itemView.context)
                .load(drawableResourceId)
                .into(holder.itemView.categoryPic)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}