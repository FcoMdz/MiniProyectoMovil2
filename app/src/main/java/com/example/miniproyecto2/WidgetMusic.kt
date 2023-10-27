package com.example.miniproyecto2

import ActionReciver
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import java.util.Random

class WidgetMusic: AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        if (appWidgetIds != null) {
            for (i in appWidgetIds.indices){
                val widgetId = appWidgetIds[i];

                actualizarWidget(context, appWidgetManager,widgetId)
            }
        }
    }
    companion object{
        fun actualizarWidget(
            context: Context?,
            appWidgetManager: AppWidgetManager?,
            widgetId: Int
        ){
            val rep = listOf(
                 Repertorio("A long way", R.drawable.a_long_way,R.raw.a_long_way),
                 Repertorio("Titanium", R.drawable.titanium,R.raw.titanium),
                 Repertorio("Inside you", R.drawable.inside_you,R.raw.inside_you),
                 Repertorio("The paranormal", R.drawable.the_paranormal,R.raw.the_paranormal)
            )
            
           // appWidgetManager?.updateAppWidget(widgetId, controles)
        }
    }
}