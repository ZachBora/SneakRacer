package com.worldcretornica.sneakracer;

import net.minecraft.server.v1_5_R2.EntityPlayer;
import net.minecraft.server.v1_5_R2.Packet11PlayerPosition;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakListener implements Listener 
{
	public static SneakRacer plugin;
	
	public SneakListener(SneakRacer instance)
	{
		plugin = instance;
	}
	
	@EventHandler()
	public void onPlayerToggleSneak(final PlayerToggleSneakEvent event) 
	{
		
		Player player = event.getPlayer();
				
		if (plugin.IsSpeedRacer(player) && event.isSneaking())
		{
			Location playerloc = player.getLocation().clone();
			
			playerloc.subtract(0, 1, 0);
			
			if (playerloc.getBlock().getType() == plugin.raceblock)
			{
				//Clone player current location
				Location dest = playerloc.clone();
				
				//Number of spaces to move forward
				int blockpower = 1;
				
				if (dest.clone().getBlock().getType() == plugin.raceblock &&
						dest.clone().getBlock().getData() == plugin.boostervalue)
				{
					blockpower = 2;
				}
				
				//Calculate the player direction
				double rot = (playerloc.getYaw() - 90) % 360;
		        if (rot < 0) {
		            rot += 360.0;
		        }
		        				        
		        //Find direction
				if (0 <= rot && rot < 22.5 || 337.5 <= rot && rot < 360)
				{
					//X -
					dest.subtract(blockpower, 0, 0);
					// "Going NORTH";
				}else if (22.5 <= rot && rot < 67.5)
				{
					//X -
					//Z -
					dest.subtract(blockpower, 0, blockpower);
					// "Going NORTH EAST";
				}else if (67.5 <= rot && rot < 112.5) {
					//Z -
					dest.subtract(0, 0, blockpower);
					// "Going EAST";
		        } else if (112.5 <= rot && rot < 157.5) {
		        	//Z -
		        	//X +
		        	dest.subtract(-blockpower, 0, blockpower);
		            // "Going SOUTH EAST";
		        } else if (157.5 <= rot && rot < 202.5) {
		        	//X +
		        	dest.subtract(-blockpower, 0, 0);
					// "Going SOUTH";
		        } else if (202.5 <= rot && rot < 247.5) {
		        	//X +
		        	//Z +
		        	dest.subtract(-blockpower, 0, -blockpower);
		            // "Going SOUTH WEST";
		        } else if (247.5 <= rot && rot < 292.5) {
		        	//Z +
		        	dest.subtract(0, 0, -blockpower);
					// "Going WEST";
		        } else if (292.5 <= rot && rot < 337.5) {
		        	//Z +
		        	//X -
		        	dest.subtract(blockpower, 0, -blockpower);
		            // "Going NORTH WEST";
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
					//dir = "Going NORTH";
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
		        {
		        	//player.teleport(dest.clone().add(0, 1, 0));
		        	customteleport(player, dest.clone().add(0, 1, 0));
		        	
		        	//CraftServer 
		        	//server.getHandle().moveToWorld(entity, toWorld.dimension, true, to, true);
		        }
			}
		}
		
	}
	
	
	public void customteleport(Player p, Location l)
	{
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
				
		double d0, d1, d2;

        d0 = l.getX();
        d1 = l.getY();
        d2 = l.getZ();
        
        ep.setPosition(d0, d1, d2);
        
        Packet11PlayerPosition pack = new Packet11PlayerPosition();
        pack.x = d0;
        pack.y = d1 + 1.6200000047683716D;
        pack.stance = d1;
        pack.z = d2;
        
        ep.playerConnection.sendPacket(pack);
	}
	
}
