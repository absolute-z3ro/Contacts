package xyz.absolutez3ro.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.absolutez3ro.contacts.data.Contact

class MainActivity : AppCompatActivity() {

    private var contactPermission = false
    private lateinit var contactAdapter: ContactAdapter
    private var contactList = emptyList<Contact>()
    private lateinit var nothingToDisplay: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        nothingToDisplay = findViewById(R.id.text_no_result)

        if (!isPermissionGranted())
            showPermissionDialog()
        else {
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        nothingToDisplay.visibility = View.GONE
        contactAdapter = ContactAdapter(this)
        recyclerView.adapter = contactAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)

        setContacts()
    }

    private fun setContacts() {
        val contacts = contactsLoader()
        contactAdapter.setContactList(contacts)
    }

    private fun contactsLoader(): List<Contact> {
        val listOfContacts = mutableListOf<Contact>()
        val URI = ContactsContract.Contacts.CONTENT_URI
        val PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        val cursor = contentResolver.query(
            URI,
            PROJECTION,
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

    private fun isPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionDialog() {
        val alertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Contact Permission required!")
                setMessage("Contact permission is required to show contacts.")
                setPositiveButton(android.R.string.ok) { _, _ -> initiatePermission() }
                setOnDismissListener { }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun permissionDeniedDialog() {
        val alertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Contact Permission required!")
                setMessage("Cannot show contacts list without contacts read permission.")
                setPositiveButton(android.R.string.ok) { _, _ -> }
                setOnDismissListener { }
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun initiatePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactPermission = true
                    setupRecyclerView()
                } else {
                    contactPermission = false
                    permissionDeniedDialog()
                }
            }
            else -> {
                contactPermission = false
                permissionDeniedDialog()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 666
    }
}