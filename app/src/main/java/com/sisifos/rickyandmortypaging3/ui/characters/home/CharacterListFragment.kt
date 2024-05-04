package com.sisifos.rickyandmortypaging3.ui.characters.home

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentCharacterListBinding
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.footer.CharactersLoadStateAdapter
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterListFragment : Fragment(R.layout.fragment_character_list) {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CharactersViewModel>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindState(
            uiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )

    }







    private fun FragmentCharacterListBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        uiActions: (UiAction) -> Unit
    ) {
        val charactersAdapter = CharactersPagingAdapter()

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            charactersAdapter = charactersAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }





    private fun FragmentCharacterListBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(searchRepo::setText)
        }





    }

    private fun FragmentCharacterListBinding.updateRepoListFromInput(
        onQueryChanged: (UiAction.Search) -> Unit,

    ) {
        searchRepo.text.trim().let { trimmedText ->
            if (trimmedText.isNotEmpty()) {
                list.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = trimmedText.toString()))
            } else {
                list.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = ""))
            }
        }
    }













    private fun FragmentCharacterListBinding.bindList(
        charactersAdapter: CharactersPagingAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        setupRecyclerView(charactersAdapter)
        characterItemClickListener(charactersAdapter)


        retryButton.setOnClickListener { charactersAdapter.retry() }
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })

        val notLoading = charactersAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for the paging source changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(charactersAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) list.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && charactersAdapter.itemCount == 0
                // show empty list
                emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds.
                list.isVisible = !isListEmpty
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

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

    private fun setupRecyclerView(charactersAdapter: CharactersPagingAdapter) {

        val footerAdapter = CharactersLoadStateAdapter{charactersAdapter.retry()}

        val gridLayoutManager = GridLayoutManager(requireContext(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == charactersAdapter.itemCount && footerAdapter.itemCount > 0) {
                        2 // Footer'ı kaplayacak şekilde iki sütun
                    } else {
                        when (charactersAdapter.getItemViewType(position)) {
                            CharactersPagingAdapter.CHARACTERS_LIST_ITEM_VIEW_TYPE -> 1
                            CharactersPagingAdapter.CHARACTERS_HEADER_ITEM_VIEW_TYPE -> 2
                            else -> throw IllegalArgumentException("Unknown view type at position $position")
                        }
                    }
                }
            }
        }

        with(binding.list) {
            layoutManager = gridLayoutManager
            adapter = charactersAdapter.withLoadStateFooter(footer = footerAdapter)
            setHasFixedSize(true)
            //addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }


    private fun characterItemClickListener(charactersAdapter: CharactersPagingAdapter){
        charactersAdapter.setOnCharacterItemClickListener { position ->
            val characterUiModel = charactersAdapter.getItemAtPosition(position) as? UiModel.CharacterItem
            characterUiModel?.let {
                val characterId = it.character.id
                val action = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(characterId)
                findNavController().navigate(action)
            }
        }
    }















    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}





















