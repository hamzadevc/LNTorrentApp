

package org.lntorrent.libretorrent.core.system;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

class FileDescriptorWrapperImpl implements FileDescriptorWrapper
{
    private ContentResolver contentResolver;
    private Uri path;
    private ParcelFileDescriptor pfd;

    public FileDescriptorWrapperImpl(@NonNull Context appContext, @NonNull Uri path)
    {
        contentResolver = appContext.getContentResolver();
        this.path = path;
    }

    @Override
    public FileDescriptor open(@NonNull String mode) throws FileNotFoundException
    {
        pfd = contentResolver.openFileDescriptor(path, mode);

        return (pfd == null ? null : pfd.getFileDescriptor());
    }

    @Override
    public void close() throws IOException
    {
        if (pfd != null)
            pfd.close();
    }
}
