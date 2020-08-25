

package org.lntorrent.libretorrent.core.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.lntorrent.libretorrent.core.model.data.Priority;

import java.util.Arrays;

public class AddTorrentParams implements Parcelable
{
    /* File path or magnet link */
    @NonNull
    public String source;
    public boolean fromMagnet;
    @NonNull
    public String sha1hash;
    @NonNull
    public String name;
    /* Optional field (e.g. if file information is available) */
    public Priority[] filePriorities;
    @NonNull
    public Uri downloadPath;
    public boolean sequentialDownload;
    public boolean addPaused;

    public AddTorrentParams(@NonNull String source,
                            boolean fromMagnet,
                            @NonNull String sha1hash,
                            @NonNull String name,
                            Priority[] filePriorities,
                            @NonNull Uri downloadPath,
                            boolean sequentialDownload,
                            boolean addPaused)
    {
        this.source = source;
        this.fromMagnet = fromMagnet;
        this.sha1hash = sha1hash;
        this.name = name;
        this.filePriorities = filePriorities;
        this.downloadPath = downloadPath;
        this.sequentialDownload = sequentialDownload;
        this.addPaused = addPaused;
    }

    public AddTorrentParams(Parcel source)
    {
        this.source = source.readString();
        fromMagnet = source.readByte() != 0;
        sha1hash = source.readString();
        name = source.readString();
        filePriorities = (Priority[])source.readArray(Priority.class.getClassLoader());
        downloadPath = source.readParcelable(Uri.class.getClassLoader());
        sequentialDownload = source.readByte() != 0;
        addPaused = source.readByte() != 0;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(source);
        dest.writeByte((byte)(fromMagnet ? 1 : 0));
        dest.writeString(sha1hash);
        dest.writeString(name);
        dest.writeArray(filePriorities);
        dest.writeParcelable(downloadPath, flags);
        dest.writeByte((byte) (sequentialDownload ? 1 : 0));
        dest.writeByte((byte) (addPaused ? 1 : 0));
    }

    public static final Parcelable.Creator<AddTorrentParams> CREATOR =
            new Parcelable.Creator<AddTorrentParams>()
            {
                @Override
                public AddTorrentParams createFromParcel(Parcel source)
                {
                    return new AddTorrentParams(source);
                }

                @Override
                public AddTorrentParams[] newArray(int size)
                {
                    return new AddTorrentParams[size];
                }
            };


    @Override
    public int hashCode()
    {
        return sha1hash.hashCode();
    }

    @Override
    public String toString()
    {
        return "AddTorrentMutableParams{" +
                "source='" + source + '\'' +
                ", fromMagnet=" + fromMagnet +
                ", sha1hash='" + sha1hash + '\'' +
                ", name='" + name + '\'' +
                ", filePriorities=" + Arrays.toString(filePriorities) +
                ", downloadPath=" + downloadPath +
                ", sequentialDownload=" + sequentialDownload +
                ", addPaused=" + addPaused +
                '}';
    }
}
