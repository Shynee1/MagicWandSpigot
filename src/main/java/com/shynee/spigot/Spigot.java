package com.shynee.spigot;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spigot extends JavaPlugin {

    public static Spigot instance;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        ItemManager.init();
        EventCommand eventCommand = new EventCommand();
        getServer().getPluginManager().registerEvents(eventCommand, this);
        getCommand("magicstick").setExecutor(eventCommand);
    }

    @Override
    public void onDisable() {
       instance = null;
    }
}
