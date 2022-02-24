package com.example.simpletodolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RvAdapter(val userList: ArrayList<Model>, val listener: OnItemClick) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_item_layout, p0, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return userList.size
    }

    //將資料綁到對的位置上
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.title?.text = userList[p1].title
        p0.description?.text = userList[p1].description
        p0.date?.text = userList[p1].created + "~" +userList[p1].due
        p0.location?.text = userList[p1].location
        // 刪除資料
        p0.btn_delete.setOnClickListener {
            println("p1 :"+p1.toString())
            println("ID :"+userList[p1].id)
            removeItem(p1, userList[p1].id)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.txv_title)
        val description = itemView.findViewById<TextView>(R.id.txv_description)
        val date = itemView.findViewById<TextView>(R.id.txv_date)
        val location = itemView.findViewById<TextView>(R.id.txv_location)
        val btn_delete = itemView.findViewById<ImageButton>(R.id.btn_delete)
    }

    // 刪除項目
    fun removeItem(position: Int, ID: String) {
        userList.removeAt(position)
        notifyItemRemoved(position)
        // 加上這行才不會在從上往下刪的時候出問題
        notifyItemRangeChanged(position, getItemCount())
        listener?.onDelete(ID)
    }
}
