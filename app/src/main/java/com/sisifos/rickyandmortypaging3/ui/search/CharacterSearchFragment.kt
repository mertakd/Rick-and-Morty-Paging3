package com.sisifos.rickyandmortypaging3.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.google.android.material.snackbar.Snackbar
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.FragmentCharacterListBinding
import com.sisifos.rickyandmortypaging3.databinding.FragmentCharacterSearchBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersUiModel
import com.sisifos.rickyandmortypaging3.ui.search.adapter.CharacterSearchAdapter
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CharacterSearchFragment : Fragment() {

    private var _binding: FragmentCharacterSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterSearchViewModel by viewModels()


    private var currentText = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        viewModel.submitQuery(currentText)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ObsoleteCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharacterSearchBinding.bind(view)



        binding.searchRecyclerView.adapter = searchCharacterAdapter



        binding.searchEditText.doAfterTextChanged {
            currentText = it?.toString() ?: ""

            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, 500L)
        }


        lifecycleScope.launch {
            viewModel.flow.collectLatest { characterSearchPagingData: PagingData<Character> ->
                searchCharacterAdapter.submitData(characterSearchPagingData)
            }
        }




    }





    val searchCharacterAdapter = CharacterSearchAdapter().apply {
        setOnCharacterSearchClickListener{ position ->
            val charactersSearchItemClick = getItemAtPosition(position)
            charactersSearchItemClick?.let {
                Snackbar.make(requireView(), "Seçilen karakter: ${it.name}", Snackbar.LENGTH_SHORT).show()
            }


        }
    }














    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}