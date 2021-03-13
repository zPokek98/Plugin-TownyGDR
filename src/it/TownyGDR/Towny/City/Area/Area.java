/**
 * 
 */
package it.TownyGDR.Towny.City.Area;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Towny.Zone.Zona;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 27 feb 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * Classe che descrive l'area della città.
 * L'area è la zona claimata dalla città dal sindaco e un insieme di
 * lotti che sono le aree elementari della città.
 * Ogni volta che il sindaco vuole claimare la zona da claimare deve essere
 * adiacente all'area gia presente se c'è.
 * 
 *********************************************************************/
public class Area implements Salva<ConfigurationSection>{
	
	private static final int CostoSoldi = 100;
	private static final int ChunkSuMembri = 5;
	
	//Variabili oggetto
	private Zona zona;	//Zona in cui si costrirà la città
	private ArrayList<ElementoArea> area; //Elementi area claimati dalla città
	
	

	//*********************************************************************** Costruttori
	/**
	 * Costruttore Area Vuota
	 */
	public Area(Zona zona) {
		this.zona = zona;
		this.area = new ArrayList<ElementoArea>();
	}
	
	//*********************************************************************** Funzioni Oggetto
	/**
	 * Ritorna la dimensione dell'area in numero di Chunk
	 * @return
	 */
	public int getSize() {
		return this.area.size();
	}
	
	/**
	 * Funzione per aggiungere l'elemento d'area alla città
	 * @param loc
	 * @return
	 */
	public boolean claim(Location loc, City city, PlayerData pd) {
		Chunk chunk = loc.getChunk();
		int x = chunk.getX();
		int z = chunk.getZ();
		ElementoArea ele = new ElementoArea(x, z);
		if(this.checkAdiacenza(ele)) {
			//Claimabile
			//Ha i requisiti per claimarlo
			
			//E' un sindaco?
			boolean check = false;
			for(Membro mem : city.getSindaco()) {
				if(mem.getUUID().equals(pd.getUUID())) {
					check = true;
					break;
				}
			}
			if(!check) return false;
			
			//ha i soldi?
			if(pd.getBalance() >= Area.CostoSoldi) {
				//Ha i soldi
				//Ha un minimo di player?
				if(city.getMembri().size() * Area.ChunkSuMembri < this.getSize()) {
					//ha abbastanza membri
					
					//Togli i soldi
					pd.withdrawMoney(Area.CostoSoldi);
					
					//Aggiungi l'area
					this.area.add(ele);
				}
			}
		}
		return false;
	}
	

	/**
	 * Controlla se una parte del lotto è contenuto nella città, se anche un suo pezzo
	 * è all'interno ritorna FALSE.
	 * @param lot
	 * @return
	 */
	public boolean containLotto(ElementoArea lot) {
		return this.area.contains(lot);
	}
	
	/**
	 * Controlla se la posizione "loc" è all'interno della città.
	 * 
	 * Funzione molto usata in futuro per controlla se un player è
	 * all'interno di una città o no!
	 * @param loc
	 * @return
	 */
	public boolean containLocation(Location loc) {
		Chunk chunk = loc.getChunk();
		int x = chunk.getX();
		int z = chunk.getZ();
		for(ElementoArea ele : this.area) {
			if(ele.getX() == x && ele.getZ() == z) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Controlla se il Lotto "lot" è adiacente all'area, se no ritorna false
	 * @param lot
	 * @return 
	 */
	public boolean checkAdiacenza(ElementoArea area) {
		if(!this.area.contains(area)) {
			//Controlla se è entro la zona
			if(!this.zona.contain(area)) return false;
			
			//Vari elementi aree affianco all'area
			ArrayList<ElementoArea> list = new ArrayList<ElementoArea>();
			list.add(new ElementoArea(area.getX() + 1 , area.getZ() + 1));
			list.add(new ElementoArea(area.getX() + 1 , area.getZ() - 1));
			list.add(new ElementoArea(area.getX() - 1 , area.getZ() + 1));
			list.add(new ElementoArea(area.getX() - 1 , area.getZ() - 1));
			
			for(ElementoArea ele : list) {
				if(this.area.contains(ele)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void save(ConfigurationSection database) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(ConfigurationSection database) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Ritorna la città dato un chunk
	 * @param chunk
	 * @return
	 */
	public static City getCityFromArea(Chunk chunk) {
		Zona zon = Zona.getZonaByLocation(chunk.getX() , chunk.getZ());
		if(zon != null) {
			if(zon.getLuogo() instanceof City) {
				return (City) zon.getLuogo();
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public ArrayList<ElementoArea> getClaimato() {
		return this.area;
	}
	
	
	
	
	
	
	
	
}
