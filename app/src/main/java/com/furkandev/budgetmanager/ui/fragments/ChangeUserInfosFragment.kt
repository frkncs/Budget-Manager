package com.furkandev.budgetmanager.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.database.userdatabase.UserDatabase
import com.furkandev.budgetmanager.databinding.FragmentChangeUserInfosBinding
import com.furkandev.budgetmanager.model.User
import com.yuvraj.livesmashbar.enums.BarStyle
import com.yuvraj.livesmashbar.enums.GravityView
import com.yuvraj.livesmashbar.view.LiveSmashBar

class ChangeUserInfosFragment : Fragment() {

    private lateinit var txtName: TextView
    private lateinit var btnSave: Button
    private lateinit var binding: FragmentChangeUserInfosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentChangeUserInfosBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables()
        onClickListeners()
    }

    private fun initializeVariables() {
        txtName = binding.txtName
        btnSave = binding.btnSave
    }

    private fun onClickListeners() {
        btnSave.setOnClickListener {

            if (canSaveValue()) {
                val userDatabase = UserDatabase.getInstance(requireContext().applicationContext)
                val userDao = userDatabase.userDatabaseDao

                val user = User()
                user.name = txtName.text.toString()
                user.gender = getSelectedGender()

                userDao.updateUser(user.name, user.gender)

                NavigateTo(
                    it,
                    ChangeUserInfosFragmentDirections.actionChangeUserInfosFragmentToMainFragment()
                )
            }
        }
    }

    private fun NavigateTo(view: View, action: NavDirections) {
        Navigation.findNavController(view).navigate(action)
    }

    private fun canSaveValue(): Boolean {
        var canSave = true

        if (txtName.text.isEmpty()) {

            txtName.error = getString(R.string.nameNull)

            canSave = false
        }
        else if (txtName.text.contains("-1")){
            txtName.error = getString(R.string.nameNotLikeThat)

            canSave = false
        }

        if (getSelectedGender() == (-1).toByte()) {

            LiveSmashBar.Builder(requireActivity())
                .icon(R.drawable.ic_baseline_error_24)
                .title(getString(R.string.error))
                .description(getString(R.string.genderNull))
                .backgroundColor(Color.RED)
                .setBarStyle(BarStyle.MESSAGE_ACTION_BUTTON)
                .gravity(GravityView.TOP)
                .duration(1000)
                .show()

            canSave = false
        }

        return canSave
    }

    private fun getSelectedGender(): Byte {

        if (binding.radioBtnMan.isChecked) {
            return 0
        } else if (binding.radioBtnWoman.isChecked) {
            return 1
        } else if (binding.radioBtnUnknown.isChecked) {
            return 2
        }

        return -1
    }
}