package com.example.fooddash.models

/*

class CatadapterJavaTesting(categoryDomains: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CatadapterJavaTesting.OldViewHolder>() {

    var categoryDomains: ArrayList<CategoryModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_category, parent, false)
        return RecyclerView.OldViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryName.setText(categoryDomains[position].getTitle())
        var picUrl = ""
        when (position) {
            0 -> {
                picUrl = "cat_1"
                holder.mainLayout.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.cat_background1)
            }
            1 -> {
                picUrl = "cat_2"
                holder.mainLayout.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.cat_background2)
            }
            2 -> {
                picUrl = "cat_3"
                holder.mainLayout.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.cat_background3)
            }
            3 -> {
                picUrl = "cat_4"
                holder.mainLayout.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.cat_background4)
            }
            4 -> {
                picUrl = "cat_5"
                holder.mainLayout.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.cat_background5)
            }
        }
        val drawableResourceId = holder.itemView.context.resources.getIdentifier(
            picUrl,
            "drawable",
            holder.itemView.context.packageName
        )
        Glide.with(holder.itemView.context)
            .load(drawableResourceId)
            .into(holder.categoryPic)
    }

    override fun getItemCount(): Int {
        return categoryDomains.size
    }

    inner class OldViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryName: TextView
        var categoryPic: ImageView
        var mainLayout: ConstraintLayout

        init {
            categoryName = itemView.findViewById(R.id.categoryName)
            categoryPic = itemView.findViewById(R.id.categoryPic)
            mainLayout = itemView.findViewById(R.id.mainLayout)
        }
    }

    init {
        this.categoryDomains = categoryDomains
    }
}
*/
