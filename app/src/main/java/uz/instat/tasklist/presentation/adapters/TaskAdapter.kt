package uz.instat.tasklist.presentation.adapters


import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import uz.instat.tasklist.R
import uz.instat.tasklist.busines.local.TaskLocal
import uz.instat.tasklist.busines.util.date
import uz.instat.tasklist.busines.util.getManualColor
import uz.instat.tasklist.busines.util.hour
import uz.instat.tasklist.busines.util.inflater
import uz.instat.tasklist.databinding.ItemTaskBinding
import java.util.*
import kotlin.collections.ArrayList


class TaskAdapter :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), Filterable {

    private val taskList: MutableList<TaskLocal> = mutableListOf()
    private val taskListFull: ArrayList<TaskLocal> = ArrayList(taskList)

    private var onItemClickListener: OnItemClickListener<TaskLocal>? = null
    fun onItemClickListener(onItemClickListener: OnItemClickListener<TaskLocal>) {
        this.onItemClickListener = onItemClickListener
    }

    fun submitList(taskList: List<TaskLocal>) {
        this.taskList.clear()
        this.taskList.addAll(taskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(parent.inflater(), parent, false),
            onItemClickListener
        )
    }


    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemId(position: Int): Long {
        return taskList[position].id
    }

    class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val onItemClickListener: OnItemClickListener<TaskLocal>?
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var task: TaskLocal? = null

        fun bind(task: TaskLocal) {
            this.task = task
            with(binding) {
                root.setOnClickListener(this@TaskViewHolder)
                val color =
                    when (task.status) {
                        0 -> {
                            getManualColor(R.color.blue)
                        }
                        1 -> {
                            getManualColor(R.color.green)
                        }
                        2 -> {
                            getManualColor(R.color.red)
                        }
                        else -> {
                            getManualColor(R.color.blue)
                        }
                    }
                viewColorTag.setBackgroundColor(color)
                tvTitle.text = task.title

                tvTime.text = task.time.hour
                tvDate.text = task.time.date
                tvAlarmTime.text = (task.time - task.alarmTime).hour
            }
        }

        override fun onClick(v: View) {
            onItemClickListener?.onItemClicked(adapterPosition, task!!)
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

interface OnItemClickListener<T> {
    fun onItemClicked(position: Int, item: T)
}



