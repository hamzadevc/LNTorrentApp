

package org.lntorrent.libretorrent.core.model.session;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lntorrent.libretorrent.core.exception.DecodeException;
import org.lntorrent.libretorrent.core.exception.TorrentAlreadyExistsException;
import org.lntorrent.libretorrent.core.logger.Logger;
import org.lntorrent.libretorrent.core.model.AddTorrentParams;
import org.lntorrent.libretorrent.core.model.TorrentEngineListener;
import org.lntorrent.libretorrent.core.model.data.MagnetInfo;
import org.lntorrent.libretorrent.core.model.data.entity.Torrent;
import org.lntorrent.libretorrent.core.settings.SessionSettings;

import java.io.File;
import java.io.IOException;

public interface TorrentSession
{
    Logger getLogger();

    void addListener(TorrentEngineListener listener);

    void removeListener(TorrentEngineListener listener);

    TorrentDownload getTask(String id);

    void setSettings(@NonNull SessionSettings settings);

    SessionSettings getSettings();

    byte[] getLoadedMagnet(String hash);

    void removeLoadedMagnet(String hash);

    Torrent addTorrent(@NonNull AddTorrentParams params,
                       boolean removeFile) throws IOException, TorrentAlreadyExistsException, DecodeException;

    void deleteTorrent(@NonNull String id, boolean withFiles);

    void restoreTorrents();

    MagnetInfo fetchMagnet(@NonNull String uri) throws Exception;

    MagnetInfo parseMagnet(@NonNull String uri);

    void cancelFetchMagnet(@NonNull String infoHash);

    long getDownloadSpeed();

    long getUploadSpeed();

    long getTotalDownload();

    long getTotalUpload();

    int getDownloadSpeedLimit();

    int getUploadSpeedLimit();

    int getListenPort();

    long getDhtNodes();

    void enableIpFilter(@NonNull Uri path);

    void disableIpFilter();

    boolean isLSDEnabled();

    void pauseAll();

    void resumeAll();

    void pauseAllManually();

    void resumeAllManually();

    void setMaxConnectionsPerTorrent(int connections);

    void setMaxUploadsPerTorrent(int uploads);

    void setAutoManaged(boolean autoManaged);

    boolean isDHTEnabled();

    boolean isPeXEnabled();

    void start();

    void startWithParams(@Nullable SessionInitParams startParams);

    void requestStop();

    boolean isRunning();

    long dhtNodes();

    int[] getPieceSizeList();

    void download(@NonNull String magnetUri, File saveDir, boolean paused);
}
