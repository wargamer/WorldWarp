package Raz.WorldWarp.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import Raz.WorldWarp.WorldWarp;

public class WDelete implements WCommand {

    @Override
    public void run(Player p, String[] args, YamlConfiguration Worlds, YamlConfiguration Config) {
        if (args.length < 1) {
                p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: " + ChatColor.RED
                                + "Use /wdelete [name].");
        } else {			
            if (p.getServer().getWorld(args[0]) != null) {
                Worlds.set("worlds." + args[0], "");
                WorldWarp.Save(Worlds);
                p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: "
                                + ChatColor.RED + args[0] + " Deleted from worlds.yml.");
                p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: "
                                + ChatColor.RED + "Please delete the folder manually.");
                p.getServer().unloadWorld(args[0], true);
            } else {
                p.sendMessage(ChatColor.DARK_RED + "[WorldWarp]: "
                                + ChatColor.RED + args[0] + " Is not a loaded world.");
            }
        }
    }

}