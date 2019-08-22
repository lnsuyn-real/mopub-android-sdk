// Copyright 2018-2019 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// http://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.mopub.common.IntentActions;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;

import static com.mopub.common.logging.MoPubLog.SdkLogEvent.CUSTOM;

public abstract class BaseVideoViewController {
    private final Context mContext;
    private final RelativeLayout mLayout;
    @NonNull private final BaseVideoViewControllerListener mBaseVideoViewControllerListener;
    @Nullable private Long mBroadcastIdentifier;

    public interface BaseVideoViewControllerListener {
        void onSetContentView(final View view);
        void onSetRequestedOrientation(final int requestedOrientation);
        void onFinish();
        void onStartActivityForResult(final Class<? extends Activity> clazz,
                final int requestCode,
                final Bundle extras);
    }

    protected BaseVideoViewController(final Context context,
            @Nullable final Long broadcastIdentifier,
            @NonNull final BaseVideoViewControllerListener baseVideoViewControllerListener) {
        Preconditions.checkNotNull(baseVideoViewControllerListener);

        mContext = context;
        mBroadcastIdentifier = broadcastIdentifier;
        mBaseVideoViewControllerListener = baseVideoViewControllerListener;
        mLayout = new RelativeLayout(mContext);
    }

     protected void onCreate() {
        final RelativeLayout.LayoutParams adViewLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        adViewLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLayout.addView(getVideoView(), 0, adViewLayout);
        mBaseVideoViewControllerListener.onSetContentView(mLayout);
    }

    protected abstract VideoView getVideoView();
    protected abstract void onPause();
    protected abstract void onResume();
    protected abstract void onDestroy();
    protected abstract void onSaveInstanceState(@NonNull Bundle outState);
    protected abstract void onConfigurationChanged(Configuration configuration);
    protected abstract void onBackPressed();

    public boolean backButtonEnabled() {
        return true;
    }

    void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // By default, the activity result is ignored
    }

    @NonNull
    protected BaseVideoViewControllerListener getBaseVideoViewControllerListener() {
        return mBaseVideoViewControllerListener;
    }

    protected Context getContext() {
        return mContext;
    }

    public ViewGroup getLayout() {
        return mLayout;
    }

    protected void videoError(boolean shouldFinish) {
        MoPubLog.log(CUSTOM, "Video cannot be played.");
        broadcastAction(IntentActions.ACTION_INTERSTITIAL_FAIL);
        if (shouldFinish) {
           mBaseVideoViewControllerListener.onFinish();
        }
    }

    protected void videoCompleted(boolean shouldFinish) {
        if (shouldFinish) {
            mBaseVideoViewControllerListener.onFinish();
        }
    }

    void broadcastAction(final String action) {
        if (mBroadcastIdentifier != null) {
            BaseBroadcastReceiver.broadcastAction(mContext, mBroadcastIdentifier, action);
        } else {
            MoPubLog.log(CUSTOM, "Tried to broadcast a video event without a broadcast identifier to send to.");
        }
    }
}
