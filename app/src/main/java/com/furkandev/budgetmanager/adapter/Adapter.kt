package com.furkandev.budgetmanager.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.furkandev.budgetmanager.holder.ViewHolder
import com.furkandev.budgetmanager.model.Spend
import com.furkandev.budgetmanager.ui.fragments.MainFragment
import com.furkandev.budgetmanager.ui.fragments.MainFragmentDirections

class Adapter (val spendList: List<Spend>, val context: Context): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent, context)
    }

    override fun getItemCount(): Int {
        return spendList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(spendList[position])

        holder.itemView.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToSpendDetailFragment(position)
            Navigation.findNavController(it).navigate(action)
        }
    }
}