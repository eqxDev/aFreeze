package me.eqxdev.afreeze.utils.factions.factions;

import me.eqxdev.afreeze.utils.factions.Faction;
import org.bukkit.entity.Player;
import org.zencode.mango.factions.types.PlayerFaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqxDev on 21/02/2017.
 */
public class Mango implements Faction {

    @Override
    public List<Player> getAllPlayerFor(Player p) {
        List<Player> players = new ArrayList<>();
        PlayerFaction pf = org.zencode.mango.Mango.getInstance().getFactionManager().getFaction(p);
        if (pf != null) {
            for (Player facPlayer : pf.getOnlinePlayers()) {
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
