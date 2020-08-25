

package org.lntorrent.libretorrent.core.storage;

import androidx.annotation.NonNull;

import org.lntorrent.libretorrent.core.model.data.entity.FastResume;
import org.lntorrent.libretorrent.core.model.data.entity.Torrent;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface TorrentRepository
{
    void addTorrent(@NonNull Torrent torrent);

    void updateTorrent(@NonNull Torrent torrent);

    void deleteTorrent(@NonNull Torrent torrent);

    Torrent getTorrentById(@NonNull String id);

    Single<Torrent> getTorrentByIdSingle(@NonNull String id);

    Flowable<Torrent> observeTorrentById(@NonNull String id);

    List<Torrent> getAllTorrents();

    void addFastResume(@NonNull FastResume fastResume);

    FastResume getFastResumeById(@NonNull String torrentId);

    void saveSession(@NonNull byte[] data) throws IOException;

    String getSessionFile();
}
