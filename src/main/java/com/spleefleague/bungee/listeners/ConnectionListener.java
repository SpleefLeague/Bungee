package com.spleefleague.bungee.listeners;

import com.spleefleague.bungee.Bungee;
import com.spleefleague.bungee.events.ConnectionEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.JSONException;

/**
 * Created by Josh on 09/08/2016.
 */
public class ConnectionListener implements Listener {

    @EventHandler
    public void onConnection(ConnectionEvent e) throws JSONException {
        if(e.getChannel().equalsIgnoreCase("rotation")) {
            if(!e.getJSONObject().has("action")) {
                return;
            }
            switch (e.getJSONObject().getString("action").toUpperCase()) {
                case "REFRESH": {
                    Bungee.getInstance().getLogger().info("Refreshing servers...");
                    Bungee.getInstance().getProxy().getScheduler().runAsync(Bungee.getInstance(), () -> {
                        Bungee.getInstance().getBalancingManager().loadFromDB();
                    });
                    break;
                }
            }
        }
    }

}
