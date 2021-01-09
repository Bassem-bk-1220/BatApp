package com.hb.batapp.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hb.batapp.R
import com.hb.batapp.utils.getDeviceLocation
import com.hb.batapp.utils.isGpsEnabled
import com.hb.batapp.utils.openDialog
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.PermissionManager.PermissionRequestListener
import com.intentfilter.androidpermissions.models.DeniedPermissions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Collections.singleton


class MapFragment : Fragment(), OnMapReadyCallback {


    private var googleMap: GoogleMap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getLocationPermission()
    }

    private fun getLocationPermission() {
        val permissionManager = PermissionManager.getInstance(context)
        permissionManager.checkPermissions(
            singleton(Manifest.permission.ACCESS_FINE_LOCATION),
            object : PermissionRequestListener {
                override fun onPermissionGranted() {
                    Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
                    if (isGpsEnabled(requireActivity())) {
                        GlobalScope.launch {
                            getDeviceLocation(requireActivity())
                        }
                    } else {
                        openDialog(
                            requireActivity(),
                            getString(R.string.open_gps),
                            getString(R.string.app_require_permission),
                            getString(R.string.ok),
                            getString(R.string.cancel),
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        )
                    }
                }

                override fun onPermissionDenied(deniedPermissions: DeniedPermissions) {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                    for (deniedPermission in deniedPermissions) {
                        if (deniedPermission.shouldShowRationale()) {
                            // Display a rationale about why this permission is required
                            openDialog(
                                requireActivity(),
                                getString(R.string.open_gps),
                                getString(R.string.app_require_permission),
                                getString(R.string.ok),
                                getString(R.string.cancel),
                                getLocationPermission()
                            )
                        }
                    }
                }
            })
    }


    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        val latitude = 35.8245
        val longitude = 10.6346
        val latlng = LatLng(latitude, longitude)
        val marker = with(MarkerOptions()) {
            position(latlng)
            icon(bitmapDescriptorFromVector(R.drawable.ic_baseline_location_on_24))
            //icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            title("Sousse - Tunisia")
            snippet("this is the capital")
        }
        googleMap?.addMarker(marker)
        moveCamera(latlng)
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }


    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}