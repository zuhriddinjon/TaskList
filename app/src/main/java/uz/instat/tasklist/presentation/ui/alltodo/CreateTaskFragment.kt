package uz.instat.tasklist.presentation.ui.alltodo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import uz.instat.tasklist.R
import uz.instat.tasklist.busines.enums.AlarmTypes
import uz.instat.tasklist.busines.enums.Errors.Companion.message
import uz.instat.tasklist.busines.interactors.UiState
import uz.instat.tasklist.busines.util.*
import uz.instat.tasklist.databinding.BottomsheetAlarmBinding
import uz.instat.tasklist.databinding.FragmentCreateTaskBinding
import uz.instat.tasklist.framework.viewmodels.MainViewModel
import uz.instat.tasklist.presentation.adapters.AlarmTypeAdapter
import uz.instat.tasklist.presentation.adapters.OnItemClickListener
import uz.instat.tasklist.presentation.ui.MainActivity
import java.util.*


@AndroidEntryPoint
class CreateTaskFragment : Fragment(), View.OnClickListener, OnItemClickListener<AlarmTypes> {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentCreateTaskBinding? = null
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        (activity as MainActivity).findNavController(R.id.nav_host_fragment_activity_main)
    }

    private val binding get() = _binding!!
    private val args: CreateTaskFragmentArgs by navArgs()
    private val typeAdapter: AlarmTypeAdapter by lazy(LazyThreadSafetyMode.NONE) { AlarmTypeAdapter() }


    private var calendar: Calendar = Calendar.getInstance()

    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private val cd = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (true) {
                    navController.popBackStack()
                } else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.task != null) {
            editViews()
        } else {
            setupViews()
        }
        setupObserver()
    }

    private fun editViews() {
        val task = args.task!!
        calendar.timeInMillis = task.time
        with(binding) {
            val d = Observable.combineLatest(
                etTitle.textChanges().skipInitialValue(),
                etNote.textChanges().skipInitialValue(),
                etDate.textChanges().skipInitialValue(),
                etTime.textChanges().skipInitialValue(),
                etAlarm.textChanges().skipInitialValue(),
                { title, description, date, time, alarm ->
                    if (title.isEmpty()) {
                        binding.etTitle.error = getString(R.string.error_req_field)
                    } else {
                        binding.etTitle.error = null
                    }
                    if (description.isEmpty()) {
                        binding.etNote.error = getString(R.string.error_req_field)
                    } else {
                        binding.etNote.error = null
                    }
                    if (date.isEmpty()) {
                        binding.etDate.error = getString(R.string.error_req_field)
                    } else {
                        binding.etDate.error = null
                    }
                    if (time.isEmpty()) {
                        binding.etTime.error = getString(R.string.error_req_field)
                    } else {
                        binding.etTime.error = null
                    }
                    title.toString() != task.title ||
                            description.toString() != task.description ||
                            date.toString() != task.time.date ||
                            time.toString() != task.time.hour ||
                            alarm.toString() != AlarmTypes.getByValue(task.alarmTime).title
                }
            ).doOnNext { btnSave.isEnabled = it }
                .subscribe()
            cd.add(d)

            timeInLay.visible()

            etTitle.setText(task.title)
            etNote.setText(task.description)
            etDate.setText(task.time.date)
            etTime.setText(task.time.hour)
            etAlarm.setText(AlarmTypes.getByValue(task.alarmTime).title)
            btnSave.text = getString(R.string.update)
            btnSave.disable()

            setOnClick()
        }
    }

    private val observerSaveTask = EventObserver<UiState<Long>> {
        when (it) {
            is UiState.Loading -> {
            }
            is UiState.Success -> {
                navController.popBackStack()
            }
            is UiState.Error -> {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val observerUpdateTask = EventObserver<UiState<Unit>> {
        when (it) {
            is UiState.Loading -> {
            }
            is UiState.Success -> {
                navController.popBackStack()
            }
            is UiState.Error -> {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupObserver() {
        viewModel.saveTaskState.observe(viewLifecycleOwner, observerSaveTask)
        viewModel.updateTaskState.observe(viewLifecycleOwner, observerUpdateTask)
    }

    private fun setupViews() = with(binding) {
        val d = Observable.combineLatest(
            etTitle.textChanges().skipInitialValue(),
            etNote.textChanges().skipInitialValue(),
            etDate.textChanges().skipInitialValue(),
            etTime.textChanges().skipInitialValue(),
            { title, description, date, time ->
                if (title.isEmpty()) {
                    binding.etTitle.error = getString(R.string.error_req_field)
                } else {
                    binding.etTitle.error = null
                }
                if (description.isEmpty()) {
                    binding.etNote.error = getString(R.string.error_req_field)
                } else {
                    binding.etNote.error = null
                }
                if (date.isEmpty()) {
                    binding.etDate.error = getString(R.string.error_req_field)
                } else {
                    binding.etDate.error = null
                }
                if (time.isEmpty()) {
                    binding.etTime.error = getString(R.string.error_req_field)
                } else {
                    binding.etTime.error = null
                }
                title.isNotEmpty() &&
                        description.isNotEmpty() &&
                        date.isNotEmpty() &&
                        time.isNotEmpty()
            }
        ).doOnNext { btnSave.isEnabled = it }
            .subscribe()
        cd.add(d)

        setOnClick()
    }

    private fun setOnClick() = with(binding) {
        etDate.setOnClickListener(this@CreateTaskFragment)
        etTime.setOnClickListener(this@CreateTaskFragment)
        etAlarm.setOnClickListener(this@CreateTaskFragment)
        btnSave.setOnClickListener(this@CreateTaskFragment)
        toolbar.setNavigationOnClickListener { navController.popBackStack() }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v?.id) {
                etDate.id -> {
                    setDateListener()
                }
                etTime.id -> {
                    setTimeListener()
                }
                etAlarm.id -> {
                    showAlarmBottomSheetDialog()
                }
                btnSave.id -> {
                    if (args.task == null) saveTask()
                    else updateTask()
                }
            }
        }
    }

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private fun showAlarmBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomBinding: BottomsheetAlarmBinding = BottomsheetAlarmBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomBinding.root)
        bottomBinding.rvAlarmType.adapter = typeAdapter
        val list = AlarmTypes.values()
        enumValues<AlarmTypes>()
        typeAdapter.submitList(list.toList())
        typeAdapter.onItemClickListener(this)
        bottomSheetDialog.show()

    }

    private fun saveTask() {
        hideKeyboard()
        val title = binding.etTitle.text.toString()
        val description = binding.etNote.text.toString()
        viewModel.saveTask(title, description, calendar.timeInMillis)
    }

    private fun updateTask() {
        hideKeyboard()
        val title = binding.etTitle.text.toString()
        val description = binding.etNote.text.toString()
        logi(calendar.timeInMillis)
        viewModel.updateTask(args.task!!, title, description, calendar.timeInMillis)
    }

    private fun setTimeListener() {
        hideKeyboard()
        timeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, min: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, min)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        binding.etTime.setText(calendar.hour)
    }

    private fun setDateListener() {
        hideKeyboard()
        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate =
            System.currentTimeMillis() + AlarmTypes.BEFORE_30.value
        datePickerDialog.show()
    }

    private fun updateDate() {
        binding.etDate.setText(calendar.date)
        if (!binding.timeInLay.isVisible) {
            setTimeListener()
            binding.timeInLay.visible()
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        _binding = null
        cd.clear()
        super.onDestroyView()
    }

    override fun onItemClicked(position: Int, item: AlarmTypes) {
        binding.etAlarm.setText(item.title)
        viewModel.alarmType = item
        bottomSheetDialog.dismiss()
    }

}