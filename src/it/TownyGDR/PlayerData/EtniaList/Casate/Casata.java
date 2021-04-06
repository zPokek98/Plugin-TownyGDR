/**
 * 
 */
package it.TownyGDR.PlayerData.EtniaList.Casate;


import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.PlayerData.Etnia;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.ArdeseCasate;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.Brial;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.Maedon;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.Nekromount;
import it.TownyGDR.PlayerData.EtniaList.Casate.Ardese.Tunnor;
import it.TownyGDR.PlayerData.EtniaList.Casate.Beriana.BerianaCasata;
import it.TownyGDR.PlayerData.EtniaList.Casate.Beriana.Gwael;
import it.TownyGDR.PlayerData.EtniaList.Casate.Beriana.Hemilock;
import it.TownyGDR.PlayerData.EtniaList.Casate.Beriana.Mady;
import it.TownyGDR.PlayerData.EtniaList.Casate.Beriana.Rhoben;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Dandu;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.DragonianaCasata;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Dragoy;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Drengot;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Hol;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Petrix;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Renix;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Roy;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Trevor;
import it.TownyGDR.PlayerData.EtniaList.Casate.Dragoniana.Zamputor;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.Darnis;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.Egeril;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.Fen;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.Forven;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.Leowell;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.NavianaCasata;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.River;
import it.TownyGDR.PlayerData.EtniaList.Casate.Naviana.Singer;
import it.TownyGDR.PlayerData.EtniaList.Casate.Selvaggia.SelvaggiaCasata;
import it.TownyGDR.PlayerData.EtniaList.Casate.Shariana.Iril;
import it.TownyGDR.PlayerData.EtniaList.Casate.Shariana.Malel;
import it.TownyGDR.PlayerData.EtniaList.Casate.Shariana.Marine;
import it.TownyGDR.PlayerData.EtniaList.Casate.Shariana.SharianaCasata;
import it.TownyGDR.PlayerData.EtniaList.Casate.Shariana.Tanak;

/*********************************************************************
 * @author: Elsalamander
 * @data: 10 mar 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Descrizione generale delle casate
 * 
 *********************************************************************/
public abstract class Casata{

	//Variabili per tutte le etnie
	protected String name;
	protected String desc;
	
	
	public Casata(String nome) {
		this.name = nome;
		this.desc = null;
	}
	
	/**
	 * Nome della etnia
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Ritorna la descrizione della etnia
	 * @return
	 */
	public String getDescription() {
		return this.desc;
	}
	
	/**
	 * Ritorna un oggetto Etnia tramite il nome
	 * @param str
	 * @return
	 */
	public static Casata getByName(String str) {
		for(String nome : Etnia.getMapEtnia().keySet()) {
			for(CasataType ty : Etnia.getMapEtnia().get(nome)) {
				if(ty.toString().equalsIgnoreCase(str)) {
					
					switch(nome) {
						case "Ardese":{
							switch((ArdeseCasate)ty) {
								case Brial:			return new Brial();
								case Maedon:		return new Maedon();
								case Nekromount:	return new Nekromount();
								case Tunnor:		return new Tunnor();
								default:			return null;
							}
						}
						case "Beriana":{
							switch((BerianaCasata)ty) {
								case Gwael:			return new Gwael();
								case Hemilock:		return new Hemilock();
								case Mady:			return new Mady();
								case Rhoben:		return new Rhoben();
								default:			return null;
							}
						}
						case "Dragoniana":{
							switch((DragonianaCasata)ty) {
								case Dandu:			return new Dandu();
								case Dragoy:		return new Dragoy();
								case Drengot:		return new Drengot();
								case Hol:			return new Hol();
								case Petrix:		return new Petrix();
								case Renix:			return new Renix();
								case Roy:			return new Roy();
								case Trevor:		return new Trevor();
								case Zamputor:		return new Zamputor();
								default:			return null;
							}
						}
						case "Naviana":{
							switch((NavianaCasata)ty) {
								case Darnis:		return new Darnis();
								case Egeril:		return new Egeril();
								case Fen:			return new Fen();
								case Forven:		return new Forven();
								case Leowell:		return new Leowell();
								case River:			return new River();
								case Singer:		return new Singer();
								default:			return null;
							}
						}
						case "Selvaggia":{
							switch((SelvaggiaCasata)ty) {
								default:			return null;
							}
						}
						case "Shariana":{
							switch((SharianaCasata)ty) {
								case Iril:			return new Iril();
								case Malel:			return new Malel();
								case Marine:		return new Marine();
								case Tanak:			return new Tanak();
								default:			return null;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public void save(ConfigurationSection config){
		config.set("Casata", this.name);
	}

	public static Casata load(ConfigurationSection config){
		String str = config.getString("Casata", null);
		if(str != null) {
			return Casata.getByName(str);
		}
		return null;
	}
}
