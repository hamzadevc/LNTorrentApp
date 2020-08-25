

package org.lntorrent.libretorrent.core.logger;

public interface LogFilter
{
    boolean apply(LogEntry entry);
}
