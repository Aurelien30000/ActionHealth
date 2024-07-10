package com.zeshanaslam.actionhealth.config;

import com.zeshanaslam.actionhealth.LookThread;
import com.zeshanaslam.actionhealth.Main;
import com.zeshanaslam.actionhealth.action.ActionStore;
import com.zeshanaslam.actionhealth.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigStore {

    public String healthMessage;
    public String healthMessageOther;
    public boolean usePerms;
    public boolean showMobs;
    public boolean showPlayers;
    public boolean delay;
    public int delayTick;
    public boolean checkPvP;
    public boolean stripName;
    public boolean rememberToggle;
    public boolean canSee;
    public boolean invisiblePotion;
    public boolean spectatorMode;
    public boolean useClientLanguage;
    public String filledHeartIcon;
    public String halfHeartIcon;
    public String emptyHeartIcon;
    public List<String> worlds = new ArrayList<>();
    public HashMap<String, String> translate = new HashMap<>();
    public List<String> regions = new ArrayList<>();
    public String mcRemappedPackage;
    public String mcVersion;
    public boolean useOldMethods;
    public boolean showOnLook;
    public double lookDistance;
    public List<String> blacklist = new ArrayList<>();
    public String toggleMessage;
    public String enableMessage;
    public String disableMessage;
    public boolean hasMVdWPlaceholderAPI;
    public boolean hasPlaceholderAPI;
    public int limitHealth;
    public boolean showNPC;
    public boolean showMiniaturePets;
    public double lookDot;
    public double lookTolerance;
    public long checkTicks;
    public ActionStore actionStore;
    public List<String> whitelist = new ArrayList<>();
    public String upperLimit;
    public int upperLimitStart;
    public int upperLimitLength;
    public boolean allowMetrics;

    public ConfigStore(Main plugin) {
        // Clear settings for reloads
        worlds.clear();
        regions.clear();
        blacklist.clear();
        translate.clear();
        whitelist.clear();

        // Check if using MVdWPlaceholderAPI
        hasMVdWPlaceholderAPI = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");

        // Check if using placeholderAPI
        hasPlaceholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        // Get settings from config
        healthMessage = plugin.getConfig().getString("Health Message");

        healthMessageOther = "";
        if (plugin.getConfig().contains("Non Player Message")) {
            healthMessageOther = plugin.getConfig().getString("Non Player Message");
        }

        usePerms = plugin.getConfig().getBoolean("Use Permissions");
        showMobs = plugin.getConfig().getBoolean("Show Mob");
        showPlayers = plugin.getConfig().getBoolean("Show Player");
        showNPC = plugin.getConfig().getBoolean("Show NPC");
        delay = plugin.getConfig().getBoolean("Delay Message");
        if (plugin.getConfig().contains("Delay Tick")) {
            delayTick = plugin.getConfig().getInt("Delay Tick");
        } else {
            delayTick = 1;
        }

        checkPvP = plugin.getConfig().getBoolean("Region PvP");
        stripName = plugin.getConfig().getBoolean("Strip Name");
        filledHeartIcon = plugin.getConfig().getString("Full Health Icon");
        halfHeartIcon = plugin.getConfig().getString("Half Health Icon");
        emptyHeartIcon = plugin.getConfig().getString("Empty Health Icon");
        if (plugin.getConfig().getBoolean("Name Change")) {
            for (String s : plugin.getConfig().getStringList("Name")) {
                String[] split = s.split(" = ");
                translate.put(split[0], split[1]);
            }
        }
        useClientLanguage = plugin.getConfig().getBoolean("Use Client Language");

        // Load disabled regions
        regions = plugin.getConfig().getStringList("Disabled regions");

        worlds = plugin.getConfig().getStringList("Disabled worlds");

        // Check if using protocol build
        mcRemappedPackage = Bukkit.getServer().getClass().getPackage().getName();
        mcRemappedPackage = mcRemappedPackage.substring(mcRemappedPackage.lastIndexOf(".") + 1);
        mcVersion = Bukkit.getBukkitVersion();
        mcVersion = mcVersion.split("-")[0];

        useOldMethods = mcRemappedPackage.equalsIgnoreCase("v1_8_R1") || mcRemappedPackage.equalsIgnoreCase("v1_7_");

        if (plugin.getConfig().contains("Remember Toggle")) {
            rememberToggle = plugin.getConfig().getBoolean("Remember Toggle");
        } else {
            rememberToggle = false;
        }

        // New options
        if (plugin.getConfig().contains("Blacklist")) {
            blacklist.addAll(plugin.getConfig().getStringList("Blacklist").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
        }

        if (plugin.getConfig().contains("Whitelist")) {
            whitelist.addAll(plugin.getConfig().getStringList("Whitelist").stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
        }

        if (plugin.getConfig().contains("Toggle Message")) {
            toggleMessage = plugin.getConfig().getString("Toggle Message");
        }

        if (plugin.getConfig().contains("On Enable")) {
            enableMessage = plugin.getConfig().getString("On Enable");
        } else {
            enableMessage = "&7ActionHealth has been &cenabled&7.";
        }

        if (plugin.getConfig().contains("On Disable")) {
            disableMessage = plugin.getConfig().getString("On Disable");
        } else {
            disableMessage = "&7ActionHealth has been &cdisabled&7.";
        }

        if (plugin.getConfig().contains("Can See")) {
            canSee = plugin.getConfig().getBoolean("Can See");
        } else {
            canSee = true;
        }

        if (plugin.getConfig().contains("Invisible Potion")) {
            invisiblePotion = plugin.getConfig().getBoolean("Invisible Potion");
        } else {
            invisiblePotion = true;
        }

        if (plugin.getConfig().contains("Spectator Mode")) {
            spectatorMode = plugin.getConfig().getBoolean("Spectator Mode");
        } else {
            spectatorMode = true;
        }

        if (plugin.getConfig().contains("Limit Health")) {
            if (plugin.getConfig().isBoolean("Limit Health")) {
                limitHealth = 10;
            } else {
                limitHealth = plugin.getConfig().getInt("Limit Health");
            }
        }
        showMiniaturePets = plugin.getConfig().getBoolean("ShowMiniaturePets");
        actionStore = new ActionStore(plugin);

        if (plugin.getConfig().contains("LookValues")) {
            lookDot = plugin.getConfig().getDouble("LookValues.Dot");
            lookTolerance = plugin.getConfig().getDouble("LookValues.Tolerance");
        } else {
            lookDot = 0;
            lookTolerance = 4;
        }

        if (plugin.getConfig().contains("LookValues.CheckTicks")) {
            checkTicks = plugin.getConfig().getLong("LookValues.CheckTicks");
        } else {
            checkTicks = 0;
        }

        if (plugin.taskID != -1) Bukkit.getScheduler().cancelTask(plugin.taskID);

        if (plugin.getConfig().contains("Show On Look")) {
            showOnLook = plugin.getConfig().getBoolean("Show On Look");
            lookDistance = plugin.getConfig().getDouble("Look Distance");

            if (showOnLook) {
                BukkitTask bukkitTask = new LookThread(plugin).runTaskTimer(plugin, 0, checkTicks);
                plugin.taskID = bukkitTask.getTaskId();
            }
        } else {
            plugin.taskID = -1;
            showOnLook = false;
        }

        if (plugin.getConfig().contains("Upper Limit Health")) {
            upperLimit = plugin.getConfig().getString("Upper Limit Health");

            String[] limits = upperLimit.split(" -> ");
            upperLimitStart = Integer.parseInt(limits[0]);
            upperLimitLength = Integer.parseInt(limits[1]);
        } else {
            upperLimit = null;
        }

        if (plugin.getConfig().contains("Allow Metrics")) {
            allowMetrics = plugin.getConfig().getBoolean("Allow Metrics");
        } else {
            allowMetrics = true;
        }

        if (allowMetrics && plugin.metrics == null) {
            plugin.metrics = new Metrics(plugin, 11639);
        }
    }

    public boolean isUsingWhiteList() {
        return !whitelist.isEmpty();
    }
}
