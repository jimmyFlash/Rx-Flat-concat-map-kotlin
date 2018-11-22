package com.jimmy.rx_flat_concat_map.data.models

import com.google.gson.annotations.SerializedName

data class Price (
    @SerializedName("flight_number")  var flightNumber: String = "p-dummy-flight",
    @SerializedName("from")  var from: String = "p-dummy-from",
    @SerializedName("to") var to: String ="p-dummy-to",
    @SerializedName("price") var price: Float = 0.55f,
    @SerializedName("seats") var seats: String = "dummy-seats",
    @SerializedName("currency") var currency: String ="dummy-currency"

)