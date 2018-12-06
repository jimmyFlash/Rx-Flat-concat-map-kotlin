package com.jimmy.rx_flat_concat_map.data

import com.jimmy.rx_flat_concat_map.data.models.Ticket
import com.jimmy.rx_flat_concat_map.networking.NetManager
import io.reactivex.Observable

class RepositoryManager(
    val netManager: NetManager,
    val from: String,
    val to: String) {

    // instance of the remote data class that connects online to retrieve data
    val remoteDataSource = RemoteDataSource()


    /**
     * retrieve al tickets data from server
     */
    fun getTickets(): Observable<List<Ticket>> {

        // check internet connection status
        netManager.isConnectedToInternet?.let {
            if (it) {
                return remoteDataSource.getTickets(from, to)
            }
        }

        // fallback to dummy offline data if connection to internet is not feasible (for demo purposes)
        // can also return empty ticket set from single observable and display message to user
        return remoteDataSource.offlineData()
    }

    /**
     * retrieve specific ticket data form server
     */
    fun getTicketPrice (ticket: Ticket) : Observable<Ticket>{
        netManager.isConnectedToInternet?.let {
            if (it) {
                return remoteDataSource.getPriceObservable(ticket)
            }
        }

        // fallback to dummy offline data if connection to internet is not feasible (for demo purposes)
        // can also return empty ticket set from single observable and display message to user
        return remoteDataSource.offlineTicketData()
    }
}