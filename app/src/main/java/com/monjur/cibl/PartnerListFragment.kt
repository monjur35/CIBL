package com.monjur.cibl

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.monjur.cibl.adapters.PartnerAdapter
import com.monjur.cibl.databinding.FragmentPartnerListBinding
import com.monjur.cibl.response.categoryList.partnerlist.Partner
import com.monjur.cibl.viewmodel.CiblViewModel


class PartnerListFragment : Fragment() {
    var binding: FragmentPartnerListBinding?=null
    lateinit var ciblViewModel: CiblViewModel
    lateinit var partnerApdapter: PartnerAdapter
    var list= mutableListOf<Partner>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPartnerListBinding.inflate(inflater)
        ciblViewModel= ViewModelProvider(this)[CiblViewModel::class.java]
        partnerApdapter= PartnerAdapter(list,this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pcId=arguments?.getString("pcId")

        if (pcId != null) {
            ciblViewModel.getPartnerList(pcId).observe(viewLifecycleOwner, Observer {

                if (it!=null){
                    if (  it.partners.isNotEmpty()){
                        list.clear()
                        list.addAll(it.partners)
                        Log.e("TAG", "click: ${it.partners}", )
                        partnerApdapter.notifyDataSetChanged()
                    }
                }

            })
        }


    }


}