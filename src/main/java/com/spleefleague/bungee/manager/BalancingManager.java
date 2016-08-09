package com.spleefleague.bungee.manager;

import com.spleefleague.bungee.Bungee;
import com.spleefleague.bungee.io.EntityBuilder;
import com.spleefleague.bungee.type.SLServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Josh on 09/08/2016.
 */
public class BalancingManager {

    public static final String DATABASE = "SpleefLeague", COLLECTION = "Servers";
    public static final int MAX_ON_SERVER = 50;

    private final LinkedList<SLServer> balancingServers;

    public BalancingManager() {
        this.balancingServers = new LinkedList<>();

        Bungee.getInstance().getProxy().getServers().clear();
        Bungee.getInstance().getProxy().getScheduler().schedule(Bungee.getInstance(), this::loadFromDB, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Load all servers from the database (handling those that are no longer in rotation).
     */
    public void loadFromDB() {
        LinkedList<SLServer> before = new LinkedList<>(balancingServers), after = new LinkedList<>(), difference = new LinkedList<>();
        Bungee.getInstance().getMongoClient().getDatabase(DATABASE).getCollection(COLLECTION)
                .find(new Document("balance", true)).forEach((Consumer<? super Document>) (Document document) -> {
            after.add(EntityBuilder.load(document, SLServer.class));
        });
        difference.addAll(after.stream().filter(slServer -> !before.contains(slServer)).collect(Collectors.toList()));
        difference.addAll(before.stream().filter(slServer -> !after.contains(slServer)).collect(Collectors.toList()));
        difference.stream().forEach((SLServer slServer) -> {
            if(slServer.getServerInfo() != null) {
                TextComponent kickComponent = new TextComponent("Your server has been removed from rotation! Please reconnect.");
                kickComponent.setColor(ChatColor.RED);
                slServer.getServerInfo().getPlayers().forEach((ProxiedPlayer proxiedPlayer) -> {
                    proxiedPlayer.disconnect(kickComponent);
                });
                Bungee.getInstance().getProxy().getServers().remove(slServer.getServerName());
            } else {
                try {
                    Bungee.getInstance().getProxy().getServers().put(slServer.getServerName(), Bungee.getInstance().getProxy()
                            .constructServerInfo(slServer.getServerName(), new InetSocketAddress(InetAddress.getByName(slServer.getIP()), slServer.getPort()), slServer.getServerName(), false));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
        balancingServers.clear();
        balancingServers.addAll(after);
    }

    /**
     * Get the next server to send players (balance) to.
     *
     * @return ServerInfo if all went well, null if not.
     */
    public ServerInfo getNext() {
        for (SLServer balancingServer : balancingServers) {
            if(balancingServer.getServerInfo() != null && balancingServer.getServerInfo().getPlayers().size() < MAX_ON_SERVER) {
                return balancingServer.getServerInfo();
            }
        }
        // Oh my, all are over 50! Lets find the one with the least players.
        SLServer preferred = balancingServers.get(0);
        for (SLServer balancingServer : balancingServers) {
            if(balancingServer.getServerInfo() == null) {
                continue;
            }
            if(preferred == null || preferred.getServerInfo() == null
                    || balancingServer.getServerInfo().getPlayers().size() < preferred.getServerInfo().getPlayers().size()) {
                preferred = balancingServer;
            }
        }
        return preferred.getServerInfo();
    }

}
