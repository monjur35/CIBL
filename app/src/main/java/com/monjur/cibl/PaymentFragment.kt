package com.monjur.cibl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.monjur.cibl.databinding.FragmentPaymentBinding


class PaymentFragment : Fragment() {
    var binding:FragmentPaymentBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPaymentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val paymentType=arguments?.getString("paymentType")

        if (paymentType=="Bkash"){
            binding?.icon?.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.bkash_money_send_icon) )
        }
        else{
            binding?.icon?.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_nagad) )
        }
    }


}