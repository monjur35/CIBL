package com.monjur.cibl.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.monjur.cibl.R
import com.monjur.cibl.adapters.CategoryAdapter
import com.monjur.cibl.databinding.FragmentDashboardBinding
import com.monjur.cibl.response.categoryList.Category


class DashboardFragment : Fragment() {
    var binding: FragmentDashboardBinding? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)

        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.nagadCard?.setOnClickListener {
            navigateToPayment("Nagad")
        }
        binding?.bkashCard?.setOnClickListener {
            navigateToPayment("Bkash")
        }

        binding?.productCard?.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_dashboardFragment_to_categoryListFragment)
        }
    }


    private fun navigateToPayment(paymentType: String) {
        val bundle = Bundle()
        bundle.putString("paymentType", paymentType)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_dashboardFragment_to_paymentFragment,bundle)

    }
    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}