

package org.lntorrent.libretorrent.core.exception;

public class FileAlreadyExistsException extends Exception
{
    public FileAlreadyExistsException() { }

    public FileAlreadyExistsException(String message)
    {
        super(message);
    }
}
