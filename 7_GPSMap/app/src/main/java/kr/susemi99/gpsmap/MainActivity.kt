package kr.susemi99.gpsmap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      var granted by remember { mutableStateOf(false) }
      val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = { granted = it })

      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        granted = true
      }

      if (granted) {
        val vm = viewModel<MainViewModel>()
        lifecycle.addObserver(vm)
        MyMap(vm)
      } else {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
          Text("권한을 허용해주세요.")
          Button(onClick = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
            Text("권한 요청")
          }
        }
      }
    }
  }
}

@Composable
fun MyMap(vm: MainViewModel) {
  val mapView = rememberMapView()
  val state = vm.state.value

  AndroidView(
    factory = { mapView },
    update = {
      mapView.getMapAsync { googleMap ->
        state.location?.let {
          val latLng = LatLng(it.latitude, it.longitude)
          googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
          googleMap.addPolyline(state.polylineOptions)
        }
      }
    })
}

@Composable
fun rememberMapView(): MapView {
  val context = LocalContext.current
  val mapView = remember { MapView(context) }
  val lifeCycleOwner = LocalLifecycleOwner.current

  DisposableEffect(lifeCycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_CREATE -> mapView.onCreate(bundleOf())
        Lifecycle.Event.ON_START -> mapView.onStart()
        Lifecycle.Event.ON_RESUME -> mapView.onResume()
        Lifecycle.Event.ON_PAUSE -> mapView.onPause()
        Lifecycle.Event.ON_STOP -> mapView.onStop()
        Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
        else -> throw IllegalStateException()
      }
    }

    lifeCycleOwner.lifecycle.addObserver(observer)

    onDispose {
      lifeCycleOwner.lifecycle.removeObserver(observer)
    }
  }
  return mapView
}

/**************************************************************************
 * View Model
 **************************************************************************/
class MainViewModel(application: Application) : AndroidViewModel(application), LifecycleEventObserver {
  private val fusedLocationProviderClient = FusedLocationProviderClient(application.applicationContext)
  private val locationRequest: LocationRequest
  private val locationCallback: MyLocationCallback

  private val _state = mutableStateOf(MapState(null, PolylineOptions().width(5f).color(Color.BLUE)))
  val state: State<MapState> = _state

  init {
    locationRequest = LocationRequest.create().apply {
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      interval = 10000
      fastestInterval = 5000
    }
    locationCallback = MyLocationCallback()
  }

  @SuppressLint("MissingPermission")
  private fun addLocationListener() {
    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
  }

  private fun removeLocationListener() {
    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
  }

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    if (event == Lifecycle.Event.ON_RESUME) {
      addLocationListener()
    } else if (event == Lifecycle.Event.ON_PAUSE) {
      removeLocationListener()
    }
  }

  inner class MyLocationCallback : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
      super.onLocationResult(locationResult)

      val location = locationResult.lastLocation
      val polylineOptions = state.value.polylineOptions

      _state.value = state.value.copy(
        location = location,
        polylineOptions = polylineOptions.add(LatLng(location.latitude, location.longitude)))
    }
  }
}

/**************************************************************************
 * Data Class
 **************************************************************************/
data class MapState(val location: Location?, val polylineOptions: PolylineOptions)