package Raz.WorldWarp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class WWarp
{
  public WWarp(Player p, String[] args, Configuration Worlds, Configuration Config)
  {
    if (args.length < 1) {
      p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Use /wwarp [name].");
      return;
    }
    if (p.getServer().getWorld(args[0]) != null) {
      Location loc = p.getServer().getWorld(args[0]).getSpawnLocation();
      p.teleport(loc);
      p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.GREEN + "Welcome to " + args[0]);
      return;
    }
    p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "That world does not exist.");
    p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Use /wlist to see worlds.");
  }
}

/* Location:           C:\Users\Matt\Desktop\WorldWarp.jar
 * Qualified Name:     Raz.WorldWarp.WWarp
 * JD-Core Version:    0.6.0
 */