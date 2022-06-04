package com.imadev.foody.ui.map

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "MapsFragment"
@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override val viewModel: MapsViewModel by activityViewModels()

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
        val rabat = com.google.android.gms.maps.model.LatLng(33.966304, -6.8549541)
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
                val latitude = rabatMarker.position.latitude
                val longitude = rabatMarker.position.longitude

                val addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )
                val address: String? = addresses[0].getAddressLine(0)
                val city: String? = addresses[0].locality
                val state: String? = addresses[0].adminArea
                val country: String? = addresses[0].countryName


                val myAddress = Address(city, state, country, address, LatLng(latitude, longitude))

                Log.d(TAG, "onMapReady: $myAddress")


                viewModel.address.value = myAddress

                findNavController().popBackStack()


            }
        }

    }


}