package com.imadev.foody.ui.map

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentMapsBinding
import com.imadev.foody.model.Address
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment
import java.util.*

private const val TAG = "MapsFragment"

class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override val viewModel: MapsViewModel by viewModels()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMapsBinding = FragmentMapsBinding.inflate(layoutInflater, container, false)


    override fun onResume() {
        setToolbarTitle(activity as MainActivity)
        super.onResume()
    }

    override fun setToolbarTitle(activity: MainActivity) {
        activity.setToolbarTitle(R.string.map)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0


        map.clear()
        val rabat = LatLng(33.966304, -6.8549541)
        val rabatMarker = map.addMarker(MarkerOptions().position(rabat).title(""))
        rabatMarker?.isDraggable = true

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(rabat, 12F))
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.apply {
            isZoomControlsEnabled = true
        }

        map.setOnCameraMoveListener {
            rabatMarker?.let {
                it.position = map.cameraPosition.target

            }

        }

        map.setMinZoomPreference(10F)

        view?.findViewById<Button>(R.id.choose_location)?.setOnClickListener {
            rabatMarker?.let {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(
                    rabatMarker.position.latitude,
                    rabatMarker.position.longitude,
                    1
                )
                val address: String? = addresses[0].getAddressLine(0)
                val city: String? = addresses[0].locality
                val state: String? = addresses[0].adminArea
                val country: String? = addresses[0].countryName
                val myAddress = Address(city, state, country, address)

                Log.d(TAG, "onMapReady: $myAddress")
                Toast.makeText(requireContext(), myAddress.toString(), Toast.LENGTH_SHORT).show()
            }
        }



    }


}