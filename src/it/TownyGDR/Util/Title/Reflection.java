/**
 * 
 */
package it.TownyGDR.Util.Title;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*********************************************************************
 * @author: Elsalamander
 * @data: 1 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/

public class Reflection {

	public void sendPacket(Player player, Object packet)
	{
	    try{
	        Object handle = player.getClass().getMethod("getHandle").invoke(player);
	        Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
	        playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	}

	public Class<?> getNMSClass(String name)
	{
	    try{
	        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
	    }catch (ClassNotFoundException e){
	        e.printStackTrace();
	    }
	    return null;
	}
}
