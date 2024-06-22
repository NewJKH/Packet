package org.nano.advancements.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
public class ProtocolLibHook {
    private ProtocolManager protocolManager;
    private Plugin plugin;

    public void register(Plugin plugin){
        protocolManager = ProtocolLibrary.getProtocolManager();
        this.plugin = plugin;
        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.CHAT
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                String message = packet.getStrings().read(0);
                Player player = event.getPlayer();
                if (message.contains("ㅎㅇ")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1, false, false));
                            if ( !player.getActivePotionEffects().isEmpty()){
                                player.sendMessage("적용됨");
                            }
                            for (Player other : Bukkit.getOnlinePlayers()) {
                                if (!other.equals(player)) {
                                    sendRemoveGlowPacket(other, player);
                                }
                            }
                        }
                    }.runTask(plugin);
                    event.setCancelled(true);
                }
            }
        });
    }
    private void sendRemoveGlowPacket(Player receiver, Player glowingPlayer) {
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            WrappedDataWatcher.WrappedDataWatcherObject obj = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
            watcher.setObject(obj, (byte) 0x00); // No effects

            PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, glowingPlayer.getEntityId());
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

            protocolManager.sendServerPacket(receiver, packet);
    }
}
