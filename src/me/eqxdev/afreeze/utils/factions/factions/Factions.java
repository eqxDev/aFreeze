package me.eqxdev.afreeze.utils.factions.factions;

import com.massivecraft.factions.entity.MPlayer;
import me.eqxdev.afreeze.utils.factions.Faction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqxDev on 21/02/2017.
 */
public class Factions implements Faction {
    @Override
    public List<Player> getAllPlayerFor(Player p) {
        List<Player> players = new ArrayList<>();
        MPlayer mp = MPlayer.get(p);
        if (mp.hasFaction()) {
            for (Player facPlayer : mp.getFaction().getOnlinePlayers()) {
                if (!facPlayer.getName().equals(p.getName())) {
                    players.add(facPlayer);
                }
            }
        } else {
            players.add(p);
        }

        return players;
    }
}
