package com.sisifos.rickyandmortypaging3.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentEpisodeListBinding
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersPagingAdapter
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.footer.CharactersLoadStateAdapter
import com.sisifos.rickyandmortypaging3.ui.episode.adapter.EpisodesPagingAdapter
import com.sisifos.rickyandmortypaging3.ui.episode.footer.EpisodeLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
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


        binding.retryButton.setOnClickListener { episodesAdapter.retry() }



        lifecycleScope.launch {
            episodesAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && episodesAdapter.itemCount == 0
                // show empty list
                binding.emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds.
                binding.episodeRecyclerView.isVisible = !isListEmpty
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }



    }













    private fun setupRecyclerView() {

        val footerAdapter = EpisodeLoadStateAdapter{episodesAdapter.retry()}



        binding.episodeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = episodesAdapter.withLoadStateFooter(footer = footerAdapter)
            setHasFixedSize(true)
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