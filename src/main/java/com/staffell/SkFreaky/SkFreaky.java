package com.staffell.SkFreaky;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Version;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Getter
public final class SkFreaky extends JavaPlugin {

    private final SkFreaky instance = this;
    private static SkriptAddon addon;

    @Nullable
    public static SkriptAddon getAddonInstance() {
        return addon;
    }

    @Override
    public void onEnable() {

        final PluginManager manager = this.getServer().getPluginManager();
        final Plugin skript = manager.getPlugin("Skript");
        if (skript == null || !skript.isEnabled()) {
            getLogger().severe("Could not find Skript! Disabling...");
            manager.disablePlugin(this);
            return;
        } else if (Skript.getVersion().compareTo(new Version(2, 9, 2)) < 0) { // You may remove this if you want.
            getLogger().severe("You are running an unsupported version of Skript. Disabling...");
            manager.disablePlugin(this);
            return;
        }
        if (!Skript.isAcceptRegistrations()) {
            getLogger().severe("The plugin can't load when it's already loaded! Disabling...");
            manager.disablePlugin(this);
            return;
        }

        int pluginId = 23405;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));

        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");

        try {
            addon.loadClasses("com.staffell.SkFreaky");
        } catch (IOException error) {
            error.printStackTrace();
            manager.disablePlugin(this);
            return;
        }
        getLogger().info("Freak is in the house.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
