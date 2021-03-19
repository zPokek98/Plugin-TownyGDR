/**
 * 
 */
package it.TownyGDR.Event;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import it.TownyGDR.PlayerData.PlayerData;

/*********************************************************************
 * @author: Elsalamander
 * @data: 3 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class EventPlayerManager implements Listener{

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerData.getPlayerData(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		try{
			(PlayerData.getPlayerData(event.getPlayer())).save();
		}catch(IOException e){
			Bukkit.getConsoleSender().sendMessage("Impossibile salvare i dati di: " + event.getPlayer().getName());
		}
	}
}
