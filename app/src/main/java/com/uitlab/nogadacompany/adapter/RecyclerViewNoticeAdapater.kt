package com.uitlab.nogadacompany.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.text.Html
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uitlab.nogadacompany.R
import com.uitlab.nogadacompany.noticedata.Notice

class RecyclerViewNoticeAdapater(private val context: Context) :
    RecyclerView.Adapter<RecyclerViewNoticeAdapater.ItemViewHolder>() {

    private val selectedItems = SparseBooleanArray()
    var listData = mutableListOf<Notice>()
    var prePositin:Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_item_expand_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    override fun getItemCount(): Int = listData.size

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val textTime = itemView.findViewById<TextView>(R.id.list_time_text)
        private val textTitle = itemView.findViewById<TextView>(R.id.list_title_text)
        private val textName = itemView.findViewById<TextView>(R.id.list_name_text)
        private val contents = itemView.findViewById<TextView>(R.id.notice_content_textview)

        fun bind(data: Notice, position: Int){
            textTime.text = data.ndate
            textTitle.text = data.ntitle
            textName.text = data.nwriter
            contents.text = Html.fromHtml(data.ncontent, Html.FROM_HTML_MODE_LEGACY).toString()

            changedVisibility(selectedItems.get(position));

            itemView.setOnClickListener {
                if (selectedItems.get(position)){
                    selectedItems.delete(position)
                }else{
                    selectedItems.delete(position)
                    selectedItems.put(position, true)
                }
                if(position != -1){
                    notifyItemChanged(prePositin)
                }
                notifyItemChanged(position)
                prePositin = position
            }

        }

        private fun changedVisibility(isExpanded: Boolean){
            var dpValue:Int = 110
            var d:Float = context.resources.displayMetrics.density
            var height:Int = (dpValue * d).toInt()
            var va: ValueAnimator

            if(isExpanded){
                va = ValueAnimator.ofInt(0, height)
            }else{
                va = ValueAnimator.ofInt(height, 0);
            }
            va.setDuration(600)
            va.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(animation: ValueAnimator?) {
                    val value = animation?.getAnimatedValue() as Int
                    contents.layoutParams.height = value;
                    contents.requestLayout()
                    if(isExpanded){
                        contents.visibility = View.VISIBLE
                    }else{
                        contents.visibility = View.GONE
                    }
                }
            })
            va.start()
        }
    }
}