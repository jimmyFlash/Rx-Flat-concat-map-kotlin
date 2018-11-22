package com.jimmy.rx_flat_concat_map.networking


import com.jimmy.rx_flat_concat_map.data.models.Price
import com.jimmy.rx_flat_concat_map.data.models.Ticket
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by jamal.safwat on 1/7/2018.
 *
 * Single Observable is used for the the endpoints, single response will be emitted
 *
 */

interface APIInterface {

    @GET(Constants.GET_TICKETS_ENDPOINT)
    fun searchTickets(@Query("from") from: String, @Query("to") to: String): Single<List<Ticket>>

    @GET(Constants.GET_SINGLE_TICKET_ENDPOINT)
    fun getPrice(@Query("flight_number") flightNumber: String, @Query("from") from: String,
                 @Query("to") to: String): Single<Price>

}
