

package org.lntorrent.libretorrent.core.system;

import android.content.Context;

import androidx.annotation.NonNull;

public class SystemFacadeHelper
{
    private static SystemFacade systemFacade;
    private static FileSystemFacade fileSystemFacade;

    public synchronized static SystemFacade getSystemFacade(@NonNull Context appContext)
    {
        if (systemFacade == null)
            systemFacade = new SystemFacadeImpl(appContext);

        return systemFacade;
    }

    public synchronized static FileSystemFacade getFileSystemFacade(@NonNull Context appContext)
    {
        if (fileSystemFacade == null)
            fileSystemFacade = new FileSystemFacadeImpl(appContext,
                    new FsModuleResolverImpl(appContext));

        return fileSystemFacade;
    }
}
