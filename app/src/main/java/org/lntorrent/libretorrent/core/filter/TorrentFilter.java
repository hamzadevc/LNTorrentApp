

package org.lntorrent.libretorrent.core.filter;

import org.lntorrent.libretorrent.core.model.data.TorrentInfo;

import io.reactivex.functions.Predicate;

public interface TorrentFilter extends Predicate<TorrentInfo> {}
