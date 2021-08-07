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
import dagger.hilt.android.AndroidEntryPoint
import uz.instat.tasklist.R
import uz.instat.tasklist.busines.enums.AlarmTypes
import uz.instat.tasklist.busines.enums.Errors.Companion.message
import uz.instat.tasklist.busines.interactors.UiState
import uz.instat.tasklist.busines.util.*
import uz.instat.tasklist.databinding.BottomsheetAlarmBinding
import uz.instat.tasklist.databinding.FragmentCreateTaskBinding
import uz.instat.tasklist.framework.viewmodels.MainViewModel
import uz.instat.tasklist.presentation.ui.MainActivity
import java.util.*


@AndroidEntryPoint
class CreateTaskFragment : Fragment(), View.OnClickListener {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentCreateTaskBinding? = null
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        (activity as MainActivity).findNavController(R.id.nav_host_fragment_activity_main)
    }

    private val binding get() = _binding!!
    private val args: CreateTaskFragmentArgs by navArgs()


    private var myCalendar: Calendar = Calendar.getInstance()

    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private var finalTime = 0L

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
            setupObserver()
        }
    }

    private fun editViews() {
        val task = args.task!!
        myCalendar.time.time = task.time
        with(binding) {
            etTitle.setText(task.title)
            etNote.setText(task.description)
            etDate.setText(task.time.date)
            etTime.setText(task.time.hour)
            etAlarm.setText(AlarmTypes.getByValue(task.alarmTime).title)
            btnSave.disable()

            etTitle.isFocusable = false
            etTitle.isClickable = false

            etNote.isFocusable = false
            etNote.isClickable = false

            etDate.isFocusable = false
            etDate.isClickable = false

            etTime.isFocusable = false
            etTime.isClickable = false

            etAlarm.isFocusable = false
            etAlarm.isClickable = false
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

    private fun setupObserver() {
        viewModel.saveTaskState.observe(viewLifecycleOwner, observerSaveTask)
    }

    private fun setupViews() = with(binding) {
        etDate.setOnClickListener(this@CreateTaskFragment)
        etTime.setOnClickListener(this@CreateTaskFragment)
        etAlarm.setOnClickListener(this@CreateTaskFragment)
        btnSave.setOnClickListener(this@CreateTaskFragment)
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
                    saveTask()
                }
            }
        }
    }

    private fun showAlarmBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomBinding: BottomsheetAlarmBinding = BottomsheetAlarmBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomBinding.root)

        bottomBinding.tv30Min.setOnClickListener {
            val alarmType = AlarmTypes.getByTitle(bottomBinding.tv30Min.text.toString())
            binding.etAlarm.setText(alarmType.title)
            viewModel.alarmType = alarmType
            bottomSheetDialog.dismiss()
        }

        bottomBinding.tv15Min.setOnClickListener {
            val alarmType = AlarmTypes.getByTitle(bottomBinding.tv15Min.text.toString())
            binding.etAlarm.setText(alarmType.title)
            viewModel.alarmType = alarmType
            bottomSheetDialog.dismiss()
        }

        bottomBinding.tv10Min.setOnClickListener {
            val alarmType = AlarmTypes.getByTitle(bottomBinding.tv10Min.text.toString())
            binding.etAlarm.setText(alarmType.title)
            viewModel.alarmType = alarmType
            bottomSheetDialog.dismiss()
        }

        bottomBinding.tv5Min.setOnClickListener {
            val alarmType = AlarmTypes.getByTitle(bottomBinding.tv5Min.text.toString())
            binding.etAlarm.setText(alarmType.title)
            viewModel.alarmType = alarmType
            bottomSheetDialog.dismiss()
        }

        bottomBinding.tv0Min.setOnClickListener {
            val alarmType = AlarmTypes.getByTitle(bottomBinding.tv0Min.text.toString())
            binding.etAlarm.setText(alarmType.title)
            viewModel.alarmType = alarmType
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()

    }

    private fun saveTask() {
        hideKeyboard()
        val title = binding.etTitle.text.toString()
        val description = binding.etNote.text.toString()
        val date = binding.etDate.text.toString()
        val time = binding.etTime.text.toString()

        var count = 0
        if (title.isEmpty()) {
            binding.etTitle.error = "Title required"
            count++
        }
        if (description.isEmpty()) {
            binding.etNote.error = "Note required"
            count++
        }
        if (date.isEmpty()) {
            binding.etDate.error = "Date required"
            count++
        }
        if (time.isEmpty()) {
            binding.etTime.error = "Time required"
            count++
        }
        if (count == 0) {
            viewModel.saveTask(title, description, finalTime)
        }
    }

    private fun setTimeListener() {
        hideKeyboard()
        timeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, min: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, min)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )

        timePickerDialog.show()

    }

    private fun updateTime() {
        finalTime = myCalendar.time.time
        binding.etTime.setText(myCalendar.hour)

    }

    private fun setDateListener() {
        hideKeyboard()

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate =
            System.currentTimeMillis() + AlarmTypes.BEFORE_30.value
        datePickerDialog.show()
    }

    private fun updateDate() {
        binding.etDate.setText(myCalendar.date)
        if (!binding.timeInLay.isVisible) {
            setTimeListener()
            binding.timeInLay.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        _binding = null
    }

}