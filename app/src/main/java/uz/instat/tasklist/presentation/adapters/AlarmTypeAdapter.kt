package uz.instat.tasklist.presentation.adapters


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.instat.tasklist.busines.enums.AlarmTypes
import uz.instat.tasklist.busines.util.inflater
import uz.instat.tasklist.databinding.ItemBottomsheetBinding
import java.util.*


class AlarmTypeAdapter :
    RecyclerView.Adapter<AlarmTypeAdapter.AlarmTypeViewHolder>() {

    private val typeList: MutableList<AlarmTypes> = mutableListOf()

    private var onTaskClickListener: OnAlarmTypeClickListener<AlarmTypes>? = null
    fun onItemClickListener(onTaskClickListener: OnAlarmTypeClickListener<AlarmTypes>) {
        this.onTaskClickListener = onTaskClickListener
    }

    fun submitList(typeList: List<AlarmTypes>) {
        this.typeList.clear()
        this.typeList.addAll(typeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmTypeViewHolder {
        return AlarmTypeViewHolder(
            ItemBottomsheetBinding.inflate(parent.inflater(), parent, false),
            onTaskClickListener
        )
    }


    override fun getItemCount() = typeList.size

    override fun onBindViewHolder(holder: AlarmTypeViewHolder, position: Int) {
        holder.bind(typeList[position])
    }

    class AlarmTypeViewHolder(
        private val binding: ItemBottomsheetBinding,
        private val onTaskClickListener: OnAlarmTypeClickListener<AlarmTypes>?
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var type: AlarmTypes? = null

        fun bind(type: AlarmTypes) {
            this.type = type
            with(binding) {
                root.setOnClickListener(this@AlarmTypeViewHolder)
                tvMin.text = type.title
            }
        }

        override fun onClick(v: View) {
            onTaskClickListener?.onItemClicked(adapterPosition, type!!)
        }
    }
}

interface OnAlarmTypeClickListener<T> {
    fun onItemClicked(position: Int, item: T)
}



