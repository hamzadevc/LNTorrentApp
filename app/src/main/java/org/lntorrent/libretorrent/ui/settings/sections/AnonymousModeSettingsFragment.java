

package org.lntorrent.libretorrent.ui.settings.sections;

import android.os.Bundle;

import androidx.preference.Preference;

import com.takisoft.preferencex.PreferenceFragmentCompat;

import org.lntorrent.libretorrent.R;
import org.lntorrent.libretorrent.core.RepositoryHelper;
import org.lntorrent.libretorrent.core.settings.SettingsRepository;
import org.lntorrent.libretorrent.ui.settings.customprefs.SwitchBarPreference;

public class AnonymousModeSettingsFragment extends PreferenceFragmentCompat
        implements
        Preference.OnPreferenceChangeListener
{
    @SuppressWarnings("unused")
    private static final String TAG = AnonymousModeSettingsFragment.class.getSimpleName();

    private SettingsRepository pref;

    public static AnonymousModeSettingsFragment newInstance()
    {
        AnonymousModeSettingsFragment fragment = new AnonymousModeSettingsFragment();

        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        pref = RepositoryHelper.getSettingsRepository(getActivity().getApplicationContext());

        String keyAnonymousMode = getString(R.string.pref_key_anonymous_mode);
        SwitchBarPreference anonymousMode = findPreference(keyAnonymousMode);
        if (anonymousMode != null) {
            anonymousMode.setChecked(pref.anonymousMode());
            bindOnPreferenceChangeListener(anonymousMode);
        }
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_anonymous_mode, rootKey);
    }

    private void bindOnPreferenceChangeListener(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference.getKey().equals(getString(R.string.pref_key_anonymous_mode)))
            pref.anonymousMode((boolean)newValue);

        return true;
    }
}
