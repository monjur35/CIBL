package com.monjur.cibl.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.monjur.cibl.PartnerListFragment
import com.monjur.cibl.databinding.CategoryItemBinding
import com.monjur.cibl.fragment.CategoryListFragment
import com.monjur.cibl.response.categoryList.partnerlist.Partner


class PartnerAdapter(
    private val partnerList: List<Partner>,
    categoryListFragment: PartnerListFragment
): RecyclerView.Adapter<PartnerAdapter.CategoryViewholder>() {

   private val listener=categoryListFragment



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {
       val itemView=CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CategoryViewholder(itemView)
    }

    override fun getItemCount(): Int {
      return partnerList.size
    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {
        holder.bind(partnerList[position])
    }



    class CategoryViewholder(private val binding:CategoryItemBinding): ViewHolder(binding.root) {

        fun bind(partner: Partner) {
            binding.name.text=partner.partner_name

            binding.root.setOnClickListener {
               /* if (category.child_count>0){
                    listener.click(category.pc_id)
                }
                else{
                    listener.navigateToDetails()
                }*/
            }
        }

    }


  /*  interface OnclickCard{
        fun click(parentId:String)
        fun navigateToDetails()
    }*/


}