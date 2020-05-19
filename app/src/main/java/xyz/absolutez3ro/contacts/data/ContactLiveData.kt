package xyz.absolutez3ro.contacts.data

import android.content.Context
import android.provider.ContactsContract

class ContactLiveData(private val context: Context) :
    ContentProviderLiveData<List<Contact>>(context, URI) {

    companion object {
        val URI = ContactsContract.Contacts.CONTENT_URI
    }

    private fun getContacts(context: Context): List<Contact> {
        val listOfContacts = mutableListOf<Contact>()
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(
            URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(0)
                val name = cursor.getString(1)

                listOfContacts.add(Contact(id, name))
            } while (cursor.moveToNext())

            cursor.close()
        }

        return listOfContacts
    }

    override fun getContentProviderValue() = getContacts(context)
}