package net.thedarktide.multiworld;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

@SuppressWarnings("deprecation")
public class WWorlds
{

	public WWorlds(Player p, String[] args, Configuration Worlds, Configuration Config)
	{
		Worlds.load();
		if (args.length < 2)
			p.sendMessage(ChatColor.DARK_RED + "[DarkPortals]: " + ChatColor.RED + "Use /wcreate [name] [environmate] <seed>.");
		else if ((args[1].equalsIgnoreCase("NETHER"))
					|| (args[1].equalsIgnoreCase("THE_END"))
					|| (args[1].equalsIgnoreCase("NORMAL")))
		{
			Long seed = null;

			if (args.length > 2)
			{
				if (isInt(args[2]))
					seed = Long.valueOf(args[2]);
				else
					seed = Long.valueOf(args[2].hashCode());
			}
			World.Environment e = World.Environment.valueOf(args[1].toUpperCase());
			p.sendMessage(ChatColor.DARK_RED + "[DarkPortals]: " + ChatColor.RED + "Attempting to create world. Expect lag");
			if (seed == null)
				p.getServer().createWorld(args[0], e);
			else
				p.getServer().createWorld(args[0], e, seed.longValue());
			seed = Long.valueOf(p.getServer().getWorld(args[0]).getSeed());
			Worlds.setProperty("worlds." + args[0] + ".name", args[0]);
			Worlds.setProperty("worlds." + args[0] + ".environmate", args[1].toUpperCase());
			Worlds.setProperty("worlds." + args[0] + ".seed", seed);
			Worlds.save();
			p.sendMessage(ChatColor.DARK_RED + "[DarkPortals]: " + ChatColor.GREEN + "Done!");
		}
		else
			p.sendMessage(ChatColor.DARK_RED + "[DarkPortals]: " + ChatColor.RED + "Use NETHER/NORMAL/SKYLANDS.");
	}

	public boolean isInt(String num)
	{
		try
		{
			Integer.parseInt(num);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

}