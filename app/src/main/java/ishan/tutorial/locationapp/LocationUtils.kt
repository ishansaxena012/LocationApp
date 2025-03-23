package ishan.tutorial.locationapp

import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

// Utility class for managing location-related functionality
class LocationUtils(val context: Context) {

    // FusedLocationProviderClient for retrieving location updates
    private val _fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Requests location updates and updates ViewModel with the latest location
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                // Retrieves the last known location and updates the ViewModel
                locationResult.lastLocation?.let {
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }

        // Configures location request with high accuracy and a 1-second update interval
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).build()

        // Starts requesting location updates
        _fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback, Looper.getMainLooper()
        )
    }

    // Checks if the app has location permissions
    fun hasLocationPermission(context: Context): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this.context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this.context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Returns true if both fine and coarse location permissions are granted
        return fineLocationPermission && coarseLocationPermission
    }

    // Converts latitude and longitude into a human-readable address using reverse geocoding
    fun reverseGeocodeLocation(location: LocationData): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinates = LatLng(location.latitude, location.longitude)

        // Gets the address from the given coordinates
        val addresses: MutableList<Address>? = geocoder.getFromLocation(
            location.latitude, location.longitude, 1
        )

        // Returns the first address found, or a default message if no address is found
        return if (addresses?.isNotEmpty() == true) {
            addresses[0].getAddressLine(0)
        } else {
            "ADDRESS NOT FOUND!"
        }
    }
}
