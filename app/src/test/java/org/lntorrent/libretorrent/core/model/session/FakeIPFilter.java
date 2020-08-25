

package org.lntorrent.libretorrent.core.model.session;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import org.lntorrent.libretorrent.core.exception.IPFilterException;

import java.util.ArrayList;
import java.util.List;

class FakeIPFilter implements IPFilter
{
    private ArrayList<Pair<String, String>> ranges = new ArrayList<>();

    @Override
    public void addRange(@NonNull String first, @NonNull String last) throws IPFilterException
    {
        ranges.add(Pair.create(first, last));
    }

    List<Pair<String, String>> getRanges()
    {
        return ranges;
    }
}
