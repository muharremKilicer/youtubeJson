package com.example.java2.cizgifilmapp2;

import com.google.android.youtube.player.YouTubePlayer;

public class PlayerStateListener implements YouTubePlayer.PlayerStateChangeListener{

    @Override
    public void onAdStarted() {
        // Called when playback of an advertisement starts.

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason reason) {
        // Called when an error occurs.

    }

    @Override
    public void onLoaded(String arg0) {
        // Called when a video has finished loading.

    }

    @Override
    public void onLoading() {
        // Called when the player begins loading a video and is not ready to accept commands affecting playback

    }

    @Override
    public void onVideoEnded() {
        // Called when the video reaches its end.

    }

    @Override
    public void onVideoStarted() {
        // Called when playback of the video starts.

    }
}
