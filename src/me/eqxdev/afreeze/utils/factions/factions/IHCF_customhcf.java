package me.eqxdev.afreeze.utils.factions.factions;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.faction.type.PlayerFaction;
import me.eqxdev.afreeze.utils.factions.Faction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqxDev on 22/02/2017.
 */
public class IHCF_customhcf implements Faction {

    @Override
    public List<Player> getAllPlayerFor(Player p) {
        List<Player> players = new ArrayList<>();
        PlayerFaction pf = HCF.getPlugin().getFactionManager().getPlayerFaction(p.getUniqueId());

        if (pf != null) {
            for (Object fac : pf.getOnlinePlayers()) {
                Player facPlayer = (Player) fac;
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