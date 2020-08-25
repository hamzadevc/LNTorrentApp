

package org.lntorrent.libretorrent.ui.addtorrent;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.lntorrent.libretorrent.R;

public class AddTorrentPagerAdapter extends FragmentStatePagerAdapter
{
    public static final int NUM_FRAGMENTS = 2;
    public static final int INFO_FRAG_POS = 0;
    public static final int FILES_FRAG_POS = 1;

    private Context context;

    public AddTorrentPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position) {
            case INFO_FRAG_POS:
                return context.getString(R.string.torrent_info);
            case FILES_FRAG_POS:
                return context.getString(R.string.torrent_files);
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
                return AddTorrentInfoFragment.newInstance();
            case FILES_FRAG_POS:
                return AddTorrentFilesFragment.newInstance();
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
