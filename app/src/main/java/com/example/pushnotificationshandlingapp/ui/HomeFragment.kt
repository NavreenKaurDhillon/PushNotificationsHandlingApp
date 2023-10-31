package com.example.pushnotificationshandlingapp.ui

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pushnotificationshandlingapp.R
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    //if user allow or donot allow permission in popup it will be handled here
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications now as permissions is granted.
        } else {
            // permission is not granted inform user that he wont be able to see the notifications
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkNotificationsPermission()
    }

    private fun checkNotificationsPermission() {
        //for android version > 12 we need to request notifications permission manually
        // in version>=12 permissions is already granted by default
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(ContentValues.TAG, "askNotificationPermission: .. has permission")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.d(ContentValues.TAG, "askNotificationPermission: .. show rationale")
                Log.e(ContentValues.TAG, "Permissions Denied")
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    R.string.notification_rationale,
                    Snackbar.LENGTH_LONG)
                    .setAction(R.string.ok) {
                        // Request permission
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 102)
                    }.show()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}