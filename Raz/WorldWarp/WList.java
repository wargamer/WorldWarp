package raz.worldwarp;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

@SuppressWarnings("deprecation")
public class WList {

	public WList(Player p, Configuration Worlds, Configuration Config) {
		List<World> worlds = p.getServer().getWorlds();
		p.sendMessage(ChatColor.RED + "[WorldWarp] Worlds");
		for (int i = 0; i < worlds.size(); i++) {
			if (((World) worlds.get(i)).getEnvironment() == World.Environment.NORMAL)
				p.sendMessage("- " + ChatColor.GREEN
						+ ((World) worlds.get(i)).getName());
			else if (((World) worlds.get(i)).getEnvironment() == World.Environment.NETHER)
				p.sendMessage("- " + ChatColor.RED
						+ ((World) worlds.get(i)).getName());
			else if (((World) worlds.get(i)).getEnvironment() == World.Environment.THE_END)
				p.sendMessage("- " + ChatColor.AQUA
						+ ((World) worlds.get(i)).getName());
		}
	}

}