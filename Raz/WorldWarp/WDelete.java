package Raz.WorldWarp;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class WDelete
{
  public WDelete(Player p, String[] args, Configuration Worlds, Configuration Config)
  {
    if (args.length < 1) {
      p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Use /wdelete [name].");
    } else {
      Worlds.load();
      if (p.getServer().getWorld(args[0]) != null) {
        Worlds.removeProperty("worlds." + args[0]);
        Worlds.save();
        p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + args[0] + " Deleted from worlds.yml.");
        p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Please delete the folder manually.");
        p.getServer().unloadWorld(args[0], true);
      } else {
        p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + args[0] + " Is not a loaded world.");
      }
    }
  }
}

/* Location:           C:\Users\Matt\Desktop\WorldWarp.jar
 * Qualified Name:     Raz.WorldWarp.WDelete
 * JD-Core Version:    0.6.0
 */