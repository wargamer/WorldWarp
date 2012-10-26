package Raz.WorldWarp.Commands;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;

public interface WCommand {
    public void run(Player p, String[] args, YamlConfiguration Worlds, YamlConfiguration Config);
}
