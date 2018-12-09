package com.jimmy.rx_flat_concat_map.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName

data class Price (
    @SerializedName("flight_number") @Bindable var flightNumber: String = "p-dummy-flight",
    @SerializedName("from")  @Bindable var from: String = "p-dummy-from",
    @SerializedName("to") @Bindable var to: String ="p-dummy-to",
    @SerializedName("price") @Bindable var price: Float = 0.55f,
    @SerializedName("seats") @Bindable var seats: String = "dummy-seats",
    @SerializedName("currency") @Bindable var currency: String ="dummy-currency"): BaseObservable()