package com.furkandev.budgetmanager.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.database.spenddatabase.SpendDatabase
import com.furkandev.budgetmanager.databinding.FragmentSpendDetailBinding
import com.furkandev.budgetmanager.model.Spend
import com.google.android.material.snackbar.Snackbar

class SpendDetailFragment : Fragment() {

    private lateinit var binding : FragmentSpendDetailBinding
    private lateinit var btnDelete: Button

    private var clickedItemPos : Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSpendDetailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables()
        initializeInformations()
        onClickListener()
    }

    private fun initializeVariables(){
        btnDelete = binding.btnDeleteSpend
    }

    private fun initializeInformations(){
        arguments?.let {

            clickedItemPos = SpendDetailFragmentArgs.fromBundle(it).clickedItemPosition

            val imgIcon = binding.imgIcon
            val txtSpendName = binding.txtSpendName
            val txtSpendCost = binding.txtSpendCost

            when (getSpends()[clickedItemPos].spendType){
                0 -> imgIcon.setImageResource(R.drawable.bill)
                1 -> imgIcon.setImageResource(R.drawable.hire)
                2 -> imgIcon.setImageResource(R.drawable.clothes)
                3 -> imgIcon.setImageResource(R.drawable.other)
            }

            txtSpendName.text = getSpends()[clickedItemPos].name

            txtSpendCost.text = spendCost()
        }
    }

    private fun onClickListener(){
        btnDelete.setOnClickListener{
            val spendDao = SpendDatabase.getInstance(requireContext().applicationContext).spendDatabaseDao
            spendDao.deleteSpendWithID(getSpends()[clickedItemPos].spendId)

            Snackbar.make(it, R.string.spendDeleted, Snackbar.LENGTH_SHORT).show()

            Navigation.findNavController(it).navigate(SpendDetailFragmentDirections.actionSpendDetailFragmentToMainFragment())
        }
    }

    private fun getSpends() : List<Spend>
    {
        var spendList : List<Spend>

        val spendDatabase = SpendDatabase.getInstance(requireContext().applicationContext)
        val spendDatabaseDao = spendDatabase.spendDatabaseDao

        spendList = spendDatabaseDao.getAllSpends()

        return spendList
    }

    private fun spendCost() : String{
        val selectedCurrencyUnit: String = when (getSpends()[clickedItemPos].spendCurrencyUnit) {
            0 -> "TRY"
            1 -> "USD"
            2 -> "EUR"
            3 -> "GBP"
            else -> "TRY"
        }
        val sharedPref: SharedPreferences =
            requireContext().applicationContext.getSharedPreferences("com.furkandev.budgetmanager", Context.MODE_PRIVATE)

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
        ) * getSpends()[clickedItemPos].price.toFloat()).toString()

        if (priceText.split(".")[1].length > 1)
            priceText = priceText
                .split(".")[0] + "." + priceText
                .split(".")[1].substring(0, 2)
        else
            priceText = priceText
                .split(".")[0] + "." + priceText
                .split(".")[1].substring(0, 1)

        return priceText + " " + when(sharedPref.getInt("selectedCurrencyUnit",0)){
            0 -> "₺"
            1 -> "$"
            2 -> "€"
            3 -> "£"
            else -> ""
        }
    }
}