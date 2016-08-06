package com.spleefleague.bungee.util;

import com.spleefleague.bungee.Bungee;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Josh on 04/08/2016.
 */
public class BasicReconnectHandler implements ReconnectHandler {

    @Override
    public ServerInfo getServer(ProxiedPlayer proxiedPlayer) {
        ServerInfo preferred = Bungee.getInstance().getProxy().getServers().get("sl1");
        for (String serverName : Bungee.getInstance().getBalancingServers()) {
            ServerInfo serverInfo = Bungee.getInstance().getProxy().getServers().get(serverName);
            if(serverInfo.getPlayers().size() < preferred.getPlayers().size()) {
                preferred = serverInfo;
            }
        }
        return preferred;
    }

    @Override
    public void setServer(ProxiedPlayer proxiedPlayer) {

    }

    @Override
    public void save() {

    }

    @Override
    public void close() {

    }

}
