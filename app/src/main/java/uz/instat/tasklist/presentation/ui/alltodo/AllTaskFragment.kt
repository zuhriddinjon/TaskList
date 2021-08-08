package uz.instat.tasklist.presentation.ui.alltodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.instat.tasklist.R
import uz.instat.tasklist.busines.interactors.UiState
import uz.instat.tasklist.busines.local.TaskLocal
import uz.instat.tasklist.busines.util.*
import uz.instat.tasklist.databinding.FragmentAllTaskBinding
import uz.instat.tasklist.framework.viewmodels.MainViewModel
import uz.instat.tasklist.presentation.adapters.OnItemClickListener
import uz.instat.tasklist.presentation.adapters.TaskAdapter
import uz.instat.tasklist.presentation.listener.swipe.MySwipeCallback
import uz.instat.tasklist.presentation.ui.MainActivity
import uz.instat.tasklist.presentation.ui.MainFragmentDirections


@AndroidEntryPoint
class AllTaskFragment : Fragment(), View.OnClickListener, OnItemClickListener<TaskLocal> {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentAllTaskBinding? = null
    private val binding get() = _binding!!
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        (activity as MainActivity).findNavController(R.id.nav_host_fragment_activity_main)
    }
    private val tAdapter: TaskAdapter by lazy(LazyThreadSafetyMode.NONE) { TaskAdapter() }
    private val taskList: MutableList<TaskLocal> = mutableListOf()
    private var lastRemovedPos: Int = 0
    private var lastRemovedTask: TaskLocal? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObserver()
        viewModel.getAllTasks()
    }

    private fun setupViews() {
        with(binding) {
            btnAddTask.setOnClickListener(this@AllTaskFragment)
            val layoutManager = LinearLayoutManager(requireContext())
            rvAllTasks.layoutManager = layoutManager
            rvAllTasks.adapter = tAdapter

            tAdapter.onItemClickListener(this@AllTaskFragment)

            rvAllTasks.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val visibleItemCount: Int = layoutManager.childCount
                        val totalItemCount: Int = layoutManager.itemCount
                        val pastVisibleItems: Int = layoutManager.findFirstVisibleItemPosition()
                        if (pastVisibleItems + visibleItemCount >= totalItemCount && totalItemCount > visibleItemCount) {
                            btnAddTask.gone()
                        } else {
                            btnAddTask.visible()
                        }
                    }
                }
            )
            object : MySwipeCallback(requireContext(), binding.rvAllTasks) {
                override fun instantiateUnderlayButton(
                    viewHolder: RecyclerView.ViewHolder,
                    underlayButtons: MutableList<UnderlayButton>
                ) {
                    underlayButtons.add(UnderlayButton(
                        requireContext(),
                        R.drawable.ic_delete,
                        getManualColor(R.color.red)
                    ) { pos ->
                        lastRemovedPos = pos
                        val id = tAdapter.getItemId(pos)
                        taskList.map {
                            if (id == it.id)
                                lastRemovedTask = it
                        }
                        showSnackbar()
                    })
                }
            }


        }

    }

    private fun showSnackbar() {
        val snackbar =
            Snackbar.make(requireView(), getString(R.string.undo_item), Snackbar.LENGTH_SHORT)
        snackbar.setAction(getString(R.string.undo)) {
            tAdapter.addTask(lastRemovedTask!!, lastRemovedPos)
            lastRemovedTask = null
            snackbar.dismiss()
        }

        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                tAdapter.removeItem(lastRemovedPos)
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                // Snackbar closed on its own
                if (lastRemovedTask != null) {
                    viewModel.deleteTask(lastRemovedTask!!)
                }
            }
        })
        snackbar.show()
    }

    private val observerAllTask = Observer<UiState<List<TaskLocal>>> {
        when (it) {
            is UiState.Loading -> {
            }
            is UiState.Success -> {
                taskList.clear()
                taskList.addAll(it.data ?: emptyList())
                tAdapter.submitList(taskList)
            }
            is UiState.Error -> {
            }
        }
    }

    private val observerDeleteTask = EventObserver<UiState<Unit>> {
        when (it) {
            is UiState.Loading -> {
            }
            is UiState.Success -> {
            }
            is UiState.Error -> {
            }
        }
    }


    private fun setupObserver() {
        viewModel.allTasks.observe(viewLifecycleOwner, observerAllTask)
        viewModel.deleteTaskState.observe(viewLifecycleOwner, observerDeleteTask)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnAddTask.id -> {
                navController.navigate(MainFragmentDirections.actionMainToCreate())
            }
            else -> {
            }
        }
    }

    override fun onItemClicked(position: Int, item: TaskLocal) {
        navController.navigate(MainFragmentDirections.actionMainToCreate(item))
    }
}