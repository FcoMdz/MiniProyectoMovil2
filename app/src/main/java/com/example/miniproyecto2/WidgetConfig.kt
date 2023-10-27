package com.example.miniproyecto2

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class WidgetConfig: Activity() {
    private var widgetId = 0

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
        WidgetMusic.actualizarWidget(this,appWidgetManager,widgetId);

        val resultado = Intent();
        resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);
        setResult(Activity.RESULT_OK,resultado);
        finish();

    }
}