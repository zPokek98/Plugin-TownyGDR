package it.TownyGDR.Event;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.SignChangeEvent;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Area.Area;
import it.TownyGDR.Towny.City.Area.Lotto;
import it.TownyGDR.Towny.City.Area.LottoVendita;
import it.TownyGDR.Towny.Task.Posiction;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Towny.Zone.Sector;

public class EventBlockManager implements Listener{
	
	

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakEvent(BlockBreakEvent event){
		//Controlla se è un cartello
		//Ottieni lo stato del blocco
		Block clickBlock = event.getBlock();
		Location l = clickBlock.getLocation();
		
		if(clickBlock != null && clickBlock.getState() instanceof Sign) {
			Sign sign = (Sign) clickBlock.getState();
			if(sign.getLines()[0].equalsIgnoreCase("Lotto in vendita")) {
				
				event.setDropItems(false); //elimina il drop
				
				City city = Area.getCityFromArea(l.getChunk());
				ArrayList<LottoVendita> list = city.getArea().getDaVendere();
				for(LottoVendita ven : list) {
					if(ven.getCartello().getLocation().equals(l)) {
						event.setCancelled(true); //elimina evento
						return;
					}
				}
			}
		}
		
		PlayerData pd = PlayerData.getPlayerData(event.getPlayer());
		if(pd.getCity() != null) {
			Posiction pos = Posiction.getPosPlayerData(pd);
			City city = pd.getCity();
			
			ArrayList<Lotto> lotti = city.getArea().getLotti();
			boolean check = false;
			for(Lotto lot : lotti) {
				if(lot.getListAree().contains(new ElementoArea(l.getChunk()))){
						if(lot.getMembro().contains(city.getMembroByUUID(pd.getUUID()))){
						//ok
						//può costruire
						check = true;
					}else{
						//non deve costruire
						//...
						//è gia false check
					}
				}
			}
			if(!check && pos.getLuogo() != null) {
				event.setCancelled(true);
			}
				
		}else{
			//event.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onBlockBurnEvent(BlockBurnEvent event)
	{
		
	}
	
	@EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onBlockFadeEvent(BlockFadeEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onBlockFormEvent(BlockFormEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onBlockFromToEvent(BlockFromToEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onBlockGrowEvent(BlockGrowEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onBlockIgniteEvent(BlockIgniteEvent event)
	{
		
	}
	
	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent event)
	{
		
	}
	
	/*
	@EventHandler
	public void onBlockPistonEvent(BlockPistonEvent event)
	{
		
	}
	*/
	
	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event)
	{
		
	}
	
	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent event)
	{
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		
		Location loc = event.getBlock().getLocation();
		
		PlayerData pd = PlayerData.getPlayerData(event.getPlayer());
		if(pd.getCity() != null) {
			Posiction pos = Posiction.getPosPlayerData(pd);
			City city = pd.getCity();
			
			ArrayList<Lotto> lotti = city.getArea().getLotti();
			boolean check = false;
			for(Lotto lot : lotti) {
				if(lot.getListAree().contains(new ElementoArea(loc.getChunk()))){
						if(lot.getMembro().contains(city.getMembroByUUID(pd.getUUID()))){
						//ok
						//può costruire
						check = true;
					}else{
						//non deve costruire
						//...
						//è gia false check
					}
				}
			}
			if(!check && pos.getLuogo() != null) {
				event.setCancelled(true);
			}
				
		}else{
			//event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockRedstoneEvent(BlockRedstoneEvent event)
	{
		
	}
	
	@EventHandler
	public void onBlockSpreadEvent(BlockSpreadEvent event)
	{
		
	}
	
	@EventHandler
	public void onEntityBlockFormEvent(EntityBlockFormEvent event)
	{
		
	}
	
	@EventHandler
	public void onLeavesDecayEvent(LeavesDecayEvent event)
	{
		
	}
	
	@EventHandler
	public void onNotePlayEvent(NotePlayEvent event)
	{
		
	}
	
	@EventHandler
	public void onSignChangeEvent(SignChangeEvent event)
	{
		
	}
}
