package com.furkandev.budgetmanager.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.adapter.Adapter
import com.furkandev.budgetmanager.database.spenddatabase.SpendDatabase
import com.furkandev.budgetmanager.database.userdatabase.UserDatabase
import com.furkandev.budgetmanager.databinding.FragmentMainBinding
import com.furkandev.budgetmanager.model.Spend
import com.furkandev.budgetmanager.service.CurrencyConverterAPI
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.internal.LinkedTreeMap
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import umairayub.madialog.MaDialog
import umairayub.madialog.MaDialogListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    private lateinit var fabAddSpend: FloatingActionButtonExpandable
    private lateinit var fabDeleteAllSpends: FloatingActionButtonExpandable
    private lateinit var spendsAdapter: Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardViewUserInformations: CardView
    private lateinit var txtName: TextView
    private lateinit var chipGroupCurrencyUnits: ChipGroup
    private lateinit var sharedPref: SharedPreferences
    private val SELECTED_CURRENCY_UNIT = "selectedCurrencyUnit"
    private val BASE_URL = "https://api.ratesapi.io/api/"

    companion object{
        lateinit var binding: FragmentMainBinding
        var convertedPrices : ArrayList<Double> = arrayListOf()
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(CurrencyConverterAPI::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentMainBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables(view)
        initializeHelloField()
        updateCurrencyUnitDatabase()
        onClickListeners()
    }

    fun initializeVariables(view: View) {

        // RecyclerView
        spendsAdapter = Adapter(getSpends(), requireContext().applicationContext)
        recyclerView = binding.recyclerView
        recyclerView.adapter = spendsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fabAddSpend.collapse()
                } else {
                    fabAddSpend.expand()
                }
            }
        })

        // FAB Buttons
        fabAddSpend = binding.fabAddSpend
        fabDeleteAllSpends = binding.fabDeleteSpends
        // LinearLayout
        cardViewUserInformations = binding.cardViewUserInformations
        // TextViews
        txtName = binding.txtName
        // ChipGroup
        chipGroupCurrencyUnits = binding.chipGroupCurrencyUnit
        // SharedPref
        sharedPref = requireActivity().getSharedPreferences(
            "com.furkandev.budgetmanager",
            Context.MODE_PRIVATE
        )

        sharedPref.edit().putBoolean("isOpenFirstTime",false).apply()

        // UpdateCurrencyUnit
        when (sharedPref.getInt(SELECTED_CURRENCY_UNIT, 0)) {
            0 -> updateCurrencyUnit(getString(R.string.tl))
            1 -> updateCurrencyUnit(getString(R.string.usd))
            2 -> updateCurrencyUnit(getString(R.string.euro))
            3 -> updateCurrencyUnit(getString(R.string.gbp))
        }
    }

    fun onClickListeners() {

        fabAddSpend.setOnClickListener {
            NavigateTo(it, MainFragmentDirections.actionMainFragmentToAddSpendFragment())
        }

        fabDeleteAllSpends.setOnClickListener {
            ShowDialogToDeleteAllSpends(it)
        }

        cardViewUserInformations.setOnClickListener {
            NavigateTo(it, MainFragmentDirections.actionMainFragmentToChangeUserInfosFragment())
        }

        chipGroupCurrencyUnits.setOnCheckedChangeListener { _, checkedId: Int ->
            val chip: Chip? = view?.findViewById(checkedId)

            chip?.let {
                updateCurrencyUnit(it.text.toString())
            }
        }

    }

    private var i = 0
    private var j = 0

    @SuppressLint("SimpleDateFormat")
    private fun updateCurrencyUnitDatabase() {

        val currencyUnitArray = arrayListOf("TRY", "USD", "EUR", "GBP")

        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val currentDate = sdf.format(Date())

        if (i >= currencyUnitArray.size - 1 && j >= currencyUnitArray.size - 1){
            sharedPref.edit().putString("currency_update_date", currentDate).apply()
        }

        if (sharedPref.getString("currency_update_date", "-1") == currentDate){
            return
        }

        if (i == j) {
            sharedPref.edit().putFloat(
                "${currencyUnitArray[i]}_${currencyUnitArray[j]}",
                1.0f
            ).apply()

            if (j < currencyUnitArray.size - 1) {
                j++
                updateCurrencyUnitDatabase()
            } else if (j >= currencyUnitArray.size - 1) {
                j = 0

                if (i < currencyUnitArray.size - 1) {
                    i++
                    updateCurrencyUnitDatabase()
                }
            }

            return
        }

        val call = service.getData(currencyUnitArray[i], currencyUnitArray[j])

        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {

                        for (currency in it) {
                            var linkedValue: LinkedTreeMap<String, Double>

                            if (currency.value is LinkedTreeMap<*, *>) {
                                linkedValue =
                                    currency.value as LinkedTreeMap<String, Double>

                                val itemPrice = linkedValue.get(currencyUnitArray[j])?.toDouble()

                                var stringItemPrice: String

                                if (itemPrice.toString().split(".")[1].length > 1)
                                    stringItemPrice = itemPrice.toString()
                                        .split(".")[0] + "." + itemPrice.toString()
                                        .split(".")[1].substring(0, 2)
                                else
                                    stringItemPrice = itemPrice.toString()
                                        .split(".")[0] + "." + itemPrice.toString()
                                        .split(".")[1].substring(0, 1)

                                sharedPref.edit().putFloat(
                                    "${currencyUnitArray[i]}_${currencyUnitArray[j]}",
                                    stringItemPrice.toFloat()
                                ).apply()

                                if (j < currencyUnitArray.size - 1) {
                                    j++
                                    updateCurrencyUnitDatabase()
                                } else if (j >= currencyUnitArray.size - 1) {
                                    j = 0

                                    if (i < currencyUnitArray.size - 1) {
                                        i++
                                        updateCurrencyUnitDatabase()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getSpends(): List<Spend> {
        var spendList: List<Spend>

        val spendDatabase = SpendDatabase.getInstance(requireContext().applicationContext)
        val spendDatabaseDao = spendDatabase.spendDatabaseDao

        spendList = spendDatabaseDao.getAllSpends()

        return spendList
    }

    private fun updateCurrencyUnit(chipText: String) {
        if (chipText == getString(R.string.tl)) {

            sharedPref.edit().putInt(SELECTED_CURRENCY_UNIT, 0).apply()
            if (!binding.chipSetTL.isChecked) binding.chipSetTL.isChecked = true

            RefreshScreenForCurUnits()

        } else if (chipText == getString(R.string.usd)) {

            sharedPref.edit().putInt(SELECTED_CURRENCY_UNIT, 1).apply()
            if (!binding.chipSetUSD.isChecked) binding.chipSetUSD.isChecked = true

            RefreshScreenForCurUnits()

        } else if (chipText == getString(R.string.euro)) {

            sharedPref.edit().putInt(SELECTED_CURRENCY_UNIT, 2).apply()
            if (!binding.chipSetEURO.isChecked) binding.chipSetEURO.isChecked = true

            RefreshScreenForCurUnits()

        } else if (chipText == getString(R.string.gbp)) {

            sharedPref.edit().putInt(SELECTED_CURRENCY_UNIT, 3).apply()
            if (!binding.chipSetSterling.isChecked) binding.chipSetSterling.isChecked = true

            RefreshScreenForCurUnits()

        }
    }

    private fun NavigateTo(view: View, action: NavDirections) {
        Navigation.findNavController(view).navigate(action)
    }

    private fun ShowDialogToDeleteAllSpends(view: View) {

        MaDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.youSureDeleteAll))
            .setPositiveButtonText(getString(R.string.yes))
            .setBackgroundColor(R.color.smooth_black)
            .setPositiveButtonListener(object : MaDialogListener {
                override fun onClick() {
                    if (SpendDatabase.getInstance(requireContext().applicationContext).spendDatabaseDao.getAllSpends()
                            .isNotEmpty()
                    ) {
                        DeleteAllSpends()
                        RefreshScreen(view)
                    }
                }
            })
            .build()
    }

    private fun DeleteAllSpends() {
        val spendDatabase = SpendDatabase.getInstance(requireContext().applicationContext)
        val spendDatabaseDao = spendDatabase.spendDatabaseDao
        spendDatabaseDao.deleteAllSpends()
    }

    private fun RefreshScreen(view: View) {
        NavigateTo(view, MainFragmentDirections.actionMainFragmentToAddSpendFragment())
        NavigateTo(view, AddSpendFragmentDirections.actionAddSpendFragmentToMainFragment())
    }

    private fun RefreshScreenForCurUnits() {
        spendsAdapter.notifyDataSetChanged()

        convertedPrices.clear()
    }

    private fun initializeHelloField() {
        val userDatabase = UserDatabase.getInstance(requireContext().applicationContext)
        val userDao = userDatabase.userDatabaseDao

        if (userDao.getUser()!!.name == "-1") {
            txtName.text = getString(R.string.welcome)
        } else {
            txtName.visibility = View.VISIBLE

            if (userDao.getUser()!!.gender == 0.toByte()) {
                // Man
                txtName.text = getString(R.string.resonateMan, userDao.getUser()!!.name)
            } else if (userDao.getUser()!!.gender == 1.toByte()) {
                // Woman
                txtName.text = getString(R.string.resonateWoman, userDao.getUser()!!.name)
            } else if (userDao.getUser()!!.gender == 2.toByte()) {
                // Unknown
                txtName.text = userDao.getUser()!!.name
            }
        }
    }
}