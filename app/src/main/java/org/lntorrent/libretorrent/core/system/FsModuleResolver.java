

package org.lntorrent.libretorrent.core.system;

import android.net.Uri;

import androidx.annotation.NonNull;

/*
 * An FsModule provider.
 */

interface FsModuleResolver
{
    FsModule resolveFsByUri(@NonNull Uri uri);
}
