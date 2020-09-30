package com.walker.fluencytest.data.models.user


import com.google.gson.annotations.SerializedName

data class UserX(
    @SerializedName("accountType")
    val accountType: Int,
    @SerializedName("dateCreated")
    val dateCreated: Long,
    @SerializedName("dateUpdated")
    val dateUpdated: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("organizationId")
    val organizationId: Int,
    @SerializedName("publicName")
    val publicName: String
)