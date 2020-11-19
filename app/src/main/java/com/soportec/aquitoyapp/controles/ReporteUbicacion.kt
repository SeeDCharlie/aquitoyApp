package com.soportec.aquitoyapp.controles

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class ReporteUbicacion(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var lat: Double? = null
    var long: Double? = null
    var ctx = appContext


    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.ctx)
        while (true) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    lat = location?.latitude
                    long = location?.longitude
                }

            println("coordenadas de ubicacions: $lat --- $long")

            Thread.sleep(1000 * 5)
        }

        return Result.success()
    }



}