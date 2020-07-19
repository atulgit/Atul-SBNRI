package com.gaana.sbnri.data.model

import java.io.Serializable

data class OctokitModel(var id: String, var open_issues_count: Integer, var license: License, var permissions: Permission?, var name: String, var description: String) : Serializable

data class License(var key:String, var name:String, var spdx_id:String) : Serializable

data class Permission(var admin: Boolean?, var push: Boolean?, var pull: Boolean?) : Serializable


