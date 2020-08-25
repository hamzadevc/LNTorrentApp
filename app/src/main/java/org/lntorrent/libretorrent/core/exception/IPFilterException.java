

package org.lntorrent.libretorrent.core.exception;

public class IPFilterException extends Exception
{
    public IPFilterException() { }

    public IPFilterException(String message)
    {
        super(message);
    }

    public IPFilterException(Exception e)
    {
        super(e);
    }
}
