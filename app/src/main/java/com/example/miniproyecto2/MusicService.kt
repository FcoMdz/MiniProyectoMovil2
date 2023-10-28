package com.example.miniproyecto2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

val rep = listOf(
    Repertorio("The paranormal", R.drawable.the_paranormal, R.raw.the_paranormal),
    Repertorio("Inside you", R.drawable.inside_you, R.raw.inside_you),
    Repertorio("Titanium", R.drawable.titanium, R.raw.titanium),
    Repertorio("A long way", R.drawable.a_long_way, R.raw.a_long_way)
)
class MusicService: Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var reproduciendo: Int = 0
    private var activo = false
    private val NOTIFICATION_ID = 1
    private val NOTIFICATION_CHANNEL_ID = "music_channel"
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Music Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun buildNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Reproduciendo música")
            .setSmallIcon(R.drawable.baseline_music_note_24)
            .setContentIntent(pendingIntent)
            .build()

        return notification
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY_ACTION" -> {
                // Lógica para reproducir la canción
                if(activo && !(mediaPlayer?.isPlaying!!)){
                    val prefs = applicationContext.getSharedPreferences("ultMus", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putBoolean("play", true)
                    editor.apply()
                    mediaPlayer?.start()

                }else if(activo && mediaPlayer?.isPlaying!!){
                    mediaPlayer?.pause()
                    val prefs = applicationContext.getSharedPreferences("ultMus", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putBoolean("play", false)
                    editor.apply()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }else{
                    val prefs = applicationContext.getSharedPreferences("ultMus", MODE_PRIVATE)
                    val song = prefs.getInt("song", 0)
                    reproduciendo = song
                    iniciarReproduccion()
                    startForeground(NOTIFICATION_ID, buildNotification())
                }
            }
            "NEXT_ACTION" -> {
               sigiente()
            }

            "PREV_ACTION" -> {
               anterior()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun iniciarReproduccion(){
        val prefs = applicationContext.getSharedPreferences("ultMus", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("song", reproduciendo)
        editor.putBoolean("play", true)
        editor.apply()
        mediaPlayer?.reset()
        mediaPlayer?.setOnCompletionListener {
            sigiente()
        }
        mediaPlayer?.setDataSource(resources.openRawResourceFd(rep[reproduciendo].sng))
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        activo = true
    }

    fun sigiente(){
        if(reproduciendo >= rep.size-1){
            reproduciendo = 0
        }else{
            reproduciendo+=1
        }
        iniciarReproduccion()
    }
    fun anterior(){
        if(reproduciendo <= 0){
            reproduciendo = rep.size-1
        }else{
            reproduciendo -= 1
        }
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(resources.openRawResourceFd(rep[reproduciendo].sng))
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        activo = true
        iniciarReproduccion()
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefs = applicationContext.getSharedPreferences("ultMus", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("play", false)
        editor.apply()
        mediaPlayer?.release()
    }
}