package me.eqxdev.afreeze.utils.factions.factions;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.eqxdev.afreeze.utils.factions.Faction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqxDev on 14/03/2017.
 */
public class FactionsUUID implements Faction {

    @Override
    public List<Player> getAllPlayerFor(Player p) {
        List<Player> players = new ArrayList<>();
        FPlayer fp = FPlayers.getInstance().getByPlayer(p);
        if (fp.hasFaction()) {
            for (Player facPlayer : fp.getFaction().getOnlinePlayers()) {
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
