package Raz.WorldWarp;

import Raz.WorldWarp.Commands.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.logging.Logger;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class WorldWarp extends JavaPlugin {
	public static YamlConfiguration Worlds;
	public static YamlConfiguration Config;
	private static final Logger log = Logger.getLogger("Minecraft");	
        private static HashMap<YamlConfiguration, File> configFiles = new HashMap<YamlConfiguration, File>();

	private static PermissionHandler permissions = null;

        public static Boolean Save(YamlConfiguration yamlConfig) {
            if(configFiles.containsKey(yamlConfig)) {
                try {
                    yamlConfig.save(configFiles.get(yamlConfig));
                } catch(IOException ex) {
                    return false;
                }
            } else
                return false;
            return true;
        }
        
        public static Boolean Load(YamlConfiguration yamlConfig) {
            if(configFiles.containsKey(yamlConfig)) {
                try {
                    yamlConfig.load(configFiles.get(yamlConfig));
                } catch(FileNotFoundException notfound) {
                    return false;
                } catch(IOException io) {
                    return false;
                } catch(InvalidConfigurationException invalid) {
                    return false;
                }
            } else
                return false;
            return true;
        }
        
        @Override
	public void onEnable() {
                PluginDescriptionFile pdfFile = this.getDescription();
		log.info(("[WorldWarp] Enabled! Running v" + pdfFile.getVersion()));

		getDataFolder().mkdirs();
                File Worldsfile = new File(getDataFolder().getPath() + "/Worlds.yml");
                Worlds = new YamlConfiguration();
                File Configfile = new File(getDataFolder().getPath() + "/Config.yml");
                Config = new YamlConfiguration();
                
                configFiles.put(Worlds, Worldsfile);
                configFiles.put(Config, Configfile);
                
                initWorlds();
                
		if (!Worldsfile.exists()) {
			log.info("[WorldWarp] Creating worlds.yml");
			try {
                                if(Worldsfile.createNewFile()) {
                                    log.warning("[WorldWarp] Could not create worlds.yml!");
                                }
			} catch (IOException e) {			
                            
			}
		} else {
                    Load(Worlds);
                }
		if (!Configfile.exists()) {
			log.info("[WorldWarp] Creating Config.yml");
			try {   
                            if(Configfile.createNewFile()) {
                                log.warning("[WorldWarp] Could not create config.yml!");
                            }
                            Config.set("Settings.permissions.use", "true");
                            Save(Config);
			} catch (IOException e) {
				
			}
		} else {
                    Load(Config);
                }
		
		setupPermissions();
		loadWorlds();
	}

	public void setupPermissions() {
		Plugin test = getServer().getPluginManager().getPlugin("Permissions");
		if (!(test == null))
			permissions = ((Permissions) test).getHandler();
	}
        
        public void initWorlds() {
            List<World> worlds = getServer().getWorlds();
            for(World world : worlds) {            
                String n = world.getName();
                String Env = world.getEnvironment().name();
                Long seed = world.getSeed();
                Worlds.set("worlds." + n + ".name", n);
                Worlds.set("worlds." + n + ".environment", Env);
                Worlds.set("worlds." + n + ".seed", seed);                
            }
            Save(Worlds);
        }

        @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if ((sender instanceof ConsoleCommandSender)) {
			System.out.println("[WorldWarp]: Please use commands In-Game!");
			return false;
		}
		Player player = (Player) sender;

		if (!(sender instanceof Player))
			return false;

		if (hasPerm(player, label.toLowerCase())) {
                    WCommand command = null;
                    if (label.equalsIgnoreCase("wcreate")) {
                            command = new WWorlds();
                    } else if (label.equalsIgnoreCase("wwarp")) {
                            command = new WWarp();
                    } else if (label.equalsIgnoreCase("wlist")) {
                            command = new WList();
                    } else if (label.equalsIgnoreCase("wdelete")) {
                            command = new WDelete();
                    } else
                        return false;
                    command.run(player, args, Worlds, Config);
		}
		return true;
	}

	private boolean hasPerm(Player player, String node) {
		if (player.isOp())
			return true;
		if (permissions.has(player, node))
			return true;
		if (player.hasPermission(node))
			return true;
		player.sendMessage(ChatColor.RED
				+ "You do not have permission to do this.");
		return false;
	}

	public World.Environment getEnv(String ev) {
		World.Environment e = World.Environment.valueOf(ev);
		return e;
	}

	public void loadWorlds() {
                ConfigurationSection section = Worlds.getConfigurationSection("worlds");
                if(section == null)
                    return;
                
		Set<String> worldNames = section.getKeys(false);
		int i = 0;
		System.out.println("[WorldWarp]: Loading worlds");
		if (worldNames != null) {
			for (String name : worldNames) {
				String env = Worlds.getString("worlds." + name + ".environment");
                                WorldCreator creator = WorldCreator.name(name); creator.environment(getEnv(env));
				getServer().createWorld(creator);
				getServer().getWorld(name).setPVP(true);
				i++;
				System.out.println("[WorldWarp]: Loaded " + i + "/"
						+ worldNames.size() + " Worlds");
			}
			System.out.println("[WorldWarp]: Done loading worlds");
		} else {
			System.out.println("[WorldWarp]: No worlds to load");
		}
	}

        @Override
	public void onDisable() {
		log.info("[WorldWarp]: Disabled!");
	}

}