package com.example.miniproyecto2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
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
                    mediaPlayer?.start()

                }else if(activo && mediaPlayer?.isPlaying!!){
                    mediaPlayer?.pause()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }else{
                    mediaPlayer?.reset()
                    mediaPlayer?.setDataSource(resources.openRawResourceFd(rep[reproduciendo].sng))
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                    activo = true
                    startForeground(NOTIFICATION_ID, buildNotification())
                }
            }
            "NEXT_ACTION" -> {
                if(reproduciendo >= rep.size){
                    reproduciendo = 0
                }else{
                    reproduciendo+=1
                }
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(resources.openRawResourceFd(rep[reproduciendo].sng))
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                activo = true
            }

            "PREV_ACTION" -> {
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
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}