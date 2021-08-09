package uz.instat.tasklist.presentation.ui.inprogress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import uz.instat.tasklist.busines.interactors.UiState
import uz.instat.tasklist.busines.local.TaskLocal
import uz.instat.tasklist.databinding.FragmentInProgressTaskBinding
import uz.instat.tasklist.framework.viewmodels.MainViewModel
import uz.instat.tasklist.presentation.adapters.TaskAdapter

@AndroidEntryPoint
class InProgressFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentInProgressTaskBinding? = null
    private val binding get() = _binding!!
    private val tAdapter: TaskAdapter by lazy(LazyThreadSafetyMode.NONE) { TaskAdapter(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInProgressTasks()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInProgressTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObserver()
    }

    private fun setupViews() {
        binding.rvInProgressTasks.adapter = tAdapter
    }

    private val observerInProgress = Observer<UiState<List<TaskLocal>>> {
        when (it) {
            is UiState.Loading -> {
            }
            is UiState.Success -> {
                tAdapter.submitList(it.data ?: emptyList())
            }
            is UiState.Error -> {
            }
        }
    }

    private fun setupObserver() {
        viewModel.inProgressTasks.observe(viewLifecycleOwner, observerInProgress)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}