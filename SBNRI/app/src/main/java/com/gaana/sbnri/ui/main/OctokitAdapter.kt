package com.gaana.sbnri.ui.main

import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gaana.sbnri.R
import com.gaana.sbnri.data.model.OctokitModel
import java.util.*
import kotlinx.android.synthetic.main.main_activity.*


class OctokitAdapter(var context: Activity, var octokitModelArrayList: ArrayList<OctokitModel>) :
    RecyclerView.Adapter<OctokitAdapter.OctokitViewHolder>() {

    override fun onBindViewHolder(holder: OctokitViewHolder, position: Int) {
        var octokitViewHolder = holder as OctokitViewHolder;
        var octokitModel: OctokitModel = octokitModelArrayList.get(position)
        octokitViewHolder.tx_open_issues_count?.text = octokitModel.open_issues_count.toString()
        octokitViewHolder.tx_name?.text = octokitModel.name
        octokitViewHolder.tx_description?.text = octokitModel.description

        if (!TextUtils.isEmpty(octokitModel.license?.name))
            octokitViewHolder.tx_license_name?.text = octokitModel.license?.name
        else
            octokitViewHolder.tx_license_name?.text = "Not Available"

        octokitViewHolder.tx_license_spdx?.text = octokitModel.license?.spdx_id

        var permission: String = ""
        if (octokitModel.permissions?.admin == true) permission += getPermissionString(
            permission,
            "Admin"
        )
        if (octokitModel.permissions?.pull == true) permission += getPermissionString(
            permission,
            "Pull"
        )
        if (octokitModel.permissions?.push == true) permission += getPermissionString(
            permission,
            "Push"
        )

        if (!TextUtils.isEmpty(permission))
            octokitViewHolder.tx_permissions?.setText(permission)
        else
            octokitViewHolder.tx_permissions?.setText("Not Available")
    }

    fun getPermissionString(permissionStr: String, permission: String): String {
        var permissionNewString: String = permissionStr
        if (TextUtils.isEmpty(permissionStr)) permissionNewString += permission
        else permissionNewString += ", "
        return permissionNewString
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OctokitViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.octokit_adapter_itemview, parent, false)
        return OctokitViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return octokitModelArrayList.size;
    }

    class OctokitViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tx_open_issues_count: TextView? = null
        var tx_description: TextView? = null
        var tx_name: TextView? = null
        var tx_license_name: TextView? = null
        var tx_license_spdx: TextView? = null
        var tx_permissions: TextView? = null

        init {
            tx_open_issues_count = view.findViewById(R.id.tx_open_issues_couns)
            tx_description = view.findViewById(R.id.tx_description)
            tx_name = view.findViewById(R.id.tx_name)
            tx_license_name = view.findViewById(R.id.tx_licence_name)
            tx_license_spdx = view.findViewById(R.id.tx_licence_spdx)
            tx_permissions = view.findViewById(R.id.tx_permissions)
        }
    }

}