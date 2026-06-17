package com.example.rpgaspects;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Bukkit行政;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class AspectCommand implements CommandExecutor {

    private final RpgAspects plugin;

    public AspectCommand(RpgAspects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Ta komenda moze byc wykonana tylko przez gracza!");
            return true;
        }

        // Pobieramy instancję listenera, aby wywołać otwarcie stworzonego GUI
        AspectListener listener = new AspectListener(plugin);
        listener.openAspectGUI(player);
        
        return true;
    }
}
