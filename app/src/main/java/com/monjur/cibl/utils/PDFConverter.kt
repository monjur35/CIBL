package com.monjur.cibl.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import com.monjur.cibl.R
import com.monjur.cibl.databinding.FragmentPaymentBinding
import com.monjur.cibl.databinding.StatusDialogBinding
import com.monjur.cibl.models.PdfData
import java.io.*
import java.util.Date


class PDFConverter {

    private fun createBitmapFromView(
        context: Context,
        view: View,
        activity: Activity
    ): Bitmap {
        return createBitmap(context, view, activity)
    }

    private fun createBitmap(
        context: Context,
        view: View,
        activity: Activity,
    ): Bitmap {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return Bitmap.createScaledBitmap(bitmap, view.measuredWidth, view.measuredHeight, true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertBitmapToPdf(bitmap: Bitmap, context: Context) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        pdfDocument.finishPage(page)


        val s= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI.path
        } else {
            TODO("VERSION.SDK_INT < Q")
        }
        Log.e("TAG", "convertBitmapToPdf: $s", )
        val file = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS), "payment receipt${Date().time}.pdf")

       // val file = File(s, "bitmapPdf.pdf")
        Log.e("TAG", "convertBitmapToPdf:$file")

        try {
            // after creating a file name we will
            // write our PDF file to that location.



            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q){
                val finalUri : Uri? = copyFileToDownloads(context, file)
            }
            else{
                pdfDocument.writeTo(FileOutputStream(file))
            }
        //    val finalUri : Uri? = copyFileToDownloads(context, file)
            // below line is to print toast message
            // on completion of PDF generation.
           // Log.e("TAG", "convertBitmapToPdf: done $finalUri", )
            Toast.makeText(context,"Downloaded",Toast.LENGTH_SHORT).show()
            Log.e("TAG", "convertBitmapToPdf:${file.path} ", )

        } catch (e: IOException) {
            // below line is used
            // to handle error
            e.printStackTrace()
            Log.e("TAG", "convertBitmapToPdf: ${e.message}", )
            Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

        pdfDocument.close()


   //renderPdf(context, filePath)
     // downloadPdf(filePath, context)


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun downloadPdf(filePath: File, context: Context) {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun createPdf(
        context: Context,
        pdfData: PdfData,
        activity: Activity
    ) {
        val inflater = LayoutInflater.from(context)
       // val view = inflater.inflate(R.layout.status_dialog, null)
        val view=StatusDialogBinding.inflate(inflater)
        view.dissmiss.visibility=View.INVISIBLE
        view.downloadPdf.visibility=View.GONE
        view.sharePdf.visibility=View.GONE

        if (pdfData.type=="Bkash"){
            view.imageView.setImageDrawable(
                AppCompatResources.getDrawable(context,
                R.drawable.bkash_money_send_icon
            ))
        }
        else{
            view.imageView.setImageDrawable(
                AppCompatResources.getDrawable(context,
                R.drawable.ic_nagad
            ) )
        }

        setDataToView(view,pdfData)


        val bitmap = createBitmapFromView(context, view.root, activity)
        convertBitmapToPdf(bitmap, activity)
    }

    private fun setDataToView(view: StatusDialogBinding, pdfData: PdfData) {
        view.fundTransferTxt.text="${pdfData.type} Fund Transfer"
        view.amount.text=pdfData.amount.toDouble().toString()
        view.time.text=pdfData.transactionTime
        view.totalAmount.text="BDT ${pdfData.amount}"
        view.number.text=pdfData.number
        view.paymentTypeNumberTxt.text="${pdfData.type} number"
        view.narration.text=pdfData.narration
        view.location.text=pdfData.location

    }


    private fun renderPdf(context: Context, filePath: File) {
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            filePath
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "application/pdf")

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }


    }

    private fun copy( source:File, destination:File) {

        val ina =  FileInputStream(source).channel
        val out =  FileOutputStream(destination).channel

        try {
            ina.transferTo(0, ina.size(), out);
        } catch(e:Exception){
            // post to log
        } finally {
            ina?.close();
            out?.close();
        }
    }


    fun copyFileToDownloads(context: Context, downloadedFile: File): Uri? {

         val DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)


        val resolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.SIZE, downloadedFile.length())
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            val authority = "${context.packageName}.provider"
            val destinyFile = File(DOWNLOAD_DIR, "sadifjasidjf.pdf")
            FileProvider.getUriForFile(context, authority, destinyFile)
        }?.also { downloadedUri ->
            resolver.openOutputStream(downloadedUri).use { outputStream ->
                val brr = ByteArray(1024)
                var len: Int
                val bufferedInputStream = BufferedInputStream(FileInputStream(downloadedFile.absoluteFile))
                while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                    outputStream?.write(brr, 0, len)
                }
                outputStream?.flush()
                bufferedInputStream.close()
            }
        }
    }


}