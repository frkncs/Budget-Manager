package com.furkandev.budgetmanager.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.furkandev.budgetmanager.R
import com.furkandev.budgetmanager.adapter.OnboardingItemAdapter
import com.furkandev.budgetmanager.model.OnboardingItem
import kotlinx.coroutines.flow.flowViaChannel

class OnBoardingScreen : Fragment() {

    private lateinit var onboardingItemAdapter: OnboardingItemAdapter
    private lateinit var indicatorsContainer: LinearLayout
    private lateinit var onboardingViewPager : ViewPager2
    private lateinit var btnOnboardingNext : Button
    private var currentPageIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showOnBoardingScreenAtFirstTime(view)
    }

    private fun initializeVariables(){
        onboardingViewPager = requireView().findViewById(R.id.onBoardingViewPager)
        btnOnboardingNext = requireView().findViewById(R.id.btnOnboardingNext)
    }

    private fun showOnBoardingScreenAtFirstTime(view: View) {
        if (checkUserOpenFirstTime()) {
            setOnBoardingItems()
            initializeVariables()
            setupIndicators()
            setCurrentIndicator(0)
            onClickListeners()
        }
        else{
            Navigation.findNavController(view).navigate(OnBoardingScreenDirections.actionOnBoardingScreenToMainFragment())
        }
    }

    private fun checkUserOpenFirstTime(): Boolean {

        val sharedPreferences = requireActivity().getSharedPreferences("com.furkandev.budgetmanager", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isOpenFirstTime",true)) {
            return true
        }

        return false
    }

    private fun setOnBoardingItems(){
        onboardingItemAdapter = OnboardingItemAdapter(
            listOf(
                OnboardingItem(
                    "gif1",
                    getString(R.string.onboarding_page1_title),
                    getString(R.string.onboarding_page1_description)
                ),
                OnboardingItem(
                    "gif2",
                    getString(R.string.onboarding_page2_title),
                    getString(R.string.onboarding_page2_description)
                ),
                OnboardingItem(
                    "gif3",
                    getString(R.string.onboarding_page3_title),
                    getString(R.string.onboarding_page3_description)
                ),
                OnboardingItem(
                    "gif4",
                    getString(R.string.onboarding_page4_title),
                    getString(R.string.onboarding_page4_description)
                ),
                OnboardingItem(
                    "gif5",
                    getString(R.string.onboarding_page5_title),
                    null
                ),
            )
        )
        val onboardingViewPager = view?.findViewById<ViewPager2>(R.id.onBoardingViewPager)
        onboardingViewPager?.adapter = onboardingItemAdapter

        onboardingViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                currentPageIndex = position
                changeButtonVisibility()
            }
        })
    }

    private fun setupIndicators(){
        indicatorsContainer = requireView().findViewById(R.id.onboardingIndicator)

        val indicators = arrayOfNulls<ImageView>(onboardingItemAdapter.itemCount)

        val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)

        for (i in indicators.indices){
            indicators[i] = ImageView(requireContext().applicationContext)
            indicators[i]?.let {
                it.setImageResource(R.drawable.indicator_inactive_background)
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int){

        val childCount = indicatorsContainer.childCount

        for (i in 0 until childCount){

            val imageView = indicatorsContainer.getChildAt(i) as ImageView

            if (i == position){
                imageView.setImageResource(R.drawable.indicator_active_background)
            }
            else{
                imageView.setImageResource(R.drawable.indicator_inactive_background)
            }
        }
    }

    private fun changeButtonVisibility(){
        if (currentPageIndex >= onboardingItemAdapter.itemCount - 1){
            btnOnboardingNext.visibility = View.VISIBLE
            btnOnboardingNext.text = getString(R.string.start)
        }
        else{
            btnOnboardingNext.visibility = View.INVISIBLE
        }
    }

    private fun onClickListeners(){
        btnOnboardingNext.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(OnBoardingScreenDirections.actionOnBoardingScreenToMainFragment())
        }
    }
}