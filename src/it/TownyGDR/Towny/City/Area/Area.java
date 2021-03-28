/**
 * 
 */
package it.TownyGDR.Towny.City.Area;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import it.TownyGDR.PlayerData.PlayerData;
import it.TownyGDR.Towny.City.City;
import it.TownyGDR.Towny.City.Membri.Membro;
import it.TownyGDR.Towny.City.Membri.MembroType;
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
	
	private ArrayList<Lotto> LottiCity;
	private ArrayList<LottoVendita> daVendere;
	
	

	//*********************************************************************** Costruttori
	/**
	 * Costruttore Area Vuota
	 */
	public Area(Zona zona) {
		this.zona = zona;
		this.area = new ArrayList<ElementoArea>();
		this.LottiCity = new ArrayList<Lotto>();
		this.daVendere = new ArrayList<LottoVendita>();
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
		if(this.checkAdiacenza(ele) || this.area.size() == 0) {
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
				Bukkit.getConsoleSender().sendMessage("passo 2");
				if((city.getMembri().size() <= this.getSize() * Area.ChunkSuMembri) || this.getSize() == 0) {
					//ha abbastanza membri
					Bukkit.getConsoleSender().sendMessage("passo 3");
					//Togli i soldi
					pd.withdrawMoney(Area.CostoSoldi);
					
					//Aggiungi l'area
					this.area.add(ele);
					return true;
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
			list.add(new ElementoArea(area.getX()     , area.getZ() + 1));
			list.add(new ElementoArea(area.getX()     , area.getZ() - 1));
			list.add(new ElementoArea(area.getX() + 1 , area.getZ()    ));
			list.add(new ElementoArea(area.getX() - 1 , area.getZ()    ));
			
			for(ElementoArea ele : list) {
				for(ElementoArea le : this.area) {
					if(ele.getX() == le.getX() && ele.getZ() == le.getZ()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void save(ConfigurationSection config) {
		//Salva i lotti
		for(Lotto lot : this.LottiCity) {
			lot.save(config);
		}
		
		LottoVendita.save(config, this.daVendere);
		
		//Salva id zona
		config.set("ZonaId", this.zona.getID());
		
		//Salva l'area
		if(this.area.size() == 0) {
			return;
		}
		String tmp = "";
		for(ElementoArea ele : this.area) {
			tmp += ele.getX() + "&" + ele.getZ() + ";";
		}
		tmp =  tmp.substring(0, tmp.length() - 1);
		config.set("Forma", tmp);
		
		
	}

	@Override
	public void load(ConfigurationSection config) {
		//Carica id zona
		this.zona = Zona.getByID(config.getInt("ZonaId"));
		
		//Carica l'area
		if(config.contains("Forma")) {
			Scanner scan = new Scanner(config.getString("Forma"));
			scan.useDelimiter(";");
			while(scan.hasNext()) {
				String coppia = scan.next();
				Scanner pt = new Scanner(coppia);
				pt.useDelimiter("&");
				int x = 0;
				int z = 0;
				try {
					x = pt.nextInt();
					z = pt.nextInt();
				}catch(InputMismatchException e) {
					//Error
				}
				pt.close();
				ElementoArea ar = new ElementoArea(x ,z);
				this.area.add(ar);
			}
			scan.close();
		}
		
		//Carica i lotti, non serve sono caricati quando vengono caricati i membri
		
		//carica i lotti da vendere
		this.daVendere = LottoVendita.load(config);
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

	/**
	 * @param lotto
	 */
	public void addLotto(Lotto lotto) {
		this.LottiCity.add(lotto);
	}

	/**
	 * @return
	 */
	public ArrayList<Lotto> getLotti() {
		return this.LottiCity;
	}
	
	/**
	 * Crea un lotto pre la vendita e aggiungi alla lista da vendere
	 * @param ele
	 * @param prezzo
	 * @param mem
	 * @return
	 */
	public LottoVendita createLottoVendita(ElementoArea ele, double prezzo, Membro mem) {
		if(mem.getType().contains(MembroType.Sindaco)) {
			LottoVendita lv = new LottoVendita(ele, prezzo, mem);
			this.daVendere.add(lv);
			return lv;
		}
		return null;
	}

	/**
	 * rimuovi il lotto in vendita
	 * @param lv
	 */
	public void removeLottoVendita(LottoVendita lv) {
		this.daVendere.remove(lv);
	}
	
	/**
	 * Ritorna il lotto in vendita data la sua posizione
	 * @param ele
	 * @return
	 */
	public LottoVendita getByElementoArea(ElementoArea ele) {
		for(LottoVendita lv : this.daVendere) {
			if(lv.getEle().equals(ele)) {
				return lv;
			}
		}
		return null;
	}
	
	
	
}
