package com.jimmy.rx_flat_concat_map.data

import com.jimmy.rx_flat_concat_map.data.models.Price
import com.jimmy.rx_flat_concat_map.data.models.Ticket
import com.jimmy.rx_flat_concat_map.networking.APIClient
import com.jimmy.rx_flat_concat_map.networking.APIInterface
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class RemoteDataSource {

    // initialize the retrofit client and bind it to the api interface for endpoint calls
    private val apiInterface: APIInterface = APIClient.getClient().create(APIInterface::class.java)


    /**
     * Making Retrofit call to fetch all ticket and passing it to observable
     */
     fun getTickets(from: String, to: String): Observable<List<Ticket>> {
        return apiInterface.searchTickets(from, to)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * utility method that simulates loading data from server and returns dummy data in case offline
     */
    fun offlineData() : Observable<List<Ticket>> {


        var arrayList = listOf(Ticket(price = Price()), Ticket(price = Price()))

//         Observable.just(), takes an item and creates Observable that emits that item
        return Observable.just(arrayList)
//               makes our Observables to wait 2 seconds before starting to emit the data
            .delay(2, TimeUnit.SECONDS)
    }


    /**
     * utility method that simulates loading data from server and returns dummy data in case offline
     */
    fun offlineTicketData() : Observable<Ticket> {
        val ticket = Ticket()
//         Observable.just(), takes an item and creates Observable that emits that item
        return Observable.just(ticket)
//               makes our Observables to wait 2 seconds before starting to emit the data
            .delay(2, TimeUnit.SECONDS)
    }
    /**
     * Making Retrofit call to get single ticket price
     * get price HTTP call returns Price object, but
     * map() operator is used to change the return type to Ticket with price object
     */
     fun getPriceObservable(ticket: Ticket): Observable<Ticket> {
        return apiInterface
            .getPrice(ticket.flightNumber, ticket.from, ticket.to)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it ->
                ticket.price = it
                return@map ticket
            }
    }


}