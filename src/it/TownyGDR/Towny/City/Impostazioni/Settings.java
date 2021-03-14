/**
 * 
 */
package it.TownyGDR.Towny.City.Impostazioni;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe astratta che descrive in modo generale le impostazioni
 * elementari per la città.
 * 
 *********************************************************************/
public abstract class Settings implements Salva<ConfigurationSection>{

	/**
	 * Nome dell'impostazione
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * Descrizione divisa in righe per essere impostata come lore
	 * @return
	 */
	public abstract ArrayList<String> getDescription();
	
	/**
	 * Imposta il valore standard
	 */
	public abstract void setDefault();
	
	/**
	 * Item di rappresentazione per la GUI
	 * @return
	 */
	public abstract ItemStack getItemShow();

}
