

package org.lntorrent.libretorrent.core.system;

import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public interface SystemFacade
{
    NetworkInfo getActiveNetworkInfo();

    NetworkCapabilities getNetworkCapabilities();

    boolean isActiveNetworkMetered();

    String getAppVersionName();
}
