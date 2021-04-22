package ru.uomkri.tchktest.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.uomkri.tchktest.R
import ru.uomkri.tchktest.repo.net.BaseUserData

class UsersAdapter : PagingDataAdapter<BaseUserData, UsersAdapter.ViewHolder>(DiffUtilCallback) {

    companion object {
        private val DiffUtilCallback = object : DiffUtil.ItemCallback<BaseUserData>() {
            override fun areItemsTheSame(oldItem: BaseUserData, newItem: BaseUserData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BaseUserData, newItem: BaseUserData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.id.text = item.id.toString()
            holder.login.text = item.login
            holder.type.text = item.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val login: TextView = view.findViewById(R.id.user_login)
        val type: TextView = view.findViewById(R.id.user_type)
        val id: TextView = view.findViewById(R.id.user_id)
    }
}