

package org.lntorrent.libretorrent;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;
import org.libtorrent4j.swig.libtorrent;
import org.lntorrent.libretorrent.core.system.LibTorrentSafAdapter;
import org.lntorrent.libretorrent.core.utils.Utils;
import org.lntorrent.libretorrent.ui.TorrentNotifier;
import org.lntorrent.libretorrent.ui.errorreport.ErrorReportActivity;

@AcraCore(buildConfigClass = BuildConfig.class)
@AcraMailSender(mailTo = "proninyaroslav@mail.ru")
@AcraDialog(reportDialogClass = ErrorReportActivity.class)

public class MainApplication extends MultiDexApplication
{
    @SuppressWarnings("unused")
    private static final String TAG = MainApplication.class.getSimpleName();

    static {
        /* Vector Drawable support in ImageView for API < 21 */
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Utils.migrateTray2SharedPreferences(this);
       // ACRA.init(this);

        LibTorrentSafAdapter adapter = new LibTorrentSafAdapter(this);
        adapter.swigReleaseOwnership();
        libtorrent.set_posix_wrapper(adapter);

        TorrentNotifier.getInstance(this).makeNotifyChans();
    }
}