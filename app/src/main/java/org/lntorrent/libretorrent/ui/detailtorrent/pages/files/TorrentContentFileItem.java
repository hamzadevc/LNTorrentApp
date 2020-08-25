

package org.lntorrent.libretorrent.ui.detailtorrent.pages.files;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lntorrent.libretorrent.core.model.filetree.FilePriority;
import org.lntorrent.libretorrent.core.model.filetree.TorrentContentFileTree;
import org.lntorrent.libretorrent.ui.FileItem;

public class TorrentContentFileItem extends FileItem
{
    public FilePriority priority;
    public long receivedBytes;
    public double availability;

    public TorrentContentFileItem(@NonNull TorrentContentFileTree tree)
    {
        super(tree.getIndex(), tree.getName(), tree.isFile(), tree.size());

        priority = tree.getFilePriority();
        receivedBytes = tree.getReceivedBytes();
        availability = tree.getAvailability();
    }

    public TorrentContentFileItem(Parcel source)
    {
        super(source);

        priority = (FilePriority)source.readSerializable();
        receivedBytes = source.readLong();
        availability = source.readDouble();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);

        dest.writeSerializable(priority);
        dest.writeLong(receivedBytes);
        dest.writeDouble(availability);
    }

    public static final Parcelable.Creator<TorrentContentFileItem> CREATOR =
            new Parcelable.Creator<TorrentContentFileItem>()
            {
                @Override
                public TorrentContentFileItem createFromParcel(Parcel source)
                {
                    return new TorrentContentFileItem(source);
                }

                @Override
                public TorrentContentFileItem[] newArray(int size)
                {
                    return new TorrentContentFileItem[size];
                }
            };

    public boolean equalsContent(@Nullable Object o)
    {
        if (!equals(o))
            return false;

        TorrentContentFileItem item = (TorrentContentFileItem)o;

        return priority.equals(item.priority) &&
                receivedBytes == item.receivedBytes &&
                availability == item.availability;
    }

    @Override
    public String toString()
    {
        return "TorrentContentFileItem{" +
                super.toString() +
                ", receivedBytes=" + receivedBytes +
                ", availability=" + availability +
                '}';
    }
}
