package com.staffell.SkFreaky.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Utils {
    @Nullable
    public static String getPrefixorSuffix(OfflinePlayer p, Boolean suffix) {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        LuckPerms luckPerms = null;
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
        assert luckPerms != null;
        User onlineUser = luckPerms.getUserManager().getUser(p.getName());
        if (onlineUser != null) {
            CachedMetaData metaData = onlineUser.getCachedData().getMetaData();
            if (suffix) return metaData.getSuffix();
            return metaData.getPrefix();
        }
        CompletableFuture<User> userLoadTask = luckPerms.getUserManager().loadUser(p.getUniqueId());
        User user;
        try {
            user = userLoadTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        CachedMetaData metaData = user.getCachedData().getMetaData();
        return metaData.getPrefix();

    }
}
