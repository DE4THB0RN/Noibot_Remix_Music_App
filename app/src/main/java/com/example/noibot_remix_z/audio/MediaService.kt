package com.example.noibot_remix_z.audio

import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.media3.exoplayer.ExoPlayer

@AndroidEntryPoint
class MusicService : MediaSessionService() {


    @Inject
    lateinit var exoPlayer: ExoPlayer

    private var mediaSession: MediaSession? = null

    /**
     * Called when the service is first created.
     * This is a good place to initialize the MediaSession if ExoPlayer is injected,
     * as Hilt field injection completes before super.onCreate() returns.
     */
    override fun onCreate() {
        super.onCreate()

        // Now that exoPlayer is injected and initialized by Hilt, we can create the MediaSession.
        mediaSession = MediaSession.Builder(this, exoPlayer)
            .setId("noibot_music_session_${System.currentTimeMillis()}") // Unique ID for your session
            // Add a MediaSession.Callback here if you need to handle custom commands
            // or control playback behavior (e.g., how items are added, custom actions).
            // .setCallback(MyMediaSessionCallback())
            .build()
    }

    /**
     * Called when a MediaController wants to connect.
     * You MUST return a MediaSession instance here.
     * If mediaSession is null (e.g., service was started but onCreate somehow skipped or failed partially,
     * though unlikely with Hilt), this provides a fallback.
     */
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        // If mediaSession wasn't initialized in onCreate for some reason,
        // ensure it is now. This is more of a safeguard.
        if (mediaSession == null && ::exoPlayer.isInitialized) {
            mediaSession = MediaSession.Builder(this, exoPlayer)
                .setId("noibot_music_session_fallback_${System.currentTimeMillis()}")
                .build()
        }
        return mediaSession
    }

    /**
     * Called when the app is swiped away from the recents screen.
     * Default behavior of MediaSessionService might handle some of this,
     * but explicit handling is good for clarity and custom logic.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        // If the player is playing, pause it.
        if (exoPlayer.playWhenReady) {
            exoPlayer.pause()
        }
        // Stop the service to allow resources to be freed if nothing is playing
        // and the app is removed from recents. Consider your app's desired behavior.
        // If you want playback to continue even after removing from recents (until explicitly stopped),
        // you might not call stopSelf() here.
        stopSelf()
    }

    /**
     * Called when the service is being destroyed.
     * Release the MediaSession and the Player.
     */
    override fun onDestroy() {
        mediaSession?.run {
            // The player is managed by Hilt and its lifecycle is tied to the scope
            // defined in PlayerModule. If it's @ServiceScoped, Hilt handles its release
            // when the service is destroyed. If @Singleton, it lives longer.
            // However, releasing the session is crucial here.
            release()
            mediaSession = null
        }
        // If ExoPlayer is @ServiceScoped, Hilt will manage its release.
        // If it's @Singleton, you might not release it here unless the app is shutting down.
        // For @ServiceScoped ExoPlayer, explicit release here is usually not needed as Hilt does it.
        // if (::exoPlayer.isInitialized) { // Check if initialized
        //     exoPlayer.release() // Only if not managed by Hilt or if you want explicit early release
        // }
        super.onDestroy()
    }
}

