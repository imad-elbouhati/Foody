package com.imadev.foody.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.imadev.foody.R
import com.imadev.foody.databinding.FragmentMapsBinding
import com.imadev.foody.ui.MainActivity
import com.imadev.foody.ui.common.BaseFragment

class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

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
        googleMap = p0

        val est = LatLng(34.05025443385189,-6.812236637979632 )

        googleMap.addMarker(MarkerOptions().position(est).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(est,16.0f))
    }


}