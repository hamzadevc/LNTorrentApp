

package org.lntorrent.libretorrent.core.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import org.libtorrent4j.swig.posix_stat_t;
import org.libtorrent4j.swig.posix_wrapper;

import java.io.FileNotFoundException;

public class LibTorrentSafAdapter extends posix_wrapper
{
    @SuppressWarnings("unused")
    private static final String TAG = LibTorrentSafAdapter.class.getSimpleName();

    private SafFileSystem fs;

    public LibTorrentSafAdapter(@NonNull Context appContext)
    {
        fs = SafFileSystem.getInstance(appContext);
    }

    @Override
    public int open(String path, int flags, int mode)
    {
        if (!SafFileSystem.FakePath.isFakePath(path))
            return super.open(path, flags, mode);

        SafFileSystem.FakePath fakePath = SafFileSystem.FakePath.deserialize(path);
        if (fakePath == null) {
            Log.i(TAG, "open: cannot parse path " + path);
            return -1;
        }

        int ret = fs.openFD(fakePath, "rw");
        if (ret < 0)
            Log.i(TAG, "Failed to create native fd for: " + path);

        return ret;
    }

    @SuppressLint("SdCardPath")
    @Override
    public int stat(String path, posix_stat_t buf)
    {
        if (!SafFileSystem.FakePath.isFakePath(path))
            return super.stat(path, buf);

        SafFileSystem.FakePath fakePath = SafFileSystem.FakePath.deserialize(path);
        if (fakePath == null) {
            Log.i(TAG, "stat: cannot parse path " + path);
            return -1;
        }

        SafFileSystem.Stat stat = fs.stat(fakePath);
        if (stat == null) {
            Log.i(TAG, "Failed to stat file for: " + path);
            /* This trick the posix layer to set the correct errno */
            return super.stat("/data/data/org.proninyaroslav.libretorrent/noexists.txt", buf);
        }

        int S_ISDIR = stat.isDir ? 0040000 : 0;
        int S_IFREG = 0100000;

        buf.setMode(S_ISDIR | S_IFREG);
        buf.setSize(stat.length);
        int t = (int)(stat.lastModified / 1000);
        buf.setAtime(t);
        buf.setMtime(t);
        buf.setCtime(t);

        return 0;
    }

    @Override
    public int mkdir(String path, int mode)
    {
        if (!SafFileSystem.FakePath.isFakePath(path))
            return super.mkdir(path, mode);

        SafFileSystem.FakePath fakePath = SafFileSystem.FakePath.deserialize(path);
        if (fakePath == null) {
            Log.i(TAG, "mkdir: cannot parse path " + path);
            return -1;
        }

        int ret = (fs.mkdirs(fakePath) ? 0 : -1);
        if (ret < 0)
            Log.e(TAG, "Failed to create dir: " + path);

        return ret;
    }

    /*
     * TODO: currently doesn't work, see https://github.com/aldenml/libtorrent4j/issues/45
     */

    @Override
    public int remove(String path)
    {
        if (!SafFileSystem.FakePath.isFakePath(path))
            return super.remove(path);

        SafFileSystem.FakePath fakePath = SafFileSystem.FakePath.deserialize(path);
        if (fakePath == null) {
            Log.i(TAG, "remove: cannot parse path " + path);
            return -1;
        }

        int ret = 0;
        try {
            ret = (fs.delete(fakePath) ? 0 : -1);
            if (ret < 0)
                Log.e(TAG, "Failed to delete file: " + path);

        } catch (FileNotFoundException e) {
            return ret;
        }

        return ret;
    }
}
