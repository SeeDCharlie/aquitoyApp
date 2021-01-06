package com.soportec.aquitoyapp.controles

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.soportec.aquitoyapp.MainActivity
import com.soportec.aquitoyapp.R
import com.soportec.aquitoyapp.modelos.VariablesConf
import com.soportec.aquitoyapp.modelos.apiInterfaz
import org.json.JSONObject


@Suppress("DEPRECATION")
class NotificationsManager(
    context: Context, workerParams: WorkerParameters
) : Worker(context, workerParams), apiInterfaz {


    override var baseUrl: String = VariablesConf.BASE_URL_API
    override var requestExecute: RequestQueue? = Volley.newRequestQueue(context)
    var motorDb = ControlSql(context)
    val context = context

    lateinit var notificationManager: NotificationManager
    lateinit var channel:NotificationChannel

    var idNo = -1

    init {
        createNotificationChannel()
        println("canal de notificaciones creado")
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = VariablesConf.CHANNEL_ONE_NOTY
            val descriptionText = "chanel descrip"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(VariablesConf.CHANNEL_ONE_NOTY, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun doWork(): Result {
        try {
            var cont = 0
            val queryOne = "select id from notifys order by id desc limit 1;"
            val queryTwo = "select id_user from sesiones where activo = 1 order by id desc limit 1;"
            val idUsr = motorDb.select(queryTwo)


            if( idUsr != null){
                val idNoty = motorDb.select(queryOne)
                var idU = idUsr.get(0).getInt("id_user")
                if(idNoty != null){
                    idNo = idNoty.get(0).getInt("id")
                }
                while(true){
                    println("consultando notificaciones !!!")
                    getNotify(idNo, idU)
                    Thread.sleep(1000 * 10)
                    cont ++
                }
            }
        }catch (error: Exception){
            print(error.message)
            return Result.retry()
        }
        return Result.success()

    }

    fun getNotify(idNoty: Int, idUsr: Int){

        var datos = JSONObject().apply {
            put("consult_notify", true)
            put("idNoty", idNoty)
            put("idUsr", idUsr)
        }

        peticionPost(datos, "consultNotify.php")
    }

    fun createNotifys(notys: JSONObject){
        var lastId = 0
        notys.keys().forEach {
            val noti = notys.getJSONObject(it)
            val msj = noti.getString("message")

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            var builder = NotificationCompat.Builder(context, VariablesConf.CHANNEL_ONE_NOTY)
                .setSmallIcon(R.mipmap.ico_alert_new_dom_foreground)
                .setContentTitle("Aquitoy Notification")
                .setContentText(msj)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(msj))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }

            lastId = noti.getInt("id")

        }

        println("el ultimo id de las notis : $lastId")

        val dats = ContentValues().apply {
            put("id", lastId)
        }
        idNo = lastId
        motorDb.insert(dats, "notifys")
    }


    override fun actionPost(obj: JSONObject) {
        super.actionPost(obj)

        when(obj.getString("tag")){

            "notify" -> {
                createNotifys(obj.getJSONObject("notys"))
            }else -> {

                println("NO hay notificaciones")
            }

        }
    }
}