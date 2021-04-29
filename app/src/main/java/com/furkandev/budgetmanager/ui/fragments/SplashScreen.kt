package com.furkandev.budgetmanager.ui.fragments

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.furkandev.budgetmanager.R

class SplashScreen : Fragment() {

    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables(view)
        setImageColorForDifferentThemes()
        startAnimation(view)
    }

    private fun initializeVariables(view: View){
        imageView = view.findViewById(R.id.imageView)
    }

    private fun setImageColorForDifferentThemes(){
        when(context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)){
            Configuration.UI_MODE_NIGHT_YES -> imageView.setColorFilter(R.color.white)
            Configuration.UI_MODE_NIGHT_NO -> imageView.setColorFilter(R.color.black)
        }
    }

    private fun startAnimation(view: View){
        val fadeInAnim = AnimationUtils.loadAnimation(requireContext().applicationContext,R.anim.fade_in)
        val fadeOutAnim = AnimationUtils.loadAnimation(requireContext().applicationContext,R.anim.fade_out)
        imageView.startAnimation(fadeInAnim)

        object : CountDownTimer(fadeInAnim.duration + 750,fadeInAnim.duration) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {

                imageView.startAnimation(fadeOutAnim)

                object: CountDownTimer(fadeOutAnim.duration, fadeOutAnim.duration){
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {

                        imageView.visibility = View.INVISIBLE

                        object:CountDownTimer(500,500){
                            override fun onTick(millisUntilFinished: Long) {

                            }

                            override fun onFinish() {
                                Navigation.findNavController(view).navigate(SplashScreenDirections.actionSplashScreenToOnBoardingScreen())
                            }

                        }.start()
                    }

                }.start()
            }

        }.start()
    }
}