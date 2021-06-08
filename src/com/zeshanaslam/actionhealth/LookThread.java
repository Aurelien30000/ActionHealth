package com.zeshanaslam.actionhealth;

import com.zeshanaslam.actionhealth.utils.TargetHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LookThread extends BukkitRunnable {

    private Main plugin;
    private Set<Byte> transparentTypeIds;
    private TargetHelper targetHelper;

    public LookThread(Main plugin) {
        this.plugin = plugin;
        this.targetHelper = new TargetHelper(plugin);
        this.transparentTypeIds = new TreeSet<>();

        transparentTypeIds.add((byte) 0);
        transparentTypeIds.add((byte) 20);
        transparentTypeIds.add((byte) 95);
        transparentTypeIds.add((byte) 102);
        transparentTypeIds.add((byte) 160);
        transparentTypeIds.add((byte) 8);
        transparentTypeIds.add((byte) 9);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.toggle.contains(player.getUniqueId())) {
                if (plugin.configStore.toggleMessage != null && !plugin.configStore.toggleMessage.equals("")) {
                    plugin.healthUtil.sendActionBar(player, plugin.healthUtil.replacePlaceholders(plugin.configStore.toggleMessage, "name", player.getName()));
                }
                continue;
            }

            List<LivingEntity> entities = targetHelper.getLivingTargets(player, plugin.configStore.lookDistance);
            if (!entities.isEmpty()) {
                for (LivingEntity livingEntity : entities) {
                    if (!plugin.healthUtil.matchesRequirements(player, livingEntity)) continue;

                    String name = plugin.healthUtil.getName(livingEntity, player);

                    if (targetHelper.canSee(player, livingEntity.getLocation(), transparentTypeIds) && !plugin.healthUtil.isBlacklisted(livingEntity, name)) {
                        if (plugin.configStore.isUsingWhiteList()) {
                            if (!plugin.healthUtil.isWhiteListed(livingEntity, name)) {
                                continue;
                            }
                        }

                        plugin.healthUtil.sendHealth(player, livingEntity, livingEntity.getHealth());
                        break;
                    }
                }
            }
        }
    }
}
