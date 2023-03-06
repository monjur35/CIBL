package com.monjur.cibl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.monjur.cibl.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {
    var binding:FragmentDashboardBinding?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDashboardBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.nagadCard?.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_dashboardFragment_to_paymentFragment)
        }
        binding?.bkashCard?.setOnClickListener {

        }
    }


    private fun navigateToPayment(paymentType:String){
        var bundle=Bundle()

    }

}