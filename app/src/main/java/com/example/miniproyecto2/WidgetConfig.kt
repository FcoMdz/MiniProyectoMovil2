package com.example.miniproyecto2

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView

class WidgetConfig: Activity() {
    private var widgetId = 0
    private lateinit var btnNext: ImageView
    private lateinit var btnPrev: ImageView
    private lateinit var btnPlay: ImageView
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widget_layout)


        val intentOrigen = intent
        val params = intentOrigen.extras

        widgetId = params!!.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )

        setResult(Activity.RESULT_CANCELED)
        val appWidgetManager = AppWidgetManager.getInstance(this@WidgetConfig);
        val prefs = applicationContext.getSharedPreferences("ultMus", MODE_PRIVATE)
        val song = prefs.getInt("song", -1)
        val play = prefs.getBoolean("play", false)
        WidgetMusic.actualizarWidget(this,appWidgetManager, widgetId, song, play);

        val resultado = Intent();
        resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);
        setResult(Activity.RESULT_OK,resultado);
        finish();

    }
}