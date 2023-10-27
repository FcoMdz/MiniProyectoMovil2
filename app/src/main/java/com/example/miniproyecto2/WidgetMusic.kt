package com.example.miniproyecto2

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
            for (i in appWidgetIds.indices){
                val widgetId = appWidgetIds[i]
                //Listener de las acciones
                actualizarWidget(context, appWidgetManager, widgetId)
            }
        }
    }


    companion object{
        fun actualizarWidget(
            context: Context,
            appWidgetManager: AppWidgetManager?,
            widgetId: Int,
        ){
            val controles = RemoteViews(context?.packageName, R.layout.widget_layout)
            controles.setOnClickPendingIntent(R.id.btn_play, getPendingSelfIntent(context, "PLAY_ACTION"))
            controles.setOnClickPendingIntent(R.id.btn_next, getPendingSelfIntent(context, "NEXT_ACTION"))
            controles.setOnClickPendingIntent(R.id.btn_prev, getPendingSelfIntent(context, "PREV_ACTION"))

            //Ajustando caratula
            /*if(song!=-1){
                controles.setTextViewText(R.id.txt_nombreCancion, rep[song].nombre)
                controles.setImageViewResource(R.id.img_cancion, rep[song].img)
            }*/
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