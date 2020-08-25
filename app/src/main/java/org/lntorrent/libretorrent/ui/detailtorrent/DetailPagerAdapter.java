

package org.lntorrent.libretorrent.ui.detailtorrent;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.lntorrent.libretorrent.R;
import org.lntorrent.libretorrent.ui.detailtorrent.pages.DetailTorrentInfoFragment;
import org.lntorrent.libretorrent.ui.detailtorrent.pages.DetailTorrentStateFragment;
import org.lntorrent.libretorrent.ui.detailtorrent.pages.files.DetailTorrentFilesFragment;
import org.lntorrent.libretorrent.ui.detailtorrent.pages.peers.DetailTorrentPeersFragment;
import org.lntorrent.libretorrent.ui.detailtorrent.pages.pieces.DetailTorrentPiecesFragment;
import org.lntorrent.libretorrent.ui.detailtorrent.pages.trackers.DetailTorrentTrackersFragment;

public class DetailPagerAdapter extends FragmentStatePagerAdapter
{
    public static final int NUM_FRAGMENTS = 6;
    public static final int INFO_FRAG_POS = 0;
    public static final int STATE_FRAG_POS = 1;
    public static final int FILES_FRAG_POS = 2;
    public static final int TRACKERS_FRAG_POS = 3;
    public static final int PEERS_FRAG_POS = 4;
    public static final int PIECES_FRAG_POS = 5;

    private Context context;

    public DetailPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position) {
            case INFO_FRAG_POS:
                return context.getString(R.string.torrent_info);
            case STATE_FRAG_POS:
                return context.getString(R.string.torrent_state);
            case FILES_FRAG_POS:
                return context.getString(R.string.torrent_files);
            case TRACKERS_FRAG_POS:
                return context.getString(R.string.torrent_trackers);
            case PEERS_FRAG_POS:
                return context.getString(R.string.torrent_peers);
            case PIECES_FRAG_POS:
                return context.getString(R.string.torrent_pieces);
            default:
                return null;
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case INFO_FRAG_POS:
                return DetailTorrentInfoFragment.newInstance();
            case STATE_FRAG_POS:
                return DetailTorrentStateFragment.newInstance();
            case FILES_FRAG_POS:
                return DetailTorrentFilesFragment.newInstance();
            case TRACKERS_FRAG_POS:
                return DetailTorrentTrackersFragment.newInstance();
            case PEERS_FRAG_POS:
                return DetailTorrentPeersFragment.newInstance();
            case PIECES_FRAG_POS:
                return DetailTorrentPiecesFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount()
    {
        return NUM_FRAGMENTS;
    }
}
