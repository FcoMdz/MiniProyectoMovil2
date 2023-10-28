package com.example.miniproyecto2

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

class WidgetMusic: AppWidgetProvider() {


    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        if (appWidgetIds != null && context!=null) {
            val prefs = context.getSharedPreferences("ultMus", Activity.MODE_PRIVATE)
            val song = prefs.getInt("song", -1)
            val play = prefs.getBoolean("play", false)
            for (i in appWidgetIds.indices){
                val widgetId = appWidgetIds[i]
                actualizarWidget(context, appWidgetManager, widgetId, song, play)
            }
        }
    }
    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == "ACTUALIZAR_WIDGET_ACTION") {
            Log.d("Test", "Reciví update")
            // Aquí puedes realizar la actualización de la vista del widget
            // Llama a la función que actualiza el widget
            val prefs = context.getSharedPreferences("ultMus", Activity.MODE_PRIVATE)
            val song = prefs.getInt("song", -1)
            val play = prefs.getBoolean("play", false)
            val appWidget = AppWidgetManager.getInstance(context)
            val widgetIds = appWidget.getAppWidgetIds(ComponentName(context, WidgetMusic::class.java))
            for (i in widgetIds.indices){
                val widgetId = widgetIds[i]
                actualizarWidget(context, appWidget, widgetId, song, play)
            }
        }
    }

    companion object{
        fun actualizarWidget(
            context: Context,
            appWidgetManager: AppWidgetManager?,
            widgetId: Int,
            song:Int,
            play:Boolean
        ){
            val controles = RemoteViews(context?.packageName, R.layout.widget_layout)
            controles.setOnClickPendingIntent(R.id.btn_play, getPendingSelfIntent(context, "PLAY_ACTION"))
            controles.setOnClickPendingIntent(R.id.btn_next, getPendingSelfIntent(context, "NEXT_ACTION"))
            controles.setOnClickPendingIntent(R.id.btn_prev, getPendingSelfIntent(context, "PREV_ACTION"))

            //Ajustando caratula
            if(song!=-1){
                controles.setTextViewText(R.id.txt_nombreCancion, rep[song].nombre)
                controles.setImageViewResource(R.id.img_cancion, rep[song].img)
            }
            if(play){
                controles.setImageViewResource(R.id.btn_play, R.drawable.btn_pause)
            }
            //Update del widget
            appWidgetManager?.updateAppWidget(widgetId, controles)
        }
        fun getPendingSelfIntent(context:Context, action:String):PendingIntent{
            val intent = Intent(context, MusicService::class.java)
            intent.action = action
            return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }
}