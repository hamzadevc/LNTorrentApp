

package org.lntorrent.libretorrent.core.model.session;

import org.lntorrent.libretorrent.core.settings.SessionSettings;

public class SessionInitParams
{
    public int portRangeFirst;
    public int portRangeSecond;
    public SessionSettings.ProxyType proxyType;
    public String proxyAddress;
    public int proxyPort;
    public boolean proxyPeersToo;
    public boolean proxyRequiresAuth;
    public String proxyLogin;
    public String proxyPassword;

    public SessionInitParams()
    {
        portRangeFirst = SessionSettings.DEFAULT_PORT_RANGE_FIRST;
        portRangeSecond = SessionSettings.DEFAULT_PORT_RANGE_SECOND;
        proxyType = SessionSettings.DEFAULT_PROXY_TYPE;
        proxyAddress = SessionSettings.DEFAULT_PROXY_ADDRESS;
        proxyPort = SessionSettings.DEFAULT_PROXY_PORT;
        proxyPeersToo = SessionSettings.DEFAULT_PROXY_PEERS_TOO;
        proxyRequiresAuth = SessionSettings.DEFAULT_PROXY_REQUIRES_AUTH;
        proxyLogin = SessionSettings.DEFAULT_PROXY_LOGIN;
        proxyPassword = SessionSettings.DEFAULT_PROXY_PASSWORD;
    }
}
