package com.jimmy.rx_flat_concat_map.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName

data class Airline (
    @SerializedName("id")  var id: Int = -1,
    @SerializedName("name")  @Bindable var name: String = "dummy-airline",
    @SerializedName("logo") @Bindable var logo: String = "dummy-logo"): BaseObservable()