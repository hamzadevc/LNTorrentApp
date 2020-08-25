

package org.lntorrent.libretorrent.core.filter;

import org.lntorrent.libretorrent.core.model.data.TorrentStateCode;
import org.lntorrent.libretorrent.core.utils.DateUtils;

public class TorrentFilterCollection
{
    public static TorrentFilter all()
    {
        return (state) -> true;
    }

    public static TorrentFilter statusDownloading()
    {
        return (state) -> state.stateCode == TorrentStateCode.DOWNLOADING;
    }

    public static TorrentFilter statusDownloaded()
    {
        return (state) -> state.stateCode == TorrentStateCode.SEEDING || state.receivedBytes == state.totalBytes;
    }

    public static TorrentFilter statusDownloadingMetadata()
    {
        return (state) -> state.stateCode == TorrentStateCode.DOWNLOADING_METADATA;
    }

    public static TorrentFilter statusError()
    {
        return (state) -> state.error != null;
    }

    public static TorrentFilter dateAddedToday()
    {
        return (state) -> {
            long dateAdded = state.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfToday(timeMillis) &&
                    dateAdded <= DateUtils.endOfToday(timeMillis);
        };
    }

    public static TorrentFilter dateAddedYesterday()
    {
        return (state) -> {
            long dateAdded = state.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfYesterday(timeMillis) &&
                    dateAdded <= DateUtils.endOfYesterday(timeMillis);
        };
    }

    public static TorrentFilter dateAddedWeek()
    {
        return (state) -> {
            long dateAdded = state.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfWeek(timeMillis) &&
                    dateAdded <= DateUtils.endOfWeek(timeMillis);
        };
    }

    public static TorrentFilter dateAddedMonth()
    {
        return (state) -> {
            long dateAdded = state.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfMonth(timeMillis) &&
                    dateAdded <= DateUtils.endOfMonth(timeMillis);
        };
    }

    public static TorrentFilter dateAddedYear()
    {
        return (state) -> {
            long dateAdded = state.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfYear(timeMillis) &&
                    dateAdded <= DateUtils.endOfYear(timeMillis);
        };
    }
}
