package com.sisifos.rickyandmortypaging3.ui.characters.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import com.sisifos.rickyandmortypaging3.MainNavGraphDirections
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentCharacterDetailBinding
import com.sisifos.rickyandmortypaging3.ui.characters.detail.adapter.EpisodeCarouselAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()
    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharacterDetailBinding.bind(view)

        binding.apply {
            carouselRecyclerView.adapter = episodesCarouselItemSelectedAdapter
            carouselRecyclerView.set3DItem(true)
            carouselRecyclerView.setAlpha(true)
            carouselRecyclerView.setInfinite(false)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.characterFlow.collect { character ->
                if (character == null) {
                    // todo handle error
                    return@collect
                }

                Picasso.get().load(character.image).into(binding.headerImageView)
                binding.nameTextView.text = character.name
                binding.aliveTextView.text = character.status
                binding.originTextDes.text = character.origin.name
                binding.speciesTextDes.text = character.species
                binding.genderTextDes.text = character.gender

                if (character.episodeList.isNotEmpty()) {
                    val episodes = character.episodeList
                    val pagingData = PagingData.from(episodes)
                    episodesCarouselItemSelectedAdapter.submitData(pagingData)
                }
            }
        }

        viewModel.fetchCharacter(args.characterId)
    }

    private val episodesCarouselItemSelectedAdapter = EpisodeCarouselAdapter().apply {
        setOnEpisodeItemClickListener { position ->
            val selectedCarouselEpisode = getItemAtPosition(position)
            selectedCarouselEpisode?.let {
                val episodeId = it
                episodeId.let { episodeClickedId ->
                    val action = MainNavGraphDirections.actionGlobalToEpisodeDetailBottomSheetFragment(
                        episodeId = episodeClickedId.id
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


