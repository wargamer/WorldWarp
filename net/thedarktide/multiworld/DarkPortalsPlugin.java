package net.thedarktide.multiworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

@SuppressWarnings("deprecation")
public class DarkPortalsPlugin extends JavaPlugin
{
	
	public static Configuration Worlds;
	public static Configuration Config;
	private static final Logger log = Logger.getLogger("Minecraft");
	public static boolean isperm = false;

	public static PermissionHandler permissions = null;

	public void onEnable()
	{
		getDataFolder().mkdirs();
		if (!new File(getDataFolder().getPath() + "/Worlds.yml").exists())
		{
			log.info("[DarkPortals] Creating worlds.yml");
			try
			{
				new File(getDataFolder().getPath() + "/Worlds.yml").createNewFile();
				Worlds = new Configuration(new File(getDataFolder().getPath() + "/Worlds.yml"));
				Worlds.load();
				try
				{
					if (new FileInputStream(new File(getDataFolder().getPath() + "/Worlds.yml")).read() == -1)
					{
						List<World> worlds = getServer().getWorlds();
						for (int i = 0; i < worlds.size(); i++)
						{
							String n = ((World) worlds.get(i)).getName();
							String Env = ((World) worlds.get(i)).getEnvironment().name();
							Long seed = Long.valueOf(((World) worlds.get(i)).getSeed());
							Worlds.setProperty("worlds." + n + ".name", n);
							Worlds.setProperty("worlds." + n + ".environmate", Env);
							Worlds.setProperty("worlds." + n + ".seed", seed);
						}
						Worlds.save();
					}
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (!new File(getDataFolder().getPath() + "/Config.yml").exists())
		{
			log.info("[DarkPortals] Creating Config.yml");
			try
			{
				new File(getDataFolder().getPath() + "/Config.yml").createNewFile();
				Config = new Configuration(new File(getDataFolder().getPath() + "/Config.yml"));
				Config.load();
				try
				{
					if (new FileInputStream(new File(getDataFolder().getPath() + "/Config.yml")).read() == -1)
					{
						Config.setProperty("Settings.permissions.use", "true");
						Config.save();
					}
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		Worlds = new Configuration(new File(getDataFolder().getPath() + "/Worlds.yml"));
		Worlds.load();
		Config = new Configuration(new File(getDataFolder().getPath() + "/Config.yml"));
		
		Config.load();
		setupPermissions();
		loadWorlds();
		
		log.info("[DarkPortals] Enabled!");
	}

	public void setupPermissions() {
		Plugin test = getServer().getPluginManager().getPlugin("Permissions");
		if (test != null)
			permissions = ((Permissions) test).getHandler();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (hasPerm(player, label.toLowerCase()))
		{
			if (label.equalsIgnoreCase("wcreate"))
				new WWorlds(player, args, Worlds, Config);
			else if (label.equalsIgnoreCase("wwarp"))
				new WWarp(player, args, Worlds, Config);
			else if (label.equalsIgnoreCase("wlist"))
				new WList(player, Worlds, Config);
			else if (label.equalsIgnoreCase("wdelete"))
				new WDelete(player, args, Worlds, Config);
			else if (label.equalsIgnoreCase("wunload"))
			{
				if (args.length == 1)
				{
					if (player.getServer().getWorld(args[0]) != null)
					{
						player.getServer().unloadWorld(args[0], true);
						player.sendMessage(ChatColor.GREEN + "World " + args[0] + " unloaded.");
					}
					else
						player.sendMessage(ChatColor.RED + "Could not find world.");
				}
				else
					player.sendMessage(ChatColor.RED + "/wunload [name]");
			}
			else if (label.equalsIgnoreCase("wwho"))
			{
				if (args.length == 1)
				{
					String players = "";
					for (World w : getServer().getWorlds())
					{
						if (w.getName().equalsIgnoreCase(args[0]))
						{
							if (w.getPlayers() == null || w.getPlayers().isEmpty())
							{
								player.sendMessage(ChatColor.RED + "No players in world " + ChatColor.GOLD + w.getName());
								return true;
							}
							for (Player p : w.getPlayers())
								players += p.getName() + ", ";
							player.sendMessage(ChatColor.GRAY + "Players in world " + ChatColor.GOLD + w.getName() + ChatColor.GRAY + ":");
							player.sendMessage(players);
							return true;
						}
					}
					player.sendMessage(ChatColor.RED + "No worlds found by the name. Use " + ChatColor.GOLD + "/wlist");
				}
				else
					player.sendMessage(ChatColor.RED + "/wwho [world name]");
			}
		}
		return true;
	}

	private boolean hasPerm(Player player, String node)
	{
		if (player.isOp()) return true;
		if (permissions.has(player, node)) return true;
		if (player.hasPermission(node)) return true;
		player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
		return false;
	}

	public World.Environment getEnv(String ev)
	{
		return World.Environment.valueOf(ev);
	}

	public void loadWorlds()
	{
		List<String> worldNames = Worlds.getKeys("worlds");
		int i = 0;
		System.out.println("[DarkPortals]: Loading worlds");
		if (worldNames != null)
		{
			for (String name : worldNames)
			{
				String env = Worlds.getString("worlds." + name + ".environmate");
				getServer().createWorld(name, getEnv(env));
				getServer().getWorld(name).setPVP(true);
				i++;
				System.out.println("[DarkPortals]: Loaded " + i + "/" + worldNames.size() + " Worlds");
			}
			System.out.println("[DarkPortals]: Done loading worlds");
		}
		else
			System.out.println("[DarkPortals]: No worlds to load");
	}

	public void onDisable()
	{
		log.info("[DarkPortals]: Disabled!");
	}

}