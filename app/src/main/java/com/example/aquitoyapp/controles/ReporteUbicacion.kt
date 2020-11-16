package com.example.aquitoyapp.controles

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices


class ReporteUbicacion(appContext: Context, workerParams: WorkerParameters) : LocationListener,
    Worker(appContext, workerParams) {

    private var locationManager: LocationManager? = null
    var ctx = appContext
    var lat: Double? = null
    var long: Double? = null

    override fun doWork(): Result {
        var client = LocationServices.getFusedLocationProviderClient(ctx)
        if (ActivityCompat.checkSelfPermission(
                this.ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            client.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.lat = location.latitude
                    this.long = location.longitude
                } else {
                    this.lat = 0.0
                    this.long = 0.0
                }
            }
        }


        while (true) {
            println("mesaje en segundo plano: $lat --- $long")


            Thread.sleep(1000 * 5)
        }

        return Result.success()
    }


    override fun onLocationChanged(p0: Location) {
        lat = p0.latitude
        long = p0.longitude
    }

}