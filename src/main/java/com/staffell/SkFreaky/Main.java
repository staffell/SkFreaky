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

public final class Main extends JavaPlugin {

    @Getter
    private Main instance;
    // If this is your first experience with Lombok, the @Getter basically just makes a getter method behind the scenes.
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

        int pluginId = 23405 ; // Input your bStats plugin ID here, if you do not wish to use bStats, you may remove this section.
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("skript_version", () -> Skript.getVersion().toString()));

        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");

        try {
            addon.loadClasses("com.staffell.skFreaky");
        } catch (IOException error) {
            error.printStackTrace();
            manager.disablePlugin(this);
            return;
        }
        getLogger().info("Successfully got Freaky.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
