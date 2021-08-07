package uz.instat.tasklist.presentation.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.instat.tasklist.R
import uz.instat.tasklist.databinding.FragmentSplashBinding
import uz.instat.tasklist.framework.viewmodels.MainViewModel
import uz.instat.tasklist.presentation.ui.MainActivity

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        (activity as MainActivity).findNavController(R.id.nav_host_fragment_activity_main)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            navController.navigate(SplashFragmentDirections.actionSplashToMain())
        }, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}