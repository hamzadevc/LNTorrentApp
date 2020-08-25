

package org.lntorrent.libretorrent.core.model.session;

import androidx.annotation.NonNull;

import org.lntorrent.libretorrent.core.exception.IPFilterException;

interface IPFilter
{
    void addRange(@NonNull String first, @NonNull String last) throws IPFilterException;
}
