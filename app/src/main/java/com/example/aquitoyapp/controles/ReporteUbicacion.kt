package com.example.aquitoyapp.controles

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.work.Worker
import androidx.work.WorkerParameters


class ReporteUbicacion(appContext: Context, workerParams: WorkerParameters) : LocationListener,
    Worker(appContext, workerParams) {

    private var locationManager: LocationManager? = null
    var ctx = appContext
    var lat: Double? = null
    var long: Double? = null

    override fun doWork(): Result {


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