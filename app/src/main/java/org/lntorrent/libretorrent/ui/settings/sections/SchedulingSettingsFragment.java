

package org.lntorrent.libretorrent.ui.settings.sections;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import com.takisoft.preferencex.PreferenceFragmentCompat;

import org.lntorrent.libretorrent.R;
import org.lntorrent.libretorrent.core.RepositoryHelper;
import org.lntorrent.libretorrent.core.settings.SettingsRepository;
import org.lntorrent.libretorrent.core.utils.Utils;
import org.lntorrent.libretorrent.service.Scheduler;
import org.lntorrent.libretorrent.ui.settings.customprefs.TimePreference;
import org.lntorrent.libretorrent.ui.settings.customprefs.TimePreferenceDialogFragmentCompat;

public class SchedulingSettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener
{
    @SuppressWarnings("unused")
    private static final String TAG = SchedulingSettingsFragment.class.getSimpleName();

    private SettingsRepository pref;

    public static SchedulingSettingsFragment newInstance()
    {
        SchedulingSettingsFragment fragment = new SchedulingSettingsFragment();
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        pref = RepositoryHelper.getSettingsRepository(getActivity().getApplicationContext());

        String keyEnableStart = getString(R.string.pref_key_enable_scheduling_start);
        SwitchPreferenceCompat enableStart = findPreference(keyEnableStart);
        if (enableStart != null) {
            enableStart.setChecked(pref.enableSchedulingStart());
            bindOnPreferenceChangeListener(enableStart);
        }

        String keyEnableStop = getString(R.string.pref_key_enable_scheduling_shutdown);
        SwitchPreferenceCompat enableStop = findPreference(keyEnableStop);
        if (enableStop != null) {
            enableStop.setChecked(pref.enableSchedulingShutdown());
            bindOnPreferenceChangeListener(enableStop);
        }

        String keyStartTime = getString(R.string.pref_key_scheduling_start_time);
        TimePreference startTime = findPreference(keyStartTime);
        if (startTime != null) {
            startTime.setTime(pref.schedulingStartTime());
            bindOnPreferenceChangeListener(startTime);
        }

        String keyStopTime = getString(R.string.pref_key_scheduling_shutdown_time);
        TimePreference stopTime = findPreference(keyStopTime);
        if (stopTime != null) {
            stopTime.setTime(pref.schedulingShutdownTime());
            bindOnPreferenceChangeListener(stopTime);
        }

        String keyRunOnlyOnce = getString(R.string.pref_key_scheduling_run_only_once);
        CheckBoxPreference runOnlyOnce = findPreference(keyRunOnlyOnce);
        if (runOnlyOnce != null) {
            runOnlyOnce.setChecked(pref.schedulingRunOnlyOnce());
            bindOnPreferenceChangeListener(runOnlyOnce);
        }

        String keySwitchWiFi = getString(R.string.pref_key_scheduling_switch_wifi);
        CheckBoxPreference switchWiFi = findPreference(keySwitchWiFi);
        if (switchWiFi != null) {
            switchWiFi.setChecked(pref.schedulingSwitchWiFi());
            bindOnPreferenceChangeListener(switchWiFi);
        }
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_scheduling, rootKey);
    }

    private void bindOnPreferenceChangeListener(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference)
    {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.getKey());
        }
        if (dialogFragment != null && isAdded()) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getParentFragmentManager(), "android.support.v7.preference" +
                    ".PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        Context context = getActivity().getApplicationContext();

        if (preference.getKey().equals(getString(R.string.pref_key_enable_scheduling_start))) {
            pref.enableSchedulingStart((boolean)newValue);

            if ((boolean)newValue) {
                int time = pref.schedulingStartTime();
                Scheduler.setStartAppAlarm(context, time);
            } else {
                Scheduler.cancelStartAppAlarm(context);
            }
            Utils.enableBootReceiver(context, (boolean)newValue);

        } else if (preference.getKey().equals(getString(R.string.pref_key_enable_scheduling_shutdown))) {
            pref.enableSchedulingShutdown((boolean)newValue);

            if ((boolean)newValue) {
                int time = pref.schedulingStartTime();
                Scheduler.setStopAppAlarm(context, time);
            } else {
                Scheduler.cancelStopAppAlarm(context);
            }
            Utils.enableBootReceiver(getActivity(), (boolean)newValue);

        } else if (preference.getKey().equals(getString(R.string.pref_key_scheduling_start_time))) {
            pref.schedulingStartTime((int)newValue);
            Scheduler.setStartAppAlarm(context, (int)newValue);

        } else if (preference.getKey().equals(getString(R.string.pref_key_scheduling_shutdown_time))) {
            pref.schedulingShutdownTime((int)newValue);
            Scheduler.setStopAppAlarm(context, (int)newValue);

        } else if (preference.getKey().equals(getString(R.string.pref_key_scheduling_run_only_once))) {
            pref.schedulingRunOnlyOnce((boolean)newValue);

        } else if (preference.getKey().equals(getString(R.string.pref_key_scheduling_switch_wifi))) {
            pref.schedulingSwitchWiFi((boolean)newValue);
        }

        return true;
    }
}
