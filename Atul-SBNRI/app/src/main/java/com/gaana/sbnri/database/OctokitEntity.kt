package com.gaana.sbnri.database

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "octokit")
data class OctokitEntity(

    @PrimaryKey
    @NonNull
    var id: String,
    var name: String,
    var description: String,
    var openIssuesCount: Integer,

    @Nullable
    var licenceKey: String?,
    @Nullable
    var licenseName: String?,
    @Nullable
    var licenceSpdxId: String?,

    @Nullable
    var pushPermission: Boolean?,

    @Nullable
    var pullPermission: Boolean?,

    @Nullable
    var adminPermission: Boolean?
)
