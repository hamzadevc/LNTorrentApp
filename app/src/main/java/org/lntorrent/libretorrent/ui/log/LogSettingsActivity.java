

package org.lntorrent.libretorrent.ui.log;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.lntorrent.libretorrent.R;
import org.lntorrent.libretorrent.core.utils.Utils;

public class LogSettingsActivity extends AppCompatActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = LogSettingsActivity.class.getSimpleName();

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(Utils.getSettingsTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_settings);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener((v) -> finish());
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
