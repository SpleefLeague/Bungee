package com.spleefleague.bungee.events;

import net.md_5.bungee.api.plugin.Event;
import org.json.JSONObject;

/**
 * Created by Josh on 04/08/2016.
 */
public class ConnectionEvent extends Event {

    private final String channel, server;
    private final JSONObject jsonObject;

    public ConnectionEvent(String channel, String server, JSONObject jsonObject) {
        this.channel = channel;
        this.server = server;
        this.jsonObject = jsonObject;
    }

    public String getChannel() {
        return channel;
    }

    public String getOriginatingServer() {
        return server;
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }

}
