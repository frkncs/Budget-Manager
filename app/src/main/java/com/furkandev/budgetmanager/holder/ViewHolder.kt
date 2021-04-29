package com.furkandev.budgetmanager.holder

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.model.Spend
import com.furkandev.budgetmanager.service.CurrencyConverterAPI
import com.furkandev.budgetmanager.ui.fragments.MainFragment
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewHolder(container: ViewGroup, val context: Context) : RecyclerView.ViewHolder(
    LayoutInflater.from(container.context).inflate(
        R.layout.row_item_list,
        container,
        false
    )
) {
    private val icon = itemView.findViewById<ImageView>(R.id.imgIcon)
    private val name = itemView.findViewById<TextView>(R.id.txtName)
    private val price = itemView.findViewById<TextView>(R.id.txtPrice)

    fun bind(spendsSpend: Spend) {

        when (spendsSpend.spendType) {
            0 -> icon.setImageResource(R.drawable.bill)
            1 -> icon.setImageResource(R.drawable.hire)
            2 -> icon.setImageResource(R.drawable.clothes)
            3 -> icon.setImageResource(R.drawable.other)
        }

        name.text = spendsSpend.name

        UpdateCurrencyUnitToSelected(spendsSpend)
    }

    @SuppressLint("SetTextI18n")
    private fun UpdateCurrencyUnitToSelected(spend: Spend) {

        val selectedCurrencyUnit: String = when (spend.spendCurrencyUnit) {
            0 -> "TRY"
            1 -> "USD"
            2 -> "EUR"
            3 -> "GBP"
            else -> "TRY"
        }
        val sharedPref: SharedPreferences =
            context.getSharedPreferences("com.furkandev.budgetmanager", Context.MODE_PRIVATE)

        val convertToCurrencyUnit = when (sharedPref.getInt("selectedCurrencyUnit", 0)) {
            0 -> "TRY"
            1 -> "USD"
            2 -> "EUR"
            3 -> "GBP"
            else -> "TRY"
        }

        var priceText = (sharedPref.getFloat(
            "${selectedCurrencyUnit}_${convertToCurrencyUnit}",
            0f
        ) * spend.price.toFloat()).toString()

        if (priceText.split(".")[1].length > 1)
            priceText = priceText
                .split(".")[0] + "." + priceText
                .split(".")[1].substring(0, 2)
        else
            priceText = priceText
                .split(".")[0] + "." + priceText
                .split(".")[1].substring(0, 1)

        price.text = priceText + " " + when(sharedPref.getInt("selectedCurrencyUnit",0)) {
            0 -> "₺"
            1 -> "$"
            2 -> "€"
            3 -> "£"
            else -> ""
        }

        MainFragment.convertedPrices.add(priceText.toDouble())

        var totalPrice = 0.0

        for (i in MainFragment.convertedPrices){
            totalPrice += i
        }

        var totalPriceText = totalPrice.toString()

        if (totalPriceText.split(".")[1].length > 1)
            totalPriceText = totalPriceText
                .split(".")[0] + "." + totalPriceText
                .split(".")[1].substring(0, 2)
        else
            totalPriceText = totalPriceText
                .split(".")[0] + "." + totalPriceText
                .split(".")[1].substring(0, 1)

        totalPriceText += when (sharedPref.getInt("selectedCurrencyUnit", 0)) {
            0 -> " ₺"
            1 -> " $"
            2 -> " €"
            3 -> " £"
            else -> ""
        }

        MainFragment.binding.txtTotalPrice.text = totalPriceText
    }
}