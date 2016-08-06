package com.spleefleague.bungee.io.connections;

import com.spleefleague.bungee.Bungee;
import com.spleefleague.bungee.events.ConnectionEvent;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONTokener;
import org.json.simple.JSONObject;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Josh on 04/08/2016.
 */
public class ConnectionClient {

    private Socket socket;

    public ConnectionClient() {
        try {
            this.socket = IO.socket("http://127.0.0.1:9092");
        } catch (URISyntaxException e) {
            return;
        }
        socket.on(Socket.EVENT_CONNECT, (Object... args) -> {
            JSONObject send = new JSONObject();
            send.put("name", "Bungee");
            send("connect", send);

            handleReconnect();
        }).on("global", (Object... args) -> {
            if (args.length != 2) {
                return;
            }
            try {
                org.json.JSONObject jsonObject = (org.json.JSONObject) args[0];
                if (jsonObject == null || jsonObject.length() == 0) {
                    jsonObject = new org.json.JSONObject(new JSONTokener(args[1].toString()));
                    if(jsonObject.length() == 0) {
                        return;
                    }
                }
                String channel = jsonObject.getString("channel"), server = jsonObject.getString("server");
                jsonObject.remove("channel");
                jsonObject.remove("server");
                Bungee.getInstance().getProxy().getPluginManager().callEvent(new ConnectionEvent(channel, server, jsonObject));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.socket.connect();
    }

    /**
     * Handle shutdown.
     */
    public void stop() {
        if (socket != null) {
            this.socket.close();
        }
    }

    /**
     * Send a packet via SocketIO.
     *
     * @param channel channel to send the packet on.
     * @param jsonObject json object to send.
     */
    public void send(String channel, JSONObject jsonObject) {
        jsonObject.put("channel", channel);
        jsonObject.put("server", "Bungee");
        this.socket.emit("global", jsonObject);
    }

    private void handleReconnect() {
        Set<JSONObject> toSend = new HashSet<>();
        for (ProxiedPlayer proxiedPlayer : Bungee.getInstance().getProxy().getPlayers()) {
            JSONObject send = new JSONObject();
            send.put("uuid", proxiedPlayer.getUniqueId().toString());
            send.put("username", proxiedPlayer.getName());
            send.put("rank", "DEFAULT");
            send.put("playerServer", "PENDING");
            send.put("action", "ADD_PLAYER");
            toSend.add(send);
        }
        for (JSONObject jsonObject : toSend) {
            send("sessions", jsonObject);
        }
    }

}
