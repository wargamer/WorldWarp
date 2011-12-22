package Raz.WorldWarp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class WorldWarp extends JavaPlugin
{
  public static Configuration Worlds;
  public static Configuration Config;
  private static final Logger log = Logger.getLogger("Minecraft");
  public static boolean isperm = false;

  public void onEnable() {
    log.info("\t  _ _ _         _   _ _ _ _               ___    ___  ");
    log.info("\t | | | |___ ___| |_| | | | |___ ___ ___  |_  |  |_  |");
    log.info("\t | | | | . |  _| | . | | | | .'|  _| . | |  _|  |_- |");
    log.info("\t |_____|___|_| |_|___|_____|__,|_| |  _| |___|[]|___|");
    log.info("\t                                   |_|         ");
    log.info("[WorldWarp] Enabled! Running 2.3");

    getDataFolder().mkdirs();
    if (!new File(getDataFolder().getPath() + "/Worlds.yml").exists()) {
      log.info("[WorldWarp] Creating worlds.yml");
      try {
        new File(getDataFolder().getPath() + "/Worlds.yml").createNewFile();
        Worlds = new Configuration(new File(getDataFolder().getPath() + "/Worlds.yml"));
        Worlds.load();
        try {
          if (new FileInputStream(new File(getDataFolder().getPath() + "/Worlds.yml")).read() == -1) {
            List worlds = getServer().getWorlds();
            for (int i = 0; i < worlds.size(); i++)
            {
              String n = ((World)worlds.get(i)).getName();
              String Env = ((World)worlds.get(i)).getEnvironment().name();
              Long seed = Long.valueOf(((World)worlds.get(i)).getSeed());
              Worlds.setProperty("worlds." + n + ".name", n);
              Worlds.setProperty("worlds." + n + ".environmate", Env);
              Worlds.setProperty("worlds." + n + ".seed", seed);
            }
            Worlds.save();
          }
        }
        catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (!new File(getDataFolder().getPath() + "/Config.yml").exists()) {
      log.info("[WorldWarp] Creating Config.yml");
      try {
        new File(getDataFolder().getPath() + "/Config.yml").createNewFile();
        Config = new Configuration(new File(getDataFolder().getPath() + "/Config.yml"));
        Config.load();
        try {
          if (new FileInputStream(new File(getDataFolder().getPath() + "/Config.yml")).read() == -1) {
            Config.setProperty("Settings.permissions.use", "true");
            Config.save();
          }
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    Worlds = new Configuration(new File(getDataFolder().getPath() + "/Worlds.yml"));
    Worlds.load();
    Config = new Configuration(new File(getDataFolder().getPath() + "/Config.yml"));
    Config.load();
    if ((Config.getString("Settings.permissions.use").equalsIgnoreCase("true")) || (Config.getString("Settings.permissions.use").equalsIgnoreCase("'true'"))) {
      log.info("[WorldWarp] Using BukkitPermissions!");
    }
    loadWorlds();
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ((sender instanceof ConsoleCommandSender)) {
      System.out.println("[WorldWarp]: Please use commands In-Game!");
      return false;
    }
    Player player = (Player)sender;

    if (!(sender instanceof Player)) return false;

    if (hasPerm(player, label.toLowerCase()))
    {
      if (label.equalsIgnoreCase("wcreate"))
      {
        new WWorlds(player, args, Worlds, Config);
      }
      else if (label.equalsIgnoreCase("wwarp"))
      {
        new WWarp(player, args, Worlds, Config);
      }
      else if (label.equalsIgnoreCase("wlist"))
      {
        new WList(player, Worlds, Config);
      }
      else if (label.equalsIgnoreCase("wdelete")) {
        new WDelete(player, args, Worlds, Config);
      }
    }
    return true;
  }

  private boolean hasPerm(Player player, String node)
  {
    if ((Config.getString("Settings.permissions.use").equalsIgnoreCase("true")) || (Config.getString("Settings.permissions.use").equalsIgnoreCase("'true'"))) {
      if (player.hasPermission("WorldWarp." + node)) {
        return true;
      }
      player.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "You do not have permissions to do that.");
      return false;
    }

    if ((node.equalsIgnoreCase("wcreate")) || (node.equalsIgnoreCase("wdelete")))
    {
      if (player.isOp()) {
        return true;
      }
      player.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "You do not have permissions to do that.");
      return false;
    }

    return true;
  }

  public World.Environment getEnv(String ev)
  {
    World.Environment e = World.Environment.valueOf(ev);
    return e;
  }

  public void loadWorlds() {
    List worldNames = Worlds.getKeys("worlds");
    int i = 0;
    System.out.println("[WorldWarp]: Loading worlds");
    if (worldNames != null)
    {
      for (String name : worldNames) {
        String env = Worlds.getString("worlds." + name + ".environmate");
        getServer().createWorld(name, getEnv(env));
        getServer().getWorld(name).setPVP(false);
        i++;
        System.out.println("[WorldWarp]: Loaded " + i + "/" + worldNames.size() + " Worlds");
      }
      System.out.println("[WorldWarp]: Done loading worlds");
    } else {
      System.out.println("[WorldWarp]: No worlds to load");
    }
  }

  public void onDisable()
  {
    log.info("[WorldWarp]: Disabled!");
  }
}

/* Location:           C:\Users\Matt\Desktop\WorldWarp.jar
 * Qualified Name:     Raz.WorldWarp.WorldWarp
 * JD-Core Version:    0.6.0
 */