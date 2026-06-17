package com.example.rpgaspects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public final class RpgAspects extends JavaPlugin {

    private final HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new AspectListener(this), this);
        
        if (this.getCommand("aspekt") != null) {
            this.getCommand("aspekt").setExecutor(new AspectCommand(this));
        }
        
        getLogger().info("RpgAspects zostal pomyslnie wlaczony na wersji 1.21.4!");
    }

    @Override
    public void onDisable() {
        playerDataMap.clear();
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData());
    }
}
