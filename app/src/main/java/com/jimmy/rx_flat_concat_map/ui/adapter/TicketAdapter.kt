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

/**
 * adapter for the ticket recyclerview
 */
class TicketAdapter  (private var context: Context,
                      var ticketList: ArrayList<Ticket>,
                      private var listener: OnItemClickListener) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        // initialize data binding with the generated class from the ticket_row layout xml
        val binding = TicketRowBinding.inflate(layoutInflater, viewGroup, false)
        // initialize the ViewHolder
        return ViewHolder(context, binding)
    }


    // item count
    override fun getItemCount(): Int  = ticketList.size

    // establish data binding and click listener
    /**
     * items[position] is implementation of indexing operator. It is same as items.get(position)
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int){
        // calls the bind method of the VH to bind to the ui and update data
        viewHolder.bind(ticketList[pos], listener)
    }


    // interface for click handeling
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    // updated the tickets array
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

                binding.ticket = ticket

                /* root(kotlin) :: getRoot(java)
                   Returns the outermost View in the layout file associated with the Binding.
                   If this binding is for a merge layout file, this will return the first root in the merge tag.
                 */

                // can replace parameter with _ if you don’t use it
                //binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition) })

                if (ticket.price != null) {
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