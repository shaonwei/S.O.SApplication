package com.example.sosapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    val context: Context,
    val contactClickDeleteInterface: ContactClickDeleteInterface,
    val contactClickInterface: ContactClickInterface
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private val allContacts = ArrayList<Contact>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are creating an initializing all our
        // variables which we have added in layout file.
        val nameTV = itemView.findViewById<TextView>(R.id.tvContactName)
        val numberTV = itemView.findViewById<TextView>(R.id.tvContactNumber)
        val deleteBTN = itemView.findViewById<Button>(R.id.btn_delete_contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_contact,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTV.setText(allContacts.get(position).name)
        holder.numberTV.setText(allContacts.get(position).phoneNumber)
        // on below line we are adding click listener to our delete image view icon.
        holder.deleteBTN.setOnClickListener {
            // on below line we are calling a note click
            // interface and we are passing a position to it.
            contactClickDeleteInterface.onDeleteIconClick(allContacts.get(position))
        }

        // on below line we are adding click listener
        // to our recycler view item.
        holder.itemView.setOnClickListener {
            // on below line we are calling a note click interface
            // and we are passing a position to it.
            contactClickInterface.onEditClick(allContacts.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allContacts.size
    }

    fun updateList(newList: List<Contact>) {
        allContacts.clear()
        allContacts.addAll(newList)
        notifyDataSetChanged()
    }
}

interface ContactClickDeleteInterface {
    fun onDeleteIconClick(contact: Contact)
}

interface ContactClickInterface {
    fun onEditClick(contact: Contact)
}
