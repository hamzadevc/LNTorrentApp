

package org.lntorrent.libretorrent.core.system;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;

public class FakeFileDescriptorWrapper implements FileDescriptorWrapper
{
    private FileDescriptor fd;

    public FakeFileDescriptorWrapper(FileDescriptor fd)
    {
        this.fd = fd;
    }

    @Override
    public FileDescriptor open(@NonNull String mode)
    {
        return fd;
    }

    @Override
    public void close() { }
}
