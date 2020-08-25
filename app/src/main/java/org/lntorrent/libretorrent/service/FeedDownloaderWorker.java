

package org.lntorrent.libretorrent.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.lntorrent.libretorrent.core.RepositoryHelper;
import org.lntorrent.libretorrent.core.exception.DecodeException;
import org.lntorrent.libretorrent.core.exception.FetchLinkException;
import org.lntorrent.libretorrent.core.model.AddTorrentParams;
import org.lntorrent.libretorrent.core.model.TorrentEngine;
import org.lntorrent.libretorrent.core.model.data.MagnetInfo;
import org.lntorrent.libretorrent.core.model.data.Priority;
import org.lntorrent.libretorrent.core.model.data.entity.FeedItem;
import org.lntorrent.libretorrent.core.model.data.metainfo.TorrentMetaInfo;
import org.lntorrent.libretorrent.core.settings.SettingsRepository;
import org.lntorrent.libretorrent.core.storage.FeedRepository;
import org.lntorrent.libretorrent.core.system.FileSystemFacade;
import org.lntorrent.libretorrent.core.system.SystemFacadeHelper;
import org.lntorrent.libretorrent.core.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * The worker for downloading torrents from RSS/Atom items.
 */

public class FeedDownloaderWorker extends Worker
{
    @SuppressWarnings("unused")
    private static final String TAG = FeedDownloaderWorker.class.getSimpleName();

    public static final String ACTION_DOWNLOAD_TORRENT_LIST = "org.proninyaroslav.libretorrent.service.FeedDownloaderWorker.ACTION_DOWNLOAD_TORRENT_LIST";
    public static final String TAG_ACTION = "action";
    public static final String TAG_ITEM_ID_LIST = "item_id_list";

    private static final long START_ENGINE_RETRY_TIME = 3000; /* ms */

    private TorrentEngine engine;
    private FeedRepository repo;
    private SettingsRepository pref;

    public FeedDownloaderWorker(@NonNull Context context, @NonNull WorkerParameters params)
    {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        engine = TorrentEngine.getInstance(getApplicationContext());
        repo = RepositoryHelper.getFeedRepository(getApplicationContext());
        pref = RepositoryHelper.getSettingsRepository(getApplicationContext());

        Data data = getInputData();
        String action = data.getString(TAG_ACTION);

        if (action == null)
            return Result.failure();

        if (ACTION_DOWNLOAD_TORRENT_LIST.equals(action))
            return addTorrents(fetchTorrents(data.getStringArray(TAG_ITEM_ID_LIST)));

        return Result.failure();
    }

    private ArrayList<AddTorrentParams> fetchTorrents(String... ids)
    {
        ArrayList<AddTorrentParams> paramsList = new ArrayList<>();
        if (ids == null)
            return paramsList;

        for (FeedItem item : repo.getItemsById(ids)) {
            AddTorrentParams params = fetchTorrent(item);
            if (params != null)
                paramsList.add(params);
        }

        return paramsList;
    }

    private AddTorrentParams fetchTorrent(FeedItem item)
    {
        if (item == null)
            return null;

        Uri downloadPath = Utils.getTorrentDownloadPath(getApplicationContext());
        if (downloadPath == null)
            return null;
        String name;
        Priority[] priorities = null;
        boolean isMagnet = false;
        String source, sha1hash;

        if (item.downloadUrl.startsWith(Utils.MAGNET_PREFIX)) {
            MagnetInfo info;
            try {
                info = engine.parseMagnet(item.downloadUrl);

            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
            sha1hash = info.getSha1hash();
            name = info.getName();
            isMagnet = true;
            source = item.downloadUrl;

        } else {
            byte[] response;
            TorrentMetaInfo info;
            try {
                response = Utils.fetchHttpUrl(getApplicationContext(), item.downloadUrl);
                info = new TorrentMetaInfo(response);

            } catch (FetchLinkException e) {
                Log.e(TAG, "URL fetch error: " + Log.getStackTraceString(e));
                return null;
            } catch (DecodeException e) {
                Log.e(TAG, "Invalid torrent: " + Log.getStackTraceString(e));
                return null;
            }
            FileSystemFacade fs = SystemFacadeHelper.getFileSystemFacade(getApplicationContext());
            if (fs.getDirAvailableBytes(downloadPath) < info.torrentSize) {
                Log.e(TAG, "Not enough free space for " + info.torrentName);
                return null;
            }
            File tmp;
            try {
                tmp = fs.makeTempFile(".torrent");
                org.apache.commons.io.FileUtils.writeByteArrayToFile(tmp, response);

            } catch (Exception e) {
                Log.e(TAG, "Error write torrent file " + info.torrentName + ": " + Log.getStackTraceString(e));
                return null;
            }
            priorities = new Priority[info.fileList.size()];
            Arrays.fill(priorities, Priority.DEFAULT);
            sha1hash = info.sha1Hash;
            name = info.torrentName;
            source = Uri.fromFile(tmp).toString();
        }

        return new AddTorrentParams(source, isMagnet, sha1hash, name,
                priorities, downloadPath, false,
                !pref.feedStartTorrents());
    }

    private Result addTorrents(ArrayList<AddTorrentParams> paramsList)
    {
        if (paramsList == null || paramsList.isEmpty())
            return Result.failure();

        if (!engine.isRunning())
            engine.start();

        /* TODO: maybe refactor */
        while (!engine.isRunning()) {
            try {
                Thread.sleep(START_ENGINE_RETRY_TIME);
                engine.start();

            } catch (InterruptedException e) {
                return Result.failure();
            }
        }

        engine.addTorrents(paramsList, true);

        return Result.success();
    }
}
