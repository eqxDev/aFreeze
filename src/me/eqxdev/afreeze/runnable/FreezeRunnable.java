package me.eqxdev.afreeze.runnable;

import me.eqxdev.afreeze.Main;
import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.redglass.BarrierManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by eqxDev on 04/02/2017.
 */
public class FreezeRunnable extends BukkitRunnable {


    @Override
    public void run() {
        if (!FreezeManager.get().isFreezeAll()) {
            if (FreezeManager.get().hasFrozenPlayers()) {
                for (UUID uuid : FreezeManager.get().getFreeze().keySet()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p == null) {
                        continue;
                    }
                    switch (FreezeManager.get().getType(uuid)) {
                        case HACKER:
                            p.openInventory(Main.get().generateInventory());
                        case PLAYER:
                            if (BarrierManager.get().getBarriers().containsKey(uuid)) {
                                BarrierManager.get().get(uuid).update(false);
                            }
                    }
                }
            }
        }
    }
}
