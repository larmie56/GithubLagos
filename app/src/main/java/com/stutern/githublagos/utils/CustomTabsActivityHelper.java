package com.stutern.githublagos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;

import com.stutern.githublagos.R;
import com.stutern.githublagos.shared.CustomTabsHelper;
import com.stutern.githublagos.shared.ServiceConnection;
import com.stutern.githublagos.shared.ServiceConnectionCallback;

public class CustomTabsActivityHelper implements ServiceConnectionCallback {

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsServiceConnection mConnection;
    private CustomTabConnectionCallback mConnectionCallback;

    public interface CustomTabConnectionCallback {
        void onCustomTabConnected();

        void onCustomTabDisconnected();
    }

    public void openCustomTab(Context context, Uri uri) {
        String packageName = CustomTabsHelper.getPackageNameToUse(context);

        if (packageName == null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.resolveActivity(context.getPackageManager());
        }

        else {
            CustomTabsIntent intentBuilder = new CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    .build();

            intentBuilder.intent.setPackage(packageName);

            intentBuilder.launchUrl(context, uri);

        }
    }

    /**
     * Binds the activity to the custom tabs service.
     * @param activity activity to be "bound" to the service
     */
    public void bindCustomTabsService(Activity activity) {
        if (mCustomTabsClient != null) return;

        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName == null) return;

        mConnection = new ServiceConnection(this);
        CustomTabsClient.bindCustomTabsService(activity, packageName, mConnection);
    }

    /**
     * Unbinds the activity from the custom tabs service
     * @param activity
     */
    public void unbindCustomTabsService(Activity activity) {
        if (mConnection == null) return;
        activity.unbindService(mConnection);
        mCustomTabsClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mCustomTabsClient = client;
        mCustomTabsClient.warmup(0L);
        if (mConnectionCallback != null) {
            mConnectionCallback.onCustomTabConnected();
        }
    }

    @Override
    public void onServiceDisconnected() {
        mCustomTabsClient = null;
        mConnection = null;
        if (mConnectionCallback != null) {
            mConnectionCallback.onCustomTabDisconnected();
        }
    }
}
