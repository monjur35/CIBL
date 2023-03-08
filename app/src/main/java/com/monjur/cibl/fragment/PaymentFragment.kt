package com.monjur.cibl.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.card.MaterialCardView
import com.monjur.cibl.R
import com.monjur.cibl.databinding.FragmentPaymentBinding
import com.monjur.cibl.models.PdfData
import com.monjur.cibl.utils.PDFConverter
import java.util.*


class PaymentFragment : Fragment() {
    var binding:FragmentPaymentBinding?=null
    lateinit var client : FusedLocationProviderClient
    var fusedlocation=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPaymentBinding.inflate(inflater)
        client= LocationServices.getFusedLocationProviderClient(requireActivity());
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val paymentType=arguments?.getString("paymentType")
        getCurrentLocation()

        if (paymentType=="Bkash"){
            binding?.icon?.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                R.drawable.bkash_money_send_icon
            ) )
            binding?.numberTxt?.text="Bkash number *"
        }
        else{
            binding?.icon?.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_nagad
            ) )
            binding?.numberTxt?.text="Nagad number *"
        }


        binding?.submitBtn?.setOnClickListener {
            val number=binding?.numberEt?.text.toString()
            val name=binding?.fullNameEt?.text.toString()
            val narration=binding?.narrationET?.text.toString()
            val amount=binding?.amountEt?.text.toString()

            if (checkValidation(number,name,amount,paymentType)){
                showAlertDialog(number,name,amount,narration,paymentType)
            }
        }
    }

    private fun checkValidation(number: String, name: String, amount: String, paymentType: String?): Boolean {
        if (number.isEmpty()){
            binding?.numberEt?.error="Enter $paymentType number"
            binding?.numberEt?.hasFocus()
            return false
        }else if (number.length!=11){
            binding?.numberEt?.error="Number must be 11 digits"
            return false
        }else if (name.isEmpty()){
            binding?.fullNameEt?.error="Please,enter name"
            return false
        } else if (amount.isEmpty()){
            binding?.amountEt?.error="Please,enter amount"
            return false
        }
        return true
    }


    private fun showAlertDialog(
        number: String,
        name: String,
        amount: String,
        narration: String,
        paymentType: String?
    ) {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.status_dialog, null)

        val dismiss=dialogLayout.findViewById<MaterialCardView>(R.id.dissmiss)
        val icon=dialogLayout.findViewById<ImageView>(R.id.imageView)
        val fundTransferTxt=dialogLayout.findViewById<TextView>(R.id.fundTransferTxt)
        val amountTv=dialogLayout.findViewById<TextView>(R.id.amount)
        val transactionTime=dialogLayout.findViewById<TextView>(R.id.time)
        val location=dialogLayout.findViewById<TextView>(R.id.location)
        val narrationTv=dialogLayout.findViewById<TextView>(R.id.narration)
        val numberTv=dialogLayout.findViewById<TextView>(R.id.number)
        val numbertxt=dialogLayout.findViewById<TextView>(R.id.paymentTypeNumberTxt)
        val totalTv=dialogLayout.findViewById<TextView>(R.id.totalAmount)
        val downloadReceipt=dialogLayout.findViewById<TextView>(R.id.downloadPdf)
        val shareReceipt=dialogLayout.findViewById<TextView>(R.id.sharePdf)



        if (paymentType=="Bkash"){
            icon.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                R.drawable.bkash_money_send_icon
            ))
        }
        else{
           icon.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
               R.drawable.ic_nagad
           ) )
        }
        fundTransferTxt.text="$paymentType Fund Transfer"
        amountTv.text=amount.toDouble().toString()
        transactionTime.text=Date().time.toString()
        totalTv.text="BDT $amount"
        numberTv.text=number
        numbertxt.text="$paymentType number"
        narrationTv.text=narration
        location.text=fusedlocation

        val pdfData= PdfData(name,amount,number,narration,paymentType.toString(),fusedlocation,transactionTime.text.toString())


        downloadReceipt.setOnClickListener {
            downloadReceipt.visibility=View.GONE
            shareReceipt.visibility=View.GONE

            val pdfConverter = PDFConverter()
            pdfConverter.createPdf(requireContext(),pdfData, requireActivity())


            downloadReceipt.visibility=View.VISIBLE
            shareReceipt.visibility=View.VISIBLE
        }


        builder.setView(dialogLayout)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dismiss.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun getCurrentLocation(){
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            val priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            val cancellationTokenSource = CancellationTokenSource()

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            client.getCurrentLocation(priority, cancellationTokenSource.token)
                .addOnSuccessListener { location ->
                    Log.d("Location", "location is found: $location")
                    Log.e("Location", "getCurrentLocation: ${location.latitude}", )

                    if (location!=null){
                        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
                        val address = geoCoder.getFromLocation(location.latitude,location.longitude,1)
                        fusedlocation = address?.get(0)?.adminArea.toString()
                    }
                    else{
                        client.lastLocation.addOnSuccessListener {
                            if (it!=null){
                                val geoCoder = Geocoder(requireContext(), Locale.getDefault())
                                val address = geoCoder.getFromLocation(it.latitude,it.longitude,1)
                                fusedlocation = address?.get(0)?.adminArea.toString()

                                Log.e("Location", "getCurrentLocation: 1 $fusedlocation", )
                            }
                        }
                    }



                }
                .addOnFailureListener { exception ->
                    Log.d("Location", "Oops location failed with exception: $exception")
                }
        }
    }
}