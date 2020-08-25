

package org.lntorrent.libretorrent.core.storage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.lntorrent.libretorrent.core.model.data.entity.FastResume;
import org.lntorrent.libretorrent.core.model.data.entity.FeedChannel;
import org.lntorrent.libretorrent.core.model.data.entity.FeedItem;
import org.lntorrent.libretorrent.core.model.data.entity.Torrent;
import org.lntorrent.libretorrent.core.storage.converter.UriConverter;
import org.lntorrent.libretorrent.core.storage.dao.FastResumeDao;
import org.lntorrent.libretorrent.core.storage.dao.FeedDao;
import org.lntorrent.libretorrent.core.storage.dao.TorrentDao;

@Database(entities = {Torrent.class,
        FastResume.class,
        FeedChannel.class,
        FeedItem.class},
        version = 6)
@TypeConverters({UriConverter.class})

public abstract class AppDatabase extends RoomDatabase
{
    private static final String DATABASE_NAME = "libretorrent.db";

    private static volatile AppDatabase INSTANCE;

    public abstract TorrentDao torrentDao();

    public abstract FastResumeDao fastResumeDao();

    public abstract FeedDao feedDao();

    public static AppDatabase getInstance(@NonNull Context appContext)
    {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null)
                    INSTANCE = buildDatabase(appContext);
            }
        }

        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context appContext)
    {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addMigrations(DatabaseMigration.getMigrations(appContext))
                .build();
    }
}