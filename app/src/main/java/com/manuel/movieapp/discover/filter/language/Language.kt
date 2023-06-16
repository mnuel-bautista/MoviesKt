package com.manuel.movieapp.discover.filter.language

import com.google.gson.annotations.SerializedName

data class Language(
    @SerializedName("iso_639_1") val iso: String,
    @SerializedName("english_name") val englishName: String,
    val name: String
)
