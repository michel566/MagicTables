package com.michelbarbosa.magictables.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.michelbarbosa.magictables.databinding.FragmentHorizontalDataBinding
import com.michelbarbosa.magictables.model.MainData
import com.michelbarbosa.magictables.model.Ordenation
import com.michelbarbosa.magictables.ui.adapters.MainDataAdapter
import com.michelbarbosa.magictables.ui.events.MainEvent
import com.michelbarbosa.magictables.ui.states.MainUiState
import com.michelbarbosa.magictables.ui.viewmodel.MainViewModel
import com.michelbarbosa.magictables.utils.setCheckedOnSingleClickListener
import com.michelbarbosa.magictables.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HorizontalDataFragment : Fragment() {

    private lateinit var binding: FragmentHorizontalDataBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainDataAdapter: MainDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHorizontalDataBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWidgets()
        setupStates()

//        testMutableList()


    }

    private fun setupWidgets() {
        binding.btnBack.setCheckedOnSingleClickListener {
            findNavController().popBackStack()
        }
        binding.flAddNewList.setOnSingleClickListener {
            setupEvents(MainEvent.Start(Ordenation.HORIZONTAL))
        //            mainDataAdapter.updateAll(viewModel.startMutableList())

//            setupEvents(MainCreateEvent.MainCreateHorizontalEvent.Start(Ordenation.HORIZONTAL))

        }

        binding.btnAddMainHorizontalList.setOnSingleClickListener {
//            updateLastItemAdapter()
//                updateAdapter()

        }
    }

    private fun setupStates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { state ->
                        when (state) {
                            is MainUiState.Loading -> Unit

                            is MainUiState.Cleared,
                            is MainUiState.EmptyList,
                            is MainUiState.Initial -> showEmptyItemScreen(true)

                            is MainUiState.Created -> {
                                showEmptyItemScreen(false)
                                startAdapter(state.list)
                            }

                            is MainUiState.Updated -> updateList(state.list)
                        }
                    }
                }
            }
        }
    }

    private fun showEmptyItemScreen(visibility: Boolean) = with(binding) {
        flAddNewList.isVisible = visibility
        content.isVisible = !visibility
    }

    private fun updateList(newList: List<MainData>) {
        if (mainDataAdapter.list.isNotEmpty()) {
            mainDataAdapter.updateAll(newList)
        }
    }

    private fun setupEvents(event: MainEvent) {
        when (event) {
            is MainEvent.Add,
            is MainEvent.Remove,
            is MainEvent.Start -> viewModel.handleEvent(event)
        }
    }

    private fun startAdapter(list: List<MainData>) {
        mainDataAdapter = MainDataAdapter(list.toMutableList(),
            ::createItem, ::removeItem
        )
        with(binding.rvMainHorizontal) {
            scrollToPosition(0)
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mainDataAdapter

//            setItemTouchHelper(mainDataAdapter, list.toMutableList(),
//                onSwipeListener = { ad, isRemoved ->
//                    if (ad.itemCount == 0 || isRemoved)
//                        viewModel.handleHorizontalEvent(
//                            MainCreateEvent.MainCreateHorizontalEvent.Clear
//                        )
//                })
        }
    }

    private fun removeItem(item: MainData, position: Int) {
//        updateLastItemAdapter()
        setupEvents(MainEvent.Remove(position))
    }
    private fun createItem(mainData: MainData) {
        setupEvents(MainEvent.Add(mainData))
    }

    private fun updateLastItemAdapter(){
        if (mainDataAdapter.list.isNotEmpty())
            mainDataAdapter.updateWithLastItemViewHolder()
    }

    //----


//    private fun removeItem(item: MainData, position: Int) {
//        mainDataAdapter.updateWithLastItemViewHolder()
//        val dummy = mainDataAdapter.list.find { it.isDummy }
//        if (dummy != null){
//            mainDataAdapter.list.remove(dummy)
//        }
//
//        viewModel.updateMutableList(mainDataAdapter.list)
//
//        mainDataAdapter.removeItem(position, item)
//
//        viewModel.removeItem(position)
//        updateAdapter()
//
////        if (mainDataAdapter.itemCount == 0)
////            viewModel.handleHorizontalEvent(MainCreateEvent.MainCreateHorizontalEvent.UpdateList(null))
//    }
//
//    private fun createItem(mainData: MainData) {
//        mainDataAdapter.addItem(
//            mainData,
//            nextItemCallback = {
//                binding.rvMainHorizontal.smoothScrollToPosition(it)
//            }
//        )
//
//        viewModel.addItem(mainData)
//        updateAdapter()
//    }
//
//
//    private fun updateAdapter() {
////        viewModel.updateMutableList(mainDataAdapter.list)
//        mainDataAdapter.updateAll(viewModel.getMutableList())
//    }
//
//    private fun testMutableList() {
//        showEmptyItemScreen(true)
//        initAdapter(viewModel.startMutableList())
//    }

}