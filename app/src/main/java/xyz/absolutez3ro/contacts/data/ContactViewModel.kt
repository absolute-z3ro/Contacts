package xyz.absolutez3ro.contacts.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    val contacts = ContactLiveData(application.applicationContext)
}