package com.spleefleague.bungee.listeners;

import com.spleefleague.bungee.Bungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.json.simple.JSONObject;

/**
 * Created by Josh on 04/08/2016.
 */
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PostLoginEvent e) {
        JSONObject toSend = new JSONObject();
        toSend.put("uuid", e.getPlayer().getUniqueId().toString());
        toSend.put("username", e.getPlayer().getName());
        toSend.put("rank", "DEFAULT");
        toSend.put("playerServer", "PENDING");
        toSend.put("action", "ADD_PLAYER");
        Bungee.getInstance().getConnectionClient().send("sessions", toSend);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerDisconnectEvent e) {
        JSONObject toSend = new JSONObject();
        toSend.put("uuid", e.getPlayer().getUniqueId().toString());
        toSend.put("action", "REMOVE_PLAYER");
        Bungee.getInstance().getConnectionClient().send("sessions", toSend);
    }

}
