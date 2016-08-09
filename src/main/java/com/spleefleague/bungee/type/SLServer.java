package com.spleefleague.bungee.type;

import com.spleefleague.bungee.Bungee;
import com.spleefleague.bungee.io.DBEntity;
import com.spleefleague.bungee.io.DBLoad;
import com.spleefleague.bungee.io.DBLoadable;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * Created by Josh on 09/08/2016.
 */
public class SLServer extends DBEntity implements DBLoadable {

    @DBLoad(fieldName = "server_name")
    private String serverName;
    @DBLoad(fieldName = "ip")
    private String ip;
    @DBLoad(fieldName = "port")
    private int port;
    @DBLoad(fieldName = "balance")
    private boolean balance;

    private SLServer() {

    }

    public String getServerName() {
        return serverName;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean balance() {
        return balance;
    }

    public ServerInfo getServerInfo() {
        return Bungee.getInstance().getProxy().getServerInfo(getServerName());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SLServer) {
            SLServer slServer = (SLServer) obj;
            return slServer.getServerName().equalsIgnoreCase(getServerName()) && slServer.getIP().equalsIgnoreCase(getIP())
                    && slServer.getPort() == getPort() && slServer.balance() == balance();
        }
        return false;
    }

}
