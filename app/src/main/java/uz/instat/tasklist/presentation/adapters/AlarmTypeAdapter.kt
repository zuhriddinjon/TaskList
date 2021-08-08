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

    private var onItemClickListener: OnItemClickListener<AlarmTypes>? = null
    fun onItemClickListener(onItemClickListener: OnItemClickListener<AlarmTypes>) {
        this.onItemClickListener = onItemClickListener
    }

    fun submitList(typeList: List<AlarmTypes>) {
        this.typeList.clear()
        this.typeList.addAll(typeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmTypeViewHolder {
        return AlarmTypeViewHolder(
            ItemBottomsheetBinding.inflate(parent.inflater(), parent, false),
            onItemClickListener
        )
    }


    override fun getItemCount() = typeList.size

    override fun onBindViewHolder(holder: AlarmTypeViewHolder, position: Int) {
        holder.bind(typeList[position])
    }

    class AlarmTypeViewHolder(
        private val binding: ItemBottomsheetBinding,
        private val onItemClickListener: OnItemClickListener<AlarmTypes>?
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
            onItemClickListener?.onItemClicked(adapterPosition, type!!)
        }
    }
}



