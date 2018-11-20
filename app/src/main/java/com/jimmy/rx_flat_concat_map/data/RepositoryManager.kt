package com.jimmy.rx_flat_concat_map.data

import com.jimmy.rx_flat_concat_map.data.models.Ticket
import com.jimmy.rx_flat_concat_map.networking.NetManager
import io.reactivex.Observable

class RepositoryManager(
    val netManager: NetManager,
    val from: String,
    val to: String) {

    val remoteDataSource = RemoteDataSource()


    fun getTickets(): Observable<List<Ticket>> {

        netManager.isConnectedToInternet?.let {
            if (it) {
                return remoteDataSource.getTickets(from, to)
            }
        }

        return remoteDataSource.offlineData()
    }

    fun getTicketPrice (ticket: Ticket) : Observable<Ticket>{
        netManager.isConnectedToInternet?.let {
            if (it) {
                return remoteDataSource.getPriceObservable(ticket)
            }
        }
        return remoteDataSource.offlineTicketData()
    }
}