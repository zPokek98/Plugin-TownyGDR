/**
 * 
 */
package it.TownyGDR.Towny.City.Event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.Task.Posiction;

/*********************************************************************
 * @author: Elsalamander
 * @data: 7 apr 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * 
 *********************************************************************/
public class EventEnterInCity extends Event implements CityEvent, Cancellable{

private static final HandlerList handlers = new HandlerList();
	
	//Varibili oggetto
	private PlayerData playerData;
	private City city;
	private Posiction posizione;
	
	private boolean cancelled;
	
	
	public EventEnterInCity(PlayerData playerData, City fazione, Posiction posizione) {
		this.playerData = playerData;
		this.city = fazione;
		this.posizione = posizione;
	}
	
	
	/**
	 * @return the playerData
	 */
	public PlayerData getPlayerData() {
		return playerData;
	}


	/**
	 * @return the fazione
	 */
	public City getCity() {
		return city;
	}


	/**
	 * @return the posizione
	 */
	public Posiction getPosizione() {
		return posizione;
	}


	@Override
	public HandlerList getHandlers()
    {
		return handlers;
	}
    
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}


}
