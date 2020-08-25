

package org.lntorrent.libretorrent.core.system;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.List;

public class FakeFsModuleResolver implements FsModuleResolver
{
    public List<String> existsFileNames;

    @Override
    public FsModule resolveFsByUri(@NonNull Uri uri)
    {
        return new FakeFsModule(existsFileNames);
    }
}
