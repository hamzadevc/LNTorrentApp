

package org.lntorrent.libretorrent.core.system;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED;
import static android.net.NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING;

public class FakeSystemFacade implements SystemFacade
{
    public Context appContext;
    public boolean isRoaming;
    public boolean isMetered;
    public int activeNetworkType = ConnectivityManager.TYPE_MOBILE;
    public NetworkInfo.DetailedState connectionState = NetworkInfo.DetailedState.CONNECTED;

    public FakeSystemFacade(@NonNull Context appContext)
    {
        this.appContext = appContext;
    }

    @Override
    public NetworkInfo getActiveNetworkInfo()
    {
        NetworkInfo info;

        try {
            Class<?> netInfoClass = NetworkInfo.class;
            Constructor<?> newInfoConstructor = netInfoClass
                    .getConstructor(int.class, int.class, String.class, String.class);
            Method setDetailedState = netInfoClass
                    .getMethod("setDetailedState", NetworkInfo.DetailedState.class, String.class, String.class);
            Method setRoaming = netInfoClass.getMethod("setRoaming", boolean.class);

            int activeNetworkType = (isMetered ? ConnectivityManager.TYPE_MOBILE : ConnectivityManager.TYPE_WIFI);
            info = (NetworkInfo)newInfoConstructor.newInstance(activeNetworkType, 0, null, null);
            setDetailedState.invoke(info, connectionState, null, null);
            setRoaming.invoke(info, isRoaming);

        } catch (Exception e) {
            return null;
        }

        return info;
    }

    @TargetApi(23)
    @Override
    public NetworkCapabilities getNetworkCapabilities()
    {
        NetworkCapabilities caps;
        try {
            Class<?> netCapsClass = NetworkCapabilities.class;
            Constructor<?> netCapsConstructor = netCapsClass.getConstructor();
            Method addCapability = netCapsClass.getMethod("addCapability", int.class);
            Method removeCapability = netCapsClass.getMethod("removeCapability", int.class);

            caps = (NetworkCapabilities)netCapsConstructor.newInstance();
            if (isMetered)
                removeCapability.invoke(caps, NET_CAPABILITY_NOT_METERED);
            else
                addCapability.invoke(caps, NET_CAPABILITY_NOT_METERED);

            if (isRoaming)
                removeCapability.invoke(caps, NET_CAPABILITY_NOT_ROAMING);
            else
                addCapability.invoke(caps, NET_CAPABILITY_NOT_ROAMING);

        } catch (Exception e) {
            return null;
        }

        return caps;
    }

    @Override
    public boolean isActiveNetworkMetered()
    {
        return isMetered;
    }

    @Override
    public String getAppVersionName()
    {
        try {
            PackageInfo info = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);

            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            /* Ignore */
        }

        return null;
    }
}
