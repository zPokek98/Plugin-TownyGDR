package it.TownyGDR.Event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import it.TownyGDR.PlayerData.PlayerData;

public class EventInventoryManager implements Listener{

	@EventHandler
	public void onBrewEvent(BrewEvent event)
	{
		
	}
	
	@EventHandler
	public void onCraftItemEvent(CraftItemEvent event)
	{
		
	}
	
	@EventHandler
	public void onFurnaceBurnEvent(FurnaceBurnEvent event)
	{
		
	}
	
	@EventHandler
	public void onFurnaceSmeltEvent(FurnaceSmeltEvent event)
	{
		
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event){
		//Nome gui
		String title = event.getView().getTitle();
		if(title.equalsIgnoreCase("Selezione Etnia")) {	
			Player p = (Player) event.getWhoClicked();
			PlayerData pd = PlayerData.getPlayerData(p);
			
			pd.interactSelectionGui(event, 0, null);
			
		}else if(title.equalsIgnoreCase("Selezione Casata")) {
			Player p = (Player) event.getWhoClicked();
			PlayerData pd = PlayerData.getPlayerData(p);
			
			pd.interactSelectionGui(event, 1, pd.getEtnia());
		}
		
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event)
	{
		
	}
	
	@EventHandler
	public void onInventoryEvent(InventoryEvent event)
	{
		
	}
	
	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent event)
	{
		
	}
}
