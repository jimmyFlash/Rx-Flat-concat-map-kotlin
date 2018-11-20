package com.jimmy.rx_flat_concat_map.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.jimmy.rx_flat_concat_map.data.RepositoryManager
import com.jimmy.rx_flat_concat_map.data.models.Ticket
import com.jimmy.rx_flat_concat_map.networking.NetManager
import com.jimmy.rx_flat_concat_map.utilities.extensions.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.logging.Handler


class MainActivityViewModel(application : Application) : AndroidViewModel(application) {

    private val TAG = MainActivityViewModel::class.java.simpleName

    // observable boolean for loading status
    private val isLoading = ObservableField(false)

    // mutable live data boolean to transmit the exception error message returned from service
    var erroMsg = MutableLiveData<String>()

    // observable string fields for the to and from
    val from =  ObservableField("DEL")
    val to =  ObservableField("HYD")

    // instance of the repository manager ( repository patterns implementation ) for loading tickets data
    var ticketRepoModel: RepositoryManager = RepositoryManager(NetManager(getApplication()), from.get()!!, to.get()!!)

    // from liveData class exposes set/post value for the list of returned tickets from call
    var ticketsLists = MutableLiveData<List<Ticket> >()

    // mutable LD for  an individual ticket
    var ticket = MutableLiveData<Ticket> ()


    //CompositeDisposable, a disposable container that can hold onto multiple other disposables
    private val compositeDisposable = CompositeDisposable()

    /* from RxJava
    A ConnectableObservable resembles an ordinary Observable, except that it does not begin emitting items when it
    is subscribed to, but only when its connect method is called. In this way you can wait for all intended Observers
    to Observable.subscribe to the Observable before the Observable begins emitting items.
     */
    lateinit var ticketsObservable : ConnectableObservable<List<Ticket>>

    // method to load tickets
    fun loadTicketData(){
        // set loading flag to true
        isLoading.set(true)

        /**from RxJava .replay()
         * Returns a ConnectableObservable that shares a single subscription to the underlying ObservableSource
         * that will replay all of its items and notifications to any future Observer.
         *
         *
         */
        ticketsObservable = ticketRepoModel.getTickets().replay()

        /**
         * Fetching all ticket first
         * Observable emits List<Ticket> at once
         * All the items will be added to RecyclerView
         * */
        compositeDisposable += ticketsObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object:  DisposableObserver<List<Ticket>>(){
                        override fun onComplete() {
                          //
                        }

                        override fun onNext(tickets: List<Ticket>) {
                            ticketsLists.value = tickets
                        }

                        override fun onError(e: Throwable) {
                            erroMsg.value = e.message.toString()
                        }

                    })


        /**
         * Fetching individual ticket price
         * First FlatMap converts single List<Ticket> to multiple emissions
         * Second FlatMap makes HTTP call on each Ticket emission
         * */
       compositeDisposable += ticketsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /**
             * Converting List<Ticket> emission to single Ticket emissions
             * */
            .flatMap {
               return@flatMap Observable.fromIterable(it)
            }

            /**
             * Fetching price on each Ticket emission
             * */
            .flatMap {
                return@flatMap ticketRepoModel.getTicketPrice(it)
            }

            .subscribeWith(object:  DisposableObserver<Ticket>(){
                override fun onComplete() {
                    //
                }

                override fun onNext(t: Ticket) {
                    ticket.value = t

                }

                override fun onError(e: Throwable) {
                    erroMsg.value = e.message.toString()
                }
            })

        ticketsObservable.connect()

    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     * It is useful when ViewModel observes some data and you need to clear this subscription
     * to prevent a leak of this ViewModel
     */
    override fun onCleared() {
        super.onCleared()

        /*
          when Activity is destroyed ViewModel’s onCleared() method will be called.
          In onCleared() method we should dispose all our Disposables.
       */
        if(!compositeDisposable.isDisposed){
            compositeDisposable.dispose()
        }
    }




}