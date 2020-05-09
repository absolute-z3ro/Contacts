package xyz.absolutez3ro.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var contactPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isPermissionGranted())
            showPermissionDialog()
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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    contactPermission = true
                else {
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