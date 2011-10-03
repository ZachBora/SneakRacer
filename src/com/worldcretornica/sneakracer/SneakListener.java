package com.worldcretornica.sneakracer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakListener extends PlayerListener {
	public static SneakRacer plugin;
	
	public SneakListener(SneakRacer instance)
	{
		plugin = instance;
	}
	
	@Override
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		
		Player player = event.getPlayer();
				
		if (plugin.IsSpeedRacer(player) && event.isSneaking())
		{
			Location playerloc = player.getLocation();
			
			playerloc.subtract(0, 1, 0);
			
			if (playerloc.getBlock().getType() == plugin.raceblock)
			{
				//Clone player current location
				Location dest = playerloc.clone();
				
				//Number of spaces to move forward
				int blockpower = 1;
				
				if (dest.clone().getBlock().getType() == plugin.raceblock &&
						dest.clone().getBlock().getState().getData().getData() == plugin.boostervalue)
				{
					blockpower = 2;
				}
				
				//Calculate the player direction
				double rot = (playerloc.getYaw() - 90) % 360;
		        if (rot < 0) {
		            rot += 360.0;
		        }
		        		
		        String dir = "";
		        
		        //Find direction
				if (0 <= rot && rot < 22.5 || 337.5 <= rot && rot < 360)
				{
					//X -
					dest.subtract(blockpower, 0, 0);
					dir = "Going NORTH";
				}else if (22.5 <= rot && rot < 67.5)
				{
					//X -
					//Z -
					dest.subtract(blockpower, 0, blockpower);
					dir = "Going NORTH EAST";
				}else if (67.5 <= rot && rot < 112.5) {
					//Z -
					dest.subtract(0, 0, blockpower);
					dir = "Going EAST";
		        } else if (112.5 <= rot && rot < 157.5) {
		        	//Z -
		        	//X +
		        	dest.subtract(-blockpower, 0, blockpower);
		            dir = "Going SOUTH EAST";
		        } else if (157.5 <= rot && rot < 202.5) {
		        	//X +
		        	dest.subtract(-blockpower, 0, 0);
					dir = "Going SOUTH";
		        } else if (202.5 <= rot && rot < 247.5) {
		        	//X +
		        	//Z +
		        	dest.subtract(-blockpower, 0, -blockpower);
		            dir = "Going SOUTH WEST";
		        } else if (247.5 <= rot && rot < 292.5) {
		        	//Z +
		        	dest.subtract(0, 0, -blockpower);
					dir = "Going WEST";
		        } else if (292.5 <= rot && rot < 337.5) {
		        	//Z +
		        	//X -
		        	dest.subtract(blockpower, 0, -blockpower);
		            dir = "Going NORTH WEST";
		        }
				
				//Check if destination block is higher or lower
				if (dest.getBlock().getType() != plugin.raceblock)
				{
					if (dest.clone().add(0, 1, 0).getBlock().getType() == plugin.raceblock)
					{
						dest.add(0,1,0);
					}else if (dest.clone().add(0, -1, 0).getBlock().getType() == plugin.raceblock)
					{
						dest.add(0,-1,0);
					}
				}
				
				//Check if theres a block above destination block
				if (dest.clone().add(0, 1, 0).getBlock().getType() == plugin.raceblock)
				{
					dest.add(0, 1, 0);
				}
				
				//player.sendMessage("Current rot: " + rot + " Current yaw: " + (dest.getYaw()));

				//Check if we should change the yaw
				if (0 <= rot && rot < 22.5 || 337.5 <= rot && rot < 360)
				{
					//X -
					if(dest.clone().add(-1, 0, 0).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(-1, 0, -1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}else if (dest.clone().add(-1, 0, 1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}
					}
					dir = "Going NORTH";
				}else if (22.5 <= rot && rot < 67.5)
				{
					//X -
					//Z -
					if(dest.clone().add(-1, 0, -1).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(-1, 0, 0).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}else if (dest.clone().add(0, 0, -1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}
					}
					//dir = "Going NORTH EAST";
				}else if (67.5 <= rot && rot < 112.5) {
					//Z -
					if(dest.clone().add(0, 0, -1).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(-1, 0, -1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}else if (dest.clone().add(1, 0, -1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}
					}
					//dir = "Going EAST";
		        } else if (112.5 <= rot && rot < 157.5) {
		        	//Z -
		        	//X +
		        	if(dest.clone().add(1, 0, -1).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(1, 0, 0).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}else if (dest.clone().add(0, 0, -1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}
					}
		            //dir = "Going SOUTH EAST";
		        } else if (157.5 <= rot && rot < 202.5) {
		        	//X +
		        	if(dest.clone().add(1, 0, 0).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(1, 0, -1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}else if (dest.clone().add(1, 0, 1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}
					}
					//dir = "Going SOUTH";
		        } else if (202.5 <= rot && rot < 247.5) {
		        	//X +
		        	//Z +
		        	if(dest.clone().add(1, 0, 1).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(1, 0, 0).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}else if (dest.clone().add(0, 0, 1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}
					}
		            //dir = "Going SOUTH WEST";
		        } else if (247.5 <= rot && rot < 292.5) {
		        	//Z +
		        	if(dest.clone().add(0, 0, 1).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(-1, 0, 1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}else if (dest.clone().add(1, 0, 1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}
					}
					//dir = "Going WEST";
		        } else if (292.5 <= rot && rot < 337.5) {
		        	//Z +
		        	//X -
		        	if(dest.clone().add(-1, 0, 1).getBlock().getType() != plugin.raceblock)
					{
						if(dest.clone().add(-1, 0, 0).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()+45));
						}else if (dest.clone().add(0, 0, 1).getBlock().getType() == plugin.raceblock)
						{
							dest.setYaw((float) (dest.getYaw()-45));
						}
					}
		           // dir = "Going NORTH WEST";
		        }
				
				
		        //player.sendMessage("On race track. " + dir);
		        
		        if (dest.getBlock().getType() == plugin.raceblock || dest.getBlock().getType() == Material.AIR)
		        	player.teleport(dest.clone().add(0, 1, 0));
			}
		}
		
	}
	
}
