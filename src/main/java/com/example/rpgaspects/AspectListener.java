package com.example.rpgaspects;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Random;

public class AspectListener implements Listener {

    private final RpgAspects plugin;
    private final Random random = new Random();

    public AspectListener(RpgAspects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker) {
            PlayerData attackerData = plugin.getPlayerData(attacker);
            
            switch (attackerData.getCurrentAspect()) {
                case BERSERKER -> {
                    double hpPercentage = attacker.getHealth() / attacker.getMaxHealth();
                    if (hpPercentage < 0.5) {
                        event.setDamage(event.getDamage() * 1.25);
                    }
                }
                case CIEŃ -> {
                    if (attacker.isSneaking()) {
                        event.setDamage(event.getDamage() * 1.30);
                        attacker.sendMessage("§8[Cien] Cios z ukrycia!");
                    }
                }
                case ŁOWCA -> {
                    if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        event.setDamage(event.getDamage() * 0.80);
                    }
                }
                case WAMPIR -> {
                    double healAmount = event.getFinalDamage() * 0.15;
                    double newHealth = Math.min(attacker.getHealth() + healAmount, attacker.getMaxHealth());
                    attacker.setHealth(newHealth);
                }
                default -> {}
            }
        }

        if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter) {
            PlayerData shooterData = plugin.getPlayerData(shooter);
            if (shooterData.getCurrentAspect() == PlayerData.AspectType.ŁOWCA && event.getEntity() instanceof Player victim) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 1));
                victim.sendMessage("§2[Lowca] Zostales unieruchomiony strzala!");
            }
        }

        if (event.getEntity() instanceof Player victim) {
            PlayerData victimData = plugin.getPlayerData(victim);

            switch (victimData.getCurrentAspect()) {
                case PALADYN -> {
                    event.setDamage(event.getDamage() * 0.85);
                }
                case CIEŃ -> {
                    long time = victim.getWorld().getTime();
                    boolean isNight = time >= 13000 && time <= 23000;
                    if (isNight && random.nextDouble() < 0.15) {
                        event.setCancelled(true);
                        victim.sendMessage("§8[Cien] Uniknales ciosu w mroku!");
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

            if (data.getCurrentAspect() == PlayerData.AspectType.WAMPIR) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || 
                    event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || 
                    event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    event.setDamage(event.getDamage() * 2.0);
                }
            }
        }
    }

    @EventHandler
    public void onShieldBlock(InventoryClickEvent event) {
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
}
