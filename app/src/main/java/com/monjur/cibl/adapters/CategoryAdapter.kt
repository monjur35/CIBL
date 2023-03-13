package com.monjur.cibl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.monjur.cibl.databinding.CategoryItemBinding
import com.monjur.cibl.fragment.CategoryListFragment
import com.monjur.cibl.response.categoryList.Category


class CategoryAdapter(
    private val categoryList: List<Category>,
    categoryListFragment: CategoryListFragment
): RecyclerView.Adapter<CategoryAdapter.CategoryViewholder>() {

   private val listener=categoryListFragment



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {
       val itemView=CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CategoryViewholder(itemView)
    }

    override fun getItemCount(): Int {
      return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {
        holder.bind(categoryList[position],listener)
    }



    class CategoryViewholder(private val binding:CategoryItemBinding): ViewHolder(binding.root) {

        fun bind(category: Category, listener: CategoryListFragment) {
            binding.name.text=category.name

            binding.root.setOnClickListener {
                if (category.child_count>0){
                    listener.click(category.pc_id)
                }
                else{
                    listener.navigateToDetails(category.pc_id)
                }
            }
        }

    }


    interface OnclickCard{
        fun click(parentId:String)
        fun navigateToDetails(pcID:String)
    }


}