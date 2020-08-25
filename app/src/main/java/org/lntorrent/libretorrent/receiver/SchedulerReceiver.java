

package org.lntorrent.libretorrent.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import org.lntorrent.libretorrent.core.RepositoryHelper;
import org.lntorrent.libretorrent.core.model.TorrentEngine;
import org.lntorrent.libretorrent.core.settings.SettingsRepository;
import org.lntorrent.libretorrent.core.utils.Utils;
import org.lntorrent.libretorrent.service.Scheduler;

import static org.lntorrent.libretorrent.service.Scheduler.SCHEDULER_WORK_START_APP;
import static org.lntorrent.libretorrent.service.Scheduler.SCHEDULER_WORK_STOP_APP;

/*
 * The receiver for AlarmManager scheduling
 */

public class SchedulerReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction() == null)
            return;

        Context appContext = context.getApplicationContext();
        SettingsRepository pref = RepositoryHelper.getSettingsRepository(appContext);

        switch (intent.getAction()) {
            case SCHEDULER_WORK_START_APP: {
                onStartApp(appContext, pref);
                break;
            }
            case SCHEDULER_WORK_STOP_APP: {
                onStopApp(appContext, pref);
                break;
            }
        }
    }

    private void onStartApp(Context appContext, SettingsRepository pref)
    {
        if (!pref.enableSchedulingStart())
            return;

        boolean oneshot = pref.schedulingRunOnlyOnce();
        if (oneshot) {
            pref.enableSchedulingStart(false);
        } else {
            Scheduler.setStartAppAlarm(appContext, pref.schedulingStartTime());
        }
        if (pref.schedulingSwitchWiFi())
            ((WifiManager)appContext.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);

        TorrentEngine.getInstance(appContext).start();
        Utils.enableBootReceiverIfNeeded(appContext);
    }

    private void onStopApp(Context appContext, SettingsRepository pref)
    {
        if (!pref.enableSchedulingShutdown())
            return;

        boolean oneshot = pref.schedulingRunOnlyOnce();
        if (oneshot) {
            pref.enableSchedulingShutdown(false);
        } else {
            Scheduler.setStartAppAlarm(appContext, pref.schedulingShutdownTime());
        }

        TorrentEngine.getInstance(appContext).forceStop();

        if (pref.schedulingSwitchWiFi())
            ((WifiManager)appContext.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(false);

        Utils.enableBootReceiverIfNeeded(appContext);
    }
}
