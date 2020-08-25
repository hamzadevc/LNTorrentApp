

package org.lntorrent.libretorrent.core.system;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.util.List;

public class FakeFsModule implements FsModule
{
    private List<String> existsFileNames;

    public FakeFsModule(List<String> existsFileNames)
    {
        this.existsFileNames = existsFileNames;
    }

    @Override
    public String getName(@NonNull Uri filePath)
    {
        String path = filePath.getPath();

        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!existsFileNames.contains(fileName))
            return null;

        return fileName;
    }

    @Override
    public String getDirPath(@NonNull Uri dir)
    {
        String path = dir.getPath();

        String dirName = path.substring(path.lastIndexOf("/") + 1);

        if (!existsFileNames.contains(dirName))
            return null;

        return dirName;
    }

    @Override
    public String getFilePath(@NonNull Uri filePath)
    {
        String path = filePath.getPath();

        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!existsFileNames.contains(fileName))
            return null;

        return fileName;
    }

    @Override
    public Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create)
    {
        if (!create && !existsFileNames.contains(fileName))
            return null;

        return Uri.parse("file://" + dir.getPath() + "/" + fileName);
    }

    @Override
    public Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir)
    {
        String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);
        if (!existsFileNames.contains(fileName))
            return null;

        return Uri.parse("file://" + dir.getPath() + "/" + relativePath);
    }

    @Override
    public boolean delete(@NonNull Uri filePath)
    {
        return true;
    }

    @Override
    public FileDescriptorWrapper openFD(@NonNull Uri path)
    {
        return new FakeFileDescriptorWrapper(FileDescriptor.out);
    }

    @Override
    public long getDirAvailableBytes(@NonNull Uri dir)
    {
        return -1;
    }

    @Override
    public boolean fileExists(@NonNull Uri filePath)
    {
        String path = filePath.getPath();

        String fileName = path.substring(path.lastIndexOf("/") + 1);

        return existsFileNames.contains(fileName);
    }

    @Override
    public long lastModified(@NonNull Uri filePath)
    {
        return System.currentTimeMillis();
    }

    @Override
    public String makeFileSystemPath(@NonNull Uri uri, String relativePath)
    {
        String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);
        if (!existsFileNames.contains(fileName))
            return null;

        return uri.getPath() + "/" + relativePath;
    }

    @Override
    public Uri getParentDirUri(@NonNull Uri filePath)
    {
        throw new UnsupportedOperationException();
    }
}
