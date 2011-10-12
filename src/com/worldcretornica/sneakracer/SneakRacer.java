package com.worldcretornica.sneakracer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class SneakRacer extends JavaPlugin {

	public final SneakListener speedlistener = new SneakListener(this);
	
	public final Set<Player> speedplayers = new HashSet<Player>();
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public String pdfdescription;
	private String pdfversion;
	
	public Material raceblock;
	public int boostervalue;
	
	private YamlConfiguration config;
	
	// Permissions
    public PermissionHandler permissions;
    public PermissionManager permpex;
    boolean permissions3;
	
	@Override
	public void onDisable() {
		speedplayers.clear();
		this.logger.info(pdfdescription + " disabled.");
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, this.speedlistener, Event.Priority.Normal, this);		
		
		PluginDescriptionFile pdfFile = this.getDescription();
		pdfdescription = pdfFile.getName();
		pdfversion = pdfFile.getVersion();
		
		setupPermissions();
		checkConfig();
		
		raceblock = Material.getMaterial(config.getInt("RaceBlockId", 35));
		boostervalue = config.getInt("BoosterValue", 14);
		
		this.logger.info(pdfdescription + " version " + pdfversion + " is enabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args)
	{
		if (label.equalsIgnoreCase("sneakracer"))
		{
			if (sender instanceof Player)
			{
				sender.sendMessage(ChatColor.BLUE + pdfdescription + "  v" + pdfversion);
				if (!this.checkPermissions((Player) sender, "SneakRacer.racer"))
				{
					sender.sendMessage("[" + pdfdescription + "] Permissions Denied");
				}else{
					Enable((Player) sender);
				}
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	public void Enable(Player player)
	{
		if(IsSpeedRacer(player)){
			speedplayers.remove(player);
			player.sendMessage(ChatColor.YELLOW + "You are no longer a sneak racer.");
		}else{
			Location playerloc = player.getLocation();
			playerloc = new Location(player.getWorld(), playerloc.getBlockX(), playerloc.getBlockY(), playerloc.getBlockZ());
			playerloc.subtract(0, 1, 0);
			if (playerloc.getBlock().getType() == this.raceblock)
			{
				speedplayers.add(player);
				player.sendMessage(ChatColor.YELLOW + "You are now a sneak racer!");
			}else{
				player.sendMessage(ChatColor.RED + "You need to be on track to become a sneak racer. The current tracked block is : " + raceblock.name());
			}
		}
	}
	
	public boolean IsSpeedRacer(Player player)
	{
		return speedplayers.contains(player);
		
	}
	
	
	private void checkConfig()
	{
		File file = new File(this.getDataFolder(), "config.yml");		
		config = new YamlConfiguration();
		if (!file.exists())
		{
			config.createSection("# SneakRacer Config");
			config.createSection("#Block id for the race road");
			config.set("RoadBlock", 35);
			config.set("BoosterValue", 14);
			try {
				config.save(file);
			} catch (IOException e) {
				logger.severe("[" + pdfdescription + "] IOException: " + e.getMessage());
			}
		}else{
			try {
				config.load(file);
			} catch (FileNotFoundException e) {
				logger.severe("[" + pdfdescription + "] FileNotFound: " + e.getMessage());
			} catch (IOException e) {
				logger.severe("[" + pdfdescription + "] IOException: " + e.getMessage());
			} catch (InvalidConfigurationException e) {
				logger.severe("[" + pdfdescription + "] Invalid Configuration: " + e.getMessage());
			}
		}
	}
	
	private void setupPermissions() {
        if(permissions != null)
            return;
        
        Plugin permTest = this.getServer().getPluginManager().getPlugin("Permissions");
        Plugin pexTest = this.getServer().getPluginManager().getPlugin("PermissionsEx");
        
        // Check to see if Permissions exists
        if (pexTest != null)
    	{
    		// We're using Permissions
    		permpex = PermissionsEx.getPermissionManager();
        	// Check for Permissions 3
        	permissions3 = false;
        	logger.info("[" + pdfdescription + "] PermissionsEx " + pexTest.getDescription().getVersion() + " found");
        	return;
    	}else if (permTest == null) {
        	logger.info("[" + pdfdescription + "] Permissions not found, using SuperPerms");
        	return;
        }
    	// Check if it's a bridge
    	if (permTest.getDescription().getVersion().startsWith("2.7.7")) {
    		logger.info("[" + pdfdescription + "] Found Permissions Bridge. Using SuperPerms");
    		return;
    	}
    	
		// We're using Permissions
    	permissions = ((Permissions) permTest).getHandler();
    	// Check for Permissions 3
    	permissions3 = permTest.getDescription().getVersion().startsWith("3");
    	logger.info("[" + pdfdescription + "] Permissions " + permTest.getDescription().getVersion() + " found");
    }
	
	public Boolean checkPermissions(Player player, String node) {
    	// Permissions
        if (this.permissions != null) {
            if (this.permissions.has(player, node))
                return true;
        // Pex
        } else if(this.permpex != null) {
        	if (this.permpex.has(player, node))
        		return true;
        // SuperPerms
        } else if (player.hasPermission(node) || player.hasPermission(pdfdescription + ".*") || player.hasPermission("*")) {
              return true;
        } else if (player.isOp()) {
            return true;
        }
        return false;
    }

}
