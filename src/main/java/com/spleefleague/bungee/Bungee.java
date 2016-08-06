package com.spleefleague.bungee;

import com.spleefleague.bungee.io.connections.ConnectionClient;
import com.spleefleague.bungee.listeners.PlayerListener;
import com.spleefleague.bungee.util.BasicReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Josh on 04/08/2016.
 */
public class Bungee extends Plugin {

    private static Bungee instance;

    private List<String> balancingServers;
    private ConnectionClient connectionClient;

    @Override
    public void onEnable() {
        instance = this;

        this.connectionClient = new ConnectionClient();
        this.balancingServers = new ArrayList<>();

        getProxy().setReconnectHandler(new BasicReconnectHandler());
        initBalancingCache();
        registerListeners();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * Get plugin instance.
     *
     * @return Bungee instance. Shouldn't be null unless not loaded yet.
     */
    public static Bungee getInstance() {
        return instance;
    }

    /**
     * Get connection client.
     *
     * @return ConnectionClient instance. Shouldn't be null unless not loaded yet.
     */
    public ConnectionClient getConnectionClient() {
        return connectionClient;
    }

    /**
     * Get a list of servers that we can balance to.
     *
     * @return list of balancing servers.
     */
    public List<String> getBalancingServers() {
        return balancingServers;
    }

    /**
     * Register listeners.
     */
    private void registerListeners() {
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
    }

    /**
     * Start the balancing cache timer.
     */
    private void initBalancingCache() {
        getProxy().getScheduler().schedule(this, () -> {
            this.balancingServers.clear();
            getProxy().getServers().values().stream().filter((ServerInfo serverInfo) -> serverInfo.getName().toLowerCase().startsWith("sl")).forEach((ServerInfo serverInfo) -> {
                this.balancingServers.add(serverInfo.getName());
            });
        }, 0, 1, TimeUnit.MINUTES);
    }

}
