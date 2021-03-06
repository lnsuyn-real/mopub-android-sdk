// Copyright 2018-2019 Twitter, Inc.
// Licensed under the MoPub SDK License Agreement
// http://www.mopub.com/legal/sdk-license-agreement/

package com.mopub.nativeads;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;

import static com.mopub.common.logging.MoPubLog.SdkLogEvent.ERROR;

class StaticNativeViewHolder {
    @Nullable View mainView;
    @Nullable TextView titleView;
    @Nullable TextView textView;
    @Nullable TextView callToActionView;
    @Nullable ImageView mainImageView;
    @Nullable ImageView iconImageView;
    @Nullable ImageView privacyInformationIconImageView;

    @VisibleForTesting
    static final StaticNativeViewHolder EMPTY_VIEW_HOLDER = new StaticNativeViewHolder();

    // Use fromViewBinder instead of a constructor
    private StaticNativeViewHolder() {}

    @NonNull
    static StaticNativeViewHolder fromViewBinder(@NonNull final View view,
            @NonNull final ViewBinder viewBinder) {
        final StaticNativeViewHolder staticNativeViewHolder = new StaticNativeViewHolder();
        staticNativeViewHolder.mainView = view;
        try {
            staticNativeViewHolder.titleView = (TextView) view.findViewById(viewBinder.titleId);
            staticNativeViewHolder.textView = (TextView) view.findViewById(viewBinder.textId);
            staticNativeViewHolder.callToActionView =
                    (TextView) view.findViewById(viewBinder.callToActionId);
            staticNativeViewHolder.mainImageView =
                    (ImageView) view.findViewById(viewBinder.mainImageId);
            staticNativeViewHolder.iconImageView =
                    (ImageView) view.findViewById(viewBinder.iconImageId);
            staticNativeViewHolder.privacyInformationIconImageView =
                    (ImageView) view.findViewById(viewBinder.privacyInformationIconImageId);
            return staticNativeViewHolder;
        } catch (ClassCastException exception) {
            MoPubLog.log(ERROR, "Could not cast from id in ViewBinder to expected View type", exception);
            return EMPTY_VIEW_HOLDER;
        }
    }
}
