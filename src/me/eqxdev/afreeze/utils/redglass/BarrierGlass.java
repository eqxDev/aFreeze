package me.eqxdev.afreeze.utils.redglass;

import me.eqxdev.afreeze.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by eqxDev on 05/02/2017.
 */
public class BarrierGlass {

    private Player player;
    private List<Location> locationList;

    public BarrierGlass(Player uuid, List<Location> locationList) {
        this.player = uuid;
        this.locationList = locationList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }


    public void update(boolean undo) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location loc : getLocationList()) {
                    if (undo) {
                        player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
                    } else {
                        player.sendBlockChange(loc, Material.STAINED_GLASS, (byte) 14);
                    }
                }
            }
        }.runTaskAsynchronously(Main.get());
    }
}
