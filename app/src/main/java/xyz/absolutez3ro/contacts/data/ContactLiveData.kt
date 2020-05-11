package xyz.absolutez3ro.contacts.data

import android.content.Context

class ContactLiveData(private val context: Context) :
    ContentProviderLiveData<List<Contact>>(context, Repository.URI) {

    private val repository = Repository.getInstance()

    override fun getContentProviderValue() = repository.getContacts(context)
}