package com.sisifos.rickyandmortypaging3.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentCharacterListBinding
import com.sisifos.rickyandmortypaging3.databinding.FragmentEpisodeListBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.ui.episode.adapter.EpisodesPagingAdapter
import com.sisifos.rickyandmortypaging3.ui.characters.home.CharacterListFragmentDirections
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersPagingAdapter
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class EpisodeListFragment : Fragment(R.layout.fragment_episode_list) {

    private var _binding: FragmentEpisodeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EpisodesViewModel by viewModels()


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




        lifecycleScope.launch {
            viewModel.flow.collectLatest { episodePagingData: PagingData<EpisodesUiModel> ->
                episodesItemSelectedAdapter.submitData(episodePagingData)
            }
        }



        binding.episodeRecyclerView.apply {
            adapter = episodesItemSelectedAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }





    }





    var episodesItemSelectedAdapter = EpisodesPagingAdapter().apply {
        setOnEpisodeItemClickListener { position ->
            val selectedEpisode = getItemAtPosition(position) as? EpisodesUiModel.Item
            selectedEpisode?.let {
                val episodeId = it

                episodeId.let {
                    val action = EpisodeListFragmentDirections.actionEpisodeListFragmentToEpisodeDetailBottomSheetFragment(
                        episodeId = it.episode.id
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}