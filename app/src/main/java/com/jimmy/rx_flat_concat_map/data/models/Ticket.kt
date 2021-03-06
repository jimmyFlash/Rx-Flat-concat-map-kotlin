package com.jimmy.rx_flat_concat_map.data.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName


/**
 * model object to map the loaded ticket data from retorfit to
 */

data class Ticket (
    @SerializedName("flight_number")  @Bindable val flightNumber: String = "dummy-flight",
    @SerializedName("from")  @Bindable val from: String = "dummy-from",
    @SerializedName("to") @Bindable val to: String = "dummy-to",
    @SerializedName("price")  @Bindable var price: Price? = null,
    @SerializedName("stops") @Bindable val numberOfStops: Int = 0,
    @SerializedName("airline") @Bindable val airline: Airline? = null,
    @SerializedName("departure") @Bindable val departure: String = "dummy-departure",
    @SerializedName("arrival") @Bindable val arrival: String = "dummy-arrival",
    @SerializedName("duration") @Bindable val duration: String = "dummy-duration",
    @SerializedName("instructions") @Bindable val instructions: String = ""):BaseObservable(){

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }

        return if (other !is Ticket) {
            false
        } else flightNumber.equals(other.flightNumber, true)
    }


    override fun hashCode(): Int {
        var hash = 3
        hash = 53 * hash + if (this.flightNumber != null) this.flightNumber.hashCode() else 0
        return hash
    }


}