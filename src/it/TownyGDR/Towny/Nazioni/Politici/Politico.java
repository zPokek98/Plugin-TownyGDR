package it.TownyGDR.Towny.Nazioni.Politici;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe astratta per descrivere le subClass dei vari titoli per
 * i politici dell nazione.
 * 
 * Qua ci saranno tutte le funzioni minime e necessarie per i vari
 * tipi di sovrani.
 * 
 * Più le funzioni statiche che sono in comune fra loro.
 * 
 * Si usano gli uuid per comodità e anche per il caso che il player è
 * offline.
 ********************************************************************/

public abstract class Politico implements Salva<ConfigurationSection>{
	
	public Politico(UUID uuid) {
		
	}

}
