

package org.lntorrent.libretorrent.ui.detailtorrent.pages.peers;

import androidx.annotation.NonNull;

import org.lntorrent.libretorrent.core.model.data.PeerInfo;

public class PeerItem extends PeerInfo
{
    public PeerItem(@NonNull PeerInfo state)
    {
        super(state.ip, state.client, state.totalDownload,
                state.totalUpload, state.relevance, state.connectionType,
                state.port, state.progress, state.downSpeed, state.upSpeed);
    }

    @Override
    public int hashCode()
    {
        return ip.hashCode();
    }

    public boolean equalsContent(Object o)
    {
        return super.equals(o);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof PeerItem))
            return false;

        if (o == this)
            return true;

        return ip.equals(((PeerItem)o).ip);
    }
}
