package com.jimmy.rx_flat_concat_map.data.models

import com.google.gson.annotations.SerializedName

data class Airline (
    @SerializedName("id")  var id: Int = -1,
    @SerializedName("name")  var name: String = "dummy-airline",
    @SerializedName("logo") var logo: String = "dummy-logo"

)