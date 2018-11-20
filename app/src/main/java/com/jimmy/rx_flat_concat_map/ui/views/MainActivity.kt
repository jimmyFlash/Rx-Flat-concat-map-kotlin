package com.jimmy.rx_flat_concat_map.ui.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.jimmy.rx_flat_concat_map.R
import com.jimmy.rx_flat_concat_map.databinding.ActivityMainBinding
import com.jimmy.rx_flat_concat_map.viewmodels.MainActivityViewModel
import android.widget.TextView
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import com.jimmy.rx_flat_concat_map.data.models.Ticket
import com.jimmy.rx_flat_concat_map.ui.adapter.TicketAdapter


class MainActivity : AppCompatActivity(),  TicketAdapter.OnItemClickListener{

    override fun onItemClick(position: Int) {
        var jimmy = "sds"
    }

    lateinit var binding : ActivityMainBinding// binding to layout representation

    lateinit var viewModel : MainActivityViewModel// view model representer


    private val TAG = MainActivity::class.java.simpleName

    // instance of the recyclerview adapter
    private val mAdapter : TicketAdapter  = TicketAdapter(this, arrayListOf(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize binding to layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get the ViewModel
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        // set appbar title based on viewmodel observables
        binding.toolbar.title = "${viewModel.from} -> ${viewModel.to}"


        // bind the reyclervire and initialize layout manager
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(1, dpToPx(5), true))
        binding.recyclerView.adapter = mAdapter


        /*
         Evaluates the pending bindings, updating any Views that have expressions bound to modified
         variables. This must be run on the UI thread.
         */
        binding.executePendingBindings()


        // create observer for the ticket list from the viewmodel to update the adapter
        viewModel.ticketsLists.observe(this, Observer<List<Ticket>>{
            if (it != null) {
                // update the adapter collection with the list of tickets
                mAdapter.replaceData(it as ArrayList<Ticket>)
            }
        })

        // register observer for the single ticket returned fromm flatmap operation the viewmodel
        viewModel.ticket.observe(this, Observer {

            val position = mAdapter.ticketList.indexOf(it)

            if (position == -1) {
                // TODO - take action
                // Ticket not found in the list
                // This shouldn't happen
                return@Observer
            }

            mAdapter.ticketList[position] = it!!
            mAdapter.notifyItemChanged(position)

        })

        // register observer for the errors  adn then manage thee update  message returned from the calls to the server
        viewModel.erroMsg.observe(this, Observer {
            if (it != null) {
                showError(it)
            }
        })

        // handler with 2 sec delay to connect to service and load data
        Handler().postDelayed({
            viewModel.loadTicketData()
        }, 2000)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**
     * Snackbar shows observer error message
     */
    private fun showError(e: String) {
        Log.e(TAG, "showError: " + e)

        val snackbar = Snackbar.make(binding.coordinatorLayout, e, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        val textView : TextView = sbView.findViewById(android.support.design.R.id.snackbar_text)
        textView.setTextColor(Color.YELLOW)
        snackbar.show()
    }

    // helper method to convert dp into pixels
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.getDisplayMetrics()))
    }



}
