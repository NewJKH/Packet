package org.nano.advancements;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.nano.advancements.protocollib.ProtocolLibHook;

import java.util.Objects;

public final class Advancements extends JavaPlugin {
    @Override
    public void onEnable() {
        onProtocolLibLoad();

    }
    private void onProtocolLibLoad(){
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null ) {
            new ProtocolLibHook().register(this);
        }else System.out.println(" 프로토콜립을 사용하지 않았습니다. ");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
