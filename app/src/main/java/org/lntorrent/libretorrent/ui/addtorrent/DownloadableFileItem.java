

package org.lntorrent.libretorrent.ui.addtorrent;

import android.os.Parcel;

import androidx.annotation.NonNull;

import org.lntorrent.libretorrent.core.model.filetree.BencodeFileTree;
import org.lntorrent.libretorrent.ui.FileItem;

public class DownloadableFileItem extends FileItem
{
    public boolean selected;

    public DownloadableFileItem(@NonNull BencodeFileTree tree)
    {
        super(tree.getIndex(), tree.getName(), tree.isFile(), tree.size());

        selected = tree.isSelected();
    }

    public DownloadableFileItem(Parcel source)
    {
        super(source);

        selected = source.readByte() != 0;
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

        dest.writeByte((byte)(selected ? 1 : 0));
    }

    public static final Creator<DownloadableFileItem> CREATOR =
            new Creator<DownloadableFileItem>()
            {
                @Override
                public DownloadableFileItem createFromParcel(Parcel source)
                {
                    return new DownloadableFileItem(source);
                }

                @Override
                public DownloadableFileItem[] newArray(int size)
                {
                    return new DownloadableFileItem[size];
                }
            };

    @Override
    public String toString()
    {
        return "DownloadableFileItem{" +
                super.toString() +
                "selected=" + selected +
                '}';
    }
}
