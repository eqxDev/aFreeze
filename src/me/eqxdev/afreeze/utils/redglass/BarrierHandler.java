package me.eqxdev.afreeze.utils.redglass;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by eqxDev on 05/02/2017.
 */
public class BarrierHandler implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (BarrierManager.get().getBarriers().containsKey(e.getPlayer().getUniqueId())) {
            BarrierManager.get().remove(e.getPlayer().getUniqueId());
        }
    }

}
