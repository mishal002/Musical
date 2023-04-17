package e_card.e_cardaddress.adresschange.musical.Data_Class

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import e_card.e_cardaddress.adresschange.musical.Activity.PlayerActivity
import e_card.e_cardaddress.adresschange.musical.R

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    lateinit var mediaSession: MediaSessionCompat
    lateinit var runnable: Runnable

    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(this, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(playPauseBtn: Int) {

        val preIntent =
            Intent(
                baseContext,
                NotificationeReceiver::class.java
            ).setAction(ApplicationClass.PREVIOUS)
        val prePendingIntent =
            PendingIntent.getBroadcast(baseContext, 0, preIntent, PendingIntent.FLAG_IMMUTABLE)

        val playIntent =
            Intent(baseContext, NotificationeReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent =
            PendingIntent.getBroadcast(
                baseContext,
                0,
                playIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val nextIntent =
            Intent(baseContext, NotificationeReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent =
            PendingIntent.getBroadcast(
                baseContext,
                0,
                nextIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val exitIntent =
            Intent(baseContext, NotificationeReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent =
            PendingIntent.getBroadcast(
                baseContext,
                0,
                exitIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val imgArt = getImgArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
        val image = if (imgArt != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.music_logo)
        }

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANEL_ID)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.music_icon)
            .setLargeIcon(image)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_icon_1, "Previous", prePendingIntent)
            .addAction(
                playPauseBtn,
                "Play",
                playPendingIntent
            )
            .addAction(R.drawable.next_icon_1, "Next", nextPendingIntent)
            .addAction(R.drawable.logout_icon, "Exit", exitPendingIntent)
            .build()
        startForeground(13, notification)
    }

    fun createMediaPlayer() {
        try {
            if (PlayerActivity.musicService == null) PlayerActivity.musicService!!.mediaPlayer =
                MediaPlayer()
            PlayerActivity.musicService!!.mediaPlayer!!.reset()
            PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
            PlayerActivity.musicService!!.mediaPlayer!!.prepare()
            PlayerActivity.binding.playPauseBtnPA.setImageResource(R.drawable.pause_icon)
            showNotification(R.drawable.pause_icon)
            PlayerActivity.binding.tvseekbarStart.text =
                formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.tvseekbarEnd.text =
                formatDuration(mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.seekbarPA.progress = 0
            PlayerActivity.binding.seekbarPA.max = mediaPlayer!!.duration
        } catch (e: Exception) {
            return
        }
    }

    fun seekBarSetup() {
        runnable = Runnable {
            PlayerActivity.binding.tvseekbarStart.text =
                formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekbarPA.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

}