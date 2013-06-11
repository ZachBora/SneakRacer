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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SneakRacer extends JavaPlugin 
{
	
	public final Set<Player> speedplayers = new HashSet<Player>();
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public String pdfdescription;
	private String pdfversion;
	
	public Material raceblock;
	public int boostervalue;
	
	private YamlConfiguration config;
	
	@Override
	public void onDisable() {
		speedplayers.clear();
		this.logger.info(pdfdescription + " disabled.");
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new SneakListener(this), this);		
		
		PluginDescriptionFile pdfFile = this.getDescription();
		pdfdescription = pdfFile.getName();
		pdfversion = pdfFile.getVersion();
		
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
	
	public Boolean checkPermissions(Player player, String node) 
	{
		if (player.hasPermission(node) || player.hasPermission(pdfdescription + ".*") || player.hasPermission("*") || player.isOp()) 
		{
              return true;
        } else {
        	return false;
        }
    }

}
