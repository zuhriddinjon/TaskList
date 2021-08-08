package uz.instat.tasklist.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.instat.tasklist.R
import uz.instat.tasklist.databinding.FragmentMainBinding
import uz.instat.tasklist.framework.viewmodels.MainViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val navSet = setOf(
        R.id.navigation_all_tasks,
        R.id.navigation_in_progress_tasks,
        R.id.navigation_performed_tasks
    )

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
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(navSet))
//        binding.bottomNav.setOnNavigationItemSelectedListener {
//            if (currentNav != it.itemId) {
//                openFragment(it.itemId)
//            }
//            return@setOnNavigationItemSelectedListener true
//        }
    }

    private var currentNav = R.id.navigation_all_tasks
    private fun openFragment(id: Int) {
        val isForward = navSet.indexOf(id) > navSet.indexOf(currentNav)
        navController.navigate(id, null, NavOptions.Builder().apply {
            setEnterAnim(if (isForward) R.anim.slide_in_right else R.anim.slide_in_left)
            setExitAnim(if (isForward) R.anim.slide_out_left else R.anim.slide_out_right)
            setPopEnterAnim(if (isForward) R.anim.slide_in_left else R.anim.slide_in_right)
            setPopExitAnim(if (isForward) R.anim.slide_out_right else R.anim.slide_out_left)
            setPopUpTo(id, true)
        }.build())
        currentNav = id
    }


    private fun setupViews() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}