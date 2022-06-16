package com.imadev.foody.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentMapsBinding
import com.imadev.foody.model.Address
import com.imadev.foody.model.LatLng
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import com.imadev.foody.utils.Constants.PERMISSIONS_REQUEST_LOCATION
import com.imadev.foody.utils.PermissionUtils
import com.imadev.foody.utils.hide
import com.imadev.foody.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "MapsFragment"

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {


    private var locationPermissionGranted: Boolean = false
    private lateinit var map: GoogleMap

    private var lastKnownLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override val viewModel: MapsViewModel by activityViewModels()

    private var defaultLocation = com.google.android.gms.maps.model.LatLng(33.966304, -6.8549541)


    companion object {
        private const val DEFAULT_ZOOM = 15

        @JvmStatic
        fun isPermissionGranted(
            grantPermissions: Array<String>, grantResults: IntArray,
            permission: String
        ): Boolean {
            for (i in grantPermissions.indices) {
                if (permission == grantPermissions[i]) {
                    return grantResults[i] == PackageManager.PERMISSION_GRANTED
                }
            }
            return false
        }
    }


    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMapsBinding = FragmentMapsBinding.inflate(layoutInflater, container, false)


    override fun onResume() {
        setToolbarTitle(activity as MainActivity)

        (activity as MainActivity).getToolbar().hide()

        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).getToolbar().show()

    }

    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.map)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocationPermission()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != PERMISSIONS_REQUEST_LOCATION) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            locationPermissionGranted = false
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableMyLocation()



    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            locationPermissionGranted = true
            setupMap()

            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                PERMISSIONS_REQUEST_LOCATION, true
            ).show(childFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSIONS_REQUEST_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        if (locationPermissionGranted) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true

        } else {
            map.isMyLocationEnabled = false
            map.uiSettings.isMyLocationButtonEnabled = false
            getLocationPermission()
        }

        map.clear()
        val defaultMarker = map.addMarker(MarkerOptions().position(defaultLocation).title(""))
        defaultMarker?.isDraggable = true

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12F))
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.apply {
            isZoomControlsEnabled = true
        }

        map.setOnCameraMoveListener {
            defaultMarker?.let {
                it.position = map.cameraPosition.target

            }

        }

        map.setMinZoomPreference(10F)


        view?.findViewById<Button>(R.id.choose_location)?.setOnClickListener {
            defaultMarker?.let {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val latitude = defaultMarker.position.latitude
                val longitude = defaultMarker.position.longitude

                val addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )
                val address: String? = addresses[0].getAddressLine(0)
                val city: String? = addresses[0].locality
                val state: String? = addresses[0].adminArea
                val country: String? = addresses[0].countryName


                val myAddress =
                    Address(city, state, country, address, LatLng(latitude, longitude))

                Log.d(TAG, "onMapReady: $myAddress")


                viewModel.address.value = myAddress

                findNavController().popBackStack()


            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(p0: Location) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            Log.d(
                                TAG, "getDeviceLocation: ${lastKnownLocation!!.latitude} ${
                                    lastKnownLocation!!.longitude
                                }"
                            )
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    com.google.android.gms.maps.model.LatLng(
                                        p0!!.latitude,
                                        p0!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }

                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            } else {
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        getDeviceLocation(p0)

    }



}