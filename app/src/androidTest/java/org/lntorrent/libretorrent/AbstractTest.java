

package org.lntorrent.libretorrent;

import android.Manifest;
import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.lntorrent.libretorrent.core.model.TorrentEngine;
import org.lntorrent.libretorrent.core.model.TorrentInfoProvider;
import org.lntorrent.libretorrent.core.storage.AppDatabase;
import org.lntorrent.libretorrent.core.storage.FeedRepository;
import org.lntorrent.libretorrent.core.storage.FeedRepositoryImpl;
import org.lntorrent.libretorrent.core.storage.TorrentRepository;
import org.lntorrent.libretorrent.core.storage.TorrentRepositoryImpl;
import org.lntorrent.libretorrent.core.system.SystemFacadeHelper;
import org.lntorrent.libretorrent.core.system.FileSystemFacade;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

public class AbstractTest
{
    @Rule
    public GrantPermissionRule runtimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE);

    protected Context context;
    protected AppDatabase db;
    protected TorrentEngine engine;
    protected TorrentInfoProvider stateProvider;
    protected TorrentRepository torrentRepo;
    protected FeedRepository feedRepo;
    protected FileSystemFacade fs;

    @Before
    public void init()
    {
        context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context,
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        torrentRepo = new TorrentRepositoryImpl(context, db);
        feedRepo = new FeedRepositoryImpl(context, db);
        engine = TorrentEngine.getInstance(context);
        stateProvider = TorrentInfoProvider.getInstance(engine);
        fs = SystemFacadeHelper.getFileSystemFacade(context);
    }

    @After
    public void finish()
    {
        db.close();
    }
}
