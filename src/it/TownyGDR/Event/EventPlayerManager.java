/**
 * 
 */
package it.TownyGDR.Event;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.Area.LottoVendita;

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
			PlayerData pd = PlayerData.getPlayerData(event.getPlayer());
			pd.save();
			PlayerData.cacheRemove(pd);
			
			
		}catch(IOException e){
			Bukkit.getConsoleSender().sendMessage("Impossibile salvare i dati di: " + event.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
	{
		
	}
	
	@EventHandler
	public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerAnimationEvent(PlayerAnimationEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onPlayerBucketEvent(PlayerBucketEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onPlayerBucketFillEvent(PlayerBucketFillEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onPlayerChannelEvent(PlayerChannelEvent event)
	{
		
	}
	*/
	
	/*
	@EventHandler
	public void onPlayerChatEvent(PlayerChatEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerEggThrowEvent(PlayerEggThrowEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onPlayerEvent(PlayerEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		//Controlla se è un cartello
		
		//Ottieni lo stato del blocco
		Block clickBlock = event.getClickedBlock();
		
		if(clickBlock != null && clickBlock.getState() instanceof Sign) {
			
			Sign sign = (Sign) clickBlock.getState();
			
			//cliccato un cartello
			switch(event.getAction()) {
				case LEFT_CLICK_BLOCK:{
					
				}break;
				
				case RIGHT_CLICK_BLOCK:{
					//che cartello è?
					if(sign.getLines()[0].equalsIgnoreCase("Lotto in vendita")) {
						LottoVendita.clickSign(event, sign);
					}
					
				}break;
				
				default: break;
			}
		}
		
		
	}
	
	@EventHandler
	public void onPlayerItemBreakEvent(PlayerItemBreakEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerLoginEvent(PlayerLoginEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onPlayerPortalEvent(PlayerPortalEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onPlayerPreLoginEvent(PlayerPreLoginEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onPlayerRegisterChannelEvent(PlayerRegisterChannelEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerTeleportEvent(PlayerTeleportEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerUnregisterChannelEvent(PlayerUnregisterChannelEvent event)
	{
		
	}
	
	@EventHandler
	public void onPlayerVelocityEvent(PlayerVelocityEvent event)
	{
		
	}
}
