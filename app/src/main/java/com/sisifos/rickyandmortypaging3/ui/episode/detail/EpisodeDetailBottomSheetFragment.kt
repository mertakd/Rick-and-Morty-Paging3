package com.sisifos.rickyandmortypaging3.ui.episode.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentEpisodeDetailBottomSheetBinding
import com.sisifos.rickyandmortypaging3.ui.episode.detail.adapter.EpisodeDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EpisodeDetailBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEpisodeDetailBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EpisodeDetailViewModel by viewModels()
    private val safeArgs: EpisodeDetailBottomSheetFragmentArgs by navArgs()
    private val episodeDetailAdapter =  EpisodeDetailAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodeDetailBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.bottomSheetRecyclerView.adapter = episodeDetailAdapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.episodeFlow.collect { episode ->
                if (episode == null) {
                    // todo handler error
                    return@collect
                }

                binding.episodeNameTextView.text = episode.name
                binding.episodeAirDateTextView.text = episode.airDate
                binding.episodeNumberTextView.text = episode.getFormattedSeason()

                if (episode.characters.isNotEmpty()) {
                    val character = episode.characters
                    val pagingData = PagingData.from(character)
                    episodeDetailAdapter.submitData(pagingData)
                }
            }
        }

        viewModel.fetchEpisode(safeArgs.episodeId)
    }












    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}