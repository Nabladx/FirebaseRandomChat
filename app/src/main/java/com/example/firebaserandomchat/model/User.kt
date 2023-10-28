package com.example.firebaserandomchat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val id: String
): Parcelable {
    constructor() : this("")
}