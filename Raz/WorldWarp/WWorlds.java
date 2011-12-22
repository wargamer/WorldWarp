package Raz.WorldWarp;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class WWorlds
{
  public WWorlds(Player p, String[] args, Configuration Worlds, Configuration Config)
  {
    Worlds.load();
    if (args.length < 2) {
      p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Use /wcreate [name] [environmate] <seed>.");
    }
    else if ((args[1].equalsIgnoreCase("NETHER")) || (args[1].equalsIgnoreCase("SKYLANDS")) || (args[1].equalsIgnoreCase("NORMAL")))
    {
      Long Seed = null;

      if (args.length > 2) {
        if (isInt(args[2]))
          Seed = Long.valueOf(args[2]);
        else {
          Seed = Long.valueOf(args[2].hashCode());
        }
      }
      World.Environment e = World.Environment.valueOf(args[1].toUpperCase());
      p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Attempting to create world. Expect lag");
      if (Seed == null) {
        p.getServer().createWorld(args[0], e);
      }
      else {
        p.getServer().createWorld(args[0], e, Seed.longValue());
      }
      Seed = Long.valueOf(p.getServer().getWorld(args[0]).getSeed());
      Worlds.setProperty("worlds." + args[0] + ".name", args[0]);
      Worlds.setProperty("worlds." + args[0] + ".environmate", args[1].toUpperCase());
      Worlds.setProperty("worlds." + args[0] + ".seed", Seed);
      Worlds.save();
      p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.GREEN + "Done!");
    } else {
      p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Use NETHER/NORMAL/SKYLANDS.");
    }
  }

  public boolean isInt(String num)
  {
    try {
      Integer.parseInt(num);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }
}

/* Location:           C:\Users\Matt\Desktop\WorldWarp.jar
 * Qualified Name:     Raz.WorldWarp.WWorlds
 * JD-Core Version:    0.6.0
 */