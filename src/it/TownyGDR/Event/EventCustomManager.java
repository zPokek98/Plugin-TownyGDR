/**
 * 
 */
package it.TownyGDR.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import it.TownyGDR.Towny.City.Event.EventEnterInCity;
import it.TownyGDR.Towny.City.Event.EventExitInCity;
import it.TownyGDR.Util.Title.Title;


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
public class EventCustomManager implements Listener{
	@EventHandler
	public void onEventEnterInCity(EventEnterInCity e) {
		Title tit = new Title();
		tit.send(e.getPlayerData().getPlayer(), e.getCity().getName(), "", 0, 1, 01);
	}
	
	@EventHandler
	public void onEventExitInCity(EventExitInCity e) {
		Title tit = new Title();
		tit.send(e.getPlayerData().getPlayer(), "WildNess", "", 0, 1, 0);
	}
}
