package me.eqxdev.afreeze.utils.factions.factions;

import com.massivecraft.factions.FPlayers;
import me.eqxdev.afreeze.utils.factions.Faction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqxDev on 05/02/2017.
 */
public class HCFactions implements Faction {

    @Override
    public List<Player> getAllPlayerFor(Player p) {
        List<Player> players = new ArrayList<>();
        com.massivecraft.factions.Faction fac = FPlayers.getInstance().getByPlayer(p).getFaction();
        if(fac != null) {
            for(Player facPlayer : fac.getOnlinePlayers()) {
                players.add(facPlayer);
            }
        } else {
            players.add(p);
        }

        return players;
    }
}
