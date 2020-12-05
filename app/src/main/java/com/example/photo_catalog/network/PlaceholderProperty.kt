package com.example.photo_catalog.network

import com.squareup.moshi.Json
import java.util.*

data class PlaceholderProperty(
    val albumId: Int,
    val id: Int,
    val title: String,
    @Json(name = "url") val url: String,
    @Json(name = "thumbnailUrl") val thumbnailUrl: String
)