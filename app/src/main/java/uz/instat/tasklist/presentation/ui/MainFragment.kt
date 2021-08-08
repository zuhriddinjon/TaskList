package uz.instat.tasklist.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import uz.instat.tasklist.R
import uz.instat.tasklist.databinding.FragmentMainBinding
import uz.instat.tasklist.framework.viewmodels.MainViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentMainBinding? = null

    private lateinit var navController: NavController

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupViews()
        onBackPressed()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == R.id.navigation_all_tasks) {
                        isEnabled = false
                        activity?.onBackPressed()
                    } else {
                        navController.popBackStack()
                    }
                }
            })

    }

    private fun setupNavigation() {
        navController =
            (childFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment)
                .navController
        binding.bottomNav.setupWithNavController(navController)
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_all_tasks -> {
                    binding.toolbar.title = getString(R.string.title_all)
                    true
                }
                R.id.navigation_in_progress_tasks -> {
                    binding.toolbar.title = getString(R.string.title_in_progress)
                    true
                }
                R.id.navigation_performed_tasks -> {
                    binding.toolbar.title = getString(R.string.title_performed)
                    true
                }
                else -> false
            }
        }
    }


    private fun setupViews() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}