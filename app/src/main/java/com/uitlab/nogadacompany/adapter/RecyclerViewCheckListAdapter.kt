package com.uitlab.nogadacompany.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uitlab.nogadacompany.checklist.CheckDataApi
import com.uitlab.nogadacompany.checklist.ListOnclickInterface
import com.uitlab.nogadacompany.databinding.ChecklistItemBinding
import kotlinx.android.synthetic.main.checklist_item.view.*

class RecyclerViewCheckListAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerViewCheckListAdapter.ItemViewHolder>() {
    var listData = mutableListOf<CheckDataApi>()
    lateinit var binding: ChecklistItemBinding
    var checkListIsChecked:MutableMap<Int, Boolean> = mutableMapOf()
    lateinit var ActivitycallbackListener: ListOnclickInterface


    fun setCallbackListener(callbacks: ListOnclickInterface){
        ActivitycallbackListener = callbacks
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewCheckListAdapter.ItemViewHolder {
        binding = ChecklistItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(
        holder: RecyclerViewCheckListAdapter.ItemViewHolder,
        position: Int
    ) {
        holder.bind(listData[position], position, ActivitycallbackListener)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: CheckDataApi, position: Int, callback:ListOnclickInterface) {
            binding.listNumber.text = position.toString()
            binding.listContent.text = data.cList
            binding.checkListCBox.isChecked = false

            binding.checkListCBox.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    var pos = adapterPosition
                    if(pos != RecyclerView.NO_POSITION){
                        checkListIsChecked.put(pos,itemView.checkListCBox.isChecked)
                        callback.onCheckBox(checkListIsChecked)
                    }
                }
            })
        }
    }
}