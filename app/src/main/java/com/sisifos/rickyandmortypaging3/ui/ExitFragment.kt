package com.sisifos.rickyandmortypaging3.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sisifos.rickyandmortypaging3.databinding.FragmentExitBinding
import dagger.hilt.android.AndroidEntryPoint
import android.view.animation.AnimationUtils
import com.sisifos.rickyandmortypaging3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExitFragment : Fragment() {


    private var _binding: FragmentExitBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExitBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExitBinding.bind(view)

        startExitAnimation()
    }


    private fun startExitAnimation() {

        CoroutineScope(Main).launch {
            delay(10000)
            val portalAnimation = AnimationUtils.loadAnimation(context, R.anim.portal_animation)
            portalAnimation.duration = 25000 // 25 saniye
            portalAnimation.fillAfter = true
            binding.exitImageView.startAnimation(portalAnimation)
        }


        CoroutineScope(Main).launch {
            delay(20000)
            val rickAnimation = AnimationUtils.loadAnimation(context, R.anim.rick_animation)
            rickAnimation.duration = 3000
            rickAnimation.fillAfter = true
            binding.rickImageView.startAnimation(rickAnimation)
        }

        CoroutineScope(Main).launch {
            delay(20000)
            val mortyAnimation = AnimationUtils.loadAnimation(context, R.anim.morty_animation)
            mortyAnimation.duration = 3000
            mortyAnimation.fillAfter = true
            binding.mortyImageView.startAnimation(mortyAnimation)
        }


        playExitSound()


        CoroutineScope(Main).launch {
            delay(30000)
            requireActivity().finish()
        }
    }



    private fun playExitSound() {
        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.motive_full_edit)
        mediaPlayer.start()


        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}