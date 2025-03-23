package ishan.tutorial.locationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// ViewModel to manage and store location data
class LocationViewModel : ViewModel() {

    // Holds the current location state, initially null
    private val _location = mutableStateOf<LocationData?>(null)

    // Exposed immutable state for UI observation
    val location: State<LocationData?> = _location

    // Updates the location state with new data
    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }
}
