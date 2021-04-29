package com.furkandev.budgetmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.model.OnboardingItem

class OnboardingItemAdapter(private val onboardingItems: List<OnboardingItem>)
    : RecyclerView.Adapter<OnboardingItemAdapter.OnboardingViewHolder>() {

    private var currentItemPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        currentItemPos = position
        holder.bind(onboardingItems[position])
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val onboardingView = view
        private val imgImage = view.findViewById<ImageView>(R.id.imgOnboardingImage)
        private val txtTitle = view.findViewById<TextView>(R.id.txtOnboaringTitle)
        private val txtDescription = view.findViewById<TextView>(R.id.txtOnboaringDescription)
        private val btnNext = view.findViewById<Button>(R.id.btnOnboardingNext)

        fun bind(onboardingItem: OnboardingItem){
            setImage(onboardingItem)
            setTitle(onboardingItem)
            setDescription(onboardingItem)
            changeButtonState()
        }

        private fun setImage(onboardingItem: OnboardingItem){
            if (onboardingItem.gifLink == null){
                imgImage.visibility = View.GONE
            }
            else{
                Glide.with(onboardingView)
                    .load("file:///android_asset/${onboardingItem.gifLink}.gif")
                    .into(imgImage)
            }
        }

        private fun setTitle(onboardingItem: OnboardingItem){
            if (onboardingItem.title == null){
                txtTitle.visibility = View.GONE
            }
            else{
                txtTitle.text = onboardingItem.title
            }
        }

        private fun setDescription(onboardingItem: OnboardingItem){
            if (onboardingItem.description == null){
                txtDescription.visibility = View.GONE
            }
            else{
                txtDescription.text = onboardingItem.description
            }
        }

        private fun changeButtonState(){
            if (currentItemPos == itemCount){
                btnNext.text = "Başla"
            }
        }
    }

}