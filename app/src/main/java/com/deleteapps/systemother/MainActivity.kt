package com.deleteapps.systemother

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), MyOnItemClickListener {
    /* access modifiers changed from: private */
    var appInfo1s: MutableList<AppInformationGetSet?> = ArrayList()
    var doubleBackToExitPressedOnce = false

    /* access modifiers changed from: private */
    var app_found_count: TextView? = null
    var fram_lay: FrameLayout? = null
    private var isDeleteClick = false

    /* access modifiers changed from: private */
    var appListAdapter: AppListAdapter? = null

    /* access modifiers changed from: private */
    var listLayout: RelativeLayout? = null

    /* access modifiers changed from: private */
    var noappsfoundLayout: RelativeLayout? = null
    var main: RelativeLayout? = null

    /* access modifiers changed from: private */
    var progressBar: ProgressBar? = null
    private val recycler_view: RecyclerView? = null
    private var rescan_now: Button? = null
    var scan_other_app: Button? = null
    var scan_system_app: Button? = null
    var scan_ui: RelativeLayout? = null
    var i = 0

    internal inner class AppUninstalledreceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            apps
        }
    }

    inner class GetAppsAsync internal constructor() : AsyncTask<Void?, Void?, List<AppInformationGetSet?>>() {
        /* access modifiers changed from: protected */
        public override fun onPreExecute() {
            super.onPreExecute()
            progressBar!!.visibility = View.VISIBLE
            scan_ui!!.visibility = View.GONE
        }

        /* access modifiers changed from: protected */
        override fun doInBackground(vararg params: Void?): List<AppInformationGetSet?>? {
            return if (i == 1) isExist(installedOtherApps, appInfo1s) else isExist(installedSystemApps, appInfo1s)
        }

        /* access modifiers changed from: protected */
        public override fun onPostExecute(list: List<AppInformationGetSet?>) {
            super.onPostExecute(list)
            progressBar!!.visibility = View.GONE
            scan_ui!!.visibility = View.GONE
            fram_lay!!.visibility = View.GONE
            if (list.size > 0) {
                listLayout!!.visibility = View.VISIBLE
                noappsfoundLayout!!.visibility = View.GONE
                if (i == 1) app_found_count!!.text = Html.fromHtml(resources.getString(R.string.other_app_found_count, Integer.valueOf(list.size))) else app_found_count!!.text = Html.fromHtml(resources.getString(R.string.sys_app_found_count, Integer.valueOf(list.size)))
            } else {
                listLayout!!.visibility = View.GONE
                noappsfoundLayout!!.visibility = View.VISIBLE
            }
            appListAdapter!!.notifyDataSetChanged()
            if (list.size == 0) {
                Collections.sort(appInfo1s) { appInfo1, appInfo12 -> appInfo1?.appName!!.compareTo(appInfo12?.appName!!, ignoreCase = true) }
            }
        }
    }

    fun isExist(list: List<AppInformationGetSet?>?, list2: MutableList<AppInformationGetSet?>): List<AppInformationGetSet?> {
        list2.clear()
        list2.addAll(list!!)
        return list2
    }

    /* access modifiers changed from: protected */
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_main)
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        fram_lay = findViewById<View>(R.id.fram_lay) as FrameLayout
        main = findViewById(R.id.main)
        scan_other_app = findViewById<View>(R.id.scan_other_app) as Button
        scan_system_app = findViewById<View>(R.id.scan_system_app) as Button
        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        listLayout = findViewById<View>(R.id.listLayout) as RelativeLayout
        scan_ui = findViewById<View>(R.id.scan_ui) as RelativeLayout
        noappsfoundLayout = findViewById<View>(R.id.noappsfoundLayout) as RelativeLayout
        app_found_count = findViewById<View>(R.id.app_found_count) as TextView
        rescan_now = findViewById<View>(R.id.rescan_now) as Button
        appListAdapter = AppListAdapter(this@MainActivity, appInfo1s, this@MainActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = appListAdapter
        (findViewById<View>(R.id.scan_now_txt) as TextView).text = Html.fromHtml(resources.getString(R.string.clickscantext))
        scan_other_app!!.setOnClickListener {
            apps
            i = 1
        }
        scan_system_app!!.setOnClickListener {
            apps
            i = 2
        }
        rescan_now!!.setOnClickListener { apps }
    }

    /* access modifiers changed from: private */
    val apps: Unit
        get() {
            GetAppsAsync().execute(*arrayOfNulls<Void>(0))
        }

    /* access modifiers changed from: protected */
    public override fun onResume() {
        super.onResume()
        if (isDeleteClick) {
            Log.d("Resume__", "call __")
            Handler().postDelayed({ apps }, 3000)
        }
        isDeleteClick = false
    }

    override fun onItemClick(i: Int) {
        isDeleteClick = true
    }

    private fun isSystemPackage(packageInfo: PackageInfo): Boolean {
        return packageInfo.applicationInfo.flags and 1 != 0
    }

    /* access modifiers changed from: private */
    val installedOtherApps: List<AppInformationGetSet?>
        get() {
            val arrayList: ArrayList<AppInformationGetSet?> = ArrayList<AppInformationGetSet?>()
            val installedPackages: List<*> = packageManager.getInstalledPackages(0)
            for (i in installedPackages.indices) {
                val packageInfo = installedPackages[i] as PackageInfo
                if (!isSystemPackage(packageInfo)) {
                    val appInfo1 = AppInformationGetSet()
                    appInfo1.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    appInfo1.packageName = packageInfo.packageName
                    appInfo1.versionName = packageInfo.versionName
                    appInfo1.versionCode = packageInfo.versionCode
                    appInfo1.icon = packageInfo.applicationInfo.loadIcon(packageManager)
                    val length = File(packageInfo.applicationInfo.publicSourceDir).length() / 1048576
                    val sb = StringBuilder()
                    sb.append(length)
                    sb.append(" MB")
                    appInfo1.size = sb.toString()
                    arrayList.add(appInfo1)
                }
            }
            return arrayList
        }

    val installedSystemApps: List<AppInformationGetSet?>
        get() {
            val arrayList: ArrayList<AppInformationGetSet?> = ArrayList<AppInformationGetSet?>()
            val installedPackages: List<*> = packageManager.getInstalledPackages(0)
            for (i in installedPackages.indices) {
                val packageInfo = installedPackages[i] as PackageInfo
                if (isSystemPackage(packageInfo)) {
                    val appInfo1 = AppInformationGetSet()
                    appInfo1.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                    appInfo1.packageName = packageInfo.packageName
                    appInfo1.versionName = packageInfo.versionName
                    appInfo1.versionCode = packageInfo.versionCode
                    appInfo1.icon = packageInfo.applicationInfo.loadIcon(packageManager)
                    val length = File(packageInfo.applicationInfo.publicSourceDir).length() / 1048576
                    val sb = StringBuilder()
                    sb.append(length)
                    sb.append(" MB")
                    appInfo1.size = sb.toString()
                    arrayList.add(appInfo1)
                }
            }
            return arrayList
        }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
        } else {
            this.doubleBackToExitPressedOnce = true
            Snackbar.make(main!!, "Press once again to exit", Snackbar.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}