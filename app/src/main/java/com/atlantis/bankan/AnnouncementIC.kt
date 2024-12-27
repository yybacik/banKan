package com.atlantis.bankan

import java.io.Serializable

data class AnnouncementIC (
    val city: String,
    val district: String,
    val hospital: String,
    val bloodType:String,
    val phoneNumber: String,
    val generalInfo: String
): Serializable