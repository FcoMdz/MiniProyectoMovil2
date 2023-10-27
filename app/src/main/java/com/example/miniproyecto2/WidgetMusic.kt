package com.example.miniproyecto2

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
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
        }
    }
}