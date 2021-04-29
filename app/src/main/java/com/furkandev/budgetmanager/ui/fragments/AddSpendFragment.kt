package com.furkandev.budgetmanager.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.database.spenddatabase.SpendDatabase
import com.furkandev.budgetmanager.databinding.FragmentAddSpendBinding
import com.furkandev.budgetmanager.model.Spend
import com.google.android.material.snackbar.Snackbar
import com.yuvraj.livesmashbar.enums.BarStyle
import com.yuvraj.livesmashbar.enums.GravityView
import com.yuvraj.livesmashbar.view.LiveSmashBar

class AddSpendFragment : Fragment() {

    private lateinit var binding: FragmentAddSpendBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddSpendBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickEvents()
    }

    fun onClickEvents() {
        binding.btnAddSpend.setOnClickListener {
            var checkedSpendType = 0
            var checkedCurrencyUnit = 0

            checkedSpendType = getCheckedSpendType()
            checkedCurrencyUnit = getCheckedCurrencyUnit()

            if (canSaveValue()) {
                val spend = Spend()
                spend.name = binding.txtDescription.text.toString()
                spend.price = binding.txtPrice.text.toString()
                spend.spendType = checkedSpendType
                spend.spendCurrencyUnit = checkedCurrencyUnit

                AddSpendToDatabase(it, spend)
            }
        }
    }

    private fun AddSpendToDatabase(view:View, spend: Spend) {
        val spendDatabase = SpendDatabase.getInstance(requireContext().applicationContext)
        val spendDatabaseDao = spendDatabase.spendDatabaseDao
        spendDatabaseDao.insertSpend(spend)

        ShowSnackbar(view)
        NavigateToMainFragment(view)
    }

    private fun ShowSnackbar(view: View) {
        val snack = Snackbar.make(view, R.string.newSpendAdded, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    private fun NavigateToMainFragment(view: View) {
        val action = AddSpendFragmentDirections.actionAddSpendFragmentToMainFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun getCheckedSpendType(): Int {
        if (binding.radioBtnBill.isChecked)
            return 0
        else if (binding.radioBtnHireSpend.isChecked)
            return 1
        else if (binding.radioBtnClothing.isChecked)
            return 2
        else if (binding.radioBtnOther.isChecked)
            return 3

        return -1
    }

    private fun getCheckedCurrencyUnit(): Int {
        if (binding.radioBtnTL.isChecked)
            return 0
        else if (binding.radioBtnUSD.isChecked)
            return 1
        else if (binding.radioBtnEURO.isChecked)
            return 2
        else if (binding.radioBtnSterling.isChecked)
            return 3

        return -1
    }

    private fun canSaveValue(): Boolean {
        var canSave = true

        if (binding.txtDescription.text.toString().isEmpty())
        {
            binding.txtDescription.error = getString(R.string.descriptionNull)
            canSave = false
        }
        if (binding.txtPrice.text.toString().isEmpty()){
            binding.txtPrice.error = getString(R.string.priceNull)
            canSave = false
        }

        if (getCheckedSpendType() == -1){
            LiveSmashBar.Builder(requireActivity())
                .icon(R.drawable.ic_baseline_error_24)
                .title(getString(R.string.error))
                .description(getString(R.string.spendNull))
                .backgroundColor(Color.RED)
                .setBarStyle(BarStyle.MESSAGE_ACTION_BUTTON)
                .gravity(GravityView.TOP)
                .duration(1000)
                .show()

            canSave = false
        }
        else if (getCheckedCurrencyUnit() == -1){
            LiveSmashBar.Builder(requireActivity())
                .icon(R.drawable.ic_baseline_error_24)
                .title(getString(R.string.error))
                .description(getString(R.string.currencyUnitNull))
                .backgroundColor(Color.RED)
                .setBarStyle(BarStyle.MESSAGE_ACTION_BUTTON)
                .gravity(GravityView.TOP)
                .duration(1000)
                .show()

            canSave = false
        }

        if (!canSave)
            return false

        return true
    }
}