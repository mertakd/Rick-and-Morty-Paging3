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
        // Portal animasyonu için bekleme
        CoroutineScope(Main).launch {
            delay(10000) // 20 saniye bekle
            val portalAnimation = AnimationUtils.loadAnimation(context, R.anim.portal_animation)
            portalAnimation.duration = 25000 // 25 saniye
            portalAnimation.fillAfter = true
            binding.exitImageView.startAnimation(portalAnimation)
        }

        // Rick animasyonu için bekleme
        CoroutineScope(Main).launch {
            delay(20000) // 20 saniye bekle
            val rickAnimation = AnimationUtils.loadAnimation(context, R.anim.rick_animation)
            rickAnimation.duration = 3000
            rickAnimation.fillAfter = true
            binding.rickImageView.startAnimation(rickAnimation)
        }

        CoroutineScope(Main).launch {
            delay(20000) // 20 saniye bekle
            val mortyAnimation = AnimationUtils.loadAnimation(context, R.anim.morty_animation)
            mortyAnimation.duration = 3000
            mortyAnimation.fillAfter = true
            binding.mortyImageView.startAnimation(mortyAnimation)
        }

        // Çıkış sesini çal
        playExitSound()

        // Fragment'in kapatılmasını ayarla
        CoroutineScope(Main).launch {
            delay(30000) // 50 saniye bekle
            requireActivity().finish()
        }
    }



    private fun playExitSound() {
        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.motive_full_edit)
        mediaPlayer.start()

        // Ses dosyasının çalması tamamlandığında veya durdurulduğunda, MediaPlayer'ı serbest bırak
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}