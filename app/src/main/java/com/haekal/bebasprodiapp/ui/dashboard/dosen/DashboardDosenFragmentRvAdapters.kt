package com.haekal.bebasprodiapp.ui.dashboard.dosen

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.haekal.bebasprodiapp.R
import com.haekal.bebasprodiapp.data.BebasProdi
import com.haekal.bebasprodiapp.utils.ekstension.loadFromUrl
import com.haekal.bebasprodiapp.utils.onClick
import kotlinx.android.synthetic.main.item_parcel.view.*

class DashboardDosenFragmentRvAdapters(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BebasProdi>() {

        override fun areItemsTheSame(oldItem: BebasProdi, newItem: BebasProdi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BebasProdi, newItem: BebasProdi): Boolean {
            return oldItem.id == newItem.id
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return DashboardSupirFragmentViewHolders(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_parcel,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DashboardSupirFragmentViewHolders -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<BebasProdi?>) {

        differ.submitList(list)
    }

    class DashboardSupirFragmentViewHolders
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BebasProdi) = with(itemView) {
            itemView.onClick {
                interaction?.onItemSelected(adapterPosition, item)
            }



            title_task.text = item.name
            desc_task.text = item.remark
            date_task.text = item.nim
            btn_status.text = item.createOn

            ic_task.loadFromUrl(item.urlPict?:"")
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BebasProdi)
    }
}
