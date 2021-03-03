package it.TownyGDR.Towny.Nazioni;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

import it.TownyGDR.Tag.Taggable;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Questa classe descrive la nazione, la nazione è un raggruppamento di 
 * 2 o più città, la nazione ha dei "sovrani/politici" che hanno poteri
 * rigurdanti la nazione i quali:
 * - Invitare un'altra città nella nazione.
 * - Eleggere un politico(Sovrano o diplomatico)
 * - Rimuovore la carica a un player
 * - Dichiarazioni/Aristizio/Resa di guerra tramite votazione
 * - Imporre un centro "nazione" necessario dalla fondazione!
 *   (quest'ultimo non può cambiare durante la guerra!)
 * - Rimuovere una città dalla nazione.
 * 
 * 
 * In supporto a questa classe ci sono almeno le seguenti classi:
 * - Classe astratta Politico(Generalizza il ruolo di politico in (Sovrano / diplomatico)
 * 		- Classe Sovrano
 * 		- Classe Diplomatico
 * 
 * - Classe Votazione(Tutte le decisioni di rilievo per la nazioni devono avere un
 *   grado di maggioranza fra i sovrani!)
 *   
 * - Classe City che rappresenta una città nel suo complesso.
 * 
 * - Classe Diplomazia che descrive in che rapporto sono le nazioni fra di
 *   loro, di defult è neutro.
 * 
 ********************************************************************/
public class Nazione implements Salva<FileConfiguration>, Taggable{

	@Override
	public void save(FileConfiguration database) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(FileConfiguration database) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasTag(String tag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> getTagList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueFromTag(String str) {
		// TODO Auto-generated method stub
		return null;
	}


}
