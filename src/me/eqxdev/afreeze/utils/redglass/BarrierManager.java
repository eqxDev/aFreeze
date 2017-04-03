package me.eqxdev.afreeze.utils.redglass;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by eqxDev on 05/02/2017.
 */
public class BarrierManager {

    private static Map<UUID, BarrierGlass> barriers = new HashMap<>();

    private static BarrierManager instance;

    public static BarrierManager get() {
        if (instance == null) {
            instance = new BarrierManager();
        }
        return instance;
    }

    public void add(Player p) {
        List<Location> locs = new ArrayList<>();
        locs.add(p.getLocation().add(1, 0, 0));
        locs.add(p.getLocation().add(1, 1, 0));
        locs.add(p.getLocation().add(1, 2, 0));

        locs.add(p.getLocation().add(0, 0, 1));
        locs.add(p.getLocation().add(0, 1, 1));
        locs.add(p.getLocation().add(0, 2, 1));

        locs.add(p.getLocation().add(-1, 0, 0));
        locs.add(p.getLocation().add(-1, 1, 0));
        locs.add(p.getLocation().add(-1, 2, 0));

        locs.add(p.getLocation().add(0, 0, -1));
        locs.add(p.getLocation().add(0, 1, -1));
        locs.add(p.getLocation().add(0, 2, -1));

        locs.add(p.getLocation().add(0, 2, 0));

        barriers.put(p.getUniqueId(), new BarrierGlass(p, locs));

        p.teleport(p.getLocation().add(0, -1, 0).getBlock().getLocation().add(0.5, 1.0, 0.5));
    }

    public BarrierGlass get(UUID uuid) {
        return barriers.get(uuid);
    }

    public Map<UUID, BarrierGlass> getBarriers() {
        return barriers;
    }

    public void remove(UUID uuid) {
        barriers.remove(uuid);
    }
}
