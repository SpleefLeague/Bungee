package com.spleefleague.bungee;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.spleefleague.bungee.io.connections.ConnectionClient;
import com.spleefleague.bungee.listeners.ConnectionListener;
import com.spleefleague.bungee.listeners.PlayerListener;
import com.spleefleague.bungee.manager.BalancingManager;
import com.spleefleague.bungee.util.BasicReconnectHandler;
import com.spleefleague.bungee.util.Config;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Josh on 04/08/2016.
 */
public class Bungee extends Plugin {

    private static Bungee instance;

    private MongoClient mongoClient;
    private BalancingManager balancingManager;
    private ConnectionClient connectionClient;

    @Override
    public void onEnable() {
        instance = this;
        Config.loadConfig();
        initMongo();

        this.connectionClient = new ConnectionClient();
        this.balancingManager = new BalancingManager();

        registerListeners();
        getProxy().setReconnectHandler(new BasicReconnectHandler());
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
     * Get the active MongoClient.
     *
     * @return MongoClient instance (hopefully connected if nothing went wrong).
     */
    public MongoClient getMongoClient() {
        return mongoClient;
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
     * Get the current BalancingManager.
     * This handles period refreshes etc.
     *
     * @return balancing manager. Shouldn't be null.
     */
    public BalancingManager getBalancingManager() {
        return balancingManager;
    }

    /**
     * Register listeners.
     */
    private void registerListeners() {
        getProxy().getPluginManager().registerListener(this, new ConnectionListener());
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
    }

    /**
     * Initiate a database connection.
     */
    private void initMongo() {
        List<MongoCredential> credentials = Config.getCredentials();
        try {
            ServerAddress address = new ServerAddress(Config.DB_HOST, Config.DB_PORT);
            this.mongoClient = new MongoClient(address, credentials);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

}
