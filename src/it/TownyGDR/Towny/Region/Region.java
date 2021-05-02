/**
 * 
 */
package it.TownyGDR.Towny.Region;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import it.CustomConfig.CustomConfig;
import it.TownyGDR.TownyGDR;
import it.TownyGDR.Towny.Zone.ElementoArea;
import it.TownyGDR.Util.Util;
import it.TownyGDR.Util.Exception.Zona.ExceptionSectorInvalidStringLoad;
import it.TownyGDR.Util.Exception.Zona.ExceptionZonaImpossibleToLoad;
import it.TownyGDR.Util.Exception.Zona.ExceptionZonaNameNotAvaiable;
import it.TownyGDR.Util.Save.Salva;

/*********************************************************************
 * @author: Elsalamander
 * @data: 1 mag 2021
 * @version: v1.0
 * 
 *
 * @text
 * Descrizione:
 * E uguale alle zone, ma senza la dipendenza dal tipo di destinazione.
 * 
 *********************************************************************/
public class Region implements Salva<CustomConfig>{
	
	//ArrayList di cache per tenere a memoria tutte le zone
	private static ArrayList<Region> ListRegion = new ArrayList<Region>();
	
	//Variabili oggetto
	private int id; //id Region
	private String name;
	
	private Map<SectorRegion, ArrayList<ElementoArea>> area; //Per usufruire dei settori al messimo e ridurre il più
	//possibile le complessità uso un albero binario di ricerca => SectorRegion implementa Comparable
	//l'elemento d'area deve essere inserito secondo un criterio
	
	
	/**
	 * Costruttore di una Region con la tipologia assegnata e nome.
	 * @param type
	 * @throws ExceptionRegionNameNotAvaiable 
	 */
	public Region(String nome) throws ExceptionZonaNameNotAvaiable {
		if(!Region.checkNameIsFree(nome)) throw new ExceptionZonaNameNotAvaiable("Nome già usato!!!");
		this.name = nome;
		this.id   = Region.getMaxID() + 1;
		this.area = new TreeMap<SectorRegion, ArrayList<ElementoArea>>();
		
		//Aggiungi alla cache
		ListRegion.add(this);
	}
	
	
	/**
	 * Controlla in tutte le zone esistenti se una ha questo nome!
	 * @param nome
	 * @return
	 */
	private static boolean checkNameIsFree(String nome) {
		//Dato che tutte le zone sono in RAM la cerco solo la
		for(Region zon : ListRegion) {
			if(zon.name.equals(nome)) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Ritorna l'id massimo presente fra tutte le zone
	 * @return
	 */
	private static int getMaxID() {
		int id = 0;
		String[] list = Util.getListNameFile("Zone"); //Lista dei nomi dei file dentro la cartella "Zone"
		for(String str : list) {
			str = str.substring(0,str.length() - 4); //Cancella ".yml" alla fine
			CustomConfig customConfig = new CustomConfig("Zone" + File.separatorChar + str , TownyGDR.getInstance());
			FileConfiguration config = customConfig.getConfig();
			int tmp = config.getInt("ID"); //Prendi l'ID nel file letto
			if(tmp > id) {
				id = tmp;
			}
		}
		return id;
	}
	
	/**
	 * Aggiungi l'elemento d'area alla Region, serve quando si setterà la Region
	 * @param elementoArea
	 */
	public boolean addElementoArea(ElementoArea elementoArea) {
		//Controlla se appartiene già alla Region prima di aggungerlo
		if(this.contain(elementoArea)) {
			return false;
		}
		
		//prendi il settore per quell'area da aggiungere
		SectorRegion sec = SectorRegion.getSectorByLocation(elementoArea.getX(), elementoArea.getZ());
		
		//Caso particolare per il caso che il settore è stato generato per questa area
		if(sec.getZone().size() == 0) {
			sec.addRegion(this);
		}else{
			//contiene già questa Region?
			ArrayList<Region> tmp = sec.getZone();
			boolean check = false;
			for(Region z : tmp) {
				if(z.getID() == this.id) {
					check = true;
					break;
				}
			}
			if(!check) {
				sec.addRegion(this);
			}
		}
		
		//la Region ha questo settore?
		ArrayList<ElementoArea> sezione = this.area.get(sec);
		
		//Controlla se è Null, se lo è significa che la Region non aveva questo settore, crea e alloca
		if(sezione == null) {
			sezione = new ArrayList<ElementoArea>();
			sezione.add(elementoArea);
			this.area.put(sec, sezione);
			return true;
		}else{
			//Controlla se l'elemento d'area c'è già
			if(!sezione.contains(elementoArea)) {
				return sezione.add(elementoArea);
			}
			return false;
		}	
	}
	
	/**
	 * Ritorna il nome della Region
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Ritorna l'id della Region
	 * @return
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Controlla se l'elemento d'area dato è dentro la Region
	 * @param ele
	 * @return
	 */
	public boolean contain(ElementoArea ele) {
		//Per tutti i settori dell'area:
		for(SectorRegion sec : this.area.keySet()) {
			//Prendi gli elemento d'area all'interno al settore della Region
			ArrayList<ElementoArea> list = this.area.get(sec);
			for(ElementoArea el : list) {
				//confronta i songoli elementi d'area
				if(el.getX() == ele.getX() && el.getZ() == ele.getZ()) {
					//trovato, finisci.
					return true;
				}
			}
			//return list.contains(ele);
		}
		return false;
	}
	
	/**
	 * Rimuovi l'elemento d'area
	 * @param elementoArea
	 * @return
	 */
	public boolean removeElementoArea(ElementoArea elementoArea) {
		//prendi il settore per quell'area dato
		SectorRegion sec = SectorRegion.getSectorByLocation(elementoArea.getX(), elementoArea.getZ());
		
		//la Region ha questo settore?
		ArrayList<ElementoArea> sezione = this.area.get(sec);
		
		//Controlla se è Null, non posso rimuover quello che non c'è, dato che la Region non può non essere
		//contenuta da questo settore
		if(sezione == null) {
			//non posso imuovere quello che non c'è
			return false;
		}else{
			//Controlla se l'elemento d'area c'è dentro la Region
			if(sezione.contains(elementoArea)) {
				//l'elemento d'area è dentro.
				//lo rimuovo
				boolean tmp = sezione.remove(elementoArea);
				
				//il settore è vuoto dopo aver rimosso l'elemto d'area?
				if(sezione.size() == 0) {
					//si elimina il settore dalla Region
					this.area.remove(sec);
					sec.removeRegion(this);
				}
				return tmp;
			}
			return false;
		}	
	}
	
	/**
	 * Ritorna la lista dei settori in cui risiede la Region
	 * @return
	 */
	public SectorRegion[] getSectorRegion(){
		return this.area.keySet().toArray(new SectorRegion[this.area.keySet().size()]);
	}
	
	
	@Override
	public void save(CustomConfig database) throws IOException {
		CustomConfig customConfig;
		if(database == null) {
			customConfig = new CustomConfig("Zone" + File.separatorChar + this.name + "(" + this.id + ")", TownyGDR.getInstance());
		}else{
			customConfig = database;
		}
		FileConfiguration config = customConfig.getConfig();
		
		config.set("ID", this.id);
		config.set("Nome", this.name);		
		
		//Salva tutti gli elementi d'area
		if(config.getConfigurationSection("Area") == null) {
			config.set("Area.tmp", "tmp");
		}
		ConfigurationSection section = config.getConfigurationSection("Area");
		for(SectorRegion tmp : this.area.keySet()) {
			ElementoArea.save(section, tmp, this.area.get(tmp));
		}
		//elimina il tmp
		config.set("Area.tmp", null);
		
		customConfig.save();
	}

	@Override
	public void load(CustomConfig database) throws IOException, ExceptionZonaImpossibleToLoad {
		CustomConfig customConfig;
		if(database == null) {
			customConfig = new CustomConfig("Regioni" + File.separatorChar + this.name + "(" + this.id + ")", TownyGDR.getInstance());
		}else{
			customConfig = database;
		}
		FileConfiguration config = customConfig.getConfig();
		
		this.id    = config.getInt("ID");
		this.name  = config.getString("Nome");
		
		if(config.contains("Area")) {
			String[] list = config.getConfigurationSection("Area").getKeys(false).stream().toArray(String[] :: new);
			for(String str : list) {
				SectorRegion sec = null;
				try{
					sec = SectorRegion.valueOf(str.replaceFirst("Settore", ""));
				}catch(ExceptionSectorInvalidStringLoad e){
					//Impossibile leggere il valore
					throw new ExceptionZonaImpossibleToLoad("Impossibile caricare la Region data");
				}
				if(sec != null) {
					ArrayList<ElementoArea> ele = ElementoArea.loadData(config.getConfigurationSection("Area." + str));
					this.area.put(sec, ele);
					sec.addRegion(this);					
				}
			}
		}
	}


	/**
	 * Ritorna le arree della Region
	 * @return
	 */
	public Map<SectorRegion, ArrayList<ElementoArea>> getArea() {
		return this.area;
	}
	
	/**
	 * Ritorna la Region data una posizione se esiste
	 * @param loc
	 * @return
	 */
	public static Region getRegionByLocation(Location loc) {
		int x = loc.getChunk().getX();
		int z = loc.getChunk().getZ();
		return getRegionByLocation(x, z);
	}
	
	/**
	 * Ritorna la Region data una posizione se esiste
	 * @param loc
	 * @return
	 */
	public static Region getRegionByLocation(int x, int z) {
		SectorRegion sec = SectorRegion.getSectorByLocation(x, z); //Prendi il settore
		return sec.getZonaByArea(x, z);
	}

	/**
	 * Carica tutte le zone per l'avvio del server
	 * @throws IOException 
	 * @throws ExceptionRegionImpossibleToLoad 
	 */
	public static void initRegion() throws IOException, ExceptionZonaImpossibleToLoad {
		String[] files = Util.getListNameFile("Regioni");
		for(String str : files) {
			//rimuovi l'estenzione
			str = str.substring(0,str.length() - 4); //togli ".yml"
			CustomConfig customConfig = new CustomConfig("Regioni" + File.separatorChar +str, TownyGDR.getInstance());
			FileConfiguration config = customConfig.getConfig();
			
			//prendi il nome della Region e il tipo
			String nome = config.getString("Nome");
			
			Region tmp = null;
			try {
				tmp = new Region(nome);
				
				//prova a caricare la Region.
				tmp.load(customConfig);
			} catch (ExceptionZonaNameNotAvaiable e) {
				//Errore
				throw new ExceptionZonaImpossibleToLoad("Impossibile caricare la Region di file: " + str);
			}
		}
	}
	
	/**
	 * Ritorna la Region in base al suo id
	 * @param id
	 * @return
	 */
	public static Region getByID(int id) {
		for(Region zon : ListRegion) {
			if(zon.id == id) {
				return zon;
			}
		}
		return null;
	}
	
	/**
	 * Ritorna la Region in base al suo nome
	 * @param name
	 * @return
	 */
	public static Region getRegion(String name) {
		for(Region zon : ListRegion) {
			if(zon.name.equals(name)) {
				return zon;
			}
		}
		return null;
	}


	/**
	 * Salva tutte le zone, per la chiususra del server
	 */
	public static void saveAll() {
		for(Region zon : ListRegion) {
			try{
				zon.save(null);
			}catch (IOException e){
				//Errore
			}
		}
	}
	
}
