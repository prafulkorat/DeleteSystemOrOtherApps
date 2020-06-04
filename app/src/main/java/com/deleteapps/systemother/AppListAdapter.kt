package com.deleteapps.systemother

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import java.util.*

class AppListAdapter(/* access modifiers changed from: private */
        var context: Context, list: List<AppInformationGetSet?>, onItemClickListener2: MyOnItemClickListener) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {
    var appInfo1s: List<AppInformationGetSet?> = ArrayList<AppInformationGetSet?>()

    /* access modifiers changed from: private */
    var onItemClickListener: MyOnItemClickListener
    private val viewPool = RecycledViewPool()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appsName: TextView
        var delete: ImageView
        var img: ImageView
        var size: TextView

        init {
            appsName = view.findViewById<View>(R.id.appsName) as TextView
            size = view.findViewById<View>(R.id.size) as TextView
            img = view.findViewById<View>(R.id.img) as ImageView
            delete = view.findViewById<View>(R.id.delete) as ImageView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val item = getItem(i)
        viewHolder.appsName.text = item!!.appName
        viewHolder.size.text = item.size
        viewHolder.img.setImageDrawable(item.icon)
        viewHolder.delete.setOnClickListener {
            val intent: Intent
            onItemClickListener.onItemClick(i)
            intent = if (VERSION.SDK_INT >= 14) {
                Intent("android.intent.action.UNINSTALL_PACKAGE")
            } else {
                Intent("android.intent.action.DELETE")
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.data = Uri.fromParts("package", item.packageName, null)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
    }

    fun getItem(i: Int): AppInformationGetSet? {
        return appInfo1s[i]
    }

    override fun getItemCount(): Int {
        return appInfo1s.size
    }

    init {
        appInfo1s = list
        onItemClickListener = onItemClickListener2
    }
}