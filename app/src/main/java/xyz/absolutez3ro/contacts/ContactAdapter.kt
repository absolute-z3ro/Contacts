package xyz.absolutez3ro.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import xyz.absolutez3ro.contacts.data.Contact

class ContactAdapter(private val context: Context) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var contacts: List<Contact>? = emptyList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val image = view.findViewById<ImageView>(R.id.contact_image)

        fun bind(contact: Contact?, context: Context) {
            name.text = contact?.name
            Glide.with(context).load(R.drawable.ic_account_circle_24px).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts?.get(position)
        holder.bind(contact, context)
    }

    override fun getItemCount() = contacts?.size ?: 0

    fun setContactList(newContactList: List<Contact>?) {
        contacts = newContactList
        notifyDataSetChanged()
    }
}