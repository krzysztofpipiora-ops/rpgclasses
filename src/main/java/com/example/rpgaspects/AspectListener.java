package com.example.rpgaspects;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegenEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AspectListener implements Listener {

    private final RpgAspects plugin;
    private final Random random = new Random();
    private final String guiTitle = "§6§lWybierz swój Aspekt";

    public AspectListener(RpgAspects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> openAspectGUI(player), 5L);
    }

    public void openAspectGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, Component.text(guiTitle));

        gui.setItem(1, createGuiItem(Material.NETHERITE_AXE, "§c§lBERSERKER", "§7+20% obrazen. Otrzymujesz 10% wiecej ran.", "§cBlokada: Brak tarczy."));
        gui.setItem(2, createGuiItem(Material.PHANTOM_MEMBRANE, "§8§lCIEŃ", "§715% na unik w nocy. +25% obrazen ze skradania.", "§cKara: Otrzymujesz 15% wiecej ran za dnia."));
        gui.setItem(3, createGuiItem(Material.NETHERITE_CHESTPLATE, "§e§lPALADYN", "§7Otrzymujesz 15% mniej obrazen od wszystkiego.", "§cKara: Posiadasz staly efekt Spowolnienia I."));
        gui.setItem(4, createGuiItem(Material.BOW, "§2§lŁOWCA", "§7Strzaly nakladaja spowolnienie na 2s.", "§cKara: Zadajesz 20% mniej obrazen wrecz."));
        gui.setItem(5, createGuiItem(Material.BREWING_STAND, "§d§lALCHEMIK", "§7Mikstury trwaja 50% dluzej.", "§cSlabosc: Perly endu zadaja 2x wiecej ran."));
        gui.setItem(6, createGuiItem(Material.REDSTONE, "§4§lWAMPIR", "§7Leczysz sie o 12% zadanych obrazen.", "§cKara: Regeneracja z jedzenia slabsza o 50%."));

        player.openInventory(gui);
    }

    private ItemStack createGuiItem(Material material, String name, String lore1, String lore2) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text(lore1));
            lore.add(Component.text(lore2));
            meta.lore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text(guiTitle))) {
            event.setCancelled(true);

            if (!(event.getWhoClicked() instanceof Player player)) return;
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            PlayerData data = plugin.getPlayerData(player);
            Material type = clickedItem.getType();

            // Usuniecie efektu spowolnienia przed nadaniem nowego aspektu
            player.removePotionEffect(PotionEffectType.SLOWNESS);

            if (type == Material.NETHERITE_AXE) data.setCurrentAspect(PlayerData.AspectType.BERSERKER);
            else if (type == Material.PHANTOM_MEMBRANE) data.setCurrentAspect(PlayerData.AspectType.CIEŃ);
            else if (type == Material.NETHERITE_CHESTPLATE) {
                data.setCurrentAspect(PlayerData.AspectType.PALADYN);
                // Paladyn otrzymuje spowolnienie przez efekt (zamiast zabugowanego atrybutu)
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 0, false, false, true));
            }
            else if (type == Material.BOW) data.setCurrentAspect(PlayerData.AspectType.ŁOWCA);
            else if (type == Material.BREWING_STAND) data.setCurrentAspect(PlayerData.AspectType.ALCHEMIK);
            else if (type == Material.REDSTONE) data.setCurrentAspect(PlayerData.AspectType.WAMPIR);

            player.sendMessage("§aWybrano aspekt: §2§l" + data.getCurrentAspect().getName());
            player.closeInventory();
            return;
        }

        if (event.getWhoClicked() instanceof Player player) {
            PlayerData data = plugin.getPlayerData(player);
            if (data.getCurrentAspect() == PlayerData.AspectType.BERSERKER) {
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SHIELD) {
                    event.setCancelled(true);
                    player.sendMessage("§cAspekt Berserkera uniemozliwia korzystanie z tarcz!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        // --- OFENSYWA ---
        if (event.getDamager() instanceof Player attacker) {
            PlayerData attackerData = plugin.getPlayerData(attacker);
            
            switch (attackerData.getCurrentAspect()) {
                case BERSERKER -> event.setDamage(event.getDamage() * 1.20);
                case CIEŃ -> {
                    if (attacker.isSneaking()) {
                        event.setDamage(event.getDamage() * 1.25);
                        attacker.sendMessage("§8[Cien] Atak z ukrycia!");
                    }
                }
                case ŁOWCA -> {
                    if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        event.setDamage(event.getDamage() * 0.80);
                    }
                }
                case WAMPIR -> {
                    double healAmount = event.getFinalDamage() * 0.12;
                    double newHealth = Math.min(attacker.getHealth() + healAmount, 20.0);
                    attacker.setHealth(newHealth);
                }
                default -> {}
            }
        }

        // --- STRZAŁY ŁOWCY ---
        if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter) {
            PlayerData shooterData = plugin.getPlayerData(shooter);
            if (shooterData.getCurrentAspect() == PlayerData.AspectType.ŁOWCA && event.getEntity() instanceof Player victim) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 0));
            }
        }

        // --- DEFENSYWA ---
        if (event.getEntity() instanceof Player victim) {
            PlayerData victimData = plugin.getPlayerData(victim);

            switch (victimData.getCurrentAspect()) {
                case PALADYN -> event.setDamage(event.getDamage() * 0.85);
                case BERSERKER -> event.setDamage(event.getDamage() * 1.10);
                case CIEŃ -> {
                    long time = victim.getWorld().getTime();
                    boolean isNight = time >= 13000 && time <= 23000;
                    if (isNight) {
                        if (random.nextDouble() < 0.15) {
                            event.setCancelled(true);
                            victim.sendMessage("§8[Cien] Unik!");
                        }
                    } else {
                        // Kara Cienia za dnia: otrzymuje 15% wiecej obrazen (zamiast modyfikatora pancerza)
                        event.setDamage(event.getDamage() * 1.15);
                    }
                }
                default -> {}
            }
        }
    }

    @EventHandler
    public void onEnvironmentalDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerData data = plugin.getPlayerData(player);
            if (data.getCurrentAspect() == PlayerData.AspectType.ALCHEMIK && event.getCause() == EntityDamageEvent.DamageCause.ENDER_PEARL) {
                event.setDamage(event.getDamage() * 2.0);
            }
        }
    }

    @EventHandler
    public void onPlayerRegen(EntityRegenEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerData data = plugin.getPlayerData(player);
            if (data.getCurrentAspect() == PlayerData.AspectType.WAMPIR && event.getRegenReason() == EntityRegenEvent.RegenReason.SATIATED) {
                if (random.nextBoolean()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
