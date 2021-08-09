package uz.instat.tasklist.presentation.adapters


import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import uz.instat.tasklist.busines.enums.TaskStatus
import uz.instat.tasklist.busines.local.TaskLocal
import uz.instat.tasklist.busines.util.date
import uz.instat.tasklist.busines.util.hour
import uz.instat.tasklist.busines.util.inflater
import uz.instat.tasklist.databinding.ItemTaskBinding
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapter(private val isAll: Boolean = true) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), Filterable {

    private val taskList: MutableList<TaskLocal> = mutableListOf()
    private val taskListFull: ArrayList<TaskLocal> = ArrayList(taskList)

    private var onTaskClickListener: OnTaskClickListener<TaskLocal>? = null
    fun onItemClickListener(onTaskClickListener: OnTaskClickListener<TaskLocal>) {
        this.onTaskClickListener = onTaskClickListener
    }

    fun submitList(taskList: List<TaskLocal>) {
        this.taskList.clear()
        this.taskList.addAll(taskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(parent.inflater(), parent, false),
            onTaskClickListener, isAll
        )
    }


    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            for (payload in payloads) {
                if (payload is TaskStatus) {
                    holder.binding.viewColorTag.setBackgroundColor(payload.color)
                    holder.binding.checkbox.isChecked = payload.isChecked
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemId(position: Int): Long {
        return taskList[position].id
    }

    class TaskViewHolder(
        val binding: ItemTaskBinding,
        private val onTaskClickListener: OnTaskClickListener<TaskLocal>?,
        private val isAll: Boolean = true
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var task: TaskLocal? = null

        fun bind(task: TaskLocal) {
            this.task = task
            with(binding) {
                checkbox.isVisible = isAll
                root.setOnClickListener(this@TaskViewHolder)
                viewColorTag.setBackgroundColor(task.status.color)
                checkbox.isChecked = task.status.isChecked
                tvTitle.text = task.title

                tvTime.text = task.time.hour
                tvDate.text = task.time.date
                tvAlarmTime.text = (task.time - task.alarmTime).hour

                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskClickListener?.onItemChecked(adapterPosition, task, isChecked)
                }
            }
        }

        override fun onClick(v: View) {
            onTaskClickListener?.onItemClicked(adapterPosition, task!!)
        }
    }

    override fun getFilter(): Filter {
        return taskFilter
    }

    fun addTask(task: TaskLocal, position: Int) {
        taskList.add(position, task)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }

    private val taskFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<TaskLocal> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(taskListFull)
            } else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in taskListFull) {
                    if (item.title.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            taskList.clear()
            taskList.addAll(results.values as Collection<TaskLocal>)
            notifyDataSetChanged()
        }
    }

}

interface OnTaskClickListener<T> {
    fun onItemClicked(position: Int, item: T)

    fun onItemChecked(position: Int, item: T, isChecked: Boolean)
}



