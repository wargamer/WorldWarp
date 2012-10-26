package Raz.WorldWarp.Commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.WorldCreator;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import Raz.WorldWarp.WorldWarp;

public class WWorlds implements WCommand {

    @Override
    public void run(Player p, String[] args, YamlConfiguration Worlds, YamlConfiguration Config) {		
        if (args.length < 2) {
                p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED
                                + "Use /wcreate [name] [environment] <seed>.");        
        } else if (World.Environment.valueOf(args[1].toUpperCase()) != null) {
            Long seed = null;
            Environment env = World.Environment.valueOf(args[1].toUpperCase());
            if (args.length > 2) {
                    if (isInt(args[2]))
                            seed = Long.valueOf(args[2]);
                    else {
                            seed = Long.valueOf(args[2].hashCode());
                    }
            }            
            p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED
                            + "Attempting to create world. Expect lag");
            World newworld = null;
            if (seed == null) {
                    WorldCreator creator = WorldCreator.name(args[0]); creator.environment(env);
                    newworld = Bukkit.getServer().createWorld(creator);
            } else {
                    WorldCreator creator = WorldCreator.name(args[0]); creator.environment(env);
                    creator.seed(seed.longValue());
                    newworld = Bukkit.getServer().createWorld(creator);
            }
            if(newworld == null) {
                p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED + "Could not create create world, maybe try to remove the uid.dat?");
                return;
            }
            seed = Long.valueOf(newworld.getSeed());
            Worlds.set("worlds." + args[0] + ".name", args[0]);
            Worlds.set("worlds." + args[0] + ".environment",
                            args[1].toUpperCase());
            Worlds.set("worlds." + args[0] + ".seed", seed);
            WorldWarp.Save(Worlds);
            p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: "
                            + ChatColor.GREEN + "Done!");
        } else {
                p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED
                                + "Use NETHER/NORMAL/SKYLANDS.");
        }
    }

    public final boolean isInt(String num) {
            try {
                    Integer.parseInt(num);
            } catch (NumberFormatException nfe) {
                    return false;
            }
            return true;
    }

}