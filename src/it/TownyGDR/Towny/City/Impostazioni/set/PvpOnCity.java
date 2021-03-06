/**
 * 
 */
package it.TownyGDR.Towny.City.Impostazioni.set;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Area.Area;
import it.TownyGDR.Towny.City.Impostazioni.Settings;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Abilitare il pvp fra i cittadini all'interno della città
 * questo non altera il pvp fra un cittadino e un player che non appartiene
 * alla città o nazione
 * 
 *********************************************************************/
public class PvpOnCity extends Settings{
	
	private static String nameSettings = "Pvp in città";
	private static ItemStack itemShow  = new ItemStack(Material.DIAMOND_SWORD);
	private static ArrayList<String> desc = new ArrayList<String>();
	static{
		desc.add("Abilitare o meno il pvp fra"); 	//0
		desc.add("i cittadini.");					//1
		desc.add("Gli utenti non appartenenti");	//2
		desc.add("posso picchiare e essere");		//3
		desc.add("picchiati indipendente dal");		//4
		desc.add("da questo valore:");				//5
		desc.add("Attuale: %val%");					//6
	}
	private static boolean standardVal = false;
	private boolean val;
	
	
	public PvpOnCity()
	{
		this.val = standardVal;
	}
	
	@Override
	public void save(ConfigurationSection config) {
		config.set("PvpOnCity", this.val);
	}

	@Override
	public void load(ConfigurationSection config) {
		this.val = config.getBoolean("PvpOnCity", standardVal);
	}

	@Override
	public String getName() {
		return nameSettings;
	}

	@Override
	public ArrayList<String> getDescription() {
		desc.get(6).replaceAll("%val%", this.val ? "SI" : "NO");
		return desc;
	}

	@Override
	public void setDefault() {
		this.val = standardVal;
	}

	@Override
	public ItemStack getItemShow() {
		return itemShow;
	}
	
	/**
	 * Annulla il pvp nella città
	 */
	public static void eventPvpDisable(EntityDamageByEntityEvent event) {
		Entity eAtt = event.getDamager();
		Entity eDiff = event.getEntity();
		
		if(eAtt instanceof Player && eDiff instanceof Player)
		{
			Player att = (Player)eAtt;
			Player diff = (Player)eDiff;
			City city = Area.getCityFromArea(att.getLocation().getChunk());
			
			if(city != null && ((PvpOnCity)(city.getImpostazioni().getSettings(nameSettings))).val)
			{
				if(city.hasMembro(att.getUniqueId()) && city.hasMembro(diff.getUniqueId()))
				{
					//azzera i danni
					event.setCancelled(true);
				}
			}
		}
	}

}
