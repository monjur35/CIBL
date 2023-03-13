package com.monjur.cibl.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.monjur.cibl.R

import com.monjur.cibl.adapters.CategoryAdapter
import com.monjur.cibl.databinding.FragmentCategoryListBinding
import com.monjur.cibl.response.categoryList.Category
import com.monjur.cibl.viewmodel.CiblViewModel


class CategoryListFragment : Fragment() , CategoryAdapter.OnclickCard {
    var binding:FragmentCategoryListBinding?=null
    lateinit var ciblViewModel:CiblViewModel
    lateinit var categoryAdapter: CategoryAdapter
    var list= mutableListOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCategoryListBinding.inflate(inflater)
        ciblViewModel=ViewModelProvider(this)[CiblViewModel::class.java]
        categoryAdapter=CategoryAdapter(list,this)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.categoryRv?.apply {
            adapter=categoryAdapter
        }



        ciblViewModel.getCategories("benefit").observe(viewLifecycleOwner, Observer {
            if (it.categories.isNotEmpty()){
                list.clear()
                list.addAll(it.categories)
                categoryAdapter.notifyDataSetChanged()

                Log.e("TAG", "onViewCreated: ${it.categories}", )
            }
        })
    }

    override fun click(parentId: String) {
        Log.e("TAG", "click: $parentId", )
        ciblViewModel.getCategories1("benefit",parentId).observe(viewLifecycleOwner, Observer {
            if (it.categories.isNotEmpty()){
                list.clear()
                list.addAll(it.categories)
                Log.e("TAG", "click: ${it.categories}", )
                categoryAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun navigateToDetails(pcId:String) {
        val bundle=Bundle()
        bundle.putString("pcId",pcId)
        Navigation.findNavController(requireView()).navigate(R.id.action_categoryListFragment_to_partnerListFragment,bundle)
    }


}