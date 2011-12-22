package Raz.WorldWarp;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class WList
{
  public WList(Player p, Configuration Worlds, Configuration Config)
  {
    List worlds = p.getServer().getWorlds();
    p.sendMessage(ChatColor.RED + "[WorldWarp] Worlds");
    for (int i = 0; i < worlds.size(); i++)
    {
      if (((World)worlds.get(i)).getEnvironment() == World.Environment.NORMAL)
        p.sendMessage("- " + ChatColor.GREEN + ((World)worlds.get(i)).getName());
      else if (((World)worlds.get(i)).getEnvironment() == World.Environment.NETHER)
        p.sendMessage("- " + ChatColor.RED + ((World)worlds.get(i)).getName());
      else if (((World)worlds.get(i)).getEnvironment() == World.Environment.SKYLANDS)
        p.sendMessage("- " + ChatColor.AQUA + ((World)worlds.get(i)).getName());
    }
  }
}

/* Location:           C:\Users\Matt\Desktop\WorldWarp.jar
 * Qualified Name:     Raz.WorldWarp.WList
 * JD-Core Version:    0.6.0
 */