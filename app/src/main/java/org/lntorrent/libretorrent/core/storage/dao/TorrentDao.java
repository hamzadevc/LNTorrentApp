

package org.lntorrent.libretorrent.core.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.lntorrent.libretorrent.core.model.data.entity.Torrent;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TorrentDao
{
    String QUERY_GET_ALL = "SELECT * FROM Torrent";
    String QUERY_GET_BY_ID = "SELECT * FROM Torrent WHERE id = :id";

    @Insert
    void add(Torrent torrent);

    @Update
    void update(Torrent torrent);

    @Delete
    void delete(Torrent torrent);

    @Query(QUERY_GET_ALL)
    List<Torrent> getAllTorrents();

    @Query(QUERY_GET_BY_ID)
    Torrent getTorrentById(String id);

    @Query(QUERY_GET_BY_ID)
    Single<Torrent> getTorrentByIdSingle(String id);

    @Query(QUERY_GET_BY_ID)
    Flowable<Torrent> observeTorrentById(String id);
}
