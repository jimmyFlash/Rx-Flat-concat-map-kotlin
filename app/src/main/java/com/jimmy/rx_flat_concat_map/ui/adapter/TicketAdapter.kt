package com.jimmy.rx_flat_concat_map.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jimmy.rx_flat_concat_map.data.models.Ticket
import com.jimmy.rx_flat_concat_map.databinding.TicketRowBinding

class TicketAdapter  (private var context: Context,
                      var ticketList: ArrayList<Ticket>,
                      private var listener: OnItemClickListener) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val binding = TicketRowBinding.inflate(layoutInflater, p0, false)
        // initialize the ViewHolder
        return ViewHolder(context, binding)
    }


    override fun getItemCount(): Int  = ticketList.size

    // establish data binding and click listener
    /**
     * items[position] is implementation of indexing operator. It is same as items.get(position)
     */
    override fun onBindViewHolder(p0: ViewHolder, p1: Int){
        var ticket : Ticket = ticketList[p1]
        p0.bind(ticketList[p1], listener)
    }


    // interface for click handeling
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    fun replaceData(arrayList: ArrayList<Ticket>) {
        ticketList = arrayList
        notifyDataSetChanged()
    }


    // recyclyer view viewholder
    // RvItemRepositoryBinding : databinding library
    /*
     ViewHolder takes instance of RvItemRepositoryBinding type instead of View type
      so we can implement Data Binding in ViewHolder for each item
     */
    class ViewHolder(private var context : Context, private var binding: TicketRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket, listener: OnItemClickListener?) {

            if (listener != null) {
                /* root(kotlin) :: getRoot(java)
                   Returns the outermost View in the layout file associated with the Binding.
                   If this binding is for a merge layout file, this will return the first root in the merge tag.
                 */

                // can replace parameter with _ if you don’t use it
                //binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition) })

                Glide.with(context)
                    .load(ticket.airline?.logo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.logo)

                binding.airlineName.text = ticket.airline?.name
                binding.departure.text = "${ticket.departure}  Dep"
                binding.arrival.text = "${ticket.arrival}  Dest"


                binding.duration.text = ticket.flightNumber
                binding.duration.append(", " + ticket.duration)
                binding.numberOfSeats.text = "${ticket.numberOfStops}  Stops"


                if (!TextUtils.isEmpty(ticket.instructions)) {
                    binding.duration.append(", " + ticket.instructions)
                }

                if (ticket.price != null) {
                    binding.price.setText("₹" + String.format("%.0f", ticket.price!!.price));
                    binding.numberOfSeats.setText(ticket.price!!.seats + " Seats");
                    binding.progressBar.visibility = View.INVISIBLE
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                }


            }

            /*
            Evaluates the pending bindings, updating any Views that have expressions bound to modified
            variables. This must be run on the UI thread.
            */
            binding.executePendingBindings()
        }
    }
}