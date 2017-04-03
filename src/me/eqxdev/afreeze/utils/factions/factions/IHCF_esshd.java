package me.eqxdev.afreeze.utils.factions.factions;

import me.eqxdev.afreeze.utils.factions.Faction;
import me.esshd.hcf.HCF;
import me.esshd.hcf.faction.type.PlayerFaction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqxDev on 21/02/2017.
 */
public class IHCF_esshd implements Faction {

    @Override
    public List<Player> getAllPlayerFor(Player p) {
        List<Player> players = new ArrayList<>();
        PlayerFaction pf = HCF.getPlugin().getFactionManager().getPlayerFaction(p.getUniqueId());

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
