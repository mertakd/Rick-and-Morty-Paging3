package com.sisifos.rickyandmortypaging3.ui.characters.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentCharacterListBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersPagingAdapter
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CharacterListFragment : Fragment(R.layout.fragment_character_list) {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharacterListBinding.bind(view)




        lifecycleScope.launch() {
            viewModel.flow.collect { charactersPagingData: PagingData<CharactersUiModel> ->

                Log.d("PagingData", "Toplanan Paging Verisi: $charactersPagingData")
                itemSelectedAdapter.submitData(charactersPagingData)
            }
        }



        /*binding.charactersRecyclerView.apply {
            adapter = itemSelectedAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2)
        }*/

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (itemSelectedAdapter.getItemViewType(position)) {
                    CharactersPagingAdapter.CHARACTERS_LIST_ITEM_VIEW_TYPE -> 1 // Karakter öğesi, bir sütun genişliği
                    CharactersPagingAdapter.CHARACTERS_HEADER_ITEM_VIEW_TYPE -> 2 // Başlık öğesi, iki sütun genişliği
                    else -> throw IllegalArgumentException("Unknown view type at position $position")
                }
            }
        }

        binding.charactersRecyclerView.layoutManager = gridLayoutManager
        binding.charactersRecyclerView.adapter = itemSelectedAdapter


    }




    /*var itemSelectedAdapter = CharactersPagingAdapter().apply {
        setOnCharacterItemClickListener { position ->
            val characterId = (getItemViewType(position) as Character).id

            characterId.let {
                val action = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                    characterId = it
                )
                findNavController().navigate(action)
            }
        }
    }*/

    var itemSelectedAdapter = CharactersPagingAdapter().apply {
        setOnCharacterItemClickListener { position ->
            val charactersUiModel = getItemAtPosition(position) as? CharactersUiModel.Item
            charactersUiModel?.let {
                val characterId = it.character.id

                characterId.let {
                    val action = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                        characterId = it
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