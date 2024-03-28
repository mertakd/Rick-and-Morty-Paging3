package com.sisifos.rickyandmortypaging3.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentEpisodeListBinding
import com.sisifos.rickyandmortypaging3.ui.episode.adapter.EpisodesPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EpisodeListFragment : Fragment(R.layout.fragment_episode_list) {

    private var _binding: FragmentEpisodeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EpisodesViewModel by viewModels()
    private val episodesAdapter by lazy { EpisodesPagingAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeListBinding.inflate(inflater, container, false)
        return binding.root
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEpisodeListBinding.bind(view)



        setupRecyclerView()
        observeViewModel()


    }





    private fun setupRecyclerView() {
        binding.episodeRecyclerView.apply {
            adapter = episodesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        episodesAdapter.setOnEpisodeItemClickListener { position ->
            val selectedEpisode = episodesAdapter.getItemAtPosition(position) as? EpisodesUiModel.Item
            selectedEpisode?.let {
                val action = EpisodeListFragmentDirections.actionEpisodeListFragmentToEpisodeDetailBottomSheetFragment(
                    episodeId = it.episode.id
                )
                findNavController().navigate(action)
            }
        }
    }





    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                episodesAdapter.submitData(state.episodeList ?: PagingData.empty())
            }
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}