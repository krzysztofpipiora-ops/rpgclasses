package com.example.rpgaspects;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

        PlayerData data = plugin.getPlayerData(player);

        if (args.length == 0) {
            player.sendMessage("§6=== SYSTEM ASPEKTOW RPG ===");
            player.sendMessage("§eTwor Poziom RPG: §f" + data.getLevel());
            player.sendMessage("§eAktywny Aspekt: §a" + data.getCurrentAspect().getName());
            player.sendMessage("§7Wpisz: §b/aspekt wybierz [NAZWA]§7 aby zmienic klase.");
            player.sendMessage("§7Dostepne aspekty:");
            for (PlayerData.AspectType type : PlayerData.AspectType.values()) {
                if (type == PlayerData.AspectType.NONE) continue;
                player.sendMessage("§e- §f" + type.name() + " §7(Wymaga poziomu: " + type.getMinLevel() + ") - " + type.getDescription());
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("wybierz") && args.length > 1) {
            String wybrany = args[1].toUpperCase();

            try {
                PlayerData.AspectType aspect = PlayerData.AspectType.valueOf(wybrany);
                
                if (data.getLevel() < aspect.getMinLevel()) {
                    player.sendMessage("§cNie posiadasz wystarczajacego poziomu! Wymagany poziom: " + aspect.getMinLevel());
                    return true;
                }

                data.setCurrentAspect(aspect);
                player.sendMessage("§aZmieniono aspekt na: §2" + aspect.getName());
                player.sendMessage("§7Zdolnosc: " + aspect.getDescription());

            } catch (IllegalArgumentException e) {
                player.sendMessage("§cNiepoprawna nazwa aspektu! Sprawdz liste pod /aspekt");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("setlevel") && args.length > 1 && player.isOp()) {
            try {
                int targetLevel = Integer.parseInt(args[1]);
                data.setLevel(targetLevel);
                player.sendMessage("§a[Cheat] Ustawiono Twoj poziom na: " + targetLevel);
            } catch (NumberFormatException e) {
                player.sendMessage("§cPodaj poprawna liczbe.");
            }
            return true;
        }

        return true;
    }
}
